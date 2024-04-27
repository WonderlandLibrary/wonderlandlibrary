package me.aquavit.liquidsense.injection.forge.mixins.block;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.events.BlockRenderSideEvent;
import me.aquavit.liquidsense.module.modules.blatant.Criticals;
import me.aquavit.liquidsense.module.modules.exploit.GhostHand;
import me.aquavit.liquidsense.module.modules.render.CaveFinder;
import me.aquavit.liquidsense.event.events.BlockBBEvent;
import me.aquavit.liquidsense.module.modules.player.NoFall;
import me.aquavit.liquidsense.module.modules.render.XRay;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.minecraft.block.Block.getIdFromBlock;

@Mixin(Block.class)
@SideOnly(Side.CLIENT)
public abstract class MixinBlock {

    @Shadow
    public abstract AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state);
    @Shadow
    protected double minX;
    @Shadow
    protected double minY;
    @Shadow
    protected double minZ;
    @Shadow
    protected double maxX;
    @Shadow
    protected double maxY;
    @Shadow
    protected double maxZ;

    @Shadow
    @Final
    protected BlockState blockState;

    @Shadow
    public abstract void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ);


	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);
        BlockBBEvent blockBBEvent = new BlockBBEvent(pos, blockState.getBlock(), axisalignedbb);
        LiquidSense.eventManager.callEvent(blockBBEvent);
        axisalignedbb = blockBBEvent.getBoundingBox();
        if(axisalignedbb != null && mask.intersectsWith(axisalignedbb))
            list.add(axisalignedbb);

    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private void shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final XRay xray = (XRay) LiquidSense.moduleManager.getModule(XRay.class);
        final CaveFinder cavefinder = (CaveFinder) LiquidSense.moduleManager.getModule(CaveFinder.class);
        LiquidSense.eventManager.callEvent(new BlockRenderSideEvent(worldIn, pos, side, maxX, minX, maxY, minY, maxZ, minZ));

        if (cavefinder.getState() && !CaveFinder.caveFinder.get()) {
            callbackInfoReturnable.setReturnValue(cavefinder.xrayBlocks.contains(this));
        }

        if(xray.getState()) {
            callbackInfoReturnable.setReturnValue(xray.xrayBlocks.contains(this));
        }

    }

    @Inject(method = "isCollidable", at = @At("HEAD"), cancellable = true)
    private void isCollidable(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (LiquidSense.moduleManager.getModule(GhostHand.class).getState() && !(GhostHand.blockValue.get() == getIdFromBlock((Block) (Object) this)))
            callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method = "getAmbientOcclusionLightValue", at = @At("HEAD"), cancellable = true)
    private void getAmbientOcclusionLightValue(final CallbackInfoReturnable<Float> floatCallbackInfoReturnable) {
        if (LiquidSense.moduleManager.getModule(XRay.class).getState())
            floatCallbackInfoReturnable.setReturnValue(1F);
    }

    @Inject(method = "getPlayerRelativeBlockHardness", at = @At("RETURN"), cancellable = true)
    public void modifyBreakSpeed(EntityPlayer playerIn, World worldIn, BlockPos pos, final CallbackInfoReturnable<Float> callbackInfo) {
        float f = callbackInfo.getReturnValue();

        // NoSlowBreak
        if (playerIn.onGround) { // NoGround
            final NoFall noFall = (NoFall) LiquidSense.moduleManager.getModule(NoFall.class);
            final Criticals criticals = (Criticals) LiquidSense.moduleManager.getModule(Criticals.class);

            if (noFall.getState() && noFall.modeValue.get().equals("NoGround") || criticals.getState() && criticals.mode.get().equals("NoGround")) {
                f /= 5F;
            }
        }

        callbackInfo.setReturnValue(f);
    }
}