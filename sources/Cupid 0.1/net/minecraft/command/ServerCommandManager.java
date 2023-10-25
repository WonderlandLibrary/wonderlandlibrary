package net.minecraft.command;

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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ServerCommandManager extends CommandHandler implements IAdminCommand {
  public ServerCommandManager() {
    registerCommand(new CommandTime());
    registerCommand(new CommandGameMode());
    registerCommand(new CommandDifficulty());
    registerCommand(new CommandDefaultGameMode());
    registerCommand(new CommandKill());
    registerCommand(new CommandToggleDownfall());
    registerCommand(new CommandWeather());
    registerCommand(new CommandXP());
    registerCommand((ICommand)new CommandTeleport());
    registerCommand(new CommandGive());
    registerCommand(new CommandReplaceItem());
    registerCommand(new CommandStats());
    registerCommand(new CommandEffect());
    registerCommand(new CommandEnchant());
    registerCommand(new CommandParticle());
    registerCommand((ICommand)new CommandEmote());
    registerCommand(new CommandShowSeed());
    registerCommand(new CommandHelp());
    registerCommand(new CommandDebug());
    registerCommand((ICommand)new CommandMessage());
    registerCommand((ICommand)new CommandBroadcast());
    registerCommand(new CommandSetSpawnpoint());
    registerCommand((ICommand)new CommandSetDefaultSpawnpoint());
    registerCommand(new CommandGameRule());
    registerCommand(new CommandClearInventory());
    registerCommand((ICommand)new CommandTestFor());
    registerCommand(new CommandSpreadPlayers());
    registerCommand(new CommandPlaySound());
    registerCommand((ICommand)new CommandScoreboard());
    registerCommand(new CommandExecuteAt());
    registerCommand(new CommandTrigger());
    registerCommand((ICommand)new CommandAchievement());
    registerCommand((ICommand)new CommandSummon());
    registerCommand((ICommand)new CommandSetBlock());
    registerCommand(new CommandFill());
    registerCommand(new CommandClone());
    registerCommand(new CommandCompare());
    registerCommand(new CommandBlockData());
    registerCommand((ICommand)new CommandTestForBlock());
    registerCommand((ICommand)new CommandMessageRaw());
    registerCommand(new CommandWorldBorder());
    registerCommand(new CommandTitle());
    registerCommand(new CommandEntityData());
    if (MinecraftServer.getServer().isDedicatedServer()) {
      registerCommand((ICommand)new CommandOp());
      registerCommand((ICommand)new CommandDeOp());
      registerCommand((ICommand)new CommandStop());
      registerCommand((ICommand)new CommandSaveAll());
      registerCommand((ICommand)new CommandSaveOff());
      registerCommand((ICommand)new CommandSaveOn());
      registerCommand((ICommand)new CommandBanIp());
      registerCommand((ICommand)new CommandPardonIp());
      registerCommand((ICommand)new CommandBanPlayer());
      registerCommand((ICommand)new CommandListBans());
      registerCommand((ICommand)new CommandPardonPlayer());
      registerCommand(new CommandServerKick());
      registerCommand((ICommand)new CommandListPlayers());
      registerCommand((ICommand)new CommandWhitelist());
      registerCommand(new CommandSetPlayerTimeout());
    } else {
      registerCommand((ICommand)new CommandPublishLocalServer());
    } 
    CommandBase.setAdminCommander(this);
  }
  
  public void notifyOperators(ICommandSender sender, ICommand command, int flags, String msgFormat, Object... msgParams) {
    boolean flag = true;
    MinecraftServer minecraftserver = MinecraftServer.getServer();
    if (!sender.sendCommandFeedback())
      flag = false; 
    ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("chat.type.admin", new Object[] { sender.getName(), new ChatComponentTranslation(msgFormat, msgParams) });
    chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.GRAY);
    chatComponentTranslation.getChatStyle().setItalic(Boolean.valueOf(true));
    if (flag)
      for (EntityPlayer entityplayer : minecraftserver.getConfigurationManager().func_181057_v()) {
        if (entityplayer != sender && minecraftserver.getConfigurationManager().canSendCommands(entityplayer.getGameProfile()) && command.canCommandSenderUseCommand(sender)) {
          boolean flag1 = (sender instanceof MinecraftServer && MinecraftServer.getServer().func_183002_r());
          boolean flag2 = (sender instanceof net.minecraft.network.rcon.RConConsoleSource && MinecraftServer.getServer().func_181034_q());
          if (flag1 || flag2 || (!(sender instanceof net.minecraft.network.rcon.RConConsoleSource) && !(sender instanceof MinecraftServer)))
            entityplayer.addChatMessage((IChatComponent)chatComponentTranslation); 
        } 
      }  
    if (sender != minecraftserver && minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands"))
      minecraftserver.addChatMessage((IChatComponent)chatComponentTranslation); 
    boolean flag3 = minecraftserver.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    if (sender instanceof CommandBlockLogic)
      flag3 = ((CommandBlockLogic)sender).shouldTrackOutput(); 
    if (((flags & 0x1) != 1 && flag3) || sender instanceof MinecraftServer)
      sender.addChatMessage((IChatComponent)new ChatComponentTranslation(msgFormat, msgParams)); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\ServerCommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */