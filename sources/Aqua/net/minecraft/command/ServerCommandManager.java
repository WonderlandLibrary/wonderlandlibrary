package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandBlockData;
import net.minecraft.command.CommandClearInventory;
import net.minecraft.command.CommandClone;
import net.minecraft.command.CommandCompare;
import net.minecraft.command.CommandDebug;
import net.minecraft.command.CommandDefaultGameMode;
import net.minecraft.command.CommandDifficulty;
import net.minecraft.command.CommandEffect;
import net.minecraft.command.CommandEnchant;
import net.minecraft.command.CommandEntityData;
import net.minecraft.command.CommandExecuteAt;
import net.minecraft.command.CommandFill;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.CommandGameRule;
import net.minecraft.command.CommandGive;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.CommandHelp;
import net.minecraft.command.CommandKill;
import net.minecraft.command.CommandParticle;
import net.minecraft.command.CommandPlaySound;
import net.minecraft.command.CommandReplaceItem;
import net.minecraft.command.CommandServerKick;
import net.minecraft.command.CommandSetPlayerTimeout;
import net.minecraft.command.CommandSetSpawnpoint;
import net.minecraft.command.CommandShowSeed;
import net.minecraft.command.CommandSpreadPlayers;
import net.minecraft.command.CommandStats;
import net.minecraft.command.CommandTime;
import net.minecraft.command.CommandTitle;
import net.minecraft.command.CommandToggleDownfall;
import net.minecraft.command.CommandTrigger;
import net.minecraft.command.CommandWeather;
import net.minecraft.command.CommandWorldBorder;
import net.minecraft.command.CommandXP;
import net.minecraft.command.IAdminCommand;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandAchievement;
import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ServerCommandManager
extends CommandHandler
implements IAdminCommand {
    public ServerCommandManager() {
        this.registerCommand((ICommand)new CommandTime());
        this.registerCommand((ICommand)new CommandGameMode());
        this.registerCommand((ICommand)new CommandDifficulty());
        this.registerCommand((ICommand)new CommandDefaultGameMode());
        this.registerCommand((ICommand)new CommandKill());
        this.registerCommand((ICommand)new CommandToggleDownfall());
        this.registerCommand((ICommand)new CommandWeather());
        this.registerCommand((ICommand)new CommandXP());
        this.registerCommand((ICommand)new CommandTeleport());
        this.registerCommand((ICommand)new CommandGive());
        this.registerCommand((ICommand)new CommandReplaceItem());
        this.registerCommand((ICommand)new CommandStats());
        this.registerCommand((ICommand)new CommandEffect());
        this.registerCommand((ICommand)new CommandEnchant());
        this.registerCommand((ICommand)new CommandParticle());
        this.registerCommand((ICommand)new CommandEmote());
        this.registerCommand((ICommand)new CommandShowSeed());
        this.registerCommand((ICommand)new CommandHelp());
        this.registerCommand((ICommand)new CommandDebug());
        this.registerCommand((ICommand)new CommandMessage());
        this.registerCommand((ICommand)new CommandBroadcast());
        this.registerCommand((ICommand)new CommandSetSpawnpoint());
        this.registerCommand((ICommand)new CommandSetDefaultSpawnpoint());
        this.registerCommand((ICommand)new CommandGameRule());
        this.registerCommand((ICommand)new CommandClearInventory());
        this.registerCommand((ICommand)new CommandTestFor());
        this.registerCommand((ICommand)new CommandSpreadPlayers());
        this.registerCommand((ICommand)new CommandPlaySound());
        this.registerCommand((ICommand)new CommandScoreboard());
        this.registerCommand((ICommand)new CommandExecuteAt());
        this.registerCommand((ICommand)new CommandTrigger());
        this.registerCommand((ICommand)new CommandAchievement());
        this.registerCommand((ICommand)new CommandSummon());
        this.registerCommand((ICommand)new CommandSetBlock());
        this.registerCommand((ICommand)new CommandFill());
        this.registerCommand((ICommand)new CommandClone());
        this.registerCommand((ICommand)new CommandCompare());
        this.registerCommand((ICommand)new CommandBlockData());
        this.registerCommand((ICommand)new CommandTestForBlock());
        this.registerCommand((ICommand)new CommandMessageRaw());
        this.registerCommand((ICommand)new CommandWorldBorder());
        this.registerCommand((ICommand)new CommandTitle());
        this.registerCommand((ICommand)new CommandEntityData());
        if (MinecraftServer.getServer().isDedicatedServer()) {
            this.registerCommand((ICommand)new CommandOp());
            this.registerCommand((ICommand)new CommandDeOp());
            this.registerCommand((ICommand)new CommandStop());
            this.registerCommand((ICommand)new CommandSaveAll());
            this.registerCommand((ICommand)new CommandSaveOff());
            this.registerCommand((ICommand)new CommandSaveOn());
            this.registerCommand((ICommand)new CommandBanIp());
            this.registerCommand((ICommand)new CommandPardonIp());
            this.registerCommand((ICommand)new CommandBanPlayer());
            this.registerCommand((ICommand)new CommandListBans());
            this.registerCommand((ICommand)new CommandPardonPlayer());
            this.registerCommand((ICommand)new CommandServerKick());
            this.registerCommand((ICommand)new CommandListPlayers());
            this.registerCommand((ICommand)new CommandWhitelist());
            this.registerCommand((ICommand)new CommandSetPlayerTimeout());
        } else {
            this.registerCommand((ICommand)new CommandPublishLocalServer());
        }
        CommandBase.setAdminCommander((IAdminCommand)this);
    }

    public void notifyOperators(ICommandSender sender, ICommand command, int flags, String msgFormat, Object ... msgParams) {
        boolean flag = true;
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (!sender.sendCommandFeedback()) {
            flag = false;
        }
        ChatComponentTranslation ichatcomponent = new ChatComponentTranslation("chat.type.admin", new Object[]{sender.getName(), new ChatComponentTranslation(msgFormat, msgParams)});
        ichatcomponent.getChatStyle().setColor(EnumChatFormatting.GRAY);
        ichatcomponent.getChatStyle().setItalic(Boolean.valueOf((boolean)true));
        if (flag) {
            for (EntityPlayer entityplayer : minecraftserver.getConfigurationManager().getPlayerList()) {
                boolean flag2;
                if (entityplayer == sender || !minecraftserver.getConfigurationManager().canSendCommands(entityplayer.getGameProfile()) || !command.canCommandSenderUseCommand(sender)) continue;
                boolean flag1 = sender instanceof MinecraftServer && MinecraftServer.getServer().shouldBroadcastConsoleToOps();
                boolean bl = flag2 = sender instanceof RConConsoleSource && MinecraftServer.getServer().shouldBroadcastRconToOps();
                if (!flag1 && !flag2 && (sender instanceof RConConsoleSource || sender instanceof MinecraftServer)) continue;
                entityplayer.addChatMessage((IChatComponent)ichatcomponent);
            }
        }
        if (sender != minecraftserver && minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands")) {
            minecraftserver.addChatMessage((IChatComponent)ichatcomponent);
        }
        boolean flag3 = minecraftserver.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
        if (sender instanceof CommandBlockLogic) {
            flag3 = ((CommandBlockLogic)sender).shouldTrackOutput();
        }
        if ((flags & 1) != 1 && flag3 || sender instanceof MinecraftServer) {
            sender.addChatMessage((IChatComponent)new ChatComponentTranslation(msgFormat, msgParams));
        }
    }
}
