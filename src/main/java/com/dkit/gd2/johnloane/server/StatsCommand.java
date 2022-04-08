package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.ComboServiceProtocol;
import com.dkit.gd2.johnloane.core.NetworkMessage;

import java.util.Date;

public class StatsCommand implements Command
{
    private int requestCount;

    public StatsCommand(int requestCount)
    {
        this.requestCount = requestCount;
    }

    @Override
    public NetworkMessage createResponse(NetworkMessage incoming)
    {
        NetworkMessage response = new NetworkMessage();
        response.setMessageType(ComboServiceProtocol.STATS);
        response.setPayload("The number of requests is " + requestCount);
        return response;
    }
}
