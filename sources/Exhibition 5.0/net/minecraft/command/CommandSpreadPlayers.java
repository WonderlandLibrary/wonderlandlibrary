// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import net.minecraft.block.material.Material;
import net.minecraft.scoreboard.Team;
import java.util.HashMap;
import net.minecraft.util.MathHelper;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.HashSet;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Sets;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.ArrayList;
import net.minecraft.util.BlockPos;
import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;

public class CommandSpreadPlayers extends CommandBase
{
    private static final String __OBFID = "CL_00001080";
    
    @Override
    public String getCommandName() {
        return "spreadplayers";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.spreadplayers.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 6) {
            throw new WrongUsageException("commands.spreadplayers.usage", new Object[0]);
        }
        final byte var3 = 0;
        final BlockPos var4 = sender.getPosition();
        final double var5 = var4.getX();
        int var6 = var3 + 1;
        final double var7 = CommandBase.func_175761_b(var5, args[var3], true);
        final double var8 = CommandBase.func_175761_b(var4.getZ(), args[var6++], true);
        final double var9 = CommandBase.parseDouble(args[var6++], 0.0);
        final double var10 = CommandBase.parseDouble(args[var6++], var9 + 1.0);
        final boolean var11 = CommandBase.parseBoolean(args[var6++]);
        final ArrayList var12 = Lists.newArrayList();
        while (var6 < args.length) {
            final String var13 = args[var6++];
            if (PlayerSelector.hasArguments(var13)) {
                final List var14 = PlayerSelector.func_179656_b(sender, var13, Entity.class);
                if (var14.size() == 0) {
                    throw new EntityNotFoundException();
                }
                var12.addAll(var14);
            }
            else {
                final EntityPlayerMP var15 = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(var13);
                if (var15 == null) {
                    throw new PlayerNotFoundException();
                }
                var12.add(var15);
            }
        }
        sender.func_174794_a(CommandResultStats.Type.AFFECTED_ENTITIES, var12.size());
        if (var12.isEmpty()) {
            throw new EntityNotFoundException();
        }
        sender.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.spreading." + (var11 ? "teams" : "players"), new Object[] { var12.size(), var10, var7, var8, var9 }));
        this.func_110669_a(sender, var12, new Position(var7, var8), var9, var10, var12.get(0).worldObj, var11);
    }
    
    private void func_110669_a(final ICommandSender p_110669_1_, final List p_110669_2_, final Position p_110669_3_, final double p_110669_4_, final double p_110669_6_, final World worldIn, final boolean p_110669_9_) throws CommandException {
        final Random var10 = new Random();
        final double var11 = p_110669_3_.field_111101_a - p_110669_6_;
        final double var12 = p_110669_3_.field_111100_b - p_110669_6_;
        final double var13 = p_110669_3_.field_111101_a + p_110669_6_;
        final double var14 = p_110669_3_.field_111100_b + p_110669_6_;
        final Position[] var15 = this.func_110670_a(var10, p_110669_9_ ? this.func_110667_a(p_110669_2_) : p_110669_2_.size(), var11, var12, var13, var14);
        final int var16 = this.func_110668_a(p_110669_3_, p_110669_4_, worldIn, var10, var11, var12, var13, var14, var15, p_110669_9_);
        final double var17 = this.func_110671_a(p_110669_2_, worldIn, var15, p_110669_9_);
        CommandBase.notifyOperators(p_110669_1_, this, "commands.spreadplayers.success." + (p_110669_9_ ? "teams" : "players"), var15.length, p_110669_3_.field_111101_a, p_110669_3_.field_111100_b);
        if (var15.length > 1) {
            p_110669_1_.addChatMessage(new ChatComponentTranslation("commands.spreadplayers.info." + (p_110669_9_ ? "teams" : "players"), new Object[] { String.format("%.2f", var17), var16 }));
        }
    }
    
    private int func_110667_a(final List p_110667_1_) {
        final HashSet var2 = Sets.newHashSet();
        for (final Entity var4 : p_110667_1_) {
            if (var4 instanceof EntityPlayer) {
                var2.add(((EntityPlayer)var4).getTeam());
            }
            else {
                var2.add(null);
            }
        }
        return var2.size();
    }
    
    private int func_110668_a(final Position p_110668_1_, final double p_110668_2_, final World worldIn, final Random p_110668_5_, final double p_110668_6_, final double p_110668_8_, final double p_110668_10_, final double p_110668_12_, final Position[] p_110668_14_, final boolean p_110668_15_) throws CommandException {
        boolean var16 = true;
        double var17 = 3.4028234663852886E38;
        int var18;
        for (var18 = 0; var18 < 10000 && var16; ++var18) {
            var16 = false;
            var17 = 3.4028234663852886E38;
            for (int var19 = 0; var19 < p_110668_14_.length; ++var19) {
                final Position var20 = p_110668_14_[var19];
                int var21 = 0;
                final Position var22 = new Position();
                for (int var23 = 0; var23 < p_110668_14_.length; ++var23) {
                    if (var19 != var23) {
                        final Position var24 = p_110668_14_[var23];
                        final double var25 = var20.func_111099_a(var24);
                        var17 = Math.min(var25, var17);
                        if (var25 < p_110668_2_) {
                            ++var21;
                            final Position position = var22;
                            position.field_111101_a += var24.field_111101_a - var20.field_111101_a;
                            final Position position2 = var22;
                            position2.field_111100_b += var24.field_111100_b - var20.field_111100_b;
                        }
                    }
                }
                if (var21 > 0) {
                    final Position position3 = var22;
                    position3.field_111101_a /= var21;
                    final Position position4 = var22;
                    position4.field_111100_b /= var21;
                    final double var26 = var22.func_111096_b();
                    if (var26 > 0.0) {
                        var22.func_111095_a();
                        var20.func_111094_b(var22);
                    }
                    else {
                        var20.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
                    }
                    var16 = true;
                }
                if (var20.func_111093_a(p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_)) {
                    var16 = true;
                }
            }
            if (!var16) {
                for (final Position var22 : p_110668_14_) {
                    if (!var22.func_111098_b(worldIn)) {
                        var22.func_111097_a(p_110668_5_, p_110668_6_, p_110668_8_, p_110668_10_, p_110668_12_);
                        var16 = true;
                    }
                }
            }
        }
        if (var18 >= 10000) {
            throw new CommandException("commands.spreadplayers.failure." + (p_110668_15_ ? "teams" : "players"), new Object[] { p_110668_14_.length, p_110668_1_.field_111101_a, p_110668_1_.field_111100_b, String.format("%.2f", var17) });
        }
        return var18;
    }
    
    private double func_110671_a(final List p_110671_1_, final World worldIn, final Position[] p_110671_3_, final boolean p_110671_4_) {
        double var5 = 0.0;
        int var6 = 0;
        final HashMap var7 = Maps.newHashMap();
        for (int var8 = 0; var8 < p_110671_1_.size(); ++var8) {
            final Entity var9 = p_110671_1_.get(var8);
            Position var11;
            if (p_110671_4_) {
                final Team var10 = (var9 instanceof EntityPlayer) ? ((EntityPlayer)var9).getTeam() : null;
                if (!var7.containsKey(var10)) {
                    var7.put(var10, p_110671_3_[var6++]);
                }
                var11 = var7.get(var10);
            }
            else {
                var11 = p_110671_3_[var6++];
            }
            var9.setPositionAndUpdate(MathHelper.floor_double(var11.field_111101_a) + 0.5f, var11.func_111092_a(worldIn), MathHelper.floor_double(var11.field_111100_b) + 0.5);
            double var12 = Double.MAX_VALUE;
            for (final Position element : p_110671_3_) {
                if (var11 != element) {
                    final double var13 = var11.func_111099_a(element);
                    var12 = Math.min(var13, var12);
                }
            }
            var5 += var12;
        }
        var5 /= p_110671_1_.size();
        return var5;
    }
    
    private Position[] func_110670_a(final Random p_110670_1_, final int p_110670_2_, final double p_110670_3_, final double p_110670_5_, final double p_110670_7_, final double p_110670_9_) {
        final Position[] var11 = new Position[p_110670_2_];
        for (int var12 = 0; var12 < var11.length; ++var12) {
            final Position var13 = new Position();
            var13.func_111097_a(p_110670_1_, p_110670_3_, p_110670_5_, p_110670_7_, p_110670_9_);
            var11[var12] = var13;
        }
        return var11;
    }
    
    static class Position
    {
        double field_111101_a;
        double field_111100_b;
        private static final String __OBFID = "CL_00001105";
        
        Position() {
        }
        
        Position(final double p_i1358_1_, final double p_i1358_3_) {
            this.field_111101_a = p_i1358_1_;
            this.field_111100_b = p_i1358_3_;
        }
        
        double func_111099_a(final Position p_111099_1_) {
            final double var2 = this.field_111101_a - p_111099_1_.field_111101_a;
            final double var3 = this.field_111100_b - p_111099_1_.field_111100_b;
            return Math.sqrt(var2 * var2 + var3 * var3);
        }
        
        void func_111095_a() {
            final double var1 = this.func_111096_b();
            this.field_111101_a /= var1;
            this.field_111100_b /= var1;
        }
        
        float func_111096_b() {
            return MathHelper.sqrt_double(this.field_111101_a * this.field_111101_a + this.field_111100_b * this.field_111100_b);
        }
        
        public void func_111094_b(final Position p_111094_1_) {
            this.field_111101_a -= p_111094_1_.field_111101_a;
            this.field_111100_b -= p_111094_1_.field_111100_b;
        }
        
        public boolean func_111093_a(final double p_111093_1_, final double p_111093_3_, final double p_111093_5_, final double p_111093_7_) {
            boolean var9 = false;
            if (this.field_111101_a < p_111093_1_) {
                this.field_111101_a = p_111093_1_;
                var9 = true;
            }
            else if (this.field_111101_a > p_111093_5_) {
                this.field_111101_a = p_111093_5_;
                var9 = true;
            }
            if (this.field_111100_b < p_111093_3_) {
                this.field_111100_b = p_111093_3_;
                var9 = true;
            }
            else if (this.field_111100_b > p_111093_7_) {
                this.field_111100_b = p_111093_7_;
                var9 = true;
            }
            return var9;
        }
        
        public int func_111092_a(final World worldIn) {
            BlockPos var2 = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
            while (var2.getY() > 0) {
                var2 = var2.offsetDown();
                if (worldIn.getBlockState(var2).getBlock().getMaterial() != Material.air) {
                    return var2.getY() + 1;
                }
            }
            return 257;
        }
        
        public boolean func_111098_b(final World worldIn) {
            BlockPos var2 = new BlockPos(this.field_111101_a, 256.0, this.field_111100_b);
            while (var2.getY() > 0) {
                var2 = var2.offsetDown();
                final Material var3 = worldIn.getBlockState(var2).getBlock().getMaterial();
                if (var3 != Material.air) {
                    return !var3.isLiquid() && var3 != Material.fire;
                }
            }
            return false;
        }
        
        public void func_111097_a(final Random p_111097_1_, final double p_111097_2_, final double p_111097_4_, final double p_111097_6_, final double p_111097_8_) {
            this.field_111101_a = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_2_, p_111097_6_);
            this.field_111100_b = MathHelper.getRandomDoubleInRange(p_111097_1_, p_111097_4_, p_111097_8_);
        }
    }
}
