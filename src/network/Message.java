package network;

public class Message {

    private String encryptedMessage;
    private String algorithmType;
    private int participantFromID;

    public Message (String encryptedMessage, String algorithmType, int participantFromID)
    {
        this.encryptedMessage = encryptedMessage;
        this.algorithmType = algorithmType;
        this.participantFromID = participantFromID;
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
}
