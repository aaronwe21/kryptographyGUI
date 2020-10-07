package commands;

import persistence.HSQLDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Commands {

    public static String showAlgorithm(){
        return "X";
    }
/*
    public static String encryptMessage(String message, AlgorithmType algorithm, String filename){

    }

    public static String decryptMessage(String message, AlgorithmType algorithm, String filename){

    }

    public static String crackEncryptedMessage(String message, AlgorithmType algorithm){

    }

    public static String registerParticipant(String name, ParticipantType type) {

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

    public static String createChannel(String name, Participant part1, Participant part2){

    }

    public static String showChannel(){

    }

    public static String dropChannel(String name){

    }

    public static String intrudeChannel(String name, Participant participant){

    }

    public static String sendMessage(String message, Participant part1, Participant part2, AlgorithmType algorithm, String filename){

        int unixTimeStampSeconds = (int)(System.currentTimeMillis()/1000L); //only works until 2038
    }*/
}
