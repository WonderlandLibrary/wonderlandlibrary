package koks.command;

import koks.Koks;
import koks.api.registry.command.Command;

/**
 * @author kroko
 * @created on 13.02.2021 : 01:12
 */

@Command.Info(name = "msg", description = "send other players private messages")
public class Msg extends Command {

    @Override
    public boolean execute(String[] args) {
        if(args.length >= 2) {
            final String target = args[0];
            String message = "";
            for(int i = 1; i < args.length; i++) {
                message += args[i] + " ";
            }
            message = message.substring(0, message.length() - 1);
            Koks.getKoks().irc.sendWhisperMessage(target, message);
        }else{
            sendHelp(this, "[Target] [Message]");
        }
        return true;
    }
}
