// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.pattern;

import java.util.Collections;
import java.util.Locale;
import org.fusesource.jansi.Ansi;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.HashMap;
import org.fusesource.jansi.AnsiRenderer;
import java.util.Map;

public final class JAnsiTextRenderer implements TextRenderer
{
    public static final Map<String, AnsiRenderer.Code[]> DefaultExceptionStyleMap;
    static final Map<String, AnsiRenderer.Code[]> DefaultMessageStyleMap;
    private static final Map<String, Map<String, AnsiRenderer.Code[]>> PrefedinedStyleMaps;
    private final String beginToken;
    private final int beginTokenLen;
    private final String endToken;
    private final int endTokenLen;
    private final Map<String, AnsiRenderer.Code[]> styleMap;
    
    private static void put(final Map<String, AnsiRenderer.Code[]> map, final String name, final AnsiRenderer.Code... codes) {
        map.put(name, codes);
    }
    
    public JAnsiTextRenderer(final String[] formats, final Map<String, AnsiRenderer.Code[]> defaultStyleMap) {
        String tempBeginToken = "@|";
        String tempEndToken = "|@";
        Map<String, AnsiRenderer.Code[]> map;
        if (formats.length > 1) {
            final String allStylesStr = formats[1];
            final String[] allStyleAssignmentsArr = allStylesStr.split(" ");
            map = new HashMap<String, AnsiRenderer.Code[]>(allStyleAssignmentsArr.length + defaultStyleMap.size());
            map.putAll(defaultStyleMap);
            for (final String styleAssignmentStr : allStyleAssignmentsArr) {
                final String[] styleAssignmentArr = styleAssignmentStr.split("=");
                if (styleAssignmentArr.length != 2) {
                    StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", this.getClass().getSimpleName(), styleAssignmentStr);
                }
                else {
                    final String styleName = styleAssignmentArr[0];
                    final String codeListStr = styleAssignmentArr[1];
                    final String[] codeNames = codeListStr.split(",");
                    if (codeNames.length == 0) {
                        StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", this.getClass().getSimpleName(), styleAssignmentStr);
                    }
                    else {
                        final String s = styleName;
                        switch (s) {
                            case "BeginToken": {
                                tempBeginToken = codeNames[0];
                                break;
                            }
                            case "EndToken": {
                                tempEndToken = codeNames[0];
                                break;
                            }
                            case "StyleMapName": {
                                final String predefinedMapName = codeNames[0];
                                final Map<String, AnsiRenderer.Code[]> predefinedMap = JAnsiTextRenderer.PrefedinedStyleMaps.get(predefinedMapName);
                                if (predefinedMap != null) {
                                    map.putAll(predefinedMap);
                                    break;
                                }
                                StatusLogger.getLogger().warn("Unknown predefined map name {}, pick one of {}", predefinedMapName, null);
                                break;
                            }
                            default: {
                                final AnsiRenderer.Code[] codes = new AnsiRenderer.Code[codeNames.length];
                                for (int i = 0; i < codes.length; ++i) {
                                    codes[i] = this.toCode(codeNames[i]);
                                }
                                map.put(styleName, codes);
                                break;
                            }
                        }
                    }
                }
            }
        }
        else {
            map = defaultStyleMap;
        }
        this.styleMap = map;
        this.beginToken = tempBeginToken;
        this.endToken = tempEndToken;
        this.beginTokenLen = tempBeginToken.length();
        this.endTokenLen = tempEndToken.length();
    }
    
    public Map<String, AnsiRenderer.Code[]> getStyleMap() {
        return this.styleMap;
    }
    
    private void render(final Ansi ansi, final AnsiRenderer.Code code) {
        if (code.isColor()) {
            if (code.isBackground()) {
                ansi.bg(code.getColor());
            }
            else {
                ansi.fg(code.getColor());
            }
        }
        else if (code.isAttribute()) {
            ansi.a(code.getAttribute());
        }
    }
    
    private void render(final Ansi ansi, final AnsiRenderer.Code... codes) {
        for (final AnsiRenderer.Code code : codes) {
            this.render(ansi, code);
        }
    }
    
    private String render(final String text, final String... names) {
        final Ansi ansi = Ansi.ansi();
        for (final String name : names) {
            final AnsiRenderer.Code[] codes = this.styleMap.get(name);
            if (codes != null) {
                this.render(ansi, codes);
            }
            else {
                this.render(ansi, this.toCode(name));
            }
        }
        return ansi.a(text).reset().toString();
    }
    
    @Override
    public void render(final String input, final StringBuilder output, final String styleName) throws IllegalArgumentException {
        output.append(this.render(input, styleName));
    }
    
    @Override
    public void render(final StringBuilder input, final StringBuilder output) throws IllegalArgumentException {
        int i = 0;
        while (true) {
            int j = input.indexOf(this.beginToken, i);
            if (j == -1) {
                if (i == 0) {
                    output.append((CharSequence)input);
                    return;
                }
                output.append(input.substring(i, input.length()));
            }
            else {
                output.append(input.substring(i, j));
                final int k = input.indexOf(this.endToken, j);
                if (k == -1) {
                    output.append((CharSequence)input);
                    return;
                }
                j += this.beginTokenLen;
                final String spec = input.substring(j, k);
                final String[] items = spec.split(" ", 2);
                if (items.length == 1) {
                    output.append((CharSequence)input);
                    return;
                }
                final String replacement = this.render(items[1], items[0].split(","));
                output.append(replacement);
                i = k + this.endTokenLen;
            }
        }
    }
    
    private AnsiRenderer.Code toCode(final String name) {
        return AnsiRenderer.Code.valueOf(name.toUpperCase(Locale.ENGLISH));
    }
    
    @Override
    public String toString() {
        return "JAnsiMessageRenderer [beginToken=" + this.beginToken + ", beginTokenLen=" + this.beginTokenLen + ", endToken=" + this.endToken + ", endTokenLen=" + this.endTokenLen + ", styleMap=" + this.styleMap + "]";
    }
    
    static {
        final Map<String, Map<String, AnsiRenderer.Code[]>> tempPreDefs = new HashMap<String, Map<String, AnsiRenderer.Code[]>>();
        Map<String, AnsiRenderer.Code[]> map = new HashMap<String, AnsiRenderer.Code[]>();
        put(map, "Prefix", AnsiRenderer.Code.WHITE);
        put(map, "Name", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE);
        put(map, "NameMessageSeparator", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE);
        put(map, "Message", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD);
        put(map, "At", AnsiRenderer.Code.WHITE);
        put(map, "CauseLabel", AnsiRenderer.Code.WHITE);
        put(map, "Text", AnsiRenderer.Code.WHITE);
        put(map, "More", AnsiRenderer.Code.WHITE);
        put(map, "Suppressed", AnsiRenderer.Code.WHITE);
        put(map, "StackTraceElement.ClassName", AnsiRenderer.Code.YELLOW);
        put(map, "StackTraceElement.ClassMethodSeparator", AnsiRenderer.Code.YELLOW);
        put(map, "StackTraceElement.MethodName", AnsiRenderer.Code.YELLOW);
        put(map, "StackTraceElement.NativeMethod", AnsiRenderer.Code.YELLOW);
        put(map, "StackTraceElement.FileName", AnsiRenderer.Code.RED);
        put(map, "StackTraceElement.LineNumber", AnsiRenderer.Code.RED);
        put(map, "StackTraceElement.Container", AnsiRenderer.Code.RED);
        put(map, "StackTraceElement.ContainerSeparator", AnsiRenderer.Code.WHITE);
        put(map, "StackTraceElement.UnknownSource", AnsiRenderer.Code.RED);
        put(map, "ExtraClassInfo.Inexact", AnsiRenderer.Code.YELLOW);
        put(map, "ExtraClassInfo.Container", AnsiRenderer.Code.YELLOW);
        put(map, "ExtraClassInfo.ContainerSeparator", AnsiRenderer.Code.YELLOW);
        put(map, "ExtraClassInfo.Location", AnsiRenderer.Code.YELLOW);
        put(map, "ExtraClassInfo.Version", AnsiRenderer.Code.YELLOW);
        tempPreDefs.put("Spock", DefaultExceptionStyleMap = Collections.unmodifiableMap((Map<? extends String, ? extends AnsiRenderer.Code[]>)map));
        map = new HashMap<String, AnsiRenderer.Code[]>();
        put(map, "Prefix", AnsiRenderer.Code.WHITE);
        put(map, "Name", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW, AnsiRenderer.Code.BOLD);
        put(map, "NameMessageSeparator", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        put(map, "Message", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD);
        put(map, "At", AnsiRenderer.Code.WHITE);
        put(map, "CauseLabel", AnsiRenderer.Code.WHITE);
        put(map, "Text", AnsiRenderer.Code.WHITE);
        put(map, "More", AnsiRenderer.Code.WHITE);
        put(map, "Suppressed", AnsiRenderer.Code.WHITE);
        put(map, "StackTraceElement.ClassName", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE);
        put(map, "StackTraceElement.ClassMethodSeparator", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        put(map, "StackTraceElement.MethodName", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        put(map, "StackTraceElement.NativeMethod", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        put(map, "StackTraceElement.FileName", AnsiRenderer.Code.RED);
        put(map, "StackTraceElement.LineNumber", AnsiRenderer.Code.RED);
        put(map, "StackTraceElement.Container", AnsiRenderer.Code.RED);
        put(map, "StackTraceElement.ContainerSeparator", AnsiRenderer.Code.WHITE);
        put(map, "StackTraceElement.UnknownSource", AnsiRenderer.Code.RED);
        put(map, "ExtraClassInfo.Inexact", AnsiRenderer.Code.YELLOW);
        put(map, "ExtraClassInfo.Container", AnsiRenderer.Code.WHITE);
        put(map, "ExtraClassInfo.ContainerSeparator", AnsiRenderer.Code.WHITE);
        put(map, "ExtraClassInfo.Location", AnsiRenderer.Code.YELLOW);
        put(map, "ExtraClassInfo.Version", AnsiRenderer.Code.YELLOW);
        tempPreDefs.put("Kirk", Collections.unmodifiableMap((Map<? extends String, ? extends AnsiRenderer.Code[]>)map));
        final Map<String, AnsiRenderer.Code[]> temp = new HashMap<String, AnsiRenderer.Code[]>();
        DefaultMessageStyleMap = Collections.unmodifiableMap((Map<? extends String, ? extends AnsiRenderer.Code[]>)temp);
        PrefedinedStyleMaps = Collections.unmodifiableMap((Map<? extends String, ? extends Map<String, AnsiRenderer.Code[]>>)tempPreDefs);
    }
}
