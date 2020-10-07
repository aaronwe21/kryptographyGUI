package network;

import com.google.common.eventbus.Subscribe;

import java.io.File;

public class ParticipantIntruder extends Participant{

    public ParticipantIntruder(int id, String name)
    {
        super(id, name);
    }

    @Subscribe
    public void receiveMessage(Message message) {
        //crackMessage
    }
}
