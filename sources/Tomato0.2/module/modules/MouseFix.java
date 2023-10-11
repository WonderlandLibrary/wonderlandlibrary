/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.Event3D;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.R3DUtils;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

public class MouseFix
extends Module {
    private boolean canTP;
    private int delay;
    private BlockPos endPos;

    public MouseFix() {
        super("MouseFix", 0, Category.EXPLOITS, 10066329, false, "Better TTF Chat", new String[]{"tp"});
    }

    @EventTarget
    public void onPreUpdate(EventPreMotionUpdates event) {
        if (this.delay > 0) {
            --this.delay;
        }
    }

    @EventTarget
    public void onRender(Event3D event) {
        boolean blockAbove;
        int x2 = Wrapper.mc.objectMouseOver.func_178782_a().getX();
        int y2 = Wrapper.mc.objectMouseOver.func_178782_a().getY();
        int z2 = Wrapper.mc.objectMouseOver.func_178782_a().getZ();
        Block block1 = Wrapper.getBlockAtPos(new BlockPos(x2, y2, z2));
        Block block2 = Wrapper.getBlockAtPos(new BlockPos((double)x2, (double)y2 + 1.0, (double)z2));
        Block block3 = Wrapper.getBlockAtPos(new BlockPos((double)x2, (double)y2 + 2.0, (double)z2));
        boolean blockBelow = !(block1 instanceof BlockSign) && block1.getMaterial().isSolid();
        boolean blockLevel = !(block2 instanceof BlockSign) && block1.getMaterial().isSolid();
        boolean bl2 = blockAbove = !(block3 instanceof BlockSign) && block1.getMaterial().isSolid();
        if (Wrapper.getBlockAtPos(Wrapper.mc.objectMouseOver.func_178782_a()).getMaterial() != Material.air && blockBelow && blockLevel && blockAbove) {
            this.canTP = true;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            R3DUtils.drawOutlinedBoundingBox(new AxisAlignedBB((double)x2 - RenderManager.renderPosX, (double)y2 - RenderManager.renderPosY, (double)z2 - RenderManager.renderPosZ, (double)x2 - RenderManager.renderPosX + 1.0, (double)(y2 + 1) - RenderManager.renderPosY, (double)z2 - RenderManager.renderPosZ + 1.0));
            GL11.glPopMatrix();
        } else {
            this.canTP = false;
        }
    }
}

