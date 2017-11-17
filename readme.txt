Instructions to download and run the tests:

Pre-requisite: Java JDK & Android SDK installed and corresponding path and Enivornment variables configured

1. Download the java project from here: https://github.com/kavin1348/FlowDrive-AppiumJava/archive/master.zip
2. Open the downloaded zip file and extract it to a local drive
3. Open Eclipse IDE and import the java project by:
	i. File->Open Projects from File System
	ii. Enter the path of the folder containing extracted zip files (from step 2) in Import source textbox
	iii. Select the project folder listed and click on finish button
4. Add the supporting jar files to eclipse by:
	i. Project->Properties-> Java build path
	ii. Open libraries tab and click on Add External JARs
	iii. Navigate to the folder containing extracted zip files and upload all the jar files and 'selenium.webdriver.3.7.0.nupkg' file
	iv. Click on apply and close
4. Add TestNG to eclipse by: 
	i. Open eclipse marktet place by: Help->Eclipse Marktet place
	ii. Search for 'TestNG for Eclipse' and install it
	iii. After installation, go to FlowDrive.java file, scroll to line19: 'import org.testng.annotations.*;'
	iv. Hover over the error and click on 'Add TestNG library' from the quick fix suggestions 
5. Download and install Flowdrive android app in your phone: https://s3-eu-west-1.amazonaws.com/flowdrive.floowapp.io/apps/21/8b3d8e298a.apk
6. Connect your phone to the PC through usb cable and install usb drivers (if needed) for your phone 
7. Enable 'USB Debugging' & 'Install via USB' options from Developer options in your phone:
8. Download and install Appium server in your PC from here: https://bitbucket.org/appium/appium.app/downloads/AppiumForWindows_1_4_16_1.zip
9. Open Appium and click on 'Launch the Appium node server' button to start the server in the default path: 127.0.0.1 port 4723
10.Open eclipse, select FlowDrive.java and run it as TestNG test
11. Sitback and wait for the test run to complete

If you encounter any errors or problems, kindly reach out to me.