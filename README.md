StrangerLights
Contents:

-Android App

-Python Script

-Arduino ino file


Introduction

This is a project inspired by the show 'Stranger Things,'in which lightbulbs are activated by the nearby presence of certain people. This project implements Android location tracking to accomplish just that, by sending the user's location in realtime to the connected lightswitches.

Setup

The primary component is the Android device. Every 0.75 seconds, it publishes the user's lat/lon coordinates to PubNub. This location data is then received by a python script. This script checks the Vincenty distance between the user's location and coordinates of predefined lights. If the user is within a threshold boundary, the script sends information of the light and state to a connected Arduino Uno over serial.
The last component is an Arduino Uno, with relays connected to each corresponding light.

Upon activation of the lights, the python script publishes their state to another channel over PubNub. This data is received by the Android app, which visually displays the state of the lights at the bottom.

Design Choices

Instead of checking the distance between the user and the lights in python script, I could've implemented geofencing on the Android device, simply sending the activation instructions over PubNub. However, I wanted to test the capabilities of PubNub with a realtime stream of constantly-updating data (and it worked). 