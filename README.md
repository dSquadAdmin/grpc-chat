**Requirement**
* Java greater than JDK 7

** Building **
* Linux\Unix
``` $./gradlew build ```
*Windows
```:\> gradelw.bat build ```
This will make grpc 1.0 in the build/distribution directory. Extract the archive and run 
```./chat-Server ``` in the unix variants 
or just ``` chat-Server-bat``` in windows.
This will host a chat server.  

To run the client change IP of the server to the IP of your machine in ChatClient.java before build.
run 
``` ./chat-client <your_server_ip> ```
or
``` chat-client.bat <your_server_ip> ```


