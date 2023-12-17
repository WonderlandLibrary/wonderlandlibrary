// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.override;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.PlayerControllerMP;

public class PlayerControllerOF extends PlayerControllerMP
{
    private boolean acting;
    private BlockPos lastClickBlockPos;
    private Entity lastClickEntity;
    
    public PlayerControllerOF(final Minecraft mcIn, final NetHandlerPlayClient netHandler) {
        super(mcIn, netHandler);
        this.acting = false;
        this.lastClickBlockPos = null;
        this.lastClickEntity = null;
    }
    
    @Override
    public boolean clickBlock(final BlockPos loc, final EnumFacing face) {
        this.acting = true;
        this.lastClickBlockPos = loc;
        final boolean flag = super.clickBlock(loc, face);
        this.acting = false;
        return flag;
    }
    
    @Override
    public boolean onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing) {
        this.acting = true;
        this.lastClickBlockPos = posBlock;
        final boolean flag = super.onPlayerDamageBlock(posBlock, directionFacing);
        this.acting = false;
        return flag;
    }
    
    @Override
    public EnumActionResult processRightClick(final EntityPlayer player, final World worldIn, final EnumHand hand) {
        this.acting = true;
        final EnumActionResult enumactionresult = super.processRightClick(player, worldIn, hand);
        this.acting = false;
        return enumactionresult;
    }
    
    @Override
    public EnumActionResult processRightClickBlock(final EntityPlayerSP player, final WorldClient worldIn, final BlockPos pos, final EnumFacing facing, final Vec3d vec, final EnumHand hand) {
        this.acting = true;
        this.lastClickBlockPos = pos;
        final EnumActionResult enumactionresult = super.processRightClickBlock(player, worldIn, pos, facing, vec, hand);
        this.acting = false;
        return enumactionresult;
    }
    
    @Override
    public EnumActionResult interactWithEntity(final EntityPlayer player, final Entity target, final EnumHand hand) {
        this.lastClickEntity = target;
        return super.interactWithEntity(player, target, hand);
    }
    
    @Override
    public EnumActionResult interactWithEntity(final EntityPlayer player, final Entity target, final RayTraceResult ray, final EnumHand hand) {
        this.lastClickEntity = target;
        return super.interactWithEntity(player, target, ray, hand);
    }
    
    public boolean isActing() {
        return this.acting;
    }
    
    public BlockPos getLastClickBlockPos() {
        return this.lastClickBlockPos;
    }
    
    public Entity getLastClickEntity() {
        return this.lastClickEntity;
    }
}
