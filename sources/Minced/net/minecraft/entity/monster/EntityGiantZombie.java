// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityGiantZombie extends EntityMob
{
    public EntityGiantZombie(final World worldIn) {
        super(worldIn);
        this.setSize(this.width * 6.0f, this.height * 6.0f);
    }
    
    public static void registerFixesGiantZombie(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityGiantZombie.class);
    }
    
    @Override
    public float getEyeHeight() {
        return 10.440001f;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0);
    }
    
    @Override
    public float getBlockPathWeight(final BlockPos pos) {
        return this.world.getLightBrightness(pos) - 0.5f;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_GIANT;
    }
}
