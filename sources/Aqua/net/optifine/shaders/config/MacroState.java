package net.optifine.shaders.config;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.src.Config;
import net.optifine.expr.ExpressionParser;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IExpressionResolver;
import net.optifine.expr.ParseException;
import net.optifine.shaders.config.MacroExpressionResolver;

public class MacroState {
    private boolean active = true;
    private Deque<Boolean> dequeState = new ArrayDeque();
    private Deque<Boolean> dequeResolved = new ArrayDeque();
    private Map<String, String> mapMacroValues = new HashMap();
    private static final Pattern PATTERN_DIRECTIVE = Pattern.compile((String)"\\s*#\\s*(\\w+)\\s*(.*)");
    private static final Pattern PATTERN_DEFINED = Pattern.compile((String)"defined\\s+(\\w+)");
    private static final Pattern PATTERN_DEFINED_FUNC = Pattern.compile((String)"defined\\s*\\(\\s*(\\w+)\\s*\\)");
    private static final Pattern PATTERN_MACRO = Pattern.compile((String)"(\\w+)");
    private static final String DEFINE = "define";
    private static final String UNDEF = "undef";
    private static final String IFDEF = "ifdef";
    private static final String IFNDEF = "ifndef";
    private static final String IF = "if";
    private static final String ELSE = "else";
    private static final String ELIF = "elif";
    private static final String ENDIF = "endif";
    private static final List<String> MACRO_NAMES = Arrays.asList((Object[])new String[]{"define", "undef", "ifdef", "ifndef", "if", "else", "elif", "endif"});

    public boolean processLine(String line) {
        Matcher matcher = PATTERN_DIRECTIVE.matcher((CharSequence)line);
        if (!matcher.matches()) {
            return this.active;
        }
        String s = matcher.group(1);
        String s1 = matcher.group(2);
        int i = s1.indexOf("//");
        if (i >= 0) {
            s1 = s1.substring(0, i);
        }
        boolean flag = this.active;
        this.processMacro(s, s1);
        this.active = !this.dequeState.contains((Object)Boolean.FALSE);
        return this.active || flag;
    }

    public static boolean isMacroLine(String line) {
        Matcher matcher = PATTERN_DIRECTIVE.matcher((CharSequence)line);
        if (!matcher.matches()) {
            return false;
        }
        String s = matcher.group(1);
        return MACRO_NAMES.contains((Object)s);
    }

    private void processMacro(String name, String param) {
        String s1;
        StringTokenizer stringtokenizer = new StringTokenizer(param, " \t");
        String s = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken() : "";
        String string = s1 = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken("").trim() : "";
        if (name.equals((Object)DEFINE)) {
            this.mapMacroValues.put((Object)s, (Object)s1);
        } else if (name.equals((Object)UNDEF)) {
            this.mapMacroValues.remove((Object)s);
        } else if (name.equals((Object)IFDEF)) {
            boolean flag6 = this.mapMacroValues.containsKey((Object)s);
            this.dequeState.add((Object)flag6);
            this.dequeResolved.add((Object)flag6);
        } else if (name.equals((Object)IFNDEF)) {
            boolean flag5 = !this.mapMacroValues.containsKey((Object)s);
            this.dequeState.add((Object)flag5);
            this.dequeResolved.add((Object)flag5);
        } else if (name.equals((Object)IF)) {
            boolean flag4 = this.eval(param);
            this.dequeState.add((Object)flag4);
            this.dequeResolved.add((Object)flag4);
        } else if (!this.dequeState.isEmpty()) {
            if (name.equals((Object)ELIF)) {
                boolean flag3 = (Boolean)this.dequeState.removeLast();
                boolean flag7 = (Boolean)this.dequeResolved.removeLast();
                if (flag7) {
                    this.dequeState.add((Object)false);
                    this.dequeResolved.add((Object)flag7);
                } else {
                    boolean flag8 = this.eval(param);
                    this.dequeState.add((Object)flag8);
                    this.dequeResolved.add((Object)flag8);
                }
            } else if (name.equals((Object)ELSE)) {
                boolean flag = (Boolean)this.dequeState.removeLast();
                boolean flag1 = (Boolean)this.dequeResolved.removeLast();
                boolean flag2 = !flag1;
                this.dequeState.add((Object)flag2);
                this.dequeResolved.add((Object)true);
            } else if (name.equals((Object)ENDIF)) {
                this.dequeState.removeLast();
                this.dequeResolved.removeLast();
            }
        }
    }

    private boolean eval(String str) {
        Matcher matcher = PATTERN_DEFINED.matcher((CharSequence)str);
        str = matcher.replaceAll("defined_$1");
        Matcher matcher1 = PATTERN_DEFINED_FUNC.matcher((CharSequence)str);
        str = matcher1.replaceAll("defined_$1");
        boolean flag = false;
        int i = 0;
        block2: do {
            flag = false;
            Matcher matcher2 = PATTERN_MACRO.matcher((CharSequence)str);
            while (matcher2.find()) {
                char c0;
                String s = matcher2.group();
                if (s.length() <= 0 || !Character.isLetter((char)(c0 = s.charAt(0))) && c0 != '_' || !this.mapMacroValues.containsKey((Object)s)) continue;
                String s1 = (String)this.mapMacroValues.get((Object)s);
                if (s1 == null) {
                    s1 = "1";
                }
                int j = matcher2.start();
                int k = matcher2.end();
                str = str.substring(0, j) + " " + s1 + " " + str.substring(k);
                flag = true;
                ++i;
                continue block2;
            }
        } while (flag && i < 100);
        if (i >= 100) {
            Config.warn((String)("Too many iterations: " + i + ", when resolving: " + str));
            return true;
        }
        try {
            MacroExpressionResolver iexpressionresolver = new MacroExpressionResolver(this.mapMacroValues);
            ExpressionParser expressionparser = new ExpressionParser((IExpressionResolver)iexpressionresolver);
            IExpression iexpression = expressionparser.parse(str);
            if (iexpression.getExpressionType() == ExpressionType.BOOL) {
                IExpressionBool iexpressionbool = (IExpressionBool)iexpression;
                boolean flag1 = iexpressionbool.eval();
                return flag1;
            }
            if (iexpression.getExpressionType() == ExpressionType.FLOAT) {
                IExpressionFloat iexpressionfloat = (IExpressionFloat)iexpression;
                float f = iexpressionfloat.eval();
                boolean flag2 = f != 0.0f;
                return flag2;
            }
            throw new ParseException("Not a boolean or float expression: " + iexpression.getExpressionType());
        }
        catch (ParseException parseexception) {
            Config.warn((String)("Invalid macro expression: " + str));
            Config.warn((String)("Error: " + parseexception.getMessage()));
            return false;
        }
    }
}
