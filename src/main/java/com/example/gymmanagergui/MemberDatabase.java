package com.example.gymmanagergui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

import static com.example.gymmanagergui.Time.NA;

/**
 * The MemberDatabase class holds a growable array of member objects which is used to maintain a database of all members of the gym
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class MemberDatabase {
    private Member [] mlist;
    MemberDatabase database;
    ClassSchedule classSchedule;
    private int size;
    private final int NOT_FOUND = -1;
    private final Date NA = new Date("00/00/0000");

    private final String MEMBER_LIST = "src/memberList.txt";

    private final String CLASS_SCHEDULE = "src/classSchedule.txt";

    /**
     * It initializes an instance of the MemberDatabase
     */
    public MemberDatabase() {
        mlist = new Member[4];
        size = 4;
    }

    /**
     * Used to grow the size of the array by increments of 4 when full
     */
    private void grow() {
        Member[] temp = new Member[size + 4];
        for(int i = 0; i < size; i++){
            temp[i] = mlist[i];
        }
        mlist = temp;
        size += 4;
    }

    /**
     * A method to check if database is full
     * @return If database is full return true, otherwise return false
     */
    private boolean isFull(){
        for(int i = 0; i < size; i++){
            if(mlist[i] == null)
                return false;
        }
        return true;
    }

    /**
     * check if the database is empty
     * @return If array is empty return true, otherwise return false
     */
    public boolean isEmpty(){
        for(int i = 0; i < size; i++){
            if(mlist[i] != null)
                return false;
        }
        return true;
    }

    /**
     * Adds a member to the first empty spot in the database
     * If the database is full, grow() is called to expand capacity
     * If the member is already in the database return false
     * @param member member to be added into database
     * @return if member is successfully added return true, otherwise return false
     */
    public boolean add (Member member) {
        if(isFull())
            grow();
        else if(find(member) != NOT_FOUND)
            return false;

        for(int i = 0; i < size; i++) {
            if (mlist[i] == null) {
                mlist[i] = member;
                return true;
            }
        }
        return false;
    }

    /**
     * Find a member in the database
     * @param member
     * @return index found if successful, -1 if member not found
     */
    public int find(Member member) {
        for(int i = 0; i < size; i++){
            if(member.equals(mlist[i])){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * To get the member from the database
     * @param m member
     * @return return member if found, null if member not found in database
     */
    public Member getMember(Member m){
         if(find(m) == NOT_FOUND)
             return null;
         return mlist[find(m)];
    }

    /**
     * Remove the member from the database
     * @param member member
     * @return return result of removal attempt from database
     */
    public boolean remove( Member member) {
        int index = find(member);
        if (index == NOT_FOUND)
            return false;
        mlist[find(member)] = null;
        for(int i = index+1; i <size; i++){
            Member temp = mlist[i-1];
            mlist[i-1] = mlist[i];
            mlist[i] = temp;
        }
        return true;
    }

    /**
     * Prints database as is
     */
    public void print() {
        for(int i = 0; i < size; i++){
            if(this.mlist[i] != null)
               mlist[i].toString();
        }
//        System.out.println("-end of list-\n");
    }

    /**
     * Prints database by membership fee
     */
    public void printMemberShipFee() {
        for(int i = 0; i < size; i++){
            if(this.mlist[i] != null)
                mlist[i].printMembership();
        }
//        System.out.println("-end of list-\n");
    }

    /**
     * Wrapper method for print(). Will print the database with formatting.
     * */
    public void printDatabase(){
        if(isEmpty()){
//            System.out.println("Member database is empty!");
            return;
        }
//        System.out.println("-list of members-");
        print();
    }

    /**
     * prints database after sorting by county and then zipcode
     */
    public void printByCounty() {
        if(isEmpty()){
//            System.out.println("Member database is empty!");
            return;
        }
        for(int i = 1; i < size; ++i){
            Member k = mlist[i];
            int j = i - 1;
            while( j >= 0 && (mlist[j] != null && mlist[i] != null) && (mlist[j].getLocation().getRank() > k.getLocation().getRank())){
                mlist[j + 1] = mlist[j];
                j = j - 1;
            }
            mlist[j + 1] = k;
        }
//        System.out.println("-list of members sorted by county and zipcode-");
        print();
    }

    /**
     * Sorts by the expiration date and then prints database
     */
    public void printByExpirationDate() {
        if(isEmpty()){
//            System.out.println("Member database is empty!");
            return;
        }
        for(int i = 1; i < size; ++i) {
            Member k = mlist[i];
            int j = i - 1;
            while(j >= 0 && (mlist[j] != null && mlist[i] != null) &&(mlist[j].getExpire().compareTo(k.getExpire())) > 0){
                mlist[j + 1] = mlist[j];
                j = j - 1;
            }
            mlist[j + 1] = k;
        }
//        System.out.println("-list of members sorted by membership expiration date-");
        print();
    }

    /**
     * Prints database after sorting by last name and then first name
     */
    public void printByName() {
        if(isEmpty()){
//            System.out.println("Member database is empty!");
            return;
        }
        for(int i = 0; i < size; i++){
            Member k = mlist[i];
            int j = i - 1;
            while(j >= 0 && (mlist[j] != null && mlist[i] != null) && (mlist[j].getLname().compareTo(k.getLname())) > 0){
                mlist[j + 1] = mlist[j];
                j = j - 1;
            }
            mlist[j + 1] = k;
        }
//        System.out.println("-list of members sorted by last name, and first name-");
        print();
    }

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
}