package wtf.evolution.helpers.render;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import wtf.evolution.helpers.render.cape.Matrix4f;
import wtf.evolution.helpers.render.cape.PoseStack;
import wtf.evolution.helpers.render.cape.Vector3f;
import wtf.evolution.helpers.render.cape.Vector4f;


public class SmoothCapeRender
{
    public void renderSmoothCape(final LayerCape layer, final AbstractClientPlayer abstractClientPlayer, final float delta) {
        final BufferBuilder worldrenderer = Tessellator.getInstance().getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        final PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        Matrix4f oldPositionMatrix = null;
        for (int part = 0; part < 16; ++part) {
            this.modifyPoseStack(layer, poseStack, abstractClientPlayer, delta, part);
            if (oldPositionMatrix == null) {
                oldPositionMatrix = poseStack.last().pose();
            }
            if (part == 0) {
                addTopVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3f, 0.0f, 0.0f, -0.3f, 0.0f, -0.06f, part);
            }
            if (part == 15) {
                addBottomVertex(worldrenderer, poseStack.last().pose(), poseStack.last().pose(), 0.3f, (part + 1) * 0.06f, 0.0f, -0.3f, (part + 1) * 0.06f, -0.06f, part);
            }
            addLeftVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, -0.3f, (part + 1) * 0.06f, 0.0f, -0.3f, part * 0.06f, -0.06f, part);
            addRightVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3f, (part + 1) * 0.06f, 0.0f, 0.3f, part * 0.06f, -0.06f, part);
            addBackVertex(worldrenderer, poseStack.last().pose(), oldPositionMatrix, 0.3f, (part + 1) * 0.06f, -0.06f, -0.3f, part * 0.06f, -0.06f, part);
            addFrontVertex(worldrenderer, oldPositionMatrix, poseStack.last().pose(), 0.3f, (part + 1) * 0.06f, 0.0f, -0.3f, part * 0.06f, 0.0f, part);
            oldPositionMatrix = poseStack.last().pose();
            poseStack.popPose();
        }
        Tessellator.getInstance().draw();
    }

    void modifyPoseStack(final LayerCape layer, final PoseStack poseStack, final AbstractClientPlayer abstractClientPlayer, final float h, final int part) {
        this.modifyPoseStackSimulation(layer, poseStack, abstractClientPlayer, h, part);
    }

    private void modifyPoseStackSimulation(final LayerCape layer, final PoseStack poseStack, final AbstractClientPlayer abstractClientPlayer, final float delta, final int part) {
        final Simulation simulation = ((CapeHolder)abstractClientPlayer).getSimulation();
        poseStack.pushPose();
        poseStack.translate(0.0, 0.0, 0.125);
        float z = simulation.points.get(part).getLerpX(delta) - simulation.points.get(0).getLerpX(delta);
        if (z > 0.0f) {
            z = 0.0f;
        }
        final float y = simulation.points.get(0).getLerpY(delta) - part - simulation.points.get(part).getLerpY(delta);
        final float sidewaysRotationOffset = 0.0f;
        float partRotation = (float)(-Math.atan2(y, z));
        partRotation = Math.max(partRotation, 0.0f);
        if (partRotation != 0.0f) {
            partRotation = (float)(3.141592653589793 - partRotation);
        }
        partRotation *= (float)57.2958;
        partRotation *= 2.0f;
        float height = 0.0f;
        if (abstractClientPlayer.isSneaking()) {
            height += 25.0f;
            poseStack.translate(0.0, 0.15000000596046448, 0.0);
        }
        final float naturalWindSwing = layer.getNatrualWindSwing(part);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(6.0f + height + naturalWindSwing));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(sidewaysRotationOffset / 2.0f));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f - sidewaysRotationOffset / 2.0f));
        poseStack.translate(0.0, y / 16.0f, z / 16.0f);
        poseStack.translate(0.0, 0.03, -0.03);
        poseStack.translate(0.0, part * 1.0f / 16.0f, 0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-partRotation));
        poseStack.translate(0.0, -part * 1.0f / 16.0f, 0);
        poseStack.translate(0.0, -0.03, 0.03);
    }

    private static void addBackVertex(final BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, final float z1, float x2, float y2, final float z2, final int part) {
        if (x1 < x2) {
            final float i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            final float i = y1;
            y1 = y2;
            y2 = i;
            final Matrix4f k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }
        final float minU = 0.015625f;
        final float maxU = 0.171875f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        final float deltaV = maxV - minV;
        final float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (part + 1);
        minV += vPerPart * part;
        vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(maxU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z2).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addFrontVertex(final BufferBuilder worldrenderer, Matrix4f matrix, Matrix4f oldMatrix, float x1, float y1, final float z1, float x2, float y2, final float z2, final int part) {
        if (x1 < x2) {
            final float i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            final float i = y1;
            y1 = y2;
            y2 = i;
            final Matrix4f k = matrix;
            matrix = oldMatrix;
            oldMatrix = k;
        }
        final float minU = 0.1875f;
        final float maxU = 0.34375f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        final float deltaV = maxV - minV;
        final float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (part + 1);
        minV += vPerPart * part;
        vertex(worldrenderer, oldMatrix, x1, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y1, z1).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y2, z2).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y2, z2).tex(maxU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addLeftVertex(final BufferBuilder worldrenderer, final Matrix4f matrix, final Matrix4f oldMatrix, float x1, float y1, final float z1, float x2, float y2, final float z2, final int part) {
        if (x1 < x2) {
            final float i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            final float i = y1;
            y1 = y2;
            y2 = i;
        }
        final float minU = 0.0f;
        final float maxU = 0.015625f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        final float deltaV = maxV - minV;
        final float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (part + 1);
        minV += vPerPart * part;
        vertex(worldrenderer, matrix, x2, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(maxU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addRightVertex(final BufferBuilder worldrenderer, final Matrix4f matrix, final Matrix4f oldMatrix, float x1, float y1, final float z1, float x2, float y2, final float z2, final int part) {
        if (x1 < x2) {
            x2 = x1;
        }
        if (y1 < y2) {
            final float i = y1;
            y1 = y2;
            y2 = i;
        }
        final float minU = 0.171875f;
        final float maxU = 0.1875f;
        float minV = 0.03125f;
        float maxV = 0.53125f;
        final float deltaV = maxV - minV;
        final float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (part + 1);
        minV += vPerPart * part;
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(maxU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static void addBottomVertex(final BufferBuilder worldrenderer, final Matrix4f matrix, final Matrix4f oldMatrix, float x1, float y1, final float z1, float x2, float y2, final float z2, final int part) {
        if (x1 < x2) {
            final float i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            final float i = y1;
            y1 = y2;
            y2 = i;
        }
        final float minU = 0.171875f;
        final float maxU = 0.328125f;
        float minV = 0.0f;
        float maxV = 0.03125f;
        final float deltaV = maxV - minV;
        final float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (part + 1);
        minV += vPerPart * part;
        vertex(worldrenderer, oldMatrix, x1, y2, z2).tex(maxU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z2).tex(minU, minV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z1).tex(minU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z1).tex(maxU, maxV).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    private static BufferBuilder vertex(final BufferBuilder worldrenderer, final Matrix4f matrix4f, final float f, final float g, final float h) {
        final Vector4f vector4f = new Vector4f(f, g, h, 1.0f);
        vector4f.transform(matrix4f);
        worldrenderer.pos(vector4f.x(), vector4f.y(), vector4f.z());
        return worldrenderer;
    }

    private static void addTopVertex(final BufferBuilder worldrenderer, final Matrix4f matrix, final Matrix4f oldMatrix, float x1, float y1, final float z1, float x2, float y2, final float z2, final int part) {
        if (x1 < x2) {
            final float i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            final float i = y1;
            y1 = y2;
            y2 = i;
        }
        final float minU = 0.015625f;
        final float maxU = 0.171875f;
        float minV = 0.0f;
        float maxV = 0.03125f;
        final float deltaV = maxV - minV;
        final float vPerPart = deltaV / 16.0f;
        maxV = minV + vPerPart * (part + 1);
        minV += vPerPart * part;
        vertex(worldrenderer, oldMatrix, x1, y2, z1).tex(maxU, maxV).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertex(worldrenderer, oldMatrix, x2, y2, z1).tex(minU, maxV).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x2, y1, z2).tex(minU, minV).normal(0.0f, 1.0f, 0.0f).endVertex();
        vertex(worldrenderer, matrix, x1, y1, z2).tex(maxU, minV).normal(0.0f, 1.0f, 0.0f).endVertex();
    }
}