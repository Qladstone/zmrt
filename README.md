# Overview
This is the source code for a web application used to find routes on a mass transit rail network, developed using the Spring Boot framework using Java 11.

# Data

A csv file named `StationMap.csv` containing the rail data should be placed in the project root. It should contain a header, with three columns:
* `Station Code`: A string consisting of a two-letter prefix (the rail line of the station) and positive integer suffix (the line postion of the station)
* `Station Name`: A string identifying a station
* `Opening Date`: The date when the station starts operating in the format `d Month yyyy` (e.g. 7 January 2004 or 19 July 1996)

Here is an example of the contents of a valid rail data file:
```
Station Code,Station Name,Opening Date
NS1,Jurong East,10 March 1990
NS2,Bukit Batok,10 March 1990
NS3,Bukit Gombak,10 March 1990
NS4,Choa Chu Kang,10 March 1990
NS21,Newton,12 December 1987
NS22,Orchard,12 December 1987
NS28,Marina South Pier,23 November 2014
EW24,Jurong East,5 November 1988
EW1,Pasir Ris,16 December 1989
EW2,Tampines,16 December 1989
EW3,Simei,16 December 1989
EW4,Tanah Merah,4 November 1989

```

# API

A single HTTP API is supported:

```
GET /routes
```

It takes in the following required query parameters:

* `origin`: A string indicating the name of the station where the route should start
* `destination`: A string indicating the name of the station where the route should end
* `start-datetime`: An ISO 8601 datetime (without timezone) indicating when the route should start

The API searches for routes and returns routes (see below for an example), where each route has a sequence of station codes. For any two station codes adjacent in the sequence, they indicate traveling along a rail line if they share the same station code prefix, otherwise a transfer from one rail line to another within a station. 

Here is an example request:

```
GET /routes?origin=Choa%20Chu%20Kang&destination=Nicoll%20Highway&start-datetime=2040-03-12T13%3A55%3A12
```

Here is an example response:

```
200 OK

{
    "routes": [
        {
            "sequence": [
                "NS4",
                "NS3",
                "NS2",
                "NS1",
                "EW24",
                "EW23",
                "EW22",
                "EW21",
                "CC22",
                "CC21",
                "CC20",
                "CC19",
                "DT9",
                "DT10",
                "DT11",
                "DT12",
                "DT13",
                "DT14",
                "DT15",
                "CC4",
                "CC5"
            ]
        }
    ]
}
```
