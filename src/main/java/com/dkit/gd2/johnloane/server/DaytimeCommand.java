package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.ComboServiceProtocol;
import com.dkit.gd2.johnloane.core.NetworkMessage;

import java.util.Date;

public class DaytimeCommand implements Command
{
    @Override
    public NetworkMessage createResponse(NetworkMessage incoming)
    {
        NetworkMessage response = new NetworkMessage();
        response.setMessageType(ComboServiceProtocol.DAYTIME);
        response.setPayload(new Date().toString());
        return response;
    }
}
