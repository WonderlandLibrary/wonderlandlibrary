// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.text;

import vip.Resolute.util.font.FontUtil;
import vip.Resolute.ui.click.skeet.SkeetUI;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.ui.click.skeet.framework.Component;

public final class TextComponent extends Component
{
    private static final MinecraftFontRenderer FONT_RENDERER;
    private final String text;
    
    public TextComponent(final Component parent, final String text, final float x, final float y) {
        super(parent, x, y, (float)TextComponent.FONT_RENDERER.getStringWidth(text), (float)TextComponent.FONT_RENDERER.getHeight());
        this.text = text;
    }
    
    @Override
    public void drawComponent(final ScaledResolution resolution, final int mouseX, final int mouseY) {
        if (SkeetUI.shouldRenderText()) {
            TextComponent.FONT_RENDERER.drawString(this.text, this.getX(), this.getY(), SkeetUI.getColor(15132390));
        }
    }
    
    static {
        FONT_RENDERER = FontUtil.tahomaVerySmall;
    }
}
