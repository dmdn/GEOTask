# GEO TASK (Android Junior)

## Project Description
The project is made as an educational one. The task text was taken from [forum startandroid.ru](http://forum.startandroid.ru/viewtopic.php?f=54&t=2458&hilit=готовимся+к+собеседованию&sid=319374037ae57c83be76a14a2d79ffd0)

## Technical Task
The application consists of 3 screens:
1. Splash (a picture with a non-uniform background on the whole screen)
2. Activate the input of the Start and End points contains:
   1. 2 Tabs Where From and Where. Inside the taba: The input field for searching when entering the geocoder determines the version and coordinates (up to 7 pieces). List of found variants (3 pieces are visible other scrolls). Clicking on an element selects it. The map shows the location of the selected option on the map
   2. The path search button. When you click the transition to the Result Form.
3. Form of result: a map that is scaled in such a way that it includes:
   * Location by geolocation
   * Path from the point From where To, point obtained by direct request (HTTP GET) to Google service Directions API (https://developers.google.com/maps/documentation/directions/).
We accept the answer from Directions API in the form of JSON. We are only interested in the coordinates of the path details are not important. We draw in the form of Polyline black.
After receiving the response from the service above the card write: Found or Unknown, depending on the answer Google.

### General requirements:
* Portrait and landscape
* Permits from 480x800 to 1080x1920
* Android 4.2
* It is desirable to design in the style of Android 4.x (Actionbar etc)

## Description of the executed app
The application was made according to technical task.

### Basic skills for the project creation
+ Work with JSON
+ Google Maps API
+ Google Maps Geocoding API
+ Google Maps Directions API
+ Android geolocation (GPS)
+ Material Design
  + Sliding Tabs
+ Working with RecyclerView
+ Work with Fragments
+ Additional Libraries were used:
  + [GSON](https://github.com/google/gson)
  + [Google Maps Android API utility library](https://github.com/googlemaps/android-maps-utils)

![screenshot_001](https://cloud.githubusercontent.com/assets/19373990/26530716/f2b89014-43e2-11e7-93f8-90b35250b1d4.jpg)
![screenshot_002](https://cloud.githubusercontent.com/assets/19373990/26530718/f2b97434-43e2-11e7-88d0-149ffb6f28c3.jpg)
![screenshot_003](https://cloud.githubusercontent.com/assets/19373990/26530720/f2bba434-43e2-11e7-9f46-977293548558.jpg)
![screenshot_004](https://cloud.githubusercontent.com/assets/19373990/26530719/f2bacb04-43e2-11e7-9aa6-4a8ac00a52dc.jpg)
![screenshot_005](https://cloud.githubusercontent.com/assets/19373990/26530721/f2bc26a2-43e2-11e7-9b94-2670c3c4c2d6.jpg)
![screenshot_006](https://cloud.githubusercontent.com/assets/19373990/26530717/f2b89b54-43e2-11e7-846d-d18e0e1217fc.jpg)
![screenshot_007](https://cloud.githubusercontent.com/assets/19373990/26530722/f2d49660-43e2-11e7-8e95-e031a642e60b.jpg)
![screenshot_2017-06-12-09-05-53](https://user-images.githubusercontent.com/19373990/27021242-bcb5de86-4f4f-11e7-96b6-d0aedec73ff6.png)
![screenshot_2017-06-12-09-11-56](https://user-images.githubusercontent.com/19373990/27021243-bcb64d26-4f4f-11e7-8a40-ef05ae84e229.png)

### Additional implementation in the app
+ The application displays the distance between 2 points
+ The application works on a real android device. To do this, Alert Dialogs were performed. Since on a real device GPS geolocation can work slowly and do not always show positions, two points with a line between them can first be displayed, and then the GPS position. If the position GPS is not defined at first, then the Marker on the Map is displayed with the coordinates (0.0000; 0.0000) and the text "Geolocation not defined". With the GPS is disabled, a marker with the text "GPS is disabled" is displayed. It may take time for the device to find a location, only after that the Marker will be moved to the point with the coordinates found.
+ The geocoding point search box can accept various text encodings. The result get in English.
+ After returning from the result page to the input / start point input form, the search results disappear (maps and values are cleared). This allows you to enter values again.
+ On the input tabs, when you rotate the screen, the results list and the marker on the map are saved.

[Download APP in APK](https://drive.google.com/file/d/0B_FuLrEepxSsc2JNS2hYMkpTbGs/view?usp=sharing)

[Screenshots GEO Task APP in PDF](https://drive.google.com/file/d/0B_FuLrEepxSscUNqU2QxUHlqblk/view?usp=sharing)
