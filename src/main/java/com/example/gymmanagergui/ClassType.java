package com.example.gymmanagergui;

/**
 * Enum class for ClassType which holds information about each fitness class
 * Includes class name, instructor, time, and index
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public enum ClassType {
    PILATES("Pilates"),
    SPINNING("Spinning"),
    CARDIO("Cardio"),
    NA("NA");

    private String name;
    /**
     * This is a constructor for the enum ClassType
     *
     * @param classtype the class type
     * */
    ClassType(String classtype) {
        this.name = classtype;
    }

    /**
     * It gets the fitness class name
     * @return returns name of fitness class
     */
    public String getName(){
        return name;
    }


    /**
     * it accepts a string and returns the fitness class type corresponding to that string
     * @param classtype it is the class type
     * @return the class type of the gym fitness, if string is not valid location, returns NA
     */
    public static ClassType idClassType(String classtype){
        switch (classtype.toLowerCase()){
            case "pilates":
                return ClassType.PILATES;
            case "spinning":
                return ClassType.SPINNING;
            case "cardio":
                return ClassType.CARDIO;
            default:
                return ClassType.NA;
        }
    }
}
