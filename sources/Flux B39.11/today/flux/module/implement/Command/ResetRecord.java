package today.flux.module.implement.Command;

import today.flux.Flux;
import today.flux.module.Command;
import today.flux.utility.ChatUtils;


@Command.Info(name = "resetrecord", syntax = { "" }, help = "Reset All your Game records.(DiscordRPC)")
public class ResetRecord extends Command {
    @Override
    public void execute(String[] args) throws Error {
        Flux.INSTANCE.getModuleManager().killAuraMod.killed = 0;
        Flux.INSTANCE.getModuleManager().autoGGMod.win = 0;
        ChatUtils.sendMessageToPlayer("Your Game Record has been resat.");
    }
}
