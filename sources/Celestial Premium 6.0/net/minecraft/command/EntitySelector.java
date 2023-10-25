/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
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
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
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
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

public class EntitySelector {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("^@([pares])(?:\\[([^ ]*)\\])?$");
    private static final Splitter field_190828_b = Splitter.on(',').omitEmptyStrings();
    private static final Splitter field_190829_c = Splitter.on('=').limit(2);
    private static final Set<String> field_190830_d = Sets.newHashSet();
    private static final String field_190831_e = EntitySelector.func_190826_c("r");
    private static final String field_190832_f = EntitySelector.func_190826_c("rm");
    private static final String field_190833_g = EntitySelector.func_190826_c("l");
    private static final String field_190834_h = EntitySelector.func_190826_c("lm");
    private static final String field_190835_i = EntitySelector.func_190826_c("x");
    private static final String field_190836_j = EntitySelector.func_190826_c("y");
    private static final String field_190837_k = EntitySelector.func_190826_c("z");
    private static final String field_190838_l = EntitySelector.func_190826_c("dx");
    private static final String field_190839_m = EntitySelector.func_190826_c("dy");
    private static final String field_190840_n = EntitySelector.func_190826_c("dz");
    private static final String field_190841_o = EntitySelector.func_190826_c("rx");
    private static final String field_190842_p = EntitySelector.func_190826_c("rxm");
    private static final String field_190843_q = EntitySelector.func_190826_c("ry");
    private static final String field_190844_r = EntitySelector.func_190826_c("rym");
    private static final String field_190845_s = EntitySelector.func_190826_c("c");
    private static final String field_190846_t = EntitySelector.func_190826_c("m");
    private static final String field_190847_u = EntitySelector.func_190826_c("team");
    private static final String field_190848_v = EntitySelector.func_190826_c("name");
    private static final String field_190849_w = EntitySelector.func_190826_c("type");
    private static final String field_190850_x = EntitySelector.func_190826_c("tag");
    private static final Predicate<String> field_190851_y = new Predicate<String>(){

        @Override
        public boolean apply(@Nullable String p_apply_1_) {
            return p_apply_1_ != null && (field_190830_d.contains(p_apply_1_) || p_apply_1_.length() > "score_".length() && p_apply_1_.startsWith("score_"));
        }
    };
    private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet(field_190835_i, field_190836_j, field_190837_k, field_190838_l, field_190839_m, field_190840_n, field_190832_f, field_190831_e);

    private static String func_190826_c(String p_190826_0_) {
        field_190830_d.add(p_190826_0_);
        return p_190826_0_;
    }

    @Nullable
    public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token) throws CommandException {
        return EntitySelector.matchOneEntity(sender, token, EntityPlayerMP.class);
    }

    public static List<EntityPlayerMP> func_193531_b(ICommandSender p_193531_0_, String p_193531_1_) throws CommandException {
        return EntitySelector.matchEntities(p_193531_0_, p_193531_1_, EntityPlayerMP.class);
    }

    @Nullable
    public static <T extends Entity> T matchOneEntity(ICommandSender sender, String token, Class<? extends T> targetClass) throws CommandException {
        List<T> list = EntitySelector.matchEntities(sender, token, targetClass);
        return (T)(list.size() == 1 ? (Entity)list.get(0) : null);
    }

    @Nullable
    public static ITextComponent matchEntitiesToTextComponent(ICommandSender sender, String token) throws CommandException {
        List<Entity> list = EntitySelector.matchEntities(sender, token, Entity.class);
        if (list.isEmpty()) {
            return null;
        }
        ArrayList<ITextComponent> list1 = Lists.newArrayList();
        for (Entity entity : list) {
            list1.add(entity.getDisplayName());
        }
        return CommandBase.join(list1);
    }

    public static <T extends Entity> List<T> matchEntities(ICommandSender sender, String token, Class<? extends T> targetClass) throws CommandException {
        Matcher matcher = TOKEN_PATTERN.matcher(token);
        if (matcher.matches() && sender.canCommandSenderUseCommand(1, "@")) {
            Map<String, String> map = EntitySelector.getArgumentMap(matcher.group(2));
            if (!EntitySelector.isEntityTypeValid(sender, map)) {
                return Collections.emptyList();
            }
            String s = matcher.group(1);
            BlockPos blockpos = EntitySelector.getBlockPosFromArguments(map, sender.getPosition());
            Vec3d vec3d = EntitySelector.getPosFromArguments(map, sender.getPositionVector());
            List<World> list = EntitySelector.getWorlds(sender, map);
            ArrayList<T> list1 = Lists.newArrayList();
            for (World world : list) {
                if (world == null) continue;
                ArrayList<Predicate<Entity>> list2 = Lists.newArrayList();
                list2.addAll(EntitySelector.getTypePredicates(map, s));
                list2.addAll(EntitySelector.getXpLevelPredicates(map));
                list2.addAll(EntitySelector.getGamemodePredicates(map));
                list2.addAll(EntitySelector.getTeamPredicates(map));
                list2.addAll(EntitySelector.getScorePredicates(sender, map));
                list2.addAll(EntitySelector.getNamePredicates(map));
                list2.addAll(EntitySelector.getTagPredicates(map));
                list2.addAll(EntitySelector.getRadiusPredicates(map, vec3d));
                list2.addAll(EntitySelector.getRotationsPredicates(map));
                if ("s".equalsIgnoreCase(s)) {
                    Entity entity = sender.getCommandSenderEntity();
                    if (entity != null && targetClass.isAssignableFrom(entity.getClass())) {
                        int k;
                        int n;
                        int i;
                        AxisAlignedBB axisalignedbb;
                        if ((map.containsKey(field_190838_l) || map.containsKey(field_190839_m) || map.containsKey(field_190840_n)) && !(axisalignedbb = EntitySelector.getAABB(blockpos, i = EntitySelector.getInt(map, field_190838_l, 0), n = EntitySelector.getInt(map, field_190839_m, 0), k = EntitySelector.getInt(map, field_190840_n, 0))).intersectsWith(entity.getEntityBoundingBox())) {
                            return Collections.emptyList();
                        }
                        for (Predicate predicate : list2) {
                            if (predicate.apply(entity)) continue;
                            return Collections.emptyList();
                        }
                        return Lists.newArrayList(entity);
                    }
                    return Collections.emptyList();
                }
                list1.addAll(EntitySelector.filterResults(map, targetClass, list2, s, world, blockpos));
            }
            return EntitySelector.getEntitiesFromPredicates(list1, map, sender, targetClass, s, vec3d);
        }
        return Collections.emptyList();
    }

    private static List<World> getWorlds(ICommandSender sender, Map<String, String> argumentMap) {
        ArrayList<World> list = Lists.newArrayList();
        if (EntitySelector.hasArgument(argumentMap)) {
            list.add(sender.getEntityWorld());
        } else {
            Collections.addAll(list, sender.getServer().worldServers);
        }
        return list;
    }

    private static <T extends Entity> boolean isEntityTypeValid(ICommandSender commandSender, Map<String, String> params) {
        String s = EntitySelector.getArgument(params, field_190849_w);
        if (s == null) {
            return true;
        }
        ResourceLocation resourcelocation = new ResourceLocation(s.startsWith("!") ? s.substring(1) : s);
        if (EntityList.isStringValidEntityName(resourcelocation)) {
            return true;
        }
        TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.generic.entity.invalidType", resourcelocation);
        textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
        commandSender.addChatMessage(textcomponenttranslation);
        return false;
    }

    private static List<Predicate<Entity>> getTypePredicates(Map<String, String> params, String type) {
        String s = EntitySelector.getArgument(params, field_190849_w);
        if (s == null || !type.equals("e") && !type.equals("r") && !type.equals("s")) {
            return !type.equals("e") && !type.equals("s") ? Collections.singletonList(new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    return p_apply_1_ instanceof EntityPlayer;
                }
            }) : Collections.emptyList();
        }
        final boolean flag = s.startsWith("!");
        final ResourceLocation resourcelocation = new ResourceLocation(flag ? s.substring(1) : s);
        return Collections.singletonList(new Predicate<Entity>(){

            @Override
            public boolean apply(@Nullable Entity p_apply_1_) {
                return EntityList.isStringEntityName(p_apply_1_, resourcelocation) != flag;
            }
        });
    }

    private static List<Predicate<Entity>> getXpLevelPredicates(Map<String, String> params) {
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        final int i = EntitySelector.getInt(params, field_190834_h, -1);
        final int j = EntitySelector.getInt(params, field_190833_g, -1);
        if (i > -1 || j > -1) {
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                    return !(i > -1 && entityplayermp.experienceLevel < i || j > -1 && entityplayermp.experienceLevel > j);
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> getGamemodePredicates(Map<String, String> params) {
        GameType gametype;
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        String s = EntitySelector.getArgument(params, field_190846_t);
        if (s == null) {
            return list;
        }
        final boolean flag = s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        try {
            int i = Integer.parseInt(s);
            gametype = GameType.parseGameTypeWithDefault(i, GameType.NOT_SET);
        }
        catch (Throwable var6) {
            gametype = GameType.parseGameTypeWithDefault(s, GameType.NOT_SET);
        }
        final GameType type = gametype;
        list.add(new Predicate<Entity>(){

            @Override
            public boolean apply(@Nullable Entity p_apply_1_) {
                if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                    return false;
                }
                EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                GameType gametype1 = entityplayermp.interactionManager.getGameType();
                return flag ? gametype1 != type : gametype1 == type;
            }
        });
        return list;
    }

    private static List<Predicate<Entity>> getTeamPredicates(Map<String, String> params) {
        boolean flag;
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        String s = EntitySelector.getArgument(params, field_190847_u);
        boolean bl = flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityLivingBase)) {
                        return false;
                    }
                    EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
                    Team team = entitylivingbase.getTeam();
                    String s1 = team == null ? "" : team.getRegisteredName();
                    return s1.equals(s_f_) != flag;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> getScorePredicates(final ICommandSender sender, Map<String, String> params) {
        final Map<String, Integer> map = EntitySelector.getScoreMap(params);
        return map.isEmpty() ? Collections.emptyList() : Lists.newArrayList(new Predicate<Entity>(){

            @Override
            public boolean apply(@Nullable Entity p_apply_1_) {
                if (p_apply_1_ == null) {
                    return false;
                }
                Scoreboard scoreboard = sender.getServer().getWorld(0).getScoreboard();
                for (Map.Entry entry : map.entrySet()) {
                    String s1;
                    ScoreObjective scoreobjective;
                    String s = (String)entry.getKey();
                    boolean flag = false;
                    if (s.endsWith("_min") && s.length() > 4) {
                        flag = true;
                        s = s.substring(0, s.length() - 4);
                    }
                    if ((scoreobjective = scoreboard.getObjective(s)) == null) {
                        return false;
                    }
                    String string = s1 = p_apply_1_ instanceof EntityPlayerMP ? p_apply_1_.getName() : p_apply_1_.getCachedUniqueIdString();
                    if (!scoreboard.entityHasObjective(s1, scoreobjective)) {
                        return false;
                    }
                    Score score = scoreboard.getOrCreateScore(s1, scoreobjective);
                    int i = score.getScorePoints();
                    if (i < (Integer)entry.getValue() && flag) {
                        return false;
                    }
                    if (i <= (Integer)entry.getValue() || flag) continue;
                    return false;
                }
                return true;
            }
        });
    }

    private static List<Predicate<Entity>> getNamePredicates(Map<String, String> params) {
        boolean flag;
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        String s = EntitySelector.getArgument(params, field_190848_v);
        boolean bl = flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    return p_apply_1_ != null && p_apply_1_.getName().equals(s_f_) != flag;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> getTagPredicates(Map<String, String> params) {
        boolean flag;
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        String s = EntitySelector.getArgument(params, field_190850_x);
        boolean bl = flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    if ("".equals(s_f_)) {
                        return p_apply_1_.getTags().isEmpty() != flag;
                    }
                    return p_apply_1_.getTags().contains(s_f_) != flag;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> getRadiusPredicates(Map<String, String> params, final Vec3d pos) {
        boolean flag1;
        double d0 = EntitySelector.getInt(params, field_190832_f, -1);
        double d1 = EntitySelector.getInt(params, field_190831_e, -1);
        final boolean flag = d0 < -0.5;
        boolean bl = flag1 = d1 < -0.5;
        if (flag && flag1) {
            return Collections.emptyList();
        }
        double d2 = Math.max(d0, 1.0E-4);
        final double d3 = d2 * d2;
        double d4 = Math.max(d1, 1.0E-4);
        final double d5 = d4 * d4;
        return Lists.newArrayList(new Predicate<Entity>(){

            @Override
            public boolean apply(@Nullable Entity p_apply_1_) {
                if (p_apply_1_ == null) {
                    return false;
                }
                double d6 = pos.squareDistanceTo(p_apply_1_.posX, p_apply_1_.posY, p_apply_1_.posZ);
                return (flag || d6 >= d3) && (flag1 || d6 <= d5);
            }
        });
    }

    private static List<Predicate<Entity>> getRotationsPredicates(Map<String, String> params) {
        ArrayList<Predicate<Entity>> list = Lists.newArrayList();
        if (params.containsKey(field_190844_r) || params.containsKey(field_190843_q)) {
            final int i = MathHelper.clampAngle(EntitySelector.getInt(params, field_190844_r, 0));
            final int j = MathHelper.clampAngle(EntitySelector.getInt(params, field_190843_q, 359));
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    int i1 = MathHelper.clampAngle(MathHelper.floor(p_apply_1_.rotationYaw));
                    if (i > j) {
                        return i1 >= i || i1 <= j;
                    }
                    return i1 >= i && i1 <= j;
                }
            });
        }
        if (params.containsKey(field_190842_p) || params.containsKey(field_190841_o)) {
            final int k = MathHelper.clampAngle(EntitySelector.getInt(params, field_190842_p, 0));
            final int l = MathHelper.clampAngle(EntitySelector.getInt(params, field_190841_o, 359));
            list.add(new Predicate<Entity>(){

                @Override
                public boolean apply(@Nullable Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    int i1 = MathHelper.clampAngle(MathHelper.floor(p_apply_1_.rotationPitch));
                    if (k > l) {
                        return i1 >= k || i1 <= l;
                    }
                    return i1 >= k && i1 <= l;
                }
            });
        }
        return list;
    }

    private static <T extends Entity> List<T> filterResults(Map<String, String> params, Class<? extends T> entityClass, List<Predicate<Entity>> inputList, String type, World worldIn, BlockPos position) {
        ArrayList<Entity> list = Lists.newArrayList();
        String s = EntitySelector.getArgument(params, field_190849_w);
        s = s != null && s.startsWith("!") ? s.substring(1) : s;
        boolean flag = !type.equals("e");
        boolean flag1 = type.equals("r") && s != null;
        int i = EntitySelector.getInt(params, field_190838_l, 0);
        int j = EntitySelector.getInt(params, field_190839_m, 0);
        int k = EntitySelector.getInt(params, field_190840_n, 0);
        int l = EntitySelector.getInt(params, field_190831_e, -1);
        Predicate<Entity> predicate = Predicates.and(inputList);
        Predicate<Entity> predicate1 = Predicates.and(EntitySelectors.IS_ALIVE, predicate);
        if (!(params.containsKey(field_190838_l) || params.containsKey(field_190839_m) || params.containsKey(field_190840_n))) {
            if (l >= 0) {
                AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(position.getX() - l, position.getY() - l, position.getZ() - l, position.getX() + l + 1, position.getY() + l + 1, position.getZ() + l + 1);
                if (flag && !flag1) {
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
            final AxisAlignedBB axisalignedbb = EntitySelector.getAABB(position, i, j, k);
            if (flag && !flag1) {
                Predicate<Entity> predicate2 = new Predicate<Entity>(){

                    @Override
                    public boolean apply(@Nullable Entity p_apply_1_) {
                        return p_apply_1_ != null && axisalignedbb.intersectsWith(p_apply_1_.getEntityBoundingBox());
                    }
                };
                list.addAll(worldIn.getPlayers(entityClass, Predicates.and(predicate1, predicate2)));
            } else {
                list.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb, predicate1));
            }
        }
        return list;
    }

    private static <T extends Entity> List<T> getEntitiesFromPredicates(List<T> matchingEntities, Map<String, String> params, ICommandSender sender, Class<? extends T> targetClass, String type, final Vec3d pos) {
        Entity entity;
        int i = EntitySelector.getInt(params, field_190845_s, !type.equals("a") && !type.equals("e") ? 1 : 0);
        if (!(type.equals("p") || type.equals("a") || type.equals("e"))) {
            if (type.equals("r")) {
                Collections.shuffle(matchingEntities);
            }
        } else {
            Collections.sort(matchingEntities, new Comparator<Entity>(){

                @Override
                public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(pos.x, pos.y, pos.z), p_compare_2_.getDistanceSq(pos.x, pos.y, pos.z)).result();
                }
            });
        }
        if ((entity = sender.getCommandSenderEntity()) != null && targetClass.isAssignableFrom(entity.getClass()) && i == 1 && matchingEntities.contains(entity) && !"r".equals(type)) {
            matchingEntities = Lists.newArrayList(entity);
        }
        if (i != 0) {
            if (i < 0) {
                Collections.reverse(matchingEntities);
            }
            matchingEntities = matchingEntities.subList(0, Math.min(Math.abs(i), matchingEntities.size()));
        }
        return matchingEntities;
    }

    private static AxisAlignedBB getAABB(BlockPos pos, int x, int y, int z) {
        boolean flag = x < 0;
        boolean flag1 = y < 0;
        boolean flag2 = z < 0;
        int i = pos.getX() + (flag ? x : 0);
        int j = pos.getY() + (flag1 ? y : 0);
        int k = pos.getZ() + (flag2 ? z : 0);
        int l = pos.getX() + (flag ? 0 : x) + 1;
        int i1 = pos.getY() + (flag1 ? 0 : y) + 1;
        int j1 = pos.getZ() + (flag2 ? 0 : z) + 1;
        return new AxisAlignedBB(i, j, k, l, i1, j1);
    }

    private static BlockPos getBlockPosFromArguments(Map<String, String> params, BlockPos pos) {
        return new BlockPos(EntitySelector.getInt(params, field_190835_i, pos.getX()), EntitySelector.getInt(params, field_190836_j, pos.getY()), EntitySelector.getInt(params, field_190837_k, pos.getZ()));
    }

    private static Vec3d getPosFromArguments(Map<String, String> params, Vec3d pos) {
        return new Vec3d(EntitySelector.getCoordinate(params, field_190835_i, pos.x, true), EntitySelector.getCoordinate(params, field_190836_j, pos.y, false), EntitySelector.getCoordinate(params, field_190837_k, pos.z, true));
    }

    private static double getCoordinate(Map<String, String> params, String key, double defaultD, boolean offset) {
        return params.containsKey(key) ? (double)MathHelper.getInt(params.get(key), MathHelper.floor(defaultD)) + (offset ? 0.5 : 0.0) : defaultD;
    }

    private static boolean hasArgument(Map<String, String> params) {
        for (String s : WORLD_BINDING_ARGS) {
            if (!params.containsKey(s)) continue;
            return true;
        }
        return false;
    }

    private static int getInt(Map<String, String> params, String key, int defaultI) {
        return params.containsKey(key) ? MathHelper.getInt(params.get(key), defaultI) : defaultI;
    }

    @Nullable
    private static String getArgument(Map<String, String> params, String key) {
        return params.get(key);
    }

    public static Map<String, Integer> getScoreMap(Map<String, String> params) {
        HashMap<String, Integer> map = Maps.newHashMap();
        for (String s : params.keySet()) {
            if (!s.startsWith("score_") || s.length() <= "score_".length()) continue;
            map.put(s.substring("score_".length()), MathHelper.getInt(params.get(s), 1));
        }
        return map;
    }

    public static boolean matchesMultiplePlayers(String selectorStr) throws CommandException {
        Matcher matcher = TOKEN_PATTERN.matcher(selectorStr);
        if (!matcher.matches()) {
            return false;
        }
        Map<String, String> map = EntitySelector.getArgumentMap(matcher.group(2));
        String s = matcher.group(1);
        int i = !"a".equals(s) && !"e".equals(s) ? 1 : 0;
        return EntitySelector.getInt(map, field_190845_s, i) != 1;
    }

    public static boolean hasArguments(String selectorStr) {
        return TOKEN_PATTERN.matcher(selectorStr).matches();
    }

    private static Map<String, String> getArgumentMap(@Nullable String argumentString) throws CommandException {
        HashMap<String, String> map = Maps.newHashMap();
        if (argumentString == null) {
            return map;
        }
        for (String s : field_190828_b.split(argumentString)) {
            Iterator<String> iterator = field_190829_c.split(s).iterator();
            String s1 = iterator.next();
            if (!field_190851_y.apply(s1)) {
                throw new CommandException("commands.generic.selector_argument", s);
            }
            map.put(s1, iterator.hasNext() ? iterator.next() : "");
        }
        return map;
    }
}

