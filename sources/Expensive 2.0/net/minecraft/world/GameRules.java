package net.minecraft.world;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicLike;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameRules
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map < RuleKey<?>, RuleType<? >> GAME_RULES = Maps.newTreeMap(Comparator.comparing((key) ->
    {
        return key.gameRuleName;
    }));
    public static final RuleKey<BooleanValue> DO_FIRE_TICK = register("doFireTick", Category.UPDATES, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> MOB_GRIEFING = register("mobGriefing", Category.MOBS, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> KEEP_INVENTORY = register("keepInventory", Category.PLAYER, BooleanValue.create(false));
    public static final RuleKey<BooleanValue> DO_MOB_SPAWNING = register("doMobSpawning", Category.SPAWNING, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_MOB_LOOT = register("doMobLoot", Category.DROPS, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_TILE_DROPS = register("doTileDrops", Category.DROPS, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_ENTITY_DROPS = register("doEntityDrops", Category.DROPS, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> COMMAND_BLOCK_OUTPUT = register("commandBlockOutput", Category.CHAT, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> NATURAL_REGENERATION = register("naturalRegeneration", Category.PLAYER, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_DAYLIGHT_CYCLE = register("doDaylightCycle", Category.UPDATES, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> LOG_ADMIN_COMMANDS = register("logAdminCommands", Category.CHAT, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> SHOW_DEATH_MESSAGES = register("showDeathMessages", Category.CHAT, BooleanValue.create(true));
    public static final RuleKey<IntegerValue> RANDOM_TICK_SPEED = register("randomTickSpeed", Category.UPDATES, IntegerValue.create(3));
    public static final RuleKey<BooleanValue> SEND_COMMAND_FEEDBACK = register("sendCommandFeedback", Category.CHAT, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> REDUCED_DEBUG_INFO = register("reducedDebugInfo", Category.MISC, BooleanValue.create(false, (server, value) ->
    {
        byte b0 = (byte)(value.get() ? 22 : 23);

        for (ServerPlayerEntity serverplayerentity : server.getPlayerList().getPlayers())
        {
            serverplayerentity.connection.sendPacket(new SEntityStatusPacket(serverplayerentity, b0));
        }
    }));
    public static final RuleKey<BooleanValue> SPECTATORS_GENERATE_CHUNKS = register("spectatorsGenerateChunks", Category.PLAYER, BooleanValue.create(true));
    public static final RuleKey<IntegerValue> SPAWN_RADIUS = register("spawnRadius", Category.PLAYER, IntegerValue.create(10));
    public static final RuleKey<BooleanValue> DISABLE_ELYTRA_MOVEMENT_CHECK = register("disableElytraMovementCheck", Category.PLAYER, BooleanValue.create(false));
    public static final RuleKey<IntegerValue> MAX_ENTITY_CRAMMING = register("maxEntityCramming", Category.MOBS, IntegerValue.create(24));
    public static final RuleKey<BooleanValue> DO_WEATHER_CYCLE = register("doWeatherCycle", Category.UPDATES, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_LIMITED_CRAFTING = register("doLimitedCrafting", Category.PLAYER, BooleanValue.create(false));
    public static final RuleKey<IntegerValue> MAX_COMMAND_CHAIN_LENGTH = register("maxCommandChainLength", Category.MISC, IntegerValue.create(65536));
    public static final RuleKey<BooleanValue> ANNOUNCE_ADVANCEMENTS = register("announceAdvancements", Category.CHAT, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DISABLE_RAIDS = register("disableRaids", Category.MOBS, BooleanValue.create(false));
    public static final RuleKey<BooleanValue> DO_INSOMNIA = register("doInsomnia", Category.SPAWNING, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_IMMEDIATE_RESPAWN = register("doImmediateRespawn", Category.PLAYER, BooleanValue.create(false, (server, value) ->
    {
        for (ServerPlayerEntity serverplayerentity : server.getPlayerList().getPlayers())
        {
            serverplayerentity.connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241775_l_, value.get() ? 1.0F : 0.0F));
        }
    }));
    public static final RuleKey<BooleanValue> DROWNING_DAMAGE = register("drowningDamage", Category.PLAYER, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> FALL_DAMAGE = register("fallDamage", Category.PLAYER, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> FIRE_DAMAGE = register("fireDamage", Category.PLAYER, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_PATROL_SPAWNING = register("doPatrolSpawning", Category.SPAWNING, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> DO_TRADER_SPAWNING = register("doTraderSpawning", Category.SPAWNING, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> FORGIVE_DEAD_PLAYERS = register("forgiveDeadPlayers", Category.MOBS, BooleanValue.create(true));
    public static final RuleKey<BooleanValue> UNIVERSAL_ANGER = register("universalAnger", Category.MOBS, BooleanValue.create(false));
    private final Map < RuleKey<?>, RuleValue<? >> rules;

    private static <T extends RuleValue<T>> RuleKey<T> register(String name, Category category, RuleType<T> type)
    {
        RuleKey<T> rulekey = new RuleKey<>(name, category);
        RuleType<?> ruletype = GAME_RULES.put(rulekey, type);

        if (ruletype != null)
        {
            throw new IllegalStateException("Duplicate game rule registration for " + name);
        }
        else
        {
            return rulekey;
        }
    }

    public GameRules(DynamicLike<?> dynamic)
    {
        this();
        this.decode(dynamic);
    }

    public GameRules()
    {
        this.rules = GAME_RULES.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (entry) ->
        {
            return entry.getValue().createValue();
        }));
    }

    private GameRules(Map < RuleKey<?>, RuleValue<? >> keyToValueMap)
    {
        this.rules = keyToValueMap;
    }

    public <T extends RuleValue<T>> T get(RuleKey<T> key)
    {
        return (T)(this.rules.get(key));
    }

    /**
     * Return the defined game rules as NBT.
     */
    public CompoundNBT write()
    {
        CompoundNBT compoundnbt = new CompoundNBT();
        this.rules.forEach((key, value) ->
        {
            compoundnbt.putString(key.gameRuleName, value.stringValue());
        });
        return compoundnbt;
    }

    private void decode(DynamicLike<?> dynamic)
    {
        this.rules.forEach((key, value) ->
        {
            dynamic.get(key.gameRuleName).asString().result().ifPresent(value::setStringValue);
        });
    }

    public GameRules clone()
    {
        return new GameRules(this.rules.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (entry) ->
        {
            return entry.getValue().clone();
        })));
    }

    public static void visitAll(IRuleEntryVisitor visitor)
    {
        GAME_RULES.forEach((key, type) ->
        {
            visit(visitor, key, type);
        });
    }

    private static <T extends RuleValue<T>> void visit(IRuleEntryVisitor visitor, RuleKey<?> key, RuleType<?> type)
    {
        visitor.visit((RuleKey)key, type);
        type.visitRule(visitor, (RuleKey) key);
    }

    public void func_234899_a_(GameRules rules, @Nullable MinecraftServer server)
    {
        rules.rules.keySet().forEach((key) ->
        {
            this.getValue(key, rules, server);
        });
    }

    private <T extends RuleValue<T>> void getValue(RuleKey<T> key, GameRules rules, @Nullable MinecraftServer server)
    {
        T t = rules.get(key);
        this.<T>get(key).changeValue(t, server);
    }

    public boolean getBoolean(RuleKey<BooleanValue> key)
    {
        return this.get(key).get();
    }

    public int getInt(RuleKey<IntegerValue> key)
    {
        return this.get(key).get();
    }

    public static class BooleanValue extends RuleValue<BooleanValue>
    {
        private boolean value;

        private static RuleType<BooleanValue> create(boolean defaultValue, BiConsumer<MinecraftServer, BooleanValue> changeListener)
        {
            return new RuleType<>(BoolArgumentType::bool, (type) ->
            {
                return new BooleanValue(type, defaultValue);
            }, changeListener, IRuleEntryVisitor::changeBoolean);
        }

        private static RuleType<BooleanValue> create(boolean defaultValue)
        {
            return create(defaultValue, (server, value) ->
            {
            });
        }

        public BooleanValue(RuleType<BooleanValue> type, boolean defaultValue)
        {
            super(type);
            this.value = defaultValue;
        }

        protected void updateValue0(CommandContext<CommandSource> context, String paramName)
        {
            this.value = BoolArgumentType.getBool(context, paramName);
        }

        public boolean get()
        {
            return this.value;
        }

        public void set(boolean valueIn, @Nullable MinecraftServer server)
        {
            this.value = valueIn;
            this.notifyChange(server);
        }

        public String stringValue()
        {
            return Boolean.toString(this.value);
        }

        protected void setStringValue(String valueIn)
        {
            this.value = Boolean.parseBoolean(valueIn);
        }

        public int intValue()
        {
            return this.value ? 1 : 0;
        }

        protected BooleanValue getValue()
        {
            return this;
        }

        protected BooleanValue clone()
        {
            return new BooleanValue(this.type, this.value);
        }

        public void changeValue(BooleanValue value, @Nullable MinecraftServer server)
        {
            this.value = value.value;
            this.notifyChange(server);
        }
    }

    public static enum Category
    {
        PLAYER("gamerule.category.player"),
        MOBS("gamerule.category.mobs"),
        SPAWNING("gamerule.category.spawning"),
        DROPS("gamerule.category.drops"),
        UPDATES("gamerule.category.updates"),
        CHAT("gamerule.category.chat"),
        MISC("gamerule.category.misc");

        private final String localeString;

        private Category(String localeString)
        {
            this.localeString = localeString;
        }

        public String getLocaleString()
        {
            return this.localeString;
        }
    }

    interface IRule<T extends RuleValue<T>>
    {
        void call(IRuleEntryVisitor p_call_1_, RuleKey<T> p_call_2_, RuleType<T> p_call_3_);
    }

    public interface IRuleEntryVisitor
    {
    default <T extends RuleValue<T>> void visit(RuleKey<T> key, RuleType<T> type)
        {
        }

    default void changeBoolean(RuleKey<BooleanValue> value1, RuleType<BooleanValue> value2)
        {
        }

    default void changeInteger(RuleKey<IntegerValue> value1, RuleType<IntegerValue> value2)
        {
        }
    }

    public static class IntegerValue extends RuleValue<IntegerValue>
    {
        private int value;

        private static RuleType<IntegerValue> create(int defaultValue, BiConsumer<MinecraftServer, IntegerValue> changeListener)
        {
            return new RuleType<>(IntegerArgumentType::integer, (type) ->
            {
                return new IntegerValue(type, defaultValue);
            }, changeListener, IRuleEntryVisitor::changeInteger);
        }

        private static RuleType<IntegerValue> create(int defaultValue)
        {
            return create(defaultValue, (server, value) ->
            {
            });
        }

        public IntegerValue(RuleType<IntegerValue> type, int defaultValue)
        {
            super(type);
            this.value = defaultValue;
        }

        protected void updateValue0(CommandContext<CommandSource> context, String paramName)
        {
            this.value = IntegerArgumentType.getInteger(context, paramName);
        }

        public int get()
        {
            return this.value;
        }

        public String stringValue()
        {
            return Integer.toString(this.value);
        }

        protected void setStringValue(String valueIn)
        {
            this.value = parseInt(valueIn);
        }

        public boolean parseIntValue(String name)
        {
            try
            {
                this.value = Integer.parseInt(name);
                return true;
            }
            catch (NumberFormatException numberformatexception)
            {
                return false;
            }
        }

        private static int parseInt(String strValue)
        {
            if (!strValue.isEmpty())
            {
                try
                {
                    return Integer.parseInt(strValue);
                }
                catch (NumberFormatException numberformatexception)
                {
                    GameRules.LOGGER.warn("Failed to parse integer {}", (Object)strValue);
                }
            }

            return 0;
        }

        public int intValue()
        {
            return this.value;
        }

        protected IntegerValue getValue()
        {
            return this;
        }

        protected IntegerValue clone()
        {
            return new IntegerValue(this.type, this.value);
        }

        public void changeValue(IntegerValue value, @Nullable MinecraftServer server)
        {
            this.value = value.value;
            this.notifyChange(server);
        }
    }

    public static final class RuleKey<T extends RuleValue<T>>
    {
        private final String gameRuleName;
        private final Category category;

        public RuleKey(String gameRuleName, Category category)
        {
            this.gameRuleName = gameRuleName;
            this.category = category;
        }

        public String toString()
        {
            return this.gameRuleName;
        }

        public boolean equals(Object p_equals_1_)
        {
            if (this == p_equals_1_)
            {
                return true;
            }
            else
            {
                return p_equals_1_ instanceof RuleKey && ((RuleKey)p_equals_1_).gameRuleName.equals(this.gameRuleName);
            }
        }

        public int hashCode()
        {
            return this.gameRuleName.hashCode();
        }

        public String getName()
        {
            return this.gameRuleName;
        }

        public String getLocaleString()
        {
            return "gamerule." + this.gameRuleName;
        }

        public Category getCategory()
        {
            return this.category;
        }
    }

    public static class RuleType<T extends RuleValue<T>>
    {
        private final Supplier < ArgumentType<? >> argTypeSupplier;
        private final Function<RuleType<T>, T> valueCreator;
        private final BiConsumer<MinecraftServer, T> changeListener;
        private final IRule<T> rule;

        private RuleType(Supplier < ArgumentType<? >> argTypeSupplier, Function<RuleType<T>, T> valueCreator, BiConsumer<MinecraftServer, T> changeListener, IRule<T> rule)
        {
            this.argTypeSupplier = argTypeSupplier;
            this.valueCreator = valueCreator;
            this.changeListener = changeListener;
            this.rule = rule;
        }

        public RequiredArgumentBuilder < CommandSource, ? > createArgument(String name)
        {
            return Commands.argument(name, this.argTypeSupplier.get());
        }

        public T createValue()
        {
            return this.valueCreator.apply(this);
        }

        public void visitRule(IRuleEntryVisitor visitor, RuleKey<T> key)
        {
            this.rule.call(visitor, key, this);
        }
    }

    public abstract static class RuleValue<T extends RuleValue<T>>
    {
        protected final RuleType<T> type;

        public RuleValue(RuleType<T> type)
        {
            this.type = type;
        }

        protected abstract void updateValue0(CommandContext<CommandSource> context, String paramName);

        public void updateValue(CommandContext<CommandSource> context, String paramName)
        {
            this.updateValue0(context, paramName);
            this.notifyChange(context.getSource().getServer());
        }

        protected void notifyChange(@Nullable MinecraftServer server)
        {
            if (server != null)
            {
                this.type.changeListener.accept(server, this.getValue());
            }
        }

        protected abstract void setStringValue(String valueIn);

        public abstract String stringValue();

        public String toString()
        {
            return this.stringValue();
        }

        public abstract int intValue();

        protected abstract T getValue();

        protected abstract T clone();

        public abstract void changeValue(T value, @Nullable MinecraftServer server);
    }
}
