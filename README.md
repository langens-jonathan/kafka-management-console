# Kafka Management Console
This is a single page web application that should help manage kafka clusters. To avoid constantly having to execute CLI commands I have built this interface. For now it is a friday night project that barely sports enough functionality to support my own use case. Maybe I will expand on it in the future, or make it look less ugly :).



## Current state
As stated above the current state is BUTT ugly and very low on functionality. It has just what I need for my current use case. I will probably only expand it in light of my own usecases.

Current todos:
+ the frontend does not connect in docker
+ the backend sometimes fails to find kafka based on setup, maybe copying /etc/hosts or something is in order?

## To install
Enter the kafka URL in the docker-compose.yml file for the backend service.
Then 
```
drc up -d
```
And you should be golden.
