/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.Event3D;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracers
extends Module {
    private int state;
    private float r = 0.33f;
    private float g = 0.34f;
    private float b = 0.33f;
    public boolean rainbow = true;
    public boolean players = true;
    public boolean monsters;
    public boolean farmHunt;

    public Tracers() {
        super("Tracers", 0, Category.RENDER, 10079232, true, "Draws cheeses to other players", new String[]{"trac", "tracres"});
    }

    @EventTarget
    private void onRender3D(Event3D event) {
        GlStateManager.pushMatrix();
        float[] rainbowArray = this.getRainbow();
        for (Object ent1 : Wrapper.mc.theWorld.loadedEntityList) {
            Entity ent = (Entity)ent1;
            if (ent == Wrapper.mc.thePlayer || !this.farmHunt && (!(ent instanceof EntityPlayer) && !(ent instanceof EntityMob) || ent instanceof EntityPlayer && !this.players || ent instanceof EntityMob && !this.monsters) || (!(ent instanceof EntityLivingBase) || ((EntityLivingBase)ent).getMaxHealth() <= 20.0f || ((EntityLivingBase)ent).isInvisible() || ent instanceof EntityHorse) && this.farmHunt || !ent.isEntityAlive()) continue;
            double x2 = this.getDiff(ent.lastTickPosX, ent.posX, Event3D.getPartialTicks(), RenderManager.renderPosX);
            double y2 = this.getDiff(ent.lastTickPosY, ent.posY, Event3D.getPartialTicks(), RenderManager.renderPosY);
            double z2 = this.getDiff(ent.lastTickPosZ, ent.posZ, Event3D.getPartialTicks(), RenderManager.renderPosZ);
            if (FriendManager.isFriend(ent.getName())) {
                GL11.glColor3f(0.27f, 0.7f, 0.92f);
                if (this.rainbow) {
                    GL11.glColor3f(rainbowArray[0], rainbowArray[1], rainbowArray[2]);
                }
            } else {
                float distance = Wrapper.mc.thePlayer.getDistanceToEntity(ent);
                if (distance <= 32.0f) {
                    GL11.glColor3f(distance / 32.0f, 0.0f, 0.0f);
                } else {
                    GL11.glColor3f(0.9f, 0.0f, 0.0f);
                }
            }
            GL11.glLoadIdentity();
            boolean bobbing = Wrapper.mc.gameSettings.viewBobbing;
            Wrapper.mc.gameSettings.viewBobbing = false;
            Wrapper.mc.entityRenderer.orientCamera(Event3D.getPartialTicks());
            GL11.glLineWidth(1.2f);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, Wrapper.mc.thePlayer.getEyeHeight(), 0.0);
            GL11.glVertex3d(x2, y2, z2);
            GL11.glVertex3d(x2, y2 + (double)ent.getEyeHeight(), z2);
            GL11.glEnd();
            Wrapper.mc.gameSettings.viewBobbing = bobbing;
        }
        GlStateManager.popMatrix();
    }

    private float[] getRainbow() {
        if (this.state == 0) {
            this.r = (float)((double)this.r + 0.01);
            this.b = (float)((double)this.b - 0.01);
            if ((double)this.r >= 0.9) {
                ++this.state;
            }
        } else if (this.state == 1) {
            this.g = (float)((double)this.g + 0.01);
            this.r = (float)((double)this.r - 0.01);
            if ((double)this.g >= 0.9) {
                ++this.state;
            }
        } else {
            this.b = (float)((double)this.b + 0.01);
            this.g = (float)((double)this.g - 0.01);
            if ((double)this.b >= 0.9) {
                this.state = 0;
            }
        }
        return new float[]{this.r, this.g, this.b};
    }

    private double getDiff(double lastI, double i2, float ticks, double ownI) {
        return lastI + (i2 - lastI) * (double)ticks - ownI;
    }
}

