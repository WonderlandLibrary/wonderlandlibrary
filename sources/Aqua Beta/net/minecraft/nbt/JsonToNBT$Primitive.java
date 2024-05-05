package net.minecraft.nbt;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.util.regex.Pattern;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

static class JsonToNBT.Primitive
extends JsonToNBT.Any {
    private static final Pattern DOUBLE = Pattern.compile((String)"[-+]?[0-9]*\\.?[0-9]+[d|D]");
    private static final Pattern FLOAT = Pattern.compile((String)"[-+]?[0-9]*\\.?[0-9]+[f|F]");
    private static final Pattern BYTE = Pattern.compile((String)"[-+]?[0-9]+[b|B]");
    private static final Pattern LONG = Pattern.compile((String)"[-+]?[0-9]+[l|L]");
    private static final Pattern SHORT = Pattern.compile((String)"[-+]?[0-9]+[s|S]");
    private static final Pattern INTEGER = Pattern.compile((String)"[-+]?[0-9]+");
    private static final Pattern DOUBLE_UNTYPED = Pattern.compile((String)"[-+]?[0-9]*\\.?[0-9]+");
    private static final Splitter SPLITTER = Splitter.on((char)',').omitEmptyStrings();
    protected String jsonValue;

    public JsonToNBT.Primitive(String p_i45139_1_, String p_i45139_2_) {
        this.json = p_i45139_1_;
        this.jsonValue = p_i45139_2_;
    }

    public NBTBase parse() throws NBTException {
        try {
            if (DOUBLE.matcher((CharSequence)this.jsonValue).matches()) {
                return new NBTTagDouble(Double.parseDouble((String)this.jsonValue.substring(0, this.jsonValue.length() - 1)));
            }
            if (FLOAT.matcher((CharSequence)this.jsonValue).matches()) {
                return new NBTTagFloat(Float.parseFloat((String)this.jsonValue.substring(0, this.jsonValue.length() - 1)));
            }
            if (BYTE.matcher((CharSequence)this.jsonValue).matches()) {
                return new NBTTagByte(Byte.parseByte((String)this.jsonValue.substring(0, this.jsonValue.length() - 1)));
            }
            if (LONG.matcher((CharSequence)this.jsonValue).matches()) {
                return new NBTTagLong(Long.parseLong((String)this.jsonValue.substring(0, this.jsonValue.length() - 1)));
            }
            if (SHORT.matcher((CharSequence)this.jsonValue).matches()) {
                return new NBTTagShort(Short.parseShort((String)this.jsonValue.substring(0, this.jsonValue.length() - 1)));
            }
            if (INTEGER.matcher((CharSequence)this.jsonValue).matches()) {
                return new NBTTagInt(Integer.parseInt((String)this.jsonValue));
            }
            if (DOUBLE_UNTYPED.matcher((CharSequence)this.jsonValue).matches()) {
                return new NBTTagDouble(Double.parseDouble((String)this.jsonValue));
            }
            if (this.jsonValue.equalsIgnoreCase("true") || this.jsonValue.equalsIgnoreCase("false")) {
                return new NBTTagByte((byte)(Boolean.parseBoolean((String)this.jsonValue) ? 1 : 0));
            }
        }
        catch (NumberFormatException var6) {
            this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
            return new NBTTagString(this.jsonValue);
        }
        if (this.jsonValue.startsWith("[") && this.jsonValue.endsWith("]")) {
            String s = this.jsonValue.substring(1, this.jsonValue.length() - 1);
            String[] astring = (String[])Iterables.toArray((Iterable)SPLITTER.split((CharSequence)s), String.class);
            try {
                int[] aint = new int[astring.length];
                for (int j = 0; j < astring.length; ++j) {
                    aint[j] = Integer.parseInt((String)astring[j].trim());
                }
                return new NBTTagIntArray(aint);
            }
            catch (NumberFormatException var5) {
                return new NBTTagString(this.jsonValue);
            }
        }
        if (this.jsonValue.startsWith("\"") && this.jsonValue.endsWith("\"")) {
            this.jsonValue = this.jsonValue.substring(1, this.jsonValue.length() - 1);
        }
        this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
        StringBuilder stringbuilder = new StringBuilder();
        for (int i = 0; i < this.jsonValue.length(); ++i) {
            if (i < this.jsonValue.length() - 1 && this.jsonValue.charAt(i) == '\\' && this.jsonValue.charAt(i + 1) == '\\') {
                stringbuilder.append('\\');
                ++i;
                continue;
            }
            stringbuilder.append(this.jsonValue.charAt(i));
        }
        return new NBTTagString(stringbuilder.toString());
    }
}
