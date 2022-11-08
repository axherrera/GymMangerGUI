package com.example.gymmanagergui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

import static com.example.gymmanagergui.FitnessClassType.idClassType;

/**
 * The ClassSchedule class holds a growable array of FitnessClass objects which is used to maintain a database of all
 * classes offered by the gym.
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class ClassSchedule {
    private FitnessClass[] classes;
    private int numClasses;
    private final int NOT_FOUND = -1;

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

    /**
     * Creates string of class schedule
     * @return returns a string of class schedule
     */
    public String displayClassSchedule(){
        if(this.isEmpty()){
            return ("Fitness class schedule is empty\n");
        }
        StringBuilder r = new StringBuilder();
        r.append("\n-Fitness Classes-\n");
        for(FitnessClass fitnessClass: this.getClasses()){
            if(fitnessClass==null)
                continue;
            r.append(fitnessClass).append('\n');
            if(fitnessClass.checkedIn.isEmpty())
                continue;
            r.append("**** participants ****\n").append(fitnessClass.classRoster(Operation.M));

            if(fitnessClass.guests.isEmpty())
                continue;
            r.append("**** guests ****\n").append(fitnessClass.classRoster(Operation.G));
        }
        r.append("-end of class list-");
        return r.toString();
    }

    /**
     * Loads Schedule from selected file
     * @param file file to read schedule from
     * @return returns a string of loaded schedule
     */
    public String loadSchedule(File file){
        try{
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()){
                StringTokenizer tk = new StringTokenizer(sc.nextLine(), " ");
                this.add(
                        new FitnessClass(
                                idClassType(tk.nextToken()),
                                tk.nextToken(),
                                Time.getTime(tk.nextToken()),
                                Location.idLocation(tk.nextToken())
                        )
                );
            }
            return displayClassSchedule();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
