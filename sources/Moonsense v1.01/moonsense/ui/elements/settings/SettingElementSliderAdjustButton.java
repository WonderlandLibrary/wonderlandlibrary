// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings;

import org.lwjgl.opengl.GL11;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;

public class SettingElementSliderAdjustButton extends SettingElement
{
    private final boolean next;
    private boolean visible;
    
    public SettingElementSliderAdjustButton(final int x, final int y, final int width, final int height, final boolean next, final Setting setting, final BiConsumer<Setting, SettingElement> consumer) {
        super(x, y, width, height, setting, consumer, null);
        this.visible = true;
        this.next = next;
        this.enabled = true;
    }
    
    public SettingElementSliderAdjustButton(final int width, final int height, final boolean next, final Setting setting, final BiConsumer<Setting, SettingElement> consumer) {
        this(0, 0, width, height, next, setting, consumer);
    }
    
    public void setVisibility(final boolean visible) {
        this.visible = visible;
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        if (!this.visible) {
            return;
        }
        GlStateManager.disableTexture2D();
        GuiUtils.setGlColor(this.enabled ? (this.hovered ? MoonsenseClient.getMainColor(255) : MoonsenseClient.getMainColor(40)) : MoonsenseClient.getMainColor(80));
        GL11.glLineWidth(1.5f);
        GL11.glEnable(2848);
        GL11.glBegin(1);
        GL11.glVertex2i(this.getX() + (this.next ? 4 : (this.width - 4)), this.getY() + 1);
        GL11.glVertex2i(this.getX() + (this.next ? (this.width - 2) : 2), this.getY() + this.height / 2);
        GL11.glVertex2i(this.getX() + (this.next ? (this.width - 2) : 2), this.getY() + this.height / 2);
        GL11.glVertex2i(this.getX() + (this.next ? 4 : (this.width - 4)), this.getY() + this.height - 1);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
    }
    
    @Override
    protected void drawSettingName() {
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        return !this.visible || super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void update() {
        super.update();
    }
}
