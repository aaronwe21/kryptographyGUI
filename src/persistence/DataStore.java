package persistence;

import network.Channel;
import network.Participant;

import java.util.ArrayList;

public enum DataStore {
    instance;

    private ArrayList<Participant> participants = new ArrayList<Participant>();
    private ArrayList<Channel> channels = new ArrayList<Channel>();

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public void removeParticipant(Participant participant) {
        participants.remove(participant);
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public Participant getParticipantByName(String name) {
        for (Participant p : participants) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    public Participant getParticipantByID(int id) {
        for (Participant p : participants) {
            if (p.getId() == id) {
                return p;
            }
        }

        return null;
    }
}
