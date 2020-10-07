package commands;

import persistence.HSQLDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Commands {

    public void showAlgorithm(){

    }

    public void encryptMessage(String message, AlgorithmType algorithm, String filename){

    }

    public void decryptMessage(String message, AlgorithmType algorithm, String filename){

    }

    public void crackEncryptedMessage(String message, AlgorithmType algorithm){

    }

    public void registerParticipant(String name, ParticipantType type) {

        try {
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT id FROM types WHERE type = '" + type.toString() + "'");
            int id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

            HSQLDB.instance.insertDataTableParticipants(name, id);
        }
        catch (SQLException sqlException)
        {
            System.out.println("SQLException: " + sqlException.getMessage());
        }

    }

    public void createChannel(String name, Participant part1, Participant part2){

    }

    public void showChannel(){

    }

    public void dropChannel(String name){

    }

    public void intrudeChannel(String name, Participant participant){

    }

    public void sendMessage(String message, Participant part1, Participant part2, AlgorithmType algorithm, String filename){

        int unixTimeStampSeconds = (int)(System.currentTimeMillis()/1000L); //only works until 2038
    }
}
