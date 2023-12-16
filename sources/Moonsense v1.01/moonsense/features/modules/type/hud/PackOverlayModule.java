// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.utils.ColorObject;
import java.util.List;
import java.util.Iterator;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.resources.ResourcePackRepository;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import moonsense.features.SCModule;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class PackOverlayModule extends SCDefaultRenderModule
{
    private int width;
    private final Setting showIcon;
    private final Setting showDescription;
    private final Setting overrideDefaultTextColors;
    private final Setting borderSurroundsImage;
    private final Setting titleColor;
    private final Setting descriptionColor;
    private final Setting onlyRenderFirstPack;
    
    public PackOverlayModule() {
        super("Pack Overlay", "Displays your top-level resource pack on the HUD.");
        new Setting(this, "Pack Display Settings");
        this.showIcon = new Setting(this, "Show Icon").setDefault(true);
        this.showDescription = new Setting(this, "Show description").setDefault(true);
        this.overrideDefaultTextColors = new Setting(this, "Override Default Text Colors").setDefault(false);
        this.borderSurroundsImage = new Setting(this, "Border Surrounds Image").setDefault(true);
        this.titleColor = new Setting(this, "Title Text Color").setDefault(new Color(16777215).getRGB(), 0);
        this.descriptionColor = new Setting(this, "Description Text Color").setDefault(new Color(8421504).getRGB(), 0);
        this.onlyRenderFirstPack = new Setting(this, "Only Render First Resource Pack").setDefault(true);
        this.settings.remove(this.backgroundWidth);
        this.settings.remove(this.backgroundHeight);
        this.settings.remove(this.brackets);
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHeight() {
        if (this.onlyRenderFirstPack.getBoolean()) {
            return 32;
        }
        return this.mc.getResourcePackRepository().getRepositoryEntries().size() * 32 + (this.mc.getResourcePackRepository().getRepositoryEntries().size() - 1) * 2;
    }
    
    @Override
    public void render(final float x, final float y) {
        GlStateManager.pushMatrix();
        this.width = 0;
        int offset = 0;
        for (final Object entry : Lists.reverse(this.mc.getResourcePackRepository().getRepositoryEntries())) {
            this.width = Math.max(this.width, this.mc.fontRendererObj.getStringWidth(((ResourcePackRepository.Entry)entry).getResourcePackName()) + (this.showIcon.getBoolean() ? 38 : 4));
            if (this.showDescription.getBoolean()) {
                final List<String> list = (List<String>)this.mc.fontRendererObj.listFormattedStringToWidth(((ResourcePackRepository.Entry)entry).getTexturePackDescription(), 157);
                for (int l = 0; l < 2 && l < list.size(); ++l) {
                    this.width = Math.max(this.width, this.mc.fontRendererObj.getStringWidth(list.get(l)) + (this.showIcon.getBoolean() ? 38 : 4));
                }
            }
        }
        for (final Object entry : Lists.reverse(this.mc.getResourcePackRepository().getRepositoryEntries())) {
            if (this.background.getBoolean()) {
                GuiUtils.drawRect(x, y + offset, x + this.width, y + offset + 32.0f, this.backgroundColor.getColor());
            }
            if (this.showIcon.getBoolean()) {
                ((ResourcePackRepository.Entry)entry).bindTexturePackIcon(this.mc.getTextureManager());
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GuiUtils.drawModalRectWithCustomSizedTexture(x, y + offset, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
                GlStateManager.enableTexture2D();
            }
            final ColorObject titleColor = this.overrideDefaultTextColors.getBoolean() ? this.titleColor.getColorObject() : this.textColor.getColorObject();
            final ColorObject descColor = this.overrideDefaultTextColors.getBoolean() ? this.descriptionColor.getColorObject() : this.textColor.getColorObject();
            this.drawString(((ResourcePackRepository.Entry)entry).getResourcePackName(), x + (this.showIcon.getBoolean() ? 36 : 2), y + (this.showDescription.getBoolean() ? 0 : 10) + offset + 2.0f, titleColor, this.textShadow.getBoolean());
            if (this.showDescription.getBoolean()) {
                final List<String> list2 = (List<String>)this.mc.fontRendererObj.listFormattedStringToWidth(((ResourcePackRepository.Entry)entry).getTexturePackDescription(), 157);
                for (int i = 0; i < 2 && i < list2.size(); ++i) {
                    this.drawString(list2.get(i), x + (this.showIcon.getBoolean() ? 36 : 2), y + offset + 12.0f + 10 * i, descColor, this.textShadow.getBoolean());
                }
            }
            if (this.border.getBoolean()) {
                if (this.borderSurroundsImage.getBoolean()) {
                    if (this.showIcon.getBoolean()) {
                        GuiUtils.drawRoundedOutline(x, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                    }
                    else {
                        GuiUtils.drawRoundedOutline(x, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                    }
                }
                else if (this.showIcon.getBoolean()) {
                    GuiUtils.drawRoundedOutline(x + 32.0f, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                }
                else {
                    GuiUtils.drawRoundedOutline(x, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                }
            }
            offset += 34;
            if (this.onlyRenderFirstPack.getBoolean()) {
                break;
            }
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        GlStateManager.pushMatrix();
        this.width = 0;
        int offset = 0;
        for (final Object entry : Lists.reverse(this.mc.getResourcePackRepository().getRepositoryEntries())) {
            this.width = Math.max(this.width, this.mc.fontRendererObj.getStringWidth(((ResourcePackRepository.Entry)entry).getResourcePackName()) + (this.showIcon.getBoolean() ? 38 : 4));
            if (this.showDescription.getBoolean()) {
                final List<String> list = (List<String>)this.mc.fontRendererObj.listFormattedStringToWidth(((ResourcePackRepository.Entry)entry).getTexturePackDescription(), 157);
                for (int l = 0; l < 2 && l < list.size(); ++l) {
                    this.width = Math.max(this.width, this.mc.fontRendererObj.getStringWidth(list.get(l)) + (this.showIcon.getBoolean() ? 38 : 4));
                }
            }
        }
        for (final Object entry : Lists.reverse(this.mc.getResourcePackRepository().getRepositoryEntries())) {
            if (this.background.getBoolean()) {
                GuiUtils.drawRect(x, y + offset, x + this.width, y + offset + 32.0f, this.backgroundColor.getColor());
            }
            if (this.showIcon.getBoolean()) {
                ((ResourcePackRepository.Entry)entry).bindTexturePackIcon(this.mc.getTextureManager());
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GuiUtils.drawModalRectWithCustomSizedTexture(x, y + offset, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
                GlStateManager.enableTexture2D();
            }
            final ColorObject titleColor = this.overrideDefaultTextColors.getBoolean() ? this.titleColor.getColorObject() : this.textColor.getColorObject();
            final ColorObject descColor = this.overrideDefaultTextColors.getBoolean() ? this.descriptionColor.getColorObject() : this.textColor.getColorObject();
            this.drawString(((ResourcePackRepository.Entry)entry).getResourcePackName(), x + (this.showIcon.getBoolean() ? 36 : 2), y + (this.showDescription.getBoolean() ? 0 : 10) + offset + 2.0f, titleColor, this.textShadow.getBoolean());
            if (this.showDescription.getBoolean()) {
                final List<String> list2 = (List<String>)this.mc.fontRendererObj.listFormattedStringToWidth(((ResourcePackRepository.Entry)entry).getTexturePackDescription(), 157);
                for (int i = 0; i < 2 && i < list2.size(); ++i) {
                    this.drawString(list2.get(i), x + (this.showIcon.getBoolean() ? 36 : 2), y + offset + 12.0f + 10 * i, descColor, this.textShadow.getBoolean());
                }
            }
            if (this.border.getBoolean()) {
                if (this.borderSurroundsImage.getBoolean()) {
                    if (this.showIcon.getBoolean()) {
                        GuiUtils.drawRoundedOutline(x, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                    }
                    else {
                        GuiUtils.drawRoundedOutline(x, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                    }
                }
                else if (this.showIcon.getBoolean()) {
                    GuiUtils.drawRoundedOutline(x + 32.0f, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                }
                else {
                    GuiUtils.drawRoundedOutline(x, y + offset, x + this.width, y + offset + 32.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                }
            }
            offset += 34;
            if (this.onlyRenderFirstPack.getBoolean()) {
                break;
            }
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_RIGHT;
    }
}
