// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import moonsense.event.impl.SCClientTickEvent;
import net.minecraft.potion.Potion;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.event.impl.SCRenderTooltipEvent;
import moonsense.event.SubscribeEvent;
import net.minecraft.util.FoodStats;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.entity.EntityPlayerSP;
import moonsense.utils.appleskin.FoodHelper;
import net.minecraft.entity.player.EntityPlayer;
import moonsense.utils.appleskin.HungerHelper;
import net.minecraft.client.Minecraft;
import moonsense.event.impl.SCRenderEvent;
import moonsense.features.SCModule;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class SaturationModule extends SCDefaultRenderModule
{
    public static SaturationModule INSTANCE;
    private final Setting appleSkinGroup;
    private final Setting showSaturationOverlay;
    private final Setting showTooltipInfo;
    private final Setting showPotentialHunger;
    private final Setting showPotentialSaturation;
    private final Setting showPotentialExhaustion;
    private final Setting showFoodValues;
    private float flashAlpha;
    private byte alphaDir;
    protected int foodIconsOffset;
    public ItemStack cachedTooltipStack;
    private final ResourceLocation modIcons;
    
    public SaturationModule() {
        super("Saturation", "Display your saturation level on the HUD.");
        this.flashAlpha = 0.0f;
        this.alphaDir = 1;
        this.modIcons = new ResourceLocation("streamlined/icons/modules/appleskin-icons.png");
        SaturationModule.INSTANCE = this;
        new Setting(this, "Apple Skin");
        this.appleSkinGroup = new Setting(this, "Apple Skin Options").setDefault(new Setting.CompoundSettingGroup("Apple Skin", new Setting[] { this.showSaturationOverlay = new Setting(null, "Show Saturation Overlay").setDefault(true).compound(true), this.showTooltipInfo = new Setting(null, "Show Tooltip Info").setDefault(true).compound(true), this.showPotentialHunger = new Setting(null, "Show Potential Hunger").setDefault(true).compound(true), this.showPotentialSaturation = new Setting(null, "Show Potential Saturation").setDefault(true).compound(true), this.showPotentialExhaustion = new Setting(null, "Show Potential Exhaustion").setDefault(true).compound(true), this.showFoodValues = new Setting(null, "Show Food Values").setDefault(true).compound(true) }));
    }
    
    @SubscribeEvent
    public void onRender(final SCRenderEvent event) {
        this.foodIconsOffset = 39;
        if (this.showPotentialExhaustion.getBoolean()) {
            final Minecraft mc = Minecraft.getMinecraft();
            final EntityPlayerSP player = mc.thePlayer;
            final ScaledResolution scale = event.resolution;
            final int left = scale.getScaledWidth() / 2 + 91;
            final int top = scale.getScaledHeight() - this.foodIconsOffset;
            this.drawExhaustionOverlay(HungerHelper.getExhaustion(player), mc, left, top, 1.0f);
        }
        if (this.showFoodValues.getBoolean() || this.showSaturationOverlay.getBoolean()) {
            final Minecraft mc = Minecraft.getMinecraft();
            final EntityPlayerSP player = mc.thePlayer;
            final ItemStack heldItem = player.getHeldItem();
            final FoodStats stats = player.getFoodStats();
            final ScaledResolution scale2 = event.resolution;
            final int left2 = scale2.getScaledWidth() / 2 + 91;
            final int top2 = scale2.getScaledHeight() - this.foodIconsOffset;
            if (this.showSaturationOverlay.getBoolean()) {
                this.drawSaturationOverlay(0.0f, stats.getSaturationLevel(), mc, left2, top2, 1.0f);
            }
            if (this.showFoodValues.getBoolean() && heldItem != null && FoodHelper.isFood(heldItem)) {
                final FoodHelper.BasicFoodValues foodValues = FoodHelper.getModifiedFoodValues(heldItem, player);
                drawHungerOverlay(foodValues.hunger, stats.getFoodLevel(), mc, left2, top2, this.flashAlpha);
                if (this.showSaturationOverlay.getBoolean()) {
                    final int newFoodValue = stats.getFoodLevel() + foodValues.hunger;
                    final float newSaturationValue = stats.getSaturationLevel() + foodValues.getSaturationIncrement();
                    this.drawSaturationOverlay((newSaturationValue > newFoodValue) ? (newFoodValue - stats.getSaturationLevel()) : foodValues.getSaturationIncrement(), stats.getSaturationLevel(), mc, left2, top2, this.flashAlpha);
                }
            }
            else {
                this.flashAlpha = 0.0f;
                this.alphaDir = 1;
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderTooltip(final SCRenderTooltipEvent.PostText event) {
        final ItemStack hoveredStack = event.getStack();
        if (hoveredStack != null) {
            final boolean shouldShowTooltip = this.showTooltipInfo.getBoolean();
            if (shouldShowTooltip) {
                final Minecraft mc = Minecraft.getMinecraft();
                final GuiScreen gui = mc.currentScreen;
                if (gui != null && FoodHelper.isFood(hoveredStack)) {
                    final EntityPlayerSP player = mc.thePlayer;
                    final ScaledResolution scale = new ScaledResolution(mc);
                    final int toolTipY = event.getY();
                    final int toolTipX = event.getX();
                    final int toolTipW = event.getWidth();
                    final int toolTipH = event.getHeight();
                    final FoodHelper.BasicFoodValues defaultFoodValues = FoodHelper.getDefaultFoodValues(hoveredStack);
                    final FoodHelper.BasicFoodValues modifiedFoodValues = FoodHelper.getModifiedFoodValues(hoveredStack, player);
                    if (!defaultFoodValues.equals(modifiedFoodValues) || defaultFoodValues.hunger != 0) {
                        final int biggestHunger = Math.max(defaultFoodValues.hunger, modifiedFoodValues.hunger);
                        final float biggestSaturationIncrement = Math.max(defaultFoodValues.getSaturationIncrement(), modifiedFoodValues.getSaturationIncrement());
                        int barsNeeded = (int)Math.ceil(Math.abs(biggestHunger) / 2.0f);
                        final boolean hungerOverflow = barsNeeded > 10;
                        final String hungerText = hungerOverflow ? (String.valueOf(((biggestHunger < 0) ? -1 : 1) * barsNeeded) + "x ") : null;
                        if (hungerOverflow) {
                            barsNeeded = 1;
                        }
                        int saturationBarsNeeded = (int)Math.max(1.0, Math.ceil(Math.abs(biggestSaturationIncrement) / 2.0f));
                        final boolean saturationOverflow = saturationBarsNeeded > 10;
                        final String saturationText = saturationOverflow ? (String.valueOf(((biggestSaturationIncrement < 0.0f) ? -1 : 1) * saturationBarsNeeded) + "x ") : null;
                        if (saturationOverflow) {
                            saturationBarsNeeded = 1;
                        }
                        final int toolTipBottomY = toolTipY + toolTipH + 1 + 3;
                        final int toolTipRightX = toolTipX + toolTipW + 1 + 3;
                        final boolean shouldDrawBelow = toolTipBottomY + 20 < scale.getScaledHeight() - 3;
                        final int rightX = toolTipRightX - 3;
                        final int leftX = rightX - Math.max(barsNeeded * 9 + (int)(mc.fontRendererObj.getStringWidth(hungerText) * 0.75f), saturationBarsNeeded * 6 + (int)(mc.fontRendererObj.getStringWidth(saturationText) * 0.75f)) - 3;
                        final int topY = shouldDrawBelow ? toolTipBottomY : (toolTipY - 20 - 3);
                        final int bottomY = topY + 19;
                        GlStateManager.disableLighting();
                        GlStateManager.disableDepth();
                        Gui.drawRect(leftX - 1, topY, rightX + 1, bottomY, -267386864);
                        Gui.drawRect(leftX, shouldDrawBelow ? bottomY : (topY - 1), rightX, shouldDrawBelow ? (bottomY + 1) : topY, -267386864);
                        Gui.drawRect(leftX, topY, rightX, bottomY, 1728053247);
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(770, 771);
                        int x = rightX - 2;
                        int y = bottomY - 18;
                        mc.getTextureManager().bindTexture(Gui.icons);
                        for (int modifiedSaturationIncrement = 0; modifiedSaturationIncrement < barsNeeded * 2; modifiedSaturationIncrement += 2) {
                            x -= 9;
                            if (modifiedFoodValues.hunger < 0) {
                                gui.drawTexturedModalRect(x, y, 34, 27, 9, 9);
                            }
                            else if (modifiedFoodValues.hunger > defaultFoodValues.hunger && defaultFoodValues.hunger <= modifiedSaturationIncrement) {
                                gui.drawTexturedModalRect(x, y, 133, 27, 9, 9);
                            }
                            else if (modifiedFoodValues.hunger <= modifiedSaturationIncrement + 1 && defaultFoodValues.hunger != modifiedFoodValues.hunger) {
                                if (modifiedFoodValues.hunger == modifiedSaturationIncrement + 1) {
                                    gui.drawTexturedModalRect(x, y, 124, 27, 9, 9);
                                }
                                else {
                                    gui.drawTexturedModalRect(x, y, 34, 27, 9, 9);
                                }
                            }
                            else {
                                gui.drawTexturedModalRect(x, y, 16, 27, 9, 9);
                            }
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 0.25f);
                            gui.drawTexturedModalRect(x, y, (defaultFoodValues.hunger - 1 == modifiedSaturationIncrement) ? 115 : 106, 27, 9, 9);
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            if (modifiedFoodValues.hunger > modifiedSaturationIncrement) {
                                gui.drawTexturedModalRect(x, y, (modifiedFoodValues.hunger - 1 == modifiedSaturationIncrement) ? 61 : 52, 27, 9, 9);
                            }
                        }
                        if (hungerText != null) {
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(0.75f, 0.75f, 0.75f);
                            mc.fontRendererObj.drawString(hungerText, (float)(x * 4 / 3 - mc.fontRendererObj.getStringWidth(hungerText) + 2), (float)(y * 4 / 3 + 2), -2236963, true);
                            GlStateManager.popMatrix();
                        }
                        y += 10;
                        x = rightX - 2;
                        final float modifiedSaturationIncrement2 = modifiedFoodValues.getSaturationIncrement();
                        final float absModifiedSaturationIncrement = Math.abs(modifiedSaturationIncrement2);
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        mc.getTextureManager().bindTexture(this.modIcons);
                        for (int i = 0; i < saturationBarsNeeded * 2; i += 2) {
                            final float effectiveSaturationOfBar = (absModifiedSaturationIncrement - i) / 2.0f;
                            x -= 6;
                            final boolean shouldBeFaded = absModifiedSaturationIncrement <= i;
                            if (shouldBeFaded) {
                                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
                            }
                            gui.drawTexturedModalRect(x, y, (effectiveSaturationOfBar >= 1.0f) ? 21 : ((effectiveSaturationOfBar > 0.5) ? 14 : ((effectiveSaturationOfBar > 0.25) ? 7 : ((effectiveSaturationOfBar > 0.0f) ? 0 : 28))), (modifiedSaturationIncrement2 >= 0.0f) ? 27 : 34, 7, 7);
                            if (shouldBeFaded) {
                                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            }
                        }
                        if (saturationText != null) {
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(0.75f, 0.75f, 0.75f);
                            mc.fontRendererObj.drawString(saturationText, (float)(x * 4 / 3 - mc.fontRendererObj.getStringWidth(saturationText) + 2), (float)(y * 4 / 3 + 1), -2236963, true);
                            GlStateManager.popMatrix();
                        }
                        GlStateManager.disableBlend();
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        GlStateManager.disableRescaleNormal();
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.disableLighting();
                        GlStateManager.disableDepth();
                    }
                }
            }
        }
    }
    
    private void drawSaturationOverlay(final float saturationGained, final float saturationLevel, final Minecraft mc, final int left, final int top, final float alpha) {
        if (mc.thePlayer.capabilities.isCreativeMode) {
            return;
        }
        if (saturationLevel + saturationGained >= 0.0f) {
            final int startBar = (saturationGained != 0.0f) ? Math.max(0, (int)saturationLevel / 2) : 0;
            final int endBar = (int)Math.ceil(Math.min(20.0f, saturationLevel + saturationGained) / 2.0f);
            final int barsNeeded = endBar - startBar;
            mc.getTextureManager().bindTexture(this.modIcons);
            enableAlpha(alpha);
            for (int i = startBar; i < startBar + barsNeeded; ++i) {
                final int x = left - i * 8 - 9;
                final float effectiveSaturationOfBar = (saturationLevel + saturationGained) / 2.0f - i;
                if (effectiveSaturationOfBar >= 1.0f) {
                    mc.ingameGUI.drawTexturedModalRect(x, top, 27, 0, 9, 9);
                }
                else if (effectiveSaturationOfBar > 0.5) {
                    mc.ingameGUI.drawTexturedModalRect(x, top, 18, 0, 9, 9);
                }
                else if (effectiveSaturationOfBar > 0.25) {
                    mc.ingameGUI.drawTexturedModalRect(x, top, 9, 0, 9, 9);
                }
                else if (effectiveSaturationOfBar > 0.0f) {
                    mc.ingameGUI.drawTexturedModalRect(x, top, 0, 0, 9, 9);
                }
            }
            disableAlpha(alpha);
            mc.getTextureManager().bindTexture(Gui.icons);
        }
    }
    
    public static void drawHungerOverlay(final int hungerRestored, final int foodLevel, final Minecraft mc, final int left, final int top, final float alpha) {
        if (hungerRestored != 0) {
            final int startBar = foodLevel / 2;
            final int endBar = (int)Math.ceil(Math.min(20, foodLevel + hungerRestored) / 2.0f);
            final int barsNeeded = endBar - startBar;
            mc.getTextureManager().bindTexture(Gui.icons);
            enableAlpha(alpha);
            for (int i = startBar; i < startBar + barsNeeded; ++i) {
                final int idx = i * 2 + 1;
                final int x = left - i * 8 - 9;
                int icon = 16;
                byte background = 13;
                if (mc.thePlayer.isPotionActive(Potion.hunger)) {
                    icon += 36;
                    background = 13;
                }
                mc.ingameGUI.drawTexturedModalRect(x, top, 16 + background * 9, 27, 9, 9);
                if (idx < foodLevel + hungerRestored) {
                    mc.ingameGUI.drawTexturedModalRect(x, top, icon + 36, 27, 9, 9);
                }
                else if (idx == foodLevel + hungerRestored) {
                    mc.ingameGUI.drawTexturedModalRect(x, top, icon + 45, 27, 9, 9);
                }
            }
            disableAlpha(alpha);
        }
    }
    
    private void drawExhaustionOverlay(final float exhaustion, final Minecraft mc, final int left, final int top, final float alpha) {
        mc.getTextureManager().bindTexture(this.modIcons);
        final float maxExhaustion = HungerHelper.getMaxExhaustion(mc.thePlayer);
        final float ratio = exhaustion / maxExhaustion;
        final int width = (int)(ratio * 81.0f);
        final byte height = 9;
        enableAlpha(0.75f);
        mc.ingameGUI.drawTexturedModalRect(left - width, top, 81 - width, 18, width, height);
        disableAlpha(0.75f);
        mc.getTextureManager().bindTexture(Gui.icons);
    }
    
    public static void enableAlpha(final float alpha) {
        GlStateManager.enableBlend();
        if (alpha != 1.0f) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
            GlStateManager.blendFunc(770, 771);
        }
    }
    
    public static void disableAlpha(final float alpha) {
        GlStateManager.disableBlend();
        if (alpha != 1.0f) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @SubscribeEvent
    public void onClientTick(final SCClientTickEvent event) {
        this.flashAlpha += this.alphaDir * 0.125f;
        if (this.flashAlpha >= 1.5f) {
            this.flashAlpha = 1.0f;
            this.alphaDir = -1;
        }
        else if (this.flashAlpha <= -0.5f) {
            this.flashAlpha = 0.0f;
            this.alphaDir = 1;
        }
    }
    
    @Override
    public Object getValue() {
        return new StringBuilder().append((int)this.mc.thePlayer.getFoodStats().getSaturationLevel()).toString();
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.BOTTOM_RIGHT;
    }
}
