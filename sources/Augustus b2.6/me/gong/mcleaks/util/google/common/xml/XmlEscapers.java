// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.xml;

import me.gong.mcleaks.util.google.common.escape.Escapers;
import me.gong.mcleaks.util.google.common.escape.Escaper;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtCompatible
public class XmlEscapers
{
    private static final char MIN_ASCII_CONTROL_CHAR = '\0';
    private static final char MAX_ASCII_CONTROL_CHAR = '\u001f';
    private static final Escaper XML_ESCAPER;
    private static final Escaper XML_CONTENT_ESCAPER;
    private static final Escaper XML_ATTRIBUTE_ESCAPER;
    
    private XmlEscapers() {
    }
    
    public static Escaper xmlContentEscaper() {
        return XmlEscapers.XML_CONTENT_ESCAPER;
    }
    
    public static Escaper xmlAttributeEscaper() {
        return XmlEscapers.XML_ATTRIBUTE_ESCAPER;
    }
    
    static {
        final Escapers.Builder builder = Escapers.builder();
        builder.setSafeRange('\0', '\ufffd');
        builder.setUnsafeReplacement("\ufffd");
        for (char c = '\0'; c <= '\u001f'; ++c) {
            if (c != '\t' && c != '\n' && c != '\r') {
                builder.addEscape(c, "\ufffd");
            }
        }
        builder.addEscape('&', "&amp;");
        builder.addEscape('<', "&lt;");
        builder.addEscape('>', "&gt;");
        XML_CONTENT_ESCAPER = builder.build();
        builder.addEscape('\'', "&apos;");
        builder.addEscape('\"', "&quot;");
        XML_ESCAPER = builder.build();
        builder.addEscape('\t', "&#x9;");
        builder.addEscape('\n', "&#xA;");
        builder.addEscape('\r', "&#xD;");
        XML_ATTRIBUTE_ESCAPER = builder.build();
    }
}
