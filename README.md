__**HomeTime - Android**__
==================

This repository contains a simple sample app using the Tram Tracker API to be used as a coding assignment for REA mobile (Android) developer candidates.

__**Existing Functionality**__


The app is hard coded to show the next upcoming trams going *north* and *south* outside the REA office on Church St - when the `REFRESH` button is pressed.

* When the app loads, the table shows the empty state,  no timetable information has already been loaded
* When you tap 'Refresh', the app retrieves the upcoming trams from the API both both north and south and places the dates in the list
* There isn't any proper error handling, if an error occurs, we just log it and move on

__**Existing Android Code**__

This application is written in Java. This is not meant to be an example of how code should be written, but rather an opportunity to think about better ways of breaking down and structuring code in a simple context.

The `MainActivity` keeps the retrieved tram data in two `List` properties, `northTrams` and `southTrams`, which are initially `null`.

Button click event handlers are also implemented here, as well as Networking code implemented using Retrofit.

The `main_layout` defines two `ListView` to display the *north* and *south* trams, as well as the `REFRESH` and `CLEAR` buttons


__**Coding Task**__
-----------

The functionality is mostly complete (error handling has been largely ignored). The code does not follow best practices and, as features are added, isn't going to be maintainable. __We would like you to look at some ways of improving the code quality, to make it easier to maintain and easier to test.__

Feel free to implement in Kotlin, or use other techniques to handle async (i.e. Rx-Java), however this is not a requirement.

Show us your __UX__ chops, and make the user interface more elegant.

Don't feel like you have to fix every issue you see in the code.

With a better code structure in place, try adding a small piece of functionality. For example, instead of just showing the time for the next tram (eg. *9:23 am*), it could also show how far away that is from the current time (eg. *9:23 am (3 min)*)

---

__Tram Tracker API__

This app uses the same API as the Tram Tracker app, but it's not an officially public API so there is a chance it'll just stop working at some stage. It's more fun to use a real API though. To use the tram tracker API, you need to first connect with an endpoint that gives you an API token. That token can then be used for future calls.

To retrieve an API token, you hit this endpoint `http://ws3.tramtracker.com.au/TramTracker/RestService/GetDeviceToken/?aid=TTIOSJSON&devInfo=HomeTimeiOS` and retrieve the token from the response. The app id and dev info parameters have been coded in for you, as these should not need to change.

```
{
  errorMessage: null,
  hasError: false,
  hasResponse: true,
  responseObject: [
    {
      DeviceToken: "some-valid-device-token"
    }
  ]
}
```

We can then use this device token to retrieve the upcoming trams. The route ID and stop IDs that we pass to the API have been hard coded for you to represent the tram stops on either side of the road. The endpoint that retrieves the tram (with stop ID and token replaced with valid values) will be of the form `http://ws3.tramtracker.com.au/TramTracker/RestService/GetNextPredictedRoutesCollection/{STOP_ID}/78/false/?aid=TTIOSJSON&cid=2&tkn={TOKEN}`, returns the upcoming trams in the form:

```
{
  errorMessage: null,
  hasError: false,
  hasResponse: true,
  responseObject: [
    {
      Destination: "North Richmond",
      PredictedArrivalDateTime: "/Date(1425407340000+1100)/",
      RouteNo: "78"
    },
    {
      Destination: "North Richmond",
      PredictedArrivalDateTime: "/Date(1425408480000+1100)/",
      RouteNo: "78"
    },
    {
      Destination: "North Richmond",
      PredictedArrivalDateTime: "/Date(1425409740000+1100)/",
      RouteNo: "78"
    }
  ]
}
```

The dates returned look like they're in a strange .NET format, rather than something friendly and widely used like ISO8601. There is a function
```
  Date dateFromDotNetDate(String dotNetDate)
```
to convert these strings into `Date` objects that we can use in the app.

You'll notice it's one of those APIs that sometimes gives you error messages inside a valid JSON response. We ignore this for now and assume that a `200` response means that the data will be on the `responseObject` field.
