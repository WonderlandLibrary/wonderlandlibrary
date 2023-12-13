package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import today.flux.event.WorldRenderEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ColorValue;
import today.flux.module.value.FloatValue;

import java.awt.*;

public class RacistHat extends Module {
    public static ColorValue racistHatColours = new ColorValue("ChinaHat", "China Hat", Color.magenta);

    public BooleanValue renderInFirstPerson = new BooleanValue("ChinaHat", "ShowInFirstPerson", false);
    public FloatValue side = new FloatValue("ChinaHat", "Side", 45.0f, 30.0f, 50.0f, 1.0f);
    public FloatValue stack = new FloatValue("ChinaHat", "Stacks", 50.0f, 45.0f, 200.0f, 5.0f);

    public RacistHat() {
        super("ChinaHat", Category.Render, false);
    }

    @EventTarget
    public void onRender3D(WorldRenderEvent evt) {
        if(mc.gameSettings.thirdPersonView == 0 && !renderInFirstPerson.getValueState()) {
            return;
        }

        this.drawChinaHat(mc.thePlayer, evt);
    }

    private void drawChinaHat(EntityLivingBase entity, WorldRenderEvent evt) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)evt.getPartialTicks() - mc.getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)evt.getPartialTicks() - mc.getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)evt.getPartialTicks() - mc.getRenderManager().renderPosZ;
        int side = (int) this.side.getValueState();
        int stack = (int) this.stack.getValueState();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y + (mc.thePlayer.isSneaking() ? 2.0 : 2.2), z);

        GL11.glRotatef(-entity.width, 0.0f, 1.0f, 0.0f);

        Color col = racistHatColours.getColor();
        GL11.glColor4f(col.getRed() / 255f, col.getGreen() / 255f, col.getBlue() / 255f, 0.2f);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glLineWidth(1.0f);

        Cylinder c = new Cylinder();
        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
        c.setDrawStyle(100011);
        c.draw(0.0f, 0.8f, 0.4f, side, stack);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glPopMatrix();
    }

}
