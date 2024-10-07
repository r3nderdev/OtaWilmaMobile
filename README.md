# THIS PROJECT IS STILL VERY WORK IN PROGRESS

I have forked the old OtaWilma app project and I have the goal of finishing it. If that means fixing the old code or remaking the entire thing from scratch, I'm not sure.

Please also note that I have _never_ made an android app in my life, so don't expect this to be done quickly. A **very** optimistic ETA would be the end of the school year, but that will most likely not happen.

## Current status:
I'm still actively learning **Kotlin** and **Android Studio**. Right now I can make simple UI that has some basic functionality.

I've also managed to get the old project open in Android Studio and I have decided that I want to overhaul the UI to use Jetpack Compose instead of XML. I will probably use some quite a bit of the old logic once I understand the code better.

# Original README:

OtaWilmaMobile is an android interface for OtaWilma. 

This allows for things like AN USABLE INTERFACE and notifications.

The app is still heavily under development, so things such as usability, and combatability are not guaranteed. If a new release does not work you can try completely uninstalling the application and redownloading it.

## Features:

Compared to the normal Wilma app this app can:

- Cache your schedule so that you can see where you should be and when even without an internet connection or the oh so reliable Wilma-servers

Compared to the normal Wilma app this app cannot:

- Do most of the things Wilma can

## Things of concern:

### Storing of critical data

If you check the option to autologin in preferences, the app **WILL STORE YOUR WILMA LOGIN CREDENTIALS ENCRYPTED WITH AES256**.
This SHOULD be safe, but to be safe the user shall check the safe of the code in [EncryptedPreferenceStorage](/app/src/main/java/com/otawilma/mobileclient/EncryptedPreferenceStorage.kt) beforehand.

### Storing of noncritical data

The application will store schedules (for now) unencrypted for caching reasons.

## Short TOS

This app stores and handles data owned by Espoo and Visma. The developer takes no responsibility for the data you use this application to store. Download and install only at your own risk. The developer does not take any responsibility for the misuse or the malfunctioning of this app. 
