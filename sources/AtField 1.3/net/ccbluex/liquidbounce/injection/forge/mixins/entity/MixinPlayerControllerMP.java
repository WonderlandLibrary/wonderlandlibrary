/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.ClickWindowEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.exploit.AbortBreaking;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoSlow;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.injection.backend.EntityImplKt;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt;
import net.ccbluex.liquidbounce.utils.render.BlockAnimationUtils;
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
    private NetHandlerPlayClient field_78774_b;
    @Shadow
    private GameType field_78779_k = GameType.SURVIVAL;

    @Overwrite
    public EnumActionResult func_187101_a(EntityPlayer entityPlayer, World world, EnumHand enumHand) {
        KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
        Animations animations = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        Animations animations2 = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        NoSlow noSlow = (NoSlow)LiquidBounce.moduleManager.getModule(NoSlow.class);
        ItemStack itemStack = entityPlayer.func_184586_b(enumHand);
        ItemStack itemStack2 = new ItemStack(Items.field_185159_cQ);
        if (this.field_78779_k == GameType.SPECTATOR) {
            return EnumActionResult.PASS;
        }
        this.func_78750_j();
        if (animations2.getState() && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword) {
            if (!(this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() != null && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (killAura.getTarget() != null && killAura.getBlockingStatus() || this.field_78776_a.field_71474_y.field_74313_G.field_74513_e) && animations.getState())) {
                this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
            }
        } else if (!(this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() != null && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (killAura.getTarget() != null && killAura.getBlockingStatus() || this.field_78776_a.field_71474_y.field_74313_G.field_74513_e) && animations.getState())) {
            this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItem(enumHand));
        }
        BlockAnimationUtils.thePlayerisBlocking = true;
        if (entityPlayer.func_184811_cZ().func_185141_a(itemStack.func_77973_b())) {
            return EnumActionResult.PASS;
        }
        EnumActionResult enumActionResult = ForgeHooks.onItemRightClick((EntityPlayer)entityPlayer, (EnumHand)enumHand);
        if (enumActionResult != null) {
            return enumActionResult;
        }
        int n = itemStack.func_190916_E();
        ActionResult actionResult = itemStack.func_77957_a(world, entityPlayer, enumHand);
        ItemStack itemStack3 = (ItemStack)actionResult.func_188398_b();
        if (itemStack3 != itemStack || itemStack3.func_190916_E() != n) {
            entityPlayer.func_184611_a(enumHand, itemStack3);
            if (itemStack3.func_190926_b()) {
                ForgeEventFactory.onPlayerDestroyItem((EntityPlayer)entityPlayer, (ItemStack)itemStack, (EnumHand)enumHand);
            }
        }
        return actionResult.func_188397_a();
    }

    @Inject(method={"attackEntity"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/multiplayer/PlayerControllerMP;syncCurrentPlayItem()V")})
    private void attackEntity(EntityPlayer entityPlayer, Entity entity, CallbackInfo callbackInfo) {
        LiquidBounce.eventManager.callEvent(new AttackEvent(EntityImplKt.wrap(entity)));
    }

    @Overwrite
    public void func_78766_c(EntityPlayer entityPlayer) {
        NoSlow noSlow = (NoSlow)LiquidBounce.moduleManager.getModule(NoSlow.class);
        BlockAnimationUtils.thePlayerisBlocking = false;
        this.func_78750_j();
        this.field_78774_b.func_147297_a((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
        entityPlayer.func_184597_cx();
    }

    @Shadow
    public abstract float func_78757_d();

    @Inject(method={"getIsHittingBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void getIsHittingBlock(CallbackInfoReturnable callbackInfoReturnable) {
        if (LiquidBounce.moduleManager.getModule(AbortBreaking.class).getState()) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Shadow
    public abstract void func_78750_j();

    @Inject(method={"windowClick"}, at={@At(value="HEAD")}, cancellable=true)
    private void windowClick(int n, int n2, int n3, ClickType clickType, EntityPlayer entityPlayer, CallbackInfoReturnable callbackInfoReturnable) {
        ClickWindowEvent clickWindowEvent = new ClickWindowEvent(n, n2, n3, BackendExtentionsKt.toInt(clickType));
        LiquidBounce.eventManager.callEvent(clickWindowEvent);
        if (clickWindowEvent.isCancelled()) {
            callbackInfoReturnable.cancel();
        }
    }

    @Overwrite
    public EnumActionResult func_187099_a(EntityPlayerSP entityPlayerSP, WorldClient worldClient, BlockPos blockPos, EnumFacing enumFacing, Vec3d vec3d, EnumHand enumHand) {
        EnumActionResult enumActionResult;
        KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
        Animations animations = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        this.func_78750_j();
        ItemStack itemStack = entityPlayerSP.func_184586_b(enumHand);
        float f = (float)(vec3d.field_72450_a - (double)blockPos.func_177958_n());
        float f2 = (float)(vec3d.field_72448_b - (double)blockPos.func_177956_o());
        float f3 = (float)(vec3d.field_72449_c - (double)blockPos.func_177952_p());
        boolean bl = false;
        if (!this.field_78776_a.field_71441_e.func_175723_af().func_177746_a(blockPos)) {
            return EnumActionResult.FAIL;
        }
        PlayerInteractEvent.RightClickBlock rightClickBlock = ForgeHooks.onRightClickBlock((EntityPlayer)entityPlayerSP, (EnumHand)enumHand, (BlockPos)blockPos, (EnumFacing)enumFacing, (Vec3d)ForgeHooks.rayTraceEyeHitVec((EntityLivingBase)entityPlayerSP, (double)(this.func_78757_d() + 1.0f)));
        if (rightClickBlock.isCanceled()) {
            if (!(this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() != null && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (killAura.getTarget() != null && killAura.getBlockingStatus() || this.field_78776_a.field_71474_y.field_74313_G.field_74513_e) && animations.getState() && blockPos == false)) {
                this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, f, f2, f3));
            }
            return rightClickBlock.getCancellationResult();
        }
        EnumActionResult enumActionResult2 = EnumActionResult.PASS;
        if (this.field_78779_k != GameType.SPECTATOR) {
            ItemBlock itemBlock;
            boolean bl2;
            enumActionResult = itemStack.onItemUseFirst((EntityPlayer)entityPlayerSP, (World)worldClient, blockPos, enumHand, enumFacing, f, f2, f3);
            if (enumActionResult != EnumActionResult.PASS) {
                if (!(this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() != null && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (killAura.getTarget() != null && killAura.getBlockingStatus() || this.field_78776_a.field_71474_y.field_74313_G.field_74513_e) && animations.getState() && blockPos == false)) {
                    this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, f, f2, f3));
                }
                return enumActionResult;
            }
            IBlockState iBlockState = worldClient.func_180495_p(blockPos);
            boolean bl3 = bl2 = entityPlayerSP.func_184614_ca().doesSneakBypassUse((IBlockAccess)worldClient, blockPos, (EntityPlayer)entityPlayerSP) && entityPlayerSP.func_184592_cb().doesSneakBypassUse((IBlockAccess)worldClient, blockPos, (EntityPlayer)entityPlayerSP);
            if (!entityPlayerSP.func_70093_af() || bl2 || rightClickBlock.getUseBlock() == Event.Result.ALLOW) {
                if (rightClickBlock.getUseBlock() != Event.Result.DENY) {
                    bl = iBlockState.func_177230_c().func_180639_a((World)worldClient, blockPos, iBlockState, (EntityPlayer)entityPlayerSP, enumHand, enumFacing, f, f2, f3);
                }
                if (bl) {
                    enumActionResult2 = EnumActionResult.SUCCESS;
                }
            }
            if (!bl && itemStack.func_77973_b() instanceof ItemBlock && !(itemBlock = (ItemBlock)itemStack.func_77973_b()).func_179222_a((World)worldClient, blockPos, enumFacing, (EntityPlayer)entityPlayerSP, itemStack)) {
                return EnumActionResult.FAIL;
            }
        }
        if (!(this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() != null && this.field_78776_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (killAura.getTarget() != null && killAura.getBlockingStatus() || this.field_78776_a.field_71474_y.field_74313_G.field_74513_e) && animations.getState() && blockPos == false)) {
            this.field_78774_b.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, f, f2, f3));
        }
        if ((bl || this.field_78779_k == GameType.SPECTATOR) && rightClickBlock.getUseItem() != Event.Result.ALLOW) {
            return EnumActionResult.SUCCESS;
        }
        if (itemStack.func_190926_b()) {
            return EnumActionResult.PASS;
        }
        if (entityPlayerSP.func_184811_cZ().func_185141_a(itemStack.func_77973_b())) {
            return EnumActionResult.PASS;
        }
        if (itemStack.func_77973_b() instanceof ItemBlock && !entityPlayerSP.func_189808_dh() && ((enumActionResult = ((ItemBlock)itemStack.func_77973_b()).func_179223_d()) instanceof BlockCommandBlock || enumActionResult instanceof BlockStructure)) {
            return EnumActionResult.FAIL;
        }
        if (this.field_78779_k.func_77145_d()) {
            int n = itemStack.func_77960_j();
            int n2 = itemStack.func_190916_E();
            if (rightClickBlock.getUseItem() != Event.Result.DENY) {
                EnumActionResult enumActionResult3 = itemStack.func_179546_a((EntityPlayer)entityPlayerSP, (World)worldClient, blockPos, enumHand, enumFacing, f, f2, f3);
                itemStack.func_77964_b(n);
                itemStack.func_190920_e(n2);
                return enumActionResult3;
            }
            return enumActionResult2;
        }
        enumActionResult = itemStack.func_77946_l();
        if (rightClickBlock.getUseItem() != Event.Result.DENY) {
            enumActionResult2 = itemStack.func_179546_a((EntityPlayer)entityPlayerSP, (World)worldClient, blockPos, enumHand, enumFacing, f, f2, f3);
        }
        if (itemStack.func_190926_b()) {
            ForgeEventFactory.onPlayerDestroyItem((EntityPlayer)entityPlayerSP, (ItemStack)enumActionResult, (EnumHand)enumHand);
        }
        return enumActionResult2;
    }
}

