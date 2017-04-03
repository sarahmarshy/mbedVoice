package arm.marsh.sarah.mbedvoice;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sarmar01 on 3/29/2017.
 */

public class VoiceListener implements RecognitionListener{

    private BluetoothGattCharacteristic rxChar;
    private BluetoothGatt mGatt;
    private Context c;

    public VoiceListener(Context c){
        this.c = c;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("READY", "OK");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("SPEECH", " START");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        Toast.makeText(c, "Error recognizing voice command", Toast.LENGTH_LONG).show();
        Log.d("VoiceListener", "errno " + error);
    }

    public void setCharacteristic(BluetoothGattCharacteristic characteristic, BluetoothGatt gatt){
        rxChar = characteristic;
        mGatt = gatt;
        Log.d("LISTENER CHAR: ", Integer.toString(rxChar.PERMISSION_WRITE));
    }

    @Override
    public void onResults(Bundle results) {
        String str = new String();
        ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        str += "#";
        str += data.get(0);
        str += "!";
        Log.d("Voice recog", str);
        rxChar.setValue(str.getBytes());
        mGatt.writeCharacteristic(rxChar);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
