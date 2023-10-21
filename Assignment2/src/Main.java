import java.time.format.DateTimeParseException;

public class Main {
    /**
     * The main method of the program. It reads the input file, gets the output file name,
     * and calls the readInputs method to process the commands.
     * @param args an array of Strings containing the input file path and the output file name.
     */
    public static void main(String[] args) {
        String[] inputs = IOClass.readFile(args[0]);
        IOClass.getOutputName(args[1]);
        assert inputs != null;
        readInputs(inputs);
    }

    /**
     * This method reads an array of String inputs and executes the commands in each input line.
     * It uses an instance of the ControllingTime class to control the devices and the time.
     * It iterates through the input array, skips empty lines, and logs the executed commands.
     * If the first line is not a "SetInitialTime" command, the program terminates.
     * If an error occurs during the execution of a command, the method catches and logs the corresponding exception message.
     * If the last line of the input array is not a "ZReport" command, the method generates a "ZReport" and logs it to the output file.
     * @param inputs an array of Strings containing the commands to execute
     */

    public static void readInputs (String[] inputs) {
            ControllingTime controls = new ControllingTime();

            assert inputs != null;
            boolean checkInitial= false;
            for (int i = 0; i < inputs.length; i++) {
                if (inputs[i].trim().isEmpty()) continue;
                IOClass.writeToOutputFile("COMMAND: " + inputs[i]);
                if (! checkInitial){
                    checkInitial = true;
                    if(!controls.isInitialTimeSet(inputs[i])) break;
                    continue;
                }
                    String[] words = inputs[i].split("\t");
                    try {
                        switch (words[0]) {
                            case "Add":
                                controls.adding( words);
                                break;
                            case "Remove":
                                controls.Removed(inputs[i]);
                                break;
                            case "SetTime":
                                controls.SetTime(inputs[i]);
                                break;
                            case "SkipMinutes":
                                controls.skipTime(words);
                                break;
                            case "SetSwitchTime":
                                controls.setSwitchTime(words[1], words[2]);
                                break;
                            case "Switch":
                                controls.SwitchDevice(inputs[i]);
                                break;
                            case "ChangeName":
                                controls.changeName(inputs[i]);
                                break;
                            case "PlugIn": case "PlugOut":
                                controls.ChangePlug(words);
                                break;
                            case "SetKelvin": case "SetBrightness": case "SetColorCode": case "SetWhite": case "SetColor":
                                controls.setterForLamps(inputs[i]);
                                break;
                            case "Nop":
                                controls.Nop();
                                break;
                            case "ZReport":
                                controls.ZReporter();
                                break;
                            default:
                                throw new Errorneus();
                        }
                    } catch (DateTimeParseException D) {
                        IOClass.writeToOutputFile("ERROR: Format of the initial date is wrong! Program is going to terminate!");
                        break;
                    } catch (Errorneus e) {
                        IOClass.writeToOutputFile(e.getMessage());
                    } catch (valueError v) {
                        IOClass.writeToOutputFile(v.getMessage());
                    } catch (Errors E) {
                        IOClass.writeToOutputFile(E.getMessage());
                    }
                }
        if (!(inputs[(inputs.length)-1].contains("ZReport"))) {
            IOClass.writeToOutputFile("ZReport: ");
            controls.ZReporter();
        }
            }
    }
