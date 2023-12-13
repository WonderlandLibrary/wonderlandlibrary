/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PacketUtil;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

import java.util.ArrayList;

@ModuleInfo(name = "Plugins", description = "Displays what plugins a server has in chat", category = Category.OTHER)
public final class Plugins extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "Bypass");

    private final ArrayList<String> cachedPlugins = new ArrayList<>();
    private final TimeUtil timer = new TimeUtil();

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        switch (mode.getMode()) {
            case "Normal":
                if (timer.hasReached(5000L)) {
                    this.registerNotification("Failed to detect any plugins.");
                    this.toggleModule();
                }
                break;

            case "Bypass":
                if (timer.hasReached(2500L))
                    this.toggleModule();
                break;
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S3APacketTabComplete) {
            switch (mode.getMode()) {
                case "Normal":
                    for (final String command : ((S3APacketTabComplete) event.getPacket()).func_149630_c()) {
                        final String[] pluginCommand = command.split(":");
                        if (pluginCommand.length > 1 && !pluginCommand[0].startsWith("/minecraft")) {
                            final String plugin = pluginCommand[0].replaceAll("/", "");

                            if (!cachedPlugins.contains("Plugins:")) {
                                Rise.addChatMessage("Plugins:");
                                cachedPlugins.add("Plugins:");
                            }

                            if (!cachedPlugins.contains(plugin)) {
                                cachedPlugins.add(plugin);
                                Rise.addChatMessage(plugin);
                            }
                        }
                    }

                    if (cachedPlugins.isEmpty())
                        this.registerNotification("No plugins found.");

                    this.toggleModule();
                    break;

                case "Bypass":
                    for (final String command : ((S3APacketTabComplete) event.getPacket()).func_149630_c()) {
                        final String[] pluginCommand = command.split(":");
                        if (pluginCommand.length > 1 && !pluginCommand[0].startsWith("/minecraft")) {
                            final String plugin = pluginCommand[0].replaceAll("/", "");

                            if (!cachedPlugins.contains(plugin))
                                cachedPlugins.add(plugin);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onEnable() {
        switch (mode.getMode()) {
            case "Normal":
                PacketUtil.sendPacket(new C14PacketTabComplete("/"));
                break;

            case "Bypass":
                final String alphabet = "abcdefghijklmnopqrstuvwxyz";

                for (int i = 0; i < alphabet.length(); i++)
                    PacketUtil.sendPacket(new C14PacketTabComplete("/" + alphabet.charAt(i)));
                break;
        }
        cachedPlugins.clear();
        timer.reset();
    }

    @Override
    protected void onDisable() {
        if (mode.is("Bypass") && !cachedPlugins.isEmpty()) {
            Rise.addChatMessage("Plugins:");

            for (final String name : cachedPlugins)
                Rise.addChatMessage(name);
        }
    }
}
