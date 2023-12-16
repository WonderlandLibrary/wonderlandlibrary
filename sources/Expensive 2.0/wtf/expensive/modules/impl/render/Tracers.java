package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ColorSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.render.ColorUtil;

/**
 * @author dedinside
 * @since 01.07.2023
 */
@FunctionAnnotation(name = "Tracers", type = Type.Render)
public class Tracers extends Function {
    public ColorSetting color = new ColorSetting("Цвет", ColorUtil.rgba(255, 255, 255, 255));
    public ColorSetting friendColor = new ColorSetting("Цвет друзей", ColorUtil.rgba(0, 255, 0, 255));

    public SliderSetting width = new SliderSetting("Ширина", 1, 1, 10, 0.1f);

    public SliderSetting alpha = new SliderSetting("Прозрачность", 255, 0, 255, 1);

    public BooleanOption ignoreNaked = new BooleanOption("Игнорировать голых", true);

    public Tracers() {
        addSettings(color, friendColor, width, alpha, ignoreNaked);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender eventRender) {
            if (eventRender.isRender3D()) {

                GL11.glPushMatrix();
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(width.getValue().floatValue());
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
                Vector3d vec = new Vector3d(0, 0, 150)
                        .rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch)))
                        .rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

                float alpha = ((int) this.alpha.getValue().floatValue()) / 255F;

                for (PlayerEntity entity : mc.world.getPlayers()) {
                    if (entity instanceof RemoteClientPlayerEntity && entity.botEntity && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
                        int tracersColor = Managment.FRIEND_MANAGER.isFriend(entity.getName().getString()) ? friendColor.get() : color.get();

                        double x = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX) * mc.getRenderPartialTicks() - mc.getRenderManager().info.getProjectedView().getX();
                        double y = entity.lastTickPosY + (entity.getPosY() - entity.lastTickPosY) * mc.getRenderPartialTicks() - mc.getRenderManager().info.getProjectedView().getY();
                        double z = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ) * mc.getRenderPartialTicks() - mc.getRenderManager().info.getProjectedView().getZ();

                        ColorUtil.setColor(tracersColor);

                        bufferBuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
                        bufferBuilder.pos(vec.x, vec.y, vec.z).endVertex();
                        bufferBuilder.pos(x, y, z).endVertex();
                        Tessellator.getInstance().draw();
                    }
                }
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GlStateManager.color4f(1, 1, 1, 1);
                GL11.glPopMatrix();
            }
        }
    }
}
