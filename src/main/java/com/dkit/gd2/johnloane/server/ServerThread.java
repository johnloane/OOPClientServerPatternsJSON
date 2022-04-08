package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.ComboServiceDetails;
import com.dkit.gd2.johnloane.core.ComboServiceProtocol;
import com.dkit.gd2.johnloane.core.NetworkMessage;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread implements Runnable
{
    private Socket dataSocket;

    public ServerThread(Socket dataSocket)
    {
        this.dataSocket = dataSocket;
    }

    @Override
    public void run()
    {
        try
        {
            // Step 3) Build output and input objects
            OutputStream out = dataSocket.getOutputStream();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));
            NetworkMessage incomingMessage = new NetworkMessage();
            incomingMessage.setMessageType(ComboServiceProtocol.NONE);
            NetworkMessage responseMessage = new NetworkMessage();
            int requestCount = 0;
            while (incomingMessage.getMessageType() != ComboServiceProtocol.END_SESSION)
            {
                // Wipe the response to make sure we never use an old value
                responseMessage = null;

                // take in information from the client
                String incoming = input.nextLine();
                System.out.println(incoming);
                JSONObject incomingJSON = new JSONObject(incoming);
                incomingMessage.readFromJSON(incomingJSON);
                requestCount++;

                CommandFactory factory = new CommandFactory(requestCount);
                // Figure out which command was sent by the client
                // I.e. what does the client want to do?
                Command command = factory.createCommand(incomingMessage.getMessageType());
                // If you get a command back from the factory, then the command is an accepted action
                // If not, either the client requested to close the session
                // OR the command is not legitimate and the server should return the unrecognised command message
                if (command != null)
                {
                    // Take the remaining text the client sent (i.e. all the information provided)
                    // and execute the requested action (e.g. echo the message back etc)
                    responseMessage = command.createResponse(incomingMessage);
                } else if (incomingMessage.getMessageType() == ComboServiceProtocol.END_SESSION)
                {
                    responseMessage.setMessageType(ComboServiceProtocol.END_SESSION);
                    responseMessage.setPayload("Session Terminated");
                } else
                {
                    // If information was missing, set the response to inform the
                    // client that the command wasn't recognised
                    responseMessage.setMessageType(ComboServiceProtocol.END_SESSION);
                    responseMessage.setPayload("Unrecognised");
                }

                // Send back the computed response
                output.println(responseMessage.writeJSON());
                output.flush();
            }
            // Shut down connection
            dataSocket.close();
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }
}
