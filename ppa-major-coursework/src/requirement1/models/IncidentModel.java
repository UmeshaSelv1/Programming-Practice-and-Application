package requirement1.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.ripley.Incident;

/**
 * IncidentModel extends the Incident class from Ripley API to provide better understanding of that object.
 * Parses data such as time, duration, isHoax flag etc.
 */
public class IncidentModel extends Incident{

    private static Pattern pattern = Pattern.compile( "(([H|h][O|o][A|a][X|x](/?+)?))" );

    private boolean isHoax;
    private EState state;
    private static String date = "\\b([0-3]?[0-9])-([0-2]?[0-9])-([1-2][0-9]{3})\\b";
    private static String time = date + "\\b([01]?[0-9]|2[0-3]):[0-5][0-9]\\b";
    private Integer durationMinutes;
    private LocalDateTime dateTime;


    public IncidentModel(Incident dummy) {
        super(dummy.getIncidentID(),
                dummy.getDateAndTime(),
                dummy.getCity(),
                dummy.getState(),
                dummy.getShape(),
                dummy.getDuration(),
                dummy.getSummary(),
                dummy.getPosted());

        // Set is hoax
        Matcher matcher = pattern.matcher(this.getSummary());
        this.isHoax = matcher.find();


        // Set state
        if (this.getState().length() == 2) {
            try {
                state = EState.valueOf(this.getState());
            }
            catch(Exception ex) {
                // this means no state is found
                state = null;
            }
        }

        // Set date
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dateTime = LocalDateTime.parse(this.getDateAndTime(), df);

        this.durationMinutes = -1;

        // handle e.g. 1-5 minutes
        Pattern patternDurationDash = Pattern.compile("([0-9]+)-([0-9]+)");

        // handle e.g. "123124212120  second", "00:02 seconds"
        Pattern patternSecondsMinutesHours = Pattern.compile("\\b((([0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}|[0-9]{1,2}:[0-9]{1,2}))|([0-9]+)[ ]*(seconds?|minutes?|hours?))\\b");

        // handle e.g. "1 1/2 hrs", "2/2 minutes" etc
        Pattern patternSlash = Pattern.compile("([0-9] ?)?([0-9]+\\/[0-9]+) *((minutes|min)|(hours|hrs))");

        Matcher matcherDuration = patternDurationDash.matcher(this.getDuration());
        Matcher matcherSMH = patternSecondsMinutesHours.matcher(this.getDuration());
        Matcher matcherSlash = patternSlash.matcher(this.getDuration());

        if (matcherDuration.find()) {
            double modifier = 1;
            if (this.getDuration().toLowerCase().contains("hour")) {
                modifier = 60;
            }
            else if (this.getDuration().toLowerCase().contains("second")){
                modifier = 0.6;
            }
            double doubleValue = (double)((Integer.parseInt(matcherDuration.group(1)) +  Integer.parseInt(matcherDuration.group(2))) / 2);
            this.durationMinutes = (int) (doubleValue * modifier);
        }else if (matcherSMH.find()) {
            if (matcherSMH.groupCount() >= 4 && matcherSMH.group(4) != null) {
                String secMinHour = matcherSMH.group().toLowerCase();
                if (secMinHour.contains("second")) {
                    int value = Integer.parseInt(matcherSMH.group(4));
                    this.durationMinutes = value / 60;
                }
                else if (secMinHour.contains("minute")) {
                    this.durationMinutes = Integer.parseInt(matcherSMH.group(4));
                }
                else if (secMinHour.contains("hour")) {
                    int value = Integer.parseInt(matcherSMH.group(4));
                    this.durationMinutes = value * 60;
                }
            }
            else if (matcherSMH.groupCount() >= 3 && matcherSMH.group(3) != null){
                String textInDateFormat = matcherSMH.group(3);
                String[] values = textInDateFormat.split(":");
                if (values.length == 3) {
                    int hours = Integer.parseInt(values[0]);
                    int minutes = Integer.parseInt(values[1]);
                    int seconds = Integer.parseInt(values[2]);
                    this.durationMinutes = hours * 60 + minutes + seconds / 60;
                }
                else {
                    int minutes = Integer.parseInt(values[0]);
                    int seconds = Integer.parseInt(values[1]);
                    this.durationMinutes = minutes + seconds / 60;
                }
            }
        }

        // handle e.g. "1 1/2 min" or "1 1/2 hours"
        if (matcherSlash.find()){
            for (int i = 0; i < matcherSlash.groupCount(); i++) {
                System.out.println(i + "- " + matcherSlash.group(i));
            }
            System.out.println();
            boolean isMinutes = matcherSlash.group(4) != null;
            Integer wholeNumber = matcherSlash.group(1) != null ? Integer.parseInt(matcherSlash.group(1).trim()) : 0;
            String group = matcherSlash.group(1);
            if (group != null) {
                String[] split = group.split("/");
                Double modifier = (split.length >= 2 && split[0] != null && split[1] != null) ? (Double.parseDouble(split[0]) / Double.parseDouble(split[1])) : 0;
                this.durationMinutes = (int) (isMinutes ? modifier : modifier * 60) + wholeNumber * 60;
            }

        }
    }

    /**
     * Flag showing if current object is a hoax
     */
    public boolean isHoax() {
        return isHoax;
    }

    /**
     * Current state of the incident
     */
    public EState getEState() {
        return state;
    }

    /**
     * Duration in minutes, parsed from the original data. Could be -1 if it's not specified
     */
    public Integer getDurationMinutes(){

        return durationMinutes;
    }

    /**
     * The date and time of that incident, parsed from the original data.
     */
    public LocalDateTime getLocalDate() {
        return this.dateTime;
    }


    @Override
    public String toString(){
        String timeStr = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String duration = this.durationMinutes == 0 ? "less than 1 minute" : String.valueOf(this.durationMinutes) + " minutes";
        return "Time: " + timeStr + " City: " + this.getCity() + " Shape: " + this.getShape() + " Duration: " + duration + " Posted: " + this.getPosted();

    }
}
