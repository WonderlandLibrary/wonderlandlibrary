/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler
implements ICommandManager {
    private static final Logger logger = LogManager.getLogger();
    private final Map commandMap = Maps.newHashMap();
    private final Set commandSet = Sets.newHashSet();
    private static final String __OBFID = "CL_00001765";

    @Override
    public int executeCommand(ICommandSender sender, String command) {
        if ((command = command.trim()).startsWith("/")) {
            command = command.substring(1);
        }
        String[] var3 = command.split(" ");
        String var4 = var3[0];
        var3 = CommandHandler.dropFirstString(var3);
        ICommand var5 = (ICommand)this.commandMap.get(var4);
        int var6 = this.getUsernameIndex(var5, var3);
        int var7 = 0;
        if (var5 == null) {
            ChatComponentTranslation var8 = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
            var8.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var8);
        } else if (var5.canCommandSenderUseCommand(sender)) {
            if (var6 > -1) {
                List var12 = PlayerSelector.func_179656_b(sender, var3[var6], Entity.class);
                String var9 = var3[var6];
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var12.size());
                for (Entity var11 : var12) {
                    var3[var6] = var11.getUniqueID().toString();
                    if (!this.func_175786_a(sender, var3, var5, command)) continue;
                    ++var7;
                }
                var3[var6] = var9;
            } else {
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                if (this.func_175786_a(sender, var3, var5, command)) {
                    ++var7;
                }
            }
        } else {
            ChatComponentTranslation var8 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
            var8.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var8);
        }
        sender.func_174794_a(CommandResultStats.Type.SUCCESS_COUNT, var7);
        return var7;
    }

    protected boolean func_175786_a(ICommandSender p_175786_1_, String[] p_175786_2_, ICommand p_175786_3_, String p_175786_4_) {
        try {
            p_175786_3_.processCommand(p_175786_1_, p_175786_2_);
            return true;
        }
        catch (WrongUsageException var7) {
            ChatComponentTranslation var6 = new ChatComponentTranslation("commands.generic.usage", new ChatComponentTranslation(var7.getMessage(), var7.getErrorOjbects()));
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
        }
        catch (CommandException var8) {
            ChatComponentTranslation var6 = new ChatComponentTranslation(var8.getMessage(), var8.getErrorOjbects());
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
        }
        catch (Throwable var9) {
            ChatComponentTranslation var6 = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
            logger.error("Couldn't process command: '" + p_175786_4_ + "'", var9);
        }
        return false;
    }

    public ICommand registerCommand(ICommand p_71560_1_) {
        this.commandMap.put(p_71560_1_.getCommandName(), p_71560_1_);
        this.commandSet.add(p_71560_1_);
        for (String var3 : p_71560_1_.getCommandAliases()) {
            ICommand var4 = (ICommand)this.commandMap.get(var3);
            if (var4 != null && var4.getCommandName().equals(var3)) continue;
            this.commandMap.put(var3, p_71560_1_);
        }
        return p_71560_1_;
    }

    private static String[] dropFirstString(String[] p_71559_0_) {
        String[] var1 = new String[p_71559_0_.length - 1];
        System.arraycopy(p_71559_0_, 1, var1, 0, p_71559_0_.length - 1);
        return var1;
    }

    @Override
    public List getTabCompletionOptions(ICommandSender sender, String input, BlockPos pos) {
        ICommand var6;
        String[] var4 = input.split(" ", -1);
        String var5 = var4[0];
        if (var4.length == 1) {
            ArrayList var9 = Lists.newArrayList();
            for (Map.Entry var8 : this.commandMap.entrySet()) {
                if (!CommandBase.doesStringStartWith(var5, (String)var8.getKey()) || !((ICommand)var8.getValue()).canCommandSenderUseCommand(sender)) continue;
                var9.add(var8.getKey());
            }
            return var9;
        }
        if (var4.length > 1 && (var6 = (ICommand)this.commandMap.get(var5)) != null && var6.canCommandSenderUseCommand(sender)) {
            return var6.addTabCompletionOptions(sender, CommandHandler.dropFirstString(var4), pos);
        }
        return null;
    }

    @Override
    public List getPossibleCommands(ICommandSender sender) {
        ArrayList var2 = Lists.newArrayList();
        for (ICommand var4 : this.commandSet) {
            if (!var4.canCommandSenderUseCommand(sender)) continue;
            var2.add(var4);
        }
        return var2;
    }

    @Override
    public Map getCommands() {
        return this.commandMap;
    }

    private int getUsernameIndex(ICommand p_82370_1_, String[] p_82370_2_) {
        if (p_82370_1_ == null) {
            return -1;
        }
        for (int var3 = 0; var3 < p_82370_2_.length; ++var3) {
            if (!p_82370_1_.isUsernameIndex(p_82370_2_, var3) || !PlayerSelector.matchesMultiplePlayers(p_82370_2_[var3])) continue;
            return var3;
        }
        return -1;
    }
}

