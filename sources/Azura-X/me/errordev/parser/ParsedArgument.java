package me.errordev.parser;

public class ParsedArgument {
    private final String input;

    public ParsedArgument(String input) {
        this.input = input;
    }

    public String getAsString() {
        return this.input;
    }

    public int getAsInt() {
        try {
            return Integer.parseInt(this.input);
        }
        catch (Exception exception) {
            return 0;
        }
    }

    public double getAsDouble() {
        try {
            return Double.parseDouble(this.input);
        }
        catch (Exception exception) {
            return 0.0;
        }
    }

    public long getAsLong() {
        try {
            return Long.parseLong(this.input);
        }
        catch (Exception exception) {
            return 0L;
        }
    }

    public float getAsFloat() {
        try {
            return Float.parseFloat(this.input);
        }
        catch (Exception exception) {
            return 0.0f;
        }
    }

    public byte getAsByte() {
        try {
            return Byte.parseByte(this.input);
        }
        catch (Exception exception) {
            return 0;
        }
    }
}
