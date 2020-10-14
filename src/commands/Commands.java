package commands;

import network.*;
import configuration.Configuration;
import persistence.DataStore;
import persistence.HSQLDB;
import persistence.Log;
import persistence.LogOperationType;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.google.common.eventbus.EventBus;

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

        printInfo("EncryptMessage-Method entered with the following parameters:", log);
        printInfo("Message: "+message, log);
        printInfo("Algorithm: "+algorithm, log);
        printInfo("Filename of Keyfile: "+filename+"\n", log);


        File keyFile = getKeyFileFromFileName(filename);
        printInfo("Created File-Object for Keyfile\n",log);

        String nameOfClass = algorithm.substring(0,1).toUpperCase();
        if(algorithm.length()>1){
            nameOfClass += algorithm.substring(1);
        }

        if(algorithm.equals("rsa")){
            nameOfClass = "RSA";
        }

        printInfo("Generated nameOfClass: "+nameOfClass+"\n",log);

        // THIS IF CAN BE DELETED IF CLASS WAS RENAMED TO "Shift"  <------------------------------------------
        if(algorithm.equals("shift")){
            nameOfClass = "CaesarCipher";
        }


        AlgorithmLoader algorithmLoader = new AlgorithmLoader(algorithm, nameOfClass);
        printInfo("Imported algorithm!",log);
        String encryptedMessage = algorithmLoader.executeEncryptMethod(message, keyFile);
        printInfo("Message encryption with "+algorithm+" completed!\n",log);

        printInfo("Encrypted message: "+encryptedMessage+"\n",log);


        return encryptedMessage;
    }

    public static String decryptMessage(String message, String algorithm, String filename){
        Log log = null;
        if (Configuration.instance.getDebugModeActive()){
            log = new Log(LogOperationType.decrypt, algorithm);
        }
        printInfo("DecryptMessage-Method entered with the following parameters:", log);
        printInfo("Encrypted Message: "+message, log);
        printInfo("Algorithm: "+algorithm, log);
        printInfo("Filename of Keyfile: "+filename+"\n", log);


        File keyFile = getKeyFileFromFileName(filename);
        printInfo("Created File-Object for Keyfile\n",log);

        String nameOfClass = algorithm.substring(0,1).toUpperCase();
        if(algorithm.length()>1){
            nameOfClass += algorithm.substring(1);
        }

        if(algorithm.equals("rsa")){
            nameOfClass = "RSA";
        }

        printInfo("Generated nameOfClass: "+nameOfClass+"\n",log);

        // THIS IF CAN BE DELETED IF CLASS WAS RENAMED TO "Shift"  <------------------------------------------
        if(algorithm.equals("shift")){
            nameOfClass = "CaesarCipher";
        }


        AlgorithmLoader algorithmLoader = new AlgorithmLoader(algorithm, nameOfClass);
        printInfo("Imported algorithm!",log);
        String decryptedMessage = algorithmLoader.executeDecryptMethod(message, keyFile);
        printInfo("Message decryption with "+algorithm+" completed!\n",log);

        printInfo("Decrypted message: "+decryptedMessage+"\n",log);


        return decryptedMessage;
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
            ResultSet nameExists = HSQLDB.instance.getDataFromManualSQL("SELECT id FROM participants WHERE name = '" + name + "'");
            if (nameExists.next())
            {
                String output = "participant " + name + " already exists, using existing postbox_" + name;
                System.out.println("--- " + output);
                return output;
            }

            //get id from type from table types
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT id FROM types WHERE name = '" + type.toString() + "'");
            int typeID = 0;
            if (resultSet.next()) {
                typeID = resultSet.getInt("id");
            }
            //insert participant and create postbox
            HSQLDB.instance.insertDataTableParticipants(name, typeID);
            HSQLDB.instance.createTablePostbox(name);
            //get id from database
            ResultSet resultSet1 = HSQLDB.instance.getDataFromManualSQL("SELECT id FROM participants WHERE name = '" + name + "'");
            int partID = 0;
            if(resultSet1.next())
            {
                partID = resultSet1.getInt("id");
            }
            //Object of Participant is instantiated
            if(typeID == 1)
            {
                DataStore.instance.addParticipant(new ParticipantNormal(partID, name));
            }
            else if (typeID == 2)
            {
                DataStore.instance.addParticipant(new ParticipantIntruder(partID, name));
            }

            String outputSuccess = "participant " + name + " with type " + type.toString() + " registered and postbox_" + name + " created";
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

    public static String createChannel(String name, String part1Name, String part2Name){
        try {

            //check if participants are of type normal
            Participant participant1 = DataStore.instance.getParticipantByName(part1Name);
            Participant participant2 = DataStore.instance.getParticipantByName(part2Name);
            ParticipantNormal part1;
            ParticipantNormal part2;
            if (participant1 instanceof ParticipantNormal)
            {
                part1 = (ParticipantNormal)participant1;
                if (participant2 instanceof ParticipantNormal)
                {
                    part2 = (ParticipantNormal)participant2;
                }
                else
                {
                    //return message for output area when participant2 not of type normal
                    String outputFalseType = "participant " + part2Name + " not of type normal";
                    System.out.println("--- " + outputFalseType);
                    return outputFalseType;
                }
            }
            else
            {
                //return message for output area when participant1 not of type normal
                String outputFalseType = "participant " + part1Name + " not of type normal";
                System.out.println("--- " + outputFalseType);
                return outputFalseType;
            }


            //check if channel exists or not
            ResultSet resultSet1 = HSQLDB.instance.getDataFromManualSQL("SELECT * FROM channel WHERE name = '" + name + "'");
            if (resultSet1.next())
            {
                //return message for output area
                String outputChannelExists = "channel " + name + " already exists";
                System.out.println("--- " + outputChannelExists);
                return outputChannelExists;
            }

            //check if channel between participant01 and participant02 exists or not
            ResultSet resultSet2 = HSQLDB.instance.getDataFromManualSQL("SELECT * FROM channel");
            while (resultSet2.next())
            {
                boolean firstCheck = resultSet2.getInt("participant_01") == part1.getId() && resultSet2.getInt("participant_02") == part2.getId();
                boolean switchCheck = resultSet2.getInt("participant_01") == part2.getId() && resultSet2.getInt("participant_02") == part1.getId();
                if (firstCheck || switchCheck)
                {
                    //return message for output area
                    String outputCommunicationExists = "communication channel between " + part1.getName() + " and " + part2.getName() + " already exists";
                    System.out.println("--- " + outputCommunicationExists);
                    return outputCommunicationExists;
                }
            }

            //check if participant01 and participant02 are identical
            if (part1.getName().equals(part2.getName()))
            {
                //return message for output area
                String outputParticipantsEqual = part1.getName() + " and " + part2.getName() + " are identical - cannot create channel on itself";
                System.out.println("--- " + outputParticipantsEqual);
                return outputParticipantsEqual;
            }

            //create Object Channel and register Participants
            Channel channel = new Channel(name);
            channel.register(part1);
            channel.register(part2);
            DataStore.instance.addChannel(channel);

            //add channel to ArrayList from Participants
            part1.addChannel(channel);
            part2.addChannel(channel);

            //save channel in database with message on gui
            HSQLDB.instance.insertDataTableChannel(name, part1.getId(), part2.getId());

            //return message for output area when channel is created
            String outputSuccess = "channel" + name + " from " + part1.getName() + " and " + part2.getName() + " successfully created";
            System.out.println("--- " + outputSuccess);
            return outputSuccess;
        }
        catch (SQLException sqlException)
        {
            String exOutput = "[method createChannel in Commands] SQLException: " + sqlException.getMessage();
            System.out.println(exOutput);
            return exOutput;
        }
    }

    public static String showChannel(){
        try
        {
            //get all channels from database
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT * FROM channel");
            String output = "";
            while (resultSet.next())
            {
                output += resultSet.getNString("name") + "\t| " + HSQLDB.instance.getParticipantNameByID(resultSet.getInt("participant_01"))
                        + " and " + HSQLDB.instance.getParticipantNameByID(resultSet.getInt("participant_02")) + "\n";

            }
            if (output.equals(""))
            {
                output = "no channels are created by now";
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
                HSQLDB.instance.updateCommand("DELETE FROM channel WHERE name = '" + name + "'");
                output = "channel " + name + " deleted";
            }
            else
            {
                output = "unknown channel " + name;
            }
            //delete channel in ArrayList from DataStore
            for (Channel c: DataStore.instance.getChannels()) {
                if (c.getName().equals(name)) {
                    DataStore.instance.getChannels().remove(c);
                }
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

    public static String intrudeChannel(String name, String participantName){

        try {
            //check if participant is of type intruder
            Participant part = DataStore.instance.getParticipantByName(participantName);
            ParticipantIntruder participant;
            if (part instanceof ParticipantIntruder)
            {
                participant = (ParticipantIntruder)part;
            }
            else
            {
                //return message for output area when participant not of type intruder
                String outputFalseType = "participant " + participantName + " not of type intruder";
                System.out.println("--- " + outputFalseType);
                return outputFalseType;
            }

            //check if channel exists or not
            ResultSet resultSet1 = HSQLDB.instance.getDataFromManualSQL("SELECT * FROM channel WHERE name = '" + name + "'");
            if (!resultSet1.next())
            {
                //return message for output area when channel does not exist
                String outputNotExist = "channel " + name + " does not exist";
                System.out.println("--- " + outputNotExist);
                return outputNotExist;
            }

            //register ParticipantIntruder for Channel
            for (Channel c: DataStore.instance.getChannels()) {
                if (c.getName().equals(name))
                {
                    c.register(participant);
                    //return message for output area when participant registered for channel
                    String outputSuccess = "channel " + name + " intruded by participant " + participant.getName();
                    System.out.println("--- " + outputSuccess);
                    return outputSuccess;
                }
            }

            //return message for output area when this part is reached
            String outputError = "ERROR: object of channel " + name + " not in ArrayList channels in DataStore";
            System.out.println("--- " + outputError);
            return outputError;


        }
        catch (SQLException sqlException)
        {
            String exOutput = "[method intrudeChannel in Commands] SQLException: " + sqlException.getMessage();
            System.out.println(exOutput);
            return exOutput;
        }
    }

    public static String sendMessage(String message, String part1, String part2, String algorithm, String keyFileName){

        //get objects participants from DataStore
        ParticipantNormal participant01 = null;
        ParticipantNormal participant02 = null;
        for (Participant p: DataStore.instance.getParticipants()) {
            if (p.getName().equals(part1) && p instanceof ParticipantNormal)
            {
                participant01 = (ParticipantNormal) p;
            }
            else if (p.getName().equals(part2) && p instanceof ParticipantNormal)
            {
                participant02 = (ParticipantNormal) p;
            }
        }
        //check if channel between participant01 and participant02 exists
        boolean exists = false;
        try {
            String statement1 = "SELECT name FROM channel WHERE participant_01 = " + participant01.getId() + " AND participant_02 = " + participant02.getId();
            String statement2 = "SELECT name FROM channel WHERE participant_01 = " + participant02.getId() + " AND participant_02 = " + participant01.getId();
            if (!(HSQLDB.instance.getDataFromManualSQL(statement1).next() || HSQLDB.instance.getDataFromManualSQL(statement2).next()))
            {
                //no channel exists
                String output = "no valid channel from " + part1 + " to " + part2;
                System.out.println("--- " + output);
                return output;
            }
            //channel exists

            //encrypt message
            String encryptedMessage = encryptMessage(message, algorithm, keyFileName);
            //save message in database
            ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT id FROM algorithms WHERE name = '" + algorithm + "'");
            int algorithmID = 0;
            if (resultSet.next())
            {
                resultSet.getInt("id");
            }
            int unixTimeStampSeconds = (int) (System.currentTimeMillis() / 1000L); //only works until 2038
            HSQLDB.instance.insertDataTableMessages(participant01.getId(), participant02.getId(), message, algorithmID, encryptedMessage, keyFileName, unixTimeStampSeconds);

            participant01.sendMessage(participant02, new Message(encryptedMessage, algorithm, participant01.getId(), unixTimeStampSeconds), getKeyFileFromFileName(keyFileName));

            //return message for output area
            String outputSuccess = "message sent from participant " + part1 + " to participant " + part2;
            System.out.println("--- " + outputSuccess);
            return outputSuccess;
        }
        catch (SQLException sqlException)
        {
            String exOutput = "[method sendMessage in Commands] SQLException: " + sqlException.getMessage();
            System.out.println(exOutput);
            return exOutput;
        }
    }

    private static File getKeyFileFromFileName(String fileName){
        return new File(Configuration.instance.keyDirectory+Configuration.instance.fileSeparator+fileName);
    }
}
