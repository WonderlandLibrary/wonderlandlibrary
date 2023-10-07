package sudo.utils.render;

import java.awt.Color;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class Vertexer1 {

    public static final int CULL_BACK = 0;
    public static final int CULL_FRONT = 1;
    public static final int CULL_NONE = 2;

    public static void vertexBoxQuads(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, QuadColor quadColor, Direction... excludeDirs) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        int cullMode = excludeDirs.length == 0 ? CULL_BACK : CULL_NONE;

        if (!ArrayUtils.contains(excludeDirs, Direction.DOWN)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.WEST)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.EAST)) {
            vertexQuad(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.NORTH)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.SOUTH)) {
            vertexQuad(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.UP)) {
            vertexQuad(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, x2, y2, z1, x1, y2, z1, cullMode, quadColor);
        }
    }
    
    public static void vertexBottomFilledRect(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, QuadColor quadColor, Direction... excludeDirs) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float z2 = (float) box.maxZ;

        int cullMode = excludeDirs.length == 0 ? CULL_BACK : CULL_NONE;

        if (!ArrayUtils.contains(excludeDirs, Direction.DOWN)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.WEST)) {
            vertexQuad(matrices, vertexConsumer, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.EAST)) {
            vertexQuad(matrices, vertexConsumer, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.NORTH)) {
            vertexQuad(matrices, vertexConsumer, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.SOUTH)) {
            vertexQuad(matrices, vertexConsumer, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, cullMode, quadColor);
        }

        if (!ArrayUtils.contains(excludeDirs, Direction.UP)) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z2, x2, y1, z2, x2, y1, z1, x1, y1, z1, cullMode, quadColor);
        }
    }

    public static void vertexBoxQuadsFace(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, QuadColor quadColor, Direction dir) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        int cullMode = CULL_BACK;

        if (dir == Direction.DOWN) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, cullMode, quadColor);
        }

        if (dir == Direction.WEST) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1, cullMode, quadColor);
        }

        if (dir == Direction.EAST) {
            vertexQuad(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, cullMode, quadColor);
        }

        if (dir == Direction.NORTH) {
            vertexQuad(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, cullMode, quadColor);
        }

        if (dir == Direction.SOUTH) {
            vertexQuad(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2, cullMode, quadColor);
        }

        if (dir == Direction.UP) {
            vertexQuad(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, x2, y2, z1, x1, y2, z1, cullMode, quadColor);
        }
    }

    public static void vertexQuad(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int cullMode, QuadColor quadColor) {
        int[] color = quadColor.getAllColors();

        if (cullMode != CULL_FRONT) {
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x1, y1, z1).color(color[0], color[1], color[2], color[3]).next();
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x2, y2, z2).color(color[4], color[5], color[6], color[7]).next();
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x3, y3, z3).color(color[8], color[9], color[10], color[11]).next();
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x4, y4, z4).color(color[12], color[13], color[14], color[15]).next();
        }

        if (cullMode != CULL_BACK) {
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x4, y4, z4).color(color[0], color[1], color[2], color[3]).next();
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x3, y3, z3).color(color[4], color[5], color[6], color[7]).next();
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x2, y2, z2).color(color[8], color[9], color[10], color[11]).next();
            vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x1, y1, z1).color(color[12], color[13], color[14], color[15]).next();
        }
    }

    public static void vertexBottomOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, QuadColor quadColor, Direction... excludeDirs) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float z2 = (float) box.maxZ;

        boolean exDown = ArrayUtils.contains(excludeDirs, Direction.DOWN);

        int[] color = quadColor.getAllColors();

        if (!exDown) {
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x1, y1, z2, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y1, z1, LineColor.single(color[12], color[13], color[14], color[15]));
        }

//        if (!exWest) {
//            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
//            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
//            vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
//            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z1, x1, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
//        }
//
//        if (!exEast) {
//            if (exDown) vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
//            vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
//            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
//            if (exUp) vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
//        }
//
//        if (!exNorth) {
//            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, LineColor.single(color[0], color[1], color[2], color[3]));
//            if (exEast) vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, LineColor.single(color[4], color[5], color[6], color[7]));
//            if (exWest) vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
//            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, LineColor.single(color[12], color[13], color[14], color[15]));
//        }
//
//        if (!exSouth) {
//            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z2, x2, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
//            if (exEast) vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
//            if (exWest) vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, LineColor.single(color[8], color[9], color[10], color[11]));
//            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
//        }
//
//        if (!exUp) {
//            vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, LineColor.single(color[0], color[1], color[2], color[3]));
//            vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
//            vertexLine(matrices, vertexConsumer, x2, y2, z2, x1, y2, z2, LineColor.single(color[8], color[9], color[10], color[11]));
//            vertexLine(matrices, vertexConsumer, x1, y2, z2, x1, y2, z1, LineColor.single(color[12], color[13], color[14], color[15]));
//        }
    }

    public static void vertexBoxLines(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, QuadColor quadColor, Direction... excludeDirs) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        boolean exDown = ArrayUtils.contains(excludeDirs, Direction.DOWN);
        boolean exWest = ArrayUtils.contains(excludeDirs, Direction.WEST);
        boolean exEast = ArrayUtils.contains(excludeDirs, Direction.EAST);
        boolean exNorth = ArrayUtils.contains(excludeDirs, Direction.NORTH);
        boolean exSouth = ArrayUtils.contains(excludeDirs, Direction.SOUTH);
        boolean exUp = ArrayUtils.contains(excludeDirs, Direction.UP);

        int[] color = quadColor.getAllColors();

        if (!exDown) {
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x1, y1, z2, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y1, z1, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (!exWest) {
            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z1, x1, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (!exEast) {
            if (exDown) vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
            if (exUp) vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (!exNorth) {
            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, LineColor.single(color[0], color[1], color[2], color[3]));
            if (exEast) vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, LineColor.single(color[4], color[5], color[6], color[7]));
            if (exWest) vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (!exSouth) {
            if (exDown) vertexLine(matrices, vertexConsumer, x1, y1, z2, x2, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
            if (exEast) vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            if (exWest) vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, LineColor.single(color[8], color[9], color[10], color[11]));
            if (exUp) vertexLine(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (!exUp) {
            vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x2, y2, z2, x1, y2, z2, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y2, z2, x1, y2, z1, LineColor.single(color[12], color[13], color[14], color[15]));
        }
    }
    public static void vertexLine(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, LineColor lineColor) {
        Matrix4f model = matrices.peek().getPositionMatrix();
        Matrix3f normal = matrices.peek().getNormalMatrix();

        Vec3f normalVec = getNormal(normal, x1, y1, z1, x2, y2, z2);

        int[] color1 = lineColor.getColor(x1, y1, z1, 0);
        int[] color2 = lineColor.getColor(x2, y2, z2, 1);

        vertexConsumer.vertex(model, x1, y1, z1).color(color1[0], color1[1], color1[2], color1[3]).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
        vertexConsumer.vertex(model, x2, y2, z2).color(color2[0], color2[1], color2[2], color2[3]).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
    }

    public static void vertexTri(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Color c) {
        Matrix4f model = matrices.peek().getPositionMatrix();
        Matrix3f normal = matrices.peek().getNormalMatrix();

        Vec3f normalVec = getNormal(normal, x1, y1, z1, x2, y2, z2);

        float r = c.getRed() / 255f;
        float g = c.getGreen() / 255f;
        float b = c.getBlue() / 255f;
        float a = c.getAlpha() / 255f;

        vertexConsumer.vertex(model, x1, y1, z1).color(r, g, b, a).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
        vertexConsumer.vertex(model, x2, y2, z2).color(r, g, b, a).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
        vertexConsumer.vertex(model, x3, y3, z3).color(r, g, b, a).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
    }

    public static void vertexTri(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Color c1, Color c2) {
        Matrix4f model = matrices.peek().getPositionMatrix();
        Matrix3f normal = matrices.peek().getNormalMatrix();

        Vec3f normalVec = getNormal(normal, x1, y1, z1, x2, y2, z2);

        float r1 = c1.getRed() / 255f;
        float g1 = c1.getGreen() / 255f;
        float b1 = c1.getBlue() / 255f;
        float a1 = c1.getAlpha() / 255f;

        float r2 = c2.getRed() / 255f;
        float g2 = c2.getGreen() / 255f;
        float b2 = c2.getBlue() / 255f;
        float a2 = c2.getAlpha() / 255f;

        vertexConsumer.vertex(model, x1, y1, z1).color(r1, g1, b1, a1).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
        vertexConsumer.vertex(model, x2, y2, z2).color(r2, g2, b2, a2).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
        vertexConsumer.vertex(model, x3, y3, z3).color(r2, g2, b2, a2).normal(normal, normalVec.getX(), normalVec.getY(), normalVec.getZ()).next();
    }

    public static Vec3f getNormal(Matrix3f normal, float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = MathHelper.sqrt(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);

        return new Vec3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }

    public static void vertexBoxLinesFace(MatrixStack matrices, VertexConsumer vertexConsumer, Box box, QuadColor quadColor, Direction face) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        boolean exDown = face == Direction.DOWN;
        boolean exWest = face == Direction.WEST;
        boolean exEast = face == Direction.EAST;
        boolean exNorth = face == Direction.NORTH;
        boolean exSouth = face == Direction.SOUTH;
        boolean exUp = face == Direction.UP;

        int[] color = quadColor.getAllColors();

        if (exDown) {
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x1, y1, z2, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y1, z1, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (exWest) {
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y2, z1, x1, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (exEast) {
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (exNorth) {
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x2, y1, z1, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y1, z1, x2, y2, z1, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x1, y1, z1, x1, y2, z1, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (exSouth) {
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x2, y1, z2, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y1, z2, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x1, y1, z2, x1, y2, z2, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y2, z2, x2, y2, z2, LineColor.single(color[12], color[13], color[14], color[15]));
        }

        if (exUp) {
            vertexLine(matrices, vertexConsumer, x1, y2, z1, x2, y2, z1, LineColor.single(color[0], color[1], color[2], color[3]));
            vertexLine(matrices, vertexConsumer, x2, y2, z1, x2, y2, z2, LineColor.single(color[4], color[5], color[6], color[7]));
            vertexLine(matrices, vertexConsumer, x2, y2, z2, x1, y2, z2, LineColor.single(color[8], color[9], color[10], color[11]));
            vertexLine(matrices, vertexConsumer, x1, y2, z2, x1, y2, z1, LineColor.single(color[12], color[13], color[14], color[15]));
        }
    }

}
