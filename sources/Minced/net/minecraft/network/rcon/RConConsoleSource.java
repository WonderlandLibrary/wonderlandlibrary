// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.rcon;

import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;

public class RConConsoleSource implements ICommandSender
{
    private final StringBuffer buffer;
    private final MinecraftServer server;
    
    public RConConsoleSource(final MinecraftServer serverIn) {
        this.buffer = new StringBuffer();
        this.server = serverIn;
    }
    
    @Override
    public String getName() {
        return "Rcon";
    }
    
    @Override
    public void sendMessage(final ITextComponent component) {
        this.buffer.append(component.getUnformattedText());
    }
    
    @Override
    public boolean canUseCommand(final int permLevel, final String commandName) {
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
