import java.io.*;
import java.net.*;

public class ClientTCP {
    public static void main(String[] args) throws IOException {

        String hostName = "fe80::a00:27ff:fece:6f90";  //Host
        int portNumber = 5555;

        if(args.length > 0){
            if(args[0] != null){
                hostName = args[0];
                if(args[1] !=  null){
                    portNumber = Integer.parseInt(args[1]);
                }
            }
        }


        System.out.println("Welcome, trying to establishing connection to server on " + hostName + ":" + portNumber);

        try(
            //Socket to server
            Socket clientSocket = new Socket(hostName, portNumber);

            //To the server
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);

            //Back to client
            BufferedReader in =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Read from client, input
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            )
        {
            String userInput;

            System.out.println("Please give URL so the server can look for Emails");
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Client: " + in.readLine());
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
