package Velo.impl.Modules.visuals;

import org.lwjgl.input.Keyboard;

import Velo.api.Module.Module;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventUpdate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;

public class BloodParticles extends Module {
	
	public static boolean isEnabled = false;
	
	public BloodParticles() {
		super("Blood Particles", "Blood Particles", Keyboard.KEY_NONE, Category.VISUALS);
	}
	
	public void onRender3DUpdate(EventRender3D event) {
        //int i = MathHelper.floor_double(this.posX);
        //int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
        //int k = MathHelper.floor_double(this.posZ);
        //BlockPos blockpos = new BlockPos(i, j, k);
        //IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        //Block block = iblockstate.getBlock();

        //if (block.getRenderType() != -1)
        //{
            //this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] {Block.getStateId(iblockstate)});
        //}
	}
	
	public void onDisable() {
		isEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
	}
}
