package com.example.gymmanagergui;

import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * The MemberDatabase class holds a growable array of member objects which is used to maintain a database of all members of the gym
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class MemberDatabase {
    private Member [] mlist;
    private int size;
    private final int NOT_FOUND = -1;

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
     * @return returns string of database
     */
    public String print() {
        StringBuilder r = new StringBuilder();
        for(int i = 0; i < size; i++){
            if(this.mlist[i] != null)
                r.append(mlist[i].toString()).append('\n');
        }
        return r.toString() + "-end of list-\n";
    }

    /**
     * Prints database by membership fee
     * @return returns string of database sorted by membership fee
     */
    public String printMemberShipFee() {
        StringBuilder r = new StringBuilder();
        for(int i = 0; i < size; i++){
            if(this.mlist[i] != null)
                r.append(mlist[i].printMembership()).append('\n');
        }
        return r.toString() + ("-end of list-\n");
    }

    /**
     * Wrapper method for print()
     * @return  return string the database with formatting.
     * */
    public String printDatabase(){
        if(isEmpty()){
            return ("Member database is empty!\n");
        }
        return ("-list of members-\n") + print();
    }

    /**
     * @return returns string of database after sorting by county and then zipcode
     */
    public String printByCounty() {
        if(isEmpty()){
            return ("Member database is empty!\n");
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
        return ("-list of members sorted by county and zipcode-\n") + print();
    }

    /**
     * * @return returns string of database sorted by expiration date
     */
    public String printByExpirationDate() {
        if(isEmpty()){
            return ("Member database is empty!\n");
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
        return ("-list of members sorted by membership expiration date-\n") + print();
    }

    /**
     * @return returns string of database after sorting by last name and then first name
     */
    public String printByName() {
        if(isEmpty()){
            return ("Member database is empty!\n");
        }
        StringBuilder r = new StringBuilder();
        for(int i = 0; i < size; i++){
            Member k = mlist[i];
            int j = i - 1;
            while(j >= 0 && (mlist[j] != null && mlist[i] != null) && (mlist[j].getLname().compareTo(k.getLname())) > 0){
                mlist[j + 1] = mlist[j];
                j = j - 1;
            }
            mlist[j + 1] = k;
        }
        return ("-list of members sorted by last name, and first name-\n") +
                print();
    }

    /**
     * Imports members from selected file
     * @param file file to read member data from
     * @return returns string of imported members
     */
    public String importMembers(File file){
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                StringTokenizer tk = new StringTokenizer(sc.nextLine(), " ");
                this.add(
                        new Member(
                                tk.nextToken(), tk.nextToken(), new Date(tk.nextToken()), new Date(tk.nextToken()), Location.idLocation(tk.nextToken())
                        )
                );
            }
            return "-list of members loaded-\n" +
                    this.print();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}