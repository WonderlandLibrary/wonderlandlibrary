// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.block.material.Material;
import java.util.Collections;
import javax.annotation.Nullable;
import java.util.Map;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.scoreboard.Team;
import com.google.common.collect.Sets;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import java.util.Collection;
import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;

public class CommandSpreadPlayers extends CommandBase
{
    @Override
    public String getName() {
        return "spreadplayers";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.spreadplayers.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 6) {
            throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
        }
        int i = 0;
        final BlockPos blockpos = sender.getPosition();
        final double d0 = CommandBase.parseDouble(blockpos.getX(), args[i++], true);
        final double d2 = CommandBase.parseDouble(blockpos.getZ(), args[i++], true);
        final double d3 = CommandBase.parseDouble(args[i++], 0.0);
        final double d4 = CommandBase.parseDouble(args[i++], d3 + 1.0);
        final boolean flag = CommandBase.parseBoolean(args[i++]);
        final List<Entity> list = (List<Entity>)Lists.newArrayList();
        while (i < args.length) {
            final String s = args[i++];
            if (EntitySelector.isSelector(s)) {
                final List<Entity> list2 = EntitySelector.matchEntities(sender, s, (Class<? extends Entity>)Entity.class);
                if (list2.isEmpty()) {
                    throw new EntityNotFoundException("commands.generic.selector.notFound", new Object[] { s });
                }
                list.addAll(list2);
            }
            else {
                final EntityPlayer entityplayer = server.getPlayerList().getPlayerByUsername(s);
                if (entityplayer == null) {
                    throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { s });
                }
                list.add(entityplayer);
            }
        }
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
        if (list.isEmpty()) {
            throw new EntityNotFoundException("commands.spreadplayers.noop");
        }
        sender.sendMessage(new TextComponentTranslation("commands.spreadplayers.spreading." + (flag ? "teams" : "players"), new Object[] { list.size(), d4, d0, d2, d3 }));
        this.spread(sender, list, new Position(d0, d2), d3, d4, list.get(0).world, flag);
    }
    
    private void spread(final ICommandSender sender, final List<Entity> p_110669_2_, final Position pos, final double spreadDistance, final double maxRange, final World worldIn, final boolean respectTeams) throws CommandException {
        final Random random = new Random();
        final double d0 = pos.x - maxRange;
        final double d2 = pos.z - maxRange;
        final double d3 = pos.x + maxRange;
        final double d4 = pos.z + maxRange;
        final Position[] acommandspreadplayers$position = this.createInitialPositions(random, respectTeams ? this.getNumberOfTeams(p_110669_2_) : p_110669_2_.size(), d0, d2, d3, d4);
        final int i = this.spreadPositions(pos, spreadDistance, worldIn, random, d0, d2, d3, d4, acommandspreadplayers$position, respectTeams);
        final double d5 = this.setPlayerPositions(p_110669_2_, worldIn, acommandspreadplayers$position, respectTeams);
        CommandBase.notifyCommandListener(sender, this, "commands.spreadplayers.success." + (respectTeams ? "teams" : "players"), acommandspreadplayers$position.length, pos.x, pos.z);
        if (acommandspreadplayers$position.length > 1) {
            sender.sendMessage(new TextComponentTranslation("commands.spreadplayers.info." + (respectTeams ? "teams" : "players"), new Object[] { String.format("%.2f", d5), i }));
        }
    }
    
    private int getNumberOfTeams(final List<Entity> p_110667_1_) {
        final Set<Team> set = (Set<Team>)Sets.newHashSet();
        for (final Entity entity : p_110667_1_) {
            if (entity instanceof EntityPlayer) {
                set.add(entity.getTeam());
            }
            else {
                set.add(null);
            }
        }
        return set.size();
    }
    
    private int spreadPositions(final Position p_110668_1_, final double p_110668_2_, final World worldIn, final Random random, final double minX, final double minZ, final double maxX, final double maxZ, final Position[] p_110668_14_, final boolean respectTeams) throws CommandException {
        boolean flag = true;
        double d0 = 3.4028234663852886E38;
        int i;
        for (i = 0; i < 10000 && flag; ++i) {
            flag = false;
            d0 = 3.4028234663852886E38;
            for (int j = 0; j < p_110668_14_.length; ++j) {
                final Position commandspreadplayers$position = p_110668_14_[j];
                int k = 0;
                final Position commandspreadplayers$position2 = new Position();
                for (int l = 0; l < p_110668_14_.length; ++l) {
                    if (j != l) {
                        final Position commandspreadplayers$position3 = p_110668_14_[l];
                        final double d2 = commandspreadplayers$position.dist(commandspreadplayers$position3);
                        d0 = Math.min(d2, d0);
                        if (d2 < p_110668_2_) {
                            ++k;
                            final Position position = commandspreadplayers$position2;
                            position.x += commandspreadplayers$position3.x - commandspreadplayers$position.x;
                            final Position position2 = commandspreadplayers$position2;
                            position2.z += commandspreadplayers$position3.z - commandspreadplayers$position.z;
                        }
                    }
                }
                if (k > 0) {
                    final Position position3 = commandspreadplayers$position2;
                    position3.x /= k;
                    final Position position4 = commandspreadplayers$position2;
                    position4.z /= k;
                    final double d3 = commandspreadplayers$position2.getLength();
                    if (d3 > 0.0) {
                        commandspreadplayers$position2.normalize();
                        commandspreadplayers$position.moveAway(commandspreadplayers$position2);
                    }
                    else {
                        commandspreadplayers$position.randomize(random, minX, minZ, maxX, maxZ);
                    }
                    flag = true;
                }
                if (commandspreadplayers$position.clamp(minX, minZ, maxX, maxZ)) {
                    flag = true;
                }
            }
            if (!flag) {
                for (final Position commandspreadplayers$position4 : p_110668_14_) {
                    if (!commandspreadplayers$position4.isSafe(worldIn)) {
                        commandspreadplayers$position4.randomize(random, minX, minZ, maxX, maxZ);
                        flag = true;
                    }
                }
            }
        }
        if (i >= 10000) {
            throw new CommandException("commands.spreadplayers.failure." + (respectTeams ? "teams" : "players"), new Object[] { p_110668_14_.length, p_110668_1_.x, p_110668_1_.z, String.format("%.2f", d0) });
        }
        return i;
    }
    
    private double setPlayerPositions(final List<Entity> p_110671_1_, final World worldIn, final Position[] p_110671_3_, final boolean p_110671_4_) {
        double d0 = 0.0;
        int i = 0;
        final Map<Team, Position> map = (Map<Team, Position>)Maps.newHashMap();
        for (int j = 0; j < p_110671_1_.size(); ++j) {
            final Entity entity = p_110671_1_.get(j);
            Position commandspreadplayers$position;
            if (p_110671_4_) {
                final Team team = (entity instanceof EntityPlayer) ? entity.getTeam() : null;
                if (!map.containsKey(team)) {
                    map.put(team, p_110671_3_[i++]);
                }
                commandspreadplayers$position = map.get(team);
            }
            else {
                commandspreadplayers$position = p_110671_3_[i++];
            }
            entity.setPositionAndUpdate(MathHelper.floor(commandspreadplayers$position.x) + 0.5f, commandspreadplayers$position.getSpawnY(worldIn), MathHelper.floor(commandspreadplayers$position.z) + 0.5);
            double d2 = Double.MAX_VALUE;
            for (final Position commandspreadplayers$position2 : p_110671_3_) {
                if (commandspreadplayers$position != commandspreadplayers$position2) {
                    final double d3 = commandspreadplayers$position.dist(commandspreadplayers$position2);
                    d2 = Math.min(d3, d2);
                }
            }
            d0 += d2;
        }
        d0 /= p_110671_1_.size();
        return d0;
    }
    
    private Position[] createInitialPositions(final Random p_110670_1_, final int p_110670_2_, final double p_110670_3_, final double p_110670_5_, final double p_110670_7_, final double p_110670_9_) {
        final Position[] acommandspreadplayers$position = new Position[p_110670_2_];
        for (int i = 0; i < acommandspreadplayers$position.length; ++i) {
            final Position commandspreadplayers$position = new Position();
            commandspreadplayers$position.randomize(p_110670_1_, p_110670_3_, p_110670_5_, p_110670_7_, p_110670_9_);
            acommandspreadplayers$position[i] = commandspreadplayers$position;
        }
        return acommandspreadplayers$position;
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return (args.length >= 1 && args.length <= 2) ? CommandBase.getTabCompletionCoordinateXZ(args, 0, targetPos) : Collections.emptyList();
    }
    
    static class Position
    {
        double x;
        double z;
        
        Position() {
        }
        
        Position(final double xIn, final double zIn) {
            this.x = xIn;
            this.z = zIn;
        }
        
        double dist(final Position pos) {
            final double d0 = this.x - pos.x;
            final double d2 = this.z - pos.z;
            return Math.sqrt(d0 * d0 + d2 * d2);
        }
        
        void normalize() {
            final double d0 = this.getLength();
            this.x /= d0;
            this.z /= d0;
        }
        
        float getLength() {
            return MathHelper.sqrt(this.x * this.x + this.z * this.z);
        }
        
        public void moveAway(final Position pos) {
            this.x -= pos.x;
            this.z -= pos.z;
        }
        
        public boolean clamp(final double p_111093_1_, final double p_111093_3_, final double p_111093_5_, final double p_111093_7_) {
            boolean flag = false;
            if (this.x < p_111093_1_) {
                this.x = p_111093_1_;
                flag = true;
            }
            else if (this.x > p_111093_5_) {
                this.x = p_111093_5_;
                flag = true;
            }
            if (this.z < p_111093_3_) {
                this.z = p_111093_3_;
                flag = true;
            }
            else if (this.z > p_111093_7_) {
                this.z = p_111093_7_;
                flag = true;
            }
            return flag;
        }
        
        public int getSpawnY(final World worldIn) {
            BlockPos blockpos = new BlockPos(this.x, 256.0, this.z);
            while (blockpos.getY() > 0) {
                blockpos = blockpos.down();
                if (worldIn.getBlockState(blockpos).getMaterial() != Material.AIR) {
                    return blockpos.getY() + 1;
                }
            }
            return 257;
        }
        
        public boolean isSafe(final World worldIn) {
            BlockPos blockpos = new BlockPos(this.x, 256.0, this.z);
            while (blockpos.getY() > 0) {
                blockpos = blockpos.down();
                final Material material = worldIn.getBlockState(blockpos).getMaterial();
                if (material != Material.AIR) {
                    return !material.isLiquid() && material != Material.FIRE;
                }
            }
            return false;
        }
        
        public void randomize(final Random rand, final double p_111097_2_, final double p_111097_4_, final double p_111097_6_, final double p_111097_8_) {
            this.x = MathHelper.nextDouble(rand, p_111097_2_, p_111097_6_);
            this.z = MathHelper.nextDouble(rand, p_111097_4_, p_111097_8_);
        }
    }
}
