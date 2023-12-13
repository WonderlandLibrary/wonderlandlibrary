package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import org.lwjgl.opengl.GL11;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner>
{
    public void renderTileEntityAt(TileEntityMobSpawner te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
        renderMob(te.getSpawnerBaseLogic(), x, y, z, partialTicks);
        GL11.glPopMatrix();
    }

    /**
     * Render the mob inside the mob spawner.
     */
    public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks)
    {
        Entity entity = mobSpawnerLogic.func_180612_a(mobSpawnerLogic.getSpawnerWorld());

        if (entity != null)
        {
            float f = 0.4375F;
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float)(mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double)partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(f, f, f);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        }
    }
}
