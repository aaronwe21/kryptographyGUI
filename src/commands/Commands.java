package commands;

import network.Participant;
import network.ParticipantType;
import configuration.Configuration;
import persistence.HSQLDB;
import persistence.Log;
import persistence.LogOperationType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.Channel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Commands {

    public static String showAlgorithm(){
        File componentDirectory = new File(Configuration.instance.componentDirectory);
        String returnString = "";
        if (componentDirectory.isDirectory()) {
            File[] componentFiles = componentDirectory.listFiles();

            if (componentFiles.length > 0) {
                for (int i=0; i<componentFiles.length; i++){
                    if ((!componentFiles[i].getName().contains("_cracker"))&&(componentFiles[i].getName().matches(".*\\.jar"))){
                        String algorithmName = componentFiles[i].getName().replace(".jar","");
                        returnString += algorithmName;
                        returnString += " | ";

                        // HIER GGF ALGORITHMEN IN DATENBANK EINFÜGEN JE NACHDEM WIE DAS IMPLEMENTIERT IST!
                    }
                }
                return returnString.substring(0,returnString.length()-3);
            }
            return "No components found in this directory!";
        }
        return "No directory 'component' found!";
    }

    public static String encryptMessage(String message, String algorithm, String filename){
        Log log = null;
        if (Configuration.instance.getDebugModeActive()){
            log = new Log(LogOperationType.encrypt, algorithm);
        }

        printInfo("Encrypt Methode, hallo Welt wie gehts dir so?", log);
        printInfo("Dies ist die zweite Zeile?", log);
        return "test";
    }

    public static String decryptMessage(String message, String algorithm, String filename){
        Log log = null;
        if (Configuration.instance.getDebugModeActive()){
            log = new Log(LogOperationType.decrypt, algorithm);
        }
        return "test";
    }

    private static void printInfo(String text, Log log){
        System.out.println(text);
        if(log != null){
            log.addLineToLog(text);
        }
    }

    public static String crackEncryptedMessage(String message, String algorithm){
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

    public static String sendMessage(String message, Participant part1, Participant part2, String algorithm, String filename){

        int unixTimeStampSeconds = (int)(System.currentTimeMillis()/1000L); //only works until 2038
        return "test";
    }
}
