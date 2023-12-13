/*
 * Decompiled with CFR 0.152.
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
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
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerSelector {
    private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
    private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
    private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
    private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet("x", "y", "z", "dx", "dy", "dz", "rm", "r");

    public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token) {
        return PlayerSelector.matchOneEntity(sender, token, EntityPlayerMP.class);
    }

    public static <T extends Entity> T matchOneEntity(ICommandSender sender, String token, Class<? extends T> targetClass) {
        List<T> list = PlayerSelector.matchEntities(sender, token, targetClass);
        return (T)(list.size() == 1 ? (Entity)list.get(0) : null);
    }

    public static IChatComponent matchEntitiesToChatComponent(ICommandSender sender, String token) {
        List<Entity> list = PlayerSelector.matchEntities(sender, token, Entity.class);
        if (list.isEmpty()) {
            return null;
        }
        ArrayList<IChatComponent> list1 = Lists.newArrayList();
        for (Entity entity : list) {
            list1.add(entity.getDisplayName());
        }
        return CommandBase.join(list1);
    }

    public static <T extends Entity> List<T> matchEntities(ICommandSender sender, String token, Class<? extends T> targetClass) {
        Matcher matcher = tokenPattern.matcher(token);
        if (matcher.matches() && sender.canCommandSenderUseCommand(1, "@")) {
            Map<String, String> map = PlayerSelector.getArgumentMap(matcher.group(2));
            if (!PlayerSelector.isEntityTypeValid(sender, map)) {
                return Collections.emptyList();
            }
            String s2 = matcher.group(1);
            BlockPos blockpos = PlayerSelector.func_179664_b(map, sender.getPosition());
            List<World> list = PlayerSelector.getWorlds(sender, map);
            ArrayList<? extends T> list1 = Lists.newArrayList();
            for (World world : list) {
                if (world == null) continue;
                ArrayList<Predicate<Entity>> list2 = Lists.newArrayList();
                list2.addAll(PlayerSelector.func_179663_a(map, s2));
                list2.addAll(PlayerSelector.func_179648_b(map));
                list2.addAll(PlayerSelector.func_179649_c(map));
                list2.addAll(PlayerSelector.func_179659_d(map));
                list2.addAll(PlayerSelector.func_179657_e(map));
                list2.addAll(PlayerSelector.func_179647_f(map));
                list2.addAll(PlayerSelector.func_180698_a(map, blockpos));
                list2.addAll(PlayerSelector.func_179662_g(map));
                list1.addAll(PlayerSelector.filterResults(map, targetClass, list2, s2, world, blockpos));
            }
            return PlayerSelector.func_179658_a(list1, map, sender, targetClass, s2, blockpos);
        }
        return Collections.emptyList();
    }

    private static List<World> getWorlds(ICommandSender sender, Map<String, String> argumentMap) {
        ArrayList<World> list = Lists.newArrayList();
        if (PlayerSelector.func_179665_h(argumentMap)) {
            list.add(sender.getEntityWorld());
        } else {
            Collections.addAll(list, MinecraftServer.getServer().worldServers);
        }
        return list;
    }

    private static <T extends Entity> boolean isEntityTypeValid(ICommandSender commandSender, Map<String, String> params) {
        String s2 = PlayerSelector.func_179651_b(params, "type");
        String string = s2 = s2 != null && s2.startsWith("!") ? s2.substring(1) : s2;
        if (s2 != null && !EntityList.isStringValidEntityName(s2)) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", s2);
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            commandSender.addChatMessage(chatcomponenttranslation);
            return false;
        }
        return true;
    }

    private static List<Predicate<Entity>> func_179663_a(Map<String, String> p_179663_0_, String p_179663_1_) {
        boolean flag2;
        boolean flag;
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        String s2 = PlayerSelector.func_179651_b(p_179663_0_, "type");
        boolean bl = flag = s2 != null && s2.startsWith("!");
        if (flag) {
            s2 = s2.substring(1);
        }
        boolean flag1 = !p_179663_1_.equals("e");
        boolean bl2 = flag2 = p_179663_1_.equals("r") && s2 != null;
        if (!(s2 != null && p_179663_1_.equals("e") || flag2)) {
            if (flag1) {
                list.add(new Predicate<Entity>(){

                    @Override
                    public boolean apply(Entity p_apply_1_) {
                        return p_apply_1_ instanceof EntityPlayer;
                    }
                });
            }
        } else {
            final String s_f = s2;
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    return EntityList.isStringEntityName(p_apply_1_, s_f) != flag;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> func_179648_b(Map<String, String> p_179648_0_) {
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        final int i2 = PlayerSelector.parseIntWithDefault(p_179648_0_, "lm", -1);
        final int j2 = PlayerSelector.parseIntWithDefault(p_179648_0_, "l", -1);
        if (i2 > -1 || j2 > -1) {
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                    return !(i2 > -1 && entityplayermp.experienceLevel < i2 || j2 > -1 && entityplayermp.experienceLevel > j2);
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> func_179649_c(Map<String, String> p_179649_0_) {
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        final int i2 = PlayerSelector.parseIntWithDefault(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());
        if (i2 != WorldSettings.GameType.NOT_SET.getID()) {
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                    return entityplayermp.theItemInWorldManager.getGameType().getID() == i2;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> func_179659_d(Map<String, String> p_179659_0_) {
        boolean flag;
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        String s2 = PlayerSelector.func_179651_b(p_179659_0_, "team");
        boolean bl = flag = s2 != null && s2.startsWith("!");
        if (flag) {
            s2 = s2.substring(1);
        }
        if (s2 != null) {
            final String s_f = s2;
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityLivingBase)) {
                        return false;
                    }
                    EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
                    Team team = entitylivingbase.getTeam();
                    String s1 = team == null ? "" : team.getRegisteredName();
                    return s1.equals(s_f) != flag;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> func_179657_e(Map<String, String> p_179657_0_) {
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        final Map<String, Integer> map = PlayerSelector.func_96560_a(p_179657_0_);
        if (map != null && map.size() > 0) {
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
                    for (Map.Entry entry : map.entrySet()) {
                        String s1;
                        ScoreObjective scoreobjective;
                        String s2 = (String)entry.getKey();
                        boolean flag = false;
                        if (s2.endsWith("_min") && s2.length() > 4) {
                            flag = true;
                            s2 = s2.substring(0, s2.length() - 4);
                        }
                        if ((scoreobjective = scoreboard.getObjective(s2)) == null) {
                            return false;
                        }
                        String string = s1 = p_apply_1_ instanceof EntityPlayerMP ? p_apply_1_.getName() : p_apply_1_.getUniqueID().toString();
                        if (!scoreboard.entityHasObjective(s1, scoreobjective)) {
                            return false;
                        }
                        Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
                        int i2 = score.getScorePoints();
                        if (i2 < (Integer)entry.getValue() && flag) {
                            return false;
                        }
                        if (i2 <= (Integer)entry.getValue() || flag) continue;
                        return false;
                    }
                    return true;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> func_179647_f(Map<String, String> p_179647_0_) {
        boolean flag;
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        String s2 = PlayerSelector.func_179651_b(p_179647_0_, "name");
        boolean bl = flag = s2 != null && s2.startsWith("!");
        if (flag) {
            s2 = s2.substring(1);
        }
        if (s2 != null) {
            final String s_f = s2;
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    return p_apply_1_.getName().equals(s_f) != flag;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> func_180698_a(Map<String, String> p_180698_0_, final BlockPos p_180698_1_) {
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        final int i2 = PlayerSelector.parseIntWithDefault(p_180698_0_, "rm", -1);
        final int j2 = PlayerSelector.parseIntWithDefault(p_180698_0_, "r", -1);
        if (p_180698_1_ != null && (i2 >= 0 || j2 >= 0)) {
            final int k2 = i2 * i2;
            final int l2 = j2 * j2;
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    int i1 = (int)p_apply_1_.getDistanceSqToCenter(p_180698_1_);
                    return !(i2 >= 0 && i1 < k2 || j2 >= 0 && i1 > l2);
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> func_179662_g(Map<String, String> p_179662_0_) {
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry")) {
            final int i2 = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "rym", 0));
            final int j2 = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "ry", 359));
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationYaw));
                    return i2 > j2 ? i1 >= i2 || i1 <= j2 : i1 >= i2 && i1 <= j2;
                }
            });
        }
        if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx")) {
            final int k2 = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "rxm", 0));
            final int l2 = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "rx", 359));
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(Entity p_apply_1_) {
                    int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationPitch));
                    return k2 > l2 ? i1 >= k2 || i1 <= l2 : i1 >= k2 && i1 <= l2;
                }
            });
        }
        return list;
    }

    private static <T extends Entity> List<T> filterResults(Map<String, String> params, Class<? extends T> entityClass, List<Predicate<Entity>> inputList, String type, World worldIn, BlockPos position) {
        ArrayList<Entity> list = Lists.newArrayList();
        String s2 = PlayerSelector.func_179651_b(params, "type");
        s2 = s2 != null && s2.startsWith("!") ? s2.substring(1) : s2;
        boolean flag = !type.equals("e");
        boolean flag1 = type.equals("r") && s2 != null;
        int i2 = PlayerSelector.parseIntWithDefault(params, "dx", 0);
        int j2 = PlayerSelector.parseIntWithDefault(params, "dy", 0);
        int k2 = PlayerSelector.parseIntWithDefault(params, "dz", 0);
        int l2 = PlayerSelector.parseIntWithDefault(params, "r", -1);
        Predicate<Entity> predicate = Predicates.and(inputList);
        Predicate<Entity> predicate1 = Predicates.and(EntitySelectors.selectAnything, predicate);
        if (position != null) {
            int j1;
            boolean flag2;
            int i1 = worldIn.playerEntities.size();
            boolean bl = flag2 = i1 < (j1 = worldIn.loadedEntityList.size()) / 16;
            if (!(params.containsKey("dx") || params.containsKey("dy") || params.containsKey("dz"))) {
                if (l2 >= 0) {
                    AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(position.getX() - l2, position.getY() - l2, position.getZ() - l2, position.getX() + l2 + 1, position.getY() + l2 + 1, position.getZ() + l2 + 1);
                    if (flag && flag2 && !flag1) {
                        list.addAll(worldIn.getPlayers(entityClass, predicate1));
                    } else {
                        list.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb1, predicate1));
                    }
                } else if (type.equals("a")) {
                    list.addAll(worldIn.getPlayers(entityClass, predicate));
                } else if (!(type.equals("p") || type.equals("r") && !flag1)) {
                    list.addAll(worldIn.getEntities(entityClass, predicate1));
                } else {
                    list.addAll(worldIn.getPlayers(entityClass, predicate1));
                }
            } else {
                final AxisAlignedBB axisalignedbb = PlayerSelector.func_179661_a(position, i2, j2, k2);
                if (flag && flag2 && !flag1) {
                    Predicate<Entity> predicate2 = new Predicate<Entity>(){

                        @Override
                        public boolean apply(Entity p_apply_1_) {
                            return p_apply_1_.posX >= axisalignedbb.minX && p_apply_1_.posY >= axisalignedbb.minY && p_apply_1_.posZ >= axisalignedbb.minZ ? p_apply_1_.posX < axisalignedbb.maxX && p_apply_1_.posY < axisalignedbb.maxY && p_apply_1_.posZ < axisalignedbb.maxZ : false;
                        }
                    };
                    list.addAll(worldIn.getPlayers(entityClass, Predicates.and(predicate1, predicate2)));
                } else {
                    list.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb, predicate1));
                }
            }
        } else if (type.equals("a")) {
            list.addAll(worldIn.getPlayers(entityClass, predicate));
        } else if (!(type.equals("p") || type.equals("r") && !flag1)) {
            list.addAll(worldIn.getEntities(entityClass, predicate1));
        } else {
            list.addAll(worldIn.getPlayers(entityClass, predicate1));
        }
        return list;
    }

    private static <T extends Entity> List<T> func_179658_a(List<T> p_179658_0_, Map<String, String> p_179658_1_, ICommandSender p_179658_2_, Class<? extends T> p_179658_3_, String p_179658_4_, final BlockPos p_179658_5_) {
        Entity entity;
        int i2 = PlayerSelector.parseIntWithDefault(p_179658_1_, "c", !p_179658_4_.equals("a") && !p_179658_4_.equals("e") ? 1 : 0);
        if (!(p_179658_4_.equals("p") || p_179658_4_.equals("a") || p_179658_4_.equals("e"))) {
            if (p_179658_4_.equals("r")) {
                Collections.shuffle(p_179658_0_);
            }
        } else if (p_179658_5_ != null) {
            Collections.sort(p_179658_0_, new Comparator<Entity>(){

                @Override
                public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(p_179658_5_), p_compare_2_.getDistanceSq(p_179658_5_)).result();
                }
            });
        }
        if ((entity = p_179658_2_.getCommandSenderEntity()) != null && p_179658_3_.isAssignableFrom(entity.getClass()) && i2 == 1 && p_179658_0_.contains(entity) && !"r".equals(p_179658_4_)) {
            p_179658_0_ = Lists.newArrayList(entity);
        }
        if (i2 != 0) {
            if (i2 < 0) {
                Collections.reverse(p_179658_0_);
            }
            p_179658_0_ = p_179658_0_.subList(0, Math.min(Math.abs(i2), p_179658_0_.size()));
        }
        return p_179658_0_;
    }

    private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_) {
        boolean flag = p_179661_1_ < 0;
        boolean flag1 = p_179661_2_ < 0;
        boolean flag2 = p_179661_3_ < 0;
        int i2 = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
        int j2 = p_179661_0_.getY() + (flag1 ? p_179661_2_ : 0);
        int k2 = p_179661_0_.getZ() + (flag2 ? p_179661_3_ : 0);
        int l2 = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
        int i1 = p_179661_0_.getY() + (flag1 ? 0 : p_179661_2_) + 1;
        int j1 = p_179661_0_.getZ() + (flag2 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB(i2, j2, k2, l2, i1, j1);
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

    private static BlockPos func_179664_b(Map<String, String> p_179664_0_, BlockPos p_179664_1_) {
        return new BlockPos(PlayerSelector.parseIntWithDefault(p_179664_0_, "x", p_179664_1_.getX()), PlayerSelector.parseIntWithDefault(p_179664_0_, "y", p_179664_1_.getY()), PlayerSelector.parseIntWithDefault(p_179664_0_, "z", p_179664_1_.getZ()));
    }

    private static boolean func_179665_h(Map<String, String> p_179665_0_) {
        for (String s2 : WORLD_BINDING_ARGS) {
            if (!p_179665_0_.containsKey(s2)) continue;
            return true;
        }
        return false;
    }

    private static int parseIntWithDefault(Map<String, String> p_179653_0_, String p_179653_1_, int p_179653_2_) {
        return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault(p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
    }

    private static String func_179651_b(Map<String, String> p_179651_0_, String p_179651_1_) {
        return p_179651_0_.get(p_179651_1_);
    }

    public static Map<String, Integer> func_96560_a(Map<String, String> p_96560_0_) {
        HashMap<String, Integer> map = Maps.newHashMap();
        for (String s2 : p_96560_0_.keySet()) {
            if (!s2.startsWith("score_") || s2.length() <= "score_".length()) continue;
            map.put(s2.substring("score_".length()), MathHelper.parseIntWithDefault(p_96560_0_.get(s2), 1));
        }
        return map;
    }

    public static boolean matchesMultiplePlayers(String p_82377_0_) {
        Matcher matcher = tokenPattern.matcher(p_82377_0_);
        if (!matcher.matches()) {
            return false;
        }
        Map<String, String> map = PlayerSelector.getArgumentMap(matcher.group(2));
        String s2 = matcher.group(1);
        int i2 = !"a".equals(s2) && !"e".equals(s2) ? 1 : 0;
        return PlayerSelector.parseIntWithDefault(map, "c", i2) != 1;
    }

    public static boolean hasArguments(String p_82378_0_) {
        return tokenPattern.matcher(p_82378_0_).matches();
    }

    private static Map<String, String> getArgumentMap(String argumentString) {
        HashMap<String, String> map = Maps.newHashMap();
        if (argumentString == null) {
            return map;
        }
        int i2 = 0;
        int j2 = -1;
        Matcher matcher = intListPattern.matcher(argumentString);
        while (matcher.find()) {
            String s2 = null;
            switch (i2++) {
                case 0: {
                    s2 = "x";
                    break;
                }
                case 1: {
                    s2 = "y";
                    break;
                }
                case 2: {
                    s2 = "z";
                    break;
                }
                case 3: {
                    s2 = "r";
                }
            }
            if (s2 != null && matcher.group(1).length() > 0) {
                map.put(s2, matcher.group(1));
            }
            j2 = matcher.end();
        }
        if (j2 < argumentString.length()) {
            Matcher matcher1 = keyValueListPattern.matcher(j2 == -1 ? argumentString : argumentString.substring(j2));
            while (matcher1.find()) {
                map.put(matcher1.group(1), matcher1.group(2));
            }
        }
        return map;
    }
}

