package run;

import java.io.*;
import java.net.Socket;

/*
Class that creates new threads for every client that connects to server
 */

public class ClientHandler extends Thread{
    final Socket s;
    String line = null;
    BufferedReader in;
    PrintWriter out;


    public ClientHandler(Socket s, BufferedReader in, PrintWriter out){
        this.s = s;
        this.in = in;
        this.out = out;

    }

    @Override
    public void run(){
        try{
            String inputLine, outputLine;
            TCPProtocol protocol = new TCPProtocol();
            outputLine = protocol.processInput(null);

            out.println(outputLine);


            while((inputLine = in.readLine()) != null){
                outputLine = protocol.processInput(inputLine);
                out.println(outputLine);

                if(outputLine.equalsIgnoreCase("K")){
                    break;
                }

                if(inputLine.equalsIgnoreCase("more")){
                    protocol.setState(0);
                }
            }

        }catch (IOException e){
            System.out.println("EIO Error");
        }

    }
}
