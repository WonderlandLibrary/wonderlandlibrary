// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart
{
    public EntityMinecartEmpty(final World worldIn) {
        super(worldIn);
    }
    
    public EntityMinecartEmpty(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public static void registerFixesMinecartEmpty(final DataFixer fixer) {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartEmpty.class);
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        if (player.isSneaking()) {
            return false;
        }
        if (this.isBeingRidden()) {
            return true;
        }
        if (!this.world.isRemote) {
            player.startRiding(this);
        }
        return true;
    }
    
    @Override
    public void onActivatorRailPass(final int x, final int y, final int z, final boolean receivingPower) {
        if (receivingPower) {
            if (this.isBeingRidden()) {
                this.removePassengers();
            }
            if (this.getRollingAmplitude() == 0) {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setDamage(50.0f);
                this.markVelocityChanged();
            }
        }
    }
    
    @Override
    public Type getType() {
        return Type.RIDEABLE;
    }
}
