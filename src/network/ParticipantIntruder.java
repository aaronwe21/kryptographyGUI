package network;

import com.google.common.eventbus.Subscribe;
import persistence.HSQLDB;

import java.io.File;

public class ParticipantIntruder extends Participant{

    public ParticipantIntruder(int id, String name)
    {
        super(id, name);
    }

    @Subscribe
    public void receiveMessage(Message message) {
        //write message in database table postbox_[name] with message = `unknown`
        int id = HSQLDB.instance.insertDataTablePostboxInt(message.getParticipantFromID(), this.name, "unknown", message.getUnixTimeStamp());
        //crackMessage
        String crackedMessage = "";
        //Rückgabestring der Variablen zuweisen -> wenn nicht erfolgreich, dann leerer String?
        //cracked in 30s?
            //message in database is replaced with cracked message
            if (!crackedMessage.equals(""))
            {
                HSQLDB.instance.updateCommand("UPDATE postbox_" + this.name + " SET message = '" + crackedMessage + "' WHERE id = " + id);
                //in GUI output area message "intruder [name] cracked message from participant [name] | [message]"
            }
            else
            {
                //not cracked
                //in GUI output area message "intruder [name] | crack message from participant [name] failed"
            }


    }
}
