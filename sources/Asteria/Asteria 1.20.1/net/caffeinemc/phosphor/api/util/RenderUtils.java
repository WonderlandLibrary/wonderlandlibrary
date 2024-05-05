package net.caffeinemc.phosphor.api.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class RenderUtils {
    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void endRender() {
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static Vec3d getInterpolatedEntityPosition(Entity entity) {
        Vec3d ePos = entity.getPos();
        Vec3d ePrevPos = new Vec3d(entity.prevX, entity.prevY, entity.prevZ);
        float tickDelta = mc.getTickDelta();
        return new Vec3d(MathHelper.lerp(tickDelta, ePrevPos.x, ePos.x), MathHelper.lerp(tickDelta, ePrevPos.y, ePos.y), MathHelper.lerp(tickDelta, ePrevPos.z, ePos.z));
    }

    public static class R2D {
        public static boolean isOnScreen(Vec3d pos) {
            return pos != null && pos.z > -1 && pos.z < 1;
        }

        public static Vec3d getScreenSpaceCoordinate(Vec3d pos, MatrixStack stack) {
            Camera camera = mc.getEntityRenderDispatcher().camera;
            Matrix4f matrix = stack.peek().getPositionMatrix();
            int displayHeight = mc.getWindow().getHeight();
            int[] viewport = new int[4];
            Vector3f target = new Vector3f();

            double deltaX = pos.x - camera.getPos().x;
            double deltaY = pos.y - camera.getPos().y;
            double deltaZ = pos.z - camera.getPos().z;

            GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

            Vector4f transformedCoordinates = new Vector4f((float) deltaX, (float) deltaY, (float) deltaZ, 1.f).mul(matrix);

            Matrix4f matrixProj = new Matrix4f(RenderSystem.getProjectionMatrix());
            Matrix4f matrixModel = new Matrix4f(RenderSystem.getModelViewMatrix());

            matrixProj.mul(matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);

            return new Vec3d(
                    target.x / mc.getWindow().getScaleFactor(),
                    (displayHeight - target.y) / mc.getWindow().getScaleFactor(),
                    target.z
            );
        }

        public static void renderQuad(MatrixStack matrices, Color c, double x1, double y1, double x2, double y2) {
            double x11 = x1;
            double x21 = x2;
            double y11 = y1;
            double y21 = y2;
            int color = c.getRGB();
            double j;
            if (x11 < x21) {
                j = x11;
                x11 = x21;
                x21 = j;
            }

            if (y11 < y21) {
                j = y11;
                y11 = y21;
                y21 = j;
            }
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            float f = (float) (color >> 24 & 255) / 255.0F;
            float g = (float) (color >> 16 & 255) / 255.0F;
            float h = (float) (color >> 8 & 255) / 255.0F;
            float k = (float) (color & 255) / 255.0F;
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            setupRender();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(matrix, (float) x11, (float) y21, 0.0F).color(g, h, k, f).next();
            bufferBuilder.vertex(matrix, (float) x21, (float) y21, 0.0F).color(g, h, k, f).next();
            bufferBuilder.vertex(matrix, (float) x21, (float) y11, 0.0F).color(g, h, k, f).next();
            bufferBuilder.vertex(matrix, (float) x11, (float) y11, 0.0F).color(g, h, k, f).next();
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            endRender();
        }

        public static void renderCircle(MatrixStack matrices, Color c, double originX, double originY, double rad, int segments) {
            int segments1 = MathHelper.clamp(segments, 4, 360);
            int color = c.getRGB();

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            float f = (float) (color >> 24 & 255) / 255.0F;
            float g = (float) (color >> 16 & 255) / 255.0F;
            float h = (float) (color >> 8 & 255) / 255.0F;
            float k = (float) (color & 255) / 255.0F;
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            setupRender();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
            for (int i = 0; i < 360; i += Math.min(360 / segments1, 360 - i)) {
                double radians = Math.toRadians(i);
                double sin = Math.sin(radians) * rad;
                double cos = Math.cos(radians) * rad;
                bufferBuilder.vertex(matrix, (float) (originX + sin), (float) (originY + cos), 0).color(g, h, k, f).next();
            }
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            endRender();
        }

        public static void renderQuadGradient(MatrixStack matrices, Color c2, Color c1, double x1, double y1, double x2, double y2, boolean vertical) {
            double x11 = x1;
            double x21 = x2;
            double y11 = y1;
            double y21 = y2;
            float r1 = c1.getRed() / 255f;
            float g1 = c1.getGreen() / 255f;
            float b1 = c1.getBlue() / 255f;
            float a1 = c1.getAlpha() / 255f;
            float r2 = c2.getRed() / 255f;
            float g2 = c2.getGreen() / 255f;
            float b2 = c2.getBlue() / 255f;
            float a2 = c2.getAlpha() / 255f;

            double j;

            if (x11 < x21) {
                j = x11;
                x11 = x21;
                x21 = j;
            }

            if (y11 < y21) {
                j = y11;
                y11 = y21;
                y21 = j;
            }
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            setupRender();

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            if (vertical) {
                bufferBuilder.vertex(matrix, (float) x11, (float) y11, 0.0F).color(r1, g1, b1, a1).next();
                bufferBuilder.vertex(matrix, (float) x11, (float) y21, 0.0F).color(r2, g2, b2, a2).next();
                bufferBuilder.vertex(matrix, (float) x21, (float) y21, 0.0F).color(r2, g2, b2, a2).next();
                bufferBuilder.vertex(matrix, (float) x21, (float) y11, 0.0F).color(r1, g1, b1, a1).next();
            } else {
                bufferBuilder.vertex(matrix, (float) x11, (float) y11, 0.0F).color(r1, g1, b1, a1).next();
                bufferBuilder.vertex(matrix, (float) x11, (float) y21, 0.0F).color(r1, g1, b1, a1).next();
                bufferBuilder.vertex(matrix, (float) x21, (float) y21, 0.0F).color(r2, g2, b2, a2).next();
                bufferBuilder.vertex(matrix, (float) x21, (float) y11, 0.0F).color(r2, g2, b2, a2).next();
            }

            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            endRender();
        }

        public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) {
            renderRoundedQuadInternal(matrix, cr, cg, cb, ca, fromX, fromY, toX, toY, rad, rad, rad, rad, samples);
        }

        public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double radC1, double radC2,
                                                     double radC3, double radC4, double samples) {
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

            double[][] map = new double[][] { new double[] { toX - radC4, toY - radC4, radC4 }, new double[] { toX - radC2, fromY + radC2, radC2 },
                    new double[] { fromX + radC1, fromY + radC1, radC1 }, new double[] { fromX + radC3, toY - radC3, radC3 } };
            for (int i = 0; i < 4; i++) {
                double[] current = map[i];
                double rad = current[2];
                for (double r = i * 90d; r < 360 / 4d + i * 90d; r += 90 / samples) {
                    float rad1 = (float) Math.toRadians(r);
                    float sin = (float) (Math.sin(rad1) * rad);
                    float cos = (float) (Math.cos(rad1) * rad);
                    bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
                }
                float rad1 = (float) Math.toRadians(360 / 4d + i * 90d);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
            }
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        }

        public static void renderRoundedQuad(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double radC1, double radC2, double radC3, double radC4,
                                             double samples) {
            int color = c.getRGB();
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            float f = (float) (color >> 24 & 255) / 255.0F;
            float g = (float) (color >> 16 & 255) / 255.0F;
            float h = (float) (color >> 8 & 255) / 255.0F;
            float k = (float) (color & 255) / 255.0F;
            setupRender();
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);

            renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, radC1, radC2, radC3, radC4, samples);
            endRender();
        }

        public static void renderRoundedQuad(MatrixStack stack, Color c, double x, double y, double x1, double y1, double rad, double samples) {
            renderRoundedQuad(stack, c, x, y, x1, y1, rad, rad, rad, rad, samples);
        }
    }

    public static class R3D {
        interface RenderAction {
            void run(BufferBuilder buffer, float x, float y, float z, float x1, float y1, float z1, float red, float green, float blue, float alpha, Matrix4f matrix);
        }

        public static void renderLine(MatrixStack matrices, Color color, Vec3d start, Vec3d end) {
            Matrix4f s = matrices.peek().getPositionMatrix();
            genericAABBRender(
                    VertexFormat.DrawMode.DEBUG_LINES,
                    VertexFormats.POSITION_COLOR,
                    GameRenderer::getPositionColorProgram,
                    s,
                    start,
                    end.subtract(start),
                    color,
                    (buffer, x, y, z, x1, y1, z1, red, green, blue, alpha, matrix) -> {
                        buffer.vertex(matrix, x, y, z).color(red, green, blue, alpha).next();
                        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
                    }
            );
        }

        private static void genericAABBRender(VertexFormat.DrawMode mode, VertexFormat format, Supplier<ShaderProgram> shader, Matrix4f stack, Vec3d start, Vec3d dimensions, Color color, RenderAction action) {
            float red = color.getRed() / 255f;
            float green = color.getGreen() / 255f;
            float blue = color.getBlue() / 255f;
            float alpha = color.getAlpha() / 255f;
            Vec3d vec3d = start;
            Vec3d end = vec3d.add(dimensions);
            float x1 = (float) vec3d.x;
            float y1 = (float) vec3d.y;
            float z1 = (float) vec3d.z;
            float x2 = (float) end.x;
            float y2 = (float) end.y;
            float z2 = (float) end.z;
            useBuffer(mode, format, shader, bufferBuilder -> action.run(bufferBuilder, x1, y1, z1, x2, y2, z2, red, green, blue, alpha, stack));
        }

        private static void useBuffer(VertexFormat.DrawMode mode, VertexFormat format, Supplier<ShaderProgram> shader, Consumer<BufferBuilder> runner) {
            Tessellator t = Tessellator.getInstance();
            BufferBuilder bb = t.getBuffer();

            bb.begin(mode, format);

            runner.accept(bb);

            setupRender();
            RenderSystem.setShader(shader);
            BufferRenderer.drawWithGlobalProgram(bb.end());
            endRender();
        }
    }
}
