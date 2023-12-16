package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.render.ColorUtil;

import java.util.ArrayList;
import java.util.List;

@FunctionAnnotation(name = "Trails", type = Type.Render)
public class TrailsFunction extends Function {

    public List<Point> points = new ArrayList<>();

    // Метод обработки событий
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender render && render.isRender3D()) {
            if (mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON) {
                return;
            }
            long currentTime = System.currentTimeMillis();

            // Удаляем устаревшие точки
            points.removeIf(p -> (currentTime - p.time) > 400);

            // Интерполируем позицию игрока между текущим и предыдущим кадрами
            Vector3d playerPos = interpolatePlayerPosition(render.partialTicks);

            // Добавляем новую точку
            points.add(new Point(playerPos));

            // Отрисовываем точки и линии
            render3DPoints();
            RenderSystem.color4f(1,1,1,1);
        }
    }

    // Интерполирует позицию игрока между текущим и предыдущим кадрами
    private Vector3d interpolatePlayerPosition(float partialTicks) {
        return new Vector3d(
                MathUtil.interpolate(mc.player.getPosX(), mc.player.prevPosX, partialTicks),
                MathUtil.interpolate(mc.player.getPosY(), mc.player.prevPosY, partialTicks),
                MathUtil.interpolate(mc.player.getPosZ(), mc.player.prevPosZ, partialTicks)
        );
    }

    // Интерполирует позицию игрока между текущим и предыдущим кадрами
    private Vector3d interpolatePlayerPosition(PlayerEntity playerEntity, float partialTicks) {
        return new Vector3d(
                MathUtil.interpolate(playerEntity.getPosX(), playerEntity.prevPosX, partialTicks),
                MathUtil.interpolate(playerEntity.getPosY(), playerEntity.prevPosY, partialTicks),
                MathUtil.interpolate(playerEntity.getPosZ(), playerEntity.prevPosZ, partialTicks)
        );
    }

    // Отрисовывает точки и линии в трехмерном пространстве
    private void render3DPoints() {
        startRendering();
        // Отрисовка точек в виде полосы
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);


        int index = 0;
        for (Point p : points) {
            int c = ColorUtil.getColorStyle(index);
            float red = (float) (c >> 16 & 255) / 255.0F;
            float green = (float) (c >> 8 & 255) / 255.0F;
            float blue = (float) (c & 255) / 255.0F;
            float alpha = (float) index / (float) points.size() * 0.7f;
            Vector3d pos = p.pos.subtract(mc.getRenderManager().info.getProjectedView());
            buffer.pos(pos.x, pos.y + mc.player.getHeight(), pos.z).color(red, green, blue, alpha).endVertex();
            buffer.pos(pos.x, pos.y, pos.z).color(red, green, blue, alpha).endVertex();
            index++;
        }
        tessellator.draw();

        RenderSystem.lineWidth(2);

        // Отрисовка линий
        renderLineStrip(points, true);
        renderLineStrip(points, false);

        stopRendering();
    }

    // Отрисовывает линию на основе списка точек
    private void renderLineStrip(List<Point> points, boolean withHeight) {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        Vector3d projectedView = mc.getRenderManager().info.getProjectedView();


        int index = 0;
        for (Point p : points) {
            int c = ColorUtil.getColorStyle(index);
            float red = (float) (c >> 16 & 255) / 255.0F;
            float green = (float) (c >> 8 & 255) / 255.0F;
            float blue = (float) (c & 255) / 255.0F;
            float alpha = (float) index / (float) points.size() * 1.5f;
            alpha = Math.min(alpha, 1f);
            Vector3d pos = p.pos.subtract(projectedView);
            if (withHeight)
                buffer.pos(pos.x, pos.y + mc.player.getHeight(), pos.z).color(red, green, blue, alpha).endVertex();
            else
                buffer.pos(pos.x, pos.y, pos.z).color(red, green, blue, alpha).endVertex();
            index++;
        }

        tessellator.draw();
    }


    // Внутренний класс, представляющий точку
    class Point {
        public Vector3d pos;
        public long time;

        public Point(Vector3d pos) {
            this.pos = pos;
            this.time = System.currentTimeMillis();
        }
    }

    private void startRendering() {
        RenderSystem.pushMatrix();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.color4f(1, 1, 1, 0.5f);

    }

    private void stopRendering() {
        RenderSystem.enableAlphaTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        RenderSystem.popMatrix();
    }
}
