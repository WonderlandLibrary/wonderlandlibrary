package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.Metadata;
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
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.FontValue;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="NameTags", description="Changes the scale of the nametags so you can always read them.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000<\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J020HJ02020HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0\nX¢\n\u0000R0X¢\n\u0000R\f0X¢\n\u0000R\r0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/NameTags;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "armorValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "borderValue", "botValue", "clearNamesValue", "distanceValue", "fontValue", "Lnet/ccbluex/liquidbounce/value/FontValue;", "healthValue", "pingValue", "scaleValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "onRender3D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "renderNameTag", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "tag", "", "Pride"})
public final class NameTags
extends Module {
    private final BoolValue healthValue = new BoolValue("Health", true);
    private final BoolValue pingValue = new BoolValue("Ping", true);
    private final BoolValue distanceValue = new BoolValue("Distance", false);
    private final BoolValue armorValue = new BoolValue("Armor", true);
    private final BoolValue clearNamesValue = new BoolValue("ClearNames", false);
    private final FontValue fontValue;
    private final BoolValue borderValue;
    private final FloatValue scaleValue;
    private final BoolValue botValue;

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
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
            if (!EntityUtils.isSelected(entity, false) || AntiBot.isBot(entity.asEntityLivingBase()) && !((Boolean)this.botValue.get()).booleanValue()) continue;
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
        boolean bot = AntiBot.isBot(entity);
        String nameColor = bot ? "§3" : (entity.isInvisible() ? "§6" : (entity.isSneaking() ? "§4" : "§7"));
        int ping = MinecraftInstance.classProvider.isEntityPlayer(entity) ? PlayerExtensionKt.getPing(entity.asEntityPlayer()) : 0;
        String string = distanceText = (Boolean)this.distanceValue.get() != false ? "§7" + MathKt.roundToInt(thePlayer.getDistanceToEntity(entity)) + "m " : "";
        String pingText = (Boolean)this.pingValue.get() != false && MinecraftInstance.classProvider.isEntityPlayer(entity) ? (ping > 200 ? "§c" : (ping > 100 ? "§e" : "§a")) + ping + "ms §7" : "";
        String healthText = (Boolean)this.healthValue.get() != false ? "§7§c " + (int)entity.getHealth() + " HP" : "";
        String botText = bot ? " §c§lBot" : "";
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
        fontRenderer.drawString(text, 1.0f + -width, Intrinsics.areEqual(fontRenderer, Fonts.minecraftFont) ? 1.0f : 1.5f, 0xFFFFFF, true);
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
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
        GL11.glPopMatrix();
    }

    public NameTags() {
        IFontRenderer iFontRenderer = Fonts.font40;
        Intrinsics.checkExpressionValueIsNotNull(iFontRenderer, "Fonts.font40");
        this.fontValue = new FontValue("Font", iFontRenderer);
        this.borderValue = new BoolValue("Border", true);
        this.scaleValue = new FloatValue("Scale", 1.0f, 1.0f, 4.0f);
        this.botValue = new BoolValue("Bots", true);
    }
}
