package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.mod.command.Command;
import net.minecraft.init.SoundEvents;

import java.util.Objects;

public class DiscCommand extends Command {
    public DiscCommand() {
        super("Disc", new String[]{"<name>"});
    }

    @Override
    public void execute(String[] commands) {
        if (Objects.equals(commands[0], "11"))
        mc.player.playSound(SoundEvents.RECORD_11, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "13"))
            mc.player.playSound(SoundEvents.RECORD_13, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "blocks"))
            mc.player.playSound(SoundEvents.RECORD_BLOCKS, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "cat"))
            mc.player.playSound(SoundEvents.RECORD_CAT, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "chirp"))
            mc.player.playSound(SoundEvents.RECORD_CHIRP, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "far"))
            mc.player.playSound(SoundEvents.RECORD_FAR, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "mall"))
            mc.player.playSound(SoundEvents.RECORD_MALL, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "mellohi"))
            mc.player.playSound(SoundEvents.RECORD_MELLOHI, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "stal"))
            mc.player.playSound(SoundEvents.RECORD_STAL, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "Strad"))
            mc.player.playSound(SoundEvents.RECORD_STRAD, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "wait"))
            mc.player.playSound(SoundEvents.RECORD_WAIT, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "ward"))
            mc.player.playSound(SoundEvents.RECORD_WARD, 2.0f, 1.0f);
        if (Objects.equals(commands[0], "anvil"))
            mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 2.0f, 9.0f);
        if (Objects.equals(commands[0], "help"))
            Command.sendMessage("Do Disc + Plus Any Disc And It Will Play");
        if (Objects.equals(commands[0], "stop"))
            mc.getSoundHandler().stopSounds();


    }
}

