# UiMutilator #
Introducing the *UiMutilator* - Android UI Testing on several devices that requires interaction. Perfect for automating UI testing for apps that relies heavily on interaction, for example chat apps. Don't like jUnit? No problems, with *UiMutilator* you can use any test runner such as TestNG to drive the UI tests.

The *UiMutilator* exposes a similar interface as the Android UiAutomator. However, this *UiMutilator* is capable of running on multiple devices.

## Usage ##
The methods exposed by *UiMutilator* differs in two major ways: The `getUiDevice()` method and the `newUiObject()` method.

Instead of calling `getUiDevice()` like in the Android UiAutomator, you can `getUiDevice().any()` to connect to any device connected to your computer. You can also use other methods such as `getUiDevice().number(1)` or `getUiDevice().withSerial("emulator-554")` to select which device to run the test on.

This has the advantage of beeing to interleave ui commands and assertions between two devices.

Instead of calling `new UiObject()` like in the Android UiAutomator, you have to call `uiDevice.newUiObject()`. This is because *UiMutilator* needs to know on which device you intend to act, since a test may be connected to several devices.

## Example ##
This is an example of a *UiMutilator* test

```Java
@Test
public void testGetText() throws Exception {
	UiDevice uiDevice = getUiDevice().any();
	uiDevice.pressHome();
	uiDevice.pressMenu();
		
	UiObject settingsOption = uiDevice.newUiObject(new UiSelector().text("System settings"));
	settingsOption.clickAndWaitForNewWindow();
		
	UiObject sound = uiDevice.newUiObject(new UiSelector().text("Sound"));
	Assert.assertEquals("Sound", sound.getText());
}
```

## Building ##
To build the 'UiMutilator.jar' from source file follow the following instructions.

* Build the ant file 'command-tests/build.xml' with tharget 'build'.
* Build the classes in the src folder.
* export alla class files generated from the sources in the src folder and, this is important, also export the command-tests/bin/command-tests.jar.

This can be done easily in for example Eclipse. There is also a test suite containing a few demonstration tests inside the test source folder.

## Author ##
Created by Samuel Carlsson <samuel.carlsson@gmail.com> & Johannes Elgh <johannes.elgh@gmail.com>
