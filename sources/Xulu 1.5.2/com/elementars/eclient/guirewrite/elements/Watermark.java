// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import dev.xulu.settings.OnChangedValue;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.Xulu;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import com.elementars.eclient.guirewrite.Element;

public class Watermark extends Element
{
    private final Value<String> text;
    private final Value<Boolean> rainbow;
    
    public Watermark() {
        super("Watermark");
        this.text = this.register(new Value<String>("Mode", this, "Xulu", new String[] { "Xulu", "PK Client", "WideHack" })).onChanged(onChangedValue -> this.width = Watermark.fontRenderer.getStringWidth(onChangedValue.getNew() + " " + "v1.5.2") + 2);
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
    }
    
    @Override
    public void onEnable() {
        this.width = Watermark.fontRenderer.getStringWidth(this.text.getValue() + " " + "v1.5.2") + 2;
        this.height = Watermark.fontRenderer.FONT_HEIGHT + 2;
        super.onEnable();
    }
    
    @Override
    public void onRender() {
        int color = ColorUtil.getClickGUIColor().getRGB();
        if (this.rainbow.getValue()) {
            color = Xulu.rgb;
        }
        if (Xulu.CustomFont) {
            Xulu.cFontRenderer.drawStringWithShadow(this.text.getValue() + " " + "v1.5.2", this.x + 1.0, this.y + 1.0, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
        else {
            Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(this.text.getValue() + " " + "v1.5.2", (float)this.x + 1.0f, (float)this.y + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
        }
    }
}
