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
        //write message in database table postbox_[name] with message = `unknown`

        //crackMessage
        //cracked in 30s?
            //message in database is replaced with cracked message
            //in GUI output area message "intruder [name] cracked message from participant [name] | [message]"
        //not cracked
            //in GUI output area message "intruder [name] | crack message from participant [name] failed"

    }
}
