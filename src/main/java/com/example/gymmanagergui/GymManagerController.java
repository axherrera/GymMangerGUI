package com.example.gymmanagergui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;

import static com.example.gymmanagergui.FitnessClassType.idClassType;

public class GymManagerController implements Initializable {

    MemberDatabase database = new MemberDatabase();
    ClassSchedule classSchedule = new ClassSchedule();

    //constants
    private final Date NA = new Date("00/00/0000");
    private final Location [] locations = {
            Location.BRIDGEWATER,
            Location.EDISON,
            Location.FRANKLIN,
            Location.PISCATAWAY,
            Location.SOMERVILLE
    };

    private final FitnessClassType [] classTypes = {
            FitnessClassType.PILATES,
            FitnessClassType.SPINNING,
            FitnessClassType.CARDIO
    };

    //Membership Tab
    @FXML
    private DatePicker dbDOB;

    @FXML
    private TextField dbFirstName, dbLastName;

    @FXML
    private ComboBox<Location> dbLocation;

    @FXML
    private ToggleGroup memType;

    @FXML
    private RadioButton dbStandard, dbFamily,dbPremium;

    //Fitness Class Tab
    @FXML
    private DatePicker fcDatePicker;
    @FXML
    private TextField fcFirstName, fcLastName, fcInstructor;

    @FXML
    private RadioButton fcMember, fcGuest;

    @FXML
    private ComboBox<Location> fcLocation;

    @FXML
    private ComboBox<FitnessClassType> fcType;

    //Tabs
    @FXML
    private Tab fitnessClassTab;

    @FXML
    private Tab gymInfoTab;

    @FXML
    private Tab membershipTab;

    @FXML
    private TextArea textArea;


    //METHODS

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initialize combo boxes
        dbLocation.getItems().addAll(locations);
        fcLocation.getItems().addAll(locations);
        fcType.getItems().addAll(classTypes);
    }

    //**MEMBERSHIP TAB METHODS**/
    @FXML
    void dbAdd(ActionEvent event) {
        try{
            String fname = dbFirstName.getText();
            String lname = dbLastName.getText();
            Date dob = new Date(dbDOB.getValue().toString());
            Location l = dbLocation.getValue();
            Operation memtype = dbStandard.isSelected()?
                    Operation.S:
                    dbFamily.isSelected()?
                            Operation.F:
                            Operation.P;
            Member tempMem = validateMemberData(fname, lname, dob.toString(), l.getTownship(), memtype);
            if(tempMem != null){
                textArea.appendText(String.format("%s %s added.\n", tempMem.getFname(), tempMem.getLname()));
                database.add(tempMem);
            }
        }
        catch (NullPointerException e){
            textArea.appendText("All fields must be filled out to add member.\n");
        }

    }

    @FXML
    void dbRemove(ActionEvent event) {
        try {
            String fname = dbFirstName.getText();
            String lname = dbLastName.getText();
            Date dob = new Date(dbDOB.getValue().toString());
            Member tempMem = new Member(fname, lname, dob, dob, Location.NA);
            if(!database.remove(tempMem)) {
                textArea.appendText(String.format(tempMem.getFname() + " " + tempMem.getLname() + " "+ " is not in the database"));
            }
            textArea.appendText(String.format(tempMem.getFname() + " " + tempMem.getLname() + " removed"));

        }
        catch (NullPointerException e){
            textArea.appendText("Must provide first name, last name, and date of birth to remove member.\n");
        }

    }

    //**FITNESS CLASS TAB METHODS**/
    @FXML
    void fcCheckin(ActionEvent event) {
        try{
            String fName = fcFirstName.getText();
            String lName = fcLastName.getText();
            Date dob = new Date(fcDatePicker.getValue().toString());
            Location location = fcLocation.getValue();
            String instructor = fcInstructor.getText();
            FitnessClassType fcTypeValue = fcType.getValue();
            Operation memtype = fcMember.isSelected()?
                    Operation.M:
                    Operation.G;

            addOrDropClass(fName, lName, dob, location, instructor, fcTypeValue, memtype, Operation.CHK);

        }
        catch (NullPointerException e){
            textArea.appendText("Must provide all information to check into a class.");
        }

    }

    @FXML
    void fcCheckout(ActionEvent event) {
        try{
            String fName = fcFirstName.getText();
            String lName = fcLastName.getText();
            Date dob = new Date(fcDatePicker.getValue().toString());
            Location location = fcLocation.getValue();
            String instructor = fcInstructor.getText();
            FitnessClassType fcTypeValue = fcType.getValue();
            Operation memtype = fcMember.isSelected()?
                    Operation.M:
                    Operation.G;

            addOrDropClass(fName, lName, dob, location, instructor, fcTypeValue, memtype, Operation.DROP);

        }
        catch (NullPointerException e){
            textArea.appendText("Must provide all information to check into a class.");
        }
    }

    //**GYM INFO TAB METHODS
    @FXML
    void loadClassSchedule(ActionEvent event) {
        try{
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Source File for the Import");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            Stage stage = new Stage();
            File sourceFile = chooser.showOpenDialog(stage);
            textArea.appendText(classSchedule.loadSchedule(sourceFile));
        }
        catch(RuntimeException e){
            textArea.appendText("There was an error loading your information. Try another file.");
        }
    }

    @FXML
    void loadMembers(ActionEvent event) {
        try{
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Source File for the Import");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            Stage stage = new Stage();
            File sourceFile = chooser.showOpenDialog(stage);
            textArea.appendText(database.importMembers(sourceFile));
        }
        catch(RuntimeException e){
            textArea.appendText("There was an error loading your information. Try another file.");
        }
    }

    @FXML
    void printByCounty(ActionEvent event) {
        textArea.appendText(this.database.printByCounty());
    }

    @FXML
    void printByExpriation(ActionEvent event) {
        textArea.appendText(this.database.printByExpirationDate());
    }

    @FXML
    void printByName(ActionEvent event) {
        textArea.appendText(this.database.printByName());

    }

    @FXML
    void printClassSchedule(ActionEvent event) {
        textArea.appendText(classSchedule.displayClassSchedule());
    }

    @FXML
    void printMemDatabase(ActionEvent event) {
        textArea.appendText(this.database.printDatabase());

    }

    @FXML
    void printFees(ActionEvent event) {
        textArea.appendText(database.printMemberShipFee());

    }

    //Helper Methods

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
        Member tempMem = switch (memType) {
            case F -> new Family(fname, lname, bday, loc);
            case P -> new Premium(fname, lname, bday, loc);
            default -> new Member(fname, lname, bday, loc);
        };
        if(database.getMember(tempMem)!=null){
            textArea.appendText(String.format("%s %s is already in the database.\n", tempMem.getFname(), tempMem.getLname()));
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
            if(date.isFuture()){
                textArea.appendText(String.format("DOB %s: cannot be today or a future date!\n", date.toString()));
                return false;
            }
            else if(!date.ofAge()){
                textArea.appendText((String.format("DOB %s: must be 18 or older to join!\n", date.toString())));
                return false;
            }
            else return true;
        }
        else if (op == Operation.EXP){
            if(!date.isFuture()){
                textArea.appendText(String.format("%s %s %s membership expired\n", fname, lname, date.toString()));
                return false;
            }
            else return true;
        }
        return false;
    }

    /**
     * method that takes user input and checks whetehr or not the class detailed by input corresponds
     * to a real class in the class schedule. If the class queried is a real class, method will return
     * a reference to the class in the ClassSchedule object. Otherwise, it will return null.
     * @param classtype type of class being queried
     * @param instructor instructor for the class
     * @param location location at which class is taking place
     * @return If the class exists in ClassSchedule return reference to the class, else return null
     */
    public FitnessClass classValidation(FitnessClassType classtype, String instructor, Location location){
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
            textArea.appendText(String.format("%s - instructor does not exist.\n", instructor));
            return null;
        }
        for(FitnessClass c : classSchedule.getClasses()){
            if(c == null)
                continue;
            if(c.getInstructor().equalsIgnoreCase(instructor)
                    && c.getLocation()==location
                    && c.getClassType()==classtype
            ){
                return c;
            }
        }
        System.out.printf("%s by %s does not exist at %s.\n", classtype.toString(), instructor, location.getTownship());
        return null;
    }

    /**
     * Adds or Drops class
     * @param op operation, either add or drop
     */
    private void addOrDropClass(
            String fName,
            String lName,
            Date dob,
            Location location,
            String instructor,
            FitnessClassType fcTypeValue,
            Operation memType,
            Operation op
    ){
        FitnessClass fClass = classValidation(fcTypeValue, instructor, location);
        if (fClass == null)
            return;

        if (!dateValidation(fName, lName, dob, Operation.DOB))
            return;

        if (database.getMember(new Member(fName, lName, dob, NA, Location.NA)) == null) {
            textArea.appendText(String.format("%s %s %s is not in the database.\n", fName, lName, dob));
            return;
        }
        Member tempMem = database.getMember(new Member(fName, lName, dob, NA, Location.NA));

        if (op == Operation.DROP) {//check if member is in class and respond accordingly
            textArea.appendText(classSchedule.getFitnessClass(fClass).dropClass(tempMem, memType));
            return;
        }
        if (!checkInValidate(tempMem, fClass, memType))
            return;
        if (!dateValidation(fName, lName, tempMem.getExpire(), Operation.EXP))
            return;

        textArea.appendText(fClass.checkIn(tempMem, memType));

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
                textArea.appendText(String.format("%s %s ran out of guest passes\n", tempMem.getFname(), tempMem.getLname()));
                return false;
            }

        }
        //validate that member can check in
        if(tempMem.getLocation()!=tempClass.getLocation()){
            if(!(tempMem instanceof Family)){
                if(type == Operation.G){
                    textArea.appendText(("Standard membership - guest check-in is not allowed.\n"));
                }
                else{
                    textArea.appendText(String.format("%s %s checking in %s - standard membership location restriction\n",
                            tempMem.getFname(),
                            tempMem.getLname(),
                            tempClass.getLocation().toString()
                            )
                    );
                }
                return false;
            }
            else{
                if(type == Operation.G){
                    textArea.appendText(String.format(
                            "%s %s Guest checking in %s - guest location restriction\n",
                            tempMem.getFname(),
                            tempMem.getLname(),
                            tempClass.getLocation().toString()
                            )
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
                textArea.appendText(String.format(
                        "%s time conflict -- %s %s has already checked in %s.\n",
                        tempClass.getClassType().getName(),
                        tempMem.getFname(),
                        tempMem.getLname(),
                        fitnessClass.getClassType().getName()
                        )
                );
                return false;
            }
            else if(fitnessClass.getClassType()==tempClass.getClassType()){
                textArea.appendText(String.format(
                        "%s %s has already checked in %s\n",
                        tempMem.getFname(),
                        tempMem.getLname(),
                        tempClass.getClassType().getName()
                        )
                );
                return false;
            }
        }
        return true;
    }
}
