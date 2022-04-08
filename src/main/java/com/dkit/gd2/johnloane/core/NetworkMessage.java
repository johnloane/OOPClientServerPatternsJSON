package com.dkit.gd2.johnloane.core;

import org.json.JSONObject;

public class NetworkMessage
{
    private ComboServiceProtocol messageType;
    private String payload;

    public NetworkMessage()
    {
    }

    public NetworkMessage(ComboServiceProtocol messageType, String payload)
    {
        this.messageType = messageType;
        this.payload = payload;
    }

    public ComboServiceProtocol getMessageType()
    {
        return messageType;
    }

    public String getPayload()
    {
        return payload;
    }

    public JSONObject writeJSON()
    {
        JSONObject jo = new JSONObject();
        jo.put("messageType", this.messageType);
        jo.put("payload", this.payload);
        return jo;
    }

    public void readFromJSON(JSONObject jo)
    {
        this.setMessageType(ComboServiceProtocol.valueOf(jo.get("messageType").toString()));
        this.setPayload(jo.get("payload").toString());
    }

    public void setPayload(String payload)
    {
        this.payload = payload;
    }

    public void setMessageType(ComboServiceProtocol messageType)
    {
        this.messageType = messageType;
    }

    @Override
    public String toString()
    {
        return "NetworkMessage{" +
                "messageType=" + messageType +
                ", payload='" + payload + '\'' +
                '}';
    }
}
