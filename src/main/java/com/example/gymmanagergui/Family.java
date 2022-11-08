package com.example.gymmanagergui;

import java.text.DecimalFormat;

/**
 * A class Family is a child of the Member class and represents a Family membership
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class Family extends Member{

    protected final double FAMILY_FEE_PER_MONTH = 59.99;
    protected int guestPasses;

    /**
     *Constructor for Family object
     * @param fname first name
     * @param lname last name
     * @param dob date of birth
     * @param location location
     */
    public Family(String fname, String lname, Date dob, Location location) {
        super(fname, lname, dob, location);
        this.guestPasses=1;
    }

    /**
     * Constructor for Family object
     * @param fname first name
     * @param lname last name
     * @param dob date of birth
     * @param expire expiration date
     * @param location location
     */
    public Family(String fname, String lname, Date dob, Date expire, Location location) {
        super(fname, lname, dob, expire, location);
        this.guestPasses=1;
    }

    /**
     * Overrides MemberShipFee() from member
     * @return membership fee for Family
     */
    @Override
    public String MemberShipFee(){
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(this.MEMBER_ONE_TIME_FEE + FAMILY_FEE_PER_MONTH * 3);
    }

    /**
     * Checks if member has available guest passes
     * @return boolean
     */
    public boolean hasGuestPasses() {
        return guestPasses != 0;
    }

    /**
     * Will check if a guest pass is able to be used. If so decrement guest passes
     * @return return true if guest pass used, false if there are no more available guest passes
     */
    public boolean useGuestPass(){
        if(guestPasses==0)
            return false;
        guestPasses--;
        return true;
    }

    /**
     * Will increment the instance variable guestPasses
     */
    public void returnGuestPass(){
        guestPasses++;
    }

    /**
     * Will override toString method
     * @return return information about Family member
     */
    @Override
    public String toString() {
        return (
                this.fname + ", " +
                        this.lname +
                        " DOB: " + this.dob.toString() +
                        ", Membership expires: " + this.expire.toString() + ", " +
                        "Location: " + this.location.toString()+
                        ", (Family) " +
                        "Guest-pass remaining: " + guestPasses
        );
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
        Family guest1 = new Family("Kaleb", "Yonas", date1, location1);
        Family guest2 = new Family("John", "Mat", date2, location5);
//        System.out.println(guest2.getFname());

        Member family1 = new Family("Gibre", "Lukas ", date1, date2, location1);
        Member family2 = new Family("Tomas", "Mike ", date1, date2, location2);
        Member family3 = new Family("Micheal", "Tomas ", date2, date1, location3);
        Member family4 = new Family("Mike", "Tato ", date1, date2, location4);
        Member family5 = new Family("Gebre", "Lakas ", date1, date2, location5);
        Member family6 = new Family("nani", "kibret", date3, date2, location5);
//        System.out.println(family6.MemberShipFee());

//        System.out.println(family1);
//        System.out.println(family4.toString());
//        System.out.println(date1.compareTo(date2));
//        System.out.println(family1.compareTo(family2));

        MemberDatabase md = new MemberDatabase();

        md.add(family1);
        md.add(family2);
        md.add(family3);
        md.add(family4);
        md.add(family5);
        md.add(family6);
      
        md.print();
//        md.printByName();
//        md.printByCounty();
//        md.printByExpirationDate();
    }
}
