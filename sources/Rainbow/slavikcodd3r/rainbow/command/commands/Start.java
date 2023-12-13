package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "start", "run" })
public class Start extends Command
{
    public void runCommand(final String[] args) {
        String start = "";
        start = args[1];
        final Runtime run = Runtime.getRuntime();
        try {
            run.exec(start);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String getHelp() {
        return "start";
    }
}
