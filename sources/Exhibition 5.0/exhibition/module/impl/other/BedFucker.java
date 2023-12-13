// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import org.lwjgl.opengl.GL11;
import exhibition.util.render.Colors;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.entity.RenderManager;
import exhibition.event.impl.EventRender3D;
import exhibition.event.RegisterEvent;
import net.minecraft.util.EnumFacing;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import exhibition.module.Module;

public class BedFucker extends Module
{
    private Block nukeBlock;
    private BlockPos blockBreaking;
    Timer timer;
    
    public BedFucker(final ModuleData data) {
        super(data);
        this.timer = new Timer();
    }
    
    @RegisterEvent(events = { EventMotion.class, EventRender3D.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                for (int y = 6; y >= -6; --y) {
                    for (int x = -6; x <= 6; ++x) {
                        for (int z = -6; z <= 6; ++z) {
                            boolean uwot = x != 0 || z != 0;
                            if (BedFucker.mc.thePlayer.isSneaking()) {
                                uwot = !uwot;
                            }
                            if (uwot) {
                                final BlockPos pos = new BlockPos(BedFucker.mc.thePlayer.posX + x, BedFucker.mc.thePlayer.posY + y, BedFucker.mc.thePlayer.posZ + z);
                                if (this.getFacingDirection(pos) != null && this.blockChecks(BedFucker.mc.theWorld.getBlockState(pos).getBlock()) && BedFucker.mc.thePlayer.getDistance(BedFucker.mc.thePlayer.posX + x, BedFucker.mc.thePlayer.posY + y, BedFucker.mc.thePlayer.posZ + z) < BedFucker.mc.playerController.getBlockReachDistance() - 0.5) {
                                    final float[] rotations = this.getBlockRotations(BedFucker.mc.thePlayer.posX + x, BedFucker.mc.thePlayer.posY + y, BedFucker.mc.thePlayer.posZ + z);
                                    em.setYaw(rotations[0]);
                                    em.setPitch(rotations[1]);
                                    this.blockBreaking = pos;
                                    return;
                                }
                            }
                        }
                    }
                }
                this.blockBreaking = null;
            }
            else if (this.blockBreaking != null) {
                if (BedFucker.mc.playerController.blockHitDelay > 1) {
                    BedFucker.mc.playerController.blockHitDelay = 1;
                }
                BedFucker.mc.thePlayer.swingItem();
                final EnumFacing direction = this.getFacingDirection(this.blockBreaking);
                if (direction != null) {
                    BedFucker.mc.playerController.breakBlock(this.blockBreaking, direction);
                }
            }
        }
        else if (this.blockBreaking != null) {
            this.drawESP(this.blockBreaking.getX(), this.blockBreaking.getY(), this.blockBreaking.getZ(), this.blockBreaking.getX() + 1, this.blockBreaking.getY() + 1, this.blockBreaking.getZ() + 1, 100.0, 100.0, 100.0);
        }
    }
    
    public void drawESP(final double x, final double y, final double z, final double x2, final double y2, final double z2, final double r, final double g, final double b) {
        final double x3 = x - RenderManager.renderPosX;
        final double y3 = y - RenderManager.renderPosY;
        final double z3 = z - RenderManager.renderPosZ;
        final double x4 = x2 - RenderManager.renderPosX;
        final double y4 = y2 - RenderManager.renderPosY;
        this.drawFilledBBESP(new AxisAlignedBB(x3, y3, z3, x4, y4, z2 - RenderManager.renderPosZ), Colors.getColor(0, 0, 50, 0));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawFilledBox(final AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        GL11.glVertex3d(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        GL11.glEnd();
    }
    
    public void drawFilledBBESP(final AxisAlignedBB axisalignedbb, final int color) {
        GL11.glPushMatrix();
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        drawFilledBox(axisalignedbb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    private boolean blockChecks(final Block block) {
        return block == Blocks.bed;
    }
    
    public float[] getBlockRotations(final double x, final double y, final double z) {
        final double var4 = x - BedFucker.mc.thePlayer.posX + 0.5;
        final double var5 = z - BedFucker.mc.thePlayer.posZ + 0.5;
        final double var6 = y - (BedFucker.mc.thePlayer.posY + BedFucker.mc.thePlayer.getEyeHeight() - 1.0);
        final double var7 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var8 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[] { var8, (float)(-(Math.atan2(var6, var7) * 180.0 / 3.141592653589793)) };
    }
    
    private EnumFacing getFacingDirection(final BlockPos pos) {
        EnumFacing direction = null;
        if (!BedFucker.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.UP;
        }
        else if (!BedFucker.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.DOWN;
        }
        else if (!BedFucker.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.EAST;
        }
        else if (!BedFucker.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.WEST;
        }
        else if (!BedFucker.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.SOUTH;
        }
        else if (!BedFucker.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.NORTH;
        }
        final MovingObjectPosition rayResult = BedFucker.mc.theWorld.rayTraceBlocks(new Vec3(BedFucker.mc.thePlayer.posX, BedFucker.mc.thePlayer.posY + BedFucker.mc.thePlayer.getEyeHeight(), BedFucker.mc.thePlayer.posZ), new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
        if (rayResult != null && rayResult.getBlockPos() == pos) {
            return rayResult.facing;
        }
        return direction;
    }
}
