// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.World;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import Lavish.event.events.EventCollide;
import Lavish.event.events.EventUpdate;
import Lavish.event.Event;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class Phase extends Module
{
    public int reset;
    
    public Phase() {
        super("Phase", 0, true, Category.Movement, "Phases through blocks");
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventUpdate) {
            --this.reset;
            double xOff = 0.0;
            double zOff = 0.0;
            final double multi = 2.6;
            final double mx = Math.cos(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
            final double mz = Math.sin(Math.toRadians(Phase.mc.thePlayer.rotationYaw + 90.0f));
            xOff = Phase.mc.thePlayer.moveForward * 1.6 * mx + Phase.mc.thePlayer.moveStrafing * 1.6 * mz;
            zOff = Phase.mc.thePlayer.moveForward * 1.6 * mz + Phase.mc.thePlayer.moveStrafing * 1.6 * mx;
            if (isInsideBlock()) {
                this.reset = 1;
            }
            if (this.reset > 0) {
                Phase.mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0.0, zOff);
            }
        }
        if (e instanceof EventCollide && ((isInsideBlock() && Phase.mc.gameSettings.keyBindJump.isKeyDown()) || (!isInsideBlock() && ((EventCollide)e).getBoundingBox() != null && ((EventCollide)e).getBoundingBox().maxY > Phase.mc.thePlayer.boundingBox.minY))) {
            ((EventCollide)e).setBoundingBox(null);
        }
    }
    
    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                    final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
