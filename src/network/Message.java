package network;

import java.io.File;

public class Message {

    private String encryptedMessage;
    private String algorithmType;
    private int participantFromID;
    private int unixTimeStamp;
    private String rsaPublicKeyFile;

    public Message (String encryptedMessage, String algorithmType, int participantFromID, int unixTimeStamp, String rsaPublicKeyFile)
    {
        this.encryptedMessage = encryptedMessage;
        this.algorithmType = algorithmType;
        this.participantFromID = participantFromID;
        this.unixTimeStamp = unixTimeStamp;
        this.rsaPublicKeyFile = rsaPublicKeyFile;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public String getAlgorithmType() {
        return algorithmType;
    }

    public int getParticipantFromID() {
        return participantFromID;
    }

    public int getUnixTimeStamp() {
        return unixTimeStamp;
    }

    public String getRsaPublicKeyFile() {
        return rsaPublicKeyFile;
    }
}
