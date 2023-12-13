package summer.cheat.cheats.movement;

import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.cheat.cheats.combat.KillAura;
import summer.cheat.cheats.render.Chams;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventMotion;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.cheat.eventsystem.events.render.EventRender3D;
import summer.base.utilities.MoveUtils;
import summer.base.utilities.RotationUtils;
import summer.base.utilities.TimerUtils;

import java.awt.*;

public class TargetStrafe extends Cheats {
    private int direction = -1;
    private TimerUtils switchTimer = new TimerUtils();
    public Minecraft mc = Minecraft.getMinecraft();
    private Setting renderHeight;
    private Setting distance;
    private Setting onSpace;
    private Setting render;
    private Setting hue;

    public TargetStrafe() {
        super("TargetStrafe", "", Selection.MOVEMENT);
        Summer.INSTANCE.settingsManager.Property(render = new Setting("Render", this, true));
        Summer.INSTANCE.settingsManager.Property(hue = new Setting("Hue", this, 0.8, 0.0, 1.0, false, render::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(renderHeight = new Setting("Render Height", this, 0.05D, 0.005D, 1.0D, false, render::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(distance = new Setting("Distance", this, 2.0, 1.0, 5.0, false));
        Summer.INSTANCE.settingsManager.Property(onSpace = new Setting("On Space", this, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate eu) {
        if (Minecraft.thePlayer.isCollidedHorizontally && this.switchTimer.hasReached(200L)) {
            this.direction = -this.direction;
            this.switchTimer.reset();
        }
    }

    @EventTarget
    public void onMove(EventMotion em) {
        if (canStrafe()) {
            strafe(em, MoveUtils.getBaseSpeed());
        }
    }

    @EventTarget
    public void onRender(EventRender3D er) {
        if (canStrafe()) {
            if(render.getValBoolean()){
                drawRadius((Entity) KillAura.target, ((EventRender3D) er).getPartialTicks(), distance.getValDouble());
            }
        }
    }

    public void strafe(EventMotion e, double moveSpeed) {
        float[] rots = RotationUtils.getRotations((Entity) KillAura.target);
        double dist = Minecraft.thePlayer.getDistanceToEntity((Entity) KillAura.target);
        if (dist >= distance.getValDouble()) {
            MoveUtils.setMotionWithValues(e, moveSpeed - 0.0, rots[0], 1.0D, this.direction);
        } else {
            MoveUtils.setMotionWithValues(e, moveSpeed, rots[0], 0.0D, this.direction);
        }
    }

    private void drawRadius(Entity entity, float partialTicks, double rad) {
        Color c = Color.getHSBColor(hue.getValFloat(), 1.0F, 1.0F);
        float red = c.getRed() / 255.0F;
        float green = c.getGreen() / 255.0F;
        float blue = c.getBlue() / 255.0F;
        float points = 90.0F;
        GlStateManager.enableDepth();
        int count = 0;
        for (double il = 0.0D; il < ((renderHeight.getValDouble() <= 0.005D)
                ? 0.001D
                : renderHeight.getValDouble()); il += 0.01D) {
            count++;
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glEnable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
            GL11.glHint(3153, 4354);
            GL11.glDisable(2929);
            GL11.glLineWidth(1.3F);
            GL11.glBegin(3);
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
                    - RenderManager.viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
                    - RenderManager.viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
                    - RenderManager.viewerPosZ;
            double pix2 = 6.283185307179586D;
            float speed = 5000.0F;
            float baseHue = (float) (System.currentTimeMillis() % (int) speed);
            while (baseHue > speed)
                baseHue -= speed;
            baseHue /= speed;
            for (int i = 0; i <= 90; i++) {
                float max = (i + (float) (il * 8.0D)) / points;
                float hue = max + baseHue;
                while (hue > 1.0F)
                    hue--;
                GL11.glColor3f(red, green, blue);
                GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586D / points), y + il,
                        z + rad * Math.sin(i * 6.283185307179586D / points));
            }
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GlStateManager.color(255.0F, 255.0F, 255.0F);
        }
    }

    public boolean canStrafe() {
        if (onSpace.getValBoolean() && !mc.gameSettings.keyBindJump.isKeyDown())
            return false;
        return (isToggled() && MoveUtils.isMoving() && KillAura.target != null
                && MoveUtils.isBlockUnderneath(KillAura.target.getPosition())
                && (CheatManager.getInstance(Speed.class).isToggled()
                || CheatManager.getInstance(Flight.class).isToggled()));
    }
}
