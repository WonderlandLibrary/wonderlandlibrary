/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 *  org.lwjgl.util.vector.Vector3f
 *  org.lwjgl.util.vector.Vector4f
 */
package net.ccbluex.liquidbounce.utils.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import net.ccbluex.liquidbounce.utils.math.MathUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class ESPUtil {
    private static final Frustum frustum = new Frustum();
    private static final FloatBuffer windPos = BufferUtils.createFloatBuffer((int)4);
    private static final IntBuffer intBuffer = GLAllocation.func_74527_f((int)16);
    private static final FloatBuffer floatBuffer1 = GLAllocation.func_74529_h((int)16);
    private static final FloatBuffer floatBuffer2 = GLAllocation.func_74529_h((int)16);

    public static boolean isInView(Entity ent) {
        frustum.func_78547_a(Stencil.mc.func_175606_aa().field_70165_t, Stencil.mc.func_175606_aa().field_70163_u, Stencil.mc.func_175606_aa().field_70161_v);
        return frustum.func_78546_a(ent.func_174813_aQ()) || ent.field_70158_ak;
    }

    public static Vector3f projectOn2D(float x, float y, float z, int scaleFactor) {
        GL11.glGetFloat((int)2982, (FloatBuffer)floatBuffer1);
        GL11.glGetFloat((int)2983, (FloatBuffer)floatBuffer2);
        GL11.glGetInteger((int)2978, (IntBuffer)intBuffer);
        if (GLU.gluProject((float)x, (float)y, (float)z, (FloatBuffer)floatBuffer1, (FloatBuffer)floatBuffer2, (IntBuffer)intBuffer, (FloatBuffer)windPos)) {
            return new Vector3f(windPos.get(0) / (float)scaleFactor, ((float)Stencil.mc.field_71440_d - windPos.get(1)) / (float)scaleFactor, windPos.get(2));
        }
        return null;
    }

    public static double[] getInterpolatedPos(Entity entity) {
        float ticks = Stencil.mc.field_71428_T.field_74281_c;
        return new double[]{MathUtils.interpolate(entity.field_70142_S, entity.field_70165_t, ticks) - Stencil.mc.func_175598_ae().field_78730_l, MathUtils.interpolate(entity.field_70137_T, entity.field_70163_u, ticks) - Stencil.mc.func_175598_ae().field_78731_m, MathUtils.interpolate(entity.field_70136_U, entity.field_70161_v, ticks) - Stencil.mc.func_175598_ae().field_78728_n};
    }

    public static Vector4f getEntityPositionsOn2D(Entity entity) {
        double[] renderingEntityPos = ESPUtil.getInterpolatedPos(entity);
        double entityRenderWidth = (double)entity.field_70130_N / 1.5;
        AxisAlignedBB bb = new AxisAlignedBB(renderingEntityPos[0] - entityRenderWidth, renderingEntityPos[1], renderingEntityPos[2] - entityRenderWidth, renderingEntityPos[0] + entityRenderWidth, renderingEntityPos[1] + (double)entity.field_70131_O + (entity.func_70093_af() ? -0.3 : 0.18), renderingEntityPos[2] + entityRenderWidth).func_72314_b(0.15, 0.15, 0.15);
        List<Vector3f> vectors = Arrays.asList(new Vector3f((float)bb.field_72340_a, (float)bb.field_72338_b, (float)bb.field_72339_c), new Vector3f((float)bb.field_72340_a, (float)bb.field_72337_e, (float)bb.field_72339_c), new Vector3f((float)bb.field_72336_d, (float)bb.field_72338_b, (float)bb.field_72339_c), new Vector3f((float)bb.field_72336_d, (float)bb.field_72337_e, (float)bb.field_72339_c), new Vector3f((float)bb.field_72340_a, (float)bb.field_72338_b, (float)bb.field_72334_f), new Vector3f((float)bb.field_72340_a, (float)bb.field_72337_e, (float)bb.field_72334_f), new Vector3f((float)bb.field_72336_d, (float)bb.field_72338_b, (float)bb.field_72334_f), new Vector3f((float)bb.field_72336_d, (float)bb.field_72337_e, (float)bb.field_72334_f));
        Vector4f entityPos = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
        ScaledResolution sr = new ScaledResolution(Stencil.mc);
        for (Vector3f vector3f : vectors) {
            vector3f = ESPUtil.projectOn2D(vector3f.x, vector3f.y, vector3f.z, sr.func_78325_e());
            if (vector3f == null || !((double)vector3f.z >= 0.0) || !((double)vector3f.z < 1.0)) continue;
            entityPos.x = Math.min(vector3f.x, entityPos.x);
            entityPos.y = Math.min(vector3f.y, entityPos.y);
            entityPos.z = Math.max(vector3f.x, entityPos.z);
            entityPos.w = Math.max(vector3f.y, entityPos.w);
        }
        return entityPos;
    }
}

