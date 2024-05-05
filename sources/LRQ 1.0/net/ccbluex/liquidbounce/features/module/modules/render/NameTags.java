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
    private final BoolValue healthValue = new BoolValue("Health", true);
    private final BoolValue pingValue = new BoolValue("Ping", true);
    private final BoolValue distanceValue = new BoolValue("Distance", false);
    private final BoolValue armorValue = new BoolValue("Armor", true);
    private final BoolValue clearNamesValue = new BoolValue("ClearNames", false);
    private final FontValue fontValue = new FontValue("Font", Fonts.font40);
    private final BoolValue borderValue = new BoolValue("Border", true);
    private final FloatValue scaleValue = new FloatValue("Scale", 1.0f, 1.0f, 4.0f);

    @EventTarget
    public final void onRender3D(Render3DEvent event) {
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
        for (IEntity entity : iWorldClient.getLoadedEntityList()) {
            String string;
            if (!EntityUtils.isSelected(entity, false)) continue;
            IEntityLivingBase iEntityLivingBase = entity.asEntityLivingBase();
            if (((Boolean)this.clearNamesValue.get()).booleanValue()) {
                IIChatComponent iIChatComponent = entity.getDisplayName();
                string = ColorUtils.stripColor(iIChatComponent != null ? iIChatComponent.getUnformattedText() : null);
                if (string == null) {
                    continue;
                }
            } else {
                IIChatComponent iIChatComponent = entity.getDisplayName();
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

    private final void renderNameTag(IEntityLivingBase entity, String tag) {
        String distanceText;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IFontRenderer fontRenderer = (IFontRenderer)this.fontValue.get();
        boolean bot = AntiBot.Companion.isBot(entity);
        String nameColor = bot ? "\u00a73" : (entity.isInvisible() ? "\u00a76" : (entity.isSneaking() ? "\u00a74" : "\u00a77"));
        int ping = MinecraftInstance.classProvider.isEntityPlayer(entity) ? EntityUtils.getPing(entity.asEntityPlayer()) : 0;
        String string = distanceText = (Boolean)this.distanceValue.get() != false ? "\u00a77" + MathKt.roundToInt((float)thePlayer.getDistanceToEntity(entity)) + "m " : "";
        String pingText = (Boolean)this.pingValue.get() != false && MinecraftInstance.classProvider.isEntityPlayer(entity) ? (ping > 200 ? "\u00a7c" : (ping > 100 ? "\u00a7e" : "\u00a7a")) + ping + "ms \u00a77" : "";
        String healthText = (Boolean)this.healthValue.get() != false ? "\u00a77\u00a7c " + (int)entity.getHealth() + " HP" : "";
        String botText = bot ? " \u00a7c\u00a7lBot" : "";
        String text = distanceText + pingText + nameColor + tag + healthText + botText;
        GL11.glPushMatrix();
        ITimer timer = MinecraftInstance.mc.getTimer();
        IRenderManager renderManager = MinecraftInstance.mc.getRenderManager();
        GL11.glTranslated((double)(entity.getLastTickPosX() + (entity.getPosX() - entity.getLastTickPosX()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosX()), (double)(entity.getLastTickPosY() + (entity.getPosY() - entity.getLastTickPosY()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosY() + (double)entity.getEyeHeight() + 0.55), (double)(entity.getLastTickPosZ() + (entity.getPosZ() - entity.getLastTickPosZ()) * (double)timer.getRenderPartialTicks() - renderManager.getRenderPosZ()));
        GL11.glRotatef((float)(-MinecraftInstance.mc.getRenderManager().getPlayerViewY()), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)MinecraftInstance.mc.getRenderManager().getPlayerViewX(), (float)1.0f, (float)0.0f, (float)0.0f);
        float distance = thePlayer.getDistanceToEntity(entity) * 0.25f;
        if (distance < 1.0f) {
            distance = 1.0f;
        }
        float scale = distance / 100.0f * ((Number)this.scaleValue.get()).floatValue();
        GL11.glScalef((float)(-scale), (float)(-scale), (float)scale);
        AWTFontRenderer.Companion.setAssumeNonVolatile(true);
        float width = (float)fontRenderer.getStringWidth(text) * 0.5f;
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        if (((Boolean)this.borderValue.get()).booleanValue()) {
            RenderUtils.quickDrawBorderedRect(-width - 2.0f, -2.0f, width + 4.0f, (float)fontRenderer.getFontHeight() + 2.0f, 2.0f, new Color(255, 255, 255, 90).getRGB(), Integer.MIN_VALUE);
        } else {
            RenderUtils.quickDrawRect(-width - 2.0f, -2.0f, width + 4.0f, (float)fontRenderer.getFontHeight() + 2.0f, Integer.MIN_VALUE);
        }
        GL11.glEnable((int)3553);
        fontRenderer.drawString(text, 1.0f + -width, fontRenderer.equals(Fonts.minecraftFont) ? 1.0f : 1.5f, 0xFFFFFF, true);
        AWTFontRenderer.Companion.setAssumeNonVolatile(false);
        if (((Boolean)this.armorValue.get()).booleanValue() && MinecraftInstance.classProvider.isEntityPlayer(entity)) {
            int[] indices;
            MinecraftInstance.mc.getRenderItem().setZLevel(-147.0f);
            for (int index : indices = new int[]{0, 1, 2, 3, 5, 4}) {
                IItemStack equipmentInSlot;
                if (entity.getEquipmentInSlot(index) == null) {
                    continue;
                }
                MinecraftInstance.mc.getRenderItem().renderItemAndEffectIntoGUI(equipmentInSlot, -50 + index * 20, -22);
            }
            GlStateManager.func_179141_d();
            GlStateManager.func_179084_k();
            GlStateManager.func_179098_w();
        }
        GL11.glPopMatrix();
    }
}

