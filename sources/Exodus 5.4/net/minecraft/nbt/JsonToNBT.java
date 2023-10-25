/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
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
    private static final Pattern field_179273_b;
    private static final Logger logger;

    private static String func_150314_a(String string, boolean bl) throws NBTException {
        int n = JsonToNBT.func_150312_a(string, ':');
        int n2 = JsonToNBT.func_150312_a(string, ',');
        if (bl) {
            if (n == -1) {
                throw new NBTException("Unable to locate name/value separator for string: " + string);
            }
            if (n2 != -1 && n2 < n) {
                throw new NBTException("Name error at: " + string);
            }
        } else if (n == -1 || n > n2) {
            n = -1;
        }
        return JsonToNBT.func_179269_a(string, n);
    }

    private static int func_150312_a(String string, char c) {
        int n = 0;
        boolean bl = true;
        while (n < string.length()) {
            char c2 = string.charAt(n);
            if (c2 == '\"') {
                if (!JsonToNBT.func_179271_b(string, n)) {
                    bl = !bl;
                }
            } else if (bl) {
                if (c2 == c) {
                    return n;
                }
                if (c2 == '{' || c2 == '[') {
                    return -1;
                }
            }
            ++n;
        }
        return -1;
    }

    private static String func_150313_b(String string, boolean bl) throws NBTException {
        if (bl && ((string = string.trim()).startsWith("{") || string.startsWith("["))) {
            return "";
        }
        int n = JsonToNBT.func_150312_a(string, ':');
        if (n == -1) {
            if (bl) {
                return "";
            }
            throw new NBTException("Unable to locate name/value separator for string: " + string);
        }
        return string.substring(0, n).trim();
    }

    private static String func_150311_c(String string, boolean bl) throws NBTException {
        if (bl && ((string = string.trim()).startsWith("{") || string.startsWith("["))) {
            return string;
        }
        int n = JsonToNBT.func_150312_a(string, ':');
        if (n == -1) {
            if (bl) {
                return string;
            }
            throw new NBTException("Unable to locate name/value separator for string: " + string);
        }
        return string.substring(n + 1).trim();
    }

    static Any func_150316_a(String string, String string2) throws NBTException {
        if ((string2 = string2.trim()).startsWith("{")) {
            string2 = string2.substring(1, string2.length() - 1);
            Compound compound = new Compound(string);
            while (string2.length() > 0) {
                char c;
                String string3 = JsonToNBT.func_150314_a(string2, true);
                if (string3.length() > 0) {
                    c = '\u0000';
                    compound.field_150491_b.add(JsonToNBT.func_179270_a(string3, c != '\u0000'));
                }
                if (string2.length() < string3.length() + 1) break;
                c = string2.charAt(string3.length());
                if (c != ',' && c != '{' && c != '}' && c != '[' && c != ']') {
                    throw new NBTException("Unexpected token '" + c + "' at: " + string2.substring(string3.length()));
                }
                string2 = string2.substring(string3.length() + 1);
            }
            return compound;
        }
        if (string2.startsWith("[") && !field_179273_b.matcher(string2).matches()) {
            string2 = string2.substring(1, string2.length() - 1);
            List list = new List(string);
            while (string2.length() > 0) {
                char c;
                String string4 = JsonToNBT.func_150314_a(string2, false);
                if (string4.length() > 0) {
                    c = '\u0001';
                    list.field_150492_b.add(JsonToNBT.func_179270_a(string4, c != '\u0000'));
                }
                if (string2.length() < string4.length() + 1) break;
                c = string2.charAt(string4.length());
                if (c != ',' && c != '{' && c != '}' && c != '[' && c != ']') {
                    throw new NBTException("Unexpected token '" + c + "' at: " + string2.substring(string4.length()));
                }
                string2 = string2.substring(string4.length() + 1);
            }
            return list;
        }
        return new Primitive(string, string2);
    }

    static int func_150310_b(String string) throws NBTException {
        int n = 0;
        boolean bl = false;
        Stack<Character> stack = new Stack<Character>();
        int n2 = 0;
        while (n2 < string.length()) {
            char c = string.charAt(n2);
            if (c == '\"') {
                if (JsonToNBT.func_179271_b(string, n2)) {
                    if (!bl) {
                        throw new NBTException("Illegal use of \\\": " + string);
                    }
                } else {
                    bl = !bl;
                }
            } else if (!bl) {
                if (c != '{' && c != '[') {
                    if (c == '}' && (stack.isEmpty() || ((Character)stack.pop()).charValue() != '{')) {
                        throw new NBTException("Unbalanced curly brackets {}: " + string);
                    }
                    if (c == ']' && (stack.isEmpty() || ((Character)stack.pop()).charValue() != '[')) {
                        throw new NBTException("Unbalanced square brackets []: " + string);
                    }
                } else {
                    if (stack.isEmpty()) {
                        ++n;
                    }
                    stack.push(Character.valueOf(c));
                }
            }
            ++n2;
        }
        if (bl) {
            throw new NBTException("Unbalanced quotation: " + string);
        }
        if (!stack.isEmpty()) {
            throw new NBTException("Unbalanced brackets: " + string);
        }
        if (n == 0 && !string.isEmpty()) {
            n = 1;
        }
        return n;
    }

    private static boolean func_179271_b(String string, int n) {
        return n > 0 && string.charAt(n - 1) == '\\' && !JsonToNBT.func_179271_b(string, n - 1);
    }

    static Any func_179272_a(String ... stringArray) throws NBTException {
        return JsonToNBT.func_150316_a(stringArray[0], stringArray[1]);
    }

    private static Any func_179270_a(String string, boolean bl) throws NBTException {
        String string2 = JsonToNBT.func_150313_b(string, bl);
        String string3 = JsonToNBT.func_150311_c(string, bl);
        return JsonToNBT.func_179272_a(string2, string3);
    }

    public static NBTTagCompound getTagFromJson(String string) throws NBTException {
        if (!(string = string.trim()).startsWith("{")) {
            throw new NBTException("Invalid tag encountered, expected '{' as first char.");
        }
        if (JsonToNBT.func_150310_b(string) != 1) {
            throw new NBTException("Encountered multiple top tags, only one expected");
        }
        return (NBTTagCompound)JsonToNBT.func_150316_a("tag", string).parse();
    }

    static {
        logger = LogManager.getLogger();
        field_179273_b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");
    }

    private static String func_179269_a(String string, int n) throws NBTException {
        Stack<Character> stack = new Stack<Character>();
        int n2 = n + 1;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        int n3 = 0;
        while (n2 < string.length()) {
            char c = string.charAt(n2);
            if (c == '\"') {
                if (JsonToNBT.func_179271_b(string, n2)) {
                    if (!bl) {
                        throw new NBTException("Illegal use of \\\": " + string);
                    }
                } else {
                    boolean bl4 = bl = !bl;
                    if (bl && !bl3) {
                        bl2 = true;
                    }
                    if (!bl) {
                        n3 = n2;
                    }
                }
            } else if (!bl) {
                if (c != '{' && c != '[') {
                    if (c == '}' && (stack.isEmpty() || ((Character)stack.pop()).charValue() != '{')) {
                        throw new NBTException("Unbalanced curly brackets {}: " + string);
                    }
                    if (c == ']' && (stack.isEmpty() || ((Character)stack.pop()).charValue() != '[')) {
                        throw new NBTException("Unbalanced square brackets []: " + string);
                    }
                    if (c == ',' && stack.isEmpty()) {
                        return string.substring(0, n2);
                    }
                } else {
                    stack.push(Character.valueOf(c));
                }
            }
            if (!Character.isWhitespace(c)) {
                if (!bl && bl2 && n3 != n2) {
                    return string.substring(0, n3 + 1);
                }
                bl3 = true;
            }
            ++n2;
        }
        return string.substring(0, n2);
    }

    static class List
    extends Any {
        protected java.util.List<Any> field_150492_b = Lists.newArrayList();

        @Override
        public NBTBase parse() throws NBTException {
            NBTTagList nBTTagList = new NBTTagList();
            for (Any any : this.field_150492_b) {
                nBTTagList.appendTag(any.parse());
            }
            return nBTTagList;
        }

        public List(String string) {
            this.json = string;
        }
    }

    static class Primitive
    extends Any {
        private static final Pattern INTEGER;
        private static final Splitter SPLITTER;
        private static final Pattern DOUBLE_UNTYPED;
        private static final Pattern FLOAT;
        protected String jsonValue;
        private static final Pattern BYTE;
        private static final Pattern DOUBLE;
        private static final Pattern LONG;
        private static final Pattern SHORT;

        public Primitive(String string, String string2) {
            this.json = string;
            this.jsonValue = string2;
        }

        static {
            DOUBLE = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
            FLOAT = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
            BYTE = Pattern.compile("[-+]?[0-9]+[b|B]");
            LONG = Pattern.compile("[-+]?[0-9]+[l|L]");
            SHORT = Pattern.compile("[-+]?[0-9]+[s|S]");
            INTEGER = Pattern.compile("[-+]?[0-9]+");
            DOUBLE_UNTYPED = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            SPLITTER = Splitter.on((char)',').omitEmptyStrings();
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
                    return new NBTTagByte((byte)(Boolean.parseBoolean(this.jsonValue) ? '\u0001' : '\u0000'));
                }
            }
            catch (NumberFormatException numberFormatException) {
                this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
                return new NBTTagString(this.jsonValue);
            }
            if (this.jsonValue.startsWith("[") && this.jsonValue.endsWith("]")) {
                String string = this.jsonValue.substring(1, this.jsonValue.length() - 1);
                String[] stringArray = (String[])Iterables.toArray((Iterable)SPLITTER.split((CharSequence)string), String.class);
                try {
                    int[] nArray = new int[stringArray.length];
                    int n = 0;
                    while (n < stringArray.length) {
                        nArray[n] = Integer.parseInt(stringArray[n].trim());
                        ++n;
                    }
                    return new NBTTagIntArray(nArray);
                }
                catch (NumberFormatException numberFormatException) {
                    return new NBTTagString(this.jsonValue);
                }
            }
            if (this.jsonValue.startsWith("\"") && this.jsonValue.endsWith("\"")) {
                this.jsonValue = this.jsonValue.substring(1, this.jsonValue.length() - 1);
            }
            this.jsonValue = this.jsonValue.replaceAll("\\\\\"", "\"");
            StringBuilder stringBuilder = new StringBuilder();
            int n = 0;
            while (n < this.jsonValue.length()) {
                if (n < this.jsonValue.length() - 1 && this.jsonValue.charAt(n) == '\\' && this.jsonValue.charAt(n + 1) == '\\') {
                    stringBuilder.append('\\');
                    ++n;
                } else {
                    stringBuilder.append(this.jsonValue.charAt(n));
                }
                ++n;
            }
            return new NBTTagString(stringBuilder.toString());
        }
    }

    static abstract class Any {
        protected String json;

        Any() {
        }

        public abstract NBTBase parse() throws NBTException;
    }

    static class Compound
    extends Any {
        protected java.util.List<Any> field_150491_b = Lists.newArrayList();

        public Compound(String string) {
            this.json = string;
        }

        @Override
        public NBTBase parse() throws NBTException {
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            for (Any any : this.field_150491_b) {
                nBTTagCompound.setTag(any.json, any.parse());
            }
            return nBTTagCompound;
        }
    }
}

