#StrangerLights
Contents:

*Android App
*Python Script
*Arduino Uno with relays


##Introduction

This is a project inspired by the show 'Stranger Things,' in which lightbulbs are activated by the nearby presence of certain characters. This project implements Android location tracking to accomplish proximity-triggered light toggling, by wirelessly transmitting the user's location in realtime to the connected lightswitches.

![Light turned on](https://github.com/Teetertater/StrangerLights/blob/master/Light_On.JPG | Width = 100)

##Overview

The primary component is the Android device. With 750ms updates, it publishes the user's lat/lon coordinates to PubNub. This location data is then received by a python script, which checks the Vincenty distance of the user's location against that of predefined lights. If the user is within a threshold boundary, the script sends information of the light and required state to an Arduino Uno connected over serial.
The Arduino is connected via USB to the device running the python script (my laptop in this case), and controls relays for each corresponding light directly.

Upon activation of the lights, the python script publishes their state to another channel over PubNub. This data is received by the Android app, which visually displays the state of the lights at the bottom.

##Build Instructions

##### Default

To build the app, download and compile the android component, or simply use the apk file I have provided (
\app\build\outputs\apk\apk-debug.apk). This will grant you the ability to control the lights at my house!
The python script must be running at the time at my end, however.

##### Custom

To configure your own lights:

Run the python script, installing corresponding dependencies as needed (struct, pyserial, geopy, pubnub). 
You can modify the predefined light coordinates at the top of the script to the location of your own. You will also need to update the serial port to the one corresponding to your Arduino connection. Lastly, upload the Arduino .ino file to the Arduino, and connect the activation pin of up to 3 relays for lights in order (0,1,2) to pins (2,3,4) respectively.

##Design Choices

Instead of checking the distance between the user and the lights in the python script, I could have implemented geofencing on the Android device; simply sending the activation instructions over PubNub. However, I wanted to test the capabilities of PubNub with a realtime stream of constantly-updating data (and it worked). The lights at the bottom take a few seconds to update to reflect the state that the user sets-- this is because the changes are only reflected when they are made in the python script, and published back to the android device. This way, the user is notified of the update only when it has gone through. 

##Future extensions

I would like to implement a setting to add and remove lights from the app, without hardcoding the string values of the coordinates. I made the python and arduino sides of the code with the ability to easily expand the amount of lights, by iterating through all the lights defined in the script. The Android side, however, would need to be adjusted to reflect the changes. For example, a new indicator at the bottom would need to be created or destroyed, corresponding to each light.
