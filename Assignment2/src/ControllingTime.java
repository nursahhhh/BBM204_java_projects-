import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * The type Controlling time.
 */
public class ControllingTime {
    /**
     * fields is created as private  , it is special for this class.
     */
    private  SmartLamp lamp;
    private SmartPlug plug;
    private SmartCamera camera;

    // current time is  the timeline value.
    private LocalDateTime currentTime;
    /**
     * This deviceList will contains SmartDevices's instance.
     */
    ArrayList<SmartDevices> deviceList = new ArrayList<>();

    /**
     * Constructor a new ControllingTime.
     */
    public ControllingTime() {

    }

    /**
     *Checks if the initial time is set correctly and updates the current time accordingly.
     *@param line a String containing the command to set the initial time and the new time in the format "SetInitialTime yyyy-MM-dd_HH:mm:ss"
     *@return true if the initial time is set successfully and false if there is an error in the command or format of the new time
     */
    public boolean isInitialTimeSet (String line){
        String[] words= line.split("\t") ;
        if ((!words[0].equals("SetInitialTime")) || (words.length != 2)) {
            IOClass.writeToOutputFile("ERROR: First command must be set initial time! Program is going to terminate!");
            return false;
        }
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime newTime =  LocalDateTime.parse(words[1], formatter);
            setCurrentTime(newTime);
            IOClass.writeToOutputFile("SUCCESS: Time has been set to " + words[1] + "!");

        }catch (DateTimeParseException d ){
            IOClass.writeToOutputFile("ERROR: Format of the initial date is wrong! Program is going to terminate!");
            return false;
        }
        return true;
    }

    /**
     * Adds a new smart device to the list of devices, based on the type specified in the input.
     * @param words an array of strings containing the input command and its parameters
     * @throws Errorneus if the input command or its parameters are incorrect
     * @throws valueError if the input values are incorrect
     */
    public void adding (String[] words){
         try {
            switch (words[1]) {
                case "SmartLamp":
                case "SmartColorLamp":
                    lamp = addingLamps(words);
                    deviceList.add(lamp);
                    break;
                case "SmartCamera":
                    camera = addingCamera(words);

                    deviceList.add(camera);

                    break;
                case "SmartPlug":
                    addingPlug(words);
                    deviceList.add(plug);
                    break;
            }
        } catch(Errorneus o){
            IOClass.writeToOutputFile( o.getMessage());
        } catch (valueError v1){
            IOClass.writeToOutputFile( v1.getMessage());
        }
    }

    /**
     * Removed method removes a smart device from the deviceList based on the name of the device passed in as a parameter.
     * It throws an error if the input line contains more or less than two words or if the device does not exist in the deviceList.
     * If the device is turned on, it calculates and stores energy usage or consumption based on the type of device.
     * It updates the switch status of the device to off and writes information about the removed device to an output file.
     * @param line A string that contains the name of the device to be removed and some additional information, separated by a tab character.
     * @throws Errorneus If the input line contains more or less than two words.
     * @throws DoesntExist If the device does not exist in the deviceList.
     */
    public void Removed(String  line){
        String[] words = line.split("\t");
        if(words.length !=2 )throw new Errorneus();
        if(!isObjectExist(words[1])) throw new DoesntExist();
       for ( SmartDevices device : deviceList){
           if(device.getName().equals(words[1])){
               if(device.SwitchStatus){
                   if (device instanceof  SmartCamera)((SmartCamera) device).StoreUsageCalculator(currentTime);
                   if(device instanceof  SmartPlug) ((SmartPlug) device).calculateEnergyConsumption(currentTime);
               }
               device.setSwitchStatus(false);
               IOClass.writeToOutputFile("SUCCESS: Information about removed smart device is as follows:");
               IOClass.writeToOutputFile(device.toString());
           }
       }
        deviceList.removeIf(SmartDevices -> SmartDevices.getName().equals(words[1]));
    }

    /**
     * Sets the current time for the program and checks the switch times of all smart devices in the deviceList.
     * If the format of the date/time is wrong, it writes an error message to the output file and terminates the program.
     * @param Time A LocalDateTime object that represents the current date and time to be set.
     */
    public void setCurrentTime(LocalDateTime Time) {
        try {
            this.currentTime =Time;
            CheckSwitchTimes(currentTime);
        }catch (DateTimeParseException d ){
           IOClass.writeToOutputFile("ERROR: Format of the  date/time  is wrong! Program is going to terminate!");
        }
    }

    /***
     * Skips the current time by a specified number of minutes and checks the switch times of all smart devices in the deviceList.
     * It throws an error if the input line contains more or less than two words or if the specified time is negative or zero.
     * If the input time value is not an integer, it throws an error.
     * @param words A string array that contains the command and the number of minutes to skip.
     * @throws Errorneus If the input line contains more or less than two words or if the specified time is not an integer.
     * @throws Errors If the specified time is negative or zero.
     */
    public void skipTime(String[] words ){
        if (words.length > 2 || words.length ==1 ){
            throw new Errorneus();
        }
        try {
            int change = Integer.parseInt(words[1]);
            if (change < 0) {
                throw new Errors("Time cannot be reversed!");
            }
            if (change == 0) {
                throw new Errors("There is nothing to skip!");
            }
            currentTime = currentTime.plusMinutes(change);
            CheckSwitchTimes(currentTime);
        }catch (NumberFormatException N) {
            throw new  Errorneus();
        }
    }

    /**
     * Sets the current time for the program based on the input string and checks the switch times of all smart devices in the deviceList.
     * It throws an error if the input line contains more or less than two words or if the specified time is in an incorrect format.
     * If the specified time is earlier than the current time, it throws an error.
     * If the specified time is the same as the current time, it throws an error.
     * @param line A string that contains the command and the new date and time to be set in the format "yyyy-MM-dd_HH:mm:ss".
     * @throws Errorneus If the input line contains more or less than two words or if the specified time is in an incorrect format.
     * @throws Errors If the specified time is earlier than the current time or if it is the same as the current time.
     */
    public void SetTime(String line){
        String [] words = line.split("\t");
        if (words.length> 2 || words.length ==1) throw new Errorneus();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            LocalDateTime newTime = LocalDateTime.parse(words[1], formatter);
            int result = newTime.compareTo(currentTime);
            if (result < 0) throw new Errors(" Time cannot be reversed!");
            if (result == 0) throw new Errors(" There is nothing to change!");
            setCurrentTime(newTime);
        }catch (DateTimeParseException e){
           IOClass.writeToOutputFile("ERROR: Time format is not correct!");
        }
    }

    /**
     * Sets the switch time for a specific smart device based on its name and the new time string.
     * It parses the input time string into a LocalDateTime object and checks if it is later than the current time.
     * If the specified time is earlier than the current time, it throws an error.
     * If the specified smart device does not exist, it throws an error.
     * If the specified device's switch time is null or later than the current time,
     * it sets the switch time to the new time and checks the switch times of all smart devices in the deviceList.
     * @param name A string that represents the name of the smart device.
     * @param time A string that represents the new switch time in the format "yyyy-MM-dd_HH:mm:ss".
     * @throws Errors If the specified switch time is earlier than the current time.
     * @throws DoesntExist If the specified smart device does not exist.
     */
    public void setSwitchTime (String name, String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime newTime = LocalDateTime.parse(time,formatter);
        if(newTime.compareTo(currentTime) < 0) throw new Errors("Switch time cannot be in the past!");
        if (!isObjectExist(name)){
            throw new DoesntExist();
        }
        for (SmartDevices device : deviceList){
            if (name.equals(device.getName())){
                if(device.switchTime == null || device.switchTime.compareTo(currentTime)>=0) {
                    device.setSwitchTime(newTime);
                    CheckSwitchTimes(currentTime);
                }
            }
        }
    }

    /**
     * This method is used to switch the status of a smart device on or off based on the input received through the command line.
     * The method takes a string input which contains the name of the device and the status to be changed to.
     * The method first checks if the input is valid and throws an error if it's not.
     * It then iterates over the list of devices to find the device with the input name and changes its status based on the input status.
     * If the input status is "On" and the device is a smart plug or camera, it sets the starting time of the device.
     * If the input status is "Off" and the device is a smart plug or camera, it calculates the energy consumption or stores the usage data, respectively.
     * @param line a string containing the device name and the status to be changed to, separated by a tab character
     * @throws Errorneus if the input is invalid
     * @throws DoesntExist if the device does not exist in the device list
     * @throws Errors if the device is already switched on or off depending on the input status
     * */

    public void SwitchDevice(String line){
        String[] words= line.split("\t");
        if (words.length!= 3) throw new Errorneus();
        if(!isObjectExist(words[1])) throw new DoesntExist();
        for (SmartDevices device : deviceList){
            if(words[1].equals(device.getName())) {
                if (words[2].equals("On")) {
                    if (device.getSwitchStatus()) {
                        throw new Errors(" This device is already switched on!");
                    }
                    device.setSwitchStatus(true);
                    if(device instanceof SmartPlug) {
                        if (((SmartPlug) device).isPlugIn){
                        ((SmartPlug) device).setStartingTime(currentTime);
                    }
                        if (device instanceof  SmartCamera){
                            ((SmartCamera) device).setStartingTime(currentTime);
                        }
                    }
                } else if (words[2].equals("Off")) {
                    if (!device.getSwitchStatus()) {
                        throw new Errors(" This device is already switched " + words[2] + "!");
                    }
                    if (device instanceof SmartPlug) ((SmartPlug) device).calculateEnergyConsumption(currentTime);
                    if(device instanceof  SmartCamera){
                        ((SmartCamera) device).StoreUsageCalculator(currentTime);
                    }
                    device.setSwitchStatus(false);
                } else throw new Errorneus();
            }
        }
    }

    /**
     * Changes the name of a smart device.
     * @param line A String containing the name of the device to be renamed and the new name separated by a tab character.
     * @throws Errorneus if the number of words in the command is not equal to 3.
     * @throws DoesntExist if the smart device with the old name does not exist.
     * @throws Errors if the old and new names are the same, or if there is already a smart device with the new name.
     * */

    public void changeName(String line){
        String[] words = line.split("\t");
        if(words.length != 3) throw new Errorneus();
        String oldName = words[1];
        String newName = words[2];
        if (oldName.equals(newName)) throw new Errors(" Both of the names are the same, nothing changed!");
        if (!isObjectExist(oldName)) throw new DoesntExist();
        if(isObjectExist(newName)) throw new Errors(" There is already a smart device with same name!");
        for (SmartDevices devices : deviceList ){
            if (oldName.equals(devices.getName())){
                devices.setName(newName);
            }
        }
    }

    /**
     * Changes the status of a smart plug by either plugging in an item or unplugging it from the plug.
     * @param Words An array of strings containing the necessary information to perform the operation.
     * The first string should be either "PlugIn" or "PlugOut", depending on the action to be taken.
     * The second string should be the name of the smart plug whose status is to be changed.
     * If "PlugIn" is selected, a third string should also be provided, containing the ampere value of the item to be plugged in.
     * @throws DoesntExist If the specified smart plug does not exist.
     * @throws Errorneus If the number of strings in the "Words" array is less than 2 or greater than 3, or if the first string is not "PlugIn" or "PlugOut".
     * @throws Errors If the plug is already occupied by an item and "PlugIn" is selected,
     * or if the plug has no item to be unplugged and "PlugOut" is selected. Also throws an error if the device specified by the second string is not a smart plug.
     */

    public void ChangePlug(String[]Words) {
        if (!isObjectExist(Words[1])) throw new DoesntExist();
        if (Words.length < 2) throw new Errorneus();
        else if (Words.length > 3 ) throw new Errorneus();
        else {
            for (SmartDevices device : deviceList) {
                if (device.getName().equals(Words[1])) {
                    if (device instanceof SmartPlug) {
                        if (Words[0].equals("PlugIn")) {
                            if (((SmartPlug) device).isPlugIn) {
                                throw new Errors(" There is already an item plugged in to that plug!");
                            } else {
                                double ampereValue = Double.parseDouble(Words[2]);
                                ((SmartPlug) device).setAmpere(ampereValue);
                                ((SmartPlug) device).setPlugIn(true);
                            }
                        } else if (Words[0].equals("PlugOut")) {
                            if (!((SmartPlug) device).isPlugIn) {
                                throw new Errors(" This plug has no item to plug out from that plug!");
                            } else {
                                if (plug.getSwitchStatus()){
                                    ((SmartPlug) device).calculateEnergyConsumption(currentTime);
                                }
                                ((SmartPlug) device).setPlugIn(false);
                            }
                        }
                        } else {
                            throw new Errors(" This device is not a smart plug!");
                        }
                    }
                }
            }
        }

    /**
     * This method is used to set the properties of a Smart Lamp or Smart Lamp With Color object based on the input command line.
     * The command line contains the property to be set and the new value.
     * The method parses the command line, and then checks if the object with the given name exists in the device list.
     * If the object exists, it checks if it is a Smart Lamp or a Smart Lamp With Color object, and sets the specified property accordingly.
     * The method throws an error if the object with the given name does not exist in the device list, or if the specified property does not match the type of the device.
     * @param line The command line to set the properties of a Smart Lamp or Smart Lamp With Color object.
     * @throws DoesntExist if the specified device does not exist in the device list.
     * @throws Errors if the specified device is not a Smart Lamp or Smart Lamp With Color object, or if the specified property is not a valid property for the device.
     * @throws Errorneus if the command line is incomplete or contains an invalid value for the specified property.
     */
    public void setterForLamps(String line ){
        String[] words = line.split("\t");
        if(!isObjectExist(words[1])) throw new DoesntExist();
        String name = words[1];
        for (SmartDevices device : deviceList) {
            if (name.equals(device.getName())) {
                try {
                    switch (words[0]) {
                        case "SetKelvin":
                            if (device instanceof SmartLamp) {
                                // there are  distinct conditions for lamp and colorLamp in setter. IscColorLamp checks type of lamp.
                                int newKelvin = Integer.parseInt(words[2]);
                                ((SmartLamp) device).setKelvin(newKelvin,true);
                                if(device instanceof  SmartLampWithColor) ((SmartLampWithColor) device).setColor(words[2],true);
                                break;
                            } else throw new Errors(" This device is not a smart lamp!");

                        case "SetBrightness":
                            if (device instanceof SmartLamp) {
                                int newBrightness = Integer.parseInt(words[2]);
                                ((SmartLamp) device).setBrightness(newBrightness);
                                break;
                            } else throw new Errors(" This device is not a smart lamp!");
                        case "SetColorCode":
                            if (device instanceof SmartLampWithColor) {
                                int colorCode = Integer.parseInt(words[2]);
                                ((SmartLampWithColor) device).setKelvin(colorCode, false);
                                ((SmartLampWithColor) device).setColor(words[2],false);
                                break;
                            } else throw new Errors(" This device is not a smart color lamp!");
                        case "SetColor":
                            if (device instanceof SmartLampWithColor) {
                                int colorValue =Integer.parseInt(words[2].substring(2), 16);
                                int brightness = Integer.parseInt(words[3]);
                                ((SmartLampWithColor) device).setKelvin(colorValue,false);
                                ((SmartLampWithColor) device).setBrightness(brightness);
                                ((SmartLampWithColor) device).setColor(words[2],false);
                                break;
                            } else throw new Errors(" This device is not a smart color lamp!");

                            case "SetWhite":
                                if(device instanceof SmartLamp){
                                    int kelvinValue = Integer.parseInt(words[2]);
                                    int brightness = Integer.parseInt(words[3]);
                                    ((SmartLamp) device).setKelvin(kelvinValue,true);
                                    ((SmartLampWithColor) device).setColor(words[2],true);
                                    ((SmartLamp) device).setBrightness(brightness);
                                }else throw new Errors(" This device is not a smart lamp!");
                    }
                }catch (ArrayIndexOutOfBoundsException missingCommand){
                    throw new Errorneus();
                }
                catch (NumberFormatException set) {
                    throw new Errorneus();
                }
            }
        }
    }
    /**
     * Sorts the device list based on switch time and finds the smallest switch time.
     * If there are no switch times in the list, throws an exception.
     * Sets the current time to the smallest switch time found and calls the CheckSwitchTimes method.
     * @throws Errors If there are no devices in the list to switch or if there are no switch times found.
     */
    public void Nop() {
        deviceList.sort(Comparator.nullsLast(Comparator.comparing(device -> device.getSwitchTime(), Comparator.nullsLast(Comparator.naturalOrder()))));
        //deviceList.sort(Comparator.nullsLast(Comparator.comparing(deviceList:: getSwitchTime(),Comparator.nullsLast(Comparator.naturalOrder()))));
        if (deviceList.size() == 0) throw new Errors(" There is nothing to switch!");
        SmartDevices smallest = deviceList.stream().filter(SmartDevices -> SmartDevices.getSwitchTime() != null)
                .min(Comparator.comparing(SmartDevices::getSwitchTime))
                .orElse(null);
        if(null == smallest.getSwitchTime()) {
            throw new Errors(" There is nothing to switch!");
        }
        this.currentTime = smallest.getSwitchTime();
        CheckSwitchTimes(currentTime);
    }

    /**
     * Writes a report of the current state of all SmartDevices in the deviceList to an output file.
     * Devices without a switch time are sorted first in the report.
     * The report is written to a file using the IOClass's writeToOutputFile method.
     */
    public void ZReporter (){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formattedDateTime = currentTime.format(formatter);
        IOClass.writeToOutputFile("Time is:\t" +formattedDateTime.toString());
        deviceList.sort(
                Comparator.nullsFirst(
                        Comparator.comparing(SmartDevices::getSwitchTime,
                                Comparator.nullsLast(Comparator.naturalOrder()))));
        for(SmartDevices device : deviceList){
            IOClass.writeToOutputFile(device.toString());

        }
    }

    /**
     * Checks the switch times of all the devices in the device list and performs relevant actions accordingly.
     * If the switch time of a device has passed or is equal to the current time, the device is turned on or off depending on its switch status.
     * If the device status on , before turn of device Calculates energyConsumption(if it is plug) or Storage usage ( for camera).
     * The device list is then sorted based on switch times, with null values appearing first.
     * ( To avoid ConcurrentModificationException , it uses clone of devicelist for sorting simultaneously)
     * @param Time The time at which the switch times of the devices should be checked.
     */
    public void CheckSwitchTimes(LocalDateTime Time){
        ArrayList<SmartDevices> sortList = new ArrayList<>(deviceList);
         sortList.sort(
                 Comparator.nullsFirst(
                         Comparator.comparing(SmartDevices::getSwitchTime,
                                 Comparator.nullsLast(Comparator.naturalOrder()))));
        for(SmartDevices device : deviceList) {
            if (device.getSwitchTime() != null) {
                long result = Time.compareTo(device.getSwitchTime());
                if (result >= 0) {
                    if(device.SwitchStatus){
                        if(device instanceof  SmartPlug){
                            ((SmartPlug) device).calculateEnergyConsumption(Time);
                        } else if (device instanceof SmartCamera) {
                            ((SmartCamera) device).StoreUsageCalculator(Time);
                        }
                    }
                        if(currentTime !=null) device.Turn(Time);
                    sortList.sort(
                            Comparator.nullsFirst(
                                    Comparator.comparing(SmartDevices::getSwitchTime,
                                            Comparator.nullsLast(Comparator.naturalOrder()))));
                }
            }
        }
        deviceList = sortList;
    }

    /**
     * Creates a new SmartLamp object based on the input parameters.
     * @param words an array of string parameters containing information about the SmartLamp to be created.
     * The array must contain at least 3 elements,(otherwise throws Errorneus) where the first element is the type of SmartLamp to create,
     * the second element is the name of the SmartLamp, and the third element is either "On" or "Off" to set the initial switch status.
     * If the array contains 4 elements, the last element must be either "On" or "Off" to set the initial switch status.
     * If the array contains 6 elements, the last two elements specify the initial brightness and color of the SmartLamp, respectively.
     * If the SmartLamp to be created is a SmartColorLamp, the color must be specified as a hexadecimal string starting with "0x".
     * @return a newly created SmartLamp object with the specified parameters.
     * @throws AlreadyExist  if there is an object with same name .
     */
    public  SmartLamp addingLamps(String[]words) {
        if (isObjectExist(words[2])){
            throw new AlreadyExist();
        }
        int i = words.length;
        switch (i) {
            case 3:
                lamp = words[1].equals("SmartLamp") ? new SmartLamp(words[2]) : new SmartLampWithColor(words[2]);
                break;
            case 4:
                if (!(words[3].equals("On") || words[3].equals("Off"))) {
                    throw new Errorneus();
                }
                boolean isOpen = words[3].equals("On");
                lamp = words[1].equals("SmartLamp") ? new SmartLamp(words[2], isOpen) : new SmartLampWithColor(words[2], isOpen);
                break;
            case 6:
                if (!(words[3].equals("On") || words[3].equals("Off"))) {
                    throw new Errorneus();
                }
                isOpen = words[3].equals("On");
                try {
                    int brightness = Integer.parseInt(words[5]);
                    if (words[4].contains("0x") &&(words[1].equals("SmartColorLamp"))){
                        lamp = new SmartLampWithColor(words[2], isOpen,words[4], brightness,false);
                    }else if(!words[4].contains("0x") &&(words[1].equals("SmartColorLamp")) ){
                        lamp = new SmartLampWithColor(words[2], isOpen, words[4], brightness,true);
                    }else if ( words[1].equals("SmartLamp")){
                        int kelvin = Integer.parseInt(words[4]);
                        lamp =new SmartLamp(words[2], isOpen, kelvin, brightness,true);
                    }
                }catch ( NumberFormatException e ){
                    throw new Errorneus();
                }
                break;
            default:
                throw new Errorneus();
        }return lamp;
    }

    /**
     * Creates a new SmartCamera object and adds it to the deviceList. Throws an Errorneus exception if the number of arguments is less than 4.
     * Throws an AlreadyExist exception if an object with the same name already exists in deviceList.
     * İf camera created with open status, sets starting time of camera.
     * @param words an array of string containing the input command parameters
     * @return the created SmartCamera object
     * @throws AlreadyExist if an object with the same name already exists in deviceList
     * @throws Errorneus if the input parameters are incorrect or incomplete
     */
    public  SmartCamera addingCamera (String[] words ){
        if (words.length < 4 ) throw new Errorneus();
        if (isObjectExist(words[2])) throw new AlreadyExist();
        int storePerMinute= Integer.parseInt(words[3]);
        if(words.length == 4 ){
            camera = new SmartCamera(words[2],storePerMinute);
        } else if (words.length == 5 ) {
            if ((words[4].equals("On")|| words[4].equals(("Off")))){
                boolean status = words[4].equals("On");
                camera = new SmartCamera(words[2],storePerMinute,status);
                if(status) camera.setStartingTime(currentTime);
            }else throw new  Errorneus();
        }else throw new Errorneus();
        return camera;
    }

    /**
     * Adds a new SmartPlug to the list of devices.
     * @param Words an array of words containing device type, device name, and device status (if any).
     * @return the new SmartPlug object that has been added to the list of devices.
     * @throws AlreadyExist if a device with the same name already exists in the list of devices.
     * @throws Errorneus if the input Words array is of invalid length or format.
     */
    public SmartPlug addingPlug(String[] Words ){
        if(Words.length < 3)throw new Errorneus();
        if (isObjectExist(Words[2]))throw new AlreadyExist();
        if (Words.length == 3){
            plug = new SmartPlug(Words[2]);
        }else if ( Words.length == 4){
            boolean status = Words[3].equals("On");
            plug = new SmartPlug(Words[2],status);
            if(status)plug.setStartingTime(currentTime);
        }else if ( Words.length == 5){
            double Ampere = Double.parseDouble(Words[4]);
            plug = new SmartPlug(Words[2],Words[3].equals("On"),Ampere);
            if(Words[3].equals("On"))plug.setStartingTime(currentTime);
        }else throw new Errorneus();
        return plug;
    }
    /**
     * Checks if a Smart Device with the given name already exists in the device list.
     * @param name the name of the Smart Device to check for existence
     * @return true if a Smart Device with the given name exists in the device list, false otherwise
     */
    public boolean isObjectExist(String name){
        for (SmartDevices device: deviceList){
            if (device.getName().equals(name)) {
                return true;
            }
            }return false;
        }
}
