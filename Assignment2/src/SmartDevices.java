import java.time.LocalDateTime;

/**
 * The type Smart devices.
 */
public abstract class SmartDevices {
    /**
     * This Switch status.
     */
    protected  boolean SwitchStatus;
    /**
     * This Switch time.
     */
    protected LocalDateTime switchTime;
    /**
     * This Name.
     */
    protected String name;

    /**
     * Gets name.
     *
     * @return the name
     */
    public abstract String getName();

    /**
     * Sets name.
     *
     * @param name the name
     */
    public abstract void setName(String name);

    /**
     * Sets switch status.
     *
     * @param status the a
     */
    public abstract void setSwitchStatus(boolean status );

    /**
     * Gets switch status.
     *
     * @return the switch status
     */
    public abstract boolean getSwitchStatus();

    /**
     * Gets switch time.
     *
     * @return the switch time
     */
    public abstract LocalDateTime  getSwitchTime();

    /**
     * Sets switch time.
     *
     * @param switchTime the switch time
     */
    public abstract void setSwitchTime(LocalDateTime switchTime);

    /**
     * Turn.
     *
     * @param time the time
     */
    public abstract void Turn(LocalDateTime time);

    /**
     * @return String which includes devices information
     */
    public abstract String toString();



}
