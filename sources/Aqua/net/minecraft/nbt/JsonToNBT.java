package net.minecraft.nbt;

import java.util.Stack;
import java.util.regex.Pattern;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonToNBT {
    private static final Logger logger = LogManager.getLogger();
    private static final Pattern field_179273_b = Pattern.compile((String)"\\[[-+\\d|,\\s]+\\]");

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
        int i = 0;
        boolean flag = false;
        Stack stack = new Stack();
        for (int j = 0; j < p_150310_0_.length(); ++j) {
            char c0 = p_150310_0_.charAt(j);
            if (c0 == '\"') {
                if (JsonToNBT.func_179271_b(p_150310_0_, j)) {
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
                ++i;
            }
            stack.push((Object)Character.valueOf((char)c0));
        }
        if (flag) {
            throw new NBTException("Unbalanced quotation: " + p_150310_0_);
        }
        if (!stack.isEmpty()) {
            throw new NBTException("Unbalanced brackets: " + p_150310_0_);
        }
        if (i == 0 && !p_150310_0_.isEmpty()) {
            i = 1;
        }
        return i;
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
                    jsontonbt$compound.field_150491_b.add((Object)JsonToNBT.func_179270_a(s1, flag1));
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
        if (p_150316_1_.startsWith("[") && !field_179273_b.matcher((CharSequence)p_150316_1_).matches()) {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            List jsontonbt$list = new List(p_150316_0_);
            while (p_150316_1_.length() > 0) {
                String s = JsonToNBT.func_150314_a(p_150316_1_, false);
                if (s.length() > 0) {
                    boolean flag = true;
                    jsontonbt$list.field_150492_b.add((Object)JsonToNBT.func_179270_a(s, flag));
                }
                if (p_150316_1_.length() < s.length() + 1) break;
                char c0 = p_150316_1_.charAt(s.length());
                if (c0 != ',' && c0 != '{' && c0 != '}' && c0 != '[' && c0 != ']') {
                    throw new NBTException("Unexpected token '" + c0 + "' at: " + p_150316_1_.substring(s.length()));
                }
                p_150316_1_ = p_150316_1_.substring(s.length() + 1);
            }
            return jsontonbt$list;
        }
        return new Primitive(p_150316_0_, p_150316_1_);
    }

    private static Any func_179270_a(String p_179270_0_, boolean p_179270_1_) throws NBTException {
        String s = JsonToNBT.func_150313_b(p_179270_0_, p_179270_1_);
        String s1 = JsonToNBT.func_150311_c(p_179270_0_, p_179270_1_);
        return JsonToNBT.func_179272_a(s, s1);
    }

    private static String func_150314_a(String p_150314_0_, boolean p_150314_1_) throws NBTException {
        int i = JsonToNBT.func_150312_a(p_150314_0_, ':');
        int j = JsonToNBT.func_150312_a(p_150314_0_, ',');
        if (p_150314_1_) {
            if (i == -1) {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150314_0_);
            }
            if (j != -1 && j < i) {
                throw new NBTException("Name error at: " + p_150314_0_);
            }
        } else if (i == -1 || i > j) {
            i = -1;
        }
        return JsonToNBT.func_179269_a(p_150314_0_, i);
    }

    private static String func_179269_a(String p_179269_0_, int p_179269_1_) throws NBTException {
        int i;
        Stack stack = new Stack();
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        int j = 0;
        for (i = p_179269_1_ + 1; i < p_179269_0_.length(); ++i) {
            char c0 = p_179269_0_.charAt(i);
            if (c0 == '\"') {
                if (JsonToNBT.func_179271_b(p_179269_0_, i)) {
                    if (!flag) {
                        throw new NBTException("Illegal use of \\\": " + p_179269_0_);
                    }
                } else {
                    boolean bl = flag = !flag;
                    if (flag && !flag2) {
                        flag1 = true;
                    }
                    if (!flag) {
                        j = i;
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
                        return p_179269_0_.substring(0, i);
                    }
                } else {
                    stack.push((Object)Character.valueOf((char)c0));
                }
            }
            if (Character.isWhitespace((char)c0)) continue;
            if (!flag && flag1 && j != i) {
                return p_179269_0_.substring(0, j + 1);
            }
            flag2 = true;
        }
        return p_179269_0_.substring(0, i);
    }

    private static String func_150313_b(String p_150313_0_, boolean p_150313_1_) throws NBTException {
        if (p_150313_1_ && ((p_150313_0_ = p_150313_0_.trim()).startsWith("{") || p_150313_0_.startsWith("["))) {
            return "";
        }
        int i = JsonToNBT.func_150312_a(p_150313_0_, ':');
        if (i == -1) {
            if (p_150313_1_) {
                return "";
            }
            throw new NBTException("Unable to locate name/value separator for string: " + p_150313_0_);
        }
        return p_150313_0_.substring(0, i).trim();
    }

    private static String func_150311_c(String p_150311_0_, boolean p_150311_1_) throws NBTException {
        if (p_150311_1_ && ((p_150311_0_ = p_150311_0_.trim()).startsWith("{") || p_150311_0_.startsWith("["))) {
            return p_150311_0_;
        }
        int i = JsonToNBT.func_150312_a(p_150311_0_, ':');
        if (i == -1) {
            if (p_150311_1_) {
                return p_150311_0_;
            }
            throw new NBTException("Unable to locate name/value separator for string: " + p_150311_0_);
        }
        return p_150311_0_.substring(i + 1).trim();
    }

    private static int func_150312_a(String p_150312_0_, char p_150312_1_) {
        boolean flag = true;
        for (int i = 0; i < p_150312_0_.length(); ++i) {
            char c0 = p_150312_0_.charAt(i);
            if (c0 == '\"') {
                if (JsonToNBT.func_179271_b(p_150312_0_, i)) continue;
                flag = !flag;
                continue;
            }
            if (!flag) continue;
            if (c0 == p_150312_1_) {
                return i;
            }
            if (c0 != '{' && c0 != '[') continue;
            return -1;
        }
        return -1;
    }

    private static boolean func_179271_b(String p_179271_0_, int p_179271_1_) {
        return p_179271_1_ > 0 && p_179271_0_.charAt(p_179271_1_ - 1) == '\\' && !JsonToNBT.func_179271_b(p_179271_0_, p_179271_1_ - 1);
    }
}
