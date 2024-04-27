package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteViewCommand extends Command {
    public RemoteViewCommand() {
        super("remoteview", "rv");
    }

    public void execute(final String[] args) {
        if (args.length < 2) {
            if (mc.getRenderViewEntity() != mc.thePlayer) {
                mc.setRenderViewEntity(mc.thePlayer);
                return;
            }
            chatSyntax("remoteview <username>");
            return;
        }

        String targetName = args[1];

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (targetName.equals(entity.getName())) {
                mc.setRenderViewEntity(entity);
                chat("Now viewing perspective of §8" + entity.getName() + "§3.");
                chat("Execute §8" + LiquidSense.commandManager.getPrefix() + "remoteview §3again to go back to yours.");
                break;
            }
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        if (args.length == 1) {
            return mc.theWorld.playerEntities.stream()
                    .map(EntityPlayer::getName)
                    .filter(module -> module.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
