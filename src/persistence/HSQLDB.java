package persistence;

import commands.Commands;
import configuration.Configuration;
import javafx.scene.web.HTMLEditorSkin;
import network.*;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

public enum HSQLDB {
    instance;

    private Connection connection;

    public void setupConnection() {
        System.out.println("--- setupConnection");

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            String databaseURL = Configuration.instance.driverName + Configuration.instance.databaseFile;
            connection = DriverManager.getConnection(databaseURL, Configuration.instance.username, Configuration.instance.password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized void update(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);

            if (result == -1) {
                System.out.println("error executing " + sqlStatement);
            }

            statement.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }

    private int getNextID(String table) {
        int nextID = 0;

        try {
            String sqlStatement = "SELECT max(id) FROM " + table;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next()) {
                nextID = resultSet.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }

        return nextID;
    }

    //region Table Algorithms

    public void dropTableAlgorithms() {
        System.out.println("--- dropTableAlgorithms");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE algorithms");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableAlgorithms() {
        System.out.println("--- createTableAlgorithms");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE algorithms ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxAlgorithms ON algorithms (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    public void insertDataTableAlgorithms(String name) {
        int nextID = getNextID("algorithms") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO algorithms (").append("id").append(",").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    //endregion

    //region Table Types

    public void dropTableTypes() {
        System.out.println("--- dropTableTypes");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE types");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableTypes() {
        System.out.println("--- createTableTypes");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE types ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(10) NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxTypes ON types (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());
    }

    public void insertDataTableTypes(String name) {
        int nextID = getNextID("types") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO types (").append("id").append(",").append("name").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'");
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    //endregion

    //region Table Participants

    public void dropTableParticipants() {
        System.out.println("--- dropTableParticipants");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE participants");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableParticipants() {
        System.out.println("--- createTableParticipants");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE participants ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("name VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("type_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("CREATE UNIQUE INDEX idxParticipants ON participants (name)");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE participants ADD CONSTRAINT fkParticipants01 ");
        sqlStringBuilder03.append("FOREIGN KEY (type_id) ");
        sqlStringBuilder03.append("REFERENCES types (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());

        update(sqlStringBuilder03.toString());
    }

    public void insertDataTableParticipants(String name, int typeID) {
        int nextID = getNextID("participants") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("id").append(",").append("name").append(",").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append("'").append(name).append("'").append(",").append(typeID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    //endregion

    //region Table Channel

    public void dropTableChannel() {
        System.out.println("--- dropTableChannel");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE channel");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableChannel() {
        System.out.println("--- createTableChannel");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE channel ( ");
        sqlStringBuilder01.append("name VARCHAR(25) NOT NULL").append(",");
        sqlStringBuilder01.append("participant_01 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_02 TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (name)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE channel ADD CONSTRAINT fkChannel01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_01) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE channel ADD CONSTRAINT fkChannel02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_02) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());

        update(sqlStringBuilder03.toString());
    }

    public void insertDataTableChannel(String name, int participant1ID, int participant2ID) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO channel (").append("name").append(",").append("participant_01").append(",").append("participant_02").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(name).append("'").append(",").append(participant1ID).append(",").append(participant2ID);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    //endregion

    //region Table Messages

    public void dropTableMessages() {
        System.out.println("--- dropTableMessages");

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE messages");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTableMessages() {
        System.out.println("--- createTableMessages");

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE messages ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_to_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("plain_message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("algorithm_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("encrypted_message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("keyfile VARCHAR(20) NOT NULL").append(",");
        sqlStringBuilder01.append("timestamp INTEGER").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE messages ADD CONSTRAINT fkMessages01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());
        update(sqlStringBuilder02.toString());

        StringBuilder sqlStringBuilder03 = new StringBuilder();
        sqlStringBuilder03.append("ALTER TABLE messages ADD CONSTRAINT fkMessages02 ");
        sqlStringBuilder03.append("FOREIGN KEY (participant_to_id) ");
        sqlStringBuilder03.append("REFERENCES participants (id) ");
        sqlStringBuilder03.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder03.toString());
        update(sqlStringBuilder03.toString());

        StringBuilder sqlStringBuilder04 = new StringBuilder();
        sqlStringBuilder04.append("ALTER TABLE messages ADD CONSTRAINT fkMessages03 ");
        sqlStringBuilder04.append("FOREIGN KEY (algorithm_id) ");
        sqlStringBuilder04.append("REFERENCES algorithms (id) ");
        sqlStringBuilder04.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder04.toString());
        update(sqlStringBuilder04.toString());
    }

    public void insertDataTableMessages(int partFrom, int partTo, String plainMessage, int algorithmID, String encryptedMessage, String keyFile, int unixTimeStamp) {
        int nextID = getNextID("messages") + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO messages (").append("id").append(",").append("participant_from_id").append(",").append("participant_to_id")
                .append(",").append("plain_message").append(",").append("algorithm_id").append(",").append("encrypted_message")
                .append(",").append("keyfile").append(",").append("timestamp").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",")
                .append(partFrom).append(",")
                .append(partTo).append(",")
                .append("'").append(plainMessage).append("'").append(",")
                .append(algorithmID).append(",")
                .append("'").append(encryptedMessage).append("'").append(",")
                .append("'").append(keyFile).append("'").append(",")
                .append(unixTimeStamp);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    //endregion

    //region Table PostboxForParticipant

    public void dropTablePostbox(String partName) {
        System.out.println("--- dropTablePostbox_" + partName);

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("DROP TABLE postbox_").append(partName);
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());

        update(sqlStringBuilder.toString());
    }

    public void createTablePostbox(String partName) {
        System.out.println("--- createTablePostbox_" + partName);

        StringBuilder sqlStringBuilder01 = new StringBuilder();
        sqlStringBuilder01.append("CREATE TABLE postbox_").append(partName).append(" ( ");
        sqlStringBuilder01.append("id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("participant_from_id TINYINT NOT NULL").append(",");
        sqlStringBuilder01.append("message VARCHAR(50) NOT NULL").append(",");
        sqlStringBuilder01.append("timestamp INTEGER").append(",");
        sqlStringBuilder01.append("PRIMARY KEY (id)");
        sqlStringBuilder01.append(" )");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder01.toString());
        update(sqlStringBuilder01.toString());

        StringBuilder sqlStringBuilder02 = new StringBuilder();
        sqlStringBuilder02.append("ALTER TABLE postbox_").append(partName).append(" ADD CONSTRAINT fkPostbox").append(partName).append("01 ");
        sqlStringBuilder02.append("FOREIGN KEY (participant_from_id) ");
        sqlStringBuilder02.append("REFERENCES participants (id) ");
        sqlStringBuilder02.append("ON DELETE CASCADE");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder02.toString());

        update(sqlStringBuilder02.toString());
    }

    public void insertDataTablePostbox(int partFrom, String partName, String message, int unixTimeStamp) {
        int nextID = getNextID("postbox_" + partName) + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO postbox_").append(partName).append(" (").append("id").append(",").append("participant_from_id")
                .append(",").append("message").append(",").append("timestamp").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append(partFrom).append(",").append("'").append(message).append("'").append(",").append(unixTimeStamp);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
    }

    public int insertDataTablePostboxInt(int partFrom, String partName, String message, int unixTimeStamp) {
        int nextID = getNextID("postbox_" + partName) + 1;
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO postbox_").append(partName).append(" (").append("id").append(",").append("participant_from_id")
                .append(",").append("message").append(",").append("timestamp").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append(nextID).append(",").append(partFrom).append(",").append("'").append(message).append("'").append(",").append(unixTimeStamp);
        sqlStringBuilder.append(")");
        System.out.println("sqlStringBuilder : " + sqlStringBuilder.toString());
        update(sqlStringBuilder.toString());
        return nextID;
    }

    //endregion

    public ResultSet getDataFromManualSQL(String sqlStatement){
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            statement.close();
            return result;

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
            return null;
        }
    }

    public String getParticipantNameByID(int id) throws SQLException {
        ResultSet resultSet = HSQLDB.instance.getDataFromManualSQL("SELECT name FROM participants WHERE id = " + id);
        return resultSet.getNString("id");
    }

    public void updateCommand(String sqlStatement) {
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(sqlStatement);

            if (result == -1) {
                System.out.println("error executing " + sqlStatement);
            }

            statement.close();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public void resetDatabase() {
        try {
            ResultSet resultSet = getDataFromManualSQL("SELECT name FROM participants");

            while (resultSet.next()) {
                dropTablePostbox(resultSet.getNString("name"));
            }
            dropTableMessages();
            dropTableChannel();
            dropTableParticipants();
            dropTableAlgorithms();
            dropTableTypes();

            DataStore.instance.getChannels().clear();
            DataStore.instance.getParticipants().clear();


            createTableAlgorithms();
            createTableTypes();
            createTableParticipants();
            createTableChannel();
            createTableMessages();


            HSQLDB.instance.insertDataTableAlgorithms("shift");
            HSQLDB.instance.insertDataTableAlgorithms("rsa");

            HSQLDB.instance.insertDataTableTypes("normal");
            HSQLDB.instance.insertDataTableTypes("intruder");

            Commands.registerParticipant("branch_hkg", ParticipantType.normal);
            Commands.registerParticipant("branch_cpt", ParticipantType.normal);
            Commands.registerParticipant("branch_sfo", ParticipantType.normal);
            Commands.registerParticipant("branch_syd", ParticipantType.normal);
            Commands.registerParticipant("branch_wuh", ParticipantType.normal);
            Commands.registerParticipant("msa", ParticipantType.intruder);


            Commands.createChannel("hkg_wuh", "branch_hkg", "branch_wuh");
            Commands.createChannel("hkg_cpt", "branch_hkg", "branch_cpt");
            Commands.createChannel("cpt_syd", "branch_cpt", "branch_syd");
            Commands.createChannel("syd_sfo", "branch_syd", "branch_sfo");


        }
        catch (SQLException sqlException)
        {
            System.out.println("[Exception in resetDatabase] " + sqlException.getMessage());
        }


    }

    public void loadDatabase() {
        try {
            //get all participants and channels in Database
            ResultSet resultSetPart = getDataFromManualSQL("SELECT * FROM participants");
            ResultSet resultSetChannels = getDataFromManualSQL("SELECT * FROM channel");

            //create objects of Participant
            while (resultSetPart.next())
            {
                if (resultSetPart.getInt("type_id") == 1)
                {
                    DataStore.instance.addParticipant(new ParticipantNormal(resultSetPart.getInt("id"), resultSetPart.getNString("name")));
                }
                else
                {
                    DataStore.instance.addParticipant(new ParticipantIntruder(resultSetPart.getInt("id"), resultSetPart.getNString("name")));
                }
            }

            //create objects of Channel
            while(resultSetChannels.next())
            {
                //create Object Channel and register Participants
                Channel channel = new Channel(resultSetChannels.getNString("name"));
                ParticipantNormal part1 = (ParticipantNormal)DataStore.instance.getParticipantByID(resultSetChannels.getInt("participant_01"));
                ParticipantNormal part2 = (ParticipantNormal)DataStore.instance.getParticipantByID(resultSetChannels.getInt("participant_02"));

                channel.register(part1);
                channel.register(part2);
                DataStore.instance.addChannel(channel);

                //add channel to ArrayList from Participants
                part1.addChannel(channel);
                part2.addChannel(channel);
            }
        }
        catch (SQLException sqlException)
        {
            System.out.println("[Exception in resetDatabase] " + sqlException.getMessage());
        }
    }


    public void shutdown() {
        System.out.println("--- shutdown");

        try {
            Statement statement = connection.createStatement();
            statement.execute("SHUTDOWN");
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        }
    }
}