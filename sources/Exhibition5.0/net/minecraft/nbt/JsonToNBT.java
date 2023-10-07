// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.nbt;

import com.google.common.collect.Iterables;
import com.google.common.base.Splitter;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import java.util.Stack;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public class JsonToNBT
{
    private static final Logger logger;
    private static final Pattern field_179273_b;
    private static final String __OBFID = "CL_00001232";
    
    public static NBTTagCompound func_180713_a(String p_180713_0_) throws NBTException {
        p_180713_0_ = p_180713_0_.trim();
        if (!p_180713_0_.startsWith("{")) {
            throw new NBTException("Invalid tag encountered, expected '{' as first char.");
        }
        if (func_150310_b(p_180713_0_) != 1) {
            throw new NBTException("Encountered multiple top tags, only one expected");
        }
        return (NBTTagCompound)func_150316_a("tag", p_180713_0_).func_150489_a();
    }
    
    static int func_150310_b(final String p_150310_0_) throws NBTException {
        int var1 = 0;
        boolean var2 = false;
        final Stack var3 = new Stack();
        for (int var4 = 0; var4 < p_150310_0_.length(); ++var4) {
            final char var5 = p_150310_0_.charAt(var4);
            if (var5 == '\"') {
                if (func_179271_b(p_150310_0_, var4)) {
                    if (!var2) {
                        throw new NBTException("Illegal use of \\\": " + p_150310_0_);
                    }
                }
                else {
                    var2 = !var2;
                }
            }
            else if (!var2) {
                if (var5 != '{' && var5 != '[') {
                    if (var5 == '}' && (var3.isEmpty() || var3.pop() != '{')) {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_150310_0_);
                    }
                    if (var5 == ']' && (var3.isEmpty() || var3.pop() != '[')) {
                        throw new NBTException("Unbalanced square brackets []: " + p_150310_0_);
                    }
                }
                else {
                    if (var3.isEmpty()) {
                        ++var1;
                    }
                    var3.push(var5);
                }
            }
        }
        if (var2) {
            throw new NBTException("Unbalanced quotation: " + p_150310_0_);
        }
        if (!var3.isEmpty()) {
            throw new NBTException("Unbalanced brackets: " + p_150310_0_);
        }
        if (var1 == 0 && !p_150310_0_.isEmpty()) {
            var1 = 1;
        }
        return var1;
    }
    
    static Any func_179272_a(final String... p_179272_0_) throws NBTException {
        return func_150316_a(p_179272_0_[0], p_179272_0_[1]);
    }
    
    static Any func_150316_a(final String p_150316_0_, String p_150316_1_) throws NBTException {
        p_150316_1_ = p_150316_1_.trim();
        if (p_150316_1_.startsWith("{")) {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            final Compound var5 = new Compound(p_150316_0_);
            while (p_150316_1_.length() > 0) {
                final String var6 = func_150314_a(p_150316_1_, true);
                if (var6.length() > 0) {
                    final boolean var7 = false;
                    var5.field_150491_b.add(func_179270_a(var6, var7));
                }
                if (p_150316_1_.length() < var6.length() + 1) {
                    break;
                }
                final char var8 = p_150316_1_.charAt(var6.length());
                if (var8 != ',' && var8 != '{' && var8 != '}' && var8 != '[' && var8 != ']') {
                    throw new NBTException("Unexpected token '" + var8 + "' at: " + p_150316_1_.substring(var6.length()));
                }
                p_150316_1_ = p_150316_1_.substring(var6.length() + 1);
            }
            return var5;
        }
        if (p_150316_1_.startsWith("[") && !JsonToNBT.field_179273_b.matcher(p_150316_1_).matches()) {
            p_150316_1_ = p_150316_1_.substring(1, p_150316_1_.length() - 1);
            final List var9 = new List(p_150316_0_);
            while (p_150316_1_.length() > 0) {
                final String var6 = func_150314_a(p_150316_1_, false);
                if (var6.length() > 0) {
                    final boolean var7 = true;
                    var9.field_150492_b.add(func_179270_a(var6, var7));
                }
                if (p_150316_1_.length() < var6.length() + 1) {
                    break;
                }
                final char var8 = p_150316_1_.charAt(var6.length());
                if (var8 != ',' && var8 != '{' && var8 != '}' && var8 != '[' && var8 != ']') {
                    throw new NBTException("Unexpected token '" + var8 + "' at: " + p_150316_1_.substring(var6.length()));
                }
                p_150316_1_ = p_150316_1_.substring(var6.length() + 1);
            }
            return var9;
        }
        return new Primitive(p_150316_0_, p_150316_1_);
    }
    
    private static Any func_179270_a(final String p_179270_0_, final boolean p_179270_1_) throws NBTException {
        final String var2 = func_150313_b(p_179270_0_, p_179270_1_);
        final String var3 = func_150311_c(p_179270_0_, p_179270_1_);
        return func_179272_a(var2, var3);
    }
    
    private static String func_150314_a(final String p_150314_0_, final boolean p_150314_1_) throws NBTException {
        int var2 = func_150312_a(p_150314_0_, ':');
        final int var3 = func_150312_a(p_150314_0_, ',');
        if (p_150314_1_) {
            if (var2 == -1) {
                throw new NBTException("Unable to locate name/value separator for string: " + p_150314_0_);
            }
            if (var3 != -1 && var3 < var2) {
                throw new NBTException("Name error at: " + p_150314_0_);
            }
        }
        else if (var2 == -1 || var2 > var3) {
            var2 = -1;
        }
        return func_179269_a(p_150314_0_, var2);
    }
    
    private static String func_179269_a(final String p_179269_0_, final int p_179269_1_) throws NBTException {
        final Stack var2 = new Stack();
        int var3 = p_179269_1_ + 1;
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        int var7 = 0;
        while (var3 < p_179269_0_.length()) {
            final char var8 = p_179269_0_.charAt(var3);
            if (var8 == '\"') {
                if (func_179271_b(p_179269_0_, var3)) {
                    if (!var4) {
                        throw new NBTException("Illegal use of \\\": " + p_179269_0_);
                    }
                }
                else {
                    var4 = !var4;
                    if (var4 && !var6) {
                        var5 = true;
                    }
                    if (!var4) {
                        var7 = var3;
                    }
                }
            }
            else if (!var4) {
                if (var8 != '{' && var8 != '[') {
                    if (var8 == '}' && (var2.isEmpty() || var2.pop() != '{')) {
                        throw new NBTException("Unbalanced curly brackets {}: " + p_179269_0_);
                    }
                    if (var8 == ']' && (var2.isEmpty() || var2.pop() != '[')) {
                        throw new NBTException("Unbalanced square brackets []: " + p_179269_0_);
                    }
                    if (var8 == ',' && var2.isEmpty()) {
                        return p_179269_0_.substring(0, var3);
                    }
                }
                else {
                    var2.push(var8);
                }
            }
            if (!Character.isWhitespace(var8)) {
                if (!var4 && var5 && var7 != var3) {
                    return p_179269_0_.substring(0, var7 + 1);
                }
                var6 = true;
            }
            ++var3;
        }
        return p_179269_0_.substring(0, var3);
    }
    
    private static String func_150313_b(String p_150313_0_, final boolean p_150313_1_) throws NBTException {
        if (p_150313_1_) {
            p_150313_0_ = p_150313_0_.trim();
            if (p_150313_0_.startsWith("{") || p_150313_0_.startsWith("[")) {
                return "";
            }
        }
        final int var2 = func_150312_a(p_150313_0_, ':');
        if (var2 != -1) {
            return p_150313_0_.substring(0, var2).trim();
        }
        if (p_150313_1_) {
            return "";
        }
        throw new NBTException("Unable to locate name/value separator for string: " + p_150313_0_);
    }
    
    private static String func_150311_c(String p_150311_0_, final boolean p_150311_1_) throws NBTException {
        if (p_150311_1_) {
            p_150311_0_ = p_150311_0_.trim();
            if (p_150311_0_.startsWith("{") || p_150311_0_.startsWith("[")) {
                return p_150311_0_;
            }
        }
        final int var2 = func_150312_a(p_150311_0_, ':');
        if (var2 != -1) {
            return p_150311_0_.substring(var2 + 1).trim();
        }
        if (p_150311_1_) {
            return p_150311_0_;
        }
        throw new NBTException("Unable to locate name/value separator for string: " + p_150311_0_);
    }
    
    private static int func_150312_a(final String p_150312_0_, final char p_150312_1_) {
        int var2 = 0;
        boolean var3 = true;
        while (var2 < p_150312_0_.length()) {
            final char var4 = p_150312_0_.charAt(var2);
            if (var4 == '\"') {
                if (!func_179271_b(p_150312_0_, var2)) {
                    var3 = !var3;
                }
            }
            else if (var3) {
                if (var4 == p_150312_1_) {
                    return var2;
                }
                if (var4 == '{' || var4 == '[') {
                    return -1;
                }
            }
            ++var2;
        }
        return -1;
    }
    
    private static boolean func_179271_b(final String p_179271_0_, final int p_179271_1_) {
        return p_179271_1_ > 0 && p_179271_0_.charAt(p_179271_1_ - 1) == '\\' && !func_179271_b(p_179271_0_, p_179271_1_ - 1);
    }
    
    static {
        logger = LogManager.getLogger();
        field_179273_b = Pattern.compile("\\[[-+\\d|,\\s]+\\]");
    }
    
    abstract static class Any
    {
        protected String field_150490_a;
        private static final String __OBFID = "CL_00001233";
        
        public abstract NBTBase func_150489_a();
    }
    
    static class Compound extends Any
    {
        protected java.util.List field_150491_b;
        private static final String __OBFID = "CL_00001234";
        
        public Compound(final String p_i45137_1_) {
            this.field_150491_b = Lists.newArrayList();
            this.field_150490_a = p_i45137_1_;
        }
        
        @Override
        public NBTBase func_150489_a() {
            final NBTTagCompound var1 = new NBTTagCompound();
            for (final Any var3 : this.field_150491_b) {
                var1.setTag(var3.field_150490_a, var3.func_150489_a());
            }
            return var1;
        }
    }
    
    static class List extends Any
    {
        protected java.util.List field_150492_b;
        private static final String __OBFID = "CL_00001235";
        
        public List(final String p_i45138_1_) {
            this.field_150492_b = Lists.newArrayList();
            this.field_150490_a = p_i45138_1_;
        }
        
        @Override
        public NBTBase func_150489_a() {
            final NBTTagList var1 = new NBTTagList();
            for (final Any var3 : this.field_150492_b) {
                var1.appendTag(var3.func_150489_a());
            }
            return var1;
        }
    }
    
    static class Primitive extends Any
    {
        private static final Pattern field_179265_c;
        private static final Pattern field_179263_d;
        private static final Pattern field_179264_e;
        private static final Pattern field_179261_f;
        private static final Pattern field_179262_g;
        private static final Pattern field_179267_h;
        private static final Pattern field_179268_i;
        private static final Splitter field_179266_j;
        protected String field_150493_b;
        private static final String __OBFID = "CL_00001236";
        
        public Primitive(final String p_i45139_1_, final String p_i45139_2_) {
            this.field_150490_a = p_i45139_1_;
            this.field_150493_b = p_i45139_2_;
        }
        
        @Override
        public NBTBase func_150489_a() {
            try {
                if (Primitive.field_179265_c.matcher(this.field_150493_b).matches()) {
                    return new NBTTagDouble(Double.parseDouble(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                if (Primitive.field_179263_d.matcher(this.field_150493_b).matches()) {
                    return new NBTTagFloat(Float.parseFloat(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                if (Primitive.field_179264_e.matcher(this.field_150493_b).matches()) {
                    return new NBTTagByte(Byte.parseByte(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                if (Primitive.field_179261_f.matcher(this.field_150493_b).matches()) {
                    return new NBTTagLong(Long.parseLong(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                if (Primitive.field_179262_g.matcher(this.field_150493_b).matches()) {
                    return new NBTTagShort(Short.parseShort(this.field_150493_b.substring(0, this.field_150493_b.length() - 1)));
                }
                if (Primitive.field_179267_h.matcher(this.field_150493_b).matches()) {
                    return new NBTTagInt(Integer.parseInt(this.field_150493_b));
                }
                if (Primitive.field_179268_i.matcher(this.field_150493_b).matches()) {
                    return new NBTTagDouble(Double.parseDouble(this.field_150493_b));
                }
                if (this.field_150493_b.equalsIgnoreCase("true") || this.field_150493_b.equalsIgnoreCase("false")) {
                    return new NBTTagByte((byte)(Boolean.parseBoolean(this.field_150493_b) ? 1 : 0));
                }
            }
            catch (NumberFormatException var13) {
                this.field_150493_b = this.field_150493_b.replaceAll("\\\\\"", "\"");
                return new NBTTagString(this.field_150493_b);
            }
            if (this.field_150493_b.startsWith("[") && this.field_150493_b.endsWith("]")) {
                final String var7 = this.field_150493_b.substring(1, this.field_150493_b.length() - 1);
                final String[] var8 = (String[])Iterables.toArray(Primitive.field_179266_j.split((CharSequence)var7), (Class)String.class);
                try {
                    final int[] var9 = new int[var8.length];
                    for (int var10 = 0; var10 < var8.length; ++var10) {
                        var9[var10] = Integer.parseInt(var8[var10].trim());
                    }
                    return new NBTTagIntArray(var9);
                }
                catch (NumberFormatException var14) {
                    return new NBTTagString(this.field_150493_b);
                }
            }
            if (this.field_150493_b.startsWith("\"") && this.field_150493_b.endsWith("\"")) {
                this.field_150493_b = this.field_150493_b.substring(1, this.field_150493_b.length() - 1);
            }
            this.field_150493_b = this.field_150493_b.replaceAll("\\\\\"", "\"");
            final StringBuilder var11 = new StringBuilder();
            for (int var12 = 0; var12 < this.field_150493_b.length(); ++var12) {
                if (var12 < this.field_150493_b.length() - 1 && this.field_150493_b.charAt(var12) == '\\' && this.field_150493_b.charAt(var12 + 1) == '\\') {
                    var11.append('\\');
                    ++var12;
                }
                else {
                    var11.append(this.field_150493_b.charAt(var12));
                }
            }
            return new NBTTagString(var11.toString());
        }
        
        static {
            field_179265_c = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[d|D]");
            field_179263_d = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+[f|F]");
            field_179264_e = Pattern.compile("[-+]?[0-9]+[b|B]");
            field_179261_f = Pattern.compile("[-+]?[0-9]+[l|L]");
            field_179262_g = Pattern.compile("[-+]?[0-9]+[s|S]");
            field_179267_h = Pattern.compile("[-+]?[0-9]+");
            field_179268_i = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            field_179266_j = Splitter.on(',').omitEmptyStrings();
        }
    }
}
