/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerSelector {
    private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
    private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
    private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
    private static final Set field_179666_d = Sets.newHashSet((Object[])new String[]{"x", "y", "z", "dx", "dy", "dz", "rm", "r"});
    private static final String __OBFID = "CL_00000086";

    public static EntityPlayerMP matchOnePlayer(ICommandSender p_82386_0_, String p_82386_1_) {
        return (EntityPlayerMP)PlayerSelector.func_179652_a(p_82386_0_, p_82386_1_, EntityPlayerMP.class);
    }

    public static Entity func_179652_a(ICommandSender p_179652_0_, String p_179652_1_, Class p_179652_2_) {
        List var3 = PlayerSelector.func_179656_b(p_179652_0_, p_179652_1_, p_179652_2_);
        return var3.size() == 1 ? (Entity)var3.get(0) : null;
    }

    public static IChatComponent func_150869_b(ICommandSender p_150869_0_, String p_150869_1_) {
        List var2 = PlayerSelector.func_179656_b(p_150869_0_, p_150869_1_, Entity.class);
        if (var2.isEmpty()) {
            return null;
        }
        ArrayList var3 = Lists.newArrayList();
        for (Entity var5 : var2) {
            var3.add(var5.getDisplayName());
        }
        return CommandBase.join(var3);
    }

    public static List func_179656_b(ICommandSender p_179656_0_, String p_179656_1_, Class p_179656_2_) {
        Matcher var3 = tokenPattern.matcher(p_179656_1_);
        if (var3.matches() && p_179656_0_.canCommandSenderUseCommand(1, "@")) {
            Map var4 = PlayerSelector.getArgumentMap(var3.group(2));
            if (!PlayerSelector.func_179655_b(p_179656_0_, var4)) {
                return Collections.emptyList();
            }
            String var5 = var3.group(1);
            BlockPos var6 = PlayerSelector.func_179664_b(var4, p_179656_0_.getPosition());
            List var7 = PlayerSelector.func_179654_a(p_179656_0_, var4);
            ArrayList var8 = Lists.newArrayList();
            for (World var10 : var7) {
                if (var10 == null) continue;
                ArrayList var11 = Lists.newArrayList();
                var11.addAll(PlayerSelector.func_179663_a(var4, var5));
                var11.addAll(PlayerSelector.func_179648_b(var4));
                var11.addAll(PlayerSelector.func_179649_c(var4));
                var11.addAll(PlayerSelector.func_179659_d(var4));
                var11.addAll(PlayerSelector.func_179657_e(var4));
                var11.addAll(PlayerSelector.func_179647_f(var4));
                var11.addAll(PlayerSelector.func_180698_a(var4, var6));
                var11.addAll(PlayerSelector.func_179662_g(var4));
                var8.addAll(PlayerSelector.func_179660_a(var4, p_179656_2_, var11, var5, var10, var6));
            }
            return PlayerSelector.func_179658_a(var8, var4, p_179656_0_, p_179656_2_, var5, var6);
        }
        return Collections.emptyList();
    }

    private static List func_179654_a(ICommandSender p_179654_0_, Map p_179654_1_) {
        ArrayList var2 = Lists.newArrayList();
        if (PlayerSelector.func_179665_h(p_179654_1_)) {
            var2.add(p_179654_0_.getEntityWorld());
        } else {
            Collections.addAll(var2, MinecraftServer.getServer().worldServers);
        }
        return var2;
    }

    private static boolean func_179655_b(ICommandSender p_179655_0_, Map p_179655_1_) {
        String var2 = PlayerSelector.func_179651_b(p_179655_1_, "type");
        String string = var2 = var2 != null && var2.startsWith("!") ? var2.substring(1) : var2;
        if (var2 != null && !EntityList.func_180125_b(var2)) {
            ChatComponentTranslation var3 = new ChatComponentTranslation("commands.generic.entity.invalidType", var2);
            var3.getChatStyle().setColor(EnumChatFormatting.RED);
            p_179655_0_.addChatMessage(var3);
            return false;
        }
        return true;
    }

    private static List func_179663_a(Map p_179663_0_, String p_179663_1_) {
        boolean var7;
        boolean var4;
        ArrayList var2 = Lists.newArrayList();
        String var3 = PlayerSelector.func_179651_b(p_179663_0_, "type");
        boolean bl = var4 = var3 != null && var3.startsWith("!");
        if (var4) {
            var3 = var3.substring(1);
        }
        boolean var6 = !p_179663_1_.equals("e");
        boolean bl2 = var7 = p_179663_1_.equals("r") && var3 != null;
        if (!(var3 != null && p_179663_1_.equals("e") || var7)) {
            if (var6) {
                var2.add(new Predicate(){
                    private static final String __OBFID = "CL_00002358";

                    public boolean func_179624_a(Entity p_179624_1_) {
                        return p_179624_1_ instanceof EntityPlayer;
                    }

                    public boolean apply(Object p_apply_1_) {
                        return this.func_179624_a((Entity)p_apply_1_);
                    }
                });
            }
        } else {
            final String var3_f = var3;
            var2.add(new Predicate(){
                private static final String __OBFID = "CL_00002362";

                public boolean func_179613_a(Entity p_179613_1_) {
                    return EntityList.func_180123_a(p_179613_1_, var3_f) ^ var4;
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179613_a((Entity)p_apply_1_);
                }
            });
        }
        return var2;
    }

    private static List func_179648_b(Map p_179648_0_) {
        ArrayList var1 = Lists.newArrayList();
        final int var2 = PlayerSelector.func_179653_a(p_179648_0_, "lm", -1);
        final int var3 = PlayerSelector.func_179653_a(p_179648_0_, "l", -1);
        if (var2 > -1 || var3 > -1) {
            var1.add(new Predicate(){
                private static final String __OBFID = "CL_00002357";

                public boolean func_179625_a(Entity p_179625_1_) {
                    if (!(p_179625_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    EntityPlayerMP var2x = (EntityPlayerMP)p_179625_1_;
                    return !(var2 > -1 && var2x.experienceLevel < var2 || var3 > -1 && var2x.experienceLevel > var3);
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179625_a((Entity)p_apply_1_);
                }
            });
        }
        return var1;
    }

    private static List func_179649_c(Map p_179649_0_) {
        ArrayList var1 = Lists.newArrayList();
        final int var2 = PlayerSelector.func_179653_a(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());
        if (var2 != WorldSettings.GameType.NOT_SET.getID()) {
            var1.add(new Predicate(){
                private static final String __OBFID = "CL_00002356";

                public boolean func_179619_a(Entity p_179619_1_) {
                    if (!(p_179619_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    EntityPlayerMP var2x = (EntityPlayerMP)p_179619_1_;
                    return var2x.theItemInWorldManager.getGameType().getID() == var2;
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179619_a((Entity)p_apply_1_);
                }
            });
        }
        return var1;
    }

    private static List func_179659_d(Map p_179659_0_) {
        boolean var3;
        ArrayList var1 = Lists.newArrayList();
        String var2 = PlayerSelector.func_179651_b(p_179659_0_, "team");
        boolean bl = var3 = var2 != null && var2.startsWith("!");
        if (var3) {
            var2 = var2.substring(1);
        }
        if (var2 != null) {
            final String var2_f = var2;
            var1.add(new Predicate(){
                private static final String __OBFID = "CL_00002355";

                public boolean func_179621_a(Entity p_179621_1_) {
                    if (!(p_179621_1_ instanceof EntityLivingBase)) {
                        return false;
                    }
                    EntityLivingBase var2x = (EntityLivingBase)p_179621_1_;
                    Team var3x = var2x.getTeam();
                    String var4 = var3x == null ? "" : var3x.getRegisteredName();
                    return var4.equals(var2_f) ^ var3;
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179621_a((Entity)p_apply_1_);
                }
            });
        }
        return var1;
    }

    private static List func_179657_e(Map p_179657_0_) {
        ArrayList var1 = Lists.newArrayList();
        final Map var2 = PlayerSelector.func_96560_a(p_179657_0_);
        if (var2 != null && var2.size() > 0) {
            var1.add(new Predicate(){
                private static final String __OBFID = "CL_00002354";

                public boolean func_179603_a(Entity p_179603_1_) {
                    boolean var6;
                    Map.Entry var4;
                    int var10;
                    Scoreboard var2x = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
                    Iterator var3 = var2.entrySet().iterator();
                    do {
                        String var8;
                        ScoreObjective var7;
                        if (!var3.hasNext()) {
                            return true;
                        }
                        var4 = var3.next();
                        String var5 = (String)var4.getKey();
                        var6 = false;
                        if (var5.endsWith("_min") && var5.length() > 4) {
                            var6 = true;
                            var5 = var5.substring(0, var5.length() - 4);
                        }
                        if ((var7 = var2x.getObjective(var5)) == null) {
                            return false;
                        }
                        String string = var8 = p_179603_1_ instanceof EntityPlayerMP ? p_179603_1_.getName() : p_179603_1_.getUniqueID().toString();
                        if (!var2x.func_178819_b(var8, var7)) {
                            return false;
                        }
                        Score var9 = var2x.getValueFromObjective(var8, var7);
                        var10 = var9.getScorePoints();
                        if (var10 >= (Integer)var4.getValue() || !var6) continue;
                        return false;
                    } while (var10 <= (Integer)var4.getValue() || var6);
                    return false;
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179603_a((Entity)p_apply_1_);
                }
            });
        }
        return var1;
    }

    private static List func_179647_f(Map p_179647_0_) {
        boolean var3;
        ArrayList var1 = Lists.newArrayList();
        String var2 = PlayerSelector.func_179651_b(p_179647_0_, "name");
        boolean bl = var3 = var2 != null && var2.startsWith("!");
        if (var3) {
            var2 = var2.substring(1);
        }
        if (var2 != null) {
            final String var2_f = var2;
            var1.add(new Predicate(){
                private static final String __OBFID = "CL_00002353";

                public boolean func_179600_a(Entity p_179600_1_) {
                    return p_179600_1_.getName().equals(var2_f) ^ var3;
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179600_a((Entity)p_apply_1_);
                }
            });
        }
        return var1;
    }

    private static List func_180698_a(Map p_180698_0_, final BlockPos p_180698_1_) {
        ArrayList var2 = Lists.newArrayList();
        final int var3 = PlayerSelector.func_179653_a(p_180698_0_, "rm", -1);
        final int var4 = PlayerSelector.func_179653_a(p_180698_0_, "r", -1);
        if (p_180698_1_ != null && (var3 >= 0 || var4 >= 0)) {
            final int var5 = var3 * var3;
            final int var6 = var4 * var4;
            var2.add(new Predicate(){
                private static final String __OBFID = "CL_00002352";

                public boolean func_179594_a(Entity p_179594_1_) {
                    int var2 = (int)p_179594_1_.func_174831_c(p_180698_1_);
                    return !(var3 >= 0 && var2 < var5 || var4 >= 0 && var2 > var6);
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179594_a((Entity)p_apply_1_);
                }
            });
        }
        return var2;
    }

    private static List func_179662_g(Map p_179662_0_) {
        int var3;
        int var2;
        ArrayList var1 = Lists.newArrayList();
        if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry")) {
            var2 = PlayerSelector.func_179650_a(PlayerSelector.func_179653_a(p_179662_0_, "rym", 0));
            var3 = PlayerSelector.func_179650_a(PlayerSelector.func_179653_a(p_179662_0_, "ry", 359));
            var1.add(new Predicate(){
                private static final String __OBFID = "CL_00002351";

                public boolean func_179591_a(Entity p_179591_1_) {
                    int var2x = PlayerSelector.func_179650_a((int)Math.floor(p_179591_1_.rotationYaw));
                    return var2 > var3 ? var2x >= var2 || var2x <= var3 : var2x >= var2 && var2x <= var3;
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179591_a((Entity)p_apply_1_);
                }
            });
        }
        if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx")) {
            var2 = PlayerSelector.func_179650_a(PlayerSelector.func_179653_a(p_179662_0_, "rxm", 0));
            var3 = PlayerSelector.func_179650_a(PlayerSelector.func_179653_a(p_179662_0_, "rx", 359));
            var1.add(new Predicate(){
                private static final String __OBFID = "CL_00002361";

                public boolean func_179616_a(Entity p_179616_1_) {
                    int var2x = PlayerSelector.func_179650_a((int)Math.floor(p_179616_1_.rotationPitch));
                    return var2 > var3 ? var2x >= var2 || var2x <= var3 : var2x >= var2 && var2x <= var3;
                }

                public boolean apply(Object p_apply_1_) {
                    return this.func_179616_a((Entity)p_apply_1_);
                }
            });
        }
        return var1;
    }

    private static List func_179660_a(Map p_179660_0_, Class p_179660_1_, List p_179660_2_, String p_179660_3_, World worldIn, BlockPos p_179660_5_) {
        ArrayList var6 = Lists.newArrayList();
        String var7 = PlayerSelector.func_179651_b(p_179660_0_, "type");
        var7 = var7 != null && var7.startsWith("!") ? var7.substring(1) : var7;
        boolean var8 = !p_179660_3_.equals("e");
        boolean var9 = p_179660_3_.equals("r") && var7 != null;
        int var10 = PlayerSelector.func_179653_a(p_179660_0_, "dx", 0);
        int var11 = PlayerSelector.func_179653_a(p_179660_0_, "dy", 0);
        int var12 = PlayerSelector.func_179653_a(p_179660_0_, "dz", 0);
        int var13 = PlayerSelector.func_179653_a(p_179660_0_, "r", -1);
        Predicate var14 = Predicates.and((Iterable)p_179660_2_);
        Predicate var15 = Predicates.and((Predicate)IEntitySelector.selectAnything, (Predicate)var14);
        if (p_179660_5_ != null) {
            int var17;
            boolean var18;
            int var16 = worldIn.playerEntities.size();
            boolean bl = var18 = var16 < (var17 = worldIn.loadedEntityList.size()) / 16;
            if (!(p_179660_0_.containsKey("dx") || p_179660_0_.containsKey("dy") || p_179660_0_.containsKey("dz"))) {
                if (var13 >= 0) {
                    AxisAlignedBB var19 = new AxisAlignedBB(p_179660_5_.getX() - var13, p_179660_5_.getY() - var13, p_179660_5_.getZ() - var13, p_179660_5_.getX() + var13 + 1, p_179660_5_.getY() + var13 + 1, p_179660_5_.getZ() + var13 + 1);
                    if (var8 && var18 && !var9) {
                        var6.addAll(worldIn.func_175661_b(p_179660_1_, var15));
                    } else {
                        var6.addAll(worldIn.func_175647_a(p_179660_1_, var19, var15));
                    }
                } else if (p_179660_3_.equals("a")) {
                    var6.addAll(worldIn.func_175661_b(p_179660_1_, var14));
                } else if (!(p_179660_3_.equals("p") || p_179660_3_.equals("r") && !var9)) {
                    var6.addAll(worldIn.func_175644_a(p_179660_1_, var15));
                } else {
                    var6.addAll(worldIn.func_175661_b(p_179660_1_, var15));
                }
            } else {
                final AxisAlignedBB var19 = PlayerSelector.func_179661_a(p_179660_5_, var10, var11, var12);
                if (var8 && var18 && !var9) {
                    Predicate var20 = new Predicate(){
                        private static final String __OBFID = "CL_00002360";

                        public boolean func_179609_a(Entity p_179609_1_) {
                            return p_179609_1_.posX >= var19.minX && p_179609_1_.posY >= var19.minY && p_179609_1_.posZ >= var19.minZ ? p_179609_1_.posX < var19.maxX && p_179609_1_.posY < var19.maxY && p_179609_1_.posZ < var19.maxZ : false;
                        }

                        public boolean apply(Object p_apply_1_) {
                            return this.func_179609_a((Entity)p_apply_1_);
                        }
                    };
                    var6.addAll(worldIn.func_175661_b(p_179660_1_, Predicates.and((Predicate)var15, (Predicate)var20)));
                } else {
                    var6.addAll(worldIn.func_175647_a(p_179660_1_, var19, var15));
                }
            }
        } else if (p_179660_3_.equals("a")) {
            var6.addAll(worldIn.func_175661_b(p_179660_1_, var14));
        } else if (!(p_179660_3_.equals("p") || p_179660_3_.equals("r") && !var9)) {
            var6.addAll(worldIn.func_175644_a(p_179660_1_, var15));
        } else {
            var6.addAll(worldIn.func_175661_b(p_179660_1_, var15));
        }
        return var6;
    }

    private static List func_179658_a(List p_179658_0_, Map p_179658_1_, ICommandSender p_179658_2_, Class p_179658_3_, String p_179658_4_, final BlockPos p_179658_5_) {
        Entity var7;
        int var6 = PlayerSelector.func_179653_a(p_179658_1_, "c", !p_179658_4_.equals("a") && !p_179658_4_.equals("e") ? 1 : 0);
        if (!(p_179658_4_.equals("p") || p_179658_4_.equals("a") || p_179658_4_.equals("e"))) {
            if (p_179658_4_.equals("r")) {
                Collections.shuffle(p_179658_0_);
            }
        } else if (p_179658_5_ != null) {
            Collections.sort(p_179658_0_, new Comparator(){
                private static final String __OBFID = "CL_00002359";

                public int func_179611_a(Entity p_179611_1_, Entity p_179611_2_) {
                    return ComparisonChain.start().compare(p_179611_1_.getDistanceSq(p_179658_5_), p_179611_2_.getDistanceSq(p_179658_5_)).result();
                }

                public int compare(Object p_compare_1_, Object p_compare_2_) {
                    return this.func_179611_a((Entity)p_compare_1_, (Entity)p_compare_2_);
                }
            });
        }
        if ((var7 = p_179658_2_.getCommandSenderEntity()) != null && p_179658_3_.isAssignableFrom(var7.getClass()) && var6 == 1 && p_179658_0_.contains(var7) && !"r".equals(p_179658_4_)) {
            p_179658_0_ = Lists.newArrayList((Object[])new Entity[]{var7});
        }
        if (var6 != 0) {
            if (var6 < 0) {
                Collections.reverse(p_179658_0_);
            }
            p_179658_0_ = p_179658_0_.subList(0, Math.min(Math.abs(var6), p_179658_0_.size()));
        }
        return p_179658_0_;
    }

    private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_) {
        boolean var4 = p_179661_1_ < 0;
        boolean var5 = p_179661_2_ < 0;
        boolean var6 = p_179661_3_ < 0;
        int var7 = p_179661_0_.getX() + (var4 ? p_179661_1_ : 0);
        int var8 = p_179661_0_.getY() + (var5 ? p_179661_2_ : 0);
        int var9 = p_179661_0_.getZ() + (var6 ? p_179661_3_ : 0);
        int var10 = p_179661_0_.getX() + (var4 ? 0 : p_179661_1_) + 1;
        int var11 = p_179661_0_.getY() + (var5 ? 0 : p_179661_2_) + 1;
        int var12 = p_179661_0_.getZ() + (var6 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB(var7, var8, var9, var10, var11, var12);
    }

    public static int func_179650_a(int p_179650_0_) {
        if ((p_179650_0_ %= 360) >= 160) {
            p_179650_0_ -= 360;
        }
        if (p_179650_0_ < 0) {
            p_179650_0_ += 360;
        }
        return p_179650_0_;
    }

    private static BlockPos func_179664_b(Map p_179664_0_, BlockPos p_179664_1_) {
        return new BlockPos(PlayerSelector.func_179653_a(p_179664_0_, "x", p_179664_1_.getX()), PlayerSelector.func_179653_a(p_179664_0_, "y", p_179664_1_.getY()), PlayerSelector.func_179653_a(p_179664_0_, "z", p_179664_1_.getZ()));
    }

    private static boolean func_179665_h(Map p_179665_0_) {
        String var2;
        Iterator var1 = field_179666_d.iterator();
        do {
            if (var1.hasNext()) continue;
            return false;
        } while (!p_179665_0_.containsKey(var2 = (String)var1.next()));
        return true;
    }

    private static int func_179653_a(Map p_179653_0_, String p_179653_1_, int p_179653_2_) {
        return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault((String)p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
    }

    private static String func_179651_b(Map p_179651_0_, String p_179651_1_) {
        return (String)p_179651_0_.get(p_179651_1_);
    }

    public static Map func_96560_a(Map p_96560_0_) {
        HashMap var1 = Maps.newHashMap();
        for (String var3 : p_96560_0_.keySet()) {
            if (!var3.startsWith("score_") || var3.length() <= "score_".length()) continue;
            var1.put(var3.substring("score_".length()), MathHelper.parseIntWithDefault((String)p_96560_0_.get(var3), 1));
        }
        return var1;
    }

    public static boolean matchesMultiplePlayers(String p_82377_0_) {
        int var4;
        Matcher var1 = tokenPattern.matcher(p_82377_0_);
        if (!var1.matches()) {
            return false;
        }
        Map var2 = PlayerSelector.getArgumentMap(var1.group(2));
        String var3 = var1.group(1);
        int n = var4 = !"a".equals(var3) && !"e".equals(var3) ? 1 : 0;
        return PlayerSelector.func_179653_a(var2, "c", var4) != 1;
    }

    public static boolean hasArguments(String p_82378_0_) {
        return tokenPattern.matcher(p_82378_0_).matches();
    }

    private static Map getArgumentMap(String p_82381_0_) {
        HashMap var1 = Maps.newHashMap();
        if (p_82381_0_ == null) {
            return var1;
        }
        int var2 = 0;
        int var3 = -1;
        Matcher var4 = intListPattern.matcher(p_82381_0_);
        while (var4.find()) {
            String var5 = null;
            switch (var2++) {
                case 0: {
                    var5 = "x";
                    break;
                }
                case 1: {
                    var5 = "y";
                    break;
                }
                case 2: {
                    var5 = "z";
                    break;
                }
                case 3: {
                    var5 = "r";
                }
            }
            if (var5 != null && var4.group(1).length() > 0) {
                var1.put(var5, var4.group(1));
            }
            var3 = var4.end();
        }
        if (var3 < p_82381_0_.length()) {
            Matcher var6 = keyValueListPattern.matcher(var3 == -1 ? p_82381_0_ : p_82381_0_.substring(var3));
            while (var6.find()) {
                var1.put(var6.group(1), var6.group(2));
            }
        }
        return var1;
    }
}

