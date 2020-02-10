package run;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


/*
Client
 */

public class ClientTCP extends Thread{

    private String hostName;
    private int portNumber;


    ClientTCP(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
    }


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
            hostName = args[0];

            if(args[1] != null){
                portNumber = Integer.parseInt(args[1]);
            }
        }

        ClientTCP client = new ClientTCP(hostName, portNumber);


        System.out.println("Welcome, trying to establishing connection to server on " + hostName + ":" + portNumber);

        try(
            //Socket to server
            Socket clientSocket = new Socket(hostName, portNumber);

            //To the server
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);

            //Back to client
            BufferedReader in =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Read from client, input
            BufferedReader toServer = new BufferedReader(new InputStreamReader(System.in));
            ) {
            //User input, in client
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String fromUser = "";
            String fromServer = "";
            char serverCode = 'æ';

            while((fromServer = in.readLine()) != null){
                if(fromServer.length() == 0){
                    TimeUnit.SECONDS.sleep(5);
                }
                if(fromServer.charAt(0) == 'A' ||fromServer.charAt(0) == 'B' || fromServer.charAt(0) == 'E'){
                    System.out.println("Server: "+ fromServer.substring(1));
                }

                else if(fromServer.equals("K")){
                    clientSocket.shutdownOutput();
                    System.out.println("CONNECTION IS KILLED");
                    break;
                }

                else if(fromServer.charAt(0) == '0'){
                    System.out.println("Code 0 - E-mail addresses found:");

                    List<String> emailMatches = new ArrayList<>();
                    emailMatches = client.gatherMails(fromServer.substring(1));
                    emailMatches.forEach(System.out::println);

                    System.out.println("Type \"more\" to look up again, or \"end\" if you are done.");
                }

                else if(fromServer.charAt(0) == '1'){
                    System.out.println("Code 1 - No Email addresses found on the page.");
                    System.out.println("Type \"more\" to look up again, or \"end\" if you are done.");
                }

                fromUser = stdIn.readLine();
                if(fromUser != null){
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
