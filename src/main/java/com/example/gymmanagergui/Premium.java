package com.example.gymmanagergui;

import java.text.DecimalFormat;

/**
 * A class Family is a child of the Family class and represents a Premium membership
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class Premium extends Family{

    /**
     * Constructor for Premium object
     * @param fname first name
     * @param lname last name
     * @param dob date of birth
     * @param location location
     */
    public Premium(String fname, String lname, Date dob, Location location) {
        super(fname, lname, dob, location);
        this.guestPasses = 3;
    }

    /**
     * Overrides the toString() method
     * @return returns information about Premium object
     */
    @Override
    public String toString() {
        return (
                this.fname + ", " +
                        this.lname +
                        " DOB: " + this.dob.toString() +
                        ", Membership expires: " + this.expire.toString() + ", " +
                        "Location: " + this.location.toString()+
                        ", (Premium) " +
                        "Guest-pass remaining: " + guestPasses
        );
    }

    /**
     * Constructor for Premium object, includes expiration date setting
     * @param fname first name
     * @param lname last name
     * @param dob date of birth
     * @param expire expiration date
     * @param location location
     */
    public Premium(String fname, String lname, Date dob, Date expire, Location location) {
        super(fname, lname, dob, expire, location);
        this.guestPasses = 3;
    }

    /**
     * Override the MemberShipFee() method
     * @return will return a String corresponding to Premium membership fee
     */
    @Override
    public String MemberShipFee(){
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(this.FAMILY_FEE_PER_MONTH * 12 - this.FAMILY_FEE_PER_MONTH);
    }

    public static void main(String[] args) {
        Date date1 = new Date("01/02/2026");
        Date date2 = new Date("01/02/2022");
        Date date3 = new Date("01/02/2023");

        Location location3 = Location.BRIDGEWATER;
        Location location2 = Location.EDISON;
        Location location1 = Location.FRANKLIN;
        Location location4 = Location.PISCATAWAY;
        Location location5 = Location.SOMERVILLE;

        Premium guest1 = new Premium("Kaleb", "Yonas", date1, location1);
        Premium guest2 = new Premium("John", "Mat", date2, location5);
        Premium guest3 = new Premium("John", "Mat", date3, location3);
//        System.out.println(guest2.getLocation());

        Family premium1 = new Premium("Gibre", "Lukas ", date1, date2, location1);
        Family premium2 = new Premium("Tomas", "Mike ", date1, date2, location2);
        Family premium3 = new Premium("Micheal", "Tomas ", date2, date1, location3);
        Family premium4 = new Premium("Mike", "Tato ", date1, date2, location4);
        Family premium5 = new Premium("Gebre", "Lakas ", date1, date2, location5);
        Family premium6 = new Premium("nani", "kibret", date3, date2, location5);
//        System.out.println(premium6.MemberShipFee());
//
//        System.out.println(premium1);
//        System.out.println(premium4.toString());
//        System.out.println(date1.compareTo(date2));
//        System.out.println(premium1.compareTo(premium2));

        MemberDatabase md = new MemberDatabase();
        md.add(premium1);
        md.add(premium2);
        md.add(premium3);
        md.add(premium4);
        md.add(premium5);
        md.add(premium5);
//        md.printMemberShipFee();
//        md.printByName();
//        md.printByCounty();
//        md.printByExpirationDate();
    }
}
