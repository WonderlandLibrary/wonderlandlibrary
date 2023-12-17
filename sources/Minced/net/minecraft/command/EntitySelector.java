// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.util.EntitySelectors;
import com.google.common.base.Predicates;
import net.minecraft.util.math.MathHelper;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.GameType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.Collection;
import net.minecraft.world.World;
import java.util.Collections;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.Entity;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Predicate;
import java.util.Set;
import com.google.common.base.Splitter;
import java.util.regex.Pattern;

public class EntitySelector
{
    private static final Pattern TOKEN_PATTERN;
    private static final Splitter COMMA_SPLITTER;
    private static final Splitter EQUAL_SPLITTER;
    private static final Set<String> VALID_ARGUMENTS;
    private static final String ARGUMENT_RANGE_MAX;
    private static final String ARGUMENT_RANGE_MIN;
    private static final String ARGUMENT_LEVEL_MAX;
    private static final String ARGUMENT_LEVEL_MIN;
    private static final String ARGUMENT_COORDINATE_X;
    private static final String ARGUMENT_COORDINATE_Y;
    private static final String ARGUMENT_COORDINATE_Z;
    private static final String ARGUMENT_DELTA_X;
    private static final String ARGUMENT_DELTA_Y;
    private static final String ARGUMENT_DELTA_Z;
    private static final String ARGUMENT_ROTX_MAX;
    private static final String ARGUMENT_ROTX_MIN;
    private static final String ARGUMENT_ROTY_MAX;
    private static final String ARGUMENT_ROTY_MIN;
    private static final String ARGUMENT_COUNT;
    private static final String ARGUMENT_MODE;
    private static final String ARGUMENT_TEAM_NAME;
    private static final String ARGUMENT_PLAYER_NAME;
    private static final String ARGUMENT_ENTITY_TYPE;
    private static final String ARGUMENT_ENTITY_TAG;
    private static final Predicate<String> IS_VALID_ARGUMENT;
    private static final Set<String> WORLD_BINDING_ARGS;
    
    private static String addArgument(final String argument) {
        EntitySelector.VALID_ARGUMENTS.add(argument);
        return argument;
    }
    
    @Nullable
    public static EntityPlayerMP matchOnePlayer(final ICommandSender sender, final String token) throws CommandException {
        return matchOneEntity(sender, token, (Class<? extends EntityPlayerMP>)EntityPlayerMP.class);
    }
    
    public static List<EntityPlayerMP> getPlayers(final ICommandSender sender, final String token) throws CommandException {
        return matchEntities(sender, token, (Class<? extends EntityPlayerMP>)EntityPlayerMP.class);
    }
    
    @Nullable
    public static <T extends Entity> T matchOneEntity(final ICommandSender sender, final String token, final Class<? extends T> targetClass) throws CommandException {
        final List<T> list = matchEntities(sender, token, targetClass);
        return (T)((list.size() == 1) ? ((T)list.get(0)) : null);
    }
    
    @Nullable
    public static ITextComponent matchEntitiesToTextComponent(final ICommandSender sender, final String token) throws CommandException {
        final List<Entity> list = matchEntities(sender, token, (Class<? extends Entity>)Entity.class);
        if (list.isEmpty()) {
            return null;
        }
        final List<ITextComponent> list2 = (List<ITextComponent>)Lists.newArrayList();
        for (final Entity entity : list) {
            list2.add(entity.getDisplayName());
        }
        return CommandBase.join(list2);
    }
    
    public static <T extends Entity> List<T> matchEntities(final ICommandSender sender, final String token, final Class<? extends T> targetClass) throws CommandException {
        final Matcher matcher = EntitySelector.TOKEN_PATTERN.matcher(token);
        if (!matcher.matches() || !sender.canUseCommand(1, "@")) {
            return Collections.emptyList();
        }
        final Map<String, String> map = getArgumentMap(matcher.group(2));
        if (!isEntityTypeValid(sender, map)) {
            return Collections.emptyList();
        }
        final String s = matcher.group(1);
        final BlockPos blockpos = getBlockPosFromArguments(map, sender.getPosition());
        final Vec3d vec3d = getPosFromArguments(map, sender.getPositionVector());
        final List<World> list = getWorlds(sender, map);
        final List<T> list2 = (List<T>)Lists.newArrayList();
        for (final World world : list) {
            if (world != null) {
                final List<Predicate<Entity>> list3 = (List<Predicate<Entity>>)Lists.newArrayList();
                list3.addAll(getTypePredicates(map, s));
                list3.addAll(getXpLevelPredicates(map));
                list3.addAll(getGamemodePredicates(map));
                list3.addAll(getTeamPredicates(map));
                list3.addAll(getScorePredicates(sender, map));
                list3.addAll(getNamePredicates(map));
                list3.addAll(getTagPredicates(map));
                list3.addAll(getRadiusPredicates(map, vec3d));
                list3.addAll(getRotationsPredicates(map));
                if ("s".equalsIgnoreCase(s)) {
                    final Entity entity = sender.getCommandSenderEntity();
                    if (entity != null && targetClass.isAssignableFrom(entity.getClass())) {
                        if (map.containsKey(EntitySelector.ARGUMENT_DELTA_X) || map.containsKey(EntitySelector.ARGUMENT_DELTA_Y) || map.containsKey(EntitySelector.ARGUMENT_DELTA_Z)) {
                            final int i = getInt(map, EntitySelector.ARGUMENT_DELTA_X, 0);
                            final int j = getInt(map, EntitySelector.ARGUMENT_DELTA_Y, 0);
                            final int k = getInt(map, EntitySelector.ARGUMENT_DELTA_Z, 0);
                            final AxisAlignedBB axisalignedbb = getAABB(blockpos, i, j, k);
                            if (!axisalignedbb.intersects(entity.getEntityBoundingBox())) {
                                return Collections.emptyList();
                            }
                        }
                        for (final Predicate<Entity> predicate : list3) {
                            if (!predicate.apply((Object)entity)) {
                                return Collections.emptyList();
                            }
                        }
                        return (List<T>)Lists.newArrayList((Object[])new Entity[] { entity });
                    }
                    return Collections.emptyList();
                }
                else {
                    list2.addAll((Collection<? extends T>)filterResults(map, (Class<? extends Entity>)targetClass, list3, s, world, blockpos));
                }
            }
        }
        return getEntitiesFromPredicates(list2, map, sender, targetClass, s, vec3d);
    }
    
    private static List<World> getWorlds(final ICommandSender sender, final Map<String, String> argumentMap) {
        final List<World> list = (List<World>)Lists.newArrayList();
        if (hasArgument(argumentMap)) {
            list.add(sender.getEntityWorld());
        }
        else {
            Collections.addAll(list, sender.getServer().worlds);
        }
        return list;
    }
    
    private static <T extends Entity> boolean isEntityTypeValid(final ICommandSender commandSender, final Map<String, String> params) {
        final String s = getArgument(params, EntitySelector.ARGUMENT_ENTITY_TYPE);
        if (s == null) {
            return true;
        }
        final ResourceLocation resourcelocation = new ResourceLocation(s.startsWith("!") ? s.substring(1) : s);
        if (EntityList.isRegistered(resourcelocation)) {
            return true;
        }
        final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.generic.entity.invalidType", new Object[] { resourcelocation });
        textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
        commandSender.sendMessage(textcomponenttranslation);
        return false;
    }
    
    private static List<Predicate<Entity>> getTypePredicates(final Map<String, String> params, final String type) {
        final String s = getArgument(params, EntitySelector.ARGUMENT_ENTITY_TYPE);
        if (s == null || (!type.equals("e") && !type.equals("r") && !type.equals("s"))) {
            return (List<Predicate<Entity>>)((type.equals("e") || type.equals("s")) ? Collections.emptyList() : Collections.singletonList(new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    return p_apply_1_ instanceof EntityPlayer;
                }
            }));
        }
        final boolean flag = s.startsWith("!");
        final ResourceLocation resourcelocation = new ResourceLocation(flag ? s.substring(1) : s);
        return Collections.singletonList((Predicate<Entity>)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return EntityList.isMatchingName(p_apply_1_, resourcelocation) != flag;
            }
        });
    }
    
    private static List<Predicate<Entity>> getXpLevelPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        final int i = getInt(params, EntitySelector.ARGUMENT_LEVEL_MIN, -1);
        final int j = getInt(params, EntitySelector.ARGUMENT_LEVEL_MAX, -1);
        if (i > -1 || j > -1) {
            list.add((Predicate<Entity>)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                        return false;
                    }
                    final EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                    return (i <= -1 || entityplayermp.experienceLevel >= i) && (j <= -1 || entityplayermp.experienceLevel <= j);
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> getGamemodePredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.ARGUMENT_MODE);
        if (s == null) {
            return list;
        }
        final boolean flag = s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        GameType gametype;
        try {
            final int i = Integer.parseInt(s);
            gametype = GameType.parseGameTypeWithDefault(i, GameType.NOT_SET);
        }
        catch (Throwable var6) {
            gametype = GameType.parseGameTypeWithDefault(s, GameType.NOT_SET);
        }
        final GameType type = gametype;
        list.add((Predicate<Entity>)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                if (!(p_apply_1_ instanceof EntityPlayerMP)) {
                    return false;
                }
                final EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
                final GameType gametype1 = entityplayermp.interactionManager.getGameType();
                return flag ? (gametype1 != type) : (gametype1 == type);
            }
        });
        return list;
    }
    
    private static List<Predicate<Entity>> getTeamPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.ARGUMENT_TEAM_NAME);
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add((Predicate<Entity>)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (!(p_apply_1_ instanceof EntityLivingBase)) {
                        return false;
                    }
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
                    final Team team = entitylivingbase.getTeam();
                    final String s1 = (team == null) ? "" : team.getName();
                    return s1.equals(s_f_) != flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> getScorePredicates(final ICommandSender sender, final Map<String, String> params) {
        final Map<String, Integer> map = getScoreMap(params);
        return map.isEmpty() ? Collections.emptyList() : Lists.newArrayList((Object[])new Predicate[] { (Predicate)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    final Scoreboard scoreboard = sender.getServer().getWorld(0).getScoreboard();
                    for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                        String s = entry.getKey();
                        boolean flag = false;
                        if (s.endsWith("_min") && s.length() > 4) {
                            flag = true;
                            s = s.substring(0, s.length() - 4);
                        }
                        final ScoreObjective scoreobjective = scoreboard.getObjective(s);
                        if (scoreobjective == null) {
                            return false;
                        }
                        final String s2 = (p_apply_1_ instanceof EntityPlayerMP) ? p_apply_1_.getName() : p_apply_1_.getCachedUniqueIdString();
                        if (!scoreboard.entityHasObjective(s2, scoreobjective)) {
                            return false;
                        }
                        final Score score = scoreboard.getOrCreateScore(s2, scoreobjective);
                        final int i = score.getScorePoints();
                        if (i < entry.getValue() && flag) {
                            return false;
                        }
                        if (i > entry.getValue() && !flag) {
                            return false;
                        }
                    }
                    return true;
                }
            } });
    }
    
    private static List<Predicate<Entity>> getNamePredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.ARGUMENT_PLAYER_NAME);
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add((Predicate<Entity>)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    return p_apply_1_ != null && p_apply_1_.getName().equals(s_f_) != flag;
                }
            });
        }
        return list;
    }
    
    private static List<Predicate<Entity>> getTagPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.ARGUMENT_ENTITY_TAG);
        final boolean flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            final String s_f_ = s;
            list.add((Predicate<Entity>)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
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
    
    private static List<Predicate<Entity>> getRadiusPredicates(final Map<String, String> params, final Vec3d pos) {
        final double d0 = getInt(params, EntitySelector.ARGUMENT_RANGE_MIN, -1);
        final double d2 = getInt(params, EntitySelector.ARGUMENT_RANGE_MAX, -1);
        final boolean flag = d0 < -0.5;
        final boolean flag2 = d2 < -0.5;
        if (flag && flag2) {
            return Collections.emptyList();
        }
        final double d3 = Math.max(d0, 1.0E-4);
        final double d4 = d3 * d3;
        final double d5 = Math.max(d2, 1.0E-4);
        final double d6 = d5 * d5;
        return (List<Predicate<Entity>>)Lists.newArrayList((Object[])new Predicate[] { (Predicate)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    final double d6 = pos.squareDistanceTo(p_apply_1_.posX, p_apply_1_.posY, p_apply_1_.posZ);
                    return (flag || d6 >= d4) && (flag2 || d6 <= d6);
                }
            } });
    }
    
    private static List<Predicate<Entity>> getRotationsPredicates(final Map<String, String> params) {
        final List<Predicate<Entity>> list = (List<Predicate<Entity>>)Lists.newArrayList();
        if (params.containsKey(EntitySelector.ARGUMENT_ROTY_MIN) || params.containsKey(EntitySelector.ARGUMENT_ROTY_MAX)) {
            final int i = MathHelper.wrapDegrees(getInt(params, EntitySelector.ARGUMENT_ROTY_MIN, 0));
            final int j = MathHelper.wrapDegrees(getInt(params, EntitySelector.ARGUMENT_ROTY_MAX, 359));
            list.add((Predicate<Entity>)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    final int i1 = MathHelper.wrapDegrees(MathHelper.floor(p_apply_1_.rotationYaw));
                    if (i > j) {
                        return i1 >= i || i1 <= j;
                    }
                    return i1 >= i && i1 <= j;
                }
            });
        }
        if (params.containsKey(EntitySelector.ARGUMENT_ROTX_MIN) || params.containsKey(EntitySelector.ARGUMENT_ROTX_MAX)) {
            final int k = MathHelper.wrapDegrees(getInt(params, EntitySelector.ARGUMENT_ROTX_MIN, 0));
            final int l = MathHelper.wrapDegrees(getInt(params, EntitySelector.ARGUMENT_ROTX_MAX, 359));
            list.add((Predicate<Entity>)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    if (p_apply_1_ == null) {
                        return false;
                    }
                    final int i1 = MathHelper.wrapDegrees(MathHelper.floor(p_apply_1_.rotationPitch));
                    if (k > l) {
                        return i1 >= k || i1 <= l;
                    }
                    return i1 >= k && i1 <= l;
                }
            });
        }
        return list;
    }
    
    private static <T extends Entity> List<T> filterResults(final Map<String, String> params, final Class<? extends T> entityClass, final List<Predicate<Entity>> inputList, final String type, final World worldIn, final BlockPos position) {
        final List<T> list = (List<T>)Lists.newArrayList();
        String s = getArgument(params, EntitySelector.ARGUMENT_ENTITY_TYPE);
        s = ((s != null && s.startsWith("!")) ? s.substring(1) : s);
        final boolean flag = !type.equals("e");
        final boolean flag2 = type.equals("r") && s != null;
        final int i = getInt(params, EntitySelector.ARGUMENT_DELTA_X, 0);
        final int j = getInt(params, EntitySelector.ARGUMENT_DELTA_Y, 0);
        final int k = getInt(params, EntitySelector.ARGUMENT_DELTA_Z, 0);
        final int l = getInt(params, EntitySelector.ARGUMENT_RANGE_MAX, -1);
        final Predicate<Entity> predicate = (Predicate<Entity>)Predicates.and((Iterable)inputList);
        final Predicate<Entity> predicate2 = (Predicate<Entity>)Predicates.and((Predicate)EntitySelectors.IS_ALIVE, (Predicate)predicate);
        if (!params.containsKey(EntitySelector.ARGUMENT_DELTA_X) && !params.containsKey(EntitySelector.ARGUMENT_DELTA_Y) && !params.containsKey(EntitySelector.ARGUMENT_DELTA_Z)) {
            if (l >= 0) {
                final AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(position.getX() - l, position.getY() - l, position.getZ() - l, position.getX() + l + 1, position.getY() + l + 1, position.getZ() + l + 1);
                if (flag && !flag2) {
                    list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (com.google.common.base.Predicate<? super Entity>)predicate2));
                }
                else {
                    list.addAll((Collection<? extends T>)worldIn.getEntitiesWithinAABB((Class<? extends Entity>)entityClass, axisalignedbb1, (com.google.common.base.Predicate<? super Entity>)predicate2));
                }
            }
            else if (type.equals("a")) {
                list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (com.google.common.base.Predicate<? super Entity>)predicate));
            }
            else if (!type.equals("p") && (!type.equals("r") || flag2)) {
                list.addAll((Collection<? extends T>)worldIn.getEntities((Class<? extends Entity>)entityClass, (com.google.common.base.Predicate<? super Entity>)predicate2));
            }
            else {
                list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (com.google.common.base.Predicate<? super Entity>)predicate2));
            }
        }
        else {
            final AxisAlignedBB axisalignedbb2 = getAABB(position, i, j, k);
            if (flag && !flag2) {
                final Predicate<Entity> predicate3 = (Predicate<Entity>)new Predicate<Entity>() {
                    public boolean apply(@Nullable final Entity p_apply_1_) {
                        return p_apply_1_ != null && axisalignedbb2.intersects(p_apply_1_.getEntityBoundingBox());
                    }
                };
                list.addAll((Collection<? extends T>)worldIn.getPlayers((Class<? extends Entity>)entityClass, (com.google.common.base.Predicate<? super Entity>)Predicates.and((Predicate)predicate2, (Predicate)predicate3)));
            }
            else {
                list.addAll((Collection<? extends T>)worldIn.getEntitiesWithinAABB((Class<? extends Entity>)entityClass, axisalignedbb2, (com.google.common.base.Predicate<? super Entity>)predicate2));
            }
        }
        return list;
    }
    
    private static <T extends Entity> List<T> getEntitiesFromPredicates(List<T> matchingEntities, final Map<String, String> params, final ICommandSender sender, final Class<? extends T> targetClass, final String type, final Vec3d pos) {
        final int i = getInt(params, EntitySelector.ARGUMENT_COUNT, (!type.equals("a") && !type.equals("e")) ? 1 : 0);
        if (!type.equals("p") && !type.equals("a") && !type.equals("e")) {
            if (type.equals("r")) {
                Collections.shuffle(matchingEntities);
            }
        }
        else {
            Collections.sort(matchingEntities, new Comparator<Entity>() {
                @Override
                public int compare(final Entity p_compare_1_, final Entity p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(pos.x, pos.y, pos.z), p_compare_2_.getDistanceSq(pos.x, pos.y, pos.z)).result();
                }
            });
        }
        final Entity entity = sender.getCommandSenderEntity();
        if (entity != null && targetClass.isAssignableFrom(entity.getClass()) && i == 1 && matchingEntities.contains(entity) && !"r".equals(type)) {
            matchingEntities = (List<T>)Lists.newArrayList((Object[])new Entity[] { entity });
        }
        if (i != 0) {
            if (i < 0) {
                Collections.reverse(matchingEntities);
            }
            matchingEntities = matchingEntities.subList(0, Math.min(Math.abs(i), matchingEntities.size()));
        }
        return matchingEntities;
    }
    
    private static AxisAlignedBB getAABB(final BlockPos pos, final int x, final int y, final int z) {
        final boolean flag = x < 0;
        final boolean flag2 = y < 0;
        final boolean flag3 = z < 0;
        final int i = pos.getX() + (flag ? x : 0);
        final int j = pos.getY() + (flag2 ? y : 0);
        final int k = pos.getZ() + (flag3 ? z : 0);
        final int l = pos.getX() + (flag ? 0 : x) + 1;
        final int i2 = pos.getY() + (flag2 ? 0 : y) + 1;
        final int j2 = pos.getZ() + (flag3 ? 0 : z) + 1;
        return new AxisAlignedBB(i, j, k, l, i2, j2);
    }
    
    private static BlockPos getBlockPosFromArguments(final Map<String, String> params, final BlockPos pos) {
        return new BlockPos(getInt(params, EntitySelector.ARGUMENT_COORDINATE_X, pos.getX()), getInt(params, EntitySelector.ARGUMENT_COORDINATE_Y, pos.getY()), getInt(params, EntitySelector.ARGUMENT_COORDINATE_Z, pos.getZ()));
    }
    
    private static Vec3d getPosFromArguments(final Map<String, String> params, final Vec3d pos) {
        return new Vec3d(getCoordinate(params, EntitySelector.ARGUMENT_COORDINATE_X, pos.x, true), getCoordinate(params, EntitySelector.ARGUMENT_COORDINATE_Y, pos.y, false), getCoordinate(params, EntitySelector.ARGUMENT_COORDINATE_Z, pos.z, true));
    }
    
    private static double getCoordinate(final Map<String, String> params, final String key, final double defaultD, final boolean offset) {
        return params.containsKey(key) ? (MathHelper.getInt(params.get(key), MathHelper.floor(defaultD)) + (offset ? 0.5 : 0.0)) : defaultD;
    }
    
    private static boolean hasArgument(final Map<String, String> params) {
        for (final String s : EntitySelector.WORLD_BINDING_ARGS) {
            if (params.containsKey(s)) {
                return true;
            }
        }
        return false;
    }
    
    private static int getInt(final Map<String, String> params, final String key, final int defaultI) {
        return params.containsKey(key) ? MathHelper.getInt(params.get(key), defaultI) : defaultI;
    }
    
    @Nullable
    private static String getArgument(final Map<String, String> params, final String key) {
        return params.get(key);
    }
    
    public static Map<String, Integer> getScoreMap(final Map<String, String> params) {
        final Map<String, Integer> map = (Map<String, Integer>)Maps.newHashMap();
        for (final String s : params.keySet()) {
            if (s.startsWith("score_") && s.length() > "score_".length()) {
                map.put(s.substring("score_".length()), MathHelper.getInt(params.get(s), 1));
            }
        }
        return map;
    }
    
    public static boolean matchesMultiplePlayers(final String selectorStr) throws CommandException {
        final Matcher matcher = EntitySelector.TOKEN_PATTERN.matcher(selectorStr);
        if (!matcher.matches()) {
            return false;
        }
        final Map<String, String> map = getArgumentMap(matcher.group(2));
        final String s = matcher.group(1);
        final int i = (!"a".equals(s) && !"e".equals(s)) ? 1 : 0;
        return getInt(map, EntitySelector.ARGUMENT_COUNT, i) != 1;
    }
    
    public static boolean isSelector(final String selectorStr) {
        return EntitySelector.TOKEN_PATTERN.matcher(selectorStr).matches();
    }
    
    private static Map<String, String> getArgumentMap(@Nullable final String argumentString) throws CommandException {
        final Map<String, String> map = (Map<String, String>)Maps.newHashMap();
        if (argumentString == null) {
            return map;
        }
        for (final String s : EntitySelector.COMMA_SPLITTER.split((CharSequence)argumentString)) {
            final Iterator<String> iterator = EntitySelector.EQUAL_SPLITTER.split((CharSequence)s).iterator();
            final String s2 = iterator.next();
            if (!EntitySelector.IS_VALID_ARGUMENT.apply((Object)s2)) {
                throw new CommandException("commands.generic.selector_argument", new Object[] { s });
            }
            map.put(s2, iterator.hasNext() ? iterator.next() : "");
        }
        return map;
    }
    
    static {
        TOKEN_PATTERN = Pattern.compile("^@([pares])(?:\\[([^ ]*)\\])?$");
        COMMA_SPLITTER = Splitter.on(',').omitEmptyStrings();
        EQUAL_SPLITTER = Splitter.on('=').limit(2);
        VALID_ARGUMENTS = Sets.newHashSet();
        ARGUMENT_RANGE_MAX = addArgument("r");
        ARGUMENT_RANGE_MIN = addArgument("rm");
        ARGUMENT_LEVEL_MAX = addArgument("l");
        ARGUMENT_LEVEL_MIN = addArgument("lm");
        ARGUMENT_COORDINATE_X = addArgument("x");
        ARGUMENT_COORDINATE_Y = addArgument("y");
        ARGUMENT_COORDINATE_Z = addArgument("z");
        ARGUMENT_DELTA_X = addArgument("dx");
        ARGUMENT_DELTA_Y = addArgument("dy");
        ARGUMENT_DELTA_Z = addArgument("dz");
        ARGUMENT_ROTX_MAX = addArgument("rx");
        ARGUMENT_ROTX_MIN = addArgument("rxm");
        ARGUMENT_ROTY_MAX = addArgument("ry");
        ARGUMENT_ROTY_MIN = addArgument("rym");
        ARGUMENT_COUNT = addArgument("c");
        ARGUMENT_MODE = addArgument("m");
        ARGUMENT_TEAM_NAME = addArgument("team");
        ARGUMENT_PLAYER_NAME = addArgument("name");
        ARGUMENT_ENTITY_TYPE = addArgument("type");
        ARGUMENT_ENTITY_TAG = addArgument("tag");
        IS_VALID_ARGUMENT = (Predicate)new Predicate<String>() {
            public boolean apply(@Nullable final String p_apply_1_) {
                return p_apply_1_ != null && (EntitySelector.VALID_ARGUMENTS.contains(p_apply_1_) || (p_apply_1_.length() > "score_".length() && p_apply_1_.startsWith("score_")));
            }
        };
        WORLD_BINDING_ARGS = Sets.newHashSet((Object[])new String[] { EntitySelector.ARGUMENT_COORDINATE_X, EntitySelector.ARGUMENT_COORDINATE_Y, EntitySelector.ARGUMENT_COORDINATE_Z, EntitySelector.ARGUMENT_DELTA_X, EntitySelector.ARGUMENT_DELTA_Y, EntitySelector.ARGUMENT_DELTA_Z, EntitySelector.ARGUMENT_RANGE_MIN, EntitySelector.ARGUMENT_RANGE_MAX });
    }
}
