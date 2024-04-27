package me.AquaVit.liquidSense.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.features.module.modules.misc.Teams;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.awt.*;

@ModuleInfo(name = "PointerESP", description = "PointerESP", category = ModuleCategory.RENDER)
public class PointerESP extends Module {

    private final BoolValue player = new BoolValue("Players", true);
    private final BoolValue mobs = new BoolValue("Mobs", false);
    private final BoolValue yourself = new BoolValue("Yourself", false);
    private final BoolValue invis = new BoolValue("Invisibles", false);
    private final BoolValue animals = new BoolValue("Animals", false);
    private final FloatValue width = new FloatValue("Width", 0.5F, 0.0F, 5.0F);
    private final IntegerValue colorRedValue = new IntegerValue("R", 0, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 160, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final BoolValue rainbow = new BoolValue("RainBow", false);
    private final BoolValue bb = new BoolValue("Solid", true);
    private final BoolValue aa = new BoolValue("Line", false);

    public BoolValue getplayer() {
        return player;
    }

    public boolean isValid(EntityLivingBase entity) {
        if (!player.get() && entity instanceof EntityPlayer) {
            return false;
        }
        if (!mobs.get() && (entity instanceof EntityMob || entity instanceof EntitySlime)) {
            return false;
        }
        if (!animals.get() && (entity instanceof EntityAnimal || entity instanceof EntitySquid)) {
            return false;
        }
        if (entity.isInvisible() && !invis.get()) {
            return false;
        }
        if (entity == mc.thePlayer && !yourself.get()) {
            return false;
        }
        if (!entity.isEntityAlive()) {
            return false;
        }
        return true;
    }



    @EventTarget
    public void onRender2D(Render2DEvent e) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        int size = 50;
        float xOffset = sr.getScaledWidth() / 2 - 24.5f;
        float yOffset = sr.getScaledHeight() / 2 - 25.2f;
        float playerOffsetX = (float) mc.thePlayer.posX;
        float playerOffSetZ = (float) mc.thePlayer.posZ;
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) o;
                if (this.isValid(ent)) {
                    float loaddist = 0.2F;
                    float pTicks = mc.timer.renderPartialTicks;
                    float posX = (float) (((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks) - playerOffsetX)
                            * loaddist);
                    float posZ = (float) (((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks) - playerOffSetZ)
                            * loaddist);
                    Color color = rainbow.get() ? ColorUtils.rainbow() : new Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get());

                    float cos = (float) Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float sin = (float) Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);
                    float var7 = 0 - rotX;
                    float var9 = 0 - rotY;
                    if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) < size / 2 - 4) {
                        float angle = (float) (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
                        float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2;
                        float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x, y, 0);
                        GlStateManager.rotate(angle, 0, 0, 1);
                        GlStateManager.scale(1.5f, 1.0, 1.0);
                        if (aa.get()){
                            RenderUtils.NdrawCircle(0, 0, 2.2f,3, width.get(),color.getRGB());
                            RenderUtils.NdrawCircle(0, 0, 1.5f, 3,width.get() ,color.getRGB());
                            RenderUtils.NdrawCircle(0, 0, 1f, 3, width.get(),color.getRGB());
                            RenderUtils.NdrawCircle(0, 0, 0.5f, 3, width.get(),color.getRGB());
                        }
                        if (bb.get()){
                            RenderUtils.drawESPCircle(0, 0, 2.2f, 3, color.getRGB());
                            RenderUtils.drawESPCircle(0, 0, 1.5f, 3, color.getRGB());
                            RenderUtils.drawESPCircle(0, 0, 1f, 3, color.getRGB());
                            RenderUtils.drawESPCircle(0, 0, 0.5f, 3, color.getRGB());

                        }
                        GlStateManager.popMatrix();
                    }
                }
            }
        }
        GlStateManager.popMatrix();
    }

}
