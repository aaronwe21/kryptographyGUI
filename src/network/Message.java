package network;

public class Message {

    private String encryptedMessage;
    private String algorithmType;
    private int participantFromID;
    private int unixTimeStamp;

    public Message (String encryptedMessage, String algorithmType, int participantFromID, int unixTimeStamp)
    {
        this.encryptedMessage = encryptedMessage;
        this.algorithmType = algorithmType;
        this.participantFromID = participantFromID;
        this.unixTimeStamp = unixTimeStamp;
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
}
