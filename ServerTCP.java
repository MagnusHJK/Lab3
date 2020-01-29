import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerTCP {
    public static void main(String[] args) throws IOException {
        int portNumber = 5555;

        if(args.length > 0){
            portNumber = Integer.parseInt(args[0]);
        }

        System.out.println("Welcome to TCP server");

        try(
            //Server socket
            ServerSocket serverSocket = new ServerSocket(portNumber);

            //Client
            Socket clientSocket = serverSocket.accept();


            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            String userInput;
            ArrayList<String> emailMatches = new ArrayList<>();

            while(true){
                userInput = in.readLine();
                URL url = new URL(userInput);
                URLConnection urlConnection = url.openConnection();
                InputStream inURL = urlConnection.getInputStream();
                BufferedReader readURL = new BufferedReader(new InputStreamReader(inURL));

                String urlResponse = null;

                Pattern emailPattern = Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})");


                while((urlResponse = readURL.readLine()) != null){
                    Matcher regExMatcher = emailPattern.matcher(urlResponse);

                    while(regExMatcher.find()){
                        emailMatches.add(regExMatcher.group());
                    }

                }
                if(emailMatches.size() > 0){
                    System.out.println(emailMatches.size() + " matches for emails found, sending to client");
                    out.println(emailMatches);
                }

                emailMatches.clear();
            }
        }
        catch (MalformedURLException e){
            System.err.println("Please provide an URL");
        }
    }
}
