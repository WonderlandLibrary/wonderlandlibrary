package wtf.diablo.module.impl.Combat;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import wtf.diablo.events.impl.MoveEvent;
import wtf.diablo.events.impl.Render3DEvent;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.ModuleManager;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.module.impl.Movement.Speed;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.player.MoveUtil;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;

@Setter
@Getter
public class TargetStrafe extends Module {
    public NumberSetting range = new NumberSetting("Range", 2, 0.1, 0.5, 3);

    public static int direction = 1;

    public TargetStrafe() {
        super("TargetStrafe", "Do da strafe", Category.COMBAT, ServerType.All);
        this.addSettings(range);
    }

    @Subscribe
    public void onMove(MoveEvent e) {
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && ModuleManager.getModule(Speed.class).isToggled()) {
            if (KillAura.target != null) {
                setSpeedVinceNewAutismo(e, MoveUtil.getBaseMoveSpeed());

                if (mc.thePlayer.isCollidedHorizontally) {
                    direction = -direction;
                } else {
                    if (mc.gameSettings.keyBindLeft.isPressed()) direction = 1;
                    if (mc.gameSettings.keyBindRight.isPressed()) direction = -1;
                }
            }
        }
    }

    @Subscribe
    public void onRender(Render3DEvent e){
        for (Entity object : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (object instanceof EntityPlayer) {
                if (!object.isInvisible()) {
                    GlStateManager.pushMatrix();
                    //GlStateManager.translate(object.posX / 4, mc.gameSettings.thirdPersonView > 0 ? 1.0 : 0.0, object.posZ / 4);
                    GlStateManager.rotate(-90.0f, 15.0f, 0.0f, 0.0f);
                    //drawBorderedCircle((int) object.posX, (int) object.posZ,range.getValue() / 1.5f, 1.0, ColorUtil.getColor(0), 0);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    public void setSpeedVinceNewAutismo(MoveEvent moveEvent, double speed) {
        EntityLivingBase target = KillAura.target;

        float yaw = mc.thePlayer.rotationYaw;
        double strafe = direction;
        double forward = mc.thePlayer.getDistanceToEntity(KillAura.target) <= range.getValue() ? 0 : 1;

        if (forward == 0 && strafe == 0) {
            moveEvent.setX(0);
            moveEvent.setZ(0);
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += ((forward > 0) ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += ((forward > 0) ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1.0;
                } else if (forward < 0) {
                    forward = -1.0;
                }
            }

            moveEvent.setX(forward * speed * -Math.sin(Math.toRadians(yaw)) + strafe * speed * Math.cos(Math.toRadians(yaw)));
            moveEvent.setZ(forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * -Math.sin(Math.toRadians(yaw)));

            //moveEvent.setYaw(MoveUtil.getDirection());
        }
    }

    public double yLevel;
    boolean deincrement;

    private void drawCircle(Entity entity, float partialTicks, double rad) {
        for (double il = 0; il < 0.05; il += 0.0006) {
            GL11.glPushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.enableDepth();
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0f);
            GL11.glBegin(1);
            yLevel += deincrement ? -0.0001 : 0.0001;
            if (yLevel > 1.8) {
                deincrement = true;
            }
            if (yLevel <= 0) {
                deincrement = false;
            }
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

            y += yLevel;

            float r = ((float) 1 / 255) * 114;
            float g = ((float) 1 / 255) * 149;
            float b = ((float) 1 / 255) * 229;

            double pix2 = Math.PI * 2D;

            float speed = 1200f;
            float baseHue = System.currentTimeMillis() % (int) speed;
            while (baseHue > speed) {
                baseHue -= speed;
            }
            baseHue /= speed;

            for (int i = 0; i <= 90; ++i) {
                float max = ((float) i) / 45F;
                float hue = max + baseHue;
                while (hue > 1) {
                    hue -= 1;
                }
                float f3 = (float) ((int) 3 >> 24 & 255) / 255.0F;
                float f = (float) ((int) 3 >> 16 & 255) / 255.0F;
                float f1 = (float) ((int) 3 >> 8 & 255) / 255.0F;
                float f2 = (float) ((int) 3 & 255) / 255.0F;
                float red = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getRed();
                float green = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getGreen();
                float blue = 0.003921569f * new Color(Color.HSBtoRGB(hue, 0.75F, 1F)).getBlue();
                GL11.glColor3f(f3, f, f1);
                GL11.glVertex3d(x + rad * Math.cos(i * pix2 / 45.0), y + il, z + rad * Math.sin(i * pix2 / 45.0));
            }

            GL11.glEnd();
            GL11.glDepthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GL11.glPopMatrix();
        }
    }

    public static void drawBorderedCircle(final int circleX, final int circleY, final double radius, final double width, final int borderColor, final int innerColor) {
        enableGL2D();
        drawCircle((float) circleX, (float) circleY, (float) (radius - 0.5 + width), 72, borderColor);
        drawFullCircle(circleX, circleY, radius, innerColor);
        disableGL2D();
    }

    public static void drawCircle(float cx, float cy, float r, final int num_segments, final int c) {
        GL11.glPushMatrix();
        cx *= 2.0f;
        cy *= 2.0f;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        final float theta = (float) (6.2831852 / num_segments);
        final float p = (float) Math.cos(theta);
        final float s = (float) Math.sin(theta);
        float x;
        r = (x = r * 2.0f);
        float y = 0.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(2);
        for (int ii = 0; ii < num_segments; ++ii) {
            GL11.glVertex2f(x + cx, y + cy);
            final float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
        GL11.glPopMatrix();
    }

    public static void drawFullCircle(int cx, int cy, double r, final int c) {
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 2160; ++i) {
            final double x = Math.sin(i * 3.141592653589793 / 360.0) * r;
            final double y = Math.cos(i * 3.141592653589793 / 360.0) * r;
            GL11.glVertex2d(cx + x, cy + y);
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }

    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
}