/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.math.MathKt
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.renderer.entity.IRenderManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.ui.font.AWTFontRenderer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="NameTags", description="Changes the scale of the nametags so you can always read them.", category=ModuleCategory.RENDER)
public final class NameTags
extends Module {
    private final FontValue fontValue;
    private final BoolValue borderValue;
    private final BoolValue distanceValue;
    private final BoolValue healthValue = new BoolValue("Health", true);
    private final FloatValue scaleValue;
    private final BoolValue clearNamesValue;
    private final BoolValue pingValue = new BoolValue("Ping", true);
    private final BoolValue armorValue;

    public NameTags() {
        this.distanceValue = new BoolValue("Distance", false);
        this.armorValue = new BoolValue("Armor", true);
        this.clearNamesValue = new BoolValue("ClearNames", false);
        this.fontValue = new FontValue("Font", Fonts.roboto40);
        this.borderValue = new BoolValue("Border", true);
        this.scaleValue = new FloatValue("Scale", 1.0f, 1.0f, 4.0f);
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        GL11.glPushAttrib((int)8192);
        GL11.glPushMatrix();
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
            String string;
            if (!EntityUtils.isSelected(iEntity, false)) continue;
            IEntityLivingBase iEntityLivingBase = iEntity.asEntityLivingBase();
            if (((Boolean)this.clearNamesValue.get()).booleanValue()) {
                IIChatComponent iIChatComponent = iEntity.getDisplayName();
                string = ColorUtils.stripColor(iIChatComponent != null ? iIChatComponent.getUnformattedText() : null);
                if (string == null) {
                    continue;
                }
            } else {
                IIChatComponent iIChatComponent = iEntity.getDisplayName();
                if (iIChatComponent == null) {
                    continue;
                }
                string = iIChatComponent.getUnformattedText();
            }
            this.renderNameTag(iEntityLivingBase, string);
        }
        GL11.glPopMatrix();
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private final void renderNameTag(IEntityLivingBase iEntityLivingBase, String string) {
        String string2;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        IFontRenderer iFontRenderer = (IFontRenderer)this.fontValue.get();
        boolean bl = AntiBot.isBot(iEntityLivingBase);
        String string3 = bl ? "\u00a73" : (iEntityLivingBase.isInvisible() ? "\u00a76" : (iEntityLivingBase.isSneaking() ? "\u00a74" : "\u00a77"));
        int n = MinecraftInstance.classProvider.isEntityPlayer(iEntityLivingBase) ? EntityUtils.getPing(iEntityLivingBase.asEntityPlayer()) : 0;
        String string4 = string2 = (Boolean)this.distanceValue.get() != false ? "\u00a77" + MathKt.roundToInt((float)iEntityPlayerSP2.getDistanceToEntity(iEntityLivingBase)) + "m " : "";
        String string5 = (Boolean)this.pingValue.get() != false && MinecraftInstance.classProvider.isEntityPlayer(iEntityLivingBase) ? (n > 200 ? "\u00a7c" : (n > 100 ? "\u00a7e" : "\u00a7a")) + n + "ms \u00a77" : "";
        String string6 = (Boolean)this.healthValue.get() != false ? "\u00a77\u00a7c " + iEntityLivingBase.getHealth() + " HP" : "";
        String string7 = bl ? " \u00a7c\u00a7lBot" : "";
        String string8 = string2 + string5 + string3 + string + string6 + string7;
        GL11.glPushMatrix();
        ITimer iTimer = MinecraftInstance.mc.getTimer();
        IRenderManager iRenderManager = MinecraftInstance.mc.getRenderManager();
        GL11.glTranslated((double)(iEntityLivingBase.getLastTickPosX() + (iEntityLivingBase.getPosX() - iEntityLivingBase.getLastTickPosX()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosX()), (double)(iEntityLivingBase.getLastTickPosY() + (iEntityLivingBase.getPosY() - iEntityLivingBase.getLastTickPosY()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosY() + (double)iEntityLivingBase.getEyeHeight() + 0.55), (double)(iEntityLivingBase.getLastTickPosZ() + (iEntityLivingBase.getPosZ() - iEntityLivingBase.getLastTickPosZ()) * (double)iTimer.getRenderPartialTicks() - iRenderManager.getRenderPosZ()));
        GL11.glRotatef((float)(-MinecraftInstance.mc.getRenderManager().getPlayerViewY()), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)MinecraftInstance.mc.getRenderManager().getPlayerViewX(), (float)1.0f, (float)0.0f, (float)0.0f);
        float f = iEntityPlayerSP2.getDistanceToEntity(iEntityLivingBase) * 0.25f;
        if (f < 1.0f) {
            f = 1.0f;
        }
        float f2 = f / 100.0f * ((Number)this.scaleValue.get()).floatValue();
        GL11.glScalef((float)(-f2), (float)(-f2), (float)f2);
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        float f3 = (float)iFontRenderer.getStringWidth(string8) * 0.5f;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        if (((Boolean)this.borderValue.get()).booleanValue()) {
            RenderUtils.quickDrawBorderedRect(-f3 - 2.0f, -2.0f, f3 + 4.0f, (float)iFontRenderer.getFontHeight() + 2.0f, 2.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        } else {
            RenderUtils.quickDrawRect(-f3 - 2.0f, -2.0f, f3 + 4.0f, (float)iFontRenderer.getFontHeight() + 2.0f, Integer.MIN_VALUE);
        }
        GL11.glEnable((int)3553);
        iFontRenderer.drawString(string8, 1.0f + -f3, iFontRenderer.equals(Fonts.minecraftFont) ? 1.0f : 1.5f, 0xFFFFFF, true);
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        if (((Boolean)this.armorValue.get()).booleanValue() && MinecraftInstance.classProvider.isEntityPlayer(iEntityLivingBase)) {
            int[] nArray;
            MinecraftInstance.mc.getRenderItem().setZLevel(-147.0f);
            for (int n2 : nArray = new int[]{0, 1, 2, 3, 5, 4}) {
                IItemStack iItemStack;
                if (iEntityLivingBase.getEquipmentInSlot(n2) == null) {
                    continue;
                }
                MinecraftInstance.mc.getRenderItem().renderItemAndEffectIntoGUI(iItemStack, -50 + n2 * 20, -22);
            }
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179098_w();
        }
        GL11.glPopMatrix();
    }
}

