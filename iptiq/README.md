# Round Robin Load Balancer Java Assignment

## Introduction
A load balancer is a component that, once 
invoked, it distributes incoming requests to a list of
registered providers and return the value obtained 
from one of the registered providers to the original
caller. This Java project is a simple representation of this process.


 
 ## Git Repo
feature branch -> 

## Steps to configure project
Run the class Execute as java application.

## Process:
Console popup:
1 - Register a server  <br /> 
2 - Call random server <br /> 
3 - Remove server <br /> 
4 - Server List  <br /> 
5 - Set concurrent call limit  <br />  
6 - Quit  <br /> 


 ## Register a server:

Press option 1 <br /> 
Enter server key you want to register <br /> 
Server Added Successfully

This process will add the server in the server list. Max server added is 10. It also randomly assigns the health of the server (scenario based) value 1 is for healthy 0 is for inactive server.

If the server is not healthy then it will remove it from the healthy server list


 ## Remove a server:<br /> 
Press option 3<br /> 
Enter server key you want to remove<br /> 
Populate the list of servers<br /> 
Enter the key of server you want to delete<br /> 
Server removed Successfully<br /> 
** Once server is removed from the active list it is assigned to removed server list. If the server is removed from removed server list, it is removed completely. 


 ## Call random server:<br /> 
Press option 2<br /> 
Print unique string id of server<br /> 


 ## Get list of registered ,removed, unhealthy servers:<br /> 
Press option 4<br /> 
Print all server list<br /> 


 ## Change unhealthy status of server:<br /> 
Press option 6<br /> 
Enter the id of the server<br /> 


 ## Quit<br /> 
Press option 7<br/> 

## Process of auto assign <br/>

Any removed server will auto assign the healthy server list after two successful health check trials.


## Process of Health check <br/>

A heart beat check process will check the health of the servers at every 20 seconds. This will publish the list of all server and also auto assign inactive server to active list after two successful trys.

