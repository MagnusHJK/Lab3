public class TCPProtocol {
    private static final int WAITING = 0;
    private static final int CHECKINGURL = 1;
    private static final int FOUNDMAIL = 2;
    private static final int NOMAIL = 3;

    private static final int MORE = 4;

    private int state = WAITING;

    public String processInput(String theInput){
        String theOutput = null;

        if(state == WAITING){
            theOutput = "Please provide an URL for me to look at.";

        }else if(state == CHECKINGURL){
            theOutput = "Looking up the provided URL and scanning for Email addresses.";
        }else if(state == FOUNDMAIL){
            theOutput = "";

        }else if(state == NOMAIL){

        }else if(state == MORE){
            state = WAITING;
        }
        return theOutput;

    }
}
