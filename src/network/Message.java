package network;

public class Message {

    private String encryptedMessage;
    private AlgorithmType algorithmType;
    private int participantFromID;

    public Message (String encryptedMessage, AlgorithmType algorithmType, int participantFromID)
    {
        this.encryptedMessage = encryptedMessage;
        this.algorithmType = algorithmType;
        this.participantFromID = participantFromID;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public int getParticipantFromID() {
        return participantFromID;
    }
}
