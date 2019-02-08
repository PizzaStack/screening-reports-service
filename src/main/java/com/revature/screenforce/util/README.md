#### package com.revature.screenforce.util

# public class Time

### Convenience class to filter reports based on number of past weeks, months, years

#### getWeekToDate

##### public java.time.LocalDate getWeekToDate(java.time.LocalDate from, int numWeeks)

Parameters:

*from* - The end date of the period for reporting aggregate metrics

*numWeeks* - The number of weeks

*Returns*:
The start date


#### getMonthToDate

##### public java.time.LocalDate getMonthToDate(java.time.LocalDate from, int numMonths)

Parameters:

*from* - The end date of the period for reporting aggregate metrics

*numMonths* - The number of months

*Returns*:
The start date


#### getYearToDate

##### public java.time.LocalDate getYearToDate(java.time.LocalDate from)

Parameters:

*from* - The end date of the period for reporting aggregate metrics

*Returns*:
The start date one year prior to from


**Useful Links** 
- [Java LocalDate Tutorial](http://tutorials.jenkov.com/java-date-time/localdate.html)
- [Oracle Java API: LocalDate](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html)
