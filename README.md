A6 Repo for CS6310 Spring 2021
# To Install Docker go to:
```
https://docs.docker.com/get-docker/
```

# Note please run all scripts from the project root directory

### To build:

```
./scripts/build.sh
```

### To test a specific scenario against the initial jar
#### Mac / Linux
```
./scripts/test.sh <scenario>
```

### To batch run the test scenarios
#### Mac / Linux
```
./scripts/batch.sh
```

### To run in interactive mode
#### Step 1 from the host 
```
./scripts/interactive.sh
```
#### Step 2 from the container
```
java -cp ./lib/*:streaming_wars.jar Main -i
```
#### Step 3 from the jar
* From there you can run any of the commands listed from the assignment:
```
create_demo,<short name>,<long name>,<number of accounts>
create_studio,<short name>,<long name>
create_event,<type>,<name>,<year produced>,<duration>,<studio>,<license fee>
create_stream,<short name>,<long name>,<subscription price>
offer_movie,<streaming service>,<movie name>
offer_ppv,<streaming service>,<pay-per-view name>,<price>
watch_event,<demographic group>,<percentage>,<streaming service>,<event name>
update_demo,<short name>,<long name>,<number of accounts>
update_event,<name>,<year produced>,<duration>,<license fee>
update_stream,<short name>,<long name>,<subscription price>
retract_movie,<streaming service>,<movie name>,<movie year>
next_month
display_demo,<short name>
display_stream,<short name>
display_studio,<short name>
display_events
display_offers
display_time
write,<file name>,<password>
stop
```

Including the new commands for user management:
```
create_user,<user name>,<password>,<user type>
update_user_type,<user name>,<user type>
update_user_password,<user name>,<password>
delete_user,<user name>
```

### To run & test in interactive mode

```
java -cp ./lib/*:streaming_wars.jar Main [-i] < commands_00.txt > stream_test_00_results.txt
```

### To run a specific scenario with your jar and output to localhost

```
docker run --mount type=bind,source="$(pwd)"/db,target=/usr/src/cs6310/db --mount type=bind,source="$(pwd)"/output,target=/usr/src/cs6310/output gatech/streamingwars sh -c 'java -cp ./lib/*:streaming_wars.jar Main < commands_00.txt' &> stream_test_00_results.txt
```

### If you get stuck in an infinite loop
Simply stop and remove the running container
```
docker ps
docker rm -f <container_id>
```

### To test with a clean image & container
After running the below command you will need to run the build command again
```
docker ps -aq | % { docker stop $_ } | % { docker rm -f $_ } | docker images -f "dangling=true" -aq | % { docker rmi -f $_ } | docker images gatech/* -aq | % { docker rmi -f $_ }
```

### Third Party Libraries
* sqlite-jdbc: https://github.com/xerial/sqlite-jdbc v3.34.0
* zip4j: https://github.com/srikanth-lingala/zip4j v2.7.0
