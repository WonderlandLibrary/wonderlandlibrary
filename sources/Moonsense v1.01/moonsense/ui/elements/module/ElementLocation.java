// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.module;

import moonsense.ui.screen.settings.GuiModuleSettings;
import org.lwjgl.input.Mouse;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.MoonsenseClient;
import moonsense.ui.screen.settings.GuiHUDEditor;
import moonsense.utils.BoxUtils;
import moonsense.config.ModuleConfig;
import moonsense.features.SCAbstractRenderModule;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiScreen;
import moonsense.features.SCModule;
import moonsense.ui.elements.Element;

public class ElementLocation extends Element
{
    private final SCModule module;
    private final GuiScreen parent;
    
    public ElementLocation(final SCModule module, final GuiScreen parent, final Consumer<Element> consumer) {
        super(0, 0, 0, 0, consumer);
        this.parent = parent;
        this.module = module;
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.enabled) {
            final int width = (int)(((SCAbstractRenderModule)this.module).getWidth() * ((SCAbstractRenderModule)this.module).getScale());
            final int height = (int)(((SCAbstractRenderModule)this.module).getHeight() * ((SCAbstractRenderModule)this.module).getScale());
            if (width == 0 || height == 0) {
                return;
            }
            final float x = (float)BoxUtils.getBoxOffX(this.module, (int)ModuleConfig.INSTANCE.getActualX(this.module), width);
            final float y = (float)BoxUtils.getBoxOffY(this.module, (int)ModuleConfig.INSTANCE.getActualY(this.module), height);
            this.hovered = (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height);
            if (this.hovered) {
                GuiHUDEditor.lastDragging = this.module;
            }
            GuiUtils.drawRect(x, y, x + width, y + height, this.hovered ? MoonsenseClient.getMainColor(50) : new Color(20, 20, 20, 50).getRGB());
            GuiUtils.drawRectOutline(x, y, x + width, y + height, this.hovered ? MoonsenseClient.getMainColor(255) : new Color(0, 0, 0, 255).getRGB());
            GlStateManager.pushMatrix();
            GlStateManager.scale(((SCAbstractRenderModule)this.module).getScale(), ((SCAbstractRenderModule)this.module).getScale(), 1.0f);
            GlStateManager.translate(BoxUtils.getBoxOffX(this.module, (int)ModuleConfig.INSTANCE.getActualX(this.module), ((SCAbstractRenderModule)this.module).getWidth()) / ((SCAbstractRenderModule)this.module).getScale(), BoxUtils.getBoxOffY(this.module, (int)ModuleConfig.INSTANCE.getActualY(this.module), ((SCAbstractRenderModule)this.module).getHeight()) / ((SCAbstractRenderModule)this.module).getScale(), 0.0f);
            ((SCAbstractRenderModule)this.module).renderDummy(0.0f, 0.0f);
            GlStateManager.popMatrix();
            if (this.module.settings.size() > 0 && this.hovered) {
                final int b = (height > 10) ? 10 : 8;
                GlStateManager.enableBlend();
                if (mouseX > x + width - b && mouseX < x + width && mouseY > y + height - b && mouseY < y + height) {
                    GuiUtils.setGlColor(new Color(0, 0, 0, 150).getRGB());
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/settings.png"));
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + width - b + 1.0f, y + height - b + 1.0f, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(MoonsenseClient.getMainColor(255));
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + width - b, y + height - b, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(new Color(0, 0, 0, 150).getRGB());
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/close.png"));
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + 1.0f, y + height - b + 1.0f, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(Color.white.darker().getRGB());
                    GuiUtils.drawModalRectWithCustomSizedTexture(x, y + height - b, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    if (Mouse.isButtonDown(0)) {
                        final GuiHUDEditor guiHUDEditor = (GuiHUDEditor)this.parent;
                        if (!GuiHUDEditor.isDraggingModule) {
                            if (this.module.isChildModule()) {
                                this.mc.displayGuiScreen(new GuiModuleSettings(this.module.getParentModule(), this.parent));
                            }
                            else {
                                this.mc.displayGuiScreen(new GuiModuleSettings(this.module, this.parent));
                            }
                        }
                    }
                }
                else if (mouseX > x && mouseX < x + b && mouseY > y + height - b && mouseY < y + height) {
                    GuiUtils.setGlColor(new Color(0, 0, 0, 150).getRGB());
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/close.png"));
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + 1.0f, y + height - b + 1.0f, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(Color.red.getRGB());
                    GuiUtils.drawModalRectWithCustomSizedTexture(x, y + height - b, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(new Color(0, 0, 0, 150).getRGB());
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/settings.png"));
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + width - b + 1.0f, y + height - b + 1.0f, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(Color.white.darker().getRGB());
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + width - b, y + height - b, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    if (Mouse.isButtonDown(0)) {
                        final GuiHUDEditor guiHUDEditor2 = (GuiHUDEditor)this.parent;
                        if (!GuiHUDEditor.isDraggingModule) {
                            ModuleConfig.INSTANCE.setEnabled(this.module, false);
                            this.enabled = false;
                        }
                    }
                }
                else {
                    GuiUtils.setGlColor(new Color(0, 0, 0, 150).getRGB());
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/settings.png"));
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + width - b + 1.0f, y + height - b + 1.0f, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(Color.white.darker().getRGB());
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + width - b, y + height - b, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(new Color(0, 0, 0, 150).getRGB());
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/close.png"));
                    GuiUtils.drawModalRectWithCustomSizedTexture(x + 1.0f, y + height - b + 1.0f, 0.0f, 0.0f, b, b, (float)b, (float)b);
                    GuiUtils.setGlColor(Color.white.darker().getRGB());
                    GuiUtils.drawModalRectWithCustomSizedTexture(x, y + height - b, 0.0f, 0.0f, b, b, (float)b, (float)b);
                }
            }
        }
    }
}
