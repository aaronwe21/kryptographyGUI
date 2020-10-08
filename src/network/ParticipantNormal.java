package network;

import com.google.common.eventbus.Subscribe;

import java.io.File;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantNormal extends Participant {

    private File key;
    private List<Channel> channels = new ArrayList<Channel>();

    public ParticipantNormal(int id, String name)
    {
        super(id, name);
    }

    @Subscribe
    public void receiveMessage(Message message) {
        if(this.id != message.getParticipantFromID())
        {
            //encryptMessage
        }
    }

    @Subscribe
    public void receiveKey(File keyFile) {
        this.key = keyFile;
    }

    public void addChannel(Channel channel)
    {
        this.channels.add(channel);
    }

    public void removeChannel(Channel channel)
    {
        if (channels.contains(channel))
        {
            channels.remove(channel);
        }
    }

    public void sendMessage()
    {

    }


}
