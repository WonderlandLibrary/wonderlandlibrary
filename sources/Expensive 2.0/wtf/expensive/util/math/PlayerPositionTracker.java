package wtf.expensive.util.math;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import org.joml.Vector4d;
import org.lwjgl.opengl.GL11;
import wtf.expensive.util.IMinecraft;
import wtf.expensive.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static baritone.utils.IRenderer.renderManager;
import static net.minecraft.client.renderer.WorldRenderer.frustum;

/**
 * @author dedinside
 * @since 01.07.2023
 */
public class PlayerPositionTracker implements IMinecraft {
    private final static IntBuffer viewport;
    private final static FloatBuffer modelview;
    private final static FloatBuffer projection;
    private final static FloatBuffer vector;

    static {
        viewport = GLAllocation.createDirectIntBuffer(16);
        modelview = GLAllocation.createDirectFloatBuffer(16);
        projection = GLAllocation.createDirectFloatBuffer(16);
        vector = GLAllocation.createDirectFloatBuffer(4);
    }


    public static boolean isInView(Entity ent) {
        assert mc.getRenderViewEntity() != null;
        frustum.setCameraPosition(mc.getRenderManager().info.getProjectedView().x, mc.getRenderManager().info.getProjectedView().y,mc.getRenderManager().info.getProjectedView().z);
        return frustum.isBoundingBoxInFrustum(ent.getBoundingBox()) || ent.ignoreFrustumCheck;
    }

    public static boolean isInView(Vector3d ent) {
        assert mc.getRenderViewEntity() != null;
        frustum.setCameraPosition(mc.getRenderManager().info.getProjectedView().x, mc.getRenderManager().info.getProjectedView().y,mc.getRenderManager().info.getProjectedView().z);
        return frustum.isBoundingBoxInFrustum(new AxisAlignedBB(ent.add(-0.5,-0.5, -0.5), ent.add(0.5,0.5, 0.5)));
    }

    public static Vector4d updatePlayerPositions(Entity player, float partialTicks) {
        Vector3d projection = mc.getRenderManager().info.getProjectedView();
        double x = MathUtil.interpolate(player.getPosX(), player.lastTickPosX, partialTicks);
        double y = MathUtil.interpolate(player.getPosY(), player.lastTickPosY, partialTicks);
        double z = MathUtil.interpolate(player.getPosZ(), player.lastTickPosZ, partialTicks);

        Vector3d size = new Vector3d(
                player.getBoundingBox().maxX - player.getBoundingBox().minX,
                player.getBoundingBox().maxY - player.getBoundingBox().minY,
                player.getBoundingBox().maxZ - player.getBoundingBox().minZ
        );

        AxisAlignedBB aabb = new AxisAlignedBB(x - size.x / 2f, y, z - size.z / 2f, x + size.x / 2f, y +size.y, z + size.z /2f);

        Vector4d position = null;

        for (int i = 0; i < 8; i++) {
            Vector3d vector = new Vector3d(
                    i % 2 == 0 ? aabb.minX : aabb.maxX,
                    (i / 2) % 2 == 0 ? aabb.minY : aabb.maxY,
                    (i / 4) % 2 == 0 ? aabb.minZ : aabb.maxZ);

            vector = project2D(vector.x - projection.x, vector.y - projection.y, vector.z - projection.z);

            if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                if (position == null) {
                    position = new Vector4d(vector.x, vector.y, vector.z, 1.0f);
                } else {
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
            }
        }

        return position;
    }



    private static final Vector2f entityPos = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);

    public static Vector2f getPositionsOn2D(Vector3d pos) {
        float x = (float) (pos.x - renderManager.renderPosX());
        float y = (float) (pos.y - renderManager.renderPosY());
        float z = (float) (pos.z - renderManager.renderPosZ());


        Vector3d vector3f = project2D(x, y, z);
        if (vector3f.z >= 0.0 && vector3f.z < 1.0) {
            return new Vector2f((float) vector3f.x, (float) vector3f.y);
        }

        return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    private static Vector3d project2D(final double x,
                                      final double y,
                                      final double z) {
        GL11.glGetFloatv(2982, modelview);
        GL11.glGetFloatv(2983, projection);
        GL11.glGetIntegerv(2978, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / 2, (mc.getMainWindow().getHeight() - vector.get(1)) / 2, vector.get(2));
        }
        return null;
    }

}
