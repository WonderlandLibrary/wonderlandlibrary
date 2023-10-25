/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules {
    private TreeMap<String, Value> theGameRules = new TreeMap();

    public int getInt(String string) {
        Value value = this.theGameRules.get(string);
        return value != null ? value.getInt() : 0;
    }

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

    public String getString(String string) {
        Value value = this.theGameRules.get(string);
        return value != null ? value.getString() : "";
    }

    public void setOrCreateGameRule(String string, String string2) {
        Value value = this.theGameRules.get(string);
        if (value != null) {
            value.setValue(string2);
        } else {
            this.addGameRule(string, string2, ValueType.ANY_VALUE);
        }
    }

    public boolean getBoolean(String string) {
        Value value = this.theGameRules.get(string);
        return value != null ? value.getBoolean() : false;
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        for (String string : nBTTagCompound.getKeySet()) {
            String string2 = nBTTagCompound.getString(string);
            this.setOrCreateGameRule(string, string2);
        }
    }

    public void addGameRule(String string, String string2, ValueType valueType) {
        this.theGameRules.put(string, new Value(string2, valueType));
    }

    public boolean areSameType(String string, ValueType valueType) {
        Value value = this.theGameRules.get(string);
        return value != null && (value.getType() == valueType || valueType == ValueType.ANY_VALUE);
    }

    public String[] getRules() {
        Set<String> set = this.theGameRules.keySet();
        return set.toArray(new String[set.size()]);
    }

    public boolean hasRule(String string) {
        return this.theGameRules.containsKey(string);
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        for (String string : this.theGameRules.keySet()) {
            Value value = this.theGameRules.get(string);
            nBTTagCompound.setString(string, value.getString());
        }
        return nBTTagCompound;
    }

    public static enum ValueType {
        ANY_VALUE,
        BOOLEAN_VALUE,
        NUMERICAL_VALUE;

    }

    static class Value {
        private String valueString;
        private boolean valueBoolean;
        private double valueDouble;
        private int valueInteger;
        private final ValueType type;

        public void setValue(String string) {
            this.valueString = string;
            this.valueBoolean = Boolean.parseBoolean(string);
            this.valueInteger = this.valueBoolean ? 1 : 0;
            try {
                this.valueInteger = Integer.parseInt(string);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            try {
                this.valueDouble = Double.parseDouble(string);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }

        public Value(String string, ValueType valueType) {
            this.type = valueType;
            this.setValue(string);
        }

        public ValueType getType() {
            return this.type;
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
    }
}

