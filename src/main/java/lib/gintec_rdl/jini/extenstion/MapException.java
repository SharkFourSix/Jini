package lib.gintec_rdl.jini.extenstion;

public class MapException extends RuntimeException {
    public MapException(String message){
        super(message);
    }

    public MapException(Class type){
        super("Failed to map type " + type + ". Make sure a mapper was registered.");
    }

    public MapException(String message, Exception e){
        super(message, e);
    }

    public MapException(Exception e){
        super(e);
    }
}
