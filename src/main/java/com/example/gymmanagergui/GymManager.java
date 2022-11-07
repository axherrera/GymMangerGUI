package com.example.gymmanagergui;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;

import static com.example.gymmanagergui.FitnessClassType.idClassType;

/**
 * Created a GymManager class represents the fitness manager
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class GymManager {
    MemberDatabase database;
    ClassSchedule classSchedule;
    private final Date NA = new Date("00/00/0000");

    private final String MEMBER_LIST = "src/memberList.txt";

    private final String CLASS_SCHEDULE = "src/classSchedule.txt";

    /**
     * Creates new instance of GymManager class
     */
    public GymManager() {
        this.database = new MemberDatabase();
        this.classSchedule = new ClassSchedule();
    }

    /**
     * It validates input data before creating member object
     * Does date validation on date of birth and location
     * @param fname first name
     * @param lname last name
     * @param birth birthdate
     * @param location location
     * @return Will return member if input is valid. Otherwise, it will return null
     */
    private Member validateMemberData(
            String fname,
            String lname,
            String birth,
            String location,
            Operation memType
    ){
        Date bday = new Date(birth);
        Location loc = Location.idLocation(location);

        if(!dateValidation(fname, lname, bday, Operation.DOB))
            return null;
        if(loc == Location.NA){
            System.out.printf("%s: invalid location!\n", location);
            return null;
        }
        Member tempMem;
        switch (memType){
            case F:
                tempMem = new Family(fname, lname, bday, loc);
                break;
            case P:
                tempMem = new Premium(fname, lname, bday, loc);
                break;
            default:
                tempMem = new Member(fname, lname, bday, loc);
        }
        if(database.getMember(tempMem)!=null){
            System.out.printf("%s %s is already in the database.\n", tempMem.getFname(), tempMem.getLname());
            return null;
        }
        return tempMem;
    }

    /**
     * Does date validation on dob and expiration date
     * Will print out error message if there is invalid date input
     * @param fname first name
     * @param lname last name
     * @param date date
     * @param op operation
     * @return if date is valid return true, otherwise return false
     */
    private boolean dateValidation(String fname, String lname, Date date, Operation op){
        if(op == Operation.DOB){
            if(!date.isValid()){
                System.out.printf("DOB %s: invalid calendar date!\n", date.toString());
                return false;
            }
            else if(date.isFuture()){
                System.out.printf("DOB %s: cannot be today or a future date!\n", date.toString());
                return false;
            }
            else if(!date.ofAge()){
                System.out.printf("DOB %s: must be 18 or older to join!\n", date.toString());
                return false;
            }
            else return true;
        }
        else if (op == Operation.EXP){
            if(!date.isValid()){
                System.out.printf("Expiration date %s: invalid calendar date!\n", date.toString());
                return false;
            }
            else if(!date.isFuture()){
                System.out.printf("%s %s %s membership expired\n", fname, lname, date.toString());
                return false;
            }
            else return true;
        }
        return false;
    }

    /**
     *
     * @param tempMem member object
     * @param tempClass specific class
     * @param type type of check in: member/guest
     * @return returns a boolean which tells if there are time conflicts between classes or not
     */
    private boolean classTimeConflict(Member tempMem, FitnessClass tempClass, Operation type){
        for(FitnessClass fitnessClass: classSchedule.getClasses()){
            if(fitnessClass==null || fitnessClass.find(tempMem, type)==null)
                continue;
            if(fitnessClass!=tempClass && fitnessClass.getTime()==tempClass.getTime()){
                System.out.printf(
                        "%s time conflict -- %s %s has already checked in %s.\n",
                        tempClass.getClassType().getName(),
                        tempMem.getFname(),
                        tempMem.getLname(),
                        fitnessClass.getClassType().getName()
                );
                return false;
            }
            else if(fitnessClass.getClassType()==tempClass.getClassType()){
                System.out.printf(
                        "%s %s has already checked in %s\n",
                        tempMem.getFname(),
                        tempMem.getLname(),
                        tempClass.getClassType().getName()
                );
                return false;
            }
        }
        return true;
    }

    /**
     * check that the there is no conflict with other classes, member is not already checked in
     * @param tempMem member object
     * @param tempClass specific class
     * @param type type of check in: member/guest
     * @return boolean that shows whether member can check into class or not
     */
    private boolean checkInValidate(Member tempMem, FitnessClass tempClass, Operation type) {
        if(tempMem instanceof Family ){
            if(!((Family) tempMem).hasGuestPasses()){
                System.out.printf("%s %s ran out of guest passes\n", tempMem.getFname(), tempMem.getLname());
                return false;
            }

        }
        //validate that member can check in
        if(tempMem.getLocation()!=tempClass.getLocation()){
            if(!(tempMem instanceof Family)){
                if(type == Operation.G){
                    System.out.println("Standard membership - guest check-in is not allowed.\n");
                }
                else{
                    System.out.printf("%s %s checking in %s - standard membership location restriction\n",
                            tempMem.getFname(),
                            tempMem.getLname(),
                            tempClass.getLocation().toString()
                    );
                }
                return false;
            }
            else{
                if(type == Operation.G){
                    System.out.printf(
                            "%s %s Guest checking in %s - guest location restriction\n",
                            tempMem.getFname(),
                            tempMem.getLname(),
                            tempClass.getLocation().toString()
                    );
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

    /**
     * Adds member to gym database
     * If adding is successful, prints out string notifying addition.
     * @param sc scanner object that will read user inputs
     */
    private void addMember(Scanner sc, Operation memType){
        StringTokenizer tk = new StringTokenizer(sc.nextLine(), " ");
        Member tempMem = validateMemberData(tk.nextToken(), tk.nextToken(), tk.nextToken(), tk.nextToken(), memType);
        if(tempMem == null)
            return;
        database.add(tempMem);
        System.out.printf("%s %s added.\n", tempMem.getFname(), tempMem.getLname());
    }

    /**
     * Remove the member from the gym database
     * And prints the first name and last name to remove
     * If the members is not the database, it prints the member is not in database
     * @param sc scanner object that will read user inputs
     */
    private void removeMember(Scanner sc){
        StringTokenizer tk = new StringTokenizer(sc.nextLine(), " ");
        Member tempMem = new Member(tk.nextToken(), tk.nextToken(), new Date(tk.nextToken()), NA, Location.NA);
        if(!this.database.remove(tempMem)) {
            System.out.println(tempMem.getFname() + " " + tempMem.getLname() + " "+ " is not in the database");
            return;
        }
        System.out.println(tempMem.getFname() + " " + tempMem.getLname() + " removed");

    }

    /**
     * It displays the schedule for the gym fitness classes
     */
    private void displayClassSchedule(){
        if(classSchedule.isEmpty()){
            System.out.println("Fitness class schedule is empty");
            return;
        }
        System.out.println("\n-Fitness Classes-");
        for(FitnessClass fitnessClass: classSchedule.getClasses()){
            if(fitnessClass==null)
                continue;
            System.out.println(fitnessClass.toString());
            if(fitnessClass.checkedIn.isEmpty())
                continue;
            System.out.println("   ** participants **");
            fitnessClass.classRoster(Operation.M);

            if(fitnessClass.guests.isEmpty())
                continue;
            System.out.println("   ** guests **");
            fitnessClass.classRoster(Operation.G);
        }
        System.out.println("-end of class list-");
    }

    /**
     * method that takes user input and checks whetehr or not the class detailed by input corresponds
     * to a real class in the class schedule. If the class queried is a real class, method will return
     * a reference to the class in the ClassSchedule object. Otherwise, it will return null.
     * @param isClass type of class being queried
     * @param instructor instructor for the class
     * @param location location at which class is taking place
     * @return If the class exists in ClassSchedule return reference to the class, else return null
     */
    public FitnessClass classValidation(String isClass, String instructor, String location){
        FitnessClassType classtype = idClassType(isClass);
        if(classtype == FitnessClassType.NA) {//check if class exists
            System.out.printf("%s class does not exist\n", isClass);
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
            System.out.printf("%s - instructor does not exist.\n", instructor);
            return null;
        }
        Location l = Location.idLocation(location);
        if( l ==Location.NA){//check if location exists
            System.out.printf("%s - invalid location.\n", location);
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
        System.out.printf("%s by %s does not exist at %s.\n", isClass, instructor, location);
        return null;
    }

    /**
     * Adds or Drops class
     * @param sc scanner object that will read user inputs
     * @param op operation, either add or drop
     */
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
            System.out.printf("%s %s %s is not in the database.\n", fname, lname, bday);
            return;
        }
        Member tempMem = database.getMember(new Member(fname, lname, bday, NA, Location.NA));

        if (op == Operation.DROP) {//check if member is in class and respond accordingly
            System.out.println(classSchedule.getFitnessClass(fClass).dropClass(tempMem, memType));
            return;
        }
        if (!checkInValidate(tempMem, fClass, memType))
            return;
        if (!dateValidation(fname, lname, tempMem.getExpire(), Operation.EXP))
            return;

        System.out.println(fClass.checkIn(tempMem, memType));
    }


    /**
     * Imports historical member information from the text file named memberList.txt located in the source directory
     * This method assumes that all historical members hav valid member information, therefore
     * forgoing new member information validation.
     */
    private void importMembers(){
        File file = new File(MEMBER_LIST);
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                StringTokenizer tk = new StringTokenizer(sc.nextLine(), " ");
                database.add(
                        new Member(
                                tk.nextToken(), tk.nextToken(), new Date(tk.nextToken()), new Date(tk.nextToken()), Location.idLocation(tk.nextToken())
                        )
                );
            }
        }
        catch(FileNotFoundException e) {
            System.out.printf("%s not found in project directory", MEMBER_LIST);
            throw new RuntimeException(e);
        }
        System.out.println("-list of members loaded-");
        database.print();
    }

    /**
     * Loads the class schedule from the text file. File must be named "classSchedule.txt" and be located
     * in the project source directory
     */
    private void loadSchedule(){
        File file = new File(CLASS_SCHEDULE);
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                StringTokenizer tk = new StringTokenizer(sc.nextLine(), " ");
                classSchedule.add(
                        new FitnessClass(
                                idClassType(tk.nextToken()),
                                tk.nextToken(),
                                Time.getTime(tk.nextToken()),
                                Location.idLocation(tk.nextToken())
                        )
                );
            }
        displayClassSchedule();
        } catch (FileNotFoundException e) {
            System.out.printf("%S not found in project directory", CLASS_SCHEDULE);
            throw new RuntimeException(e);
        }
    }
    /**
     * Checks operation and calls corresponding method
     * @param op operation to be used
     * @param sc scanner object to be passed into corresponding method
     */
    private void checkOP(String op, Scanner sc) {
        switch (op) {
            case "A":
                addMember(sc, Operation.S);
                break;
            case "AF":
                addMember(sc, Operation.F);
                break;
            case "AP":
                addMember(sc, Operation.P);
                break;
            case "R":
                removeMember(sc);
                break;
            case "P":
                this.database.printDatabase();
                break;
            case "PC":
                this.database.printByCounty();
                break;
            case "PN":
                this.database.printByName();;
                break;
            case "PD":
                this.database.printByExpirationDate();
                break;
            case "S":
                displayClassSchedule();
                break;
            case "C":
                addOrDropClass(sc, Operation.CHK, Operation.M);
                break;
            case "CG":
                addOrDropClass(sc, Operation.CHK, Operation.G);
                break;
            case "D":
                addOrDropClass(sc, Operation.DROP, Operation.M);
                break;
            case "DG":
                addOrDropClass(sc, Operation.DROP, Operation.G);
                break;
            case "LS":
                loadSchedule();
                break;
            case "LM":
                importMembers();
                break;
            case "PF":
                this.database.printMemberShipFee();


            default:
                System.out.println(op + " is an invalid command!\n");
        }
    }

    /**
     * Runs an instance of the GymManager class. Will terminate with the input 'Q'
     */
    public void run(){
        System.out.println("Gym Manager running...");
        Scanner scan = new Scanner(System.in);
        String op = scan.next();
        while(!op.equals("Q")){
            checkOP(op, scan);
            op = scan.next();
        }
        System.out.println("Gym Manager terminated.");
    }
}
