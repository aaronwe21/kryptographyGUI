package persistence;

import network.Channel;
import network.Participant;

import java.util.ArrayList;

public enum DataStore {
    instance;

    private ArrayList<Participant> participants = new ArrayList<Participant>();
    private ArrayList<Channel> channels = new ArrayList<Channel>();

    public void addParticipant(Participant participant)
    {
        participants.add(participant);
    }

    public void removeParticipant(Participant participant)
    {
        if(participants.contains(participant))
        {
            participants.remove(participant);
        }
    }

    public void addChannel(Channel channel)
    {
        channels.add(channel);
    }

    public void removeChannel(Channel channel)
    {
        if (channels.contains(channel))
        {
            channels.remove(channel);
        }
    }
}