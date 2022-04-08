package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.ComboServiceDetails;
import com.dkit.gd2.johnloane.core.ComboServiceProtocol;
import com.dkit.gd2.johnloane.core.NetworkMessage;

public class EchoCommand implements Command
{
    @Override
    public NetworkMessage createResponse(NetworkMessage incoming)
    {
        NetworkMessage response = new NetworkMessage();
        response.setMessageType(ComboServiceProtocol.ECHO);
        if(!incoming.getPayload().equals(""))
        {
            response.setPayload(incoming.getPayload());
        }

        return response;
    }
}
