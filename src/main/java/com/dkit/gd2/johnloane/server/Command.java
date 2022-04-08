package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.NetworkMessage;

public interface Command
{
    public NetworkMessage createResponse(NetworkMessage incomingMessage);
}
