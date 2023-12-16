// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.util.IChatComponent;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandShowSeed extends CommandBase
{
    private static final String __OBFID = "CL_00001053";
    
    @Override
    public boolean canCommandSenderUseCommand(final ICommandSender sender) {
        return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUseCommand(sender);
    }
    
    @Override
    public String getCommandName() {
        return "seed";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.seed.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        final Object var3 = (sender instanceof EntityPlayer) ? ((EntityPlayer)sender).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
        final ChatComponentTranslation cc = new ChatComponentTranslation("commands.seed.success", new Object[] { ((World)var3).getSeed() });
        cc.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.COPY_TEXT, new StringBuilder().append((Object)((World)var3).getSeed()).toString()));
        sender.addChatMessage(cc);
    }
}
