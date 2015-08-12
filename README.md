# OpenTripPlanner-client-gwt

## Presentation

This project is an open-source client for [OpenTripPlanner](http://www.opentripplanner.org/) made using [GWT](http://www.gwtproject.org/).

It is released under the GPLv2 licence, see LICENSE file.

The interface is available for now only in English and French.

## Screenshot
![Screenshot](http://mecatran.github.io/OpenTripPlanner-client-gwt/img/screenshot1.png)

## Building

Modify the settings in `src/main/webapp/index.html` (router ID and URL to the OpenTripPlanner server, mainly).

Compile the project with:

    $ mvn package

Open `target/opentripplanner-gwt-client-VERSION/index.html` in a browser. That's it!

## Debug / develop

To use GWT super-dev mode, compile the project with maven (`mvn package`) as a boot-strap.

Then import the project in your IDE of choice (let's say Eclipse, import > maven).

Create a new launch configuration with the following parameters:
* Main class: `com.google.gwt.dev.codeserver.CodeServer`
* Program arguments: `com.mecatran.otp.gwt.OpenTripPlannerClient`
* VM arguments: `-Xmx512M`
* JRE > 1.7
* Classpath:
  * Add the GWT library to bootstrap entries
  * Add `src/main` and `src/resources` as user entries (Advanced... button)

Start the configuration you just launched, and visit the URL it gives on the console.
