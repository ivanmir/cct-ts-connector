# BAPI call under Cloud Foundry 
Calling BAPIs under Cloud Foundry using the following:

* Spring Boot 
    * web
    * tomcat
    * looging
    * devtools
* XS2 security
* CF Service Instances 
    * web
    * XSUAA: jco-xsuaa --> Created with security/xs-security.json
    * Connectivity: connectivity-service 
    * Destination: destination-service 

## Installation

### Clone the git repository
- Clone App to a folder with your preferred Git Client.

### Cloud Foundry Dependent Services
- Create the CF services described above (destination, connectivity and xsuaa). To create each service instance, go to the sub-account's "Space" menu and select "Services" --> Service Market Place". Then select the desired service type and click on "Instances" to be able to add. Then click on the "New Instance" button. Pay attention to the xsuaa which requires the plan type to be "Application" and the contents of the xs-security.json (provided in the cloned project under the folder security). Make any adjustments on the file according to your needs (new roles & scopes) to be verified later under the REST controller.

### Add destination
On the space menu select "Services" --> "Service Instances" and click on the destination service. Then on the menu select "Destinations". Each destination listed here will be available only to the space. If you wish to share a destination between spaces, you may create it at the sub-account level. The system will try to find a destination first at the space level. Therefore, destinations with the same name at the sub-account level will be ignored. Click on the button "New Destination" and add a destination to your ABAP system. Don't forget to enter:
- jco.client.ashost
- jco.client.client
- jco.client.sysnr
- jco.client.lang
- jco.destination.pool_capacity

Take note of the Destination name you used here, we will have to use the same name under our code.

### Role tampletes
- Create a role template via Cloud Cockpit under Sub-Account Menu "Security" --> "Role Collections". Click on the "New Role Collection" button and give it a name of your preference. Once created click on the name's link to assign new roles to it. Click on the "Add Role" button and choose one of the roles you listed on the xs-security.json file (the sample here will list a role named all for xs-app-name cct-jco).

### Assign Role Template to User
- Under the cockpit go back to the sub-account and click on the "Trust Configuration" item under the menu "Security". Click on the name of your account's IdP. Enter the e-mail address of the user that will be allowed to use this application and click on the button "Show Assignments" (if this is the first time a user is assigned a Role Collection the system will propmt you to add the user. If this is the case, click "Add User"). Click on the button "Add Role Collection" and select the one you created on the previous step.

### Adapt the destination name in the code
- Open the CallBAPI class under /src/com/sap/gs/cct/tsconnector and adapt line 29 to reflect the Destination name you took note from a previous step.

```
    JCoDestination destination = JCoDestinationManager.getDestination("<destination_name>");
```

### Build
- Create The war file.
Run maven command line with the following arguments:

```
    mvn clean install
```

### Deploy to Cloud Foundry
- Adjust the routes for the application under the manifest.yml (pay attention to the CF landscape domain for each URL used)

- Goto your local application folder (C:\...) and type:

```
    C:\...> cf push
```

### Deploy an App Router
- Clone the app-router to a folder with your preferred Git Client.
https://github.com/ivanmir/cct-approuter.git

Example call

via approuter:

```
    https://cct-approuter.cfapps.us10.hana.ondemand.com/bapi
```

| WARNING: Don't call this app's URL directly as it expects users to be already authenticated. |
| --- |
