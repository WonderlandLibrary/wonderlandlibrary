// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.Xulu;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class OldName extends Element
{
    private final Value<Boolean> rainbow;
    
    public OldName() {
        super("OldName");
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
    }
    
    @Override
    public void onEnable() {
        this.width = OldName.fontRenderer.getStringWidth("Elementars.com") + 2;
        this.height = OldName.fontRenderer.FONT_HEIGHT + 2;
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        int color = ColorUtil.getClickGUIColor().getRGB();
        if (this.rainbow.getValue()) {
            color = Xulu.rgb;
        }
        if (Xulu.CustomFont) {
            Xulu.cFontRenderer.drawStringWithShadow("Elementars.com", this.x + 1.0, this.y + 1.0, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
        else {
            Wrapper.getMinecraft().fontRenderer.drawStringWithShadow("Elementars.com", (float)this.x + 1.0f, (float)this.y + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
    }
}
