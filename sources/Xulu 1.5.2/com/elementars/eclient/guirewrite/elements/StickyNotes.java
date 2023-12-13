// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.Xulu;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.util.ListHelper;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class StickyNotes extends Element
{
    private final Value<Boolean> rainbow;
    public static String saveText;
    public static String[] text;
    
    public StickyNotes() {
        super("StickyNotes");
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
    }
    
    @Override
    public void onRender() {
        this.width = StickyNotes.fontRenderer.getStringWidth(ListHelper.longest(StickyNotes.text)) + 2;
        this.height = (StickyNotes.fontRenderer.FONT_HEIGHT + 1) * StickyNotes.text.length + 1;
        int color = ColorUtil.getClickGUIColor().getRGB();
        if (this.rainbow.getValue()) {
            color = Xulu.rgb;
        }
        double yCount = this.y;
        for (final String s : StickyNotes.text) {
            final String test = s.replaceAll("&", String.valueOf('§'));
            if (Xulu.CustomFont) {
                Xulu.cFontRenderer.drawStringWithShadow(test, this.x + 1.0, yCount + 1.0, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
            }
            else {
                StickyNotes.fontRenderer.drawStringWithShadow(test, (float)this.x + 1.0f, (float)yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
            }
            yCount += 10.0;
        }
    }
    
    public static void processText(final String stringIn) {
        StickyNotes.text = stringIn.split("@");
        StickyNotes.saveText = stringIn;
    }
    
    @Override
    public void onMiddleClick() {
        TextNotes.initTextBox();
    }
    
    static {
        StickyNotes.text = new String[] { "Put text here" };
    }
}
