package net.minecraft.command;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
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
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
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
    private static final Pattern tokenPattern = Pattern.compile((String)"^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
    private static final Pattern intListPattern = Pattern.compile((String)"\\G([-!]?[\\w-]*)(?:$|,)");
    private static final Pattern keyValueListPattern = Pattern.compile((String)"\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
    private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet((Object[])new String[]{"x", "y", "z", "dx", "dy", "dz", "rm", "r"});

    public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token) {
        return PlayerSelector.matchOneEntity(sender, token, EntityPlayerMP.class);
    }

    public static <T extends Entity> T matchOneEntity(ICommandSender sender, String token, Class<? extends T> targetClass) {
        List<? extends T> list = PlayerSelector.matchEntities(sender, token, targetClass);
        return (T)(list.size() == 1 ? (Entity)list.get(0) : null);
    }

    public static IChatComponent matchEntitiesToChatComponent(ICommandSender sender, String token) {
        List<Entity> list = PlayerSelector.matchEntities(sender, token, Entity.class);
        if (list.isEmpty()) {
            return null;
        }
        ArrayList list1 = Lists.newArrayList();
        for (Entity entity : list) {
            list1.add((Object)entity.getDisplayName());
        }
        return CommandBase.join((List)list1);
    }

    public static <T extends Entity> List<T> matchEntities(ICommandSender sender, String token, Class<? extends T> targetClass) {
        Matcher matcher = tokenPattern.matcher((CharSequence)token);
        if (matcher.matches() && sender.canCommandSenderUseCommand(1, "@")) {
            Map<String, String> map = PlayerSelector.getArgumentMap(matcher.group(2));
            if (!PlayerSelector.isEntityTypeValid(sender, map)) {
                return Collections.emptyList();
            }
            String s = matcher.group(1);
            BlockPos blockpos = PlayerSelector.func_179664_b(map, sender.getPosition());
            List<World> list = PlayerSelector.getWorlds(sender, map);
            ArrayList list1 = Lists.newArrayList();
            for (World world : list) {
                if (world == null) continue;
                ArrayList list2 = Lists.newArrayList();
                list2.addAll(PlayerSelector.func_179663_a(map, s));
                list2.addAll(PlayerSelector.getXpLevelPredicates(map));
                list2.addAll(PlayerSelector.getGamemodePredicates(map));
                list2.addAll(PlayerSelector.getTeamPredicates(map));
                list2.addAll(PlayerSelector.getScorePredicates(map));
                list2.addAll(PlayerSelector.getNamePredicates(map));
                list2.addAll(PlayerSelector.func_180698_a(map, blockpos));
                list2.addAll(PlayerSelector.getRotationsPredicates(map));
                list1.addAll(PlayerSelector.filterResults(map, targetClass, (List<Predicate<Entity>>)list2, s, world, blockpos));
            }
            return PlayerSelector.func_179658_a(list1, map, sender, targetClass, s, blockpos);
        }
        return Collections.emptyList();
    }

    private static List<World> getWorlds(ICommandSender sender, Map<String, String> argumentMap) {
        ArrayList list = Lists.newArrayList();
        if (PlayerSelector.func_179665_h(argumentMap)) {
            list.add((Object)sender.getEntityWorld());
        } else {
            Collections.addAll((Collection)list, (Object[])MinecraftServer.getServer().worldServers);
        }
        return list;
    }

    private static <T extends Entity> boolean isEntityTypeValid(ICommandSender commandSender, Map<String, String> params) {
        String s = PlayerSelector.func_179651_b(params, "type");
        String string = s = s != null && s.startsWith("!") ? s.substring(1) : s;
        if (s != null && !EntityList.isStringValidEntityName((String)s)) {
            ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[]{s});
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
            commandSender.addChatMessage((IChatComponent)chatcomponenttranslation);
            return false;
        }
        return true;
    }

    private static List<Predicate<Entity>> func_179663_a(Map<String, String> p_179663_0_, String p_179663_1_) {
        boolean flag2;
        boolean flag;
        ArrayList list = Lists.newArrayList();
        String s = PlayerSelector.func_179651_b(p_179663_0_, "type");
        boolean bl = flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        boolean flag1 = !p_179663_1_.equals((Object)"e");
        boolean bl2 = flag2 = p_179663_1_.equals((Object)"r") && s != null;
        if (!(s != null && p_179663_1_.equals((Object)"e") || flag2)) {
            if (flag1) {
                list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
            }
        } else {
            String s_f = s;
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static List<Predicate<Entity>> getXpLevelPredicates(Map<String, String> p_179648_0_) {
        ArrayList list = Lists.newArrayList();
        int i = PlayerSelector.parseIntWithDefault(p_179648_0_, "lm", -1);
        int j = PlayerSelector.parseIntWithDefault(p_179648_0_, "l", -1);
        if (i > -1 || j > -1) {
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static List<Predicate<Entity>> getGamemodePredicates(Map<String, String> p_179649_0_) {
        ArrayList list = Lists.newArrayList();
        int i = PlayerSelector.parseIntWithDefault(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());
        if (i != WorldSettings.GameType.NOT_SET.getID()) {
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static List<Predicate<Entity>> getTeamPredicates(Map<String, String> p_179659_0_) {
        boolean flag;
        ArrayList list = Lists.newArrayList();
        String s = PlayerSelector.func_179651_b(p_179659_0_, "team");
        boolean bl = flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            String s_f = s;
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static List<Predicate<Entity>> getScorePredicates(Map<String, String> p_179657_0_) {
        ArrayList list = Lists.newArrayList();
        Map<String, Integer> map = PlayerSelector.func_96560_a(p_179657_0_);
        if (map != null && map.size() > 0) {
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static List<Predicate<Entity>> getNamePredicates(Map<String, String> p_179647_0_) {
        boolean flag;
        ArrayList list = Lists.newArrayList();
        String s = PlayerSelector.func_179651_b(p_179647_0_, "name");
        boolean bl = flag = s != null && s.startsWith("!");
        if (flag) {
            s = s.substring(1);
        }
        if (s != null) {
            String s_f = s;
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static List<Predicate<Entity>> func_180698_a(Map<String, String> p_180698_0_, BlockPos p_180698_1_) {
        ArrayList list = Lists.newArrayList();
        int i = PlayerSelector.parseIntWithDefault(p_180698_0_, "rm", -1);
        int j = PlayerSelector.parseIntWithDefault(p_180698_0_, "r", -1);
        if (p_180698_1_ != null && (i >= 0 || j >= 0)) {
            int k = i * i;
            int l = j * j;
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static List<Predicate<Entity>> getRotationsPredicates(Map<String, String> p_179662_0_) {
        ArrayList list = Lists.newArrayList();
        if (p_179662_0_.containsKey((Object)"rym") || p_179662_0_.containsKey((Object)"ry")) {
            int i = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "rym", 0));
            int j = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "ry", 359));
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        if (p_179662_0_.containsKey((Object)"rxm") || p_179662_0_.containsKey((Object)"rx")) {
            int k = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "rxm", 0));
            int l = PlayerSelector.func_179650_a(PlayerSelector.parseIntWithDefault(p_179662_0_, "rx", 359));
            list.add((Object)new /* Unavailable Anonymous Inner Class!! */);
        }
        return list;
    }

    private static <T extends Entity> List<T> filterResults(Map<String, String> params, Class<? extends T> entityClass, List<Predicate<Entity>> inputList, String type, World worldIn, BlockPos position) {
        ArrayList list = Lists.newArrayList();
        String s = PlayerSelector.func_179651_b(params, "type");
        s = s != null && s.startsWith("!") ? s.substring(1) : s;
        boolean flag = !type.equals((Object)"e");
        boolean flag1 = type.equals((Object)"r") && s != null;
        int i = PlayerSelector.parseIntWithDefault(params, "dx", 0);
        int j = PlayerSelector.parseIntWithDefault(params, "dy", 0);
        int k = PlayerSelector.parseIntWithDefault(params, "dz", 0);
        int l = PlayerSelector.parseIntWithDefault(params, "r", -1);
        Predicate predicate = Predicates.and(inputList);
        Predicate predicate1 = Predicates.and((Predicate)EntitySelectors.selectAnything, (Predicate)predicate);
        if (position != null) {
            int j1;
            boolean flag2;
            int i1 = worldIn.playerEntities.size();
            boolean bl = flag2 = i1 < (j1 = worldIn.loadedEntityList.size()) / 16;
            if (!(params.containsKey((Object)"dx") || params.containsKey((Object)"dy") || params.containsKey((Object)"dz"))) {
                if (l >= 0) {
                    AxisAlignedBB axisalignedbb1 = new AxisAlignedBB((double)(position.getX() - l), (double)(position.getY() - l), (double)(position.getZ() - l), (double)(position.getX() + l + 1), (double)(position.getY() + l + 1), (double)(position.getZ() + l + 1));
                    if (flag && flag2 && !flag1) {
                        list.addAll((Collection)worldIn.getPlayers(entityClass, predicate1));
                    } else {
                        list.addAll((Collection)worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb1, predicate1));
                    }
                } else if (type.equals((Object)"a")) {
                    list.addAll((Collection)worldIn.getPlayers(entityClass, predicate));
                } else if (!(type.equals((Object)"p") || type.equals((Object)"r") && !flag1)) {
                    list.addAll((Collection)worldIn.getEntities(entityClass, predicate1));
                } else {
                    list.addAll((Collection)worldIn.getPlayers(entityClass, predicate1));
                }
            } else {
                AxisAlignedBB axisalignedbb = PlayerSelector.func_179661_a(position, i, j, k);
                if (flag && flag2 && !flag1) {
                    11 predicate2 = new /* Unavailable Anonymous Inner Class!! */;
                    list.addAll((Collection)worldIn.getPlayers(entityClass, Predicates.and((Predicate)predicate1, (Predicate)predicate2)));
                } else {
                    list.addAll((Collection)worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb, predicate1));
                }
            }
        } else if (type.equals((Object)"a")) {
            list.addAll((Collection)worldIn.getPlayers(entityClass, predicate));
        } else if (!(type.equals((Object)"p") || type.equals((Object)"r") && !flag1)) {
            list.addAll((Collection)worldIn.getEntities(entityClass, predicate1));
        } else {
            list.addAll((Collection)worldIn.getPlayers(entityClass, predicate1));
        }
        return list;
    }

    private static <T extends Entity> List<T> func_179658_a(List<T> p_179658_0_, Map<String, String> p_179658_1_, ICommandSender p_179658_2_, Class<? extends T> p_179658_3_, String p_179658_4_, BlockPos p_179658_5_) {
        Entity entity;
        int i = PlayerSelector.parseIntWithDefault(p_179658_1_, "c", !p_179658_4_.equals((Object)"a") && !p_179658_4_.equals((Object)"e") ? 1 : 0);
        if (!(p_179658_4_.equals((Object)"p") || p_179658_4_.equals((Object)"a") || p_179658_4_.equals((Object)"e"))) {
            if (p_179658_4_.equals((Object)"r")) {
                Collections.shuffle(p_179658_0_);
            }
        } else if (p_179658_5_ != null) {
            Collections.sort(p_179658_0_, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        }
        if ((entity = p_179658_2_.getCommandSenderEntity()) != null && p_179658_3_.isAssignableFrom(entity.getClass()) && i == 1 && p_179658_0_.contains((Object)entity) && !"r".equals((Object)p_179658_4_)) {
            p_179658_0_ = Lists.newArrayList((Object[])new Entity[]{entity});
        }
        if (i != 0) {
            if (i < 0) {
                Collections.reverse(p_179658_0_);
            }
            p_179658_0_ = p_179658_0_.subList(0, Math.min((int)Math.abs((int)i), (int)p_179658_0_.size()));
        }
        return p_179658_0_;
    }

    private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_) {
        boolean flag = p_179661_1_ < 0;
        boolean flag1 = p_179661_2_ < 0;
        boolean flag2 = p_179661_3_ < 0;
        int i = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
        int j = p_179661_0_.getY() + (flag1 ? p_179661_2_ : 0);
        int k = p_179661_0_.getZ() + (flag2 ? p_179661_3_ : 0);
        int l = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
        int i1 = p_179661_0_.getY() + (flag1 ? 0 : p_179661_2_) + 1;
        int j1 = p_179661_0_.getZ() + (flag2 ? 0 : p_179661_3_) + 1;
        return new AxisAlignedBB((double)i, (double)j, (double)k, (double)l, (double)i1, (double)j1);
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
        for (String s : WORLD_BINDING_ARGS) {
            if (!p_179665_0_.containsKey((Object)s)) continue;
            return true;
        }
        return false;
    }

    private static int parseIntWithDefault(Map<String, String> p_179653_0_, String p_179653_1_, int p_179653_2_) {
        return p_179653_0_.containsKey((Object)p_179653_1_) ? MathHelper.parseIntWithDefault((String)((String)p_179653_0_.get((Object)p_179653_1_)), (int)p_179653_2_) : p_179653_2_;
    }

    private static String func_179651_b(Map<String, String> p_179651_0_, String p_179651_1_) {
        return (String)p_179651_0_.get((Object)p_179651_1_);
    }

    public static Map<String, Integer> func_96560_a(Map<String, String> p_96560_0_) {
        HashMap map = Maps.newHashMap();
        for (String s : p_96560_0_.keySet()) {
            if (!s.startsWith("score_") || s.length() <= "score_".length()) continue;
            map.put((Object)s.substring("score_".length()), (Object)MathHelper.parseIntWithDefault((String)((String)p_96560_0_.get((Object)s)), (int)1));
        }
        return map;
    }

    public static boolean matchesMultiplePlayers(String p_82377_0_) {
        Matcher matcher = tokenPattern.matcher((CharSequence)p_82377_0_);
        if (!matcher.matches()) {
            return false;
        }
        Map<String, String> map = PlayerSelector.getArgumentMap(matcher.group(2));
        String s = matcher.group(1);
        int i = !"a".equals((Object)s) && !"e".equals((Object)s) ? 1 : 0;
        return PlayerSelector.parseIntWithDefault(map, "c", i) != 1;
    }

    public static boolean hasArguments(String p_82378_0_) {
        return tokenPattern.matcher((CharSequence)p_82378_0_).matches();
    }

    private static Map<String, String> getArgumentMap(String argumentString) {
        HashMap map = Maps.newHashMap();
        if (argumentString == null) {
            return map;
        }
        int i = 0;
        int j = -1;
        Matcher matcher = intListPattern.matcher((CharSequence)argumentString);
        while (matcher.find()) {
            String s = null;
            switch (i++) {
                case 0: {
                    s = "x";
                    break;
                }
                case 1: {
                    s = "y";
                    break;
                }
                case 2: {
                    s = "z";
                    break;
                }
                case 3: {
                    s = "r";
                }
            }
            if (s != null && matcher.group(1).length() > 0) {
                map.put((Object)s, (Object)matcher.group(1));
            }
            j = matcher.end();
        }
        if (j < argumentString.length()) {
            Matcher matcher1 = keyValueListPattern.matcher((CharSequence)(j == -1 ? argumentString : argumentString.substring(j)));
            while (matcher1.find()) {
                map.put((Object)matcher1.group(1), (Object)matcher1.group(2));
            }
        }
        return map;
    }
}
