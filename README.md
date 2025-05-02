Improve Detroit Data Pull

I created this as a solution to download all of the 'Car traveling in bike lane' issues reported in Detroit's ImproveDetroit app. The available arcgis dataset did not have that column, so I went up to the SeeClickFix origin database.

This Java SpringBoot code will hit the [SeeClickFix issue API](https://dev.seeclickfix.com/v2/issues/) and pull down all issues in the category 'Traffic Complaints'. Many cities use SeeClickFix back ends, but this code specifically uses a bounding box around Detroit and the request_types of 22880 to limit results. As of May 2025 there are around 4200 Traffic Complaints, but over 500k total complaints. Issues endpoint is maximum 100 issues per page and SeeClickFix rate limits at 20 calls per minute, a max of 2,000 per min.

The SeeClickFix endpoints do not require any authorization and return in json. Viewing the map on the [website](https://detroitmi.gov/webapp/improve-detroit-report-issue-online) the call to /issues/ can be copied into a browser or curl, for example:  
https://seeclickfix.com/api/v2/issues?min_lat=42.321030105074314&min_lng=-83.06650430969236&max_lat=42.34437972780504&max_lng=-83.02170069030765&status=open%2Cacknowledged%2Cclosed&fields%5Bissue%5D=id%2Csummary%2Cdescription%2Cstatus%2Clat%2Clng%2Caddress%2Cmedia%2Ccreated_at%2Cacknowledged_at%2Cclosed_at&page=1

The response is a SeeClickFixResponse object, containing a list of IssueResponseModel (the data we want) and a MetadataResponseModel (containing pagination information). Inside IssueResponseModel has id, status, lat, lng, created at, and most important for this use case a list of QuestionResponseModel. These are Question\Answer pairs, which is where "Car traveling in bike lane" is.

Because of multiple objects and lists I created a MySQL database to store and easily search the data, with one table for Issues, Reporter (user object), Question (question, answer, issue id), and Request Type (currently only Traffic Issues). This allows easy string contains search on either Issue.description or Question.answer for mis-categorized items.


warnings: I have not tested this out with any other cities, or categories of issues. There are likely other fields in other categories or cities that would break the code.

Endpoints:
/getTrafficIssuesFromApi/ - Call the SeeClickFix Issues endpoint and collect all Detroit "Traffic Complaints" issues and save to MySQL database
/writeLocalDatabaseToCSV/ - Export MySQL database to CSV files for each table (to export or share)

To Run:
Create or connect to MySQL database and update application.properties connection info
gradle build
boot run!

