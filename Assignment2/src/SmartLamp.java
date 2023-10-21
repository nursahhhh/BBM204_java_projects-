import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * The type Smart lamp.
 */
public class SmartLamp extends SmartDevices  {
    /**
     * This Name.
     */
    protected  String name;
    /**
     * This Kelvin holds both kelvin value and colorValue (for child class)
     */
    protected  int kelvin;
    /**
     * This brightness
     */
    protected int brightness ;


    /**
     * Constructor a new Smart lamp.
     */
    public SmartLamp() {
    }

    /**
     * Constructor a new Smart lamp
     * Called other constructor in same class with default value of switchStatus (false)
     * @param name the name
     *
     */
    public SmartLamp(String name) {
        this(name,false);
    }

    /**
     * Constructor a new Smart lamp.
     * Called other constructor in same class with default value of switchStatus (false) ,
     kelvin(4000) , brightness(100)
     * @param name         this name
     * @param switchStatus this switch status
     */
    public SmartLamp(String name, boolean switchStatus) {
        this(name, switchStatus, 4000,100,false);
    }

    /**
     * Constructor a new Smart lamp.
     * "The isKelvin parameter indicates whether the kelvin parameter represents a temperature in Kelvin or a color code."
     * @throws valueError if  kelvin is out of correct interval.
     * @param name         this name
     * @param switchStatus this switch status
     * @param kelvin       this kelvin
     * @param brightness   this brightness
     * @param isKelvin     this is kelvin
     */
    public SmartLamp(String name, boolean switchStatus, int kelvin, int brightness , boolean isKelvin) {


        this.name = name;
        SwitchStatus = switchStatus;
        if (( kelvin < 0x000000 || kelvin > 0xffffff ) && (!isKelvin)){
            throw new  valueError(" Color code value must be in range of 0x0-0xFFFFFF!");
        }
        if (((kelvin < 2000 ) || (kelvin > 6500)) && (isKelvin)){
            throw  new valueError (" Kelvin value must be in range of 2000K-6500K!");
        }
        this.kelvin = kelvin;
        if (( brightness < 0) || (brightness > 100)){
            throw  new  valueError (" Brightness must be in range of 0%-100%!");
        }

        this.brightness = brightness;


    }

    /**
     * Gets name
     * @return name  this name
     */
    @Override
    public String getName() {
        return name;
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
     * Sets  SwitchStatus
     * @param switchStatus  this SwitchStatus
     */
    @Override
    public void setSwitchStatus(boolean switchStatus) {
        SwitchStatus = switchStatus;
    }


    /**
     * Sets kelvin.
     * Checks whether isKelvin holds kelvin value or color value.
     * @throws valueError if  kelvin is out of correct interval.
     * @param kelvin   this kelvin
     * @param isKelvin this isKelvin
     *
     */
    public void setKelvin(int kelvin,boolean isKelvin) {
        if (( kelvin < 0x000000 || kelvin > 0xffffff ) && (!isKelvin)){
            throw new  valueError(" Color code value must be in range of 0x0-0xFFFFFF!");
        }

        if (((kelvin < 2000 ) || (kelvin > 6500)) && (isKelvin)){
            throw  new valueError (" Kelvin value must be in range of 2000K-6500K!");
        }
        this.kelvin = kelvin;

    }

    /**
     * Sets brightness.
     *Checks whether brightness is in correct interval.
     * @param brightness this brightness
     * @throws valueError if brightness value is not within the valid range of 0-100.
     */
    public void setBrightness(int brightness) {

        if (( brightness < 0) || (brightness > 100)){
            throw  new  valueError ("Brightness must be in range of 0%-100%!");
        } else this.brightness = brightness;
    }

    /**
     * Gets Status
     * @return this SwitchStatus
     */
    @Override
    public boolean getSwitchStatus() {

        return SwitchStatus;
    }
    /**
     * If SwitchTime earlier or equal time,
     * Toggles the switch's current status and clears its scheduled switch time (if applicable).
     * Sets the switch's status to the opposite of its current status.
     */
    @Override
    public void Turn(LocalDateTime time) {
        if (switchTime.compareTo(time) <= 0)
        {
            switchTime = null;
        }
        SwitchStatus = !SwitchStatus;
    }
    /**
     * Gets SwitchTime
     *  @return this SwitchTime
     */
    @Override
    public LocalDateTime getSwitchTime() {
        return switchTime;
    }
    /**
     * Sets SwitchTime
     * * @param switchTime
     */
    @Override
    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }
    /**
     * Returns a string representation of this SmartLamp object.
     * This method returns a string that describes the state of this SmartLamp object.
     * The string is formatted using the `String.format()` method and includes placeholders for each of the relevant values.
     * @return a string representation of this SmartLamp object
     */
    @Override
    public String toString() {
        String status = SwitchStatus ? "on":"off";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formattedSwitchTime = switchTime == null ? "null":switchTime.format(formatter);
        return String.format("Smart Lamp %s is %s and its kelvin value is %dK with %d%% brightness, " +
                "and its time to switch its status is %s.",name, status, kelvin, brightness,formattedSwitchTime);

    }
}
