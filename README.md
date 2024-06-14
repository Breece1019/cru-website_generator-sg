## Getting Started

This project is to help out Cru Ohio State staff with one specific part of
their website that is still html code and has to be edited manually.

The full website can be found [here](https://www.cruohiostate.com/)

## How to compile (NOT WORKING ATM)
run `javac --module-path "lib/javafx-sdk-21.0.3/lib" --add-modules javafx.controls,javafx.fxml -cp "lib/*.jar" -d bin src/*.java`
in your main project folder in your CLI.

Then, run `jar cfm Website-Helper.jar MANIFEST.MF ./*.class` in the bin folder.

Make sure you have a MANIFEST.MF file that looks like:

```
Manifest-Version: 1.0
Class-Path: lib/jsoup-1.17.2.jar
Main-Class: Main
```

## How to use

Start the program, and you will see one button that allows you to load a file.
Load in the .html file that contains the Cru bible studies.

You will be greeted with a new window that displays all regions and studies. To
change the names of any of these items, highlight it with your mouse by
clicking on it once, then press `/` on your keyboard to edit the item. Type in
what you wish to rename it then press `Enter` on your keyboard. This will
update the item. When you are finished with all your changes, press the
`Generate` button in the bottom-right corner to save the file.