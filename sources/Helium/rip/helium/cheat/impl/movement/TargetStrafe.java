package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import rip.helium.ChatUtil;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.combat.aura.Aura;
import rip.helium.event.minecraft.*;
import rip.helium.utils.ColorUtils;
import rip.helium.utils.MovementUtils;
import rip.helium.utils.PlayerUtils;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.ColorProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import tv.twitch.chat.Chat;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class TargetStrafe extends Cheat {

    public static int direction = -1;

    public static BooleanProperty onlyspeed = new BooleanProperty("Only speed", "Requires the module 'speed' in order to strafe.", null, false);
    public static BooleanProperty space = new BooleanProperty("Hold Space", "Requires you to hold space.", null, true);
    public static BooleanProperty showCircle = new BooleanProperty("Show Circle", "Show the circle around the person", null, true);
    public static BooleanProperty rainbow = new BooleanProperty("Rainbow", " ", null, true);
    //public static ColorProperty color = new ColorProperty("Color", " ", null, 255, 255, 255, 255);
    public static DoubleProperty distance = new DoubleProperty("Distance", "Distance check", null, 2.5, 0.1, 6.0, 0.1, null);

    public TargetStrafe() {
        super("TargetStrafe", "strafes at targets", CheatCategory.COMBAT);
        registerProperties(distance, rainbow, space);
    }


    @Collect
    public final void onUpdate(PlayerUpdateEvent event) {
        if (event.isPre()) {
            if (mc.thePlayer.isCollidedHorizontally) {
                switchDirection();
            }
        }
    }


    private void switchDirection() {
        if (direction == 1) {
            direction = -1;
        } else {
            direction = 1;
        }
    }

    public final static boolean doStrafeAtSpeed(PlayerMoveEvent event, final double moveSpeed) {
        final boolean strafe = canStrafe();
        if (strafe) {
            float[] rotations = PlayerUtils.getNeededRotations(Aura.getCurrentTarget());
            if (mc.thePlayer.getDistanceToEntity(Aura.getCurrentTarget()) <= distance.getValue().floatValue()) {
                MovementUtils.setSpeed(event, moveSpeed, rotations[0], direction, 0);
            } else {
                MovementUtils.setSpeed(event, moveSpeed, rotations[0], direction, 1);
            }
        }
        return strafe;
    }

    public final static boolean doStrafeAtUpdate(PlayerUpdateEvent event, final double moveSpeed) {
        final boolean strafe = canStrafe();
        if (strafe) {
            float[] rotations = PlayerUtils.getNeededRotations(Aura.getCurrentTarget());
            if (mc.thePlayer.getDistanceToEntity(Aura.getCurrentTarget()) <= distance.getValue().floatValue()) {
                MovementUtils.setCockSpeed(event, moveSpeed, rotations[0], direction, 0);
            } else {
                MovementUtils.setCockSpeed(event, moveSpeed, rotations[0], direction, 1);
            }
        }
        return strafe;
    }


    @Collect
    public final void onRender3D(EntityRenderEvent event) {
        if (canStrafe()) {
            Color color = ColorUtils.getRainbow(-6000, 1);
            drawCircle(Aura.getCurrentTarget(), event.getPartialTicks(), distance.getValue().floatValue(), color);
        }
    }

    double nigger = 0;
    boolean up;


    private void drawCircle(Entity entity, float partialTicks, double rad, Color color) {
        glPushMatrix();
        glColor3d(color.getRed(), color.getGreen(), color.getBlue());
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(5.5f);
        glBegin(GL_LINE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY + nigger;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        if (nigger < 1.8) {
            if (up) {
                nigger = nigger + 0.01;
            }
            up = true;
        } else {
            nigger = 0;
        }


        final double pix2 = Math.PI * 2.0D;
        //ChatUtil.chat(color.getRed() + " is red, " + color.getBlue() + " is blue, " + color.getGreen());
        for (int i = 0; i <= 90; ++i) {
            //ChatUtil.chat(color.getRGB() + " hi");
            //glColor3d(color.getRed(), color.getGreen(), color.getBlue());
            // for (int k = 1; z < 5; z++) {
            glVertex3d(x + 1 * Math.cos(i * pix2 / 45.0), y, z + 1 * Math.sin(i * pix2 / 45.0));
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static boolean canStrafe() {
        if (TargetStrafe.space.getValue()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                if (Helium.instance.cheatManager.isCheatEnabled("KillAura") && Aura.getCurrentTarget() != null && Helium.instance.cheatManager.isCheatEnabled("TargetStrafe")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (Helium.instance.cheatManager.isCheatEnabled("KillAura") && Aura.getCurrentTarget() != null && Helium.instance.cheatManager.isCheatEnabled("TargetStrafe")) {
                return true;
            } else {
                return false;
            }
        }
    }
}