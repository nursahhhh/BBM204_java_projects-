import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This type Smart plug.
 */
public class SmartPlug extends SmartDevices {
    /**
     * This IsPlugIn created as protected because child class should reach this values.
     */
    protected boolean isPlugIn ;
    /**
     * This energyConsumption created as private because it is special for this class.
     */
    private  double energyConsumption;
    /**
     * This Ampere created as private because it is special for this class.
     */
    private double Ampere ;
    /**
     * This StartingTime created as private because it is special for this class.
     */
    private LocalDateTime StartingTime;
    /**
     * voltage created as final because it is default and can not change.
     */
    private final int  voltage = 220;

    /**
     * Constructor a new Smart plug.
     *
     * @param name the name
     */
    public SmartPlug(String name) {
      this(name,false);
    }

    /**
     * Constructor a new Smart plug.
     * Declares default value for energyConsumption.
     * @param name         the name
     * @param SwitchStatus the switch status
     */
    public SmartPlug(String name,boolean SwitchStatus) {
        this.name = name;
        this.SwitchStatus = SwitchStatus;
        this.energyConsumption = 0;
    }

    /**
     * Constructor a new Smart plug.
     * @throw valueError if amper value equal or smaller than zero.
     * @param name         the name
     * @param SwitchStatus the switch status
     * @param ampere       the ampere
     */
    public SmartPlug(String name, boolean SwitchStatus, double ampere) {
        this.energyConsumption = 0;
        this.name = name;
        this.SwitchStatus = SwitchStatus;
        if (ampere <=0) throw new valueError(" Ampere value must be a positive number!");
        this.Ampere =ampere;

    }

    /**
     * Calculates energy consumption.
     * Checks whether Starting time is null or not to avoid nullPointerException.
     * Updates StartingTime.
     * @param NextTime the next time
     */
    public void calculateEnergyConsumption( LocalDateTime NextTime) {

        if (StartingTime!= null) {
            Duration duration = Duration.between(StartingTime, NextTime);
            StartingTime = NextTime;
            double timePassing = (double) duration.toMinutes();
            double hour = timePassing / 60.00;

            energyConsumption += voltage * Ampere * hour;
        }
    }

    /**
     * If SwitchTime earlier or equal time,
     * Toggles the switch's current status and clears its scheduled switch time (if applicable).
     * Sets the switch's status to the opposite of its current status.
     *
     */
    @Override
    public void Turn(LocalDateTime time) {
        // if plug on then Turn() close the plug.
        // And the energy consumed in the time between opening and closing was calculated.

        if (switchTime.compareTo(time) <= 0)
        {
            switchTime = null;
        }
        this.SwitchStatus = !SwitchStatus;

    }

    /**
     * Returns a string representation of this SmartPlug object.
     * This method returns a string that describes the state of this SmartPlug object.
     * The string is formatted using the `String.format()` method and includes placeholders for each of the relevant values.
     * @return a string representation of this SmartPlug object
     */

    @Override
    public String toString() {
        String openOrClose = SwitchStatus ? "on":"off";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String formattedSwitchTime = switchTime == null ? "null":switchTime.format(formatter);
       return String.format("Smart Plug %s is %s and consumed %.2fW so far (excluding current device)," +
                       " and its time to switch its status is %s.", name, openOrClose, energyConsumption,formattedSwitchTime);

    }

    /**
     * Sets energy consumption.
     *
     * @param energyConsumption the energy consumption
     */
    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    /**
     * Gets ampere.
     *
     * @return this ampere
     */
    public double getAmpere() {
        return Ampere;
    }

    /**
     * Sets ampere.
     *
     * @param ampere this ampere
     */
    public void setAmpere(double ampere) {
        if (ampere <= 0) throw new valueError(" Ampere value must be a positive number!");
        this.Ampere= ampere;

    }

    /**
     * Sets plugIn.
     *
     * @param plugIn this plug ın
     */
    public void setPlugIn(boolean plugIn) {
        isPlugIn = plugIn;
    }

    /**
     * IsPlugIn boolean.
     *
     * @return this boolean
     */
    public boolean isPlugIn() {
        return isPlugIn;
    }

    /**
     * Gets starting time.
     *
     * @return this starting time
     */
    public LocalDateTime getStartingTime() {
        return StartingTime;
    }

    /**
     * Sets starting time.
     *
     * @param startingTime the starting time
     */
    public void setStartingTime(LocalDateTime startingTime) {
        StartingTime = startingTime;
    }

    /**
     * Gets name
     * @return this name.
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
     * Sets switchStatus
     * @param status this status
     */
    @Override
    public void setSwitchStatus(boolean status) {
        SwitchStatus = status;
    }

    /**
     * Gets SwitchStatus
     * @return SwitchStatus
     */
    @Override
    public boolean getSwitchStatus() {
        return SwitchStatus;
    }

    /**
     * Gets SwitchStatus
     * @return switchTime
     */
    @Override
    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    /**
     * Sets switchTime
     * @param switchTime this switchTime
     */
    @Override
    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }


}
