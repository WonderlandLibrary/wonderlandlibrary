/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.command;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Doubles;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandListener;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.InvalidBlockStateException;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.exception.ExceptionUtils;

public abstract class CommandBase
implements ICommand {
    private static ICommandListener commandListener;
    private static final Splitter field_190796_b;
    private static final Splitter field_190797_c;

    protected static SyntaxErrorException toSyntaxException(JsonParseException e) {
        Throwable throwable = ExceptionUtils.getRootCause(e);
        String s = "";
        if (throwable != null && (s = throwable.getMessage()).contains("setLenient")) {
            s = s.substring(s.indexOf("to accept ") + 10);
        }
        return new SyntaxErrorException("commands.tellraw.jsonException", s);
    }

    public static NBTTagCompound entityToNBT(Entity theEntity) {
        ItemStack itemstack;
        NBTTagCompound nbttagcompound = theEntity.writeToNBT(new NBTTagCompound());
        if (theEntity instanceof EntityPlayer && !(itemstack = ((EntityPlayer)theEntity).inventory.getCurrentItem()).isEmpty()) {
            nbttagcompound.setTag("SelectedItem", itemstack.writeToNBT(new NBTTagCompound()));
        }
        return nbttagcompound;
    }

    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }

    public static int parseInt(String input) throws NumberInvalidException {
        try {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException var2) {
            throw new NumberInvalidException("commands.generic.num.invalid", input);
        }
    }

    public static int parseInt(String input, int min) throws NumberInvalidException {
        return CommandBase.parseInt(input, min, Integer.MAX_VALUE);
    }

    public static int parseInt(String input, int min, int max) throws NumberInvalidException {
        int i = CommandBase.parseInt(input);
        if (i < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", i, min);
        }
        if (i > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", i, max);
        }
        return i;
    }

    public static long parseLong(String input) throws NumberInvalidException {
        try {
            return Long.parseLong(input);
        }
        catch (NumberFormatException var2) {
            throw new NumberInvalidException("commands.generic.num.invalid", input);
        }
    }

    public static long parseLong(String input, long min, long max) throws NumberInvalidException {
        long i = CommandBase.parseLong(input);
        if (i < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", i, min);
        }
        if (i > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", i, max);
        }
        return i;
    }

    public static BlockPos parseBlockPos(ICommandSender sender, String[] args, int startIndex, boolean centerBlock) throws NumberInvalidException {
        BlockPos blockpos = sender.getPosition();
        return new BlockPos(CommandBase.parseDouble(blockpos.getX(), args[startIndex], -30000000, 30000000, centerBlock), CommandBase.parseDouble(blockpos.getY(), args[startIndex + 1], 0, 256, false), CommandBase.parseDouble(blockpos.getZ(), args[startIndex + 2], -30000000, 30000000, centerBlock));
    }

    public static double parseDouble(String input) throws NumberInvalidException {
        try {
            double d0 = Double.parseDouble(input);
            if (!Doubles.isFinite(d0)) {
                throw new NumberInvalidException("commands.generic.num.invalid", input);
            }
            return d0;
        }
        catch (NumberFormatException var3) {
            throw new NumberInvalidException("commands.generic.num.invalid", input);
        }
    }

    public static double parseDouble(String input, double min) throws NumberInvalidException {
        return CommandBase.parseDouble(input, min, Double.MAX_VALUE);
    }

    public static double parseDouble(String input, double min, double max) throws NumberInvalidException {
        double d0 = CommandBase.parseDouble(input);
        if (d0 < min) {
            throw new NumberInvalidException("commands.generic.num.tooSmall", String.format("%.2f", d0), String.format("%.2f", min));
        }
        if (d0 > max) {
            throw new NumberInvalidException("commands.generic.num.tooBig", String.format("%.2f", d0), String.format("%.2f", max));
        }
        return d0;
    }

    public static boolean parseBoolean(String input) throws CommandException {
        if (!"true".equals(input) && !"1".equals(input)) {
            if (!"false".equals(input) && !"0".equals(input)) {
                throw new CommandException("commands.generic.boolean.invalid", input);
            }
            return false;
        }
        return true;
    }

    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender sender) throws PlayerNotFoundException {
        if (sender instanceof EntityPlayerMP) {
            return (EntityPlayerMP)sender;
        }
        throw new PlayerNotFoundException("commands.generic.player.unspecified");
    }

    public static List<EntityPlayerMP> func_193513_a(MinecraftServer p_193513_0_, ICommandSender p_193513_1_, String p_193513_2_) throws CommandException {
        List<EntityPlayerMP> list = EntitySelector.func_193531_b(p_193513_1_, p_193513_2_);
        return list.isEmpty() ? Lists.newArrayList(CommandBase.func_193512_a(p_193513_0_, null, p_193513_2_)) : list;
    }

    public static EntityPlayerMP getPlayer(MinecraftServer server, ICommandSender sender, String target) throws PlayerNotFoundException, CommandException {
        return CommandBase.func_193512_a(server, EntitySelector.matchOnePlayer(sender, target), target);
    }

    private static EntityPlayerMP func_193512_a(MinecraftServer p_193512_0_, @Nullable EntityPlayerMP p_193512_1_, String p_193512_2_) throws CommandException {
        if (p_193512_1_ == null) {
            try {
                p_193512_1_ = p_193512_0_.getPlayerList().getPlayerByUUID(UUID.fromString(p_193512_2_));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        if (p_193512_1_ == null) {
            p_193512_1_ = p_193512_0_.getPlayerList().getPlayerByUsername(p_193512_2_);
        }
        if (p_193512_1_ == null) {
            throw new PlayerNotFoundException("commands.generic.player.notFound", p_193512_2_);
        }
        return p_193512_1_;
    }

    public static Entity getEntity(MinecraftServer server, ICommandSender sender, String target) throws EntityNotFoundException, CommandException {
        return CommandBase.getEntity(server, sender, target, Entity.class);
    }

    public static <T extends Entity> T getEntity(MinecraftServer server, ICommandSender sender, String target, Class<? extends T> targetClass) throws EntityNotFoundException, CommandException {
        Object entity;
        block6: {
            entity = EntitySelector.matchOneEntity(sender, target, targetClass);
            if (entity == null) {
                entity = server.getPlayerList().getPlayerByUsername(target);
            }
            if (entity == null) {
                try {
                    UUID uuid = UUID.fromString(target);
                    entity = server.getEntityFromUuid(uuid);
                    if (entity == null) {
                        entity = server.getPlayerList().getPlayerByUUID(uuid);
                    }
                }
                catch (IllegalArgumentException var6) {
                    if (target.split("-").length != 5) break block6;
                    throw new EntityNotFoundException("commands.generic.entity.invalidUuid", target);
                }
            }
        }
        if (entity != null && targetClass.isAssignableFrom(entity.getClass())) {
            return entity;
        }
        throw new EntityNotFoundException(target);
    }

    public static List<Entity> getEntityList(MinecraftServer server, ICommandSender sender, String target) throws EntityNotFoundException, CommandException {
        return EntitySelector.hasArguments(target) ? EntitySelector.matchEntities(sender, target, Entity.class) : Lists.newArrayList(CommandBase.getEntity(server, sender, target));
    }

    public static String getPlayerName(MinecraftServer server, ICommandSender sender, String target) throws PlayerNotFoundException, CommandException {
        try {
            return CommandBase.getPlayer(server, sender, target).getName();
        }
        catch (CommandException commandexception) {
            if (EntitySelector.hasArguments(target)) {
                throw commandexception;
            }
            return target;
        }
    }

    public static String getEntityName(MinecraftServer server, ICommandSender sender, String target) throws EntityNotFoundException, CommandException {
        try {
            return CommandBase.getPlayer(server, sender, target).getName();
        }
        catch (PlayerNotFoundException var6) {
            try {
                return CommandBase.getEntity(server, sender, target).getCachedUniqueIdString();
            }
            catch (EntityNotFoundException entitynotfoundexception) {
                if (EntitySelector.hasArguments(target)) {
                    throw entitynotfoundexception;
                }
                return target;
            }
        }
    }

    public static ITextComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index) throws CommandException, PlayerNotFoundException {
        return CommandBase.getChatComponentFromNthArg(sender, args, index, false);
    }

    public static ITextComponent getChatComponentFromNthArg(ICommandSender sender, String[] args, int index, boolean p_147176_3_) throws PlayerNotFoundException, CommandException {
        TextComponentString itextcomponent = new TextComponentString("");
        for (int i = index; i < args.length; ++i) {
            if (i > index) {
                itextcomponent.appendText(" ");
            }
            ITextComponent itextcomponent1 = new TextComponentString(args[i]);
            if (p_147176_3_) {
                ITextComponent itextcomponent2 = EntitySelector.matchEntitiesToTextComponent(sender, args[i]);
                if (itextcomponent2 == null) {
                    if (EntitySelector.hasArguments(args[i])) {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", args[i]);
                    }
                } else {
                    itextcomponent1 = itextcomponent2;
                }
            }
            itextcomponent.appendSibling(itextcomponent1);
        }
        return itextcomponent;
    }

    public static String buildString(String[] args, int startPos) {
        StringBuilder stringbuilder = new StringBuilder();
        for (int i = startPos; i < args.length; ++i) {
            if (i > startPos) {
                stringbuilder.append(" ");
            }
            String s = args[i];
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }

    public static CoordinateArg parseCoordinate(double base, String selectorArg, boolean centerBlock) throws NumberInvalidException {
        return CommandBase.parseCoordinate(base, selectorArg, -30000000, 30000000, centerBlock);
    }

    public static CoordinateArg parseCoordinate(double base, String selectorArg, int min, int max, boolean centerBlock) throws NumberInvalidException {
        boolean flag = selectorArg.startsWith("~");
        if (flag && Double.isNaN(base)) {
            throw new NumberInvalidException("commands.generic.num.invalid", base);
        }
        double d0 = 0.0;
        if (!flag || selectorArg.length() > 1) {
            boolean flag1 = selectorArg.contains(".");
            if (flag) {
                selectorArg = selectorArg.substring(1);
            }
            d0 += CommandBase.parseDouble(selectorArg);
            if (!flag1 && !flag && centerBlock) {
                d0 += 0.5;
            }
        }
        double d1 = d0 + (flag ? base : 0.0);
        if (min != 0 || max != 0) {
            if (d1 < (double)min) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", String.format("%.2f", d1), min);
            }
            if (d1 > (double)max) {
                throw new NumberInvalidException("commands.generic.num.tooBig", String.format("%.2f", d1), max);
            }
        }
        return new CoordinateArg(d1, d0, flag);
    }

    public static double parseDouble(double base, String input, boolean centerBlock) throws NumberInvalidException {
        return CommandBase.parseDouble(base, input, -30000000, 30000000, centerBlock);
    }

    public static double parseDouble(double base, String input, int min, int max, boolean centerBlock) throws NumberInvalidException {
        double d0;
        boolean flag = input.startsWith("~");
        if (flag && Double.isNaN(base)) {
            throw new NumberInvalidException("commands.generic.num.invalid", base);
        }
        double d = d0 = flag ? base : 0.0;
        if (!flag || input.length() > 1) {
            boolean flag1 = input.contains(".");
            if (flag) {
                input = input.substring(1);
            }
            d0 += CommandBase.parseDouble(input);
            if (!flag1 && !flag && centerBlock) {
                d0 += 0.5;
            }
        }
        if (min != 0 || max != 0) {
            if (d0 < (double)min) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", String.format("%.2f", d0), min);
            }
            if (d0 > (double)max) {
                throw new NumberInvalidException("commands.generic.num.tooBig", String.format("%.2f", d0), max);
            }
        }
        return d0;
    }

    public static Item getItemByText(ICommandSender sender, String id) throws NumberInvalidException {
        ResourceLocation resourcelocation = new ResourceLocation(id);
        Item item = Item.REGISTRY.getObject(resourcelocation);
        if (item == null) {
            throw new NumberInvalidException("commands.give.item.notFound", resourcelocation);
        }
        return item;
    }

    public static Block getBlockByText(ICommandSender sender, String id) throws NumberInvalidException {
        ResourceLocation resourcelocation = new ResourceLocation(id);
        if (!Block.REGISTRY.containsKey(resourcelocation)) {
            throw new NumberInvalidException("commands.give.block.notFound", resourcelocation);
        }
        return Block.REGISTRY.getObject(resourcelocation);
    }

    public static IBlockState func_190794_a(Block p_190794_0_, String p_190794_1_) throws NumberInvalidException, InvalidBlockStateException {
        try {
            int i = Integer.parseInt(p_190794_1_);
            if (i < 0) {
                throw new NumberInvalidException("commands.generic.num.tooSmall", i, 0);
            }
            if (i > 15) {
                throw new NumberInvalidException("commands.generic.num.tooBig", i, 15);
            }
            return p_190794_0_.getStateFromMeta(Integer.parseInt(p_190794_1_));
        }
        catch (RuntimeException var7) {
            try {
                Map<IProperty<?>, Comparable<?>> map = CommandBase.func_190795_c(p_190794_0_, p_190794_1_);
                IBlockState iblockstate = p_190794_0_.getDefaultState();
                for (Map.Entry<IProperty<?>, Comparable<?>> entry : map.entrySet()) {
                    iblockstate = CommandBase.func_190793_a(iblockstate, entry.getKey(), entry.getValue());
                }
                return iblockstate;
            }
            catch (RuntimeException var6) {
                throw new InvalidBlockStateException("commands.generic.blockstate.invalid", p_190794_1_, Block.REGISTRY.getNameForObject(p_190794_0_));
            }
        }
    }

    private static <T extends Comparable<T>> IBlockState func_190793_a(IBlockState p_190793_0_, IProperty<T> p_190793_1_, Comparable<?> p_190793_2_) {
        return p_190793_0_.withProperty(p_190793_1_, p_190793_2_);
    }

    public static Predicate<IBlockState> func_190791_b(final Block p_190791_0_, String p_190791_1_) throws InvalidBlockStateException {
        if (!"*".equals(p_190791_1_) && !"-1".equals(p_190791_1_)) {
            try {
                final int i = Integer.parseInt(p_190791_1_);
                return new Predicate<IBlockState>(){

                    @Override
                    public boolean apply(@Nullable IBlockState p_apply_1_) {
                        return i == p_apply_1_.getBlock().getMetaFromState(p_apply_1_);
                    }
                };
            }
            catch (RuntimeException var3) {
                final Map<IProperty<?>, Comparable<?>> map = CommandBase.func_190795_c(p_190791_0_, p_190791_1_);
                return new Predicate<IBlockState>(){

                    @Override
                    public boolean apply(@Nullable IBlockState p_apply_1_) {
                        if (p_apply_1_ != null && p_190791_0_ == p_apply_1_.getBlock()) {
                            for (Map.Entry entry : map.entrySet()) {
                                if (p_apply_1_.getValue((IProperty)entry.getKey()).equals(entry.getValue())) continue;
                                return false;
                            }
                            return true;
                        }
                        return false;
                    }
                };
            }
        }
        return Predicates.alwaysTrue();
    }

    private static Map<IProperty<?>, Comparable<?>> func_190795_c(Block p_190795_0_, String p_190795_1_) throws InvalidBlockStateException {
        HashMap<IProperty<?>, Comparable<?>> map = Maps.newHashMap();
        if ("default".equals(p_190795_1_)) {
            return p_190795_0_.getDefaultState().getProperties();
        }
        BlockStateContainer blockstatecontainer = p_190795_0_.getBlockState();
        Iterator<String> iterator = field_190796_b.split(p_190795_1_).iterator();
        while (true) {
            Object comparable;
            IProperty<?> iproperty;
            if (!iterator.hasNext()) {
                return map;
            }
            String s = iterator.next();
            Iterator<String> iterator1 = field_190797_c.split(s).iterator();
            if (!iterator1.hasNext() || (iproperty = blockstatecontainer.getProperty(iterator1.next())) == null || !iterator1.hasNext() || (comparable = CommandBase.func_190792_a(iproperty, iterator1.next())) == null) break;
            map.put(iproperty, (Comparable<?>)comparable);
        }
        throw new InvalidBlockStateException("commands.generic.blockstate.invalid", p_190795_1_, Block.REGISTRY.getNameForObject(p_190795_0_));
    }

    @Nullable
    private static <T extends Comparable<T>> T func_190792_a(IProperty<T> p_190792_0_, String p_190792_1_) {
        return (T)((Comparable)p_190792_0_.parseValue(p_190792_1_).orNull());
    }

    public static String joinNiceString(Object[] elements) {
        StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < elements.length; ++i) {
            String s = elements[i].toString();
            if (i > 0) {
                if (i == elements.length - 1) {
                    stringbuilder.append(" and ");
                } else {
                    stringbuilder.append(", ");
                }
            }
            stringbuilder.append(s);
        }
        return stringbuilder.toString();
    }

    public static ITextComponent join(List<ITextComponent> components) {
        TextComponentString itextcomponent = new TextComponentString("");
        for (int i = 0; i < components.size(); ++i) {
            if (i > 0) {
                if (i == components.size() - 1) {
                    itextcomponent.appendText(" and ");
                } else if (i > 0) {
                    itextcomponent.appendText(", ");
                }
            }
            itextcomponent.appendSibling(components.get(i));
        }
        return itextcomponent;
    }

    public static String joinNiceStringFromCollection(Collection<String> strings) {
        return CommandBase.joinNiceString(strings.toArray(new String[strings.size()]));
    }

    public static List<String> getTabCompletionCoordinate(String[] inputArgs, int index, @Nullable BlockPos pos) {
        String s;
        if (pos == null) {
            return Lists.newArrayList("~");
        }
        int i = inputArgs.length - 1;
        if (i == index) {
            s = Integer.toString(pos.getX());
        } else if (i == index + 1) {
            s = Integer.toString(pos.getY());
        } else {
            if (i != index + 2) {
                return Collections.emptyList();
            }
            s = Integer.toString(pos.getZ());
        }
        return Lists.newArrayList(s);
    }

    public static List<String> getTabCompletionCoordinateXZ(String[] inputArgs, int index, @Nullable BlockPos lookedPos) {
        String s;
        if (lookedPos == null) {
            return Lists.newArrayList("~");
        }
        int i = inputArgs.length - 1;
        if (i == index) {
            s = Integer.toString(lookedPos.getX());
        } else {
            if (i != index + 1) {
                return Collections.emptyList();
            }
            s = Integer.toString(lookedPos.getZ());
        }
        return Lists.newArrayList(s);
    }

    public static boolean doesStringStartWith(String original, String region) {
        return region.regionMatches(true, 0, original, 0, original.length());
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] args, String ... possibilities) {
        return CommandBase.getListOfStringsMatchingLastWord(args, Arrays.asList(possibilities));
    }

    public static List<String> getListOfStringsMatchingLastWord(String[] inputArgs, Collection<?> possibleCompletions) {
        String s = inputArgs[inputArgs.length - 1];
        ArrayList<String> list = Lists.newArrayList();
        if (!possibleCompletions.isEmpty()) {
            for (String s1 : Iterables.transform(possibleCompletions, Functions.toStringFunction())) {
                if (!CommandBase.doesStringStartWith(s, s1)) continue;
                list.add(s1);
            }
            if (list.isEmpty()) {
                for (String object : possibleCompletions) {
                    if (!(object instanceof ResourceLocation) || !CommandBase.doesStringStartWith(s, ((ResourceLocation)((Object)object)).getPath())) continue;
                    list.add(String.valueOf(object));
                }
            }
        }
        return list;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    public static void notifyCommandListener(ICommandSender sender, ICommand command, String translationKey, Object ... translationArgs) {
        CommandBase.notifyCommandListener(sender, command, 0, translationKey, translationArgs);
    }

    public static void notifyCommandListener(ICommandSender sender, ICommand command, int flags, String translationKey, Object ... translationArgs) {
        if (commandListener != null) {
            commandListener.notifyListener(sender, command, flags, translationKey, translationArgs);
        }
    }

    public static void setCommandListener(ICommandListener listener) {
        commandListener = listener;
    }

    @Override
    public int compareTo(ICommand p_compareTo_1_) {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

    static {
        field_190796_b = Splitter.on(',');
        field_190797_c = Splitter.on('=').limit(2);
    }

    public static class CoordinateArg {
        private final double result;
        private final double amount;
        private final boolean isRelative;

        protected CoordinateArg(double resultIn, double amountIn, boolean relative) {
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

