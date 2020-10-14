package network;

import com.google.common.eventbus.Subscribe;
import commands.Commands;
import configuration.Configuration;
import gui.GUI;
import persistence.DataStore;
import persistence.HSQLDB;

import java.io.File;
import java.nio.charset.IllegalCharsetNameException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            //decryptMessage
            String decryptedMessage = Commands.decryptMessage(message.getEncryptedMessage(), message.getAlgorithmType(), key.getName());
            //write message in database table postbox_[participant_name]
            HSQLDB.instance.insertDataTablePostbox(message.getParticipantFromID(), this.name, decryptedMessage, message.getUnixTimeStamp());
            //write message "[participant_name] received new message]" in GUI output area
            String outputText = this.name + " received new message";
            System.out.println("--- " + outputText);
            Configuration.instance.gui.writeTextToOutputArea(outputText);
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

    public void removeChannel(Channel channel) {
        if (channels.contains(channel))
        {
            channels.remove(channel);
        }
    }

    public void sendMessage(Participant participant02, Message message, File keyFile)
    {
        try {
            //get name from channel
            String statement1 = "SELECT name FROM channel WHERE participant_01 = " + message.getParticipantFromID() + " AND participant_02 = " + participant02.getId();
            String statement2 = "SELECT name FROM channel WHERE participant_01 = " + participant02.getId() + " AND participant_02 = " + message.getParticipantFromID();
            ResultSet resultSet1 = HSQLDB.instance.getDataFromManualSQL(statement1);
            ResultSet resultSet2 = HSQLDB.instance.getDataFromManualSQL(statement2);
            String channelName = "";
            if (resultSet1.next())
            {
                channelName = resultSet1.getNString("name");
            }
            else if (resultSet2.next())
            {
                channelName = resultSet2.getNString("name");
            }
            //search channel in ArrayList channels
            Channel channel = null;
            for (Channel c: channels)
            {
                if (c.getName().equals(channelName))
                {
                    channel = c;
                }
            }

            //send keyFile to other participant from channel
            channel.post(keyFile);
            //send message over EventBus(Channel)
            channel.post(message);

        }
        catch (SQLException sqlException)
        {
            String exOutput = "[method sendMessage in ParticipantNormal] SQLException: " + sqlException.getMessage();
            System.out.println(exOutput);
        }
    }


}
