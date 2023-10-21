import java.time.format.DateTimeFormatter;

/**
 * The type Smart lamp with color.
 */
public class SmartLampWithColor extends SmartLamp{
    /*
     * <color> holds colorCode(0x00000 format ) and kelvin as String.
     */
    private String  color;

    /**
     * Constructor a new Smart lamp with color.
     */
    public SmartLampWithColor() {
    }

    /**
     * Constructor a new Smart lamp with color.
     * Call constructor of parent class.
     * Declare default value for color.
     * @param name the name og object
     */
    public SmartLampWithColor(String name) {
        super(name);
        this.color = "4000K";
    }

    /**
     * Constructor a new Smart lamp with color.
     * Call constructor of parent class with name and switchStatus
     *  Declare default value for color.
     * @param name         this name
     * @param switchStatus this switch status
     */
    public SmartLampWithColor(String name, boolean switchStatus) {
        super(name, switchStatus);
        this.color = "4000K";
    }

    /**
     * Constructor a new Smart lamp with color.
     * Decides  whether type of color is hexadecimal or integer by using ternary assignment,
     *  Converts color to integer with parseInt()
     * If iskelvin is true, color is kelvin format ; else color is hexadecimal format.
     * Call constructor of parent class with name,switchStatus,the result of ternary assignment,brightness and isKelvin
     * @param name         the name
     * @param switchStatus the switch status
     * @param color        the color
     * @param brightness   the brightness
     * @param isKelvin     the is kelvin
     */
    public SmartLampWithColor(String name, boolean switchStatus, String color, int brightness,boolean isKelvin) {
        super(name,
                switchStatus, isKelvin ?  Integer.parseInt(color):
                        Integer.parseInt(color.substring(2), 16),
                            brightness,isKelvin);
        this.color = isKelvin ? color + "K" : color;



    }


    /**
     * Sets color.
     *
     * @param color    the color
     * @param isKelvin the is kelvin
     */
    public void setColor(String color, boolean isKelvin) {
        this.color = isKelvin ? color + "K" : color;
    }
    /**
     * Returns a string representation of this SmartColorLamp object.
     * This method returns a string that describes the state of this SmartColorLamp object.
     * The string is formatted using the `String.format()` method and includes placeholders for each of the relevant values.
     * @return a string representation of this SmartColorLamp object
     */
    @Override
    public String toString() {
        String status = SwitchStatus ? "on":"off";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formattedSwitchTime = switchTime == null ? "null.": switchTime.format(formatter) +".";
        return "Smart Color Lamp " + name + " is " + status +
                " and its color value is " + color +
                " with " + brightness + "% brightness," + " and its time to switch its status is "+ formattedSwitchTime;
    }


}
