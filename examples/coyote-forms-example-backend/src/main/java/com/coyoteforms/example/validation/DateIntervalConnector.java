package com.coyoteforms.example.validation;

import com.coyoteforms.example.dto.DateIntervalDto;
import com.coyoteforms.validator.Connector;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DateIntervalConnector implements Connector<DateIntervalDto> {

    private Clock clock;

    public DateIntervalConnector() {
        this.clock = Clock.systemUTC();
    }

    @Override
    public Map<String, String> collectInputValues(DateIntervalDto interval) {
        Map<String, String> inputValues = new HashMap<>();

        // add field inputs to the map in order to be validated, use empty string if value is not present
        inputValues.put("startDate", interval.getStartDate() == null ? "" : interval.getStartDate().toString());
        inputValues.put("endDate", interval.getEndDate() == null ? "" : interval.getEndDate().toString());

        // add the custom inputs to the map if it can be computed, otherwise leave it out
        if (interval.getStartDate() != null) {
            if (LocalDate.now(clock).plusDays(6).isBefore(interval.getStartDate())) {
                inputValues.put("daysInAdvanceAtLeastOneWeek", "true");
            }
            if (LocalDate.now(clock).plusDays(13).isBefore(interval.getStartDate())) {
                inputValues.put("daysInAdvanceAtLeasTwoWeeks", "true");
            }
        }
        if (interval.getStartDate() != null && interval.getEndDate() != null) {
            boolean endDateIsAfterStart = interval.getEndDate().isAfter(interval.getStartDate());
            inputValues.put("endDateIsAfterStart", Boolean.toString(endDateIsAfterStart));

            if (endDateIsAfterStart) {
                long workDayCount = calculateWorkDaysCountBetween(interval.getStartDate(), interval.getEndDate());
                inputValues.put("intervalLength", Long.toString(workDayCount));
                if (workDayCount > 3) {
                    inputValues.put("intervalLengthIsMoreThan3Days", "true");
                }
            }
        }
        return inputValues;
    }

    private long calculateWorkDaysCountBetween(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate.plusDays(1))
                .filter(this::isWorkday)
                .count();
    }

    private boolean isWorkday(LocalDate date) {
        return !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

}
