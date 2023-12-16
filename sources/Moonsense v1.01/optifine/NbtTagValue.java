// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.Arrays;

public class NbtTagValue
{
    private String[] parents;
    private String name;
    private int type;
    private String value;
    private static final int TYPE_TEXT = 0;
    private static final int TYPE_PATTERN = 1;
    private static final int TYPE_IPATTERN = 2;
    private static final int TYPE_REGEX = 3;
    private static final int TYPE_IREGEX = 4;
    private static final String PREFIX_PATTERN = "pattern:";
    private static final String PREFIX_IPATTERN = "ipattern:";
    private static final String PREFIX_REGEX = "regex:";
    private static final String PREFIX_IREGEX = "iregex:";
    
    public NbtTagValue(final String tag, String value) {
        this.parents = null;
        this.name = null;
        this.type = 0;
        this.value = null;
        final String[] names = Config.tokenize(tag, ".");
        this.parents = Arrays.copyOfRange(names, 0, names.length - 1);
        this.name = names[names.length - 1];
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
        this.value = value;
    }
    
    public boolean matches(final NBTTagCompound nbt) {
        if (nbt == null) {
            return false;
        }
        Object tagBase = nbt;
        for (int i = 0; i < this.parents.length; ++i) {
            final String tag = this.parents[i];
            tagBase = getChildTag((NBTBase)tagBase, tag);
            if (tagBase == null) {
                return false;
            }
        }
        if (this.name.equals("*")) {
            return this.matchesAnyChild((NBTBase)tagBase);
        }
        final NBTBase var5 = getChildTag((NBTBase)tagBase, this.name);
        return var5 != null && this.matches(var5);
    }
    
    private boolean matchesAnyChild(final NBTBase tagBase) {
        if (tagBase instanceof NBTTagCompound) {
            final NBTTagCompound tagList = (NBTTagCompound)tagBase;
            final Set count = tagList.getKeySet();
            for (final String nbtBase : count) {
                final NBTBase nbtBase2 = tagList.getTag(nbtBase);
                if (this.matches(nbtBase2)) {
                    return true;
                }
            }
        }
        if (tagBase instanceof NBTTagList) {
            final NBTTagList var7 = (NBTTagList)tagBase;
            for (int var8 = var7.tagCount(), var9 = 0; var9 < var8; ++var9) {
                final NBTBase var10 = var7.get(var9);
                if (this.matches(var10)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static NBTBase getChildTag(final NBTBase tagBase, final String tag) {
        if (tagBase instanceof NBTTagCompound) {
            final NBTTagCompound tagList1 = (NBTTagCompound)tagBase;
            return tagList1.getTag(tag);
        }
        if (tagBase instanceof NBTTagList) {
            final NBTTagList tagList2 = (NBTTagList)tagBase;
            final int index = Config.parseInt(tag, -1);
            return (index < 0) ? null : tagList2.get(index);
        }
        return null;
    }
    
    private boolean matches(final NBTBase nbtBase) {
        if (nbtBase == null) {
            return false;
        }
        final String nbtValue = getValue(nbtBase);
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
    
    private static String getValue(final NBTBase nbtBase) {
        if (nbtBase == null) {
            return null;
        }
        if (nbtBase instanceof NBTTagString) {
            final NBTTagString d6 = (NBTTagString)nbtBase;
            return d6.getString();
        }
        if (nbtBase instanceof NBTTagInt) {
            final NBTTagInt d7 = (NBTTagInt)nbtBase;
            return Integer.toString(d7.getInt());
        }
        if (nbtBase instanceof NBTTagByte) {
            final NBTTagByte d8 = (NBTTagByte)nbtBase;
            return Byte.toString(d8.getByte());
        }
        if (nbtBase instanceof NBTTagShort) {
            final NBTTagShort d9 = (NBTTagShort)nbtBase;
            return Short.toString(d9.getShort());
        }
        if (nbtBase instanceof NBTTagLong) {
            final NBTTagLong d10 = (NBTTagLong)nbtBase;
            return Long.toString(d10.getLong());
        }
        if (nbtBase instanceof NBTTagFloat) {
            final NBTTagFloat d11 = (NBTTagFloat)nbtBase;
            return Float.toString(d11.getFloat());
        }
        if (nbtBase instanceof NBTTagDouble) {
            final NBTTagDouble d12 = (NBTTagDouble)nbtBase;
            return Double.toString(d12.getDouble());
        }
        return nbtBase.toString();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.parents.length; ++i) {
            final String parent = this.parents[i];
            if (i > 0) {
                sb.append(".");
            }
            sb.append(parent);
        }
        if (sb.length() > 0) {
            sb.append(".");
        }
        sb.append(this.name);
        sb.append(" = ");
        sb.append(this.value);
        return sb.toString();
    }
}
