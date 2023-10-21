public class Main {
    public static void main(String[] args) {
        String[] contents = IOClass.readFile(args[0]);
        IOClass.getOutputName(args[1]);
        library library = new library();
        library.ReadCommand(contents);

    }
}