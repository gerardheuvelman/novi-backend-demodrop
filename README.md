# DemoDrop- a general purpose online music demo marketplace (back end web project)

## About
Welcome to DemoDrop, a general purpose online marketplace where Misic producers can upload their material and sell the rights to DJ's and other parties that are interested in playing said music.
DemoDrop is continuously being developed by Gerard Heuvelman, as the final assignment of the bootcamp Full Stack Dveloper of Novi University, Utrecht, The Netherlands.

At this time, DemoDrop exists solely as a source-code only prototype, and as such still requires an IDE to run. 

### Version
1.0

### Known bugs
None.

## Main usages
DemoDrop offers the following functionality:
- Uploading demos. 
- Searching for demos.
- Listening to a demo (online).
- Downloading a demo.
- Sending a message to a demo owner.
- Replying to received messages.
- Extensive administrative functionality.

## Installation guide
Steps below are only to install and deploy this (back end) portion of the Demo Drop Web application. You must also install and run the front-end project, which can be found at https://github.com/gerardheuvelman/-novi-frontend-demodrop.

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
   - UPLOAD_FOLDER: [uploads] (to use the test data, copy this setting exactly)
5. install dependencies by typing "mvn install" in a terminal window.
   Optionally, You can set the following properties in application.properties:
   - If you want to run the app with an empty dataset (no test data), then set the "spring.jpa.defer-datasource-initialization=" from true to false.
   - If you want the test data to regenerate each time you restart the app, you can change "spring.jpa.hibernate.ddl-auto=" from "update" to "create-drop".
7. Run the project. 
8. Run the frontend project of the same name (using its own instructions). A browser window will appear.
9. Log in as "admin", with password "Admin"
10. Navigate to Admin Control Panel => USer Control Panel => Create Admin User. to create your own admin account.
11. !!IMPORTANT!!  The inital, easily compromizable admin acccount must be made secure. In the user detail page for this account, eiter click on "Edit this user", and change it's password to a strong one, or click on "Delete this user", to delete the admin account in its entirety.

## Test Data
Test data is provided in the file data.sql in the folder source/main/resources. This rtest data had one single Admin account called "admin" with "admin" also as the password. All other users in the test data all have "acme" as their passwords. To explore the applications data , you may want to start with loggin in as user "gerard", whoo has two demos to his name, and has active convesations with user "admin" (admin accounts also have all "normal" functionality), who hs uploaded only one demo. 

After exploring this, Try logging in as "admin" to see the other side of the communications. Be sure to then also click on "Admin Control Panel" in the menu bar, to see all administrative functionality of the app, which accounts for about half of DemoDrop's functionality.

In case you decide not to use the test data, you must (before starting the app) either manually create The first admin account using PgAdmin, or rename "data minimal.sql" to data.sql, overwriting the original test data with a dataset containing only a single admin user account. Using this account you can create other admin accounts.

## Running tests
To run all unit tests in IntelliJ, right-click the green folder name "java" in the directory /src/test/, and select "Run all tests".
Doing this will also run integration tests, but these will fail due to inappropriate test context. To run the integration tests, right-click the IntegrationTests" folder and select "Run tests" in that folder. In this context all (enabled) tests should pass. 

## Roadmap:
- implement pagination for large list requests.
- Accept other file formats other than .mp3.

---
# TECHNICAL DOCUMENTATION

### Test Requests
Although the app features 75%-80% test coverage in unit tests, there is currently only one single set of integration tests (for the "Demo" request paths). To make up for this shortcoming, I have made an extensive list of 81 test request using the Postman API platform. Some of these are requests that are meant to fail as they are considered illegal according to the "associative security" logic implemented on nearly all routes (making these so-called "secure" routes actually secure).

To get and mount the full list of test data in JSON format, see the file "DemoDrop.postman_collection.json", that is included in the root folder of the project. This file can be imported into the "Postman" app, allowing you to test all endpoints extensively, which you should probably do, before you decide to fully trust the functionality and security of this product.

### Associative security:
Associative security (or associative authorization) is what I have named the extra layer of security (on top of Spring Boot's "standard" security) that I have built into all so-called "secure" request routes. In a nutshell, the logic (different logic for each route) simpl checks the object the user  is trying to create, access, alter or delete, against the contents of hte JWT that is sent along with the request, and checks to see if the presented identity is either the "owner" of this data, or otherwise, has a valid associative "need to know" relationship with it.

For instance, when a user makes a PUT request to /conversations/{conversationId}, the extra logic (implemented in the Service layer) checks the identity of the user, making sure that this user is either the producer of the demo that is associated with this conversation, or the interested user, who had initially inquired about this demo in the first place. If the found identity is neither of these, The request is denied, returning 403 Forbidden.

This layer of security is automatically overridden when the user making the request has admin privileges.

## Data Transfer Objects

When making requests to Demodrop's Public API, knowledge of the structure of DemoDrop's Data Transfer Objects (DTOs) is required.
The following DTOs are used:

### Input only DTOs

The structure and usage of DemoDrop's Dtos are described below, using the original Spring Boot annotations, which should be self-explanatory.

#### AuthenticationRequest
- String username (@NotBlank @Size(min=3, max=30))
- String password  (@NotBlank @Size(min=4, max=30)

#### NameIdInputDto (general purpose)
- String name

#### NumberIdInputDto (general purpose)
- Long id

#### UserInputDto
- String Username (@NotBlank @Size(min=3, max=30))
- String password (@NotBlank @Size(min=4, max=30) 
- String email (@NotBlank @Email)

### General purpose Dtos

#### AudioFileDto
- Long audioFileId 
- Date createdDate
- String originalFileName (NotBlank)
- Demo demo (@JsonIncludeProperties({"demoId", "title"}))

#### AuthorityDto
- String username (@NotBlank @Size(min=3, max=30))
- String authority (@NotBlank)


#### ConversationDto
- Long ConversationId
- Date CreatedDate (@NotNull)
- Date latestReplyDate (@NotNull)
- String subject (@NotBlank @Size(min=2 , max =100))
- String body ( @NotBlank @Size(min=2 , max =100000))
- Boolean readBbyProducer (@NotNull)
- Boolean readByInterestedUser (@NotNull)
- Demo demo (@JsonIncludeProperties({"demoId", "title"})
- User producer (@JsonIncludeProperties({"username", "email"})
- User interestedUser (@JsonIncludeProperties({"username", "email"})

#### DemoDto
- Long demoId
- Date createdDate (@NotBlank)
- String title (@NotBlank @Size(min=2, max =30))
- Double length ( @NotNull @Min(10) @Max(10000))
- Double bpm (@NotNull @Min(10) @Max(1000)))
- AudioFile audioFile(@JsonIncludeProperties({"audioFileId", "originalFileName"}))
- Genre genre (@JsonIncludeProperties({"name"})))
- User user (@JsonIncludeProperties({"username"}))
- List<User> favoriteOfUsers (@JsonIncludeProperties({"username"}))

#### EmailDetailsDto 
- String recipientUsername (@NotBlank @Size(min=3, max=30))
- String msgBody (@NotBlank))
- String subject (@NotBlank)
- String attachment

#### GenreDto
- String name (@NotBlank)

#### UserDto
- String username (@NotBlank @Size(min=3, max=30))
- String password (@NotBlank @Size(min=4, max=30))
- Boolean enabled (@NotNull)
- String apikey (@NotBlank))
- String email ( @Email))
- Date createdDate ( @NotNull)
- Set<Authority> authorities ( @JsonIncludeProperties({"authority"}))
- List<Demo> demos ( @JsonIncludeProperties({"title"})

#### UserPublicDto
- String username (@NotBlank @Size(min=3, max=30)))
- Date createdDate (@NotNull))
- List<Demo> demos ( @JsonIncludeProperties({"demoId", "createdDate", "title", "length", "bpm", "genre"}))


### Output Dtos

#### AuthenticationResponse
-  String jwt (@NotBlank)

---

## API SPECIFICATION

Demodrop ezposes a REST - style api. This is not a public API, and is meant to only be consumes by the DEmoDrop Front end application 

### VERSION 1.0

list of UNIQUE endpoints (50 at present):

#### Authenticate user
- Request: POST /authenticate
- Description: Authenticates the user in the system bt checking username/password combination. The user can identify him/herself with a json web token upon subsequent requests, until either the user logs out, or the token expires. If user's account has been disabled, the response will be 404 Forbidden.
- Body: Json {AuthenticationRequest}
- Example: {"username" : "admin", "password" : "admin"}
- Response: 200 OK / Json { jwt: ... } or 401 Unauthorized  

#### List public info users
- Request: GET /users/public
- Description: Lists publicly available (non-sensitive) user info. Either all (?limit=0) or limited.
- Query Params: limit (int) to limit the amount of items returned
- Response: 200 OK / json [{UserDto}] or [ ]. 

#### Get public info user
- Request: GET /users/public/{username}
- Description: Gets a singe record of non-sensitive user info.
- Response: 200 OK / json ({UserDto}) or 404 Not Found.

#### Check authentication
- Request: GET /authenticated
- Description: Retrieves the user's current authentication status 
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / true or false

#### List users (admin only)
- Request: GET /users
- Description: Lists user info, both public and private. Either all (?limit=0) or limited
- Authentication: Bearer Token
- Authorization: "admin"
- Query Params: limit (int) To limit the amount of items returned
- Response: 200 OK / json [{UserDto}] or [ ]

#### Get full user info
- Request: GET /users/{username}
- Description: gets user info (also private).
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / json ({UserDto}), or 404 Not Found

#### Check account status
- Request: GET /users/{username}/getstatus
- Description: Checks the user's current account status.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / "enabled" or "disabled"

#### Create (register) user
- Request: POST /users
- Description: Creates a new "User" object in the database
- Body: Json {UserInputDto}
- Example: {"username": "newuser", "password" : "1234", "email" : "newuser@gmail.com"}
- Response: 201 Created / json {UserDto}, or 422 Unprocessable Entity (in case of database conflict)

#### Create admin user (admin only)
- Request: POST /users/admin
- Description: Creates a new account with admin privileges.
- Authentication: Bearer Token
- Authorization: "admin"
- Body: json {UserInputDto}
- Example {"username": "admin2", "password" : "admin2", "email" : "admin2@gmail.com"}
- Response:  201 Created / json {UserDto}, or 422 Unprocessable Entity (in case of database conflict) 

#### Edit user details
- Request: PUT /users/{username}
- Description: Alters user Details (username must remain the same).
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body: json {UserInputDto}
- Example: {"username": "user", "password": "12345", "enabled": false, "apikey": "7847493", "email": "user@gmail.com"}
- Response: 200 OK / "user "{username}" was updated successfully."

#### Set new password
- Request PATCH /users/{username}/change-password
- Description: Resets the user's password. You must provide a UserDto object, passing in only the new password.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body: Json  {UserInputDto}
- Example: {"username": null, "password": "12345", "email": null}
- Response: 200 OK / "Password was updated successfully." "Old hash:{hash}" "New hash:{hash} "

#### Change email
- Request: PATCH /users/{username}/change-email
- Description: Changes a users email. The request must include a UserInputDto object, passing in only the new email address. 
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body: Json {UserInputDto}
- Example: {"username": null, "password": null, "email": "mynewemail@gmail.com"}
- Response: 200 OK / Json {UserDto} from database

#### Disable / enable account (admin only)
- Request: PATCH /users/{username}/setstatus
- Description: Disables or enables account. This setting will be taken into account when processing an authorization request.
- Authentication: Bearer Token
- Authorization: "admin"
- Query Params: status (boolean)
- Response: 200 OK / false for "account disabled"; true for "account enabled"

#### List demos
- Request: GET /demos
- Description: Lists demo's Either a full list (?limit=0), or a limited amount of results.
- Query Params: limit (int)
- Response: 200 OK / json {UserDto}

#### List demos by genre
- Request: GET /demos/bygenre
- Description: Lists demos of a certain genre, limited or unlimited
- Query Params: genre (string); limit (int)
- Response: 200 OK / json - [ DemoDto ], or  [ ]

#### Get single demo
- Request: GET /demos/{demoId}
- Description Retrieves a single "DemoDto" object.
- Response: 200 OK / json {DemoDto} or 404 Not Found

#### Get demo fav status
- Request: GET /demos/{demoId}/isfav
- Description: Checks whether the demo object is on the user's list of favorite demos.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- response: 200 OK / true or false.

#### Create demo
- Request:  POST /demos
- Description: Creates a demo. This request returns a Demo ID and is typally fllowed by an "Upload file and Associate with demo" request, with this unique ID given back as a request parameter. The request body must be a DemoDto. Genre must be an objet of type "Genre" instead of a string.
- Authentication: Bearer Token
- Authorization: "standard" or "admin"
- Body: Json {DemoDto}
- Example: {"title": "postedTitle1", "length": 102.0, "bpm": 140.0, "aufioFile": null, "genre": {"name":"Dance"}}
- Response: 201 Created / json {DemoDto}, containing the new DemoId, which must be used to associate an AudioFile with it.

#### Edit demo
- Request: PUT localhost:/demos/{demoId}
- Description: Alters a demo. This request does not alter the AudioFile object associated with this demo. Genre must be an objet of type "Genre" instead of a string.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body: Json {DemoDto}
- Example: {"title": "New Prime Audio", "length": 500.0, "bpm": 124.0, "file": null, "genre": {"name": "Dance" }}
- Response:  200 OK / json {DemoDto}

#### Set demo fav status
- Request: PATCH /demos/{demoId}/setfav
- Description: adds a demo to the user's list of favorite demos.
- Authentication: Bearer Token
- Authorization: "none"
- Query Params: status (boolean)
- Response: 200 OK / FavStatus from the database (boolean)

#### List conversations (admin only)
- Request: GET /conversations
- Description: Lists conversations sorted by latest reply date, either all (?limit=0) or limited to a specific number of items.
- Authentication: Bearer Token
- Authorization: "admin"
- Query Params: limit (int)
- Response: 200 OK / json [ ConversationDto ] or [ ]

#### Get single conversation
- Request: /conversations/{conversationId}
- Description: Retrieves a single conversationDto object
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / json {ConversationDto}, or 404 Not Found

#### Create conversation
- Request: POST /conversations
- Description: Creates a new Conversation from a ConversationDto.
- Authentication: Bearer Token
- Authorization: "standard" or "admin"
- Body: Json {ConversationDto}
- Example: {"subject": "postedSubject1", "body": "postedBody1", "demo": {"demoId":1001}}
- Response: 200 OK / json {ConversationDto} from database

#### Reply to conversation
- Request: PUT /conversations/{conversationId}
- Description: Reply to (edit) a conversation
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body: Json {ConversationDto}
- Example: {"subject": "updatedSubject1", "body": "updatedBody1"}
- Response: 200 OK / json {ConversationDto} from database

#### Mark conversation as read
- Request: PATCH /conversations/{conversationId}/markread
- Description: Mark a conversation as read.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- 200 OK / json {ConversationDto}

#### Upload file & assign
- Request: POST /demos/{demoId}/upload
- Description: Follow up request to either "Create new demo" or (optionally) to "edit demo". Oploadsa file to the projects upload folder (you can set this using a .env file, or by altering application.properties directly)
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body form-data
- Example: file/multipart C:/myDocuments/myNewDemo.mp3
- Response: 200 OK / "A new file was successfully uploaded and associated with demoId {demoId}"

#### INTERNAL create & save file
- Request: POST /audiofiles
- Description: Internal subrequest to "upload file & Assign". Creates te actual file on disk (renamed accoring to the ID of an associated "AudioFile" object) and links it to this object that is, in turn, linked to the appropriate Demo.
- Authentication: Bearer Token
- Authorization: "associative"
- Body: form-data
- Example: file/mulitpart C:/myDocuments/myNewDemo.mp3
- Response: N/A

#### Download single file
- Request: GET /demos/{demoId}/download
- Description: Retrieves the mp'3 file associated to the demoId from disk, renames it back to the original file name and triggers the user's OS's fiel download mechanism.
- Response: 200 OK / file/multipart

#### List personal demos
- Request: GET /users/{username}/demos
- Description: Lists users personal demo THis is public data, so it does not require authorization or even authentication.
- Response: 200 OK / json [{DemoDto}] or [ ] 

#### List favorite demos
- Request: GET /users/{username}/favdemos
- Description: Lists th user's favorite demos.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / json [{DemoDto}] or [ ]

#### List personal conversations
Request: GET /users/{username}/conversations
- Description: Lists conversations the user is involved in, either as producer or as interested user.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / json [{ConversationDto}] or [ ]

#### List personal authorities (admin only)
- Request: GET /users/{username}/authorities
- Description: Lists the roles that the user has, in the form of AuthorityDto objects
- Authentication: Bearer Token
- Authorization: "admin"
- Response: 200 OK / json [{AuthorityDto}] or [ ]

#### List demos containing search query
- Request: GET /demos/find
- Description: Does a non-case-sensitive substring search of the title field of all demos and returns matching demos in the form of DemoDtos.  
- Query Params: query (String)
- Response: 200 OK / json [{DemoDto}] or [ ]

#### Assign personal authority (admin only)
- Reuest: localhost:8080/users/{username}/authorities
- Description: Assign a role to a user
- Authentication: Bearer Token
- Authorization: "admin"
- Body: Json {AuthorityDto}
- Example: {"authority": "ROLE_ADMIN"}
- Response: 204 No Content

#### List music genres
- Request: GET /genres
- Description: Lists all music genres in the form of GenreDto objects
- Response: 200 OK / json [{GenreDto}] or [ ]

#### Get single genre
- Request: GET /genres/{genreName}
- fetches a genre by its name
- Response: 200 OK / json {GenreDto}, or 404 Not Found

#### Create genre (admin only)
- Request: POST /genres
- Description: Creates a new music genre.
- Authentication: Bearer Token
- Authorization: "admin"
- Body: json {GenreDto}
- Example: {"name":"a new genre"}
- Response: 201 Created / json {GenreDto}

#### Send simple email
- Request: POST /email/sendsimple
- Request description: Using DemoDrop Email sending capabilities, users must hav the right credentials to send to any given recipient. To prevent misuse, the recipient must always be an active user of DemoDrop.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body: Json {EmailDetailsDto}
- Example: {"recipientUsername":"user1", "msgBody":"Simple email body...", "subject":"simple email subject "}
- Response: "Mail Sent Successfully..."

#### Send email w/attachment
- Request: POST /email/sendwithattachment
- Description: Same as above, but with an attached file
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Body: Json {EmailDetailsDto}
- Example: {"recipientUsername":"user1", "msgBody":"Simple email body...", "subject":"simple email subject ", "attachment" : "C:/MyDocuments/mySong.mp3"}
- Response: "Mail Sent Successfully"

####  List audioFiles (admin only)
- Request: GET /audiofiles
- Description: List AudioTileDtos, either all (?limit=0) or limited.
- Authentication: Bearer Token
- Authorization: "admin"
- Query Param: limit (int)
- Response: 200 OK / json [{AudioFileDto}] or [ ]


#### Get audioFile (admin only)
- Request: GET audiofiles/{audioFileId}
- Description: Retrieves a single AudioFileDto
- Authentication: Bearer Token
- Authorization: "admin"
- Response: 200 OK / json {AudioFileDtoDto}, or 404 Not Found

#### Edit audioFile (admin only)
- Request: PUT audiofiles/{audioFileId}
- Description: alters an existing AudioFile object.
Authorization
- Authentication: Bearer Token
- Authorization: "admin"
- Body: json {AudioFileDto}
- Example: {"originalFileName": "anotherversion.mp3"}


#### Delete orphaned mp3 files (admin only)
- Request: DELETE /audiofiles/purge
- Description: DEletes all mp3 files on disk that no longer have a corresponding AudioFile Object in the database
- Authentication: Bearer Token
- Authorization: "admin"
- Response: 200 OK / "{numDeletedFiles} orphaned mp3 files were successfully deleted from uploads."

#### Delete genre (admin only)
- Request: DELETE /genres/{genreName}
- Description: deletes a music genre. All demos with this genre will have their genre set to "unknown"
- Authentication: Bearer Token
- Authorization: "admin"
- Response: 200 OK / "Genre {genreName} was deleted successfully."

#### Remove personal authority (admin only)
- Request: DELETE /users/{username}/authorities/{roleName}
- Description: Removes a role from a user.
- Authentication: Bearer Token
- Authorization: "admin"
- Response: 204 No Content 

#### Delete conversation (admin only)
- Request: DELETE /conversations/{ConversationId}
- Description: Deletes a conversation.
- Authentication: Bearer Token
- Authorization: "admin"
- Response: 200 OK / "Conversation {conversationId} was deleted successfully."


#### Delete all conversations (admin only)
- Request: DELETE /conversations
- Description: Delete all conversations in the system.
- Description: Deletes a conversation.
- Authentication: Bearer Token
- Authorization: "admin"
- Response: 200 OK / "{numDeletedConversations} conversations deleted successfully."

#### Delete single demo
- Request: DELETE /demos/{demoId}
- Description: DEletes a fsingle demo. Deletion will cascade to the corresponding AudioFile object.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / "Demo {demoId} was deleted successfully."

##### Delete all demos (admin only)
- Request: DELETE /demos
- Description: Deletes all demos in the system. Deletion will cascade to the corresponding AudioFile objects.
- Authorization: "admin"
- Response: 200 OK / "{numDeletedDemos} demos deleted successfully."

#### Remove user account
- Response: DELETE /users/{username}
- Description: Deletes a user account.
- Authentication: Bearer Token
- Authorization: "associative" or "admin"
- Response: 200 OK / "User "{username}" was deleted successfully."





