# Info
This project is to help out Cru Ohio State staff with one specific part of
their website that is still html code and edited manually.

The full website can be found [here](https://www.cruohiostate.com/)

## Getting Started
To run this code, you must have Maven and Java on your machine.

Once you have installed these items, open this repository on your machine.

Run this list of commands on your machine to start the project:
* `cd website-generator-helper`
* `mvn clean install`
* `mvn javafx:run`

This should be all you need to do to get the project running!

To build into a package:
* `mvn javafx:jlink`
* `jpackage --input target/ --name CruWebsiteHelper --main-jar website-generator-helper-1.0-SNAPSHOT.jar --main-class com.cru.websitegeneratorhelper.Main --type msi --dest output/ --runtime-image target/image`

This is for Windows, but you can configure jpackage to work for Mac as well.

## Features
You can currently use this application to modify existing study information.
You cannot yet add or remove studies, nor can you change the order of studies.

The small group *.html* file is loaded into the application. It then parses the
small groups into a more readable format and displays it to you, with buttons
that aid in modification.

## How to Use
Start the program, and you will see one button that allows you to load a file.
Load in the .html file that contains the Cru bible studies.

You will be greeted with a new window that displays all regions and studies. To
change the names of any of these items, highlight it with your mouse by
clicking on it once, then press `/` on your keyboard to edit the item. Type in
what you wish to rename it then press `Enter` on your keyboard. This will
update the item. If you want to cancel your changes, press `ESC` to stop the
modification. When you are finished with all your changes, press the
`Generate` button in the bottom-right corner to save the file.

Highlight an item and press the `Remove` button in the window to remove that
item and any of its related children.

Highlight an item and press the `Add` button in the window to add a child
into that item.

Highlight an item and press the `Up Arrow` or `Down Arrow` buttons in the
window to reposition an element.