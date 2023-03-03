# KNOWN BUGS: 
- It may be possible to remove the account of another person than yourself.

TODO:
- implement delete mapping for audiofile 


# AANDACHTSPUNTEN VERDEDIGING: 
- Ik gebruik een package genaamd "Modelmapper" om de vertaling tussen modellen en DTO's gemakkelijk en beheersbaar te houden.

# DemoDrop- a general purpose online music demo marketplace (back end web project)

## About

Welcome to DemoDrop. DemoDrop is a general purpose online marketplace where Misic producers can upload their material and sell the rights to DJ's and other parties that are interested in playing said music. 

DemoDrop is being developed by Gerard Heuvelman, as the final assignment of the bootcamp Full Stack Dveloper of Novi University, Utrecht, The Netherlands

At this time, DemoDrop exists solely as a source-code only prototype, and as such still requires an IDE to run. 

## Main usages
DemoDrop offers the following functionality:
- Uploading demos. 
- Searching for demos
- Listening to a demo (online)
- Downloading a demo
- Sending a message to a demo owner
- Replying to received messages

## Installation guide
Steps below are only to install and deploy this (back end) portion of the Demo Drop Web application. You must also install and run the front-end project, which can be found at www.github.com/......

Prerequisites:
- OS: Windows, MacOS or Linux.
- Java JDK 17 or higher needs to be installed on your system
- PostgreSQL must be installed on your system. Also
- PGAdmin 4 (or higher) must be also be installed.

Installation steps:

1. Clone this project (Spring boot backend  project for the "DemoDrop" web app)
2. In PGAdmin, create a new database named "demodrop". Take note of your login credentials.
3. Open the project in an IDE.
4. At the root level of the project, create a file called ".env" and write the following lines in it, replacing the [EXAMPLE DATA]:
   - MAIL_SMTP_SERVER = [smtp.gmail.com]
   - MAIL_SMTP_PORT = [587]
   - MAIL_USERNAME = [myemail@gmail.com]
   - MAIL_PASSWORD = [Rumpl3$t1lt$k1n]
   - DATASOURCE_URL = [jdbc:postgresql://localhost:5432/demodrop]
   - DATASOURCE_USERNAME = [postgres]
   - DATASOURCE_PASSWORD = [myp0$tgr3$p@$$w0rd]
   - CLIENT_APP_URL = [http://localhost:3000]

5. install dependencies by typing "mvn install" in a terminal window
7. Run the project.

Initial configuration:  (!!!IMPORTANT!!!)
1. Log in using username "admin" and password "admin". You will be redirected to the Admin Control Panel (ACP)
2. Create a new admin account. with a strong password
3. Delete the user "admin"