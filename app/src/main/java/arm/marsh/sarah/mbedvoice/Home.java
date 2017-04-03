package arm.marsh.sarah.mbedvoice;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


public class Home extends AppCompatActivity  {

    final private int REQUEST_CODE_LOCATION = 1;
    final private int REQUEST_CODE_RECORD = 2;

    private ArrayAdapter<String> devices;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mGatt;
    private static final int REQUEST_ENABLE_BT = 1;
    private HashMap<String, BluetoothDevice> bleDevices;

    private static final long SCAN_PERIOD = 10000;
    private Handler mHandler;

    private BluetoothLeScanner mLEScanner;

    private boolean found_characteristic;

    //Listening vars
    private SpeechRecognizer sr;
    private VoiceListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        bleDevices = new HashMap<>();

        mHandler = new Handler();
        listener = new VoiceListener(this);
        Log.d("Create listener", "create");

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        found_characteristic = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    @Override
    protected void onDestroy() {
        if (mGatt != null) {
            mGatt.close();
        }
        mGatt = null;
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "coarse location permission granted");
                    showDevices();
                } else {
                    Toast.makeText(getApplicationContext(), "ACCESS LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            case REQUEST_CODE_RECORD: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "record audio permitted");
                    listenVoice();
                } else {
                    Toast.makeText(getApplicationContext(), "ACCESS LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private void scanLeDevice(final boolean enable) {
        mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLEScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);
            mLEScanner.startScan(mScanCallback);
        } else {
            mLEScanner.stopScan(mScanCallback);
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice btDevice = result.getDevice();
            String ident;
            if(btDevice.getName() != null){
                ident = btDevice.getName();
            }
            else {
                ident = btDevice.getAddress();
            }
            if(!bleDevices.containsKey(ident)) {
                bleDevices.put(ident, btDevice);
                devices.add(ident);
                devices.notifyDataSetChanged();
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.i("Scan Result", "Batch");
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            found_characteristic = false;
            mGatt = device.connectGatt(this, false, gattCallback);
            scanLeDevice(false);// will stop after first device detection
        }
        else{
            mGatt.disconnect();
            mGatt = null;
            connectToDevice(device);
        }
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            Log.i("onServicesDiscovered", services.toString());
            for(BluetoothGattService service : gatt.getServices()) {
                Log.d("Service uuid", service.getUuid().toString());
                for (BluetoothGattCharacteristic c : service.getCharacteristics()) {
                    Log.d("Char uuid", c.getUuid().toString());
                    Log.d("Write type", Integer.toString(c.getWriteType()));
                    if(c.getWriteType() == 1) {
                        found_characteristic = true;
                        Log.d("Char write: ", Integer.toString(c.PERMISSION_WRITE));
                        listener.setCharacteristic(c, mGatt);
                        return;
                    }

                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicWrite", characteristic.toString());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void showDevices(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            } else {
                scanLeDevice(true);
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Home.this);
                builderSingle.setTitle("Available Devices");

                devices = new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1);

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(devices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = devices.getItem(which);
                        BluetoothDevice dev = bleDevices.get(strName);
                        connectToDevice(dev);
                        Toast.makeText(getApplicationContext(), "Connected to " + strName, Toast.LENGTH_SHORT)
                                .show();
                        dialog.dismiss();
                    }
                });
                builderSingle.show();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scan:
                bleDevices = new HashMap<String, BluetoothDevice>();
                showDevices();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void listenVoice(){
        if (mGatt == null){
            Toast.makeText(this, "No device connected!", Toast.LENGTH_LONG).show();
            return;
        }
        if(!found_characteristic){
            Toast.makeText(this, "Cannot write to connected device.", Toast.LENGTH_LONG).show();
            return;
        }
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(listener);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
        sr.startListening(intent);
        Log.d("Listening", "OK");
    }

    public void listenVoiceBtnHandle(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_RECORD);
            } else {
                listenVoice();
            }
        }
    }
}
