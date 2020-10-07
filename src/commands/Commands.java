package commands;

public class Commands {

    public void showAlgorithm(){

    }

    public void encryptMessage(String message, AlgorithmType algorithm, String filename){

    }

    public void decryptMessage(String message, AlgorithmType algorithm, String filename){

    }

    public void crackEncryptedMessage(String message, AlgorithmType algorithm){

    }

    public void registerParticipant(String name, ParticipantType type){

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
