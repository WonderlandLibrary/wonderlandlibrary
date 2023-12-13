// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import vip.Resolute.util.render.RenderUtils;
import java.awt.Color;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBow;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class Trajectories extends Module
{
    private boolean isBow;
    
    public Trajectories() {
        super("Trajectories", 0, "Renders a projectile's trajectory", Category.RENDER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D) {
            final ItemStack stack = Trajectories.mc.thePlayer.getHeldItem();
            if (stack == null || !this.isItemValid(stack)) {
                return;
            }
            this.isBow = (stack.getItem() instanceof ItemBow);
            final double playerYaw = Trajectories.mc.thePlayer.rotationYaw;
            final double playerPitch = Trajectories.mc.thePlayer.rotationPitch;
            Trajectories.mc.getRenderManager();
            double projectilePosX = RenderManager.renderPosX - Math.cos(Math.toRadians(playerYaw)) * 0.1599999964237213;
            Trajectories.mc.getRenderManager();
            double projectilePosY = RenderManager.renderPosY + Trajectories.mc.thePlayer.getEyeHeight();
            Trajectories.mc.getRenderManager();
            double projectilePosZ = RenderManager.renderPosZ - Math.sin(Math.toRadians(playerYaw)) * 0.1599999964237213;
            double projectileMotionX = -Math.sin(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch)) * (this.isBow ? 1.0 : 0.4);
            double projectileMotionY = -Math.sin(Math.toRadians(playerPitch - (this.isThrowablePotion(stack) ? 20 : 0))) * (this.isBow ? 1.0 : 0.4);
            double projectileMotionZ = Math.cos(Math.toRadians(playerYaw)) * Math.cos(Math.toRadians(playerPitch)) * (this.isBow ? 1.0 : 0.4);
            double shootPower = Trajectories.mc.thePlayer.getItemInUseDuration();
            if (this.isBow) {
                shootPower /= 20.0;
                shootPower = (shootPower * shootPower + shootPower * 2.0) / 3.0;
                if (shootPower < 0.1) {
                    return;
                }
                if (shootPower > 1.0) {
                    shootPower = 1.0;
                }
            }
            final double distance = Math.sqrt(projectileMotionX * projectileMotionX + projectileMotionY * projectileMotionY + projectileMotionZ * projectileMotionZ);
            projectileMotionX /= distance;
            projectileMotionY /= distance;
            projectileMotionZ /= distance;
            projectileMotionX *= (this.isBow ? shootPower : 0.5) * 3.0;
            projectileMotionY *= (this.isBow ? shootPower : 0.5) * 3.0;
            projectileMotionZ *= (this.isBow ? shootPower : 0.5) * 3.0;
            boolean projectileHasLanded = false;
            MovingObjectPosition landingPosition = null;
            GlStateManager.resetColor();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GlStateManager.disableTexture2D();
            this.color(10.0, 120.0, 200.0, 200.0);
            GL11.glLineWidth(1.5f);
            GL11.glBegin(3);
            while (!projectileHasLanded && projectilePosY > 0.0) {
                final Vec3 currentPosition = new Vec3(projectilePosX, projectilePosY, projectilePosZ);
                final Vec3 nextPosition = new Vec3(projectilePosX + projectileMotionX, projectilePosY + projectileMotionY, projectilePosZ + projectileMotionZ);
                final MovingObjectPosition possibleLandingPositon = Trajectories.mc.theWorld.rayTraceBlocks(currentPosition, nextPosition, false, true, false);
                if (possibleLandingPositon != null && possibleLandingPositon.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                    landingPosition = possibleLandingPositon;
                    projectileHasLanded = true;
                }
                projectilePosX += projectileMotionX;
                projectilePosY += projectileMotionY;
                projectilePosZ += projectileMotionZ;
                projectileMotionX *= 0.99;
                projectileMotionY *= 0.99;
                projectileMotionZ *= 0.99;
                projectileMotionY -= (this.isBow ? 0.05 : (this.isThrowablePotion(stack) ? 0.05 : 0.03));
                final double n = projectilePosX;
                Trajectories.mc.getRenderManager();
                final double n2 = n - RenderManager.renderPosX;
                final double n3 = projectilePosY;
                Trajectories.mc.getRenderManager();
                final double n4 = n3 - RenderManager.renderPosY;
                final double n5 = projectilePosZ;
                Trajectories.mc.getRenderManager();
                GL11.glVertex3d(n2, n4, n5 - RenderManager.renderPosZ);
            }
            GL11.glEnd();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GlStateManager.resetColor();
            if (landingPosition != null && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                RenderUtils.drawBox(landingPosition.getBlockPos(), new Color(255, 0, 0, 100), false);
            }
        }
    }
    
    private boolean isItemValid(final ItemStack stack) {
        return stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemEnderPearl || stack.getItem() instanceof ItemEgg || stack.getItem() instanceof ItemSnowball || this.isThrowablePotion(stack);
    }
    
    private boolean isThrowablePotion(final ItemStack stack) {
        return stack.getItem() instanceof ItemPotion && ItemPotion.isSplash(Trajectories.mc.thePlayer.getHeldItem().getItemDamage());
    }
    
    public final void color(final double red, final double green, final double blue, final double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }
}
