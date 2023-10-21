import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Smart camera.
 */
public class SmartCamera extends SmartDevices {
    private int storagePerRecord;
    private double StorageUsage;
    private LocalDateTime  startingTime;


    /**
     * Constructor a new Smart camera.
     * Called other constructor of this class with default SwitchStatus (false).
     * @param name             the name
     * @param storagePerMinute the storage per minute
     */
    public SmartCamera(String name ,int storagePerMinute) {
     this(name,storagePerMinute,false);
    }

    /**
     *  Constructor a new Smart camera.
     * @throw valueError throw  if storagePerRecord is not  positive number.
     * @param name             the name
     * @param storagePerRecord the storage per record
     * @param SwitchStatus     the switch status
     */
    public SmartCamera(String name ,int storagePerRecord,boolean SwitchStatus) {
        this.name = name;
        if ( storagePerRecord <= 0) {
            throw new valueError(" Megabyte value must be a positive number!");
                    }
        this.storagePerRecord = storagePerRecord;
        this.SwitchStatus = SwitchStatus;
    }

    /**
     * Calculates usage of store.
     * Calculates timeDifference between startingTime and time  in Duration type.
     * @param time the time
     */
    public void StoreUsageCalculator (LocalDateTime time) {
        Duration duration = Duration.between(startingTime, time);
        long timePassing = duration.toMinutes();
        StorageUsage = (int) timePassing * storagePerRecord;
    }

    /**
     * Sets name
     * @param name this name
     */

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets status
     * @param status this status
     */
    @Override
    public void setSwitchStatus(boolean status) {
        this.SwitchStatus = status;
    }

    /**
     * Gets status
     * @return SwitchStatus
     */
    @Override
    public boolean getSwitchStatus() {
        return SwitchStatus;
    }

    /**
     * Gets SwitchTime
     * @return switchTime
     */
    @Override
    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    /**
     * Sets SwitchTime
     * Before changing switchTime , starting time assigning to switchTime for calculation.
     * @param switchTime
     */
    @Override
    public void setSwitchTime(LocalDateTime switchTime) {
        startingTime = this.switchTime;
        this.switchTime = switchTime;


    }

    /**
     * Sets starting time.
     * @param startingTime the starting time
     */
    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    /**
     * Returns a string representation of this SmartCamera object.
     * This method returns a string that describes the state of this SmartCamera object.
     * The string is formatted using the `String.format()` method and includes placeholders for each of the relevant values.
     * @return a string representation of this SmartCamera object
     */

    @Override
    public String toString() {
        String openOrClose = SwitchStatus ? "on":"off";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formattedSwitchTime = switchTime == null ? "null":switchTime.format(formatter);
       return String.format("Smart Camera %s is %s and used %.2f MB of storage so far (excluding current status)," +
                       " and its time to switch its status is %s.", name, openOrClose, StorageUsage,formattedSwitchTime);
    }

    /**
     * If SwitchTime earlier or equal time,
     * Toggles the switch's current status and clears its scheduled switch time (if applicable).
     * Updates startingTimes
     * Sets the switch's status to the opposite of its current status.
     *
     */
    public void Turn(LocalDateTime time) {
        if (SwitchStatus ){
           // StoreUsage(time);
        }
        if (switchTime.compareTo(time) <= 0)
        {
            switchTime = null;
        }
        this.startingTime = switchTime;
        this.SwitchStatus = !SwitchStatus;
    }

    /***
     * Gets name
     * @return this name
     */

    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets storage usage.
     *
     * @return this storage usage
     */
    public double getStorageUsage() {
        return StorageUsage;
    }
}
