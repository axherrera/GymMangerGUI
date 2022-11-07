package com.example.gymmanagergui;

/**
 * An enum Location used for the location of gyms
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public enum Location {
    BRIDGEWATER ("Bridgewater", "08807", "Somerset County", "3"),
    EDISON("Edison", "08837", "Middlesex County", "1"),
    FRANKLIN("Franklin", "08873", "Somerset County", "4"),
    PISCATAWAY("Piscataway", "08854", "Middlesex County", "2"),
    SOMERVILLE("Somerville", "08876", "Somerset County", "5"),
    NA("NA", "NA", "NA", "6");
    private final String township;
    private final String zipcode;
    private final String county;
    private final int rank;
    /**
     * To get the township
     * @return township
     */
    public String getTownship() {
        return township;
    }
    /**
     * To ge the zipcode
     * @return zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * To get the county
     * @return county
     */
    public String getCounty() {
        return county;
    }

    /**
     * To get the rank
     * @return rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * The constructor of the enum Location
     * @param township township
     * @param zipcode zipcode
     * @param county county
     * @param rank rank
     */
    Location(String township, String zipcode, String county, String rank){
        this.township = township;
        this.zipcode = zipcode;
        this.county = county;
        this.rank = Integer.parseInt(rank);
    }

    /**
     * Overrides the toString method
     * @return township, zipcode and county
     */
    @Override
    public String toString() {
        return township + ", " + zipcode + ", "+ county;
    }

    /**
     * Checks if input string corresponds to a valid location
     * If input is not valid, returns instance of NA location
     * @param location locations
     * @return an instance of the Location enum corresponding to class
     */
    public static Location idLocation(String location) {
        location = location.toLowerCase();
        switch (location) {
            case "piscataway":
                return Location.PISCATAWAY;
            case "bridgewater":
                return Location.BRIDGEWATER;
            case "edison":
                return Location.EDISON;
            case "franklin":
                return Location.FRANKLIN;
            case "somerville":
                return Location.SOMERVILLE;
            default:
                return Location.NA;
        }
    }
}