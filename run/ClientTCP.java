package run;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ClientTCP {


    public List<String> gatherMails(String emails){
        List<String> emailMatches = new ArrayList<>();

        if(emails.contains(",")){
            emailMatches = Arrays.asList(emails.split(", [ ]*"));
        }else{
            emailMatches.add(emails);
        }
        return emailMatches;
    }




    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";  //Host
        int portNumber = 5555;
        final char TERMINATIONCHAR = '\n';

        if(args.length > 0){
            if(args[0] != null){
                hostName = args[0];
                if(args[1] !=  null){
                    portNumber = Integer.parseInt(args[1]);
                }
            }
        }

        ClientTCP clientMethods = new ClientTCP();

        System.out.println("Welcome, trying to establishing connection to server on " + hostName + ":" + portNumber);

        try(
            //Socket to server
            Socket clientSocket = new Socket(hostName, portNumber);

            //To the server
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);

            //Back to client
            BufferedReader fromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Read from client, input
            BufferedReader toServer = new BufferedReader(new InputStreamReader(System.in));
            )
        {
            String userInput = "";
            char serverCode = 'æ';
            List<String> emailMatches = new ArrayList<>();

            //makes first contact
            System.out.println(fromServer.readLine());
            while ((userInput = toServer.readLine()) != null) {
                out.println(userInput + TERMINATIONCHAR);
                serverCode = (char) fromServer.read();



                if(serverCode == '5'){
                    System.out.println(fromServer.readLine());
                }
                if(serverCode == '6'){
                    System.out.println(fromServer.readLine());
                    System.out.println(fromServer.readLine());
                }
                if(serverCode == '0'){
                    System.out.println("Code 0 - E-mail addresses found: (type 'more' to look up more URLs)");
                    emailMatches = clientMethods.gatherMails(fromServer.readLine());
                    emailMatches.forEach(System.out::println);
                }
                if(serverCode == '1'){
                    System.out.println(serverCode);
                    System.out.println("Code 1 - No Email addresses found on the page.");
                }

            }
            //serverCode is in ASCII - CODE 0
            if(serverCode == 48){
                System.out.println("Code 0 - E-mail addresses found:");
                emailMatches.forEach(System.out::println);

            }else if(serverCode == 49){
                System.out.println("Code 1 - No Email addresses found on the page.");
            }else{
                System.out.println("Code 2 - Server couldn't find the webpage");
            }

        }
        catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (MalformedURLException e){
            System.err.println("Please provide an URL");

        }catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

}
