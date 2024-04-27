/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockCommandBlock
 *  net.minecraft.block.BlockStructure
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.GameType
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.common.ForgeHooks
 *  net.minecraftforge.event.ForgeEventFactory
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$RightClickBlock
 *  net.minecraftforge.fml.common.eventhandler.Event$Result
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import me.aquavit.liquidsense.utils.Debug;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.injection.backend.EntityImplKt;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={PlayerControllerMP.class})
public abstract class MixinPlayerControllerMP {
    @Shadow
    protected Minecraft field_78776_a;
    @Shadow
    private GameType field_78779_k = GameType.SURVIVAL;
    @Shadow
    private NetHandlerPlayClient field_78774_b;

    @Shadow
    public abstract void func_78750_j();

    @Shadow
    public abstract float func_78757_d();

    @Inject(method={"attackEntity"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V")})
    private void attackEntity(EntityPlayer entityPlayer, Entity targetEntity, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new AttackEvent(EntityImplKt.wrap(targetEntity)));
    }

    @Inject(method={"getIsHittingBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void getIsHittingBlock(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (LiquidBounce.moduleManager.getModule(AbortBreaking.class).getState()) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Inject(method={"windowClick"}, at={@At(value="HEAD")}, cancellable=true)
    private void windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player, CallbackInfoReturnable<ItemStack> callbackInfo) {
        ClickWindowEvent event = new ClickWindowEvent(windowId, slotId, mouseButton, BackendExtentionsKt.toInt(type));
        LiquidBounce.eventManager.callEvent(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Overwrite
    public EnumActionResult func_187099_a(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand) {
        Block block;
        this.func_78750_j();
        ItemStack itemstack = player.func_184586_b(hand);
        float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
        float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
        float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
        boolean flag = false;
        if (!this.field_78776_a.field_71441_e.func_175723_af().func_177746_a(pos)) {
            return EnumActionResult.FAIL;
        }
        PlayerInteractEvent.RightClickBlock event = ForgeHooks.onRightClickBlock((EntityPlayer)player, (EnumHand)hand, (BlockPos)pos, (EnumFacing)direction, (Vec3d)ForgeHooks.rayTraceEyeHitVec((EntityLivingBase)player, (double)(this.func_78757_d() + 1.0f)));
        if (event.isCanceled()) {
            this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
            return event.getCancellationResult();
        }
        EnumActionResult result = EnumActionResult.PASS;
        if (this.field_78779_k != GameType.SPECTATOR) {
            ItemBlock itemblock;
            boolean bypass;
            EnumActionResult ret = itemstack.onItemUseFirst((EntityPlayer)player, (World)worldIn, pos, hand, direction, f, f1, f2);
            if (ret != EnumActionResult.PASS) {
                this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
                return ret;
            }
            IBlockState iblockstate = worldIn.func_180495_p(pos);
            boolean bl = bypass = player.func_184614_ca().doesSneakBypassUse((IBlockAccess)worldIn, pos, (EntityPlayer)player) && player.func_184592_cb().doesSneakBypassUse((IBlockAccess)worldIn, pos, (EntityPlayer)player);
            if (!player.func_70093_af() || bypass || event.getUseBlock() == Event.Result.ALLOW) {
                if (event.getUseBlock() != Event.Result.DENY) {
                    flag = iblockstate.func_177230_c().func_180639_a((World)worldIn, pos, iblockstate, (EntityPlayer)player, hand, direction, f, f1, f2);
                }
                if (flag) {
                    result = EnumActionResult.SUCCESS;
                }
            }
            if (!flag && itemstack.func_77973_b() instanceof ItemBlock && !(itemblock = (ItemBlock)itemstack.func_77973_b()).func_179222_a((World)worldIn, pos, direction, (EntityPlayer)player, itemstack)) {
                return EnumActionResult.FAIL;
            }
        }
        this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        if ((flag || this.field_78779_k == GameType.SPECTATOR) && event.getUseItem() != Event.Result.ALLOW) {
            return EnumActionResult.SUCCESS;
        }
        if (itemstack.func_190926_b()) {
            return EnumActionResult.PASS;
        }
        if (player.func_184811_cZ().func_185141_a(itemstack.func_77973_b())) {
            return EnumActionResult.PASS;
        }
        if (itemstack.func_77973_b() instanceof ItemBlock && !player.func_189808_dh() && ((block = ((ItemBlock)itemstack.func_77973_b()).func_179223_d()) instanceof BlockCommandBlock || block instanceof BlockStructure)) {
            return EnumActionResult.FAIL;
        }
        if (this.field_78779_k.func_77145_d()) {
            int i = itemstack.func_77960_j();
            int j = itemstack.func_190916_E();
            if (event.getUseItem() != Event.Result.DENY) {
                EnumActionResult enumactionresult = itemstack.func_179546_a((EntityPlayer)player, (World)worldIn, pos, hand, direction, f, f1, f2);
                itemstack.func_77964_b(i);
                itemstack.func_190920_e(j);
                return enumactionresult;
            }
            return result;
        }
        ItemStack copyForUse = itemstack.func_77946_l();
        if (event.getUseItem() != Event.Result.DENY) {
            result = itemstack.func_179546_a((EntityPlayer)player, (World)worldIn, pos, hand, direction, f, f1, f2);
        }
        if (itemstack.func_190926_b()) {
            ForgeEventFactory.onPlayerDestroyItem((EntityPlayer)player, (ItemStack)copyForUse, (EnumHand)hand);
        }
        return result;
    }

    @Overwrite
    public EnumActionResult func_187101_a(EntityPlayer player, World worldIn, EnumHand hand) {
        OldHitting ot = (OldHitting)LiquidBounce.moduleManager.getModule(OldHitting.class);
        KillAura aura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
        NoSlow noSlow = (NoSlow)LiquidBounce.moduleManager.getModule(NoSlow.class);
        ItemStack itemstack = player.func_184586_b(hand);
        ItemStack shield = new ItemStack(Items.field_185159_cQ);
        if (noSlow.getState() && ((Boolean)noSlow.getValue().get()).booleanValue() && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword) {
            this.field_78776_a.field_71439_g.field_71071_by.field_184439_c.set(0, (Object)shield);
        }
        if (this.field_78779_k == GameType.SPECTATOR) {
            return EnumActionResult.PASS;
        }
        this.func_78750_j();
        if (ot.getState() && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword) {
            this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
        } else {
            this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItem(hand));
        }
        Debug.thePlayerisBlocking = true;
        if (player.func_184811_cZ().func_185141_a(itemstack.func_77973_b())) {
            return EnumActionResult.PASS;
        }
        EnumActionResult cancelResult = ForgeHooks.onItemRightClick((EntityPlayer)player, (EnumHand)hand);
        if (cancelResult != null) {
            return cancelResult;
        }
        int i = itemstack.func_190916_E();
        ActionResult actionresult = itemstack.func_77957_a(worldIn, player, hand);
        ItemStack itemstack1 = (ItemStack)actionresult.func_188398_b();
        if (itemstack1 != itemstack || itemstack1.func_190916_E() != i) {
            player.func_184611_a(hand, itemstack1);
            if (itemstack1.func_190926_b()) {
                ForgeEventFactory.onPlayerDestroyItem((EntityPlayer)player, (ItemStack)itemstack, (EnumHand)hand);
            }
        }
        return actionresult.func_188397_a();
    }

    @Overwrite
    public void func_78766_c(EntityPlayer playerIn) {
        NoSlow noSlow = (NoSlow)LiquidBounce.moduleManager.getModule(NoSlow.class);
        Debug.thePlayerisBlocking = false;
        this.func_78750_j();
        this.field_78774_b.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
        playerIn.func_184597_cx();
    }
}

