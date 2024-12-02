# Meal rest controller curl:
* GetAll GET http://localhost:8080/topjava/rest/meals/
* Create new POST http://localhost:8080/topjava/rest/meals/
content
```
{"id":100012,"dateTime":"2024-01-31T20:00:00","description":"restУжин","calories":1500}
```
* Get created GET http://localhost:8080/topjava/rest/meals/100012
* Update created PUT http://localhost:8080/topjava/rest/meals/100012
content 
```
{
  "dateTime": "2024-01-31T20:00:00",
  "description": "restУжин updated",
  "calories": 200
}
```
* Delete created DELETE http://localhost:8080/topjava/rest/meals/100012
* filter with DateTime
  * empty params GET http://localhost:8080/topjava/rest/meals/filtercustomdatetime?startDateTime=&endDateTime=
  * only time GET http://localhost:8080/topjava/rest/meals/filtercustomdatetime?startDateTime=10%3A00&endDateTime=14%3A00
  * date and time GET http://localhost:8080/topjava/rest/meals/filtercustomdatetime?startDateTime=2020-01-30T10%3A00%3A00&endDateTime=2020-01-30T14%3A00%3A00
  * only date GET http://localhost:8080/topjava/rest/meals/filtercustomdatetime?startDateTime=2020-01-30&endDateTime=2020-01-30
