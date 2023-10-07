package koks.module.visual;

import koks.api.event.Event;
import koks.api.font.Fonts;
import koks.api.registry.module.Module;
import koks.api.utils.Animation;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import koks.event.Render2DEvent;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@Module.Info(name = "TargetHUD", category = Module.Category.VISUAL, description = "")
public class TargetHUD extends Module {

    final Animation animation = new Animation();

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof Render2DEvent) {
            final Resolution resolution = Resolution.getResolution();
            final RenderUtil renderUtil = RenderUtil.getInstance();

            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof final EntityLivingBase target) {

                float x = resolution.getWidth() / 2f + 20, y = resolution.getHeight() / 2f + 15, height = 45, width = 120;
                if (width < Fonts.ralewayRegular25.getStringWidth(target.getDisplayName().getFormattedText()) + 25) {
                    width = Fonts.ralewayRegular25.getStringWidth(target.getDisplayName().getFormattedText()) + 25 + 20;
                }

                renderUtil.drawOutlineRect(x, y, x + width, y + height, 1, new Color(25, 25, 25).getRGB(), new Color(33, 33, 33).getRGB());
                int cur = 1;
                for (double left = x - 1; left < x + width + 1; left += 1) {
                    double nextStep = left + 1;
                    nextStep = MathHelper.clamp_double(nextStep, x, x + width + 1);
                    renderUtil.drawRect(left, y  - 1, left + 1, y + 1, getRainbow(10 * cur, 6000, 0.7F, 1F).getRGB());
                    cur++;
                }

                GL11.glPushMatrix();
                Fonts.ralewayRegular25.drawString(target.getDisplayName().getFormattedText(), x + 25, y + 2, Color.white, false);
                GL11.glPopMatrix();

                final float p = target.getHealth() / target.getMaxHealth() > 1 ? 1 : target.getHealth() / target.getMaxHealth();
                animation.setGoalX(p);

                final int calcSpeed = 100 * (resolution.getWidth() * 100 / Toolkit.getDefaultToolkit().getScreenSize().width) / 100 + target.hurtTime;
                animation.setSpeed((float) Math.abs(Math.cos(Math.toRadians(Math.abs(animation.getX() - Math.abs(animation.getGoalX())))) * calcSpeed));
                final float animatedP = animation.getAnimationX();
                final float endX = ((width - 35) * animatedP);

                renderUtil.drawRect(x + 24, y + height / 2 - 3, x + width - 9, y + height / 2 + 3, new Color(25, 25, 25).getRGB());

                renderUtil.drawRect(x + 25, y + height / 2 - 2, x + 25 + endX, y + height / 2 + 2, getLinearHealthColor(animatedP).getRGB());

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LIGHTING);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                try {
                    GuiInventory.drawEntityOnScreen((int) (x + 12), (int) (y + 40), 18, getYaw(), target.rotationPitch * -1, target);
                } catch (ReportedException e1) {
                    e1.printStackTrace();
                }
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glPopMatrix();
            }
        }
    }


    private Color getLinearHealthColor(float percentage) {
        return new Color(1 - percentage, percentage, 0);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
