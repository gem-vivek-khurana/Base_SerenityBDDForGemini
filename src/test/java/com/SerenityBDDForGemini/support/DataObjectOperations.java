package com.SerenityBDDForGemini.support;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import net.serenitybdd.core.Serenity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class DataObjectOperations {
    final Faker faker = new Faker(new Locale("en-US"));

    public String transformDataValue(String value) {
        if (value.contains("`$`") || value.contains("`$") || value.contains("$`")) {
            value = value.replace("`$", "$").replace("$`", "$");
            return value;
        }
        String[] arr = value.split("\\$");
        for (int i = 0; i < arr.length; i++) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime previous = LocalDateTime.of(transformDateValue("t-1"), transformTimeValue("n"));
            DateTimeFormatter dtf;
            switch (arr[i]) {
                case "CURRENT_DATE" -> arr[i] = DateTimeFormatters.UI_DTF.getDtf().format(now);
                case "CURRENT_DATE_WITH_HYPHEN" -> arr[i] = DateTimeFormatters.HYPHEN_DTF.getDtf().format(now);
                case "CURRENT_DATE_FOR_NAME" ->
                        arr[i] = DateTimeFormatters.NAME_DTF.getDtf().format(now);
                case "CURRENT_DATE_TIME" -> {
                    dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm");
                    arr[i] = dtf.format(now);
                }
                case "CURRENT_DATE_WITH_SHORT_YEAR" -> arr[i] = DateTimeFormatters.SHORT_YEAR_DTF.getDtf().format(now);
                case "PREVIOUS_DATE" -> arr[i] = DateTimeFormatters.UI_DTF.getDtf().format(previous);
                case "PREVIOUS_DATE_WITH_HYPHEN" -> arr[i] = DateTimeFormatters.HYPHEN_DTF.getDtf().format(previous);
                case "PREVIOUS_DATE_FOR_NAME" ->
                        arr[i] = DateTimeFormatters.NAME_DTF.getDtf().format(previous);
                case "PREVIOUS_DATE_TIME" -> {
                    dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm");
                    arr[i] = dtf.format(previous);
                }
                case "PREVIOUS_DATE_WITH_SHORT_YEAR" -> arr[i] = DateTimeFormatters.SHORT_YEAR_DTF.getDtf()
                        .format(previous);
                case "RANDOM_MOBILE_NUMBER" -> arr[i] = String.valueOf(
                        (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L);
                case "SESSION_VARIABLE" -> {
                    Class<?> sessionVariableClass = Serenity.sessionVariableCalled(arr[i + 1].strip()).getClass();
                    if (sessionVariableClass.equals(Integer.class)) {
                        int sessionValue = Serenity.sessionVariableCalled(arr[i + 1].strip());
                        arr[i] = Integer.toString(sessionValue);
                    } else if (sessionVariableClass.equals(Long.class)) {
                        long sessionValue = Serenity.sessionVariableCalled(arr[i + 1].strip());
                        arr[i] = Long.toString(sessionValue);
                    } else if (sessionVariableClass.equals(Double.class)) {
                        double sessionValue = Serenity.sessionVariableCalled(arr[i + 1].strip());
                        arr[i] = Double.toString(sessionValue);
                    } else if (sessionVariableClass.equals(String.class)) {
                        arr[i] = Serenity.sessionVariableCalled(arr[i + 1].strip());
                    }
                    arr[i + 1] = "";
                }
                case "FAKE_DATA" -> {
                    HashMap<String, String> fakeData = generateFakeData();
                    arr[i] = fakeData.get(arr[i + 1].trim());
                    Serenity.setSessionVariable(arr[i + 1]).to(fakeData.get(arr[i + 1].trim()));
                    arr[i + 1] = "";
                }
                case "CURRENT_MONTH_NUMBER" -> {
                    int currMonth = now.getMonthValue();
                    arr[i] = currMonth < 10 ? "0" + currMonth : Integer.toString(currMonth);
                }
                case "CURRENT_MONTH_NAME" -> arr[i] = WordUtils.capitalizeFully(now.getMonth().name());
            }
        }
        return StringUtils.join(arr);
    }

    /**
     * Returns the date in accordance with the value in #{@link LocalDate} format
     * @param value t, t+1, t-1, t+2m, t-2m, t+2y, t-2y, w
     * @return #@{@link LocalDate}
     */
    public LocalDate transformDateValue(String value) {
        if (value.equals("t")) return LocalDate.now();
        LocalDate currentWeekFirstDay = null;
        if (value.equals("w")) {
            currentWeekFirstDay = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            return currentWeekFirstDay;
        }
        // Deducing if days, months or years to add or subtract
        boolean toAdd = value.contains("+");
        boolean toSubtract = value.contains("-");
        LocalDate finalValue = LocalDate.now();

        if (value.startsWith("t")) {
            String[] arr = value.contains("+") ? value.split("\\+") : value.split("-");
            try {
                arr[1] = arr[1].contains("&dtf=") ? arr[1].split("&dtf=")[0] : arr[1];
            } catch (ArrayIndexOutOfBoundsException ignored) {}
            if (toAdd) {
                if (arr[1].toLowerCase().contains("m")) {
                    finalValue = LocalDate.now().plusMonths(Integer.parseInt(arr[1].toLowerCase().split("m")[0]));
                } else if (arr[1].toLowerCase().contains("y")) {
                    finalValue = LocalDate.now().plusYears(Integer.parseInt(arr[1].toLowerCase().split("y")[0]));
                } else {
                    finalValue = LocalDate.now().plusDays(Integer.parseInt(arr[1]));
                }
            } else if(toSubtract) {
                if (arr[1].toLowerCase().contains("m")) {
                    finalValue = LocalDate.now().minusMonths(Integer.parseInt(arr[1].toLowerCase().split("m")[0]));
                } else if (arr[1].toLowerCase().contains("y")) {
                    finalValue = LocalDate.now().minusYears(Integer.parseInt(arr[1].toLowerCase().split("y")[0]));
                } else {
                    finalValue = LocalDate.now().minusDays(Integer.parseInt(arr[1]));
                }
            }
        }
        if (value.startsWith("w")) {
            if (value.contains("t")) {
                String[] weekAndDay = value.split("/");
                String[] weekAndAppendages = weekAndDay[0].contains("-") ? weekAndDay[0].split("-") :
                        weekAndDay[0].split("\\+");

                boolean weekToAdd = weekAndDay[0].contains("+"); boolean weekToSubtract = weekAndDay[0].contains("-");

                if (weekToAdd) {
                    finalValue = LocalDate.now().plusWeeks(Integer.parseInt(weekAndAppendages[1]))
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                } else if (weekToSubtract) {
                    finalValue = LocalDate.now().minusWeeks(Integer.parseInt(weekAndAppendages[1]))
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                }

                String[] dayAndAppendages = weekAndDay[1].contains("-") ? weekAndDay[1].split("-") :
                        weekAndDay[1].split("\\+");

                boolean dayToAdd = weekAndDay[1].contains("+"); boolean dayToSubtract = weekAndDay[1].contains("-");

                if (dayToAdd) finalValue = finalValue.plusDays(Integer.parseInt(dayAndAppendages[1]));
                else if (dayToSubtract) finalValue = finalValue.minusDays(Integer.parseInt(dayAndAppendages[1]));
            } else {
                String[] weekAndAppendages = value.contains("-") ? value.split("-") : value.split("\\+");

                boolean weekToAdd = value.contains("+"); boolean weekToSubtract = value.contains("-");

                if (weekToAdd) {
                    finalValue = LocalDate.now().plusWeeks(Integer.parseInt(weekAndAppendages[1]))
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                } else if (weekToSubtract) {
                    finalValue = LocalDate.now().minusWeeks(Integer.parseInt(weekAndAppendages[1]))
                            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                }
            }
        }
        return finalValue;
    }

    public String transformDateValue(String value, DateTimeFormatter dtf) {
        return dtf.format(transformDateValue(value));
    }

    public String transformUIDateValue(String value) {
        return transformDateValue(value, DateTimeFormatters.UI_DTF.getDtf());
    }

    public LocalTime transformTimeValue(String value) {
        if (value.equals("n")) {
            return LocalTime.now();
        }
        String[] arr = value.contains("+") ? value.split("\\+") : value.split("-");

        // Deducing if days, months or years to add or subtract
        boolean toAdd = value.contains("+");
        boolean toSubtract = value.contains("-");
        LocalTime finalValue = null;

        if (toAdd) {
            if (arr[1].toLowerCase().contains("m")) {
                finalValue = LocalTime.now().plusMinutes(Integer.parseInt(arr[1].toLowerCase().split("m")[0]));
            } else if (arr[1].toLowerCase().contains("h")) {
                finalValue = LocalTime.now().plusHours(Integer.parseInt(arr[1].toLowerCase().split("h")[0]));
            }
        } else if(toSubtract) {
            if (arr[1].toLowerCase().contains("m")) {
                finalValue = LocalTime.now().minusMinutes(Integer.parseInt(arr[1].toLowerCase().split("m")[0]));
            } else if (arr[1].toLowerCase().contains("h")) {
                finalValue = LocalTime.now().minusHours(Integer.parseInt(arr[1].toLowerCase().split("h")[0]));
            }
        }
        return finalValue;
    }

    public String extractDtfAndTransformDateValue(String value) {
        if (!value.contains("&dtf=")) throw new RuntimeException("This method is only supported when dtf value is" +
                " provided in the value.");
        String[] breakDtf = value.split("&dtf=");
        return transformDateValue(breakDtf[0], DataObjectOperations.DateTimeFormatters.getDtf(breakDtf[1]));
    }

    public String extractDtfAndTransformTimeValue(String value) {
        if (!value.contains("&dtf=")) throw new RuntimeException("This method is only supported when dtf value is" +
                " provided in the value.");
        String[] breakDtf = value.split("&dtf=");
        return transformTimeValue(breakDtf[0], DataObjectOperations.DateTimeFormatters.getDtf(breakDtf[1]));
    }

    public String transformTimeValue(String value, DateTimeFormatter dtf) {
        return dtf.format(transformTimeValue(value));
    }

    public HashMap<String, String> generateFakeData() {
        HashMap<String, String> fakeData = new HashMap<>();
        Address address = faker.address();
        String zipCode = address.zipCode();
        zipCode = zipCode.contains("-") ? zipCode.split("-")[0] : zipCode;
        fakeData.put("Address Line 1", address.streetAddress());
        fakeData.put("Street", address.streetName());
        fakeData.put("City", address.cityName());
        fakeData.put("State", address.state());
        fakeData.put("Latitude", address.latitude());
        fakeData.put("Longitude", address.longitude());
        try { fakeData.put("County", address.countyByZipCode(zipCode)); } catch (Exception ignored) {}
        fakeData.put("Country", "USA");
        fakeData.put("Zip Code", zipCode);
        fakeData.put("Email", faker.bothify("????##@yopmail.com"));
        fakeData.put("Birth Date", transformUIDateValue("t-" + faker.number().numberBetween(25, 80) + "y"));
        fakeData.put("SSN", faker.idNumber().ssnValid());
        fakeData.put("Work Phone", faker.phoneNumber().phoneNumber());
        fakeData.put("Home Phone", faker.phoneNumber().phoneNumber());
        fakeData.put("Mobile", faker.phoneNumber().cellPhone());
        fakeData.put("Contact Phone", faker.phoneNumber().subscriberNumber());
        fakeData.put("Company Name", faker.company().name());
        fakeData.put("Community Name", faker.harryPotter().house());
        return fakeData;
    }

    public List<Map<String, String>> transformDataTable(List<Map<String, String>> dataTable) {
        dataTable = createWritableCopy(dataTable);
        dataTable.forEach(dataRow ->
                dataRow.keySet().forEach(key -> {
                    if (dataRow.get(key).contains("$")) {
                        dataRow.put(key, transformDataValue(dataRow.get(key)));
                    }
                })
        );
        return dataTable;
    }

    public List<Map<String, String>> createWritableCopy(List<Map<String, String>> dataTable) {
        return dataTable.stream().map(HashMap::new).collect(Collectors.toList());
    }

    public enum DateTimeFormatters {
        UI_DTF(DateTimeFormatter.ofPattern("MM/dd/yyyy")), NAME_DTF(DateTimeFormatter.ofPattern("MMddyyyy")),
        MONTH_DATE(DateTimeFormatter.ofPattern("MMMM d, yyyy")), DATE_MONTH(DateTimeFormatter.ofPattern("d MMMM yyyy")),
        MONTH_YEAR_CAPS(DateTimeFormatter.ofPattern("MMM yyyy")),
        HYPHEN_DTF(DateTimeFormatter.ofPattern("MM-dd-yy")), MONTH_WITH_PADDED_DATE(DateTimeFormatter.ofPattern("MMM dd")),
        PADDED_DATE_AND_YEAR_CAPS(DateTimeFormatter.ofPattern("dd,yyyy")),
        API_DTF(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)),
        SHORT_YEAR_DTF(DateTimeFormatter.ofPattern("MM/dd/yy")), UI_TIME_DTF(DateTimeFormatter.ofPattern("hh:mm a")),
        NON_PADDED_UI_TIME_DTF(DateTimeFormatter.ofPattern("h:mm a")),
        HOUR_MINUTE_SECONDS_DTF(DateTimeFormatter.ofPattern("HH:mm:ss"));

        private final DateTimeFormatter dtf;

        public DateTimeFormatter getDtf() {
            return this.dtf;
        }

        public static DateTimeFormatter getDtf(String dtfName) {
            switch (dtfName.toLowerCase()) {
                case "ui_dtf" -> {
                    return UI_DTF.getDtf();
                }
                case "name_dtf" -> {
                    return NAME_DTF.getDtf();
                }
                case "hyphen_dtf" -> {
                    return HYPHEN_DTF.getDtf();
                }
                case "api_dtf" -> {
                    return API_DTF.getDtf();
                }
                case "ui_time_dtf" -> {
                    return UI_TIME_DTF.getDtf();
                }
                case "hour_minute_seconds_dtf" -> {
                    return HOUR_MINUTE_SECONDS_DTF.getDtf();
                }
                case "short_year_dtf" -> {
                    return SHORT_YEAR_DTF.getDtf();
                }
                case "month_year_caps" -> {
                    return MONTH_YEAR_CAPS.getDtf();
                }
                case "month_date" -> {
                    return MONTH_DATE.getDtf();
                }
                case "date_month" -> {
                    return DATE_MONTH.getDtf();
                }
                case "month_with_padded_date" -> {
                    return MONTH_WITH_PADDED_DATE.getDtf();
                }
                case "padded_date_and_year_caps" -> {
                    return PADDED_DATE_AND_YEAR_CAPS.getDtf();
                }
                case "non_padded_ui_time_dtf" -> {
                    return NON_PADDED_UI_TIME_DTF.getDtf();
                }
            }
            throw new RuntimeException("Unsupported DTF value: " + dtfName + ". Please add to #{DateTimeFormatters} " +
                    "enum on #{DataObjectOperations} class");
        }

        public static ArrayList<DateTimeFormatter> getFormattersList() {
            return (ArrayList<DateTimeFormatter>) Arrays.stream(DateTimeFormatters.values())
                    .map(DateTimeFormatters::getDtf).collect(Collectors.toList());
        }

        DateTimeFormatters(DateTimeFormatter dtf) {
            this.dtf = dtf;
        }
    }
}