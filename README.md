JOB BLENDER
=============

a jobcoin mixer


### Description

Bitcoin is not an anonymous protocol. People still can see where the coin goes from one address to another. 
A coin mixer is one way to maintain privacy in the network.

### Flow
- user provide their addresses to the job-blender
- job-blender will save it, and provide a deposit address
- user save this address, and use it to receive any coin
- the system will monitor/poll the coin network, and look for the deposit address that it owns
- then it will mix it, by sending it to the house address
- it will house all coin together into one address
- then it will dole out the coin, to the user addresses that the system has saved before
- the system will deduct the fees for this process

### How To
- clone the repo
- `./gradlew build`
- `./gradlew run`
- or, you can use intellij or other IDE to run it, on the main class App.java
- it will run a console app
![Alt text](image/option.png?raw=true "Option")
- choose option 1
- put in your address, comma separated
![Alt text](image/insert_address.png?raw=true "Input")
- it will return the deposit address
![Alt text](image/deposit_address.png?raw=true "Deposit")
- go to https://jobcoin.gemini.com/crushed-cassette
- try to send some coin to that deposit address
- wait for 10-15 second
- refresh the page
- you will see the transaction
![Alt text](image/transactions.png?raw=true "Transactions")
- some fees will be deducted,it is inside the config.json
- once you are done, choose option 2 in the console to exit
![Alt text](image/exit.png?raw=true "Exit")

### Logic
- the system will do polling every 5 second and call the all transactions API
- it will try to compare with the latest timestamp, will in the beginning will be set to starting up time
- once the poll is completed, it will be updated with the latest time in the result
- then it will try to filter only the toAddress that belongs to the system deposit address
- then it will mix them all together by sending it to house address
- then the dole service will dole it out from house address to the user addresses
- it will do simple fees deduction
- and it will do simple division to get the number for each user address
- then it put the doled amount to queue
- which some executor will poll it and send it to user address over the time

### Next Step
- the unit test still can be improved, some of the function is hard to test
- can use mocks to check if the internal service is called properly
- also, the database right now is still a simple hashmap
- might need to update that to an actual db, so that when the system restart, we still can get all of the deposit address that we created before.
- the system is still console, might need to create it as an API service
- currently the system is calling the all transaction api, after some time it will be too big
- need to have a pagination api to only call the recent one that we want based on time
- or might move to other api based on deposit address that the system own, even though the time taken to call the api needs to be calculated.
- also, need to add logging for better tracing