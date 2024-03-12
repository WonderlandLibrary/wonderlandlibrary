// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.override;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
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
    public boolean sendUseItem(final EntityPlayer player, final World worldIn, final ItemStack stack) {
        this.acting = true;
        final boolean flag = super.sendUseItem(player, worldIn, stack);
        this.acting = false;
        return flag;
    }
    
    @Override
    public boolean onPlayerRightClick(final EntityPlayerSP p_178890_1, final WorldClient p_178890_2, final ItemStack p_178890_3, final BlockPos p_178890_4, final EnumFacing p_178890_5, final Vec3 p_178890_6) {
        this.acting = true;
        this.lastClickBlockPos = p_178890_4;
        final boolean flag = super.onPlayerRightClick(p_178890_1, p_178890_2, p_178890_3, p_178890_4, p_178890_5, p_178890_6);
        this.acting = false;
        return flag;
    }
    
    @Override
    public boolean interactWithEntitySendPacket(final EntityPlayer player, final Entity target) {
        this.lastClickEntity = target;
        return super.interactWithEntitySendPacket(player, target);
    }
    
    @Override
    public boolean isPlayerRightClickingOnEntity(final EntityPlayer player, final Entity target, final MovingObjectPosition ray) {
        this.lastClickEntity = target;
        return super.isPlayerRightClickingOnEntity(player, target, ray);
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
