package run;

import java.io.*;
import java.net.*;
import java.sql.SQLOutput;


public class ServerTCP {
    public static void main(String[] args) throws IOException {
        int portNumber = 5555;
        final char TERMINATIONCHAR = '\n';

        if(args.length > 0){
            portNumber = Integer.parseInt(args[0]);
        }

        System.out.println("Welcome to TCP server");

        try(
            //Server socket
            ServerSocket serverSocket = new ServerSocket(portNumber);

        ){
            while (true){
                try{
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    int clientID = 0;

                    ClientHandler client = new ClientHandler(clientSocket, in, out, clientID++);
                    System.out.println("Client #" + clientID + " connected");
                    client.start();
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Connection Error");
                }
            }
        }
    }
}
