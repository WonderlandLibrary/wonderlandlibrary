package me.nyan.flush.module.impl.movement;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventMove;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.combat.Aura;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.combat.CombatUtils;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import static org.lwjgl.opengl.GL11.*;

public class TargetStrafe extends Module {
    private final Aura aura = getModule(Aura.class);
    private final NumberSetting range = new NumberSetting("Range", this, 1, 0.4, 6, 0.05);
    private final BooleanSetting onlySpeedOrFly = new BooleanSetting("Only Speed/Fly", this, true);
    private final BooleanSetting space = new BooleanSetting("Hold Space", this, true);
    private final BooleanSetting circle = new BooleanSetting("Circle", this, true);

    private float strafe = 0.0F;

    public TargetStrafe() {
        super("TargetStrafe", Category.MOVEMENT);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (mc.thePlayer.isCollidedHorizontally) {
            switchDirection();
        }
        if (mc.thePlayer.movementInput.moveStrafe != 0.0F) {
            strafe = mc.thePlayer.movementInput.moveStrafe;
        }
        strafe();
    }

    public void strafe() {
        if (shouldStrafe()) {
            MovementUtils.setSpeed(
                    MovementUtils.getSpeed(),
                    CombatUtils.getRotations(aura.target, false)[0],
                    mc.thePlayer.getDistanceToEntity(aura.target) <= range.getValue() ? 0.0F : 1.0F,
                    strafe
            );
        }
    }

    public void strafe(EventMove e) {
        if (shouldStrafe()) {
            MovementUtils.setSpeed(
                    e,
                    Math.sqrt(MovementUtils.square(e.getX()) + MovementUtils.square(e.getZ())),
                    CombatUtils.getRotations(aura.target, false)[0],
                    mc.thePlayer.getDistanceToEntity(aura.target) <= range.getValue() ? 0.0F : 1.0F,
                    strafe
            );
        }
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        if (shouldStrafe() && circle.getValue()) {
            drawCircle(aura.target, e.getPartialTicks(), range.getValue());
        }
    }

    private void drawCircle(Entity entity, float partialTicks, double radius) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        glPushMatrix();
        glEnable(GL_LINE_SMOOTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        glDepthMask(false);
        glLineWidth(1.0F);
        RenderUtils.glColor(-1);

        glBegin(GL_LINE_STRIP);
        for (int i = 0; i < 90; i++) {
            glVertex3d(
                    x + radius * Math.cos(i * MathHelper.PI2 / 45.0),
                    y,
                    z + radius * Math.sin(i * MathHelper.PI2 / 45.0)
            );
        }
        glEnd();

        glDepthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        glPopMatrix();
    }

    private void switchDirection() {
        strafe = strafe == 1.0F ? -1.0F : 1.0F;
    }

    private boolean shouldStrafe() {
        Aura aura = getModule(Aura.class);
        return isEnabled() && aura.isEnabled() && aura.target != null &&
                (!space.getValue() || mc.gameSettings.keyBindJump.isKeyDown()) && ((isEnabled(Speed.class) ||
                isEnabled(Fly.class)) || !onlySpeedOrFly.getValue());
    }
}