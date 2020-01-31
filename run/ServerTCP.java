package run;

import java.io.*;
import java.net.*;


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

            //Client
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){

            String inputLine, outputLine;
            TCPProtocol protocol = new TCPProtocol();
            outputLine = protocol.processInput(null);
            out.println(outputLine);

            while((inputLine = in.readLine()) != null){
                outputLine = protocol.processInput(inputLine);

                out.println(outputLine);

                if(inputLine.equalsIgnoreCase("more")){
                    protocol.setState(0);
                }
            }
        }
    }
}
