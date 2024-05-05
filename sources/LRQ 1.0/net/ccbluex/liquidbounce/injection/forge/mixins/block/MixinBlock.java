/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.BlockStateContainer
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.block;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.BlockBBEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.exploit.GhostHand;
import net.ccbluex.liquidbounce.features.module.modules.player.NoFall;
import net.ccbluex.liquidbounce.features.module.modules.render.XRay;
import net.ccbluex.liquidbounce.features.module.modules.world.NoSlowBreak;
import net.ccbluex.liquidbounce.injection.backend.AxisAlignedBBImplKt;
import net.ccbluex.liquidbounce.injection.backend.BlockImplKt;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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

@SideOnly(value=Side.CLIENT)
@Mixin(value={Block.class})
public abstract class MixinBlock {
    @Shadow
    @Final
    protected BlockStateContainer field_176227_L;

    @Overwrite
    protected static void func_185492_a(BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable AxisAlignedBB blockBox) {
        if (blockBox != null) {
            AxisAlignedBB axisalignedbb = blockBox.func_186670_a(pos);
            WorldClient world = Minecraft.func_71410_x().field_71441_e;
            if (world != null) {
                BlockBBEvent blockBBEvent = new BlockBBEvent(BackendExtentionsKt.wrap(pos), BlockImplKt.wrap(world.func_180495_p(pos).func_177230_c()), AxisAlignedBBImplKt.wrap(axisalignedbb));
                LiquidBounce.eventManager.callEvent(blockBBEvent);
                AxisAlignedBB axisAlignedBB = axisalignedbb = blockBBEvent.getBoundingBox() == null ? null : AxisAlignedBBImplKt.unwrap(blockBBEvent.getBoundingBox());
            }
            if (axisalignedbb != null && entityBox.func_72326_a(axisalignedbb)) {
                collidingBoxes.add(axisalignedbb);
            }
        }
    }

    @Shadow
    public abstract AxisAlignedBB func_180646_a(IBlockState var1, IBlockAccess var2, BlockPos var3);

    @Shadow
    public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return null;
    }

    @Inject(method={"shouldSideBeRendered"}, at={@At(value="HEAD")}, cancellable=true)
    private void shouldSideBeRendered(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        XRay xray = (XRay)LiquidBounce.moduleManager.getModule(XRay.class);
        if (Objects.requireNonNull(xray).getState()) {
            callbackInfoReturnable.setReturnValue((Object)xray.getXrayBlocks().contains(BlockImplKt.wrap((Block)this)));
        }
    }

    @Inject(method={"isCollidable"}, at={@At(value="HEAD")}, cancellable=true)
    private void isCollidable(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        GhostHand ghostHand = (GhostHand)LiquidBounce.moduleManager.getModule(GhostHand.class);
        if (Objects.requireNonNull(ghostHand).getState() && (Integer)ghostHand.getBlockValue().get() != Block.func_149682_b((Block)((Block)this))) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Inject(method={"getAmbientOcclusionLightValue"}, at={@At(value="HEAD")}, cancellable=true)
    private void getAmbientOcclusionLightValue(CallbackInfoReturnable<Float> floatCallbackInfoReturnable) {
        if (Objects.requireNonNull(LiquidBounce.moduleManager.getModule(XRay.class)).getState()) {
            floatCallbackInfoReturnable.setReturnValue((Object)Float.valueOf(1.0f));
        }
    }

    @Inject(method={"getPlayerRelativeBlockHardness"}, at={@At(value="RETURN")}, cancellable=true)
    public void modifyBreakSpeed(IBlockState state, EntityPlayer playerIn, World worldIn, BlockPos pos, CallbackInfoReturnable<Float> callbackInfo) {
        float f = ((Float)callbackInfo.getReturnValue()).floatValue();
        NoSlowBreak noSlowBreak = (NoSlowBreak)LiquidBounce.moduleManager.getModule(NoSlowBreak.class);
        if (Objects.requireNonNull(noSlowBreak).getState()) {
            if (((Boolean)noSlowBreak.getWaterValue().get()).booleanValue() && playerIn.func_70055_a(Material.field_151586_h) && !EnchantmentHelper.func_185287_i((EntityLivingBase)playerIn)) {
                f *= 5.0f;
            }
            if (((Boolean)noSlowBreak.getAirValue().get()).booleanValue() && !playerIn.field_70122_E) {
                f *= 5.0f;
            }
        } else if (playerIn.field_70122_E) {
            NoFall noFall = (NoFall)LiquidBounce.moduleManager.getModule(NoFall.class);
            Criticals criticals = (Criticals)LiquidBounce.moduleManager.getModule(Criticals.class);
            if (Objects.requireNonNull(noFall).getState() && ((String)noFall.modeValue.get()).equalsIgnoreCase("NoGround") || Objects.requireNonNull(criticals).getState() && ((String)criticals.getModeValue().get()).equalsIgnoreCase("NoGround")) {
                f /= 5.0f;
            }
        }
        callbackInfo.setReturnValue((Object)Float.valueOf(f));
    }
}

