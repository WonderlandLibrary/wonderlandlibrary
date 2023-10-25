/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.rcon;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class RConConsoleSource
implements ICommandSender {
    private final StringBuffer buffer = new StringBuffer();
    private final MinecraftServer server;

    public RConConsoleSource(MinecraftServer serverIn) {
        this.server = serverIn;
    }

    @Override
    public String getName() {
        return "Rcon";
    }

    @Override
    public void addChatMessage(ITextComponent component) {
        this.buffer.append(component.getUnformattedText());
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true;
    }

    @Override
    public World getEntityWorld() {
        return this.server.getEntityWorld();
    }

    @Override
    public boolean sendCommandFeedback() {
        return true;
    }

    @Override
    public MinecraftServer getServer() {
        return this.server;
    }
}

