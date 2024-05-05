package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.CommandSpreadPlayers;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CommandSpreadPlayers
extends CommandBase {
    public String getCommandName() {
        return "spreadplayers";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender) {
        return "commands.spreadplayers.usage";
    }

    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 6) {
            throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
        }
        int i = 0;
        BlockPos blockpos = sender.getPosition();
        double d0 = CommandSpreadPlayers.parseDouble((double)blockpos.getX(), (String)args[i++], (boolean)true);
        double d1 = CommandSpreadPlayers.parseDouble((double)blockpos.getZ(), (String)args[i++], (boolean)true);
        double d2 = CommandSpreadPlayers.parseDouble((String)args[i++], (double)0.0);
        double d3 = CommandSpreadPlayers.parseDouble((String)args[i++], (double)(d2 + 1.0));
        boolean flag = CommandSpreadPlayers.parseBoolean((String)args[i++]);
        ArrayList list = Lists.newArrayList();
        while (i < args.length) {
            String s;
            if (PlayerSelector.hasArguments((String)(s = args[i++]))) {
                List list1 = PlayerSelector.matchEntities((ICommandSender)sender, (String)s, Entity.class);
                if (list1.size() == 0) {
                    throw new EntityNotFoundException();
                }
                list.addAll((Collection)list1);
                continue;
            }
            EntityPlayerMP entityplayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(s);
            if (entityplayer == null) {
                throw new PlayerNotFoundException();
            }
            list.add((Object)entityplayer);
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
        if (list.isEmpty()) {
            throw new EntityNotFoundException();
        }
        sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.spreadplayers.spreading." + (flag ? "teams" : "players"), new Object[]{list.size(), d3, d0, d1, d2}));
        this.func_110669_a(sender, (List<Entity>)list, new Position(d0, d1), d2, d3, ((Entity)list.get((int)0)).worldObj, flag);
    }

    private void func_110669_a(ICommandSender p_110669_1_, List<Entity> p_110669_2_, Position p_110669_3_, double p_110669_4_, double p_110669_6_, World worldIn, boolean p_110669_9_) throws CommandException {
        Random random = new Random();
        double d0 = p_110669_3_.field_111101_a - p_110669_6_;
        double d1 = p_110669_3_.field_111100_b - p_110669_6_;
        double d2 = p_110669_3_.field_111101_a + p_110669_6_;
        double d3 = p_110669_3_.field_111100_b + p_110669_6_;
        Position[] acommandspreadplayers$position = this.func_110670_a(random, p_110669_9_ ? this.func_110667_a(p_110669_2_) : p_110669_2_.size(), d0, d1, d2, d3);
        int i = this.func_110668_a(p_110669_3_, p_110669_4_, worldIn, random, d0, d1, d2, d3, acommandspreadplayers$position, p_110669_9_);
        double d4 = this.func_110671_a(p_110669_2_, worldIn, acommandspreadplayers$position, p_110669_9_);
        CommandSpreadPlayers.notifyOperators((ICommandSender)p_110669_1_, (ICommand)this, (String)("commands.spreadplayers.success." + (p_110669_9_ ? "teams" : "players")), (Object[])new Object[]{acommandspreadplayers$position.length, p_110669_3_.field_111101_a, p_110669_3_.field_111100_b});
        if (acommandspreadplayers$position.length > 1) {
            p_110669_1_.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.spreadplayers.info." + (p_110669_9_ ? "teams" : "players"), new Object[]{String.format((String)"%.2f", (Object[])new Object[]{d4}), i}));
        }
    }

    private int func_110667_a(List<Entity> p_110667_1_) {
        HashSet set = Sets.newHashSet();
        for (Entity entity : p_110667_1_) {
            if (entity instanceof EntityPlayer) {
                set.add((Object)((EntityPlayer)entity).getTeam());
                continue;
            }
            set.add((Object)null);
        }
        return set.size();
    }

    private int func_110668_a(Position p_110668_1_, double p_110668_2_, World worldIn, Random p_110668_5_, double p_110668_6_, double p_110668_8_, double p_110668_10_, double p_110668_12_, Position[] p_110668_14_, boolean p_110668_15_) throws CommandException {
        int i;
        boolean flag = true;
        double d0 = 3.4028234663852886E38;
        for (i = 0; i < 10000 && flag; ++i) {
            flag = false;
            d0 = 3.4028234663852886E38;
            for (int j = 0; j < p_110668_14_.length; ++j) {
                Position commandspreadplayers$position = p_110668_14_[j];
                int k = 0;
                Position commandspreadplayers$position1 = new Position();
                for (int l = 0; l < p_110668_14_.length; ++l) {
                    if (j == l) continue;
                    Position commandspreadplayers$position2 = p_110668_14_[l];
                    double d1 = commandspreadplayers$position.func_111099_a(commandspreadplayers$position2);
                    d0 = Math.min((double)d1, (double)d0);
                    if (!(d1 < p_110668_2_)) continue;
                    ++k;
                    commandspreadplayers$position1.field_111101_a += commandspreadplayers$position2.field_111101_a - commandspreadplayers$position.field_111101_a;
                    commandspreadplayers$position1.field_111100_b += commandspreadplayers$position2.field_111100_b - commandspreadplayers$position.field_111100_b;
                }
                if (k > 0) {
                    commandspreadplayers$position1.field_111101_a /= (double)k;
                    commandspreadplayers$position1.field_111100_b /= (double)k;
                    double d2 = commandspreadplayers$position1.func_111096_b();
                    if (d2 > 0.0) {
                        commandspreadplayers$position1.func_111095_a();
                        commandspreadplayers$position.func_111094_b(commandspreadplayers$position1);
                    } else {
                        commandspreadplayers$position.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
                    }
                    flag = true;
                }
                if (!commandspreadplayers$position.func_111093_a(p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_)) continue;
                flag = true;
            }
            if (flag) continue;
            for (Position commandspreadplayers$position3 : p_110668_14_) {
                if (commandspreadplayers$position3.func_111098_b(worldIn)) continue;
                commandspreadplayers$position3.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
                flag = true;
            }
        }
        if (i >= 10000) {
            throw new CommandException("commands.spreadplayers.failure." + (p_110668_15_ ? "teams" : "players"), new Object[]{p_110668_14_.length, p_110668_1_.field_111101_a, p_110668_1_.field_111100_b, String.format((String)"%.2f", (Object[])new Object[]{d0})});
        }
        return i;
    }

    private double func_110671_a(List<Entity> p_110671_1_, World worldIn, Position[] p_110671_3_, boolean p_110671_4_) {
        double d0 = 0.0;
        int i = 0;
        HashMap map = Maps.newHashMap();
        for (int j = 0; j < p_110671_1_.size(); ++j) {
            Position commandspreadplayers$position;
            Entity entity = (Entity)p_110671_1_.get(j);
            if (p_110671_4_) {
                Team team;
                Team team2 = team = entity instanceof EntityPlayer ? ((EntityPlayer)entity).getTeam() : null;
                if (!map.containsKey((Object)team)) {
                    map.put((Object)team, (Object)p_110671_3_[i++]);
                }
                commandspreadplayers$position = (Position)map.get((Object)team);
            } else {
                commandspreadplayers$position = p_110671_3_[i++];
            }
            entity.setPositionAndUpdate((double)((float)MathHelper.floor_double((double)commandspreadplayers$position.field_111101_a) + 0.5f), (double)commandspreadplayers$position.func_111092_a(worldIn), (double)MathHelper.floor_double((double)commandspreadplayers$position.field_111100_b) + 0.5);
            double d2 = Double.MAX_VALUE;
            for (int k = 0; k < p_110671_3_.length; ++k) {
                if (commandspreadplayers$position == p_110671_3_[k]) continue;
                double d1 = commandspreadplayers$position.func_111099_a(p_110671_3_[k]);
                d2 = Math.min((double)d1, (double)d2);
            }
            d0 += d2;
        }
        return d0 /= (double)p_110671_1_.size();
    }

    private Position[] func_110670_a(Random p_110670_1_, int p_110670_2_, double p_110670_3_, double p_110670_5_, double p_110670_7_, double p_110670_9_) {
        Position[] acommandspreadplayers$position = new Position[p_110670_2_];
        for (int i = 0; i < acommandspreadplayers$position.length; ++i) {
            Position commandspreadplayers$position = new Position();
            commandspreadplayers$position.func_111097_a(p_110670_1_, p_110670_3_, p_110670_5_, p_110670_7_, p_110670_9_);
            acommandspreadplayers$position[i] = commandspreadplayers$position;
        }
        return acommandspreadplayers$position;
    }

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return args.length >= 1 && args.length <= 2 ? CommandSpreadPlayers.func_181043_b((String[])args, (int)0, (BlockPos)pos) : null;
    }
}
