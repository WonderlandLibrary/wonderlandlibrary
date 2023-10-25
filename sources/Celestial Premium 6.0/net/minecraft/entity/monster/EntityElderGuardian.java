/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityElderGuardian
extends EntityGuardian {
    public EntityElderGuardian(World p_i47288_1_) {
        super(p_i47288_1_);
        this.setSize(this.width * 2.35f, this.height * 2.35f);
        this.enablePersistence();
        if (this.wander != null) {
            this.wander.setExecutionChance(400);
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0);
    }

    public static void func_190768_b(DataFixer p_190768_0_) {
        EntityLiving.registerFixesMob(p_190768_0_, EntityElderGuardian.class);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ELDER_GUARDIAN;
    }

    @Override
    public int getAttackDuration() {
        return 60;
    }

    public void func_190767_di() {
        this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation = 1.0f;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEvents.ENTITY_ELDERGUARDIAN_AMBIENTLAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_HURT : SoundEvents.ENTITY_ELDER_GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH : SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
    }

    @Override
    protected SoundEvent func_190765_dj() {
        return SoundEvents.field_191240_aK;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        int i = 1200;
        if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
            Potion potion = MobEffects.MINING_FATIGUE;
            List<EntityPlayerMP> list = this.world.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>(){

                @Override
                public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
                    return EntityElderGuardian.this.getDistanceSqToEntity(p_apply_1_) < 2500.0 && p_apply_1_.interactionManager.survivalOrAdventure();
                }
            });
            int j = 2;
            int k = 6000;
            int l = 1200;
            for (EntityPlayerMP entityplayermp : list) {
                if (entityplayermp.isPotionActive(potion) && entityplayermp.getActivePotionEffect(potion).getAmplifier() >= 2 && entityplayermp.getActivePotionEffect(potion).getDuration() >= 1200) continue;
                entityplayermp.connection.sendPacket(new SPacketChangeGameState(10, 0.0f));
                entityplayermp.addPotionEffect(new PotionEffect(potion, 6000, 2));
            }
        }
        if (!this.hasHome()) {
            this.setHomePosAndDistance(new BlockPos(this), 16);
        }
    }
}

