// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.config;

import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagString;
import net.optifine.util.StrUtils;
import net.minecraft.nbt.NBTTagInt;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.Arrays;
import net.minecraft.src.Config;
import java.util.regex.Pattern;

public class NbtTagValue
{
    private String[] parents;
    private String name;
    private boolean negative;
    private int type;
    private String value;
    private int valueFormat;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_PATTERN = 1;
    private static final int TYPE_IPATTERN = 2;
    private static final int TYPE_REGEX = 3;
    private static final int TYPE_IREGEX = 4;
    private static final String PREFIX_PATTERN = "pattern:";
    private static final String PREFIX_IPATTERN = "ipattern:";
    private static final String PREFIX_REGEX = "regex:";
    private static final String PREFIX_IREGEX = "iregex:";
    private static final int FORMAT_DEFAULT = 0;
    private static final int FORMAT_HEX_COLOR = 1;
    private static final String PREFIX_HEX_COLOR = "#";
    private static final Pattern PATTERN_HEX_COLOR;
    
    public NbtTagValue(final String tag, String value) {
        this.parents = null;
        this.name = null;
        this.negative = false;
        this.type = 0;
        this.value = null;
        this.valueFormat = 0;
        final String[] astring = Config.tokenize(tag, ".");
        this.parents = Arrays.copyOfRange(astring, 0, astring.length - 1);
        this.name = astring[astring.length - 1];
        if (value.startsWith("!")) {
            this.negative = true;
            value = value.substring(1);
        }
        if (value.startsWith("pattern:")) {
            this.type = 1;
            value = value.substring("pattern:".length());
        }
        else if (value.startsWith("ipattern:")) {
            this.type = 2;
            value = value.substring("ipattern:".length()).toLowerCase();
        }
        else if (value.startsWith("regex:")) {
            this.type = 3;
            value = value.substring("regex:".length());
        }
        else if (value.startsWith("iregex:")) {
            this.type = 4;
            value = value.substring("iregex:".length()).toLowerCase();
        }
        else {
            this.type = 0;
        }
        value = StringEscapeUtils.unescapeJava(value);
        if (this.type == 0 && NbtTagValue.PATTERN_HEX_COLOR.matcher(value).matches()) {
            this.valueFormat = 1;
        }
        this.value = value;
    }
    
    public boolean matches(final NBTTagCompound nbt) {
        return this.negative ? (!this.matchesCompound(nbt)) : this.matchesCompound(nbt);
    }
    
    public boolean matchesCompound(final NBTTagCompound nbt) {
        if (nbt == null) {
            return false;
        }
        NBTBase nbtbase = nbt;
        for (int i = 0; i < this.parents.length; ++i) {
            final String s = this.parents[i];
            nbtbase = getChildTag(nbtbase, s);
            if (nbtbase == null) {
                return false;
            }
        }
        if (this.name.equals("*")) {
            return this.matchesAnyChild(nbtbase);
        }
        nbtbase = getChildTag(nbtbase, this.name);
        return nbtbase != null && this.matchesBase(nbtbase);
    }
    
    private boolean matchesAnyChild(final NBTBase tagBase) {
        if (tagBase instanceof NBTTagCompound) {
            final NBTTagCompound nbttagcompound = (NBTTagCompound)tagBase;
            for (final String s : nbttagcompound.getKeySet()) {
                final NBTBase nbtbase = nbttagcompound.getTag(s);
                if (this.matchesBase(nbtbase)) {
                    return true;
                }
            }
        }
        if (tagBase instanceof NBTTagList) {
            final NBTTagList nbttaglist = (NBTTagList)tagBase;
            for (int i = nbttaglist.tagCount(), j = 0; j < i; ++j) {
                final NBTBase nbtbase2 = nbttaglist.get(j);
                if (this.matchesBase(nbtbase2)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static NBTBase getChildTag(final NBTBase tagBase, final String tag) {
        if (tagBase instanceof NBTTagCompound) {
            final NBTTagCompound nbttagcompound = (NBTTagCompound)tagBase;
            return nbttagcompound.getTag(tag);
        }
        if (!(tagBase instanceof NBTTagList)) {
            return null;
        }
        final NBTTagList nbttaglist = (NBTTagList)tagBase;
        if (tag.equals("count")) {
            return new NBTTagInt(nbttaglist.tagCount());
        }
        final int i = Config.parseInt(tag, -1);
        return (i >= 0 && i < nbttaglist.tagCount()) ? nbttaglist.get(i) : null;
    }
    
    public boolean matchesBase(final NBTBase nbtBase) {
        if (nbtBase == null) {
            return false;
        }
        final String s = getNbtString(nbtBase, this.valueFormat);
        return this.matchesValue(s);
    }
    
    public boolean matchesValue(final String nbtValue) {
        if (nbtValue == null) {
            return false;
        }
        switch (this.type) {
            case 0: {
                return nbtValue.equals(this.value);
            }
            case 1: {
                return this.matchesPattern(nbtValue, this.value);
            }
            case 2: {
                return this.matchesPattern(nbtValue.toLowerCase(), this.value);
            }
            case 3: {
                return this.matchesRegex(nbtValue, this.value);
            }
            case 4: {
                return this.matchesRegex(nbtValue.toLowerCase(), this.value);
            }
            default: {
                throw new IllegalArgumentException("Unknown NbtTagValue type: " + this.type);
            }
        }
    }
    
    private boolean matchesPattern(final String str, final String pattern) {
        return StrUtils.equalsMask(str, pattern, '*', '?');
    }
    
    private boolean matchesRegex(final String str, final String regex) {
        return str.matches(regex);
    }
    
    private static String getNbtString(final NBTBase nbtBase, final int format) {
        if (nbtBase == null) {
            return null;
        }
        if (nbtBase instanceof NBTTagString) {
            final NBTTagString nbttagstring = (NBTTagString)nbtBase;
            return nbttagstring.getString();
        }
        if (nbtBase instanceof NBTTagInt) {
            final NBTTagInt nbttagint = (NBTTagInt)nbtBase;
            return (format == 1) ? ("#" + StrUtils.fillLeft(Integer.toHexString(nbttagint.getInt()), 6, '0')) : Integer.toString(nbttagint.getInt());
        }
        if (nbtBase instanceof NBTTagByte) {
            final NBTTagByte nbttagbyte = (NBTTagByte)nbtBase;
            return Byte.toString(nbttagbyte.getByte());
        }
        if (nbtBase instanceof NBTTagShort) {
            final NBTTagShort nbttagshort = (NBTTagShort)nbtBase;
            return Short.toString(nbttagshort.getShort());
        }
        if (nbtBase instanceof NBTTagLong) {
            final NBTTagLong nbttaglong = (NBTTagLong)nbtBase;
            return Long.toString(nbttaglong.getLong());
        }
        if (nbtBase instanceof NBTTagFloat) {
            final NBTTagFloat nbttagfloat = (NBTTagFloat)nbtBase;
            return Float.toString(nbttagfloat.getFloat());
        }
        if (nbtBase instanceof NBTTagDouble) {
            final NBTTagDouble nbttagdouble = (NBTTagDouble)nbtBase;
            return Double.toString(nbttagdouble.getDouble());
        }
        return nbtBase.toString();
    }
    
    @Override
    public String toString() {
        final StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < this.parents.length; ++i) {
            final String s = this.parents[i];
            if (i > 0) {
                stringbuffer.append(".");
            }
            stringbuffer.append(s);
        }
        if (stringbuffer.length() > 0) {
            stringbuffer.append(".");
        }
        stringbuffer.append(this.name);
        stringbuffer.append(" = ");
        stringbuffer.append(this.value);
        return stringbuffer.toString();
    }
    
    static {
        PATTERN_HEX_COLOR = Pattern.compile("^#[0-9a-f]{6}+$");
    }
}
