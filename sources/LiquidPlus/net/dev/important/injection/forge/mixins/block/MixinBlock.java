/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.BlockState
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.injection.forge.mixins.block;

import java.util.List;
import net.dev.important.Client;
import net.dev.important.event.BlockBBEvent;
import net.dev.important.modules.module.modules.combat.Criticals;
import net.dev.important.modules.module.modules.exploit.GhostHand;
import net.dev.important.modules.module.modules.player.NoFall;
import net.dev.important.modules.module.modules.render.XRay;
import net.dev.important.modules.module.modules.world.NoSlowBreak;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
    protected BlockState field_176227_L;

    @Shadow
    public abstract AxisAlignedBB func_180640_a(World var1, BlockPos var2, IBlockState var3);

    @Shadow
    public abstract void func_149676_a(float var1, float var2, float var3, float var4, float var5, float var6);

    @Shadow
    public IBlockState func_180642_a(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return null;
    }

    @Overwrite
    public void func_180638_a(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        AxisAlignedBB axisalignedbb = this.func_180640_a(worldIn, pos, state);
        BlockBBEvent blockBBEvent = new BlockBBEvent(pos, this.field_176227_L.func_177622_c(), axisalignedbb);
        Client.eventManager.callEvent(blockBBEvent);
        axisalignedbb = blockBBEvent.getBoundingBox();
        if (axisalignedbb != null && mask.func_72326_a(axisalignedbb)) {
            list.add(axisalignedbb);
        }
    }

    @Inject(method={"shouldSideBeRendered"}, at={@At(value="HEAD")}, cancellable=true)
    private void shouldSideBeRendered(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        XRay xray = (XRay)Client.moduleManager.getModule(XRay.class);
        if (xray.getState()) {
            callbackInfoReturnable.setReturnValue(xray.getXrayBlocks().contains(this));
        }
    }

    @Inject(method={"isCollidable"}, at={@At(value="HEAD")}, cancellable=true)
    private void isCollidable(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        GhostHand ghostHand = (GhostHand)Client.moduleManager.getModule(GhostHand.class);
        if (ghostHand.getState() && (Integer)ghostHand.getBlockValue().get() != Block.func_149682_b((Block)((Block)this))) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"getAmbientOcclusionLightValue"}, at={@At(value="HEAD")}, cancellable=true)
    private void getAmbientOcclusionLightValue(CallbackInfoReturnable<Float> floatCallbackInfoReturnable) {
        if (Client.moduleManager.getModule(XRay.class).getState()) {
            floatCallbackInfoReturnable.setReturnValue(Float.valueOf(1.0f));
        }
    }

    @Inject(method={"getPlayerRelativeBlockHardness"}, at={@At(value="RETURN")}, cancellable=true)
    public void modifyBreakSpeed(EntityPlayer playerIn, World worldIn, BlockPos pos, CallbackInfoReturnable<Float> callbackInfo) {
        float f = callbackInfo.getReturnValue().floatValue();
        NoSlowBreak noSlowBreak = (NoSlowBreak)Client.moduleManager.getModule(NoSlowBreak.class);
        if (noSlowBreak.getState()) {
            if (((Boolean)noSlowBreak.getWaterValue().get()).booleanValue() && playerIn.func_70055_a(Material.field_151586_h) && !EnchantmentHelper.func_77510_g((EntityLivingBase)playerIn)) {
                f *= 5.0f;
            }
            if (((Boolean)noSlowBreak.getAirValue().get()).booleanValue() && !playerIn.field_70122_E) {
                f *= 5.0f;
            }
        } else if (playerIn.field_70122_E) {
            NoFall noFall = (NoFall)Client.moduleManager.getModule(NoFall.class);
            Criticals criticals = (Criticals)Client.moduleManager.getModule(Criticals.class);
            if (noFall.getState() && ((String)noFall.modeValue.get()).equalsIgnoreCase("NoGround") || criticals.getState() && ((String)criticals.getModeValue().get()).equalsIgnoreCase("NoGround")) {
                f /= 5.0f;
            }
        }
        callbackInfo.setReturnValue(Float.valueOf(f));
    }
}

