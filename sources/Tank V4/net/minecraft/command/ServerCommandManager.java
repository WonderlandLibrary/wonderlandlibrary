package net.minecraft.command;

import java.util.Iterator;
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

public class ServerCommandManager extends CommandHandler implements IAdminCommand {
   public ServerCommandManager() {
      this.registerCommand(new CommandTime());
      this.registerCommand(new CommandGameMode());
      this.registerCommand(new CommandDifficulty());
      this.registerCommand(new CommandDefaultGameMode());
      this.registerCommand(new CommandKill());
      this.registerCommand(new CommandToggleDownfall());
      this.registerCommand(new CommandWeather());
      this.registerCommand(new CommandXP());
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
      this.registerCommand(new CommandAchievement());
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
      if (MinecraftServer.getServer().isDedicatedServer()) {
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
      } else {
         this.registerCommand(new CommandPublishLocalServer());
      }

      CommandBase.setAdminCommander(this);
   }

   public void notifyOperators(ICommandSender var1, ICommand var2, int var3, String var4, Object... var5) {
      boolean var6 = true;
      MinecraftServer var7 = MinecraftServer.getServer();
      if (!var1.sendCommandFeedback()) {
         var6 = false;
      }

      ChatComponentTranslation var8 = new ChatComponentTranslation("chat.type.admin", new Object[]{var1.getName(), new ChatComponentTranslation(var4, var5)});
      var8.getChatStyle().setColor(EnumChatFormatting.GRAY);
      var8.getChatStyle().setItalic(true);
      if (var6) {
         Iterator var10 = var7.getConfigurationManager().func_181057_v().iterator();

         label84:
         while(true) {
            EntityPlayer var9;
            boolean var11;
            boolean var12;
            do {
               do {
                  do {
                     do {
                        if (!var10.hasNext()) {
                           break label84;
                        }

                        var9 = (EntityPlayer)var10.next();
                     } while(var9 == var1);
                  } while(!var7.getConfigurationManager().canSendCommands(var9.getGameProfile()));
               } while(!var2.canCommandSenderUseCommand(var1));

               var11 = var1 instanceof MinecraftServer && MinecraftServer.getServer().func_183002_r();
               var12 = var1 instanceof RConConsoleSource && MinecraftServer.getServer().func_181034_q();
            } while(!var11 && !var12 && (var1 instanceof RConConsoleSource || var1 instanceof MinecraftServer));

            var9.addChatMessage(var8);
         }
      }

      if (var1 != var7 && var7.worldServers[0].getGameRules().getBoolean("logAdminCommands")) {
         var7.addChatMessage(var8);
      }

      boolean var13 = var7.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
      if (var1 instanceof CommandBlockLogic) {
         var13 = ((CommandBlockLogic)var1).shouldTrackOutput();
      }

      if ((var3 & 1) != 1 && var13 || var1 instanceof MinecraftServer) {
         var1.addChatMessage(new ChatComponentTranslation(var4, var5));
      }

   }
}
