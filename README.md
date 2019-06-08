# Gopool

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

Gopool is an open source, cloud-based, mobile-ready application for carpooling. Powered by JAVA and Firebase. Made for my final year project to showcase the technology stack. The carpooling mobile applciation provides a platform to car share. The user and vehicle registration details are stored via Firebase. Users can login and create/ send booking requests to other users on the app. Payments can be made via a sandbox payment system. The user interface designed in XML is friendly and can be adapted to user requirments

![alt text](https://raw.githubusercontent.com/DrExpresso/Gopool/master/screenshots.png?token=AG7UF5FJEAVOUMXNG4HEZ4C5AU6DA)

# Features!
  - Same gender carpooling
  - Payment intergration via Braintree
  - Book and search for ride
  - Edit profile activity
  - Leaderboards
  - Realtime Notifications
  - User reviews

# APK Download
The direct APK can be downloaded from here: [APK DOWNLOAD](https://github.com/DrExpresso/Gopool/blob/master/apk/app-debug.apk)


### Installation

This project is meant to be built using Android Studio. It can also be built from the `gradle` command line.

1 Check out the source code:

```sh
$ git clone https://github.com/DrExpresso/Gopool.git
```

2 Open Android Studio and choose Open an Existing Android Studio Project

3 Choose `final`.

4 Click the **Run** button.

### Configuration
Current configuration that is required to make the app work:
 - Google Maps API key (AndroidManifest.xml) - Must setup account and add to project https://cloud.google.com/maps-platform/

- Firebase (build.gradle) - Must create firebase project and bind the project name and files, guide can be found [here](https://firebase.google.com/docs/android/setup). **NOTE: Firebase messaging service also requried. Setup can be found [here](https://firebase.google.com/docs/cloud-messaging/android/client)

- Braintree (build.gradle) - Must configure application details guide can be found [here](https://developers.braintreepayments.com/start/hello-client/android/v2). **NOTE: To use the payment system, web hosting is required. This can be done using XAMP or heroku. Guide for heroku [here](https://github.com/braintree/braintree_php_example). Following same procedure and edit the put the deployed heroku app URL in place of the localhost.


License
----
```The MIT License (MIT)

Copyright (c) 2018 DrExpresso

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS ```
