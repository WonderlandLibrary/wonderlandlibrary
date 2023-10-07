// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import java.util.Set;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import java.util.TreeMap;

public class GameRules
{
    private TreeMap theGameRules;
    private static final String __OBFID = "CL_00000136";
    
    public GameRules() {
        this.theGameRules = new TreeMap();
        this.addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("logAdminCommands", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("randomTickSpeed", "3", ValueType.NUMERICAL_VALUE);
        this.addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
        this.addGameRule("reducedDebugInfo", "false", ValueType.BOOLEAN_VALUE);
    }
    
    public void addGameRule(final String key, final String value, final ValueType type) {
        this.theGameRules.put(key, new Value(value, type));
    }
    
    public void setOrCreateGameRule(final String key, final String ruleValue) {
        final Value var3 = this.theGameRules.get(key);
        if (var3 != null) {
            var3.setValue(ruleValue);
        }
        else {
            this.addGameRule(key, ruleValue, ValueType.ANY_VALUE);
        }
    }
    
    public String getGameRuleStringValue(final String name) {
        final Value var2 = this.theGameRules.get(name);
        return (var2 != null) ? var2.getGameRuleStringValue() : "";
    }
    
    public boolean getGameRuleBooleanValue(final String name) {
        final Value var2 = this.theGameRules.get(name);
        return var2 != null && var2.getGameRuleBooleanValue();
    }
    
    public int getInt(final String name) {
        final Value var2 = this.theGameRules.get(name);
        return (var2 != null) ? var2.getInt() : 0;
    }
    
    public NBTTagCompound writeGameRulesToNBT() {
        final NBTTagCompound var1 = new NBTTagCompound();
        for (final String var3 : this.theGameRules.keySet()) {
            final Value var4 = this.theGameRules.get(var3);
            var1.setString(var3, var4.getGameRuleStringValue());
        }
        return var1;
    }
    
    public void readGameRulesFromNBT(final NBTTagCompound nbt) {
        final Set var2 = nbt.getKeySet();
        for (final String var4 : var2) {
            final String var5 = nbt.getString(var4);
            this.setOrCreateGameRule(var4, var5);
        }
    }
    
    public String[] getRules() {
        return (String[])this.theGameRules.keySet().toArray(new String[0]);
    }
    
    public boolean hasRule(final String name) {
        return this.theGameRules.containsKey(name);
    }
    
    public boolean areSameType(final String key, final ValueType otherValue) {
        final Value var3 = this.theGameRules.get(key);
        return var3 != null && (var3.getType() == otherValue || otherValue == ValueType.ANY_VALUE);
    }
    
    static class Value
    {
        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        private final ValueType type;
        private static final String __OBFID = "CL_00000137";
        
        public Value(final String value, final ValueType type) {
            this.type = type;
            this.setValue(value);
        }
        
        public void setValue(final String value) {
            this.valueString = value;
            if (value != null) {
                if (value.equals("false")) {
                    this.valueBoolean = false;
                    return;
                }
                if (value.equals("true")) {
                    this.valueBoolean = true;
                    return;
                }
            }
            this.valueBoolean = Boolean.parseBoolean(value);
            this.valueInteger = (this.valueBoolean ? 1 : 0);
            try {
                this.valueInteger = Integer.parseInt(value);
            }
            catch (NumberFormatException ex) {}
            try {
                this.valueDouble = Double.parseDouble(value);
            }
            catch (NumberFormatException ex2) {}
        }
        
        public String getGameRuleStringValue() {
            return this.valueString;
        }
        
        public boolean getGameRuleBooleanValue() {
            return this.valueBoolean;
        }
        
        public int getInt() {
            return this.valueInteger;
        }
        
        public ValueType getType() {
            return this.type;
        }
    }
    
    public enum ValueType
    {
        ANY_VALUE("ANY_VALUE", 0, "ANY_VALUE", 0), 
        BOOLEAN_VALUE("BOOLEAN_VALUE", 1, "BOOLEAN_VALUE", 1), 
        NUMERICAL_VALUE("NUMERICAL_VALUE", 2, "NUMERICAL_VALUE", 2);
        
        private static final ValueType[] $VALUES;
        private static final String __OBFID = "CL_00002151";
        private static final ValueType[] $VALUES$;
        
        private ValueType(final String p_i46375_1_, final int p_i46375_2_, final String p_i45750_1_, final int p_i45750_2_) {
        }
        
        static {
            $VALUES = new ValueType[] { ValueType.ANY_VALUE, ValueType.BOOLEAN_VALUE, ValueType.NUMERICAL_VALUE };
            $VALUES$ = new ValueType[] { ValueType.ANY_VALUE, ValueType.BOOLEAN_VALUE, ValueType.NUMERICAL_VALUE };
        }
    }
}
