package run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCPProtocol{
    private static final int WAITING = 0;   //Greetings
    private static final int CHECKINGURL = 1;   //Checks the given URL
    private static final int FOUNDMAIL = 2; //Email(s) was found
    private static final int NOMAIL = 3;    //No email(s) was found
    private static final int MORE = 4;  //Client wants to look up more
    private static final int KILL = 5;  //Server kills the connection
    ArrayList<String> emailMatches = new ArrayList<>(); //The found email(s)
    private final char TERMINATIONCHAR = '\n';


    private int state = WAITING;

    public void setState(int state) {
        this.state = state;
    }

    public String processInput(String theInput){
        String theOutput = null;

        if(state == WAITING){
            System.out.println("STATE: WAITING");
            theOutput = "APlease provide an URL for me to look at.";
            state = CHECKINGURL;
            return theOutput;
        }
        else if(state == CHECKINGURL){
            System.out.println("STATE: CHECKING URL");
            theOutput = "BLooking up the provided URL and scanning for Email addresses.";

            if(emailMatches.size() > 0)
                emailMatches.clear();

            if(theInput != null){
                try{
                    URL url = new URL(theInput);

                    URLConnection urlConnection = url.openConnection();

                    InputStream inURL = urlConnection.getInputStream();
                    BufferedReader readURL = new BufferedReader(new InputStreamReader(inURL));
                    String urlResponse = "";


                    Pattern emailPattern = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})");


                    while((urlResponse = readURL.readLine()) != null){
                        Matcher regExMatcher = emailPattern.matcher(urlResponse);

                        while(regExMatcher.find()){
                            emailMatches.add(regExMatcher.group());
                        }
                    }
                    if(emailMatches.size() > 0){
                        System.out.println(emailMatches.size() + " Emails found! Sending to client");
                        state = FOUNDMAIL;

                    }else{
                        state = NOMAIL;
                    }
                }catch (MalformedURLException e){
                    System.err.println("Error, not a URL");
                    theOutput = "EError, that was not a URL";
                    state = MORE;
                }catch (IOException e){
                    System.err.println("Error for I/O");
                    theOutput = "EError for I/O";
                }
            }
        }

        if(state == FOUNDMAIL){
            System.out.println("STATE: FOUNDMAIL");
            state = MORE;
            return "0" + emailMatches.toString();
        }


        else if(state == NOMAIL){
            System.out.println("STATE: NOMAIL");
            theOutput = "1No mail was found";
            state = MORE;
            return theOutput;
        }

        if(state == MORE){
            System.out.println("STATE: MORE");
            if(theInput.equalsIgnoreCase("more")){
                theOutput = "APlease provide an URL for me to look at.";
                state = CHECKINGURL;
                return theOutput;
            }else{
                state = KILL;
            }
        }

        if(state == KILL){
            theOutput = "K";
        }
        return theOutput;
    }
}
