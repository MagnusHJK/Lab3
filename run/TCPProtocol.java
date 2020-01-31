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
    private static final int WAITING = 0;
    private static final int CHECKINGURL = 1;
    private static final int FOUNDMAIL = 2;
    private static final int NOMAIL = 3;

    private static final int MORE = 4;

    private int state = WAITING;

    public void setState(int state) {
        this.state = state;
    }

    public String processInput(String theInput){
        String theOutput = null;

        if(state == WAITING){
            theOutput = "Please provide an URL for me to look at.";
            state = CHECKINGURL;
            return theOutput;
        }
        else if(state == CHECKINGURL){
            theOutput = "6 Looking up the provided URL and scanning for Email addresses.";

            if(theInput != null){
                try{
                    URL url = new URL(theInput);

                    URLConnection urlConnection = url.openConnection();

                    InputStream inURL = urlConnection.getInputStream();
                    BufferedReader readURL = new BufferedReader(new InputStreamReader(inURL));
                    String urlResponse = "";

                    Pattern emailPattern = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})");

                    ArrayList<String> emailMatches = new ArrayList<>();


                    while((urlResponse = readURL.readLine()) != null){
                        Matcher regExMatcher = emailPattern.matcher(urlResponse);

                        while(regExMatcher.find()){
                            emailMatches.add(regExMatcher.group());
                        }
                    }
                    if(emailMatches.size() > 0){
                        System.out.println("0" + emailMatches.size() + " Emails found! Sending to client");
                        state = MORE;
                        return "0" + emailMatches.toString();

                    }else{
                        state = NOMAIL;
                    }
                }catch (MalformedURLException e){
                    System.err.println("Error, not a url");
                    state = MORE;
                }catch (IOException e){
                    System.err.println("Error for I/O");
                }
            }
        }

        else if(state == NOMAIL){
            theOutput = "1 No mail was found";
            state = WAITING;

        }
        else if(state == MORE){
            state = WAITING;
        }
        return theOutput;
    }
}
