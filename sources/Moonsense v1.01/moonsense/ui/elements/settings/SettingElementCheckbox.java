// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;

public class SettingElementCheckbox extends SettingElement
{
    private int bgFade;
    private int slide;
    
    public SettingElementCheckbox(final int x, final int y, final int width, final int height, final Setting setting, final BiConsumer<Setting, SettingElement> consumer, final AbstractGuiScreen parent) {
        super(x, y, width, height, setting, consumer, parent);
        this.bgFade = 50;
        this.slide = 0;
    }
    
    @Override
    public void renderElement(final float partialTicks) {
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        if (this.setting.getBoolean()) {
            GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 5.0f, MoonsenseClient.getMainColor(this.bgFade));
            GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 5.0f, 2.0f, MoonsenseClient.getMainColor(255));
            GuiUtils.drawRoundedRect((float)(this.getX() + 1 + this.slide), (float)(this.getY() + 1), (float)(this.getX() + this.width - 10 - 1 + this.slide), (float)(this.getY() + this.height - 1), 4.0f, MoonsenseClient.getMainColor(255));
        }
        else {
            GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 5.0f, GuiUtils.getRGB(new Color(120, 120, 120, 255).brighter().getRGB(), this.bgFade));
            GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 5.0f, 2.0f, GuiUtils.getRGB(new Color(120, 120, 120, 255).brighter().getRGB(), 255));
            GuiUtils.drawRoundedRect((float)(this.getX() + 1 + this.slide), (float)(this.getY() + 1), (float)(this.getX() + this.width - 10 - 1 + this.slide), (float)(this.getY() + this.height - 1), 4.0f, GuiUtils.getRGB(new Color(120, 120, 120, 255).brighter().getRGB(), 255));
        }
    }
    
    @Override
    public void update() {
        if (this.hovered && this.bgFade + 10 < 150) {
            this.bgFade += 10;
        }
        else if (!this.hovered && this.bgFade - 10 > 50) {
            this.bgFade -= 10;
        }
        if (this.setting.getBoolean() && this.slide < 10) {
            ++this.slide;
        }
        else if (!this.setting.getBoolean() && this.slide > 0) {
            --this.slide;
        }
    }
    
    private void renderCheckmark(final float offset, final int color) {
        GuiUtils.setGlColor(color);
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex2f(this.getX() + 2 + offset, this.getY() + this.height / 2 + offset);
        GL11.glVertex2f(this.getX() + this.width / 3 + 1 + offset, this.getY() + this.height / 3 * 2 + 1 + offset);
        GL11.glVertex2f(this.getX() + this.width / 3 + 1 + offset, this.getY() + this.height / 3 * 2 + 1 + offset);
        GL11.glVertex2f(this.getX() + this.width - 2 + offset, this.getY() + 3 + offset);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
    }
}
