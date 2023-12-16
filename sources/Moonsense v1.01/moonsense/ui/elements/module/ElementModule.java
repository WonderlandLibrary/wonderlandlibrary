// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.module;

import moonsense.config.ModuleConfig;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import java.util.function.Consumer;
import moonsense.features.SCModule;
import moonsense.ui.elements.Element;

public class ElementModule extends Element
{
    private final SCModule module;
    private float scale;
    private int bgFade;
    private int toggleFade;
    private int optionsFade;
    private final int disabledColor;
    
    public ElementModule(final int x, final int y, final int width, final int height, final SCModule module, final Consumer<Element> consumer) {
        super(x, y, width, height, true, consumer);
        this.disabledColor = new Color(120, 120, 120, 255).brighter().getRGB();
        this.module = module;
    }
    
    @Override
    public void init() {
        this.scale = this.rescale(1.0f);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        if (this.module.hasIcon()) {
            GlStateManager.pushMatrix();
            this.mc.getTextureManager().bindTexture(this.module.getIcon());
            final int b = (this.module.getTextureIndex() != -1) ? this.module.getTextureIndex() : 25;
            GlStateManager.enableBlend();
            GuiUtils.setGlColor(new Color(0, 0, 0, 75).getRGB());
            GuiUtils.drawModalRectWithCustomSizedTexture(this.getX() + 5 + (24 - b) / 2 + 0.75f - 5.0f + 3.0f, this.getY() - 0.5f + (24 - b) / 2 + 0.75f + 1.0f + 2.5f, 0.0f, 0.0f, b, b, (float)b, (float)b);
            GuiUtils.setGlColor(-1);
            GuiUtils.drawModalRectWithCustomSizedTexture((float)(this.getX() + 5 + (24 - b) / 2 - 5 + 3), this.getY() - 0.5f + (24 - b) / 2 + 1.0f + 2.5f, 0.0f, 0.0f, b, b, (float)b, (float)b);
            GlStateManager.popMatrix();
        }
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 5.0f, this.hovered ? new Color(100, 111, 117, 100).getRGB() : new Color(42, 50, 55, 100).getRGB());
        MoonsenseClient.textRenderer.drawCenteredString(this.module.displayName, (float)(this.getX() + this.width / 2), this.getY() + 7.5f + 2.5f, -1);
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        if (this.module.isServerDisabled()) {
            GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 7.0f, new Color(120, 120, 120, this.bgFade).getRGB());
            GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 5.0f, 2.0f, this.disabledColor);
        }
        else {
            GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 7.0f, new Color(0, 0, 0, this.bgFade).getRGB());
            GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 5.0f, 2.0f, ModuleConfig.INSTANCE.isEnabled(this.module) ? new Color(50, 180, 50, 150).getRGB() : new Color(180, 50, 50, 150).getRGB());
        }
    }
    
    @Override
    public void update() {
        if (this.hovered && this.bgFade + 5 <= 70) {
            this.bgFade += 5;
        }
        else if (!this.hovered && this.bgFade - 5 >= 0) {
            this.bgFade -= 5;
        }
    }
    
    private float rescale(final float scale) {
        if (MoonsenseClient.textRenderer.getWidth(this.module.displayName) / (1.0f / scale) > this.width - 37) {
            return this.rescale(scale - 0.05f);
        }
        return scale;
    }
}
