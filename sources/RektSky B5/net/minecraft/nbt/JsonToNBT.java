/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Stack;
import java.util.regex.Pattern;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonToNBT {
    private static final Logger logger = LogManager.getLogger();
    private static final Pattern field_179273_b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");

    public static NBTTagCompound getTagFromJson(String jsonString) throws NBTException {
        if (!(jsonString = jsonString.trim()).startsWith("{")) {
            throw new NBTException("Invalid tag encountered, expected '{' as first char.");
        }
        if (JsonToNBT.func_150310_b(jsonString) != 1) {
            throw new NBTException("Encountered multiple top tags, only one expected");
        }
        return (NBTTagCompound)JsonToNBT.func_150316_a("tag", jsonString).parse();
    }

    static int func_150310_b(String p_150310_0_) throws NBTException {
        int i2 = 0;
        boolean flag = false;
        Stack<Character> stack = new Stack<Character>();
        for (int j2 = 0; j2 < p_150310_0_.length(); ++j2) {
            char c0 = p_150310_0_.charAt(j2);
            if (c0 == '\"') {
                if (JsonToNBT.func_179271_b(p_150310_0_, j2)) {
                    if (flag) continue;
                    throw new NBTException("Illegal use of \\\": " + p_150310_0_);
                }
                flag = !flag;
                continue;
            }
            if (flag) continue;
            if (c0 != '{' && c0 != '[') {
                if (c0 == '}' && (stack.isEmpty() || ((Character)stack.pop()).charValue() != '{')) {
                    throw new NBTException("Unbalanced curly brackets {}: " + p_150310_0_);
                }
                if (c0 != ']' || !stack.isEmpty() && ((Character)stack.pop()).charValue() == '[') continue;
                throw new NBTException("Unbalanced square brackets []: " + p_150310_0_);
            }
            if (stack.isEmpty()) {
                ++i2;
            }
            stack.push(Character.valueOf(c0));
        }
        if (flag) {
            throw new NBTException("Unbalanced quotation: " + p_150310_0_);
        }
        if (!stack.isEmpty()) {
            throw new NBTException("Unbalanced brackets: " + p_150310_0_);
        }
        if (i2 == 0 && !p_150310_0_.isEmpty()) {
            i2 = 1;
        }
        return i2;
    }

    static Any func_179272_a(String ... p_179272_0_) throws NBTException {
        return JsonToNBT.func_150316_a(p_179272_0_[0], p_179272_0_[1]);
    }

    static Any func_150316_a(String p_150316_0_, String p_150316_1_) throws NBTException {
        if ((p_150316_1_ = p_150316_1_.trim()).startsWith("{")) {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            Compound jsontonbt$compound = new Compound(p_150316_0_);
            while (p_150316_1_.length() > 0) {
                String s1 = JsonToNBT.func_150314_a(p_150316_1_, true);
                if (s1.length() > 0) {
                    boolean flag1 = false;
                    jsontonbt$compound.field_150491_b.add(JsonToNBT.func_179270_a(s1, flag1));
                }
                if (p_150316_1_.length() < s1.length() + 1) break;
                char c1 = p_150316_1_.charAt(s1.length());
                if (c1 != ',' && c1 != '{' && c1 != '}' && c1 != '[' && c1 != ']') {
                    throw new NBTException("Unexpected token '" + c1 + "' at: " + p_150316_1_.substring(s1.length()));
                }
                p_150316_1_ = p_150316_1_.substring(s1.length() + 1);
            }
            return jsontonbt$compound;
        }
        if (p_150316_1_.startsWith("[") && !field_179273_b.matcher(p_150316_1_).matches()) {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            List jsontonbt$list = new List(p_150316_0_);
            while (p_150316_1_.length() > 0) {
                String s2 = JsonToNBT.func_150314_a(p_150316_1_, false);
                if (s2.length() > 0) {
                    boolean flag = true;
                    jsontonbt$list.field_150492_b.add(JsonToNBT.func_179270_a(s2, flag));
                }
                if (p_150316_1_.length() < s2.length() + 1) break;
                char c0 = p_150316_1_.charAt(s2.length());
                if (c0 != ',' && c0 != '{' && c0 != '}' && c0 != '[' && c0 != ']') {
                    throw new NBTException("Unexpected token '" + c0 + "' at: " + p_150316_1_.substring(s2.length()));
                }
                p_150316_1_ = p_150316_1_.substring(s2.length() + 1);
            }
            return jsontonbt$list;
        }
        return new Primitive(p_150316_0_, p_150316_1_);
    }

    private static Any func_179270_a(String p_179270_0_, boolean p_179270_1_) throws NBTException {
        String s2 = JsonToNBT.func_150313_b(p_179270_0_, p_179270_1_);
        String s1 = JsonToNBT.func_150311_c(p_179270_0_, p_179270_1_);
        return JsonToNBT.func_179272_a(s2, s1);
    }

    private static String func_150314_a(String p_150314_0_, boolean p_150314_1_) throws NBTException {
        int i2 = JsonToNBT.func_150312_a(p_150314_0_, ':');
        int j2 = JsonToNBT.func_150312_a(p_150314_0_, ',');
        if (p_150314_1_) {
            if (i2 == -1) {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150314_0_);
            }
            if (j2 != -1 && j2 < i2) {
                throw new NBTException("Name error at: " + p_150314_0_);
            }
        } else if (i2 == -1 || i2 > j2) {
            i2 = -1;
        }
        return JsonToNBT.func_179269_a(p_150314_0_, i2);
    }

    private static String func_179269_a(String p_179269_0_, int p_179269_1_) throws NBTException {
        int i2;
        Stack<Character> stack = new Stack<Character>();
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        int j2 = 0;
        for (i2 = p_179269_1_ + 1; i2 < p_179269_0_.length(); ++i2) {
            char c0 = p_179269_0_.charAt(i2);
            if (c0 == '\"') {
                if (JsonToNBT.func_179271_b(p_179269_0_, i2)) {
                    if (!flag) {
                        throw new NBTException("Illegal use of \\\": " + p_179269_0_);
                    }
                } else {
                    boolean bl = flag = !flag;
                    if (flag && !flag2) {
                        flag1 = true;
                    }
                    if (!flag) {
                        j2 = i2;
                    }
                }
            } else if (!flag) {
                if (c0 != '{' && c0 != '[') {
                    if (c0 == '}' && (stack.isEmpty() || ((Character)stack.pop()).charValue() != '{')) {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_179269_0_);
                    }
                    if (c0 == ']' && (stack.isEmpty() || ((Character)stack.pop()).charValue() != '[')) {
                        throw new NBTException("Unbalanced square brackets []: " + p_179269_0_);
                    }
                    if (c0 == ',' && stack.isEmpty()) {
                        return p_179269_0_.substring(0, i2);
                    }
                } else {
                    stack.push(Character.valueOf(c0));
                }
            }
            if (Character.isWhitespace(c0)) continue;
            if (!flag && flag1 && j2 != i2) {
                return p_179269_0_.substring(0, j2 + 1);
            }
            flag2 = true;
        }
        return p_179269_0_.substring(0, i2);
    }

    private static String func_150313_b(String p_150313_0_, boolean p_150313_1_) throws NBTException {
        if (p_150313_1_ && ((p_150313_0_ = p_150313_0_.trim()).startsWith("{") || p_150313_0_.startsWith("["))) {
            return "";
        }
        int i2 = JsonToNBT.func_150312_a(p_150313_0_, ':');
        if (i2 == -1) {
            if (p_150313_1_) {
                return "";
            }
            throw new NBTException("Unable to locate name/value separator for string: " + p_150313_0_);
        }
        return p_150313_0_.substring(0, i2).trim();
    }

    private static String func_150311_c(String p_150311_0_, boolean p_150311_1_) throws NBTException {
        if (p_150311_1_ && ((p_150311_0_ = p_150311_0_.trim()).startsWith("{") || p_150311_0_.startsWith("["))) {
            return p_150311_0_;
        }
        int i2 = JsonToNBT.func_150312_a(p_150311_0_, ':');
        if (i2 == -1) {
            if (p_150311_1_) {
                return p_150311_0_;
            }
            throw new NBTException("Unable to locate name/value separator for string: " + p_150311_0_);
        }
        return p_150311_0_.substring(i2 + 1).trim();
    }

    private static int func_150312_a(String p_150312_0_, char p_150312_1_) {
        boolean flag = true;
        for (int i2 = 0; i2 < p_150312_0_.length(); ++i2) {
            char c0 = p_150312_0_.charAt(i2);
            if (c0 == '\"') {
                if (JsonToNBT.func_179271_b(p_150312_0_, i2)) continue;
                flag = !flag;
                continue;
            }
            if (!flag) continue;
            if (c0 == p_150312_1_) {
                return i2;
            }
            if (c0 != '{' && c0 != '[') continue;
            return -1;
        }
        return -1;
    }

    private static boolean func_179271_b(String p_179271_0_, int p_179271_1_) {
        return p_179271_1_ > 0 && p_179271_0_.charAt(p_179271_1_ - 1) == '\\' && !JsonToNBT.func_179271_b(p_179271_0_, p_179271_1_ - 1);
    }

    static class Primitive
    extends Any {
        private static final Pattern DOUBLE = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
        private static final Pattern FLOAT = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
        private static final Pattern BYTE = Pattern.compile("[-+]?[0-9]+[b|B]");
        private static final Pattern LONG = Pattern.compile("[-+]?[0-9]+[l|L]");
        private static final Pattern SHORT = Pattern.compile("[-+]?[0-9]+[s|S]");
        private static final Pattern INTEGER = Pattern.compile("[-+]?[0-9]+");
        private static final Pattern DOUBLE_UNTYPED = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
        private static final Splitter SPLITTER = Splitter.on(',').omitEmptyStrings();
        protected String jsonValue;

        public Primitive(String p_i45139_1_, String p_i45139_2_) {
            this.json = p_i45139_1_;
            this.jsonValue = p_i45139_2_;
        }

        @Override
        public NBTBase parse() throws NBTException {
            try {
                if (DOUBLE.matcher(this.jsonValue).matches()) {
                    return new NBTTagDouble(Double.parseDouble(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (FLOAT.matcher(this.jsonValue).matches()) {
                    return new NBTTagFloat(Float.parseFloat(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (BYTE.matcher(this.jsonValue).matches()) {
                    return new NBTTagByte(Byte.parseByte(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (LONG.matcher(this.jsonValue).matches()) {
                    return new NBTTagLong(Long.parseLong(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (SHORT.matcher(this.jsonValue).matches()) {
                    return new NBTTagShort(Short.parseShort(this.jsonValue.substring(0, this.jsonValue.length() - 1)));
                }
                if (INTEGER.matcher(this.jsonValue).matches()) {
                    return new NBTTagInt(Integer.parseInt(this.jsonValue));
                }
                if (DOUBLE_UNTYPED.matcher(this.jsonValue).matches()) {
                    return new NBTTagDouble(Double.parseDouble(this.jsonValue));
                }
                if (this.jsonValue.equalsIgnoreCase("true") || this.jsonValue.equalsIgnoreCase("false")) {
                    return new NBTTagByte((byte)(Boolean.parseBoolean(this.jsonValue) ? 1 : 0));
                }
            }
            catch (NumberFormatException var6) {
                this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
                return new NBTTagString(this.jsonValue);
            }
            if (this.jsonValue.startsWith("[") && this.jsonValue.endsWith("]")) {
                String s2 = this.jsonValue.substring(1, this.jsonValue.length() - 1);
                String[] astring = Iterables.toArray(SPLITTER.split(s2), String.class);
                try {
                    int[] aint = new int[astring.length];
                    for (int j2 = 0; j2 < astring.length; ++j2) {
                        aint[j2] = Integer.parseInt(astring[j2].trim());
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
            for (int i2 = 0; i2 < this.jsonValue.length(); ++i2) {
                if (i2 < this.jsonValue.length() - 1 && this.jsonValue.charAt(i2) == '\\' && this.jsonValue.charAt(i2 + 1) == '\\') {
                    stringbuilder.append('\\');
                    ++i2;
                    continue;
                }
                stringbuilder.append(this.jsonValue.charAt(i2));
            }
            return new NBTTagString(stringbuilder.toString());
        }
    }

    static class List
    extends Any {
        protected java.util.List<Any> field_150492_b = Lists.newArrayList();

        public List(String json) {
            this.json = json;
        }

        @Override
        public NBTBase parse() throws NBTException {
            NBTTagList nbttaglist = new NBTTagList();
            for (Any jsontonbt$any : this.field_150492_b) {
                nbttaglist.appendTag(jsontonbt$any.parse());
            }
            return nbttaglist;
        }
    }

    static class Compound
    extends Any {
        protected java.util.List<Any> field_150491_b = Lists.newArrayList();

        public Compound(String p_i45137_1_) {
            this.json = p_i45137_1_;
        }

        @Override
        public NBTBase parse() throws NBTException {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (Any jsontonbt$any : this.field_150491_b) {
                nbttagcompound.setTag(jsontonbt$any.json, jsontonbt$any.parse());
            }
            return nbttagcompound;
        }
    }

    static abstract class Any {
        protected String json;

        Any() {
        }

        public abstract NBTBase parse() throws NBTException;
    }
}

