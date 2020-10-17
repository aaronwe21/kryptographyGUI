package network;

import com.google.common.eventbus.Subscribe;
import commands.Commands;
import configuration.Configuration;
import persistence.HSQLDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantIntruder extends Participant {

    public ParticipantIntruder(int id, String name) {
        super(id, name);
    }

    @Subscribe
    public void receiveMessage(Message message) {
        try {

            //write message in database table postbox_[name] with message = `unknown`
            int messageID = HSQLDB.instance.insertDataTablePostboxInt(message.getParticipantFromID(), this.name, "unknown", message.getUnixTimeStamp());
            //try to crack message
            String crackedMessage = "";
            crackedMessage = Commands.crackEncryptedMessage(message.getEncryptedMessage(), message.getAlgorithmType(), message.getRsaPublicKeyFile());
            //get name of participant_from
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT name FROM participants WHERE id = " + message.getParticipantFromID());
            resultSet.next();
            String partFromName = resultSet.getNString("name");
            //message cracked or not
            if (crackedMessage.matches("cracking encrypted message \".+\" failed")) {
                String outputFailed = "intruder " + this.name + " | crack message from participant " + partFromName + " failed";
                System.out.println("--- " + outputFailed);
                Configuration.instance.gui.addTextToOutputArea(outputFailed);
            } else //message cracked
            {
                //replace unknown in database with crackedMessage
                HSQLDB.instance.updateCommand("UPDATE postbox_" + this.name + " SET message = '" + crackedMessage + "' WHERE id = " + messageID);
                //send outputMessage to outputArea in GUI
                String outputSuccess = "intruder " + this.name + " cracked message from participant " + partFromName + " | " + crackedMessage;
                System.out.println("--- " + outputSuccess);
                Configuration.instance.gui.addTextToOutputArea(outputSuccess);
            }
        } catch (SQLException sqlException) {
            System.out.println("[SQLException in receiveMessage() from ParticipantIntruder] " + sqlException.getMessage());
        }

    }
}
