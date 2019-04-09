
# Project K.L.A.U.S. 
TS connector using spring mvc

## Local Deployment of App

1. Install local Eclipse EE.
Download Eclipse: Eclipse Java EE IDE for Web Developers (Version Oxygen) from https://tools.hana.ondemand.com/


2. Install SAP Tomcat Server.
To run the app locally a server is needed. We used Tomcat 8. To install Tomcat go to eclipse, click on Help -> Install new Software. If you use eclipse oxygen, Paste
```
    https://tools.hana.ondemand.com/oxygen
```
Install SAP Cloud Platform Tools
Alternatively you can download and setup manually:
Tomcat 8 from: https://tools.hana.ondemand.com/

3. Clone App to a folder with your preferred Git Client.

4. Create The war file.
For creating the war file, go on Run -> Run Configuration. Click on Maven build and press on Button (upper left corner) "New Launch Configuration". Select the launch configuration and choose right project for Base Directory. Into goals write
```
    clean install
```
Now run the newly created configuration and hope that it is successful.

5. Add the Destination to the folder where Eclipse is installed as .JcoDestination File. Ask colleague for file (Workaround needs to be fixed!)
6. Create the Server.
Go on tab "Servers" right click somewhere. Choose New->Server. Select under SAP "Java Web Tomcat 8" and click next. Now add the war file created in step 4. And finish. Now the tomcat server starts and should be accesible
7. For local development ensure you have `CoDeX_B7W.jcoDestination` in Location where Tomcat starts. 


Example call


http://localhost:8080/cct-ts-connector/assignments/D021809



## Deployment on CF

Deploy app to cf (use CLI; installing/handling see https://wiki.wdf.sap.corp/wiki/display/GEMINI/Getting+Started+With+Cloud+Foundry):
Goto you local eclipse application folder (C:\...); insert cmd in the location field => a command prompt opens for the correct location.
Type:
```
    C:\...> cf push
```

Just be aware that the approuter is needed which handles the routing. So the app will only work if the approuter in parallel is running. You can find the approuter here:
https://github.wdf.sap.corp/SAPLanguagePlatform/cct-approuter


Example call

via approuter:

https://cct-approuter.cfapps.sap.hana.ondemand.com/ts/assignments/D021809


