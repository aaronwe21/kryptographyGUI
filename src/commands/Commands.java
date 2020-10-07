package commands;

import network.AlgorithmType;
import network.Participant;
import network.ParticipantType;
import persistence.HSQLDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Commands {

    public static String showAlgorithm(){
        return "test";
    }

    public static String encryptMessage(String message, AlgorithmType algorithm, String filename){
        return "test";
    }

    public static String decryptMessage(String message, AlgorithmType algorithm, String filename){
        return "test";
    }

    public static String crackEncryptedMessage(String message, AlgorithmType algorithm){
        return "test";
    }

    public static String registerParticipant(String name, ParticipantType type) {

        try {
            //check if name already exists in table participants
            ResultSet nameExists = HSQLDB.instance.getDataFromManualSQL("SELECT COUNT(id) FROM participants WHERE name = '" + name + "'");
            if (nameExists.next())
            {
                String output = "participant" + name + "already exists, using existing postbox_" + name;
                System.out.println("--- " + output);
                return output;
            }

            //get id from type from table types
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT id FROM types WHERE type = '" + type.toString() + "'");
            int id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            //insert participant and create postbox
            HSQLDB.instance.insertDataTableParticipants(name, id);
            HSQLDB.instance.createTablePostbox(name);


            String outputSuccess = "participant" + name + "with type " + type.toString() + " registered and postbox_" + name + " created";
            System.out.println("--- " + outputSuccess);
            return outputSuccess;

        }
        catch (SQLException sqlException)
        {
            String exOutput = "[method registerParticipant] SQLException: " + sqlException.getMessage();
            System.out.println(exOutput);
            return exOutput;
        }

    }

    public static String createChannel(String name, Participant part1, Participant part2){
        return "test";
    }

    public static String showChannel(){
        try
        {
            //get all channel from database
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT * FROM channel");
            String output = "";
            while (resultSet.next())
            {
                output += resultSet.getNString("name") + "\t| " + HSQLDB.instance.getParticipantNameByID(resultSet.getInt("participant_01"))
                        + " and " + HSQLDB.instance.getParticipantNameByID(resultSet.getInt("participant_02")) + "\n";

            }
            if (output.equals(""))
            {
                output = "no channel are created by now.";
            }
            System.out.print("--- channel: \n" + output);
            return output;


        }
        catch (SQLException sqlException)
        {
            String exOutput = "[method showChannel] SQLException: " + sqlException.getMessage();
            System.out.println(exOutput);
            return exOutput;
        }
    }

    public static String dropChannel(String name){
        try
        {
            String output = "";

            //check if channel exists or not
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT * FROM channel WHERE name = '" + name + "'");
            if (resultSet.next())
            {
                HSQLDB.instance.deleteRows("DELETE FROM channel WHERE name = '" + name + "'");
                output = "channel " + name + " deleted";
            }
            else
            {
                output = "unknown channel " + name;
            }

            return output;
        }
        catch (SQLException sqlException)
        {
            String exOutput = "[method showChannel] SQLException: " + sqlException.getMessage();
            System.out.println(exOutput);
            return exOutput;
        }
    }

    public static String intrudeChannel(String name, Participant participant){
        return "test";
    }

    public static String sendMessage(String message, Participant part1, Participant part2, AlgorithmType algorithm, String filename){

        int unixTimeStampSeconds = (int)(System.currentTimeMillis()/1000L); //only works until 2038
        return "test";
    }
}
