# Improve Detroit Data Pull


## Background:
I created this as a solution to download all of the 'Car traveling in bike lane' issues reported in Detroit's ImproveDetroit app. The available arcgis dataset did not have that column, so I went up to the SeeClickFix origin database.
To report a car in the bike lane a user selects "Traffic Complaints" category and then responds to the first question "Car Traveling in bike lane"
![image](https://github.com/user-attachments/assets/88878027-4f63-4345-a79b-1b7d0fa2ae84)
![image](https://github.com/user-attachments/assets/73f353b5-e728-4993-8408-9e57bf107de8)


## What Is This??
This Java SpringBoot code will hit the [SeeClickFix issue API](https://dev.seeclickfix.com/v2/issues/) and pull down all issues in the category 'Traffic Complaints' and save to a local MySQL database. Many cities use SeeClickFix back ends, but this code specifically uses a bounding box around Detroit, the request_types of 22880 to limit results, and details=true to return the Question\Answer list. As of May 2025 there are around 4200 Traffic Complaints, but over 500k total issues. Issues endpoint is maximum 100 issues per page and SeeClickFix rate limits at 20 calls per minute, a max of 2,000 per min.


The SeeClickFix endpoints do not require any authorization and return in json. Viewing the map on the [website](https://detroitmi.gov/webapp/improve-detroit-report-issue-online) the call to /issues/ can be copied into a browser or curl, for example:  
https://seeclickfix.com/api/v2/issues?min_lat=42.321030105074314&min_lng=-83.06650430969236&max_lat=42.34437972780504&max_lng=-83.02170069030765&status=open%2Cacknowledged%2Cclosed&fields%5Bissue%5D=id%2Csummary%2Cdescription%2Cstatus%2Clat%2Clng%2Caddress%2Cmedia%2Ccreated_at%2Cacknowledged_at%2Cclosed_at&page=1

The response is a SeeClickFixResponse object, containing a list of IssueResponseModel (the data we want) and a MetadataResponseModel (containing pagination information). Inside IssueResponseModel has id, status, lat, lng, created at, and most important for this use case a list of QuestionResponseModel. These are Question\Answer pairs, which is where "Car traveling in bike lane" is.

Because of multiple objects and lists I created a MySQL database to store and easily search the data, with one table for Issues, Reporter (user object), Question (question, answer, issue id), and Request Type (currently only Traffic Issues). This allows easy string contains search on either Issue.description or Question.answer for mis-categorized items.

The filtered dataset of Traffic Issues where the user answered "Car Traveling in bike lane" is on this Looker Studio dashboard: https://lookerstudio.google.com/u/1/reporting/ee8e458b-94b1-45b5-b93f-359f87e408b7/page/oQ4HF
![image](https://github.com/user-attachments/assets/1bde2693-2916-4821-b128-36f90c1f54d7)


## warnings: I have not tested this out with any other categories or other cities. There are likely other fields in other categories or cities that would break the code.

## Endpoints:
getTrafficIssuesFromApi/ - Call the SeeClickFix Issues endpoint and collect all Detroit "Traffic Complaints" issues and save to MySQL database

writeLocalDatabaseToCSV/ - Export MySQL database to CSV files for each table (to export or share)

## To Run:
Create or connect to MySQL database and update application.properties connection info
gradle build
boot run!

## To do:
Add code to download category "Pothole" - which is used for debris in bike lanes
Add code to download category "Traffic Sign Issue" - some users are adding issues to here for broken or missing bike lane delineators

