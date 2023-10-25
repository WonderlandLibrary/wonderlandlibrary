package org.dreamcore.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.render.RenderHelper;

public class Trajectories extends Feature {

    public Trajectories() {
        super("Trajectories", "", Type.Visuals);
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        ItemStack stack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
        if (!isItemValid(stack))
            return;

        boolean isBow = stack.getItem() instanceof ItemBow;

        double playerYaw = mc.player.rotationYaw;
        double playerPitch = mc.player.rotationPitch;

        double projectilePosX = mc.getRenderManager().renderPosX - Math.cos(Math.toRadians(playerYaw)) * .16F;
        double projectilePosY = mc.getRenderManager().renderPosY + mc.player.getEyeHeight();
        double projectilePosZ = mc.getRenderManager().renderPosZ - Math.sin(Math.toRadians(playerYaw)) * .16F;

        double projectileMotionX = (-Math.sin(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);
        double projectileMotionY = -Math.sin(Math.toRadians(playerPitch - (isThrowablePotion(stack) ? 20 : 0))) * (isBow ? 1 : .4);
        double projectileMotionZ = (Math.cos(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch))) * (isBow ? 1 : .4);

        double shootPower = mc.player.getItemInUseDuration();

        if (isBow) {
            shootPower /= 20;

            shootPower = ((shootPower * shootPower) + (shootPower * 2)) / 3;

            if (shootPower < .1) return;
            if (shootPower > 1) shootPower = 1;
        }

        double distance = Math.sqrt(projectileMotionX * projectileMotionX + projectileMotionY * projectileMotionY + projectileMotionZ * projectileMotionZ);

        projectileMotionX /= distance;
        projectileMotionY /= distance;
        projectileMotionZ /= distance;

        projectileMotionX *= (isBow ? shootPower : .5) * 3;
        projectileMotionY *= (isBow ? shootPower : .5) * 3;
        projectileMotionZ *= (isBow ? shootPower : .5) * 3;

        boolean projectileHasLanded = false;
        RayTraceResult landingPosition = null;

        GlStateManager.resetColor();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.disableTexture2D();
        RenderHelper.setColor(ClientHelper.getClientColor().getRGB());
        GL11.glLineWidth(1.5F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            while (!projectileHasLanded && projectilePosY > 0) {
                Vec3d currentPosition = new Vec3d(projectilePosX, projectilePosY, projectilePosZ);
                Vec3d nextPosition = new Vec3d(projectilePosX + projectileMotionX, projectilePosY + projectileMotionY, projectilePosZ + projectileMotionZ);

                RayTraceResult possibleLandingPositon = mc.world.rayTraceBlocks(currentPosition, nextPosition, false, true, false);

                if (possibleLandingPositon != null) {
                    if (possibleLandingPositon.typeOfHit != RayTraceResult.Type.MISS) {
                        landingPosition = possibleLandingPositon;
                        projectileHasLanded = true;
                    }
                }

                projectilePosX += projectileMotionX;
                projectilePosY += projectileMotionY;
                projectilePosZ += projectileMotionZ;

                projectileMotionX *= .99;
                projectileMotionY *= .99;
                projectileMotionZ *= .99;

                projectileMotionY -= (isBow ? .05 : isThrowablePotion(stack) ? .05 : .03); // Gravitation

                GL11.glVertex3d(projectilePosX - mc.getRenderManager().renderPosX, projectilePosY - mc.getRenderManager().renderPosY, projectilePosZ - mc.getRenderManager().renderPosZ);
            }
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.resetColor();

        if (landingPosition != null) {
            if (landingPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
                RenderHelper.blockEsp(landingPosition.getBlockPos(), ClientHelper.getClientColor(), true);
            }
        }
    }

    private boolean isItemValid(ItemStack stack) {
        return (stack.getItem() instanceof ItemBow) || (stack.getItem() instanceof ItemEnderPearl) || (stack.getItem() instanceof ItemEgg) || (stack.getItem() instanceof ItemSnowball) || isThrowablePotion(stack);
    }

    private boolean isThrowablePotion(ItemStack stack) {
        return stack.getItem() instanceof ItemPotion;
    }

}
