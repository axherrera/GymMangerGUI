package com.example.gymmanagergui;

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
     */
    public void print() {
        for(int i = 0; i < size; i++){
            if(this.mlist[i] != null)
                System.out.println(mlist[i]);
        }
        System.out.println("-end of list-\n");
    }

    /**
     * Prints database by membership fee
     */
    public void printMemberShipFee() {
        for(int i = 0; i < size; i++){
            if(this.mlist[i] != null)
                System.out.println(mlist[i].printMembership());
        }
        System.out.println("-end of list-\n");
    }

    /**
     * Wrapper method for print(). Will print the database with formatting.
     * */
    public void printDatabase(){
        if(isEmpty()){
            System.out.println("Member database is empty!");
            return;
        }
        System.out.println("-list of members-");
        print();
    }

    /**
     * prints database after sorting by county and then zipcode
     */
    public void printByCounty() {
        if(isEmpty()){
            System.out.println("Member database is empty!");
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
        System.out.println("-list of members sorted by county and zipcode-");
        print();
    }

    /**
     * Sorts by the expiration date and then prints database
     */
    public void printByExpirationDate() {
        if(isEmpty()){
            System.out.println("Member database is empty!");
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
        System.out.println("-list of members sorted by membership expiration date-");
        print();
    }

    /**
     * Prints database after sorting by last name and then first name
     */
    public void printByName() {
        if(isEmpty()){
            System.out.println("Member database is empty!");
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
        System.out.println("-list of members sorted by last name, and first name-");
        print();
    }
}