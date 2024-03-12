// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.render.GradientUtil;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.utils.font.AbstractFontRenderer;
import net.minecraft.client.gui.Gui;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.GLUtil;
import dev.tenacity.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;
import dev.tenacity.commands.impl.FriendCommand;
import net.minecraft.util.StringUtils;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.event.impl.render.ShaderEvent;
import java.util.Iterator;
import dev.tenacity.utils.render.ESPUtil;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.render.NametagRenderEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.text.DecimalFormat;
import java.util.HashMap;
import dev.tenacity.module.Category;
import java.awt.Color;
import java.text.NumberFormat;
import org.lwjgl.util.vector.Vector4f;
import net.minecraft.entity.Entity;
import java.util.Map;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.Module;

public class ESP2D extends Module
{
    private final MultipleBoolSetting validEntities;
    private final BooleanSetting mcfont;
    public final BooleanSetting boxEsp;
    private final ModeSetting boxColorMode;
    private final ColorSetting boxColor;
    private final BooleanSetting itemHeld;
    private final BooleanSetting equipmentVisual;
    private final BooleanSetting healthBar;
    private final ModeSetting healthBarMode;
    private final BooleanSetting healthBarText;
    private final BooleanSetting nametags;
    private final NumberSetting scale;
    private final BooleanSetting redTags;
    private final MultipleBoolSetting nametagSettings;
    private final Map<Entity, Vector4f> entityPosition;
    private final NumberFormat df;
    private final Color backgroundColor;
    private Color firstColor;
    private Color secondColor;
    private Color thirdColor;
    private Color fourthColor;
    
    public ESP2D() {
        super("2DESP", "2D ESP", Category.RENDER, "Draws a box in 2D space around entitys");
        this.validEntities = new MultipleBoolSetting("Valid Entities", new BooleanSetting[] { new BooleanSetting("Players", true), new BooleanSetting("Animals", true), new BooleanSetting("Mobs", true) });
        this.mcfont = new BooleanSetting("Minecraft Font", true);
        this.boxEsp = new BooleanSetting("Box", true);
        this.boxColorMode = new ModeSetting("Box Mode", "Sync", new String[] { "Sync", "Custom" });
        this.boxColor = new ColorSetting("Box Color", Color.PINK);
        this.itemHeld = new BooleanSetting("Item Held Text", true);
        this.equipmentVisual = new BooleanSetting("Equipment", true);
        this.healthBar = new BooleanSetting("Health Bar", true);
        this.healthBarMode = new ModeSetting("Health Bar Mode", "Color", new String[] { "Health", "Color" });
        this.healthBarText = new BooleanSetting("Health Bar Text", true);
        this.nametags = new BooleanSetting("Nametags", true);
        this.scale = new NumberSetting("Tag Scale", 0.75, 1.0, 0.35, 0.05);
        this.redTags = new BooleanSetting("Red Tags", false);
        this.nametagSettings = new MultipleBoolSetting("Nametag Settings", new BooleanSetting[] { this.redTags, new BooleanSetting("Formatted Tags", false), new BooleanSetting("Add PostProcessing", false), new BooleanSetting("Health Text", true), new BooleanSetting("Background", true), new BooleanSetting("Red Background", false), new BooleanSetting("Round", true) });
        this.entityPosition = new HashMap<Entity, Vector4f>();
        this.df = new DecimalFormat("0.#");
        this.backgroundColor = new Color(10, 10, 10, 130);
        this.firstColor = Color.BLACK;
        this.secondColor = Color.BLACK;
        this.thirdColor = Color.BLACK;
        this.fourthColor = Color.BLACK;
        this.boxColorMode.addParent(this.boxEsp, ParentAttribute.BOOLEAN_CONDITION);
        this.boxColor.addParent(this.boxColorMode, modeSetting -> modeSetting.is("Custom"));
        this.scale.addParent(this.nametags, ParentAttribute.BOOLEAN_CONDITION);
        this.nametagSettings.addParent(this.nametags, ParentAttribute.BOOLEAN_CONDITION);
        this.healthBarMode.addParent(this.healthBar, ParentAttribute.BOOLEAN_CONDITION);
        this.healthBarText.addParent(this.healthBar, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.validEntities, this.mcfont, this.boxEsp, this.boxColorMode, this.boxColor, this.itemHeld, this.healthBar, this.healthBarMode, this.healthBarText, this.equipmentVisual, this.nametags, this.scale, this.nametagSettings);
    }
    
    @Override
    public void onNametagRenderEvent(final NametagRenderEvent e) {
        if (this.nametags.isEnabled()) {
            e.cancel();
        }
    }
    
    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        this.entityPosition.clear();
        for (final Entity entity : ESP2D.mc.theWorld.loadedEntityList) {
            if (this.shouldRender(entity) && ESPUtil.isInView(entity)) {
                this.entityPosition.put(entity, ESPUtil.getEntityPositionsOn2D(entity));
            }
        }
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent e) {
        if (this.nametagSettings.getSetting("Add PostProcessing").isEnabled() && this.nametags.isEnabled()) {
            for (final Entity entity : this.entityPosition.keySet()) {
                final Vector4f pos = this.entityPosition.get(entity);
                final float x = pos.getX();
                final float y = pos.getY();
                final float right = pos.getZ();
                final float bottom = pos.getW();
                if (entity instanceof EntityLivingBase) {
                    AbstractFontRenderer font = ESP2D.tenacityBoldFont20;
                    if (this.mcfont.isEnabled()) {
                        font = ESP2D.mc.fontRendererObj;
                    }
                    final EntityLivingBase renderingEntity = (EntityLivingBase)entity;
                    final String name = this.nametagSettings.getSetting("Formatted Tags").isEnabled() ? renderingEntity.getDisplayName().getFormattedText() : StringUtils.stripControlCodes(renderingEntity.getDisplayName().getUnformattedText());
                    final StringBuilder text = new StringBuilder((FriendCommand.isFriend(renderingEntity.getName()) ? "§d" : (this.redTags.isEnabled() ? "§c" : "§f")) + name);
                    if (this.nametagSettings.getSetting("Health Text").isEnabled()) {
                        text.append(String.format(" §7[§r%s HP§7]", this.df.format(renderingEntity.getHealth())));
                    }
                    final double fontScale = this.scale.getValue();
                    float middle = x + (right - x) / 2.0f;
                    float textWidth = 0.0f;
                    textWidth = font.getStringWidth(text.toString());
                    middle -= (float)(textWidth * fontScale / 2.0);
                    final double fontHeight = font.getHeight() * fontScale;
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)middle, y - (fontHeight + 2.0), 0.0);
                    GL11.glScaled(fontScale, fontScale, 1.0);
                    GL11.glTranslated((double)(-middle), -(y - (fontHeight + 2.0)), 0.0);
                    final Color backgroundTagColor = this.nametagSettings.getSetting("Red Background").isEnabled() ? Color.RED : Color.BLACK;
                    RenderUtil.resetColor();
                    GLUtil.startBlend();
                    if (this.nametagSettings.getSetting("Round").isEnabled()) {
                        RoundedUtil.drawRound(middle - 3.0f, (float)(y - (fontHeight + 7.0)), textWidth + 6.0f, (float)(fontHeight / fontScale + 4.0), 4.0f, backgroundTagColor);
                    }
                    else {
                        Gui.drawRect2(middle - 3.0f, (float)(y - (fontHeight + 7.0)), textWidth + 6.0f, fontHeight / fontScale + 4.0, backgroundTagColor.getRGB());
                    }
                    GL11.glPopMatrix();
                }
            }
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent e) {
        if (this.boxColorMode.is("Custom")) {
            if (this.boxColor.isRainbow()) {
                this.firstColor = this.boxColor.getRainbow().getColor(0);
                this.secondColor = this.boxColor.getRainbow().getColor(90);
                this.thirdColor = this.boxColor.getRainbow().getColor(180);
                this.fourthColor = this.boxColor.getRainbow().getColor(270);
            }
            else {
                this.gradientColorWheel(Pair.of(this.boxColor.getColor(), this.boxColor.getAltColor()));
            }
        }
        else if (HUDMod.isRainbowTheme()) {
            this.firstColor = HUDMod.color1.getRainbow().getColor(0);
            this.secondColor = HUDMod.color1.getRainbow().getColor(90);
            this.thirdColor = HUDMod.color1.getRainbow().getColor(180);
            this.fourthColor = HUDMod.color1.getRainbow().getColor(270);
        }
        else {
            this.gradientColorWheel(HUDMod.getClientColors());
        }
        for (final Entity entity : this.entityPosition.keySet()) {
            final Vector4f pos = this.entityPosition.get(entity);
            final float x = pos.getX();
            final float y = pos.getY();
            final float right = pos.getZ();
            final float bottom = pos.getW();
            if (entity instanceof EntityLivingBase) {
                AbstractFontRenderer font = ESP2D.tenacityBoldFont20;
                if (this.mcfont.isEnabled()) {
                    font = ESP2D.mc.fontRendererObj;
                }
                final EntityLivingBase renderingEntity = (EntityLivingBase)entity;
                if (this.nametags.isEnabled()) {
                    final float healthValue = renderingEntity.getHealth() / renderingEntity.getMaxHealth();
                    final Color healthColor = (healthValue > 0.75) ? new Color(66, 246, 123) : ((healthValue > 0.5) ? new Color(228, 255, 105) : ((healthValue > 0.35) ? new Color(236, 100, 64) : new Color(255, 65, 68)));
                    final String name = this.nametagSettings.getSetting("Formatted Tags").isEnabled() ? renderingEntity.getDisplayName().getFormattedText() : StringUtils.stripControlCodes(renderingEntity.getDisplayName().getUnformattedText());
                    final StringBuilder text = new StringBuilder((FriendCommand.isFriend(renderingEntity.getName()) ? "§d" : (this.redTags.isEnabled() ? "§c" : "§f")) + name);
                    if (this.nametagSettings.getSetting("Health Text").isEnabled()) {
                        text.append(String.format(" §7[§r%s HP§7]", this.df.format(renderingEntity.getHealth())));
                    }
                    final double fontScale = this.scale.getValue();
                    float middle = x + (right - x) / 2.0f;
                    final double fontHeight = font.getHeight() * fontScale;
                    final float textWidth = font.getStringWidth(text.toString());
                    middle -= (float)(textWidth * fontScale / 2.0);
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)middle, y - (fontHeight + 2.0), 0.0);
                    GL11.glScaled(fontScale, fontScale, 1.0);
                    GL11.glTranslated((double)(-middle), -(y - (fontHeight + 2.0)), 0.0);
                    if (this.nametagSettings.getSetting("Background").isEnabled()) {
                        final Color backgroundTagColor = this.nametagSettings.getSetting("Red Background").isEnabled() ? ColorUtil.applyOpacity(Color.RED, 0.65f) : this.backgroundColor;
                        if (this.nametagSettings.getSetting("Round").isEnabled()) {
                            RoundedUtil.drawRound(middle - 3.0f, (float)(y - (fontHeight + 7.0)), textWidth + 6.0f, (float)(fontHeight / fontScale + 4.0), 4.0f, backgroundTagColor);
                        }
                        else {
                            Gui.drawRect2(middle - 3.0f, (float)(y - (fontHeight + 7.0)), textWidth + 6.0f, fontHeight / fontScale + 4.0, backgroundTagColor.getRGB());
                        }
                    }
                    RenderUtil.resetColor();
                    if (this.mcfont.isEnabled()) {
                        RenderUtil.resetColor();
                        ESP2D.mc.fontRendererObj.drawString(StringUtils.stripControlCodes(text.toString()), middle + 0.5f, (float)(y - (fontHeight + 4.0)) + 0.5f, Color.BLACK);
                        RenderUtil.resetColor();
                        ESP2D.mc.fontRendererObj.drawString(text.toString(), middle, (float)(y - (fontHeight + 4.0)), healthColor.getRGB());
                    }
                    else {
                        ESP2D.tenacityBoldFont20.drawSmoothStringWithShadow(text.toString(), middle, (float)(y - (fontHeight + 5.0)), healthColor.getRGB());
                    }
                    GL11.glPopMatrix();
                }
                if (this.itemHeld.isEnabled() && renderingEntity.getHeldItem() != null) {
                    final float fontScale2 = 0.5f;
                    float middle2 = x + (right - x) / 2.0f;
                    final String text2 = renderingEntity.getHeldItem().getDisplayName();
                    final float textWidth2 = font.getStringWidth(text2);
                    middle2 -= textWidth2 * fontScale2 / 2.0f;
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)middle2, (double)(bottom + 4.0f), 0.0);
                    GL11.glScaled((double)fontScale2, (double)fontScale2, 1.0);
                    GL11.glTranslated((double)(-middle2), (double)(-(bottom + 4.0f)), 0.0);
                    GlStateManager.bindTexture(0);
                    RenderUtil.resetColor();
                    Gui.drawRect2(middle2 - 3.0f, bottom + 1.0f, font.getStringWidth(text2) + 6.0f, font.getHeight() + 5, this.backgroundColor.getRGB());
                    if (this.mcfont.isEnabled()) {
                        ESP2D.mc.fontRendererObj.drawStringWithShadow(text2, middle2, bottom + 4.0f, -1);
                    }
                    else {
                        ESP2D.tenacityBoldFont20.drawSmoothStringWithShadow(text2, middle2, bottom + 4.0f, -1);
                    }
                    GL11.glPopMatrix();
                }
                if (this.equipmentVisual.isEnabled()) {
                    final float scale = 0.4f;
                    final float equipmentX = right + 5.0f;
                    final float equipmentY = y - 1.0f;
                    GL11.glPushMatrix();
                    GL11.glTranslated((double)equipmentX, (double)equipmentY, 0.0);
                    GL11.glScaled((double)scale, (double)scale, 1.0);
                    GL11.glTranslated((double)(-equipmentX), (double)(-y), 0.0);
                    RenderUtil.resetColor();
                    RenderHelper.enableGUIStandardItemLighting();
                    float seperation = 0.0f;
                    final float length = bottom - y - 2.0f;
                    for (int i = 3; i >= 0; --i) {
                        if (renderingEntity.getCurrentArmor(i) == null) {
                            seperation += length / 3.0f / scale;
                        }
                        else {
                            ESP2D.mc.getRenderItem().renderItemAndEffectIntoGUI(renderingEntity.getCurrentArmor(i), (int)equipmentX, (int)(equipmentY + seperation));
                            seperation += length / 3.0f / scale;
                        }
                    }
                    RenderHelper.disableStandardItemLighting();
                    GL11.glPopMatrix();
                }
                if (this.healthBar.isEnabled()) {
                    float healthValue = renderingEntity.getHealth() / renderingEntity.getMaxHealth();
                    final Color healthColor = (healthValue > 0.75) ? new Color(66, 246, 123) : ((healthValue > 0.5) ? new Color(228, 255, 105) : ((healthValue > 0.35) ? new Color(236, 100, 64) : new Color(255, 65, 68)));
                    final float height = bottom - y + 1.0f;
                    Gui.drawRect2(x - 3.5f, y - 0.5f, 2.0, height + 1.0f, new Color(0, 0, 0, 180).getRGB());
                    if (this.healthBarMode.is("Color")) {
                        GradientUtil.drawGradientTB(x - 3.0f, y, 1.0f, height, 0.3f, this.firstColor, this.fourthColor);
                        GradientUtil.drawGradientTB(x - 3.0f, y + (height - height * healthValue), 1.0f, height * healthValue, 1.0f, this.firstColor, this.fourthColor);
                    }
                    else {
                        Gui.drawRect2(x - 3.0f, y, 1.0, height, ColorUtil.applyOpacity(healthColor, 0.3f).getRGB());
                        Gui.drawRect2(x - 3.0f, y + (height - height * healthValue), 1.0, height * healthValue, healthColor.getRGB());
                    }
                    if (this.healthBarText.isEnabled()) {
                        healthValue *= 100.0f;
                        final String health = String.valueOf(MathUtils.round(healthValue, 1)).substring(0, (healthValue == 100.0f) ? 3 : 2);
                        final String text3 = health + "%";
                        final double fontScale3 = 0.5;
                        final float textX = x - (font.getStringWidth(text3) / 2.0f + 2.0f);
                        final float fontHeight2 = this.mcfont.isEnabled() ? ((float)(ESP2D.mc.fontRendererObj.FONT_HEIGHT * fontScale3)) : ((float)(ESP2D.tenacityBoldFont20.getHeight() * fontScale3));
                        final float newHeight = height - fontHeight2;
                        final float textY = y + (newHeight - newHeight * (healthValue / 100.0f));
                        GL11.glPushMatrix();
                        GL11.glTranslated((double)(textX - 5.0f), (double)textY, 1.0);
                        GL11.glScaled(fontScale3, fontScale3, 1.0);
                        GL11.glTranslated((double)(-(textX - 5.0f)), (double)(-textY), 1.0);
                        if (this.mcfont.isEnabled()) {
                            ESP2D.mc.fontRendererObj.drawStringWithShadow(text3, textX, textY, -1);
                        }
                        else {
                            ESP2D.tenacityBoldFont20.drawSmoothStringWithShadow(text3, textX, textY, -1);
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
            if (this.boxEsp.isEnabled()) {
                final float outlineThickness = 0.5f;
                RenderUtil.resetColor();
                GradientUtil.drawGradientLR(x, y, right - x, 1.0f, 1.0f, this.firstColor, this.secondColor);
                GradientUtil.drawGradientTB(x, y, 1.0f, bottom - y, 1.0f, this.firstColor, this.fourthColor);
                GradientUtil.drawGradientLR(x, bottom, right - x, 1.0f, 1.0f, this.fourthColor, this.thirdColor);
                GradientUtil.drawGradientTB(right, y, 1.0f, bottom - y + 1.0f, 1.0f, this.secondColor, this.thirdColor);
                Gui.drawRect2(x - 0.5f, y - outlineThickness, right - x + 2.0f, outlineThickness, Color.BLACK.getRGB());
                Gui.drawRect2(x - outlineThickness, y, outlineThickness, bottom - y + 1.0f, Color.BLACK.getRGB());
                Gui.drawRect2(x - 0.5f, bottom + 1.0f, right - x + 2.0f, outlineThickness, Color.BLACK.getRGB());
                Gui.drawRect2(right + 1.0f, y, outlineThickness, bottom - y + 1.0f, Color.BLACK.getRGB());
                Gui.drawRect2(x + 1.0f, y + 1.0f, right - x - 1.0f, outlineThickness, Color.BLACK.getRGB());
                Gui.drawRect2(x + 1.0f, y + 1.0f, outlineThickness, bottom - y - 1.0f, Color.BLACK.getRGB());
                Gui.drawRect2(x + 1.0f, bottom - outlineThickness, right - x - 1.0f, outlineThickness, Color.BLACK.getRGB());
                Gui.drawRect2(right - outlineThickness, y + 1.0f, outlineThickness, bottom - y - 1.0f, Color.BLACK.getRGB());
            }
        }
    }
    
    private void gradientColorWheel(final Pair<Color, Color> colors) {
        this.firstColor = ColorUtil.interpolateColorsBackAndForth(15, 0, colors.getFirst(), colors.getSecond(), false);
        this.secondColor = ColorUtil.interpolateColorsBackAndForth(15, 90, colors.getFirst(), colors.getSecond(), false);
        this.thirdColor = ColorUtil.interpolateColorsBackAndForth(15, 180, colors.getFirst(), colors.getSecond(), false);
        this.fourthColor = ColorUtil.interpolateColorsBackAndForth(15, 270, colors.getFirst(), colors.getSecond(), false);
    }
    
    private boolean shouldRender(final Entity entity) {
        if (entity.isDead || entity.isInvisible()) {
            return false;
        }
        if (!this.validEntities.getSetting("Players").isEnabled() || !(entity instanceof EntityPlayer)) {
            return (this.validEntities.getSetting("Animals").isEnabled() && entity instanceof EntityAnimal) || (this.validEntities.getSetting("mobs").isEnabled() && entity instanceof EntityMob);
        }
        if (entity == ESP2D.mc.thePlayer) {
            return ESP2D.mc.gameSettings.thirdPersonView != 0;
        }
        return !entity.getDisplayName().getUnformattedText().contains("[NPC");
    }
}
