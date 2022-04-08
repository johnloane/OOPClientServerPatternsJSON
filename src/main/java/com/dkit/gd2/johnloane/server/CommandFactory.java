package com.dkit.gd2.johnloane.server;

import com.dkit.gd2.johnloane.core.ComboServiceProtocol;

public class CommandFactory
{
    private int requestCount;

    public CommandFactory(int requestCount)
    {
        this.requestCount = requestCount;
    }

    public Command createCommand(ComboServiceProtocol command)
    {
        Command c = null;
        // Check what the command instruction is
        if(command.equals(ComboServiceProtocol.ECHO))
        {
            // Create add echo command
            c = new EchoCommand();
        }
        else if(command.equals(ComboServiceProtocol.DAYTIME))
        {
            // Create daytime command
            c = new DaytimeCommand();
        }
        else if(command.equals(ComboServiceProtocol.STATS))
        {
            // Create stats
            c = new StatsCommand(requestCount);
        }
        return c;
    }
}
