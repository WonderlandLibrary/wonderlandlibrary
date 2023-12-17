// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.server.MinecraftServer;

public class ServerCommandManager extends CommandHandler implements ICommandListener
{
    private final MinecraftServer server;
    
    public ServerCommandManager(final MinecraftServer serverIn) {
        this.server = serverIn;
        this.registerCommand(new CommandTime());
        this.registerCommand(new CommandGameMode());
        this.registerCommand(new CommandDifficulty());
        this.registerCommand(new CommandDefaultGameMode());
        this.registerCommand(new CommandKill());
        this.registerCommand(new CommandToggleDownfall());
        this.registerCommand(new CommandWeather());
        this.registerCommand(new CommandXP());
        this.registerCommand(new CommandTP());
        this.registerCommand(new CommandTeleport());
        this.registerCommand(new CommandGive());
        this.registerCommand(new CommandReplaceItem());
        this.registerCommand(new CommandStats());
        this.registerCommand(new CommandEffect());
        this.registerCommand(new CommandEnchant());
        this.registerCommand(new CommandParticle());
        this.registerCommand(new CommandEmote());
        this.registerCommand(new CommandShowSeed());
        this.registerCommand(new CommandHelp());
        this.registerCommand(new CommandDebug());
        this.registerCommand(new CommandMessage());
        this.registerCommand(new CommandBroadcast());
        this.registerCommand(new CommandSetSpawnpoint());
        this.registerCommand(new CommandSetDefaultSpawnpoint());
        this.registerCommand(new CommandGameRule());
        this.registerCommand(new CommandClearInventory());
        this.registerCommand(new CommandTestFor());
        this.registerCommand(new CommandSpreadPlayers());
        this.registerCommand(new CommandPlaySound());
        this.registerCommand(new CommandScoreboard());
        this.registerCommand(new CommandExecuteAt());
        this.registerCommand(new CommandTrigger());
        this.registerCommand(new AdvancementCommand());
        this.registerCommand(new RecipeCommand());
        this.registerCommand(new CommandSummon());
        this.registerCommand(new CommandSetBlock());
        this.registerCommand(new CommandFill());
        this.registerCommand(new CommandClone());
        this.registerCommand(new CommandCompare());
        this.registerCommand(new CommandBlockData());
        this.registerCommand(new CommandTestForBlock());
        this.registerCommand(new CommandMessageRaw());
        this.registerCommand(new CommandWorldBorder());
        this.registerCommand(new CommandTitle());
        this.registerCommand(new CommandEntityData());
        this.registerCommand(new CommandStopSound());
        this.registerCommand(new CommandLocate());
        this.registerCommand(new CommandReload());
        this.registerCommand(new CommandFunction());
        if (serverIn.isDedicatedServer()) {
            this.registerCommand(new CommandOp());
            this.registerCommand(new CommandDeOp());
            this.registerCommand(new CommandStop());
            this.registerCommand(new CommandSaveAll());
            this.registerCommand(new CommandSaveOff());
            this.registerCommand(new CommandSaveOn());
            this.registerCommand(new CommandBanIp());
            this.registerCommand(new CommandPardonIp());
            this.registerCommand(new CommandBanPlayer());
            this.registerCommand(new CommandListBans());
            this.registerCommand(new CommandPardonPlayer());
            this.registerCommand(new CommandServerKick());
            this.registerCommand(new CommandListPlayers());
            this.registerCommand(new CommandWhitelist());
            this.registerCommand(new CommandSetPlayerTimeout());
        }
        else {
            this.registerCommand(new CommandPublishLocalServer());
        }
        CommandBase.setCommandListener(this);
    }
    
    @Override
    public void notifyListener(final ICommandSender sender, final ICommand command, final int flags, final String translationKey, final Object... translationArgs) {
        boolean flag = true;
        final MinecraftServer minecraftserver = this.server;
        if (!sender.sendCommandFeedback()) {
            flag = false;
        }
        final ITextComponent itextcomponent = new TextComponentTranslation("chat.type.admin", new Object[] { sender.getName(), new TextComponentTranslation(translationKey, translationArgs) });
        itextcomponent.getStyle().setColor(TextFormatting.GRAY);
        itextcomponent.getStyle().setItalic(true);
        if (flag) {
            for (final EntityPlayer entityplayer : minecraftserver.getPlayerList().getPlayers()) {
                if (entityplayer != sender && minecraftserver.getPlayerList().canSendCommands(entityplayer.getGameProfile()) && command.checkPermission(this.server, sender)) {
                    final boolean flag2 = sender instanceof MinecraftServer && this.server.shouldBroadcastConsoleToOps();
                    final boolean flag3 = sender instanceof RConConsoleSource && this.server.shouldBroadcastRconToOps();
                    if (!flag2 && !flag3 && (sender instanceof RConConsoleSource || sender instanceof MinecraftServer)) {
                        continue;
                    }
                    entityplayer.sendMessage(itextcomponent);
                }
            }
        }
        if (sender != minecraftserver && minecraftserver.worlds[0].getGameRules().getBoolean("logAdminCommands")) {
            minecraftserver.sendMessage(itextcomponent);
        }
        boolean flag4 = minecraftserver.worlds[0].getGameRules().getBoolean("sendCommandFeedback");
        if (sender instanceof CommandBlockBaseLogic) {
            flag4 = ((CommandBlockBaseLogic)sender).shouldTrackOutput();
        }
        if (((flags & 0x1) != 0x1 && flag4) || sender instanceof MinecraftServer) {
            sender.sendMessage(new TextComponentTranslation(translationKey, translationArgs));
        }
    }
    
    @Override
    protected MinecraftServer getServer() {
        return this.server;
    }
}
