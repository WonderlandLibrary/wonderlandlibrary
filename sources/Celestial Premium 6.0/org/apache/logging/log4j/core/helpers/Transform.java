/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.core.helpers.Strings;

public final class Transform {
    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";
    private static final String CDATA_PSEUDO_END = "]]&gt;";
    private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
    private static final int CDATA_END_LEN = "]]>".length();

    private Transform() {
    }

    public static String escapeHtmlTags(String input) {
        if (Strings.isEmpty(input) || input.indexOf(34) == -1 && input.indexOf(38) == -1 && input.indexOf(60) == -1 && input.indexOf(62) == -1) {
            return input;
        }
        StringBuilder buf = new StringBuilder(input.length() + 6);
        char ch = ' ';
        int len = input.length();
        for (int i = 0; i < len; ++i) {
            ch = input.charAt(i);
            if (ch > '>') {
                buf.append(ch);
                continue;
            }
            if (ch == '<') {
                buf.append("&lt;");
                continue;
            }
            if (ch == '>') {
                buf.append("&gt;");
                continue;
            }
            if (ch == '&') {
                buf.append("&amp;");
                continue;
            }
            if (ch == '\"') {
                buf.append("&quot;");
                continue;
            }
            buf.append(ch);
        }
        return buf.toString();
    }

    public static void appendEscapingCDATA(StringBuilder buf, String str) {
        if (str != null) {
            int end = str.indexOf(CDATA_END);
            if (end < 0) {
                buf.append(str);
            } else {
                int start = 0;
                while (end > -1) {
                    buf.append(str.substring(start, end));
                    buf.append(CDATA_EMBEDED_END);
                    start = end + CDATA_END_LEN;
                    if (start < str.length()) {
                        end = str.indexOf(CDATA_END, start);
                        continue;
                    }
                    return;
                }
                buf.append(str.substring(start));
            }
        }
    }

    public static String escapeJsonControlCharacters(String input) {
        if (Strings.isEmpty(input) || input.indexOf(34) == -1 && input.indexOf(92) == -1 && input.indexOf(47) == -1 && input.indexOf(8) == -1 && input.indexOf(12) == -1 && input.indexOf(10) == -1 && input.indexOf(13) == -1 && input.indexOf(9) == -1) {
            return input;
        }
        StringBuilder buf = new StringBuilder(input.length() + 6);
        int len = input.length();
        block10: for (int i = 0; i < len; ++i) {
            char ch = input.charAt(i);
            String escBs = "\\\\";
            switch (ch) {
                case '\"': {
                    buf.append("\\\\");
                    buf.append(ch);
                    continue block10;
                }
                case '\\': {
                    buf.append("\\\\");
                    buf.append(ch);
                    continue block10;
                }
                case '/': {
                    buf.append("\\\\");
                    buf.append(ch);
                    continue block10;
                }
                case '\b': {
                    buf.append("\\\\");
                    buf.append('b');
                    continue block10;
                }
                case '\f': {
                    buf.append("\\\\");
                    buf.append('f');
                    continue block10;
                }
                case '\n': {
                    buf.append("\\\\");
                    buf.append('n');
                    continue block10;
                }
                case '\r': {
                    buf.append("\\\\");
                    buf.append('r');
                    continue block10;
                }
                case '\t': {
                    buf.append("\\\\");
                    buf.append('t');
                    continue block10;
                }
                default: {
                    buf.append(ch);
                }
            }
        }
        return buf.toString();
    }
}

