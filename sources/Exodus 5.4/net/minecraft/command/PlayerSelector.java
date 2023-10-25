/*
 * Decompiled with CFR 0.152.
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
    private static final Pattern keyValueListPattern;
    private static final Pattern intListPattern;
    private static final Set<String> WORLD_BINDING_ARGS;

    private static BlockPos func_179664_b(Map<String, String> map, BlockPos blockPos) {
        return new BlockPos(PlayerSelector.parseIntWithDefault(map, "x", blockPos.getX()), PlayerSelector.parseIntWithDefault(map, "y", blockPos.getY()), PlayerSelector.parseIntWithDefault(map, "z", blockPos.getZ()));
    }

    public static <T extends Entity> T matchOneEntity(ICommandSender iCommandSender, String string, Class<? extends T> clazz) {
        List<T> list = PlayerSelector.matchEntities(iCommandSender, string, clazz);
        return (T)(list.size() == 1 ? (Entity)list.get(0) : null);
    }

    private static String func_179651_b(Map<String, String> map, String string) {
        return map.get(string);
    }

    private static List<Predicate<Entity>> func_180698_a(Map<String, String> map, final BlockPos blockPos) {
        ArrayList arrayList = Lists.newArrayList();
        final int n = PlayerSelector.parseIntWithDefault(map, "rm", -1);
        final int n2 = PlayerSelector.parseIntWithDefault(map, "r", -1);
        if (blockPos != null && (n >= 0 || n2 >= 0)) {
            final int n3 = n * n;
            final int n4 = n2 * n2;
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    int n5 = (int)entity.getDistanceSqToCenter(blockPos);
                    return !(n >= 0 && n5 < n3 || n2 >= 0 && n5 > n4);
                }
            });
        }
        return arrayList;
    }

    private static List<Predicate<Entity>> func_179663_a(Map<String, String> map, String string) {
        boolean bl;
        boolean bl2;
        ArrayList arrayList = Lists.newArrayList();
        String string2 = PlayerSelector.func_179651_b(map, "type");
        boolean bl3 = bl2 = string2 != null && string2.startsWith("!");
        if (bl2) {
            string2 = string2.substring(1);
        }
        boolean bl4 = !string.equals("e");
        boolean bl5 = bl = string.equals("r") && string2 != null;
        if (!(string2 != null && string.equals("e") || bl)) {
            if (bl4) {
                arrayList.add(new Predicate<Entity>(){

                    public boolean apply(Entity entity) {
                        return entity instanceof EntityPlayer;
                    }
                });
            }
        } else {
            final String string3 = string2;
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    return EntityList.isStringEntityName(entity, string3) ^ bl2;
                }
            });
        }
        return arrayList;
    }

    public static int func_179650_a(int n) {
        if ((n %= 360) >= 160) {
            n -= 360;
        }
        if (n < 0) {
            n += 360;
        }
        return n;
    }

    private static List<Predicate<Entity>> func_179649_c(Map<String, String> map) {
        ArrayList arrayList = Lists.newArrayList();
        final int n = PlayerSelector.parseIntWithDefault(map, "m", WorldSettings.GameType.NOT_SET.getID());
        if (n != WorldSettings.GameType.NOT_SET.getID()) {
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    if (!(entity instanceof EntityPlayerMP)) {
                        return false;
                    }
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entity;
                    return entityPlayerMP.theItemInWorldManager.getGameType().getID() == n;
                }
            });
        }
        return arrayList;
    }

    private static List<Predicate<Entity>> func_179647_f(Map<String, String> map) {
        boolean bl;
        ArrayList arrayList = Lists.newArrayList();
        String string = PlayerSelector.func_179651_b(map, "name");
        boolean bl2 = bl = string != null && string.startsWith("!");
        if (bl) {
            string = string.substring(1);
        }
        if (string != null) {
            final String string2 = string;
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    return entity.getName().equals(string2) ^ bl;
                }
            });
        }
        return arrayList;
    }

    static {
        intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
        keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
        WORLD_BINDING_ARGS = Sets.newHashSet((Object[])new String[]{"x", "y", "z", "dx", "dy", "dz", "rm", "r"});
    }

    private static List<Predicate<Entity>> func_179657_e(Map<String, String> map) {
        ArrayList arrayList = Lists.newArrayList();
        final Map<String, Integer> map2 = PlayerSelector.func_96560_a(map);
        if (map2 != null && map2.size() > 0) {
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
                    for (Map.Entry entry : map2.entrySet()) {
                        String string;
                        ScoreObjective scoreObjective;
                        String string2 = (String)entry.getKey();
                        boolean bl = false;
                        if (string2.endsWith("_min") && string2.length() > 4) {
                            bl = true;
                            string2 = string2.substring(0, string2.length() - 4);
                        }
                        if ((scoreObjective = scoreboard.getObjective(string2)) == null) {
                            return false;
                        }
                        String string3 = string = entity instanceof EntityPlayerMP ? entity.getName() : entity.getUniqueID().toString();
                        if (!scoreboard.entityHasObjective(string, scoreObjective)) {
                            return false;
                        }
                        Score score = scoreboard.getValueFromObjective(string, scoreObjective);
                        int n = score.getScorePoints();
                        if (n < (Integer)entry.getValue() && bl) {
                            return false;
                        }
                        if (n <= (Integer)entry.getValue() || bl) continue;
                        return false;
                    }
                    return true;
                }
            });
        }
        return arrayList;
    }

    public static Map<String, Integer> func_96560_a(Map<String, String> map) {
        HashMap hashMap = Maps.newHashMap();
        for (String string : map.keySet()) {
            if (!string.startsWith("score_") || string.length() <= 6) continue;
            hashMap.put(string.substring(6), MathHelper.parseIntWithDefault(map.get(string), 1));
        }
        return hashMap;
    }

    private static Map<String, String> getArgumentMap(String string) {
        HashMap hashMap = Maps.newHashMap();
        if (string == null) {
            return hashMap;
        }
        int n = 0;
        int n2 = -1;
        Matcher matcher = intListPattern.matcher(string);
        while (matcher.find()) {
            String string2 = null;
            switch (n++) {
                case 0: {
                    string2 = "x";
                    break;
                }
                case 1: {
                    string2 = "y";
                    break;
                }
                case 2: {
                    string2 = "z";
                    break;
                }
                case 3: {
                    string2 = "r";
                }
            }
            if (string2 != null && matcher.group(1).length() > 0) {
                hashMap.put(string2, matcher.group(1));
            }
            n2 = matcher.end();
        }
        if (n2 < string.length()) {
            matcher = keyValueListPattern.matcher(n2 == -1 ? string : string.substring(n2));
            while (matcher.find()) {
                hashMap.put(matcher.group(1), matcher.group(2));
            }
        }
        return hashMap;
    }

    public static IChatComponent matchEntitiesToChatComponent(ICommandSender iCommandSender, String string) {
        List<Entity> list = PlayerSelector.matchEntities(iCommandSender, string, Entity.class);
        if (list.isEmpty()) {
            return null;
        }
        ArrayList arrayList = Lists.newArrayList();
        for (Entity entity : list) {
            arrayList.add(entity.getDisplayName());
        }
        return CommandBase.join(arrayList);
    }

    private static List<World> getWorlds(ICommandSender iCommandSender, Map<String, String> map) {
        ArrayList arrayList = Lists.newArrayList();
        if (PlayerSelector.func_179665_h(map)) {
            arrayList.add(iCommandSender.getEntityWorld());
        } else {
            Collections.addAll(arrayList, MinecraftServer.getServer().worldServers);
        }
        return arrayList;
    }

    public static boolean hasArguments(String string) {
        return tokenPattern.matcher(string).matches();
    }

    public static boolean matchesMultiplePlayers(String string) {
        int n;
        Matcher matcher = tokenPattern.matcher(string);
        if (!matcher.matches()) {
            return false;
        }
        Map<String, String> map = PlayerSelector.getArgumentMap(matcher.group(2));
        String string2 = matcher.group(1);
        int n2 = n = !"a".equals(string2) && !"e".equals(string2) ? 1 : 0;
        return PlayerSelector.parseIntWithDefault(map, "c", n) != 1;
    }

    public static <T extends Entity> List<T> matchEntities(ICommandSender iCommandSender, String string, Class<? extends T> clazz) {
        Matcher matcher = tokenPattern.matcher(string);
        if (matcher.matches() && iCommandSender.canCommandSenderUseCommand(1, "@")) {
            Map<String, String> map = PlayerSelector.getArgumentMap(matcher.group(2));
            if (!PlayerSelector.isEntityTypeValid(iCommandSender, map)) {
                return Collections.emptyList();
            }
            String string2 = matcher.group(1);
            BlockPos blockPos = PlayerSelector.func_179664_b(map, iCommandSender.getPosition());
            List<World> list = PlayerSelector.getWorlds(iCommandSender, map);
            ArrayList arrayList = Lists.newArrayList();
            for (World world : list) {
                if (world == null) continue;
                ArrayList arrayList2 = Lists.newArrayList();
                arrayList2.addAll(PlayerSelector.func_179663_a(map, string2));
                arrayList2.addAll(PlayerSelector.func_179648_b(map));
                arrayList2.addAll(PlayerSelector.func_179649_c(map));
                arrayList2.addAll(PlayerSelector.func_179659_d(map));
                arrayList2.addAll(PlayerSelector.func_179657_e(map));
                arrayList2.addAll(PlayerSelector.func_179647_f(map));
                arrayList2.addAll(PlayerSelector.func_180698_a(map, blockPos));
                arrayList2.addAll(PlayerSelector.func_179662_g(map));
                arrayList.addAll(PlayerSelector.filterResults(map, clazz, arrayList2, string2, world, blockPos));
            }
            return PlayerSelector.func_179658_a(arrayList, map, iCommandSender, clazz, string2, blockPos);
        }
        return Collections.emptyList();
    }

    private static List<Predicate<Entity>> func_179662_g(Map<String, String> map) {
        int n;
        int n2;
        ArrayList arrayList = Lists.newArrayList();
        if (map.containsKey("rym") || map.containsKey("ry")) {
            n2 = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(map, "rym", 0));
            n = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(map, "ry", 359));
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    int n3 = PlayerSelector.func_179650_a((int)Math.floor(entity.rotationYaw));
                    return n2 > n ? n3 >= n2 || n3 <= n : n3 >= n2 && n3 <= n;
                }
            });
        }
        if (map.containsKey("rxm") || map.containsKey("rx")) {
            n2 = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(map, "rxm", 0));
            n = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(map, "rx", 359));
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    int n3 = PlayerSelector.func_179650_a((int)Math.floor(entity.rotationPitch));
                    return n2 > n ? n3 >= n2 || n3 <= n : n3 >= n2 && n3 <= n;
                }
            });
        }
        return arrayList;
    }

    private static int parseIntWithDefault(Map<String, String> map, String string, int n) {
        return map.containsKey(string) ? MathHelper.parseIntWithDefault(map.get(string), n) : n;
    }

    private static <T extends Entity> List<T> func_179658_a(List<T> arrayList, Map<String, String> map, ICommandSender iCommandSender, Class<? extends T> clazz, String string, final BlockPos blockPos) {
        Entity entity;
        int n = PlayerSelector.parseIntWithDefault(map, "c", !string.equals("a") && !string.equals("e") ? 1 : 0);
        if (!(string.equals("p") || string.equals("a") || string.equals("e"))) {
            if (string.equals("r")) {
                Collections.shuffle(arrayList);
            }
        } else if (blockPos != null) {
            Collections.sort(arrayList, new Comparator<Entity>(){

                @Override
                public int compare(Entity entity, Entity entity2) {
                    return ComparisonChain.start().compare(entity.getDistanceSq(blockPos), entity2.getDistanceSq(blockPos)).result();
                }
            });
        }
        if ((entity = iCommandSender.getCommandSenderEntity()) != null && clazz.isAssignableFrom(entity.getClass()) && n == 1 && arrayList.contains(entity) && !"r".equals(string)) {
            arrayList = Lists.newArrayList((Object[])new Entity[]{entity});
        }
        if (n != 0) {
            if (n < 0) {
                Collections.reverse(arrayList);
            }
            arrayList = arrayList.subList(0, Math.min(Math.abs(n), arrayList.size()));
        }
        return arrayList;
    }

    private static <T extends Entity> List<T> filterResults(Map<String, String> map, Class<? extends T> clazz, List<Predicate<Entity>> list, String string, World world, BlockPos blockPos) {
        ArrayList arrayList = Lists.newArrayList();
        String string2 = PlayerSelector.func_179651_b(map, "type");
        string2 = string2 != null && string2.startsWith("!") ? string2.substring(1) : string2;
        boolean bl = !string.equals("e");
        boolean bl2 = string.equals("r") && string2 != null;
        int n = PlayerSelector.parseIntWithDefault(map, "dx", 0);
        int n2 = PlayerSelector.parseIntWithDefault(map, "dy", 0);
        int n3 = PlayerSelector.parseIntWithDefault(map, "dz", 0);
        int n4 = PlayerSelector.parseIntWithDefault(map, "r", -1);
        Predicate predicate = Predicates.and(list);
        Predicate predicate2 = Predicates.and(EntitySelectors.selectAnything, (Predicate)predicate);
        if (blockPos != null) {
            int n5;
            boolean bl3;
            int n6 = world.playerEntities.size();
            boolean bl4 = bl3 = n6 < (n5 = world.loadedEntityList.size()) / 16;
            if (!(map.containsKey("dx") || map.containsKey("dy") || map.containsKey("dz"))) {
                if (n4 >= 0) {
                    AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos.getX() - n4, blockPos.getY() - n4, blockPos.getZ() - n4, blockPos.getX() + n4 + 1, blockPos.getY() + n4 + 1, blockPos.getZ() + n4 + 1);
                    if (bl && bl3 && !bl2) {
                        arrayList.addAll(world.getPlayers(clazz, predicate2));
                    } else {
                        arrayList.addAll(world.getEntitiesWithinAABB(clazz, axisAlignedBB, predicate2));
                    }
                } else if (string.equals("a")) {
                    arrayList.addAll(world.getPlayers(clazz, predicate));
                } else if (!(string.equals("p") || string.equals("r") && !bl2)) {
                    arrayList.addAll(world.getEntities(clazz, predicate2));
                } else {
                    arrayList.addAll(world.getPlayers(clazz, predicate2));
                }
            } else {
                final AxisAlignedBB axisAlignedBB = PlayerSelector.func_179661_a(blockPos, n, n2, n3);
                if (bl && bl3 && !bl2) {
                    Predicate<Entity> predicate3 = new Predicate<Entity>(){

                        public boolean apply(Entity entity) {
                            return entity.posX >= axisAlignedBB.minX && entity.posY >= axisAlignedBB.minY && entity.posZ >= axisAlignedBB.minZ ? entity.posX < axisAlignedBB.maxX && entity.posY < axisAlignedBB.maxY && entity.posZ < axisAlignedBB.maxZ : false;
                        }
                    };
                    arrayList.addAll(world.getPlayers(clazz, Predicates.and((Predicate)predicate2, (Predicate)predicate3)));
                } else {
                    arrayList.addAll(world.getEntitiesWithinAABB(clazz, axisAlignedBB, predicate2));
                }
            }
        } else if (string.equals("a")) {
            arrayList.addAll(world.getPlayers(clazz, predicate));
        } else if (!(string.equals("p") || string.equals("r") && !bl2)) {
            arrayList.addAll(world.getEntities(clazz, predicate2));
        } else {
            arrayList.addAll(world.getPlayers(clazz, predicate2));
        }
        return arrayList;
    }

    private static List<Predicate<Entity>> func_179659_d(Map<String, String> map) {
        boolean bl;
        ArrayList arrayList = Lists.newArrayList();
        String string = PlayerSelector.func_179651_b(map, "team");
        boolean bl2 = bl = string != null && string.startsWith("!");
        if (bl) {
            string = string.substring(1);
        }
        if (string != null) {
            final String string2 = string;
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    if (!(entity instanceof EntityLivingBase)) {
                        return false;
                    }
                    EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
                    Team team = entityLivingBase.getTeam();
                    String string = team == null ? "" : team.getRegisteredName();
                    return string.equals(string2) ^ bl;
                }
            });
        }
        return arrayList;
    }

    public static EntityPlayerMP matchOnePlayer(ICommandSender iCommandSender, String string) {
        return PlayerSelector.matchOneEntity(iCommandSender, string, EntityPlayerMP.class);
    }

    private static <T extends Entity> boolean isEntityTypeValid(ICommandSender iCommandSender, Map<String, String> map) {
        String string = PlayerSelector.func_179651_b(map, "type");
        String string2 = string = string != null && string.startsWith("!") ? string.substring(1) : string;
        if (string != null && !EntityList.isStringValidEntityName(string)) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", string);
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            iCommandSender.addChatMessage(chatComponentTranslation);
            return false;
        }
        return true;
    }

    private static AxisAlignedBB func_179661_a(BlockPos blockPos, int n, int n2, int n3) {
        boolean bl = n < 0;
        boolean bl2 = n2 < 0;
        boolean bl3 = n3 < 0;
        int n4 = blockPos.getX() + (bl ? n : 0);
        int n5 = blockPos.getY() + (bl2 ? n2 : 0);
        int n6 = blockPos.getZ() + (bl3 ? n3 : 0);
        int n7 = blockPos.getX() + (bl ? 0 : n) + 1;
        int n8 = blockPos.getY() + (bl2 ? 0 : n2) + 1;
        int n9 = blockPos.getZ() + (bl3 ? 0 : n3) + 1;
        return new AxisAlignedBB(n4, n5, n6, n7, n8, n9);
    }

    private static boolean func_179665_h(Map<String, String> map) {
        for (String string : WORLD_BINDING_ARGS) {
            if (!map.containsKey(string)) continue;
            return true;
        }
        return false;
    }

    private static List<Predicate<Entity>> func_179648_b(Map<String, String> map) {
        ArrayList arrayList = Lists.newArrayList();
        final int n = PlayerSelector.parseIntWithDefault(map, "lm", -1);
        final int n2 = PlayerSelector.parseIntWithDefault(map, "l", -1);
        if (n > -1 || n2 > -1) {
            arrayList.add(new Predicate<Entity>(){

                public boolean apply(Entity entity) {
                    if (!(entity instanceof EntityPlayerMP)) {
                        return false;
                    }
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP)entity;
                    return !(n > -1 && entityPlayerMP.experienceLevel < n || n2 > -1 && entityPlayerMP.experienceLevel > n2);
                }
            });
        }
        return arrayList;
    }
}

