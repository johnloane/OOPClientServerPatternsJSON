package com.dkit.gd2.johnloane.client;

import com.dkit.gd2.johnloane.core.ComboServiceDetails;
import com.dkit.gd2.johnloane.core.ComboServiceProtocol;
import com.dkit.gd2.johnloane.core.NetworkMessage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) {
        try
        {
            // Step 1 (on consumer side) - Establish channel of communication
            Socket dataSocket = new Socket("localhost", ComboServiceDetails.SERVER_PORT);

            // Step 2) Build output and input objects
            OutputStream out = dataSocket.getOutputStream();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));

            Scanner keyboard = new Scanner(System.in);
            NetworkMessage responseMessage = new NetworkMessage();
            while(!responseMessage.equals(ComboServiceProtocol.END_SESSION.name()))
            {
                displayMenu();
                int choice = getNumber(keyboard);
                //responseMessage = null;
                if(choice >=0 && choice < 4)
                {
                    switch (choice)
                    {
                        case 0:
                            responseMessage.setMessageType(ComboServiceProtocol.END_SESSION);
                            responseMessage.setPayload("");

                            // Send message
                            output.println(responseMessage);
                            output.flush();

                            String response = input.nextLine();
                            System.out.println("Response " + response);
                            JSONObject responseJSON = new JSONObject(response);
                            responseMessage.readFromJSON(responseJSON);
                            if(responseMessage.getMessageType() == ComboServiceProtocol.SESSION_TERMINATED)
                            {
                                System.out.println("Session ended.");
                            }

                            break;
                        case 1:
                            responseMessage = generateEcho(keyboard);

                            // Send message
                            output.println(responseMessage.writeJSON());
                            output.flush();

                            // Get response
                            response = input.nextLine();
                            System.out.println(response);
                            responseJSON = new JSONObject(response);
                            responseMessage.readFromJSON(responseJSON);
                            System.out.println("Received response: " + responseMessage.getPayload());
                            break;
                        case 2:
                            responseMessage.setMessageType(ComboServiceProtocol.DAYTIME);
                            responseMessage.setPayload("");

                            // Send message
                            output.println(responseMessage.writeJSON());
                            output.flush();

                            // Get response
                            response = input.nextLine();
                            responseJSON = new JSONObject(response);
                            responseMessage.readFromJSON(responseJSON);
                            System.out.println("The current date and time is: " + responseMessage.getPayload());
                            break;

                        case 3:
                            responseMessage.setMessageType(ComboServiceProtocol.STATS);

                            // Send message
                            output.println(responseMessage.writeJSON());
                            output.flush();

                            // Get response
                            response = input.nextLine();
                            responseJSON = new JSONObject(response);
                            responseMessage.readFromJSON(responseJSON);
                            System.out.println("The current date and time is: " + responseMessage.getPayload());
                            break;
                    }
                    if(responseMessage.getMessageType() == ComboServiceProtocol.UNRECOGNISED)
                    {
                        System.out.println("Sorry, that request cannot be recognised.");
                    }
                }
                else
                {
                    System.out.println("Please select an option from the menu");
                }
            }
            System.out.println("Thank you for using the (Stream-based) Combo system.");
            dataSocket.close();
        }catch(Exception e)
        {
            System.out.println("An error occurred: "  + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void displayMenu()
    {
        System.out.println("0) Exit");
        System.out.println("1) Echo a message");
        System.out.println("2) Get the current day and time");
        System.out.println("3) Stats - get how many requests the server has responded to");
    }

    // A method to retrieve a number from the user at all times
    // This method deals with repeatedly requesting a number from the user
    // until they enter numeric data instead of text.
    public static int getNumber(Scanner keyboard)
    {
        boolean numberEntered = false;
        int number = 0;
        while(!numberEntered)
        {
            try{
                number = keyboard.nextInt();
                numberEntered = true;
            }
            catch(InputMismatchException e)
            {
                System.out.println("Please enter a number.");
                keyboard.nextLine();
            }
        }
        keyboard.nextLine();
        return number;
    }


    // Methods to provide command functionality on client side

    public static NetworkMessage generateEcho(Scanner keyboard)
    {
        // Set up command text
        NetworkMessage message = new NetworkMessage();
        message.setMessageType(ComboServiceProtocol.ECHO);

        // Get message to be echoed
        System.out.println("Please enter the message to be echoed: ");
        String echo = keyboard.nextLine();
        // Add new text to end of command string
        message.setPayload(echo);

        // Return final message
        return message;
    }
}
