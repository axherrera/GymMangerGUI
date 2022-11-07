package com.example.gymmanagergui;

import java.util.Scanner;
import java.util.StringTokenizer;

import static com.example.gymmanagergui.ClassType.idClassType;

/**
 * The ClassSchedule class holds a growable array of FitnessClass objects which is used to maintain a database of all
 * classes offered by the gym.
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class ClassSchedule {
    MemberDatabase database;
    ClassSchedule classSchedule;
    private FitnessClass[] classes;
    private int numClasses;
    private final int NOT_FOUND = -1;

    private final Date NA = new Date("00/00/0000");

    private final String MEMBER_LIST = "src/memberList.txt";

    private final String CLASS_SCHEDULE = "src/classSchedule.txt";

    /**
     * Constructor for ClassSchedule object. Will set FitnessClass array siz at 4 by default
     */
    public ClassSchedule(){
        classes = new FitnessClass[4];
        numClasses = 4;
    }

    /**
     * Getter for all classes in the schedule
     * @return returns array of FitnessClass which holds all fitness classes in the schedule
     */
    public FitnessClass[] getClasses() {
        return classes;
    }

    /**
     * Used to grow the size of the array by increments of 4 when full
     */
    private void grow() {
        FitnessClass[] temp = new FitnessClass[numClasses+4];
        for(int i = 0; i<numClasses; i++){
            temp[i] = classes[i];
        }
        classes = temp;
        numClasses+=4;
    }

    /**
     * A method to check if class schedule is full
     * @return If class schedule is full return true, otherwise return false
     */
    private boolean isFull(){
        for(int i = 0; i < numClasses; i++){
            if(classes[i] == null)
                return false;
        }
        return true;
    }

    /**
     * Checks if the schedule is empty
     * @return boolean true if schedule is empty, false if not
     */
    public boolean isEmpty(){
        for(int i = 0; i < numClasses; i++){
            if(classes[i] != null)
                return false;
        }
        return true;
    }

    /**
     * Adds a class to the first empty spot in the class schedule
     * If the schedule is full, grow() is called to expand capacity
     * @param fitnessClass class to be added into class schedule
     * @return if class is successfully added return true, otherwise return false
     */
    public boolean add (FitnessClass fitnessClass) {
        if(isFull())
            grow();
        for(int i = 0; i < numClasses; i++){
            if(classes[i] == null){
                classes[i] = fitnessClass;
                return true;
            }
        }
        return false;
    }

    /**
     * Find a fitness class in the class schedule
     * @param fitnessClass
     * @return index found if successful, -1 if class not found
     */
    public int find(FitnessClass fitnessClass) {
        for(int i = 0; i < numClasses; i++){
            if(fitnessClass.equals(classes[i])){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * To get the class from the schedule
     * @param fitnessClass fitness class
     * @return return class if found, null if class not found in database
     */
    public FitnessClass getFitnessClass(FitnessClass fitnessClass){
        if(find(fitnessClass) == NOT_FOUND)
            return null;
        return classes[find(fitnessClass)];
    }

    private void displayClassSchedule(){
        if(classSchedule.isEmpty()){
//            System.out.println("Fitness class schedule is empty");
            return;
        }
//        System.out.println("\n-Fitness Classes-");
        for(FitnessClass fitnessClass: classSchedule.getClasses()){
            if(fitnessClass==null)
                continue;
            fitnessClass.toString();
            if(fitnessClass.checkedIn.isEmpty())
                continue;
//            System.out.println("   ** participants **");
            fitnessClass.classRoster(Operation.M);

            if(fitnessClass.guests.isEmpty())
                continue;
//            System.out.println("   ** guests **");
            fitnessClass.classRoster(Operation.G);
        }
//        System.out.println("-end of class list-");
    }

    private boolean dateValidation(String fname, String lname, Date date, Operation op){
        if(op == Operation.DOB){
            if(!date.isValid()){
//                System.out.printf("DOB %s: invalid calendar date!\n", date.toString());
                date.toString();
                return false;
            }
            else if(date.isFuture()){
//                System.out.printf("DOB %s: cannot be today or a future date!\n", date.toString());
                date.toString();
                return false;
            }
            else if(!date.ofAge()){
//                System.out.printf("DOB %s: must be 18 or older to join!\n", date.toString());
                date.toString();
                return false;
            }
            else return true;
        }
        else if (op == Operation.EXP){
            if(!date.isValid()){
//                System.out.printf("Expiration date %s: invalid calendar date!\n", date.toString());
                date.toString();
                return false;
            }
            else if(!date.isFuture()){
//                System.out.printf("%s %s %s membership expired\n", fname, lname, date.toString());
                date.toString();
                return false;
            }
            else return true;
        }
        return false;
    }

    public FitnessClass classValidation(String isClass, String instructor, String location){
        ClassType classtype = idClassType(isClass);
        if(classtype == ClassType.NA) {//check if class exists
//            System.out.printf("%s class does not exist\n", isClass);
            isClass.toString();
            return null;
        }
        boolean temp = false;//checks if instructor exists for class
        for(FitnessClass c : classSchedule.getClasses()){
            if(c==null)
                continue;
            if(c.getInstructor().equalsIgnoreCase(instructor)){
                temp = true;
                break;
            }
        }
        if(!temp){
//            System.out.printf("%s - instructor does not exist.\n", instructor);
            instructor.toString();
            return null;
        }
        Location l = Location.idLocation(location);
        if( l ==Location.NA){//check if location exists
//            System.out.printf("%s - invalid location.\n", location);
            location.toString();
            return null;
        }
        for(FitnessClass c : classSchedule.getClasses()){
            if(c == null)
                continue;
            if(c.getInstructor().equalsIgnoreCase(instructor)
                    && c.getLocation()==l
                    && c.getClassType()==classtype
            ){
                return c;
            }
        }
//        System.out.printf("%s by %s does not exist at %s.\n", isClass, instructor, location);

        return null;
    }

    private boolean classTimeConflict(Member tempMem, FitnessClass tempClass, Operation type){
        for(FitnessClass fitnessClass: classSchedule.getClasses()){
            if(fitnessClass==null || fitnessClass.find(tempMem, type)==null)
                continue;
            if(fitnessClass!=tempClass && fitnessClass.getTime()==tempClass.getTime()){

                        tempClass.getClassType().getName();
                        tempMem.getFname();
                        tempMem.getLname();
                        fitnessClass.getClassType().getName();

                return false;
            }
            else if(fitnessClass.getClassType()==tempClass.getClassType()){

                        tempMem.getFname();
                        tempMem.getLname();
                        tempClass.getClassType().getName();
                return false;
            }
        }
        return true;
    }
    private boolean checkInValidate(Member tempMem, FitnessClass tempClass, Operation type) {
        if(tempMem instanceof Family ){
            if(!((Family) tempMem).hasGuestPasses()){
//                System.out.printf("%s %s ran out of guest passes\n", tempMem.getFname(), tempMem.getLname());
                tempMem.getFname().toString(); tempMem.getLname().toString();
                return false;
            }

        }
        //validate that member can check in
        if(tempMem.getLocation()!=tempClass.getLocation()){
            if(!(tempMem instanceof Family)){
                if(type == Operation.G){
//                    System.out.println("Standard membership - guest check-in is not allowed.\n");
                }
                else{

                            tempMem.getFname();
                            tempMem.getLname();
                            tempClass.getLocation().toString();

                }
                return false;
            }
            else{
                if(type == Operation.G){

                            tempMem.getFname();
                            tempMem.getLname();
                            tempClass.getLocation().toString();

                }
            }
            return true;
        }
        //validate that there are no time conflicts no need to check for guests
        if(type != Operation.G) {
            return classTimeConflict(tempMem, tempClass, type);
        }
        else
            ((Family) tempMem).useGuestPass();
        return true;
    }
    private void addOrDropClass(Scanner sc, Operation op, Operation memType) {
        StringTokenizer tk = new StringTokenizer(sc.nextLine(), " ");
        String isClass = tk.nextToken();
        String instructor = tk.nextToken();
        String location = tk.nextToken();

        FitnessClass fClass = classValidation(isClass, instructor, location);
        if (fClass == null)
            return;

        String fname = tk.nextToken();
        String lname = tk.nextToken();
        Date bday = new Date(tk.nextToken());

        if (!dateValidation(fname, lname, bday, Operation.DOB))
            return;

        if (database.getMember(new Member(fname, lname, bday, NA, Location.NA)) == null) {

            fname.toString();
            lname.toString();
            bday.toString();
            return;
        }
        Member tempMem = database.getMember(new Member(fname, lname, bday, NA, Location.NA));

        if (op == Operation.DROP) {//check if member is in class and respond accordingly
//            System.out.println(classSchedule.getFitnessClass(fClass).dropClass(tempMem, memType));
            classSchedule.getFitnessClass(fClass).dropClass(tempMem, memType);
            return;
        }
        if (!checkInValidate(tempMem, fClass, memType))
            return;
        if (!dateValidation(fname, lname, tempMem.getExpire(), Operation.EXP))
            return;

//        System.out.println(fClass.checkIn(tempMem, memType));
        fClass.checkIn(tempMem, memType);
    }

}
