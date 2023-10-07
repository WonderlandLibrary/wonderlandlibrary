package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.module.Module;

@CommandInfo(name = "Panic", description = "Disables all modules", syntax = ".panic", aliases = "panic")
public final class Panic extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        for (final Module m : Rise.INSTANCE.getModuleManager().getModuleList()) {
            if (m.isEnabled()) {
                m.toggleModule();
            }
        }

        mc.timer.timerSpeed = 1;
        mc.thePlayer.speedInAir = 0.02f;

        Rise.INSTANCE.getNotificationManager().registerNotification("Disabled all modules.");
    }
}
