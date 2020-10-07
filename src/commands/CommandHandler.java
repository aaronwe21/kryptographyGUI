package commands;

public class CommandHandler {
    public static void execute(String inputCommand)
    {
        inputCommand = inputCommand.trim();
        if(inputCommand.equals("show algorithm")){

            return;
        }
        if(inputCommand.matches("encrypt message \".+\" using .+ and keyfile .+\\.json"))
        {
            System.out.println(inputCommand);
            return;
        }
        if(inputCommand.matches("decrypt message \".+\" using .+ and keyfile .+\\.json")){

            return;
        }
        if(inputCommand.matches("crack encrypted message \".+\" using .+")){

            return;
        }
        if(inputCommand.matches("register participant .+ with type (normal|intruder)")){

            return;
        }
        if(inputCommand.matches()){

            return;
        }
        if(inputCommand.matches()){

            return;
        }
        if(inputCommand.matches()){

            return;
        }
        if(inputCommand.matches()){

            return;
        }
        if(inputCommand.matches()){

            return;
        }
    }
}
