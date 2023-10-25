/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrMatcher;

public class StrSubstitutor {
    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    private static final int BUF_SIZE = 256;
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private StrMatcher suffixMatcher;
    private StrLookup variableResolver;
    private boolean enableSubstitutionInVariables;

    public StrSubstitutor() {
        this(null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map<String, String> valueMap) {
        this((StrLookup)new MapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix) {
        this((StrLookup)new MapLookup(valueMap), prefix, suffix, '$');
    }

    public StrSubstitutor(Map<String, String> valueMap, String prefix, String suffix, char escape) {
        this((StrLookup)new MapLookup(valueMap), prefix, suffix, escape);
    }

    public StrSubstitutor(StrLookup variableResolver) {
        this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(StrLookup variableResolver, String prefix, String suffix, char escape) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefix(prefix);
        this.setVariableSuffix(suffix);
        this.setEscapeChar(escape);
    }

    public StrSubstitutor(StrLookup variableResolver, StrMatcher prefixMatcher, StrMatcher suffixMatcher, char escape) {
        this.setVariableResolver(variableResolver);
        this.setVariablePrefixMatcher(prefixMatcher);
        this.setVariableSuffixMatcher(suffixMatcher);
        this.setEscapeChar(escape);
    }

    public static String replace(Object source, Map<String, String> valueMap) {
        return new StrSubstitutor(valueMap).replace(source);
    }

    public static String replace(Object source, Map<String, String> valueMap, String prefix, String suffix) {
        return new StrSubstitutor(valueMap, prefix, suffix).replace(source);
    }

    public static String replace(Object source, Properties valueProperties) {
        if (valueProperties == null) {
            return source.toString();
        }
        HashMap<String, String> valueMap = new HashMap<String, String>();
        Enumeration<?> propNames = valueProperties.propertyNames();
        while (propNames.hasMoreElements()) {
            String propName = (String)propNames.nextElement();
            String propValue = valueProperties.getProperty(propName);
            valueMap.put(propName, propValue);
        }
        return StrSubstitutor.replace(source, valueMap);
    }

    public String replace(String source) {
        return this.replace(null, source);
    }

    public String replace(LogEvent event, String source) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(source);
        if (!this.substitute(event, buf, 0, source.length())) {
            return source;
        }
        return buf.toString();
    }

    public String replace(String source, int offset, int length) {
        return this.replace(null, source, offset, length);
    }

    public String replace(LogEvent event, String source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        if (!this.substitute(event, buf, 0, length)) {
            return source.substring(offset, offset + length);
        }
        return buf.toString();
    }

    public String replace(char[] source) {
        return this.replace((LogEvent)null, source);
    }

    public String replace(LogEvent event, char[] source) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(source.length).append(source);
        this.substitute(event, buf, 0, source.length);
        return buf.toString();
    }

    public String replace(char[] source, int offset, int length) {
        return this.replace(null, source, offset, length);
    }

    public String replace(LogEvent event, char[] source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        this.substitute(event, buf, 0, length);
        return buf.toString();
    }

    public String replace(StringBuffer source) {
        return this.replace(null, source);
    }

    public String replace(LogEvent event, StringBuffer source) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(source.length()).append(source);
        this.substitute(event, buf, 0, buf.length());
        return buf.toString();
    }

    public String replace(StringBuffer source, int offset, int length) {
        return this.replace(null, source, offset, length);
    }

    public String replace(LogEvent event, StringBuffer source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        this.substitute(event, buf, 0, length);
        return buf.toString();
    }

    public String replace(StringBuilder source) {
        return this.replace(null, source);
    }

    public String replace(LogEvent event, StringBuilder source) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(source.length()).append((CharSequence)source);
        this.substitute(event, buf, 0, buf.length());
        return buf.toString();
    }

    public String replace(StringBuilder source, int offset, int length) {
        return this.replace(null, source, offset, length);
    }

    public String replace(LogEvent event, StringBuilder source, int offset, int length) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        this.substitute(event, buf, 0, length);
        return buf.toString();
    }

    public String replace(Object source) {
        return this.replace(null, source);
    }

    public String replace(LogEvent event, Object source) {
        if (source == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder().append(source);
        this.substitute(event, buf, 0, buf.length());
        return buf.toString();
    }

    public boolean replaceIn(StringBuffer source) {
        if (source == null) {
            return false;
        }
        return this.replaceIn(source, 0, source.length());
    }

    public boolean replaceIn(StringBuffer source, int offset, int length) {
        return this.replaceIn(null, source, offset, length);
    }

    public boolean replaceIn(LogEvent event, StringBuffer source, int offset, int length) {
        if (source == null) {
            return false;
        }
        StringBuilder buf = new StringBuilder(length).append(source, offset, length);
        if (!this.substitute(event, buf, 0, length)) {
            return false;
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }

    public boolean replaceIn(StringBuilder source) {
        return this.replaceIn(null, source);
    }

    public boolean replaceIn(LogEvent event, StringBuilder source) {
        if (source == null) {
            return false;
        }
        return this.substitute(event, source, 0, source.length());
    }

    public boolean replaceIn(StringBuilder source, int offset, int length) {
        return this.replaceIn(null, source, offset, length);
    }

    public boolean replaceIn(LogEvent event, StringBuilder source, int offset, int length) {
        if (source == null) {
            return false;
        }
        return this.substitute(event, source, offset, length);
    }

    protected boolean substitute(LogEvent event, StringBuilder buf, int offset, int length) {
        return this.substitute(event, buf, offset, length, null) > 0;
    }

    private int substitute(LogEvent event, StringBuilder buf, int offset, int length, List<String> priorVariables) {
        StrMatcher prefixMatcher = this.getVariablePrefixMatcher();
        StrMatcher suffixMatcher = this.getVariableSuffixMatcher();
        char escape = this.getEscapeChar();
        boolean top = priorVariables == null;
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = this.getChars(buf);
        int bufEnd = offset + length;
        int pos = offset;
        block0: while (pos < bufEnd) {
            int startMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd);
            if (startMatchLen == 0) {
                ++pos;
                continue;
            }
            if (pos > offset && chars[pos - 1] == escape) {
                buf.deleteCharAt(pos - 1);
                chars = this.getChars(buf);
                --lengthChange;
                altered = true;
                --bufEnd;
                continue;
            }
            int startPos = pos;
            pos += startMatchLen;
            int endMatchLen = 0;
            int nestedVarCount = 0;
            while (pos < bufEnd) {
                if (this.isEnableSubstitutionInVariables() && (endMatchLen = prefixMatcher.isMatch(chars, pos, offset, bufEnd)) != 0) {
                    ++nestedVarCount;
                    pos += endMatchLen;
                    continue;
                }
                endMatchLen = suffixMatcher.isMatch(chars, pos, offset, bufEnd);
                if (endMatchLen == 0) {
                    ++pos;
                    continue;
                }
                if (nestedVarCount == 0) {
                    String varName = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
                    if (this.isEnableSubstitutionInVariables()) {
                        StringBuilder bufName = new StringBuilder(varName);
                        this.substitute(event, bufName, 0, bufName.length());
                        varName = bufName.toString();
                    }
                    int endPos = pos += endMatchLen;
                    if (priorVariables == null) {
                        priorVariables = new ArrayList<String>();
                        priorVariables.add(new String(chars, offset, length));
                    }
                    this.checkCyclicSubstitution(varName, priorVariables);
                    priorVariables.add(varName);
                    String varValue = this.resolveVariable(event, varName, buf, startPos, endPos);
                    if (varValue != null) {
                        int varLen = varValue.length();
                        buf.replace(startPos, endPos, varValue);
                        altered = true;
                        int change = this.substitute(event, buf, startPos, varLen, priorVariables);
                        pos += (change += varLen - (endPos - startPos));
                        bufEnd += change;
                        lengthChange += change;
                        chars = this.getChars(buf);
                    }
                    priorVariables.remove(priorVariables.size() - 1);
                    continue block0;
                }
                --nestedVarCount;
                pos += endMatchLen;
            }
        }
        if (top) {
            return altered ? 1 : 0;
        }
        return lengthChange;
    }

    private void checkCyclicSubstitution(String varName, List<String> priorVariables) {
        if (!priorVariables.contains(varName)) {
            return;
        }
        StringBuilder buf = new StringBuilder(256);
        buf.append("Infinite loop in property interpolation of ");
        buf.append(priorVariables.remove(0));
        buf.append(": ");
        this.appendWithSeparators(buf, priorVariables, "->");
        throw new IllegalStateException(buf.toString());
    }

    protected String resolveVariable(LogEvent event, String variableName, StringBuilder buf, int startPos, int endPos) {
        StrLookup resolver = this.getVariableResolver();
        if (resolver == null) {
            return null;
        }
        return resolver.lookup(event, variableName);
    }

    public char getEscapeChar() {
        return this.escapeChar;
    }

    public void setEscapeChar(char escapeCharacter) {
        this.escapeChar = escapeCharacter;
    }

    public StrMatcher getVariablePrefixMatcher() {
        return this.prefixMatcher;
    }

    public StrSubstitutor setVariablePrefixMatcher(StrMatcher prefixMatcher) {
        if (prefixMatcher == null) {
            throw new IllegalArgumentException("Variable prefix matcher must not be null!");
        }
        this.prefixMatcher = prefixMatcher;
        return this;
    }

    public StrSubstitutor setVariablePrefix(char prefix) {
        return this.setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
    }

    public StrSubstitutor setVariablePrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Variable prefix must not be null!");
        }
        return this.setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
    }

    public StrMatcher getVariableSuffixMatcher() {
        return this.suffixMatcher;
    }

    public StrSubstitutor setVariableSuffixMatcher(StrMatcher suffixMatcher) {
        if (suffixMatcher == null) {
            throw new IllegalArgumentException("Variable suffix matcher must not be null!");
        }
        this.suffixMatcher = suffixMatcher;
        return this;
    }

    public StrSubstitutor setVariableSuffix(char suffix) {
        return this.setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
    }

    public StrSubstitutor setVariableSuffix(String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("Variable suffix must not be null!");
        }
        return this.setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
    }

    public StrLookup getVariableResolver() {
        return this.variableResolver;
    }

    public void setVariableResolver(StrLookup variableResolver) {
        this.variableResolver = variableResolver;
    }

    public boolean isEnableSubstitutionInVariables() {
        return this.enableSubstitutionInVariables;
    }

    public void setEnableSubstitutionInVariables(boolean enableSubstitutionInVariables) {
        this.enableSubstitutionInVariables = enableSubstitutionInVariables;
    }

    private char[] getChars(StringBuilder sb) {
        char[] chars = new char[sb.length()];
        sb.getChars(0, sb.length(), chars, 0);
        return chars;
    }

    public void appendWithSeparators(StringBuilder sb, Iterable<?> iterable, String separator) {
        if (iterable != null) {
            separator = separator == null ? "" : separator;
            Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                sb.append(it.next());
                if (!it.hasNext()) continue;
                sb.append(separator);
            }
        }
    }

    public String toString() {
        return "StrSubstitutor(" + this.variableResolver.toString() + ")";
    }
}

