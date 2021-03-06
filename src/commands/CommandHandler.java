package commands;

import network.ParticipantType;

public class CommandHandler {
    public static String execute(String inputCommand) {
        inputCommand = inputCommand.trim();
        if (inputCommand.equals("reset database")) {
            return Commands.resetDatabase();
        }
        if (inputCommand.equals("show algorithm")) {
            return Commands.showAlgorithm();
        }
        if (inputCommand.matches("encrypt message \".+\" using .+ and keyfile .+\\.json")) {
            String message = extractMessageFromCommand(inputCommand);
            String algorithm = extractAlgorithmFromCommand(inputCommand);
            String filename = extractKeyFileNameFromCommand(inputCommand);
            return Commands.encryptMessage(message, algorithm, filename);
        }
        if (inputCommand.matches("decrypt message \".+\" using .+ and keyfile .+\\.json")) {
            String message = extractMessageFromCommand(inputCommand);
            String algorithm = extractAlgorithmFromCommand(inputCommand);
            String filename = extractKeyFileNameFromCommand(inputCommand);
            return Commands.decryptMessage(message, algorithm, filename);
        }
        // Specification extended
        if (inputCommand.matches("crack encrypted message \".+\" using .+([a-zA-Z0-9]| and keyfile .+\\.json)")) {
            String message = extractMessageFromCommand(inputCommand);
            String algorithm = extractAlgorithmFromCommand(inputCommand);
            String filename = null;
            if (algorithm.equals("rsa")) {
                filename = extractKeyFileNameFromCommand(inputCommand);
                if (!filename.matches(".+\\.json")) {
                    return "Invalid command entered!";
                }
            }
            return Commands.crackEncryptedMessage(message, algorithm, filename);
        }
        if (inputCommand.matches("register participant .+ with type (normal|intruder)")) {
            String participantName = extractOneParticipantFromCommand(inputCommand);
            ParticipantType type = extractParticipantTypeFromCommand(inputCommand);
            return Commands.registerParticipant(participantName, type);
        }
        if (inputCommand.matches("create channel .+ from .+ to .+")) {
            String channelName = extractChannelNameFromCommand(inputCommand);
            String[] participantNames = extractTwoParticipantsFromCommand(inputCommand);
            return Commands.createChannel(channelName, participantNames[0], participantNames[1]);
        }
        if (inputCommand.matches("show channel")) {
            return Commands.showChannel();
        }
        if (inputCommand.matches("drop channel .+")) {
            String channelName = extractChannelNameFromCommand(inputCommand);
            return Commands.dropChannel(channelName);
        }
        if (inputCommand.matches("intrude channel .+ by .+")) {
            String channelName = extractChannelNameFromCommand(inputCommand);
            String participantName = extractOneParticipantFromCommand(inputCommand);
            return Commands.intrudeChannel(channelName, participantName);
        }
        if (inputCommand.matches("send message \".+\" from .+ to .+ using .+ and keyfile .+\\.json")) {
            String message = extractMessageFromCommand(inputCommand);
            String[] participantNames = extractTwoParticipantsFromCommand(inputCommand);
            String algorithm = extractAlgorithmFromCommand(inputCommand);
            String filename = extractKeyFileNameFromCommand(inputCommand);
            return Commands.sendMessage(message, participantNames[0], participantNames[1], algorithm, filename);
        }
        return "Invalid command entered!";
    }

    private static String extractMessageFromCommand(String inputCommand) {
        return inputCommand.replaceAll(".+ message \"", "").replaceAll("\" (from|using) .+", "");
    }

    private static String extractAlgorithmFromCommand(String inputCommand) {
        return inputCommand.replaceAll(".+ using ", "").replaceAll("( and .+)*", "");
    }

    private static String extractKeyFileNameFromCommand(String inputCommand) {
        return inputCommand.replaceAll(".+ keyfile ", "").replaceAll(".json.+", ".json");
    }

    private static String extractChannelNameFromCommand(String inputCommand) {
        return inputCommand.replaceAll(".+ channel ", "").replaceAll(" (from|by).*", "");
    }

    private static String extractOneParticipantFromCommand(String inputCommand) {
        return inputCommand.replaceAll(".+ (participant|by) ", "").replaceAll(" with.+", "");
    }

    private static String[] extractTwoParticipantsFromCommand(String inputCommand) {
        String[] participants = new String[2];
        participants[0] = inputCommand.replaceAll(".+ from ", "").replaceAll(" to.+", "");
        participants[1] = inputCommand.replaceAll(".+ to ", "").replaceAll(" using.+", "");
        return participants;
    }

    private static ParticipantType extractParticipantTypeFromCommand(String inputCommand) {
        String type = inputCommand.replaceAll(".+ type ", "");
        if (type.equals("intruder")) {
            return ParticipantType.intruder;
        }
        return ParticipantType.normal;
    }
}
