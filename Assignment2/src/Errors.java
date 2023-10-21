public class Errors extends java.lang.Error {
    public Errors(String message) {
        super ("ERROR :" + message);
    }
}
class valueError extends Errors{
    public valueError (String message){
        super( message);
    }
}
class AlreadyExist extends Errors{
    public AlreadyExist() {
        super(" There is already a smart device with same name!");
    }
}
class  Errorneus extends Errors{
    public Errorneus(){
        super(" Erroneous command!");
    }
}
class DoesntExist extends Errors{
    public DoesntExist() {
        super(" There is not such a device!");
    }

}
