# Improve Detroit Data Pull

I created this as a solution to download 'Car traveling in bike lane' issues reported in Detroit's ImproveDetroit app. The available ARCGIS dataset did not have that column, so I went up to the SeeClickFix database to get all the columns.

### Endpoints:

`/getTrafficIssuesFromApi` Call the SeeClickFix Issues endpoint and collect Detroit issues and save to MySQL database
- String `requestTypes`: comma separted list of request type integers. empty will get all types
- OffsetDateTime `afterTimestamp`: get issues with created_at EQUAL TO or greater than inputted time. empty will get all records. example formats: 2024-09-01T10:51:31 or with timezone: 2024-09-01T10:51:31-04:00

`/getIssueModelFromLocalDatabase` used if you want to pull data from the database into java

`/getIssueDtoFromLocalDatabase` used if you want to pull data from the database into java

### To Run:
- Create or connect to MySQL database and update application.properties connection info
- gradle build
- boot run!
- hit endpoint /getTrafficIssuesFromApi

### Background Info
This Java SpringBoot code will hit the [SeeClickFix issue API](https://dev.seeclickfix.com/v2/issues/) pull data into a local mysql database.
Issues endpoint is maximum 100 issues per page and SeeClickFix rate limits at 20 calls per minute, a max of 2,000 per min.


The response is a `SeeClickFixResponse` object, containing a list of `IssueResponseModel` (the data we want) and a `MetadataResponseModel` (containing pagination information). Inside `IssueResponseModel` data fields and a list of `QuestionResponseModel`. These are Question\Answer pairs, which is where "Car traveling in bike lane" is.

Because of multiple objects and lists I created a MySQL database to store and easily search the data, with one table for Issues, Reporter (user object), Question (question, answer, issue id), and Request Type (currently only Traffic Issues). This allows easy string contains search on either Issue.description or Question.answer for mis-categorized items.

warnings: I have not tested this out with any other cities, or categories of issues. There are likely other fields in other categories or cities that would break the code.

### SeeClickFix Issues API endpoint input parameters used in this project:
- `place_url=detroit` return issues for this geoarea, look up your place with lat+lng on this endpoint and get the `url_name` https://seeclickfix.com/api/v2/places?lat=42.36&lng=-83.06 
- `per_page=100` items per page
- `sort_direction=ASC` sort order
- `sort=created_at` sort column
- `request_types=requestTypes` 
- `details=true` To get "Questions" you have to set "details"=true and add questions to the field list
- `page=currentPage` used for iterating over all possible pages
- `status=open,acknowledged,closed,archived` these are all possible statuses
- `fields[issue]=id,status,summary,description,lat,lng,created_at,acknowledged_at,closed_at,request_type,questions,reporter` which fields you want to be returned
- `after=afterTimestamp` only get items created after this timestamp 
