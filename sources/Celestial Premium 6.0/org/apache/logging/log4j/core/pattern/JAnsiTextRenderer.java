/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.fusesource.jansi.Ansi
 *  org.fusesource.jansi.AnsiRenderer$Code
 */
package org.apache.logging.log4j.core.pattern;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.core.pattern.TextRenderer;
import org.apache.logging.log4j.status.StatusLogger;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiRenderer;

public final class JAnsiTextRenderer
implements TextRenderer {
    public static final Map<String, AnsiRenderer.Code[]> DefaultExceptionStyleMap;
    static final Map<String, AnsiRenderer.Code[]> DefaultMessageStyleMap;
    private static final Map<String, Map<String, AnsiRenderer.Code[]>> PrefedinedStyleMaps;
    private final String beginToken;
    private final int beginTokenLen;
    private final String endToken;
    private final int endTokenLen;
    private final Map<String, AnsiRenderer.Code[]> styleMap;

    private static void put(Map<String, AnsiRenderer.Code[]> map, String name, AnsiRenderer.Code ... codes) {
        map.put(name, codes);
    }

    public JAnsiTextRenderer(String[] formats, Map<String, AnsiRenderer.Code[]> defaultStyleMap) {
        Map<String, AnsiRenderer.Code[]> map;
        String tempBeginToken = "@|";
        String tempEndToken = "|@";
        if (formats.length > 1) {
            String allStylesStr = formats[1];
            String[] allStyleAssignmentsArr = allStylesStr.split(" ");
            map = new HashMap<String, AnsiRenderer.Code[]>(allStyleAssignmentsArr.length + defaultStyleMap.size());
            map.putAll(defaultStyleMap);
            block10: for (String styleAssignmentStr : allStyleAssignmentsArr) {
                String[] styleAssignmentArr = styleAssignmentStr.split("=");
                if (styleAssignmentArr.length != 2) {
                    StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", this.getClass().getSimpleName(), styleAssignmentStr);
                    continue;
                }
                String styleName = styleAssignmentArr[0];
                String codeListStr = styleAssignmentArr[1];
                String[] codeNames = codeListStr.split(",");
                if (codeNames.length == 0) {
                    StatusLogger.getLogger().warn("{} parsing style \"{}\", expected format: StyleName=Code(,Code)*", this.getClass().getSimpleName(), styleAssignmentStr);
                    continue;
                }
                switch (styleName) {
                    case "BeginToken": {
                        tempBeginToken = codeNames[0];
                        continue block10;
                    }
                    case "EndToken": {
                        tempEndToken = codeNames[0];
                        continue block10;
                    }
                    case "StyleMapName": {
                        String predefinedMapName = codeNames[0];
                        Map<String, AnsiRenderer.Code[]> predefinedMap = PrefedinedStyleMaps.get(predefinedMapName);
                        if (predefinedMap != null) {
                            map.putAll(predefinedMap);
                            continue block10;
                        }
                        StatusLogger.getLogger().warn("Unknown predefined map name {}, pick one of {}", predefinedMapName, null);
                        continue block10;
                    }
                    default: {
                        AnsiRenderer.Code[] codes = new AnsiRenderer.Code[codeNames.length];
                        for (int i = 0; i < codes.length; ++i) {
                            codes[i] = this.toCode(codeNames[i]);
                        }
                        map.put(styleName, codes);
                    }
                }
            }
        } else {
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

    private void render(Ansi ansi, AnsiRenderer.Code code) {
        if (code.isColor()) {
            if (code.isBackground()) {
                ansi.bg(code.getColor());
            } else {
                ansi.fg(code.getColor());
            }
        } else if (code.isAttribute()) {
            ansi.a(code.getAttribute());
        }
    }

    private void render(Ansi ansi, AnsiRenderer.Code ... codes) {
        for (AnsiRenderer.Code code : codes) {
            this.render(ansi, code);
        }
    }

    private String render(String text, String ... names) {
        Ansi ansi = Ansi.ansi();
        for (String name : names) {
            AnsiRenderer.Code[] codes = this.styleMap.get(name);
            if (codes != null) {
                this.render(ansi, codes);
                continue;
            }
            this.render(ansi, this.toCode(name));
        }
        return ansi.a(text).reset().toString();
    }

    @Override
    public void render(String input, StringBuilder output, String styleName) throws IllegalArgumentException {
        output.append(this.render(input, styleName));
    }

    @Override
    public void render(StringBuilder input, StringBuilder output) throws IllegalArgumentException {
        int i = 0;
        while (true) {
            int j;
            if ((j = input.indexOf(this.beginToken, i)) == -1) {
                if (i == 0) {
                    output.append((CharSequence)input);
                    return;
                }
                output.append(input.substring(i, input.length()));
                return;
            }
            output.append(input.substring(i, j));
            int k = input.indexOf(this.endToken, j);
            if (k == -1) {
                output.append((CharSequence)input);
                return;
            }
            String spec = input.substring(j += this.beginTokenLen, k);
            String[] items = spec.split(" ", 2);
            if (items.length == 1) {
                output.append((CharSequence)input);
                return;
            }
            String replacement = this.render(items[1], items[0].split(","));
            output.append(replacement);
            i = k + this.endTokenLen;
        }
    }

    private AnsiRenderer.Code toCode(String name) {
        return AnsiRenderer.Code.valueOf((String)name.toUpperCase(Locale.ENGLISH));
    }

    public String toString() {
        return "JAnsiMessageRenderer [beginToken=" + this.beginToken + ", beginTokenLen=" + this.beginTokenLen + ", endToken=" + this.endToken + ", endTokenLen=" + this.endTokenLen + ", styleMap=" + this.styleMap + "]";
    }

    static {
        HashMap<String, Map<String, AnsiRenderer.Code[]>> tempPreDefs = new HashMap<String, Map<String, AnsiRenderer.Code[]>>();
        HashMap<String, Object> map = new HashMap<String, AnsiRenderer.Code[]>();
        JAnsiTextRenderer.put(map, "Prefix", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "Name", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "NameMessageSeparator", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "Message", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD);
        JAnsiTextRenderer.put(map, "At", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "CauseLabel", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "Text", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "More", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "Suppressed", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "StackTraceElement.ClassName", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "StackTraceElement.ClassMethodSeparator", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "StackTraceElement.MethodName", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "StackTraceElement.NativeMethod", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "StackTraceElement.FileName", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "StackTraceElement.LineNumber", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "StackTraceElement.Container", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "StackTraceElement.ContainerSeparator", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "StackTraceElement.UnknownSource", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Inexact", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Container", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.ContainerSeparator", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Location", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Version", AnsiRenderer.Code.YELLOW);
        DefaultExceptionStyleMap = Collections.unmodifiableMap(map);
        tempPreDefs.put("Spock", DefaultExceptionStyleMap);
        map = new HashMap();
        JAnsiTextRenderer.put(map, "Prefix", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "Name", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW, AnsiRenderer.Code.BOLD);
        JAnsiTextRenderer.put(map, "NameMessageSeparator", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "Message", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE, AnsiRenderer.Code.BOLD);
        JAnsiTextRenderer.put(map, "At", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "CauseLabel", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "Text", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "More", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "Suppressed", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "StackTraceElement.ClassName", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "StackTraceElement.ClassMethodSeparator", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "StackTraceElement.MethodName", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "StackTraceElement.NativeMethod", AnsiRenderer.Code.BG_RED, AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "StackTraceElement.FileName", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "StackTraceElement.LineNumber", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "StackTraceElement.Container", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "StackTraceElement.ContainerSeparator", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "StackTraceElement.UnknownSource", AnsiRenderer.Code.RED);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Inexact", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Container", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.ContainerSeparator", AnsiRenderer.Code.WHITE);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Location", AnsiRenderer.Code.YELLOW);
        JAnsiTextRenderer.put(map, "ExtraClassInfo.Version", AnsiRenderer.Code.YELLOW);
        tempPreDefs.put("Kirk", Collections.unmodifiableMap(map));
        HashMap temp = new HashMap();
        DefaultMessageStyleMap = Collections.unmodifiableMap(temp);
        PrefedinedStyleMaps = Collections.unmodifiableMap(tempPreDefs);
    }
}

