# Controlling an mbed device with voice commands

Requires:
* Android phone running *at least* Android 5.0 Lollipop
* mbed device capable of BLE (I've used [Nordic NR51-Dk](https://developer.mbed.org/platforms/Nordic-nRF51-DK/)) or a [BLE component](https://developer.mbed.org/components/cat/bluetooth/) attached to non-BLE board

## Setting it up

### Device application

The device code can be found [here](https://developer.mbed.org/users/sarahmarshy/code/mbed-os-example-ble-voice). The application controls something on the board's DigitalOut `D2` pin now. Attach a [component](https://developer.mbed.org/components/) to this pin that can be controlled by writing to it digitally. I've chosen a siren light attached to a [Grove Relay](https://developer.mbed.org/components/Grove-Relay-module/). Compile the code and flash the resulting binary to your board.

#### How it works

The device is exposing a UART BLE service, where a BLE peer can send data over the RX characteristic of this service. Read more about UART/Serial Port Emulation over BLE in [Nordic's documentation](https://devzone.nordicsemi.com/documentation/nrf51/6.0.0/s110/html/a00066.html). 

The device can receive strings in this RX characteristic. When the word "panic" is received, the application sets the `DigitalOut` pin of `D2` on the mbed device to `HIGH`. If the word "chill" is received, the pin is set to `LOW`. 

#### Modifying the device code

To modify the behavior of the device when it receives a string command, look at the `check_command` [function](https://developer.mbed.org/users/sarahmarshy/code/mbed-os-example-ble-voice/annotate/46aad4b5185f/source/main.cpp#l47). Here, you can change the acceptable commands, and also what is actuated upon the receipt of these commands. You could modify [this line](https://developer.mbed.org/users/sarahmarshy/code/mbed-os-example-ble-voice/annotate/46aad4b5185f/source/main.cpp#l25) to `DigitalOut light(LED1);` if you'd like to control your board's LED with voice commands.

### Android application

The full Android Studio application can be cloned from [here](https://github.com/sarahmarshy/mbedVoice) if you'd like to play with the source code. 

To use the app as is, you'll need to allow installation of Non-Market applications. This might vary according to your Android version. For Android 7.1.1, visit `Settings > Security > Device Administration > Turn on "Unknown sources"`. 

Next, you can download the APK from: https://github.com/sarahmarshy/mbedVoice/blob/master/app/app-release.apk. Simply click the downloaded file to install it on your device.

#### How it works 

The application allows you to pair with a BLE device. Once paired, the app uses the [Android speech API](https://developer.android.com/reference/android/speech/package-summary.html) to recognize a spoken sentence. On completion, the app parses into a string the recognition result with the highest confidence value. This string is sent to the RX characteristic of the paired device. It will send the data in this format: `!your words here#`. If the device has not exposed any writeable characteristics, nothing will be sent. 

## Using the system

Assuming you've compiled and flashed the device code, open a serial connection with your mbed device using a serial device terminal at 9600 baud. This will allow you to view the voice commands sent to your device.

Launch the mbed voice application on your phone. 

Select the BLE connect icon.

![](/img/empty.png)

Select allow location access. [BLE feature of Android](https://developer.android.com/guide/topics/connectivity/bluetooth-le.html#user-permission) requires this.

Connect to `mbedVoiceUART`. This name is defined in the mbed application code.

![](/img/ble_device_list.png)

You will see a message if you have succesfully paired with the device. 

![](/img/connected.png)

To send voice commands, click the speech bubble in the middle of the screen. When prompted, allow recording audio. You should hear an audio cue when the phone is listening for a command, and one when it detects you've stopped speaking. When you've stopped speaking, look at your serial device terminal. You will see the words you've just spoken! 

![](/img/teraterm.png)













