SG Rail Routing Service
===

Singapore railway is a railway network with multiline and multiple interchange stations. This simple application expose an API
to find and display one or more routes from a specified origin to a specified destination for given journey starting time
, ordered by some efficiency heuristic.


Resulted routes have details,
- Steps station FROM - station TO
- Line change
- Total Number of stations to travel
- Total travel time

### Designing the solution
Considering stations and connectivity as gra ph theory use case, I've used the Breadth-First Search (BFS) and modified it to
get the all possible routes from start to destination. Updated the result path details with the travel time/ wail time
considering peak, night and off-peak hours given. Added extra check to avoid circling through the same station again and
again.

### Assumption made for this the solution

- stations input csv always in the format of below
```
EW23,Clementi,12 March 1988
EW24,Jurong East,5 November 1988
EW26,Lakeside,5 November 1988

<linecode><number>, <station-name>, <start-date>

Assuming all start dates are in the format of "d MMMM yyyy"
```
- station names are unique

#### 

### Prerequisite to run or deploy the service

As, a prerequisite docker should be installed in the environment( assumes using ubuntu)
```
sudo apt-get update
curl --fail --silent --show-error --location https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-get install software-properties-common
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt-get update
sudo apt-get install -y docker-ce
``` 

## How to deploy application from docker hub
1. `docker pull hmdmph/sg-routing-service:1.0.0`
2. `docker run -p 8080:8080 hmdmph/sg-routing-service:1.0.0`
### [Click here to go find-routes swagger URL](http://localhost:8080/swagger-ui/index.html#/route-controller/findByIdUsingGET)

## How to run the application from source

get the source from [github](https://github.com/hmdmph/sg-routing-service)


### [Click here to go find-routes swagger URL](http://localhost:8080/swagger-ui/index.html#/route-controller/findByIdUsingGET)

### sample payload

```
Source: Tuas Link
Destination: Redhill
Time: 2021-02-21T10:54:00Z
```



