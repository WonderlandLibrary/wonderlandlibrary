/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules {
    private TreeMap<String, Value> theGameRules = new TreeMap();

    public GameRules() {
        this.addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doEntityDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("logAdminCommands", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("randomTickSpeed", "3", ValueType.NUMERICAL_VALUE);
        this.addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("reducedDebugInfo", "false", ValueType.BOOLEAN_VALUE);
    }

    public void addGameRule(String key, String value, ValueType type) {
        this.theGameRules.put(key, new Value(value, type));
    }

    public void setOrCreateGameRule(String key, String ruleValue) {
        Value gamerules$value = this.theGameRules.get(key);
        if (gamerules$value != null) {
            gamerules$value.setValue(ruleValue);
            return;
        }
        this.addGameRule(key, ruleValue, ValueType.ANY_VALUE);
    }

    public String getString(String name) {
        Value gamerules$value = this.theGameRules.get(name);
        if (gamerules$value == null) return "";
        String string = gamerules$value.getString();
        return string;
    }

    public boolean getBoolean(String name) {
        Value gamerules$value = this.theGameRules.get(name);
        if (gamerules$value == null) return false;
        boolean bl = gamerules$value.getBoolean();
        return bl;
    }

    public int getInt(String name) {
        Value gamerules$value = this.theGameRules.get(name);
        if (gamerules$value == null) return 0;
        int n = gamerules$value.getInt();
        return n;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator<String> iterator = this.theGameRules.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            Value gamerules$value = this.theGameRules.get(s);
            nbttagcompound.setString(s, gamerules$value.getString());
        }
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        Iterator<String> iterator = nbt.getKeySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            String s1 = nbt.getString(s);
            this.setOrCreateGameRule(s, s1);
        }
    }

    public String[] getRules() {
        Set<String> set = this.theGameRules.keySet();
        return set.toArray(new String[set.size()]);
    }

    public boolean hasRule(String name) {
        return this.theGameRules.containsKey(name);
    }

    public boolean areSameType(String key, ValueType otherValue) {
        Value gamerules$value = this.theGameRules.get(key);
        if (gamerules$value == null) return false;
        if (gamerules$value.getType() == otherValue) return true;
        if (otherValue != ValueType.ANY_VALUE) return false;
        return true;
    }

    public static enum ValueType {
        ANY_VALUE,
        BOOLEAN_VALUE,
        NUMERICAL_VALUE;

    }

    static class Value {
        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        private final ValueType type;

        public Value(String value, ValueType type) {
            this.type = type;
            this.setValue(value);
        }

        public void setValue(String value) {
            this.valueString = value;
            this.valueBoolean = Boolean.parseBoolean(value);
            this.valueInteger = this.valueBoolean ? 1 : 0;
            try {
                this.valueInteger = Integer.parseInt(value);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            try {
                this.valueDouble = Double.parseDouble(value);
                return;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }

        public String getString() {
            return this.valueString;
        }

        public boolean getBoolean() {
            return this.valueBoolean;
        }

        public int getInt() {
            return this.valueInteger;
        }

        public ValueType getType() {
            return this.type;
        }
    }
}

