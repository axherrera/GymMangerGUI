package com.example.gymmanagergui;

/**
 * Instance of a fitness class which consists of a class type and database of all checked in members
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class FitnessClass {
    protected MemberDatabase checkedIn = new MemberDatabase();
    protected MemberDatabase guests = new MemberDatabase();
    private final String instructor;
    private final Time time;
    private final Location location;
    private final ClassType classType;
    private final String classInfo;

    /**
     * Initializes a newly created FitnessClass object
     *
     * @param classType classType
     */
    public FitnessClass(ClassType classType, String instructor, Time time, Location location){
        this.classType = classType;
        this.instructor = instructor;
        this.time = time;
        this.location = location;
        this.classInfo = String.format("%s - %s, %s, %s", classType.getName(), instructor, time.toString(), location.getTownship());
    }

    /**
     * This is to get the fitness class type
     * @return class type
     */
    public ClassType getClassType(){
        return this.classType;
    }

    public Time getTime() {
        return time;
    }

    public Location getLocation() {
        return location;
    }

    public String getInstructor() {
        return instructor;
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
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        FitnessClass mem = (FitnessClass) obj;
        return (classType.equals(mem.classType) && time.equals(mem.time)  && location.equals(mem.location) && instructor.equals(mem.instructor));
    }

    /**
     * Checks in a member into the fitness class object
     * Prints out string to console detailing which member got added to which class
     * @param member member of the gym fitness
     * @param memType operation to be done. Can either be a guest or member addition
     */

    public String checkIn(Member member, Operation memType){
        if(memType==Operation.G){
            guests.add(member);
            return String.format(
                    "%s %s (guest) checked into %s", member.getFname(), member.getLname(), classInfo
            );
        }
        checkedIn.add(member);
        return String.format("%s %s checked into %s", member.getFname(), member.getLname(), classInfo);
    }

    /**
     * Drops a member from fitness class object
     * Prints warning message if member is not a participant of the class
     * Otherwise prints notification that member dropped the class
     * @param member member of the fitness chain
     * @param memType operation to be done. Can either be a guest or member deletion
     */
    public String dropClass(Member member, Operation memType){
        if(memType==Operation.G){
            if(!guests.remove(member)){
                return String.format("%s %s (guest) is not a participant in %s", member.getFname(), member.getLname(), classType.getName());
            }
            ((Family)member).returnGuestPass();
            return String.format("%s %s (guest) done with the class\n", member.getFname(), member.getLname());
        }
        if(!checkedIn.remove(member)){
            return String.format("%s %s is not a participant in %s", member.getFname(), member.getLname(), classType.getName());
        }
        return String.format("%s %s done with the class\n", member.getFname(), member.getLname());
    }

    /**
     * Finds out whether a member checked into the class or not
     *
     * @param m member of the gym fitness
     * @return if member is found return member, return null if no such member exists
     */
    public Member find(Member m ,Operation type){
        if(type == Operation.G)
            return this.guests.getMember(m);
        return this.checkedIn.getMember(m);
    }

    /**
     * Overrides toString method
     * @return Fitness Class information
     */
    @Override
    public String toString(){
        return classInfo;
    }

    /**
     * It prints class information as well as all members attending
     * If class is empty, does not attempt to print out the participant list
     */
    public void classRoster(Operation type) {
        if(type == Operation.G)
            guests.printDatabase();
        else
            checkedIn.printDatabase();
    }


}
