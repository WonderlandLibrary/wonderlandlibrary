// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.player;

import vip.Resolute.util.font.FontUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.framework.Component;
import net.minecraft.client.Minecraft;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.ui.click.skeet.framework.ButtonComponent;

public class PlayerComponent extends ButtonComponent
{
    private static final MinecraftFontRenderer FONT_RENDERER;
    private Minecraft mc;
    
    public PlayerComponent(final Component parent, final float x, final float y) {
        super(parent, x, y, (float)(PlayerComponent.FONT_RENDERER.getStringWidth("[") * 2.0), (float)PlayerComponent.FONT_RENDERER.getHeight());
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public float getWidth() {
        return super.getWidth();
    }
    
    @Override
    public void drawComponent(final ScaledResolution lockedResolution, final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        final float width = this.getWidth();
        try {
            GuiInventory.drawEntityOnScreen((int)(x + 52.0f), (int)(y + 124.0f), 50, 0.0f, 0.0f, Minecraft.getMinecraft().thePlayer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isHovered(final int mouseX, final int mouseY) {
        final float x = this.getX();
        final float y = this.getY();
        return mouseX >= x + 50.166668f - this.getWidth() && mouseY >= y - 10.0f && mouseX <= x + 150.0f && mouseY <= y + this.getHeight() - 10.0f;
    }
    
    @Override
    public void onKeyPress(final int keyCode) {
    }
    
    @Override
    public void onPress(final int mouseButton) {
    }
    
    static {
        FONT_RENDERER = FontUtil.tahomaSmall;
    }
}
