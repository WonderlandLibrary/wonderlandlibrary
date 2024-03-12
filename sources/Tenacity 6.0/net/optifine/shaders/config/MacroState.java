// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

import java.util.Arrays;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionResolver;
import net.optifine.expr.ParseException;
import net.optifine.expr.IExpressionFloat;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.ExpressionParser;
import net.minecraft.src.Config;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.Deque;

public class MacroState
{
    private boolean active;
    private Deque<Boolean> dequeState;
    private Deque<Boolean> dequeResolved;
    private Map<String, String> mapMacroValues;
    private static final Pattern PATTERN_DIRECTIVE;
    private static final Pattern PATTERN_DEFINED;
    private static final Pattern PATTERN_DEFINED_FUNC;
    private static final Pattern PATTERN_MACRO;
    private static final String DEFINE = "define";
    private static final String UNDEF = "undef";
    private static final String IFDEF = "ifdef";
    private static final String IFNDEF = "ifndef";
    private static final String IF = "if";
    private static final String ELSE = "else";
    private static final String ELIF = "elif";
    private static final String ENDIF = "endif";
    private static final List<String> MACRO_NAMES;
    
    public MacroState() {
        this.active = true;
        this.dequeState = new ArrayDeque<Boolean>();
        this.dequeResolved = new ArrayDeque<Boolean>();
        this.mapMacroValues = new HashMap<String, String>();
    }
    
    public boolean processLine(final String line) {
        final Matcher matcher = MacroState.PATTERN_DIRECTIVE.matcher(line);
        if (!matcher.matches()) {
            return this.active;
        }
        final String s = matcher.group(1);
        String s2 = matcher.group(2);
        final int i = s2.indexOf("//");
        if (i >= 0) {
            s2 = s2.substring(0, i);
        }
        final boolean flag = this.active;
        this.processMacro(s, s2);
        this.active = !this.dequeState.contains(Boolean.FALSE);
        return this.active || flag;
    }
    
    public static boolean isMacroLine(final String line) {
        final Matcher matcher = MacroState.PATTERN_DIRECTIVE.matcher(line);
        if (!matcher.matches()) {
            return false;
        }
        final String s = matcher.group(1);
        return MacroState.MACRO_NAMES.contains(s);
    }
    
    private void processMacro(final String name, final String param) {
        final StringTokenizer stringtokenizer = new StringTokenizer(param, " \t");
        final String s = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken() : "";
        final String s2 = stringtokenizer.hasMoreTokens() ? stringtokenizer.nextToken("").trim() : "";
        if (name.equals("define")) {
            this.mapMacroValues.put(s, s2);
        }
        else if (name.equals("undef")) {
            this.mapMacroValues.remove(s);
        }
        else if (name.equals("ifdef")) {
            final boolean flag6 = this.mapMacroValues.containsKey(s);
            this.dequeState.add(flag6);
            this.dequeResolved.add(flag6);
        }
        else if (name.equals("ifndef")) {
            final boolean flag7 = !this.mapMacroValues.containsKey(s);
            this.dequeState.add(flag7);
            this.dequeResolved.add(flag7);
        }
        else if (name.equals("if")) {
            final boolean flag8 = this.eval(param);
            this.dequeState.add(flag8);
            this.dequeResolved.add(flag8);
        }
        else if (!this.dequeState.isEmpty()) {
            if (name.equals("elif")) {
                final boolean flag9 = this.dequeState.removeLast();
                final boolean flag10 = this.dequeResolved.removeLast();
                if (flag10) {
                    this.dequeState.add(false);
                    this.dequeResolved.add(flag10);
                }
                else {
                    final boolean flag11 = this.eval(param);
                    this.dequeState.add(flag11);
                    this.dequeResolved.add(flag11);
                }
            }
            else if (name.equals("else")) {
                final boolean flag12 = this.dequeState.removeLast();
                final boolean flag13 = this.dequeResolved.removeLast();
                final boolean flag14 = !flag13;
                this.dequeState.add(flag14);
                this.dequeResolved.add(true);
            }
            else if (name.equals("endif")) {
                this.dequeState.removeLast();
                this.dequeResolved.removeLast();
            }
        }
    }
    
    private boolean eval(String str) {
        final Matcher matcher = MacroState.PATTERN_DEFINED.matcher(str);
        str = matcher.replaceAll("defined_$1");
        final Matcher matcher2 = MacroState.PATTERN_DEFINED_FUNC.matcher(str);
        str = matcher2.replaceAll("defined_$1");
        boolean flag = false;
        int i = 0;
        while (true) {
            flag = false;
            final Matcher matcher3 = MacroState.PATTERN_MACRO.matcher(str);
            while (matcher3.find()) {
                final String s = matcher3.group();
                if (s.length() > 0) {
                    final char c0 = s.charAt(0);
                    if ((Character.isLetter(c0) || c0 == '_') && this.mapMacroValues.containsKey(s)) {
                        String s2 = this.mapMacroValues.get(s);
                        if (s2 == null) {
                            s2 = "1";
                        }
                        final int j = matcher3.start();
                        final int k = matcher3.end();
                        str = str.substring(0, j) + " " + s2 + " " + str.substring(k);
                        flag = true;
                        ++i;
                        break;
                    }
                    continue;
                }
            }
            if (flag && i < 100) {
                continue;
            }
            break;
        }
        if (i >= 100) {
            Config.warn("Too many iterations: " + i + ", when resolving: " + str);
            return true;
        }
        try {
            final IExpressionResolver iexpressionresolver = new MacroExpressionResolver(this.mapMacroValues);
            final ExpressionParser expressionparser = new ExpressionParser(iexpressionresolver);
            final IExpression iexpression = expressionparser.parse(str);
            if (iexpression.getExpressionType() == ExpressionType.BOOL) {
                final IExpressionBool iexpressionbool = (IExpressionBool)iexpression;
                final boolean flag2 = iexpressionbool.eval();
                return flag2;
            }
            if (iexpression.getExpressionType() == ExpressionType.FLOAT) {
                final IExpressionFloat iexpressionfloat = (IExpressionFloat)iexpression;
                final float f = iexpressionfloat.eval();
                final boolean flag3 = f != 0.0f;
                return flag3;
            }
            throw new ParseException("Not a boolean or float expression: " + iexpression.getExpressionType());
        }
        catch (ParseException parseexception) {
            Config.warn("Invalid macro expression: " + str);
            Config.warn("Error: " + parseexception.getMessage());
            return false;
        }
    }
    
    static {
        PATTERN_DIRECTIVE = Pattern.compile("\\s*#\\s*(\\w+)\\s*(.*)");
        PATTERN_DEFINED = Pattern.compile("defined\\s+(\\w+)");
        PATTERN_DEFINED_FUNC = Pattern.compile("defined\\s*\\(\\s*(\\w+)\\s*\\)");
        PATTERN_MACRO = Pattern.compile("(\\w+)");
        MACRO_NAMES = Arrays.asList("define", "undef", "ifdef", "ifndef", "if", "else", "elif", "endif");
    }
}
