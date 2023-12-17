// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import com.google.common.collect.Iterables;
import com.google.common.base.Functions;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.block.state.BlockStateContainer;
import com.google.common.collect.Maps;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import java.util.UUID;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.primitives.Doubles;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.MinecraftServer;
import java.util.Collections;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import org.apache.commons.lang3.exception.ExceptionUtils;
import com.google.gson.JsonParseException;
import com.google.common.base.Splitter;

public abstract class CommandBase implements ICommand
{
    private static ICommandListener commandListener;
    private static final Splitter COMMA_SPLITTER;
    private static final Splitter EQUAL_SPLITTER;
    
    protected static SyntaxErrorException toSyntaxException(final JsonParseException e) {
        final Throwable throwable = ExceptionUtils.getRootCause((Throwable)e);
        String s = "";
        if (throwable != null) {
            s = throwable.getMessage();
            if (s.contains("setLenient")) {
                s = s.substring(s.indexOf("to accept ") + 10);
            }
        }
        return new SyntaxErrorException("commands.tellraw.jsonException", new Object[] { s });
    }
    
    public static NBTTagCompound entityToNBT(final Entity theEntity) {
        final NBTTagCompound nbttagcompound = theEntity.writeToNBT(new NBTTagCompound());
        if (theEntity instanceof EntityPlayer) {
            final ItemStack itemstack = ((EntityPlayer)theEntity).inventory.getCurrentItem();
            if (!itemstack.isEmpty()) {
                nbttagcompound.setTag("SelectedItem", itemstack.writeToNBT(new NBTTagCompound()));
            }
        }
        return nbttagcompound;
    }
    
    public int getRequiredPermissionLevel() {
        return 4;
    }
    
    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean checkPermission(final MinecraftServer server, final ICommandSender sender) {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        return Collections.emptyList();
    }
    
    public static int parseInt(final String input) throws NumberInvalidException {
        try {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException var2) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
        }
    }
    
    public static int parseInt(final String input, final int min) throws NumberInvalidException {
        return parseInt(input, min, Integer.MAX_VALUE);
    }
    
    public static int parseInt(final String input, final int min, final int max) throws NumberInvalidException {
        final int i = parseInt(input);
        if (i < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { i, min });
        }
        if (i > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { i, max });
        }
        return i;
    }
    
    public static long parseLong(final String input) throws NumberInvalidException {
        try {
            return Long.parseLong(input);
        }
        catch (NumberFormatException var2) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
        }
    }
    
    public static long parseLong(final String input, final long min, final long max) throws NumberInvalidException {
        final long i = parseLong(input);
        if (i < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { i, min });
        }
        if (i > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { i, max });
        }
        return i;
    }
    
    public static BlockPos parseBlockPos(final ICommandSender sender, final String[] args, final int startIndex, final boolean centerBlock) throws NumberInvalidException {
        final BlockPos blockpos = sender.getPosition();
        return new BlockPos(parseDouble(blockpos.getX(), args[startIndex], -30000000, 30000000, centerBlock), parseDouble(blockpos.getY(), args[startIndex + 1], 0, 256, false), parseDouble(blockpos.getZ(), args[startIndex + 2], -30000000, 30000000, centerBlock));
    }
    
    public static double parseDouble(final String input) throws NumberInvalidException {
        try {
            final double d0 = Double.parseDouble(input);
            if (!Doubles.isFinite(d0)) {
                throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
            }
            return d0;
        }
        catch (NumberFormatException var3) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { input });
        }
    }
    
    public static double parseDouble(final String input, final double min) throws NumberInvalidException {
        return parseDouble(input, min, Double.MAX_VALUE);
    }
    
    public static double parseDouble(final String input, final double min, final double max) throws NumberInvalidException {
        final double d0 = parseDouble(input);
        if (d0 < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { String.format("%.2f", d0), String.format("%.2f", min) });
        }
        if (d0 > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { String.format("%.2f", d0), String.format("%.2f", max) });
        }
        return d0;
    }
    
    public static boolean parseBoolean(final String input) throws CommandException {
        if ("true".equals(input) || "1".equals(input)) {
            return true;
        }
        if (!"false".equals(input) && !"0".equals(input)) {
            throw new CommandException("commands.generic.boolean.invalid", new Object[] { input });
        }
        return false;
    }
    
    public static EntityPlayerMP getCommandSenderAsPlayer(final ICommandSender sender) throws PlayerNotFoundException {
        if (sender instanceof EntityPlayerMP) {
            return (EntityPlayerMP)sender;
        }
        throw new PlayerNotFoundException("commands.generic.player.unspecified");
    }
    
    public static List<EntityPlayerMP> getPlayers(final MinecraftServer p_193513_0_, final ICommandSender p_193513_1_, final String p_193513_2_) throws CommandException {
        final List<EntityPlayerMP> list = EntitySelector.getPlayers(p_193513_1_, p_193513_2_);
        return list.isEmpty() ? Lists.newArrayList((Object[])new EntityPlayerMP[] { getPlayer(p_193513_0_, null, p_193513_2_) }) : list;
    }
    
    public static EntityPlayerMP getPlayer(final MinecraftServer server, final ICommandSender sender, final String target) throws PlayerNotFoundException, CommandException {
        return getPlayer(server, EntitySelector.matchOnePlayer(sender, target), target);
    }
    
    private static EntityPlayerMP getPlayer(final MinecraftServer p_193512_0_, @Nullable EntityPlayerMP p_193512_1_, final String p_193512_2_) throws CommandException {
        if (p_193512_1_ == null) {
            try {
                p_193512_1_ = p_193512_0_.getPlayerList().getPlayerByUUID(UUID.fromString(p_193512_2_));
            }
            catch (IllegalArgumentException ex) {}
        }
        if (p_193512_1_ == null) {
            p_193512_1_ = p_193512_0_.getPlayerList().getPlayerByUsername(p_193512_2_);
        }
        if (p_193512_1_ == null) {
            throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { p_193512_2_ });
        }
        return p_193512_1_;
    }
    
    public static Entity getEntity(final MinecraftServer server, final ICommandSender sender, final String target) throws EntityNotFoundException, CommandException {
        return getEntity(server, sender, target, (Class<? extends Entity>)Entity.class);
    }
    
    public static <T extends Entity> T getEntity(final MinecraftServer server, final ICommandSender sender, final String target, final Class<? extends T> targetClass) throws EntityNotFoundException, CommandException {
        Entity entity = EntitySelector.matchOneEntity(sender, target, (Class<? extends Entity>)targetClass);
        if (entity == null) {
            entity = server.getPlayerList().getPlayerByUsername(target);
        }
        if (entity == null) {
            try {
                final UUID uuid = UUID.fromString(target);
                entity = server.getEntityFromUuid(uuid);
                if (entity == null) {
                    entity = server.getPlayerList().getPlayerByUUID(uuid);
                }
            }
            catch (IllegalArgumentException var6) {
                if (target.split("-").length == 5) {
                    throw new EntityNotFoundException("commands.generic.entity.invalidUuid", new Object[] { target });
                }
            }
        }
        if (entity != null && targetClass.isAssignableFrom(entity.getClass())) {
            return (T)entity;
        }
        throw new EntityNotFoundException(target);
    }
    
    public static List<Entity> getEntityList(final MinecraftServer server, final ICommandSender sender, final String target) throws EntityNotFoundException, CommandException {
        return EntitySelector.isSelector(target) ? EntitySelector.matchEntities(sender, target, (Class<? extends Entity>)Entity.class) : Lists.newArrayList((Object[])new Entity[] { getEntity(server, sender, target) });
    }
    
    public static String getPlayerName(final MinecraftServer server, final ICommandSender sender, final String target) throws PlayerNotFoundException, CommandException {
        try {
            return getPlayer(server, sender, target).getName();
        }
        catch (CommandException commandexception) {
            if (EntitySelector.isSelector(target)) {
                throw commandexception;
            }
            return target;
        }
    }
    
    public static String getEntityName(final MinecraftServer server, final ICommandSender sender, final String target) throws EntityNotFoundException, CommandException {
        try {
            return getPlayer(server, sender, target).getName();
        }
        catch (PlayerNotFoundException var6) {
            try {
                return getEntity(server, sender, target).getCachedUniqueIdString();
            }
            catch (EntityNotFoundException entitynotfoundexception) {
                if (EntitySelector.isSelector(target)) {
                    throw entitynotfoundexception;
                }
                return target;
            }
        }
    }
    
    public static ITextComponent getChatComponentFromNthArg(final ICommandSender sender, final String[] args, final int index) throws CommandException, PlayerNotFoundException {
        return getChatComponentFromNthArg(sender, args, index, false);
    }
    
    public static ITextComponent getChatComponentFromNthArg(final ICommandSender sender, final String[] args, final int index, final boolean p_147176_3_) throws PlayerNotFoundException, CommandException {
        final ITextComponent itextcomponent = new TextComponentString("");
        for (int i = index; i < args.length; ++i) {
            if (i > index) {
                itextcomponent.appendText(" ");
            }
            ITextComponent itextcomponent2 = new TextComponentString(args[i]);
            if (p_147176_3_) {
                final ITextComponent itextcomponent3 = EntitySelector.matchEntitiesToTextComponent(sender, args[i]);
                if (itextcomponent3 == null) {
                    if (EntitySelector.isSelector(args[i])) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] { args[i] });
                    }
                }
                else {
                    itextcomponent2 = itextcomponent3;
                }
            }
            itextcomponent.appendSibling(itextcomponent2);
        }
        return itextcomponent;
    }
    
    public static String buildString(final String[] args, final int startPos) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (int i = startPos; i < args.length; ++i) {
            if (i > startPos) {
                stringbuilder.append(" ");
            }
            final String s = args[i];
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
    public static CoordinateArg parseCoordinate(final double base, final String selectorArg, final boolean centerBlock) throws NumberInvalidException {
        return parseCoordinate(base, selectorArg, -30000000, 30000000, centerBlock);
    }
    
    public static CoordinateArg parseCoordinate(final double base, String selectorArg, final int min, final int max, final boolean centerBlock) throws NumberInvalidException {
        final boolean flag = selectorArg.startsWith("~");
        if (flag && Double.isNaN(base)) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { base });
        }
        double d0 = 0.0;
        if (!flag || selectorArg.length() > 1) {
            final boolean flag2 = selectorArg.contains(".");
            if (flag) {
                selectorArg = selectorArg.substring(1);
            }
            d0 += parseDouble(selectorArg);
            if (!flag2 && !flag && centerBlock) {
                d0 += 0.5;
            }
        }
        final double d2 = d0 + (flag ? base : 0.0);
        if (min != 0 || max != 0) {
            if (d2 < min) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { String.format("%.2f", d2), min });
            }
            if (d2 > max) {
                throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { String.format("%.2f", d2), max });
            }
        }
        return new CoordinateArg(d2, d0, flag);
    }
    
    public static double parseDouble(final double base, final String input, final boolean centerBlock) throws NumberInvalidException {
        return parseDouble(base, input, -30000000, 30000000, centerBlock);
    }
    
    public static double parseDouble(final double base, String input, final int min, final int max, final boolean centerBlock) throws NumberInvalidException {
        final boolean flag = input.startsWith("~");
        if (flag && Double.isNaN(base)) {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] { base });
        }
        double d0 = flag ? base : 0.0;
        if (!flag || input.length() > 1) {
            final boolean flag2 = input.contains(".");
            if (flag) {
                input = input.substring(1);
            }
            d0 += parseDouble(input);
            if (!flag2 && !flag && centerBlock) {
                d0 += 0.5;
            }
        }
        if (min != 0 || max != 0) {
            if (d0 < min) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { String.format("%.2f", d0), min });
            }
            if (d0 > max) {
                throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { String.format("%.2f", d0), max });
            }
        }
        return d0;
    }
    
    public static Item getItemByText(final ICommandSender sender, final String id) throws NumberInvalidException {
        final ResourceLocation resourcelocation = new ResourceLocation(id);
        final Item item = Item.REGISTRY.getObject(resourcelocation);
        if (item == null) {
            throw new NumberInvalidException("commands.give.item.notFound", new Object[] { resourcelocation });
        }
        return item;
    }
    
    public static Block getBlockByText(final ICommandSender sender, final String id) throws NumberInvalidException {
        final ResourceLocation resourcelocation = new ResourceLocation(id);
        if (!Block.REGISTRY.containsKey(resourcelocation)) {
            throw new NumberInvalidException("commands.give.block.notFound", new Object[] { resourcelocation });
        }
        return Block.REGISTRY.getObject(resourcelocation);
    }
    
    public static IBlockState convertArgToBlockState(final Block p_190794_0_, final String p_190794_1_) throws NumberInvalidException, InvalidBlockStateException {
        try {
            final int i = Integer.parseInt(p_190794_1_);
            if (i < 0) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] { i, 0 });
            }
            if (i > 15) {
                throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] { i, 15 });
            }
            return p_190794_0_.getStateFromMeta(Integer.parseInt(p_190794_1_));
        }
        catch (RuntimeException var7) {
            try {
                final Map<IProperty<?>, Comparable<?>> map = getBlockStatePropertyValueMap(p_190794_0_, p_190794_1_);
                IBlockState iblockstate = p_190794_0_.getDefaultState();
                for (final Map.Entry<IProperty<?>, Comparable<?>> entry : map.entrySet()) {
                    iblockstate = getBlockState(iblockstate, entry.getKey(), entry.getValue());
                }
                return iblockstate;
            }
            catch (RuntimeException var8) {
                throw new InvalidBlockStateException("commands.generic.blockstate.invalid", new Object[] { p_190794_1_, Block.REGISTRY.getNameForObject(p_190794_0_) });
            }
        }
    }
    
    private static <T extends Comparable<T>> IBlockState getBlockState(final IBlockState p_190793_0_, final IProperty<T> p_190793_1_, final Comparable<?> p_190793_2_) {
        return p_190793_0_.withProperty(p_190793_1_, p_190793_2_);
    }
    
    public static Predicate<IBlockState> convertArgToBlockStatePredicate(final Block p_190791_0_, final String p_190791_1_) throws InvalidBlockStateException {
        if (!"*".equals(p_190791_1_) && !"-1".equals(p_190791_1_)) {
            try {
                final int i = Integer.parseInt(p_190791_1_);
                return (Predicate<IBlockState>)new Predicate<IBlockState>() {
                    public boolean apply(@Nullable final IBlockState p_apply_1_) {
                        return i == p_apply_1_.getBlock().getMetaFromState(p_apply_1_);
                    }
                };
            }
            catch (RuntimeException var3) {
                final Map<IProperty<?>, Comparable<?>> map = getBlockStatePropertyValueMap(p_190791_0_, p_190791_1_);
                return (Predicate<IBlockState>)new Predicate<IBlockState>() {
                    public boolean apply(@Nullable final IBlockState p_apply_1_) {
                        if (p_apply_1_ != null && p_190791_0_ == p_apply_1_.getBlock()) {
                            for (final Map.Entry<IProperty<?>, Comparable<?>> entry : map.entrySet()) {
                                if (!p_apply_1_.getValue(entry.getKey()).equals(entry.getValue())) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                };
            }
        }
        return (Predicate<IBlockState>)Predicates.alwaysTrue();
    }
    
    private static Map<IProperty<?>, Comparable<?>> getBlockStatePropertyValueMap(final Block p_190795_0_, final String p_190795_1_) throws InvalidBlockStateException {
        final Map<IProperty<?>, Comparable<?>> map = (Map<IProperty<?>, Comparable<?>>)Maps.newHashMap();
        if ("default".equals(p_190795_1_)) {
            return (Map<IProperty<?>, Comparable<?>>)p_190795_0_.getDefaultState().getProperties();
        }
        final BlockStateContainer blockstatecontainer = p_190795_0_.getBlockState();
        for (final String s : CommandBase.COMMA_SPLITTER.split((CharSequence)p_190795_1_)) {
            final Iterator<String> iterator2 = CommandBase.EQUAL_SPLITTER.split((CharSequence)s).iterator();
            if (iterator2.hasNext()) {
                final IProperty<?> iproperty = blockstatecontainer.getProperty(iterator2.next());
                if (iproperty != null) {
                    if (iterator2.hasNext()) {
                        final Comparable<?> comparable = getValueHelper(iproperty, iterator2.next());
                        if (comparable != null) {
                            map.put(iproperty, comparable);
                            continue;
                        }
                    }
                }
            }
            throw new InvalidBlockStateException("commands.generic.blockstate.invalid", new Object[] { p_190795_1_, Block.REGISTRY.getNameForObject(p_190795_0_) });
        }
        return map;
    }
    
    @Nullable
    private static <T extends Comparable<T>> T getValueHelper(final IProperty<T> p_190792_0_, final String p_190792_1_) {
        return (T)p_190792_0_.parseValue(p_190792_1_).orNull();
    }
    
    public static String joinNiceString(final Object[] elements) {
        final StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < elements.length; ++i) {
            final String s = elements[i].toString();
            if (i > 0) {
                if (i == elements.length - 1) {
                    stringbuilder.append(" and ");
                }
                else {
                    stringbuilder.append(", ");
                }
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }
    
    public static ITextComponent join(final List<ITextComponent> components) {
        final ITextComponent itextcomponent = new TextComponentString("");
        for (int i = 0; i < components.size(); ++i) {
            if (i > 0) {
                if (i == components.size() - 1) {
                    itextcomponent.appendText(" and ");
                }
                else if (i > 0) {
                    itextcomponent.appendText(", ");
                }
            }
            itextcomponent.appendSibling(components.get(i));
        }
        return itextcomponent;
    }
    
    public static String joinNiceStringFromCollection(final Collection<String> strings) {
        return joinNiceString(strings.toArray(new String[strings.size()]));
    }
    
    public static List<String> getTabCompletionCoordinate(final String[] inputArgs, final int index, @Nullable final BlockPos pos) {
        if (pos == null) {
            return (List<String>)Lists.newArrayList((Object[])new String[] { "~" });
        }
        final int i = inputArgs.length - 1;
        String s;
        if (i == index) {
            s = Integer.toString(pos.getX());
        }
        else if (i == index + 1) {
            s = Integer.toString(pos.getY());
        }
        else {
            if (i != index + 2) {
                return Collections.emptyList();
            }
            s = Integer.toString(pos.getZ());
        }
        return (List<String>)Lists.newArrayList((Object[])new String[] { s });
    }
    
    public static List<String> getTabCompletionCoordinateXZ(final String[] inputArgs, final int index, @Nullable final BlockPos lookedPos) {
        if (lookedPos == null) {
            return (List<String>)Lists.newArrayList((Object[])new String[] { "~" });
        }
        final int i = inputArgs.length - 1;
        String s;
        if (i == index) {
            s = Integer.toString(lookedPos.getX());
        }
        else {
            if (i != index + 1) {
                return Collections.emptyList();
            }
            s = Integer.toString(lookedPos.getZ());
        }
        return (List<String>)Lists.newArrayList((Object[])new String[] { s });
    }
    
    public static boolean doesStringStartWith(final String original, final String region) {
        return region.regionMatches(true, 0, original, 0, original.length());
    }
    
    public static List<String> getListOfStringsMatchingLastWord(final String[] args, final String... possibilities) {
        return getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
    }
    
    public static List<String> getListOfStringsMatchingLastWord(final String[] inputArgs, final Collection<?> possibleCompletions) {
        final String s = inputArgs[inputArgs.length - 1];
        final List<String> list = (List<String>)Lists.newArrayList();
        if (!possibleCompletions.isEmpty()) {
            for (final String s2 : Iterables.transform((Iterable)possibleCompletions, Functions.toStringFunction())) {
                if (doesStringStartWith(s, s2)) {
                    list.add(s2);
                }
            }
            if (list.isEmpty()) {
                for (final Object object : possibleCompletions) {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation)object).getPath())) {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return false;
    }
    
    public static void notifyCommandListener(final ICommandSender sender, final ICommand command, final String translationKey, final Object... translationArgs) {
        notifyCommandListener(sender, command, 0, translationKey, translationArgs);
    }
    
    public static void notifyCommandListener(final ICommandSender sender, final ICommand command, final int flags, final String translationKey, final Object... translationArgs) {
        if (CommandBase.commandListener != null) {
            CommandBase.commandListener.notifyListener(sender, command, flags, translationKey, translationArgs);
        }
    }
    
    public static void setCommandListener(final ICommandListener listener) {
        CommandBase.commandListener = listener;
    }
    
    @Override
    public int compareTo(final ICommand p_compareTo_1_) {
        return this.getName().compareTo(p_compareTo_1_.getName());
    }
    
    static {
        COMMA_SPLITTER = Splitter.on(',');
        EQUAL_SPLITTER = Splitter.on('=').limit(2);
    }
    
    public static class CoordinateArg
    {
        private final double result;
        private final double amount;
        private final boolean isRelative;
        
        protected CoordinateArg(final double resultIn, final double amountIn, final boolean relative) {
            this.result = resultIn;
            this.amount = amountIn;
            this.isRelative = relative;
        }
        
        public double getResult() {
            return this.result;
        }
        
        public double getAmount() {
            return this.amount;
        }
        
        public boolean isRelative() {
            return this.isRelative;
        }
    }
}
