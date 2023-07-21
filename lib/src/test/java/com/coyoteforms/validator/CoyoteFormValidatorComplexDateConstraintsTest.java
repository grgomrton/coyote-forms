package com.coyoteforms.validator;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CoyoteFormValidatorComplexDateConstraintsTest {

    private static String RULE_SET = "{" +
            "  \"constraints\": [" +
            "    {" +
            "      \"inputId\": \"intervalLength\"," +
            "      \"condition\": [ \"always\" ]," +
            "      \"permittedValues\": [ \".*\" ]" +
            "    }," +
            "  {" +
            "    \"inputId\": \"intervalLengthIsMoreThan3Days\"," +
            "    \"condition\": [ \"always\" ]," +
            "    \"permittedValues\": [ \".*\" ]" +
            "  }," +
            "  {" +
            "    \"inputId\": \"daysInAdvanceAtLeastOneWeek\"," +
            "    \"condition\": [ \"always\" ]," +
            "    \"permittedValues\": [ \".*\" ]" +
            "  }," +
            "  {" +
            "    \"inputId\": \"daysInAdvanceAtLeasTwoWeeks\"," +
            "    \"condition\": [ \"always\" ]," +
            "    \"permittedValues\": [ \".*\" ]" +
            "  }," +
            "    {" +
            "      \"inputId\": \"endDateIsAfterStart\"," +
            "      \"condition\": [ \"always\" ]," +
            "      \"permittedValues\": [ \".*\" ]" +
            "    }," +
            "    {" +
            "      \"inputId\": \"startDate\"," +
            "      \"condition\": [ \"intervalLength is 1\", \"daysInAdvanceAtLeastOneWeek is true\", \"endDateIsAfterStart is true\" ]," +
            "      \"permittedValues\": [ \".*\" ]," +
            "      \"helperText\": \"Up to 3 days the notification period is one week\"" +
            "    }," +
            "    {" +
            "      \"inputId\": \"startDate\"," +
            "      \"condition\": [ \"intervalLength is 2\", \"daysInAdvanceAtLeastOneWeek is true\", \"endDateIsAfterStart is true\" ]," +
            "      \"permittedValues\": [ \".*\" ]," +
            "      \"helperText\": \"Up to 3 days the notification period is one week\"" +
            "    }," +
            "  {" +
            "      \"inputId\": \"startDate\"," +
            "      \"condition\": [ \"intervalLength is 3\", \"daysInAdvanceAtLeastOneWeek is true\", \"endDateIsAfterStart is true\" ]," + // todo move to enddate rule
            "      \"permittedValues\": [ \".*\" ]," +
            "      \"helperText\": \"Up to 3 days the notification period is one week\"" +
            "    }," +
            "  {" +
            "      \"inputId\": \"startDate\"," +
            "      \"condition\": [ \"intervalLengthIsMoreThan3Days is true\", \"daysInAdvanceAtLeasTwoWeeks is true\", \"endDateIsAfterStart is true\" ]," +
            "      \"permittedValues\": [ \".*\" ]," +
            "      \"helperText\": \"More than 3 days leave must be entered two weeks prior\"" +
            "    }" +
            "  ]" +
            "}";

    private static Clock NOW;

    private static IntervalConnector connector;

    private static CoyoteFormValidator<Interval> validator;

    @BeforeAll
    public static void init() {
        NOW = Clock.fixed(Instant.parse("2023-07-21T12:00:00.00Z"), ZoneId.of("UTC"));
        connector = new IntervalConnector(NOW);
        validator = new CoyoteFormValidator<>(RULE_SET, connector);
    }

    @Data
    @Builder
    public static class Interval {

        private LocalDate startDate;

        private LocalDate endDate;

    }

    public static class IntervalConnector implements Connector<Interval> {

        private Clock testClock;

        public IntervalConnector(Clock testClock) {
            this.testClock = testClock; // so we can freeze time in the test
        }

        @Override
        public Map<String, String> collectInputValues(Interval interval) {
            Map<String, String> inputValues = new HashMap<>();
            if (interval.getStartDate() != null) {
                if (LocalDate.now(testClock).plusDays(6).isBefore(interval.getStartDate())) {
                    inputValues.put("daysInAdvanceAtLeastOneWeek", "true");
                }
                if (LocalDate.now(testClock).plusDays(13).isBefore(interval.getStartDate())) {
                    inputValues.put("daysInAdvanceAtLeasTwoWeeks", "true");
                }
            }
            if (interval.getStartDate() != null && interval.getEndDate() != null) {
                long workDayCount = calculateWorkDaysCountBetween(interval.getStartDate(), interval.getEndDate());
                inputValues.put("intervalLength", Long.toString(workDayCount));
                if (workDayCount > 3) {
                    inputValues.put("intervalLengthIsMoreThan3Days", "true");
                }
                inputValues.put("endDateIsAfterStart", Boolean.toString(interval.getEndDate().isAfter(interval.getStartDate())));
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

    @Test
    public void twoDaysVacatioForTheNextWeekCanBeAdded() {
        LocalDate startDate = LocalDate.parse("2023-07-28");
        LocalDate endDate = LocalDate.parse("2023-07-31");
        Interval request = Interval.builder().startDate(startDate).endDate(endDate).build();

        Map<String, Set<String>> invalidInputs = validator.validate(request);

        assertThat(invalidInputs).isEmpty();
    }

    @Test
    public void longerVacatioForTheNextWeekCannotBeAdded() {
        LocalDate startDate = LocalDate.parse("2023-07-28");
        LocalDate endDate = LocalDate.parse("2023-08-02");
        Interval request = Interval.builder().startDate(startDate).endDate(endDate).build();

        Map<String, Set<String>> invalidInputs = validator.validate(request);

        assertThat(invalidInputs.keySet()).containsExactlyInAnyOrder("startDate", "endDate");
    }

}
