// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.html;

import me.gong.mcleaks.util.google.common.escape.Escapers;
import me.gong.mcleaks.util.google.common.escape.Escaper;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class HtmlEscapers
{
    private static final Escaper HTML_ESCAPER;
    
    public static Escaper htmlEscaper() {
        return HtmlEscapers.HTML_ESCAPER;
    }
    
    private HtmlEscapers() {
    }
    
    static {
        HTML_ESCAPER = Escapers.builder().addEscape('\"', "&quot;").addEscape('\'', "&#39;").addEscape('&', "&amp;").addEscape('<', "&lt;").addEscape('>', "&gt;").build();
    }
}
