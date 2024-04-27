package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "PrevFallPos",description = "Let you see your fall position before you land.",category = ModuleCategory.RENDER)
public class PrevFallPos extends Module {
    private final IntegerValue displayDistanceValue = new IntegerValue("DisplayDistance",5,1,8);
    private final IntegerValue rValue = new IntegerValue("R",255,0,255);
    private final IntegerValue gValue = new IntegerValue("G",80,0,255);
    private final IntegerValue bValue = new IntegerValue("B",80,0,255);
    private final IntegerValue alphaValue = new IntegerValue("Alpha",60,0,255);
    private final BoolValue informationTagValue = new BoolValue("InformationTag",true);
    private BlockPos detectedLocation = null;

    @EventTarget
    private void onUpdate(UpdateEvent event) {
        detectedLocation = null;

        if (mc.thePlayer == null) return;
        EntityPlayerSP thePlayer = mc.thePlayer;

        if (!thePlayer.onGround && !thePlayer.isOnLadder() && !thePlayer.isInWater()) {
            FallingPlayer fallingPlayer = new FallingPlayer(
                    thePlayer.posX,
                    thePlayer.posY,
                    thePlayer.posZ,
                    thePlayer.motionX,
                    thePlayer.motionY,
                    thePlayer.motionZ,
                    thePlayer.rotationYaw,
                    thePlayer.moveStrafing,
                    thePlayer.moveForward
            );

            detectedLocation = fallingPlayer.findCollision(60).getPos();
        }
    }

    @EventTarget
    private void onRender3D(Render3DEvent event) {
        if (mc.thePlayer == null) return;
        EntityPlayerSP thePlayer = mc.thePlayer;

        if (detectedLocation == null ||
                (thePlayer.posY - (detectedLocation.getY() + 1)) < displayDistanceValue.get()) return;

        int x = detectedLocation.getX();
        int y = detectedLocation.getY();
        int z = detectedLocation.getZ();

        RenderManager renderManager = mc.getRenderManager();

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        RenderUtils.glColor(new Color(rValue.get(), gValue.get(), bValue.get(), alphaValue.get()));
        RenderUtils.drawFilledBox(
                AxisAlignedBB.fromBounds(
                        x - renderManager.renderPosX,
                        y + 1 - renderManager.renderPosY,
                        z - renderManager.renderPosZ,
                        x - renderManager.renderPosX + 1.0,
                        y + 1.2 - renderManager.renderPosY,
                        z - renderManager.renderPosZ + 1.0)
        );

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);

        int fallDist = (int) Math.floor(thePlayer.fallDistance + (thePlayer.posY - (y + 0.5)));

        if (fallDist > 3 && informationTagValue.get()) {
            RenderUtils.renderNameTag(fallDist + "m (" + (fallDist - 3) + " damage)", x + 0.5, y + 1.7, z + 0.5);
        } else if (informationTagValue.get()){
            RenderUtils.renderNameTag(fallDist + "m (0 damage)", x + 0.5, y + 1.7, z + 0.5);
        }


        GlStateManager.resetColor();
    }
}
