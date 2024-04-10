package info.sigmaclient.sigma.utils.render;

import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import static info.sigmaclient.sigma.modules.Module.mc;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.nglGetFloatv;
import static org.lwjgl.opengl.GL11C.nglGetIntegerv;

public class M3DUtil{
    private static final FloatBuffer windPos = BufferUtils.createFloatBuffer(4);
    private static final IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer floatBuffer1 = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer floatBuffer2 = GLAllocation.createDirectFloatBuffer(16);

    public static boolean isInView(Entity ent) {
        RenderUtils.renderPos s = RenderUtils.getRenderPos();
        return ent.isInRangeToRender3d(s.renderPosX, s.renderPosY, s.renderPosZ) || ent.ignoreFrustumCheck;
    }
    private static void __gluMultMatrixVecf(FloatBuffer m, float[] in, float[] out) {
        for(int i = 0; i < 4; ++i) {
            out[i] = in[0] * m.get(m.position() + i) + in[1] * m.get(m.position() + 4 + i) + in[2] * m.get(m.position() + 8 + i) + in[3] * m.get(m.position() + 12 + i);
        }
    }
    public static boolean gluProject(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
        float[] in = new float[4];
        float[] out = new float[4];
        in[0] = objx;
        in[1] = objy;
        in[2] = objz;
        in[3] = 1.0F;
        __gluMultMatrixVecf(modelMatrix, in, out);
        __gluMultMatrixVecf(projMatrix, out, in);
        if ((double)in[3] == 0.0) {
            return false;
        } else {
            in[3] = 1.0F / in[3] * 0.5F;
            in[0] = in[0] * in[3] + 0.5F;
            in[1] = in[1] * in[3] + 0.5F;
            in[2] = in[2] * in[3] + 0.5F;
            win_pos.put(0, in[0] * (float)viewport.get(viewport.position() + 2) + (float)viewport.get(viewport.position() + 0));
            win_pos.put(1, in[1] * (float)viewport.get(viewport.position() + 3) + (float)viewport.get(viewport.position() + 1));
            win_pos.put(2, in[2]);
            return true;
        }
    }



    public static void glGetFloat(int pname, FloatBuffer params) {
        nglGetFloatv(pname, MemoryUtil.memAddress(params));
    }
    public static void glGetInteger(int pname, IntBuffer params) {
        nglGetIntegerv(pname, MemoryUtil.memAddress(params));
    }

    public static Vector3f projectOn2D(float x, float y, float z, int scaleFactor) {
        glGetFloat(GL_MODELVIEW_MATRIX, floatBuffer1);
        glGetFloat(GL_PROJECTION_MATRIX, floatBuffer2);
        glGetInteger(GL_VIEWPORT, intBuffer);
        if (gluProject(x, y, z, floatBuffer1, floatBuffer2, intBuffer, windPos)) {
            return new Vector3f(windPos.get(0) / scaleFactor, (mc.getMainWindow().getFramebufferHeight() - windPos.get(1)) / scaleFactor, windPos.get(2));
        }
        return null;
    }

    public static double[] getInterpolatedPos(Entity entity) {
        float ticks = mc.timer.renderPartialTicks;
        RenderUtils.renderPos s = RenderUtils.getRenderPos();
        return new double[]{
                MathHelper.lerp(ticks, entity.lastTickPosX, entity.getPosX()) - s.renderPosX,
                MathHelper.lerp(ticks, entity.lastTickPosY, entity.getPosY()) - s.renderPosY,
                MathHelper.lerp(ticks, entity.lastTickPosZ, entity.getPosZ()) - s.renderPosZ
        };
    }
    public static double[] getInterpolatedPos(Vector3d entity) {
        RenderUtils.renderPos s = RenderUtils.getRenderPos();
        return new double[]{
                entity.x - s.renderPosX,
                entity.y - s.renderPosY,
                entity.z - s.renderPosZ
        };
    }

    public static AxisAlignedBB getInterpolatedBoundingBox(Vector3d entity) {
        final double[] renderingEntityPos = getInterpolatedPos(entity);
        final double entityRenderWidth = 0.6 / 1.5;
        return new AxisAlignedBB(renderingEntityPos[0] - entityRenderWidth,
                renderingEntityPos[1], renderingEntityPos[2] - entityRenderWidth, renderingEntityPos[0] + entityRenderWidth,
                renderingEntityPos[1] + 1.8f + 0.18, renderingEntityPos[2] + entityRenderWidth).expand(0.15, 0.15, 0.15);
    }

    public static AxisAlignedBB getInterpolatedBoundingBox(Entity entity) {
        final double[] renderingEntityPos = getInterpolatedPos(entity);
        final double entityRenderWidth = entity.getWidth() / 1.5;
        return new AxisAlignedBB(renderingEntityPos[0] - entityRenderWidth,
                renderingEntityPos[1], renderingEntityPos[2] - entityRenderWidth, renderingEntityPos[0] + entityRenderWidth,
                renderingEntityPos[1] + entity.getHeight() + (entity.isSneaking() ? -0.3 : 0.18), renderingEntityPos[2] + entityRenderWidth).expand(0.15, 0.15, 0.15);
    }

    public static Vector4f getEntityPositionsOn2D(Entity entity) {
        final AxisAlignedBB bb = getInterpolatedBoundingBox(entity);

        float yOffset = 0;


        final List<Vector3f> vectors = Arrays.asList(
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.maxY - yOffset, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY - yOffset, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.minX, (float) bb.maxY - yOffset, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY - yOffset, (float) bb.maxZ));

        Vector4f entityPos = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
        ScaledResolution sr = new ScaledResolution(mc);
        for (Vector3f vector3f : vectors) {
            vector3f = projectOn2D(vector3f.x, vector3f.y, vector3f.z, sr.getScaleFactor());
            if (vector3f != null && vector3f.z >= 0.0 && vector3f.z < 1.0) {
                entityPos.x = Math.min(vector3f.x, entityPos.x);
                entityPos.y = Math.min(vector3f.y, entityPos.y);
                entityPos.z = Math.max(vector3f.x, entityPos.z);
                entityPos.w = Math.max(vector3f.y, entityPos.w);
            }
        }
        return entityPos;
    }


}
