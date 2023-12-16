// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.settings.mode;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.function.BiConsumer;
import moonsense.settings.Setting;
import moonsense.ui.elements.settings.SettingElement;

public class SettingElementModeButton extends SettingElement
{
    private final boolean next;
    
    public SettingElementModeButton(final int x, final int y, final int width, final int height, final boolean next, final Setting setting, final BiConsumer<Setting, SettingElement> consumer) {
        super(x, y, width, height, setting, consumer, null);
        this.next = next;
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        GuiUtils.setGlColor(this.enabled ? (this.hovered ? MoonsenseClient.getMainColor(255) : MoonsenseClient.getMainColor(150)) : MoonsenseClient.getMainColor(80));
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(2.0f);
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
    public void update() {
        super.update();
    }
}
