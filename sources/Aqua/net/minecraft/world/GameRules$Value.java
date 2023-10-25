package net.minecraft.world;

import net.minecraft.world.GameRules;

static class GameRules.Value {
    private String valueString;
    private boolean valueBoolean;
    private int valueInteger;
    private double valueDouble;
    private final GameRules.ValueType type;

    public GameRules.Value(String value, GameRules.ValueType type) {
        this.type = type;
        this.setValue(value);
    }

    public void setValue(String value) {
        this.valueString = value;
        if (value != null) {
            if (value.equals((Object)"false")) {
                this.valueBoolean = false;
                return;
            }
            if (value.equals((Object)"true")) {
                this.valueBoolean = true;
                return;
            }
        }
        this.valueBoolean = Boolean.parseBoolean((String)value);
        this.valueInteger = this.valueBoolean ? 1 : 0;
        try {
            this.valueInteger = Integer.parseInt((String)value);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        try {
            this.valueDouble = Double.parseDouble((String)value);
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

    public GameRules.ValueType getType() {
        return this.type;
    }
}
