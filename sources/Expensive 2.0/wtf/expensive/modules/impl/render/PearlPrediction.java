package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import org.joml.Vector2d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.ProjectionUtils;
import wtf.expensive.util.render.RenderUtil;

/**
 * @author dedinside
 * @since 26.06.2023
 */
@FunctionAnnotation(name = "Pearl Prediction", type = Type.Render)
public class PearlPrediction extends Function {
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender && ((EventRender) event).isRender3D()) {

            RenderSystem.pushMatrix();
            RenderSystem.translated(-mc.getRenderManager().renderPosX(), -mc.getRenderManager().renderPosY(),-mc.getRenderManager().renderPosZ());
            RenderSystem.enableBlend();
            RenderSystem.disableTexture();
            RenderSystem.disableDepthTest();
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            RenderSystem.lineWidth(2);
            buffer.begin(1, DefaultVertexFormats.POSITION_COLOR);

            for (Entity e : mc.world.getAllEntities()) {
                if (e instanceof EnderPearlEntity pearl) {
                    renderLine(pearl);
                }
            }
            buffer.finishDrawing();
            WorldVertexBufferUploader.draw(buffer);
            RenderSystem.enableDepthTest();
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            RenderSystem.popMatrix();
        }


    }

    private double calculateInterpolatedPosition(double previousPos, double currentPos, float partialTicks) {
        return -(previousPos + (currentPos - previousPos) * partialTicks);
    }

    private void renderLine(EnderPearlEntity pearl) {

        Vector3d pearlPosition = pearl.getPositionVec().add(0, 0, 0);
        Vector3d pearlMotion = pearl.getMotion();
        Vector3d lastPosition;

        for (int i = 0; i <= 300; i++) {
            lastPosition = pearlPosition;
            pearlPosition = pearlPosition.add(pearlMotion);
            pearlMotion = updatePearlMotion(pearl, pearlMotion);

            if (shouldEntityHit(pearlPosition.add(0, 1, 0), lastPosition.add(0, 1, 0)) || pearlPosition.y <= 0) {
                break;
            }
            float[] colors = getLineColor(i);
            buffer.pos(lastPosition.x, lastPosition.y, lastPosition.z).color(colors[0], colors[1], colors[2], 1).endVertex();
            buffer.pos(pearlPosition.x, pearlPosition.y, pearlPosition.z).color(colors[0], colors[1], colors[2], 1).endVertex();
        }
    }

    private Vector3d updatePearlMotion(EnderPearlEntity pearl, Vector3d originalPearlMotion) {
        Vector3d pearlMotion = originalPearlMotion;
        if (pearl.isInWater()) {
            pearlMotion = pearlMotion.scale(0.8f);
        } else {
            pearlMotion = pearlMotion.scale(0.99f);
        }

        if (!pearl.hasNoGravity())
            pearlMotion.y -= pearl.getGravityVelocity();

        return pearlMotion;
    }

    private boolean shouldEntityHit(Vector3d pearlPosition, Vector3d lastPosition) {
        final RayTraceContext rayTraceContext = new RayTraceContext(
                lastPosition,
                pearlPosition,
                RayTraceContext.BlockMode.COLLIDER,
                RayTraceContext.FluidMode.NONE,
                mc.player
        );
        final BlockRayTraceResult blockHitResult = mc.world.rayTraceBlocks(rayTraceContext);

        return blockHitResult.getType() == RayTraceResult.Type.BLOCK;
    }

    private float[] getLineColor(int index) {
        int color = Managment.STYLE_MANAGER.getCurrentStyle().getColor(index * 2);
        return RenderUtil.IntColor.rgb(color);
    }
}
