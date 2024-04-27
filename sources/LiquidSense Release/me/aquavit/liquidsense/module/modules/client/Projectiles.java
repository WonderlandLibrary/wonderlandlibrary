package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "Projectiles", description = "Allows you to see where arrows will land.", category = ModuleCategory.CLIENT)
public class Projectiles extends Module {

    private BoolValue dynamicBowPower = new BoolValue("DynamicBowPower", true);

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (mc.thePlayer.getHeldItem() == null) return;

        Item item = mc.thePlayer.getHeldItem().getItem();
        RenderManager renderManager = mc.getRenderManager();
        boolean isBow = false;
        float motionFactor = 1.5F;
        float motionSlowdown = 0.99F;
        float gravity;
        float size;

        // Check items
        if (item instanceof ItemBow) {
            if (dynamicBowPower.get() && !mc.thePlayer.isUsingItem())
                return;

            isBow = true;
            gravity = 0.05F;
            size = 0.3F;

            // Calculate power of bow
            float power = dynamicBowPower.get() ? mc.thePlayer.getItemInUseDuration() : item.getMaxItemUseDuration(new ItemStack(item)) / 20f;
            power = (power * power + power * 2F) / 3F;
            if (power < 0.1F)
                return;

            if (power > 1F)
                power = 1F;

            motionFactor = power * 3F;
        } else if (item instanceof ItemFishingRod) {
            gravity = 0.04F;
            size = 0.25F;
            motionSlowdown = 0.92F;
        } else if (item instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getItemDamage())) {
            gravity = 0.05F;
            size = 0.25F;
            motionFactor = 0.5F;
        } else {
            if (!(item instanceof ItemSnowball) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemEgg))
            return;

            gravity = 0.03F;
            size = 0.25F;
        }

        // Yaw and pitch of player
        float yaw = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getYaw() : mc.thePlayer.rotationYaw;

        float pitch = RotationUtils.targetRotation != null ? RotationUtils.targetRotation.getPitch() : mc.thePlayer.rotationPitch;

        // Positions
        double posX = renderManager.renderPosX - MathHelper.cos(yaw / 180F * 3.1415927F) * 0.16F;
        double posY = renderManager.renderPosY + mc.thePlayer.getEyeHeight() - 0.10000000149011612;
        double posZ = renderManager.renderPosZ - MathHelper.sin(yaw / 180F * 3.1415927F) * 0.16F;

        // Motions
        double motionX = (-MathHelper.sin(yaw / 180f * 3.1415927F) * MathHelper.cos(pitch / 180F * 3.1415927F) * (isBow ? 1.0 : 0.4));
        double motionY = -MathHelper.sin((pitch +
                (item instanceof ItemPotion && ItemPotion.isSplash(mc.thePlayer.getHeldItem().getItemDamage()) ? -20 : 0)) / 180f * 3.1415927f) * (isBow ? 1.0 : 0.4);
        double motionZ = (MathHelper.cos(yaw / 180f * 3.1415927F) * MathHelper.cos(pitch / 180F * 3.1415927F) * (isBow ? 1.0 : 0.4));
        double distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);

        motionX /= distance;
        motionY /= distance;
        motionZ /= distance;
        motionX *= motionFactor;
        motionY *= motionFactor;
        motionZ *= motionFactor;

        // Landing
        MovingObjectPosition landingPosition = null;
        boolean hasLanded = false;
        boolean hitEntity = false;

        // Start drawing of path
        GL11.glDepthMask(false);
        RenderUtils.enableGlCap(GL11.GL_BLEND, GL11.GL_LINE_SMOOTH);
        RenderUtils.disableGlCap(GL11.GL_DEPTH_TEST, GL11.GL_ALPHA_TEST, GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        RenderUtils.glColor(new Color(0, 160, 255, 255));
        GL11.glLineWidth(2f);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        while (!hasLanded && posY > 0.0) {
            // Set pos before and after
            Vec3 posBefore = new Vec3(posX, posY, posZ);
            Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

            // Get landing position
            landingPosition = mc.theWorld.rayTraceBlocks(posBefore, posAfter, false,
                    true, false);

            // Set pos before and after
            posBefore = new Vec3(posX, posY, posZ);
            posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

            // Check if arrow is landing
            if (landingPosition != null) {
                hasLanded = true;
                posAfter = new Vec3(landingPosition.hitVec.xCoord, landingPosition.hitVec.yCoord, landingPosition.hitVec.zCoord);
            }

            // Set arrow box
            AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size, posY - size, posZ - size, posX + size,
                    posY + size, posZ + size).addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0);

            int chunkMinX = MathHelper.floor_double((arrowBox.minX - 2.0) / 16.0);
            int chunkMaxX = MathHelper.floor_double((arrowBox.maxX + 2.0) / 16.0);
            int chunkMinZ = MathHelper.floor_double((arrowBox.minZ - 2.0) / 16.0);
            int chunkMaxZ = MathHelper.floor_double((arrowBox.maxZ + 2.0) / 16.0);

            // Check which entities colliding with the arrow
            ArrayList<Entity> collidedEntities = new ArrayList<>();
            for (int x = chunkMinX; x <= chunkMaxX; ++x)
                for (int z = chunkMinZ;z <= chunkMaxZ; ++z)
                    mc.theWorld.getChunkFromChunkCoords(x, z)
                            .getEntitiesWithinAABBForEntity(mc.thePlayer, arrowBox, collidedEntities, null);

            // Check all possible entities
            for (Entity possibleEntity : collidedEntities) {
                if (possibleEntity.canBeCollidedWith() && possibleEntity != mc.thePlayer) {
                    AxisAlignedBB possibleEntityBoundingBox = possibleEntity.getEntityBoundingBox().expand(size, size, size);

                    MovingObjectPosition possibleEntityLanding = possibleEntityBoundingBox.calculateIntercept(posBefore, posAfter);
                    if (possibleEntityLanding != null)continue;

                    hitEntity = true;
                    hasLanded = true;
                    landingPosition = possibleEntityLanding;
                }
            }

            // Affect motions of arrow
            posX += motionX;
            posY += motionY;
            posZ += motionZ;

            // Check is next position water
            if (mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock().getMaterial() == Material.water) {
                // Update motion
                motionX *= 0.6;
                motionY *= 0.6;
                motionZ *= 0.6;
            } else { // Update motion
                motionX *= motionSlowdown;
                motionY *= motionSlowdown;
                motionZ *= motionSlowdown;
            }

            motionY -= gravity;

            // Draw path
            GL11.glVertex3d(posX - renderManager.renderPosX, posY - renderManager.renderPosY,
                    posZ - renderManager.renderPosZ);
        }

        // End the rendering of the path
        GL11.glEnd();
        GL11.glPushMatrix();
        GL11.glTranslated(posX - renderManager.renderPosX, posY - renderManager.renderPosY,
                posZ - renderManager.renderPosZ);

        if (landingPosition != null) {
            // Switch rotation of hit cylinder of the hit axis
            switch (landingPosition.sideHit.getAxis().ordinal()){
                case 0:
                    GL11.glRotatef(90F, 0F, 0F, 1F);
                    break;
                case 2:
                    GL11.glRotatef(90F, 1F, 0F, 0F);
                    break;
            }

            // Check if hitting a entity
            if (hitEntity)
                RenderUtils.glColor(new Color(255, 0, 0, 150));
        }

        // Rendering hit cylinder
        GL11.glRotatef(-90F, 1F, 0F, 0F);

        Cylinder cylinder = new Cylinder();
        cylinder.setDrawStyle(GLU.GLU_LINE);
        cylinder.draw(0.2F, 0F, 0F, 60, 1);

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        RenderUtils.resetCaps();
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }
}
