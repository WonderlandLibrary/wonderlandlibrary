// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class CommandHandler implements ICommandManager
{
    private static final Logger logger;
    private final Map commandMap;
    private final Set commandSet;
    private static final String __OBFID = "CL_00001765";
    
    public CommandHandler() {
        this.commandMap = Maps.newHashMap();
        this.commandSet = Sets.newHashSet();
    }
    
    @Override
    public int executeCommand(final ICommandSender sender, String command) {
        command = command.trim();
        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        String[] var3 = command.split(" ");
        final String var4 = var3[0];
        var3 = dropFirstString(var3);
        final ICommand var5 = this.commandMap.get(var4);
        final int var6 = this.getUsernameIndex(var5, var3);
        int var7 = 0;
        if (var5 == null) {
            final ChatComponentTranslation var8 = new ChatComponentTranslation("commands.generic.notFound", new Object[0]);
            var8.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var8);
        }
        else if (var5.canCommandSenderUseCommand(sender)) {
            if (var6 > -1) {
                final List var9 = PlayerSelector.func_179656_b(sender, var3[var6], Entity.class);
                final String var10 = var3[var6];
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var9.size());
                for (final Entity var12 : var9) {
                    var3[var6] = var12.getUniqueID().toString();
                    if (this.func_175786_a(sender, var3, var5, command)) {
                        ++var7;
                    }
                }
                var3[var6] = var10;
            }
            else {
                sender.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                if (this.func_175786_a(sender, var3, var5, command)) {
                    ++var7;
                }
            }
        }
        else {
            final ChatComponentTranslation var8 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
            var8.getChatStyle().setColor(EnumChatFormatting.RED);
            sender.addChatMessage(var8);
        }
        sender.func_174794_a(CommandResultStats.Type.SUCCESS_COUNT, var7);
        return var7;
    }
    
    protected boolean func_175786_a(final ICommandSender p_175786_1_, final String[] p_175786_2_, final ICommand p_175786_3_, final String p_175786_4_) {
        try {
            p_175786_3_.processCommand(p_175786_1_, p_175786_2_);
            return true;
        }
        catch (WrongUsageException var7) {
            final ChatComponentTranslation var6 = new ChatComponentTranslation("commands.generic.usage", new Object[] { new ChatComponentTranslation(var7.getMessage(), var7.getErrorOjbects()) });
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
        }
        catch (CommandException var8) {
            final ChatComponentTranslation var6 = new ChatComponentTranslation(var8.getMessage(), var8.getErrorOjbects());
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
        }
        catch (Throwable var9) {
            final ChatComponentTranslation var6 = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            var6.getChatStyle().setColor(EnumChatFormatting.RED);
            p_175786_1_.addChatMessage(var6);
            CommandHandler.logger.error("Couldn't process command: '" + p_175786_4_ + "'", var9);
        }
        return false;
    }
    
    public ICommand registerCommand(final ICommand p_71560_1_) {
        this.commandMap.put(p_71560_1_.getCommandName(), p_71560_1_);
        this.commandSet.add(p_71560_1_);
        for (final String var3 : p_71560_1_.getCommandAliases()) {
            final ICommand var4 = this.commandMap.get(var3);
            if (var4 == null || !var4.getCommandName().equals(var3)) {
                this.commandMap.put(var3, p_71560_1_);
            }
        }
        return p_71560_1_;
    }
    
    private static String[] dropFirstString(final String[] p_71559_0_) {
        final String[] var1 = new String[p_71559_0_.length - 1];
        System.arraycopy(p_71559_0_, 1, var1, 0, p_71559_0_.length - 1);
        return var1;
    }
    
    @Override
    public List getTabCompletionOptions(final ICommandSender sender, final String input, final BlockPos pos) {
        final String[] var4 = input.split(" ", -1);
        final String var5 = var4[0];
        if (var4.length == 1) {
            final ArrayList var6 = Lists.newArrayList();
            for (final Map.Entry var8 : this.commandMap.entrySet()) {
                if (CommandBase.doesStringStartWith(var5, var8.getKey()) && var8.getValue().canCommandSenderUseCommand(sender)) {
                    var6.add(var8.getKey());
                }
            }
            return var6;
        }
        if (var4.length > 1) {
            final ICommand var9 = this.commandMap.get(var5);
            if (var9 != null && var9.canCommandSenderUseCommand(sender)) {
                return var9.addTabCompletionOptions(sender, dropFirstString(var4), pos);
            }
        }
        return null;
    }
    
    @Override
    public List getPossibleCommands(final ICommandSender sender) {
        final ArrayList var2 = Lists.newArrayList();
        for (final ICommand var4 : this.commandSet) {
            if (var4.canCommandSenderUseCommand(sender)) {
                var2.add(var4);
            }
        }
        return var2;
    }
    
    @Override
    public Map getCommands() {
        return this.commandMap;
    }
    
    private int getUsernameIndex(final ICommand p_82370_1_, final String[] p_82370_2_) {
        if (p_82370_1_ == null) {
            return -1;
        }
        for (int var3 = 0; var3 < p_82370_2_.length; ++var3) {
            if (p_82370_1_.isUsernameIndex(p_82370_2_, var3) && PlayerSelector.matchesMultiplePlayers(p_82370_2_[var3])) {
                return var3;
            }
        }
        return -1;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
