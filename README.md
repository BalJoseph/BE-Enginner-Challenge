# BE-Enginner-Challenge

This solution was built using Java 21

## How to run 
To run type ./run.sh

## Questions

### a)

#### main/
In the main directory you can find the solution which was built using Java Spring with the MVC design pattern.

In the controller package, you can find the sole controller which handles all of the 3 requested end points.

In the model package you can find 4 sub packages.
1 package for each endpoint which contains the corresponding models which are required to serve that endpoint.
And another package which contains the data structures required to store the users' and accreditations' information.

In the service package you can find the DatabaseService which stores and retrieves the information requested by the endpoints.

AccreditationStatusChecker performs the background process which is responsible for expiring CONFIRMED accreditations whose last update was older than 30 days.

#### test/

In the Test directory you can find the AccreditationControllerTest in the controller package which performs integration tests to help validate the endpoints.

### b)
A possible way to log historical accreditation status updates could be with the use of a new table. Which uses the id of the accreditation as a foreign key and the status and timestamp of when it was changed as fields.

Another simpler solution could be to add this information as an array field in the already existing accreditation table.

### c)
One way this could be scaled would be by actually implementing a real database service, so that you could scale horizontally and run more instances to be able to meet the demand.

Another way would be by optimising the processing that goes in the endpoint. As it currently stands the performance of the GET request is scaling according to the total number of accreditations because the algorithm has to iterate through all of the accreditations to check which ones belong to the user. An alternative way to this would be to store a reference to the accreditations in the user's table or to group all of the accreditations which belong to the same user into the same record.








 

