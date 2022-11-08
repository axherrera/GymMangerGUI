package com.example.gymmanagergui;


import java.text.DecimalFormat;

/**
 * A class Member represents to the members of the fitness gym
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class Member implements Comparable<Member>{
    protected String fname;
    protected String lname;
    protected Date dob;
    protected Date expire;
    protected Location location;
    protected final double MEMBER_ONE_TIME_FEE = 29.99;
    private final double MEMBER_MONTHLY_FEE = 39.99;

    /**
     * The constructor of the Member class. Sets custom expiration date depending on membership type
     * @param fname first name
     * @param lname last name
     * @param dob date of birth
     * @param location location
     */
    public Member(String fname, String lname, Date dob, Location location){
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.location = location;
        this.expire = Date.genExpDate();
    }
    /**
     * The constructor of the Member class
     * @param fname first name
     * @param lname last name
     * @param dob date of birth
     * @param expire expiration date
     * @param location location
     */
    public Member(
            String fname,
            String lname,
            Date dob,
            Date expire,
            Location location
    ) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.expire = expire;
        this.location = location;
    }

    /**
     * To get the first name
     * @return first name
     */
    public String getFname() {
        return this.fname;
    }

    /**
     * To get the last name
     * @return last name
     */
    public String getLname() {
        return this.lname;
    }

    /**
     * To get the date of birth
     * @return date of birth
     */
    public Date getDob() {
        return this.dob;
    }

    /**
     * To set the date of birth
     * @param dob
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * To get the expiration date
     * @return expiration date
     */
    public Date getExpire() {
        return this.expire;
    }

    /**
     * To get the location
     * @return location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * To set the location
     * @param location location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Creates String of member information including membership fee
     * @return return string of member information
     */
    public String printMembership() {
        return (
                this.fname + ", " +
                        this.lname +
                        " DOB: " + this.dob.toString() +
                        ", Membership expires: " + this.expire.toString() + ", " +
                        "Location: " + this.location.toString() + ", Membership fee: " + this.MemberShipFee()
        );
    }

    /**
     * It overrides the toString method
     * @return first name, last name, date of birth and expiration date
     */
    @Override
    public String toString() {
        return (
                this.fname + ", " +
                        this.lname +
                        " DOB: " + this.dob.toString() +
                        ", Membership expires: " + this.expire.toString() + ", " +
                        "Location: " + this.location.toString()
        );
    }

    /**
     * It overrides the equals method
     * @param obj obejct
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(!(obj instanceof Member)){
            return false;
        }
        Member mem = (Member) obj;
        return (fname.toLowerCase().equals(mem.fname.toLowerCase()) && lname.toLowerCase().equals(mem.lname.toLowerCase()) && dob.equals(mem.dob));
    }

    /**
     * It overrides the compareTo method
     * @param o the object to be compared.
     * @return first name and last name
     */
    @Override
    public int compareTo(Member o) {
        int comp = this.fname.compareTo(o.fname);
        if(comp == 0){
            return this.lname.compareTo(o.lname);
        } else{
            return comp;
        }
    }

    /**
     * Will calculate the membership fee for the given member
     * @return returns the member fee in string format
     */
    public String MemberShipFee(){
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(MEMBER_ONE_TIME_FEE + MEMBER_MONTHLY_FEE * 3);
    }


    /**
     * It is the main class to check for the methods
     * @param args parameters
     */
    public static void main(String[] args) {


        Date date1 = new Date("01/02/2026");
        Date date2 = new Date("01/02/2022");
        Date date3 = new Date("01/02/2023");


        Location location3 = Location.BRIDGEWATER;
        Location location2 = Location.EDISON;
        Location location1 = Location.FRANKLIN;
        Location location4 = Location.PISCATAWAY;
        Location location5 = Location.SOMERVILLE;

        Member member1 = new Member("Gibre", "Lukas ", date1, date2, location1);
        Member member2 = new Member("Tomas", "Mike ", date1, date2, location2);
        Member member3 = new Member("Micheal", "Tomas ", date2, date1, location3);
        Member member4 = new Member("Mike", "Tato ", date1, date2, location4);
        Member member5 = new Member("Gebre", "Lakas ", date1, date2, location5);
        Member member6 = new Member("nani", "kibret", date3, date2, location5);
//        System.out.println(member6.MemberShipFee());

//        System.out.println(member1);
//        System.out.println(member4.toString());
//        System.out.println(date1.compareTo(date2));
//        System.out.println(member1.compareTo(member2));

        MemberDatabase md = new MemberDatabase();
        md.add(member1);
        md.add(member2);
        md.add(member3);
        md.add(member4);
        md.add(member5);
        md.add(member6);
//        md.print();
        md.printMemberShipFee();
//        md.printByName();
//        md.printByCounty();
//        md.printByExpirationDate();
    }
}

