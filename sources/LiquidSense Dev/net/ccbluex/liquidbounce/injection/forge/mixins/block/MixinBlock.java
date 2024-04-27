/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import me.AquaVit.liquidSense.modules.render.UHCXray;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.event.BlockRenderSideEvent;
import net.ccbluex.liquidbounce.event.UpdateModelEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GhostHand;
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
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
    @Final
    protected double minX;
    @Shadow
    @Final
    protected double minY;
    @Shadow
    @Final
    protected double minZ;
    @Shadow
    @Final
    protected double maxX;
    @Shadow
    @Final
    protected double maxY;
    @Shadow
    @Final
    protected double maxZ;

    @Shadow
    @Final
    protected BlockState blockState;

    @Shadow
    public abstract void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ);


    /**
     * @author CCBlueX
     */
    @Overwrite
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(worldIn, pos, state);
        BlockBBEvent blockBBEvent = new BlockBBEvent(pos, blockState.getBlock(), axisalignedbb);
        LiquidBounce.eventManager.callEvent(blockBBEvent);
        axisalignedbb = blockBBEvent.getBoundingBox();
        if(axisalignedbb != null && mask.intersectsWith(axisalignedbb))
            list.add(axisalignedbb);

    }
    /*
    @Overwrite
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side,CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final XRay xray = (XRay) LiquidBounce.moduleManager.getModule(XRay.class);
        final UHCXray XRay2 = (UHCXray) LiquidBounce.moduleManager.getModule(UHCXray.class);

        return side == EnumFacing.DOWN && this.minY > 0.0D ? true : (side == EnumFacing.UP && this.maxY < 1.0D ? true : (side == EnumFacing.NORTH && this.minZ > 0.0D ? true : (side == EnumFacing.SOUTH && this.maxZ < 1.0D ? true : (side == EnumFacing.WEST && this.minX > 0.0D ? true : (side == EnumFacing.EAST && this.maxX < 1.0D ? true : !worldIn.getBlockState(pos).getBlock().doesSideBlockRendering(worldIn, pos, side))))));
    }

     */
    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    private boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final XRay xray = (XRay) LiquidBounce.moduleManager.getModule(XRay.class);
        final UHCXray XRay2 = (UHCXray) LiquidBounce.moduleManager.getModule(UHCXray.class);

        LiquidBounce.eventManager.callEvent(new BlockRenderSideEvent(worldIn, pos, side, maxX, minX, maxY, minY, maxZ, minZ));

        if(xray.getState())
            callbackInfoReturnable.setReturnValue(xray.getXrayBlocks().contains(this));

        if (XRay2.getState()) {
            if (XRay2.CAVEFINDER.get()) {
                callbackInfoReturnable.setReturnValue(xray.getCavefinder().contains(this));
                if (XRay2.containsID(getIdFromBlock((Block)(Object)this))) {
                    return true;
                }
                return false;
            } else {
                if (XRay2.containsID(getIdFromBlock((Block)(Object)this))) {
                    return true;
                }
                return false;
            }
        } else {
            return (side == EnumFacing.DOWN && this.minY > 0.0) || (side == EnumFacing.UP && this.maxY < 1.0)
                    || (side == EnumFacing.NORTH && this.minZ > 0.0) || (side == EnumFacing.SOUTH && this.maxZ < 1.0)
                    || (side == EnumFacing.WEST && this.minX > 0.0) || (side == EnumFacing.EAST && this.maxX < 1.0)
                    || !worldIn.getBlockState(pos).getBlock().isOpaqueCube();
        }

    }

    @Inject(method = "isCollidable", at = @At("HEAD"), cancellable = true)
    private void isCollidable(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final GhostHand ghostHand = (GhostHand) LiquidBounce.moduleManager.getModule(GhostHand.class);

        if (ghostHand.getState() && !(ghostHand.getBlockValue().get() == getIdFromBlock((Block) (Object) this)))
            callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method = "getAmbientOcclusionLightValue", at = @At("HEAD"), cancellable = true)
    private void getAmbientOcclusionLightValue(final CallbackInfoReturnable<Float> floatCallbackInfoReturnable) {
        if (LiquidBounce.moduleManager.getModule(XRay.class).getState())
            floatCallbackInfoReturnable.setReturnValue(1F);
    }

    @Inject(method = "getPlayerRelativeBlockHardness", at = @At("RETURN"), cancellable = true)
    public void modifyBreakSpeed(EntityPlayer playerIn, World worldIn, BlockPos pos, final CallbackInfoReturnable<Float> callbackInfo) {
        float f = callbackInfo.getReturnValue();

        // NoSlowBreak
        if (playerIn.onGround) { // NoGround
            final NoFall noFall = (NoFall) LiquidBounce.moduleManager.getModule(NoFall.class);
            final Criticals criticals = (Criticals) LiquidBounce.moduleManager.getModule(Criticals.class);

            if (noFall.getState() && noFall.modeValue.get().equalsIgnoreCase("NoGround") ||
                    criticals.getState() && criticals.getModeValue().get().equalsIgnoreCase("NoGround")) {
                f /= 5F;
            }
        }

        callbackInfo.setReturnValue(f);
    }
}