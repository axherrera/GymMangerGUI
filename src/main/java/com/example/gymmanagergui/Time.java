package com.example.gymmanagergui;

/**
 * An enumeration to be used by the ClassType enumeration
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public enum Time {
    MORNING("09:30"),
    AFTERNOON("14:00"),
    EVENING("18:30"),
    NA("");

    private final String time;

    Time(String time){
        this.time = time;
    }

    /**
     * Overrides the toString method to print time as a string
     * @return
     */
    @Override
    public String toString(){
        return time;
    }

    /**
     * Identifies the Time object corresponding to the provided string argument
     * @param time time
     * @return returns the Time object corresponding to the argument
     */
    public static Time getTime(String time){
        switch(time.toLowerCase()){
            case "morning":
                return Time.MORNING;
            case "afternoon":
                return Time.AFTERNOON;
            case "evening":
                return Time.EVENING;
            default:
                return Time.NA;
        }
    }
}
