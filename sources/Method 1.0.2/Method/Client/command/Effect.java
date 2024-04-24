package Method.Client.command;


import Method.Client.utils.Utils;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.potion.Potion;

@Command.CommandFeatures(description = "Add potion effect", Syntax = "effect <add/remove/clear> <id> <duration> <amplifier>")

public class Effect extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (args[0].equalsIgnoreCase("add")) {
            int id = Integer.parseInt(args[1]);
            int duration = Integer.parseInt(args[2]);
            int amplifier = Integer.parseInt(args[3]);
            if (Potion.getPotionById(id) == null) {
                ChatUtils.error("Potion is null");
                return;
            }
            Utils.addEffect(id, duration, amplifier);
        } else if (args[0].equalsIgnoreCase("remove")) {
            int id = Integer.parseInt(args[1]);
            if (Potion.getPotionById(id) == null) {
                ChatUtils.error("Potion is null");
                return;
            }
            Utils.removeEffect(id);
        } else if (args[0].equalsIgnoreCase("clear")) {
            Utils.clearEffects();
        }
    }



}