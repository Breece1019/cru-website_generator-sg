# Info
This project aims to be a quicker solution for Cru staff at Ohio State to
update Small Groups and Small Group Leaders on their website.

## Getting Started
To run this code, you must have Maven and Java on your machine.

Once you have installed these items, open this repository on your machine.

Run this list of commands on your machine to start the project:
* `cd website-generator-helper`
* `mvn clean install`
* `mvn javafx:run`

This should be all you need to do to get the project running!

## How to Use
You can currently use this application to modify existing study information.
You cannot yet add or remove studies, nor can you change the order of studies.

Load the small group *.html* file into the application. This will parse the
small groups into a more readable format and display it to you.

* Pressing `/` while highlighting any text field will display a new text field,
where you can make your changes to the item. Press `Enter` when you are
finished editing, or `ESC` if you wish to cancel your modification.
* the **Add**,**Remove**, **Up Arrow**, and **Down Arrow** currently serve no
real function, so ignore them for now.