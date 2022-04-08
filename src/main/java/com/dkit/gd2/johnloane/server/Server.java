package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.ComboServiceDetails;
import com.dkit.gd2.johnloane.core.ComboServiceProtocol;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{
    public static void main(String[] args) {
        try{
            // Set up a connection socket for other programs to connect to
            ServerSocket listeningSocket = new ServerSocket(ComboServiceDetails.SERVER_PORT);

            boolean continueRunning = true;

            while(continueRunning)
            {
                // Step 2) wait for incoming connection and build communications link
                Socket dataSocket = listeningSocket.accept();
                ServerThread runnable = new ServerThread(dataSocket);
                Thread serverThread = new Thread(runnable);
                serverThread.start();
            }

            listeningSocket.close();
        }
        catch(Exception e)
        {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
