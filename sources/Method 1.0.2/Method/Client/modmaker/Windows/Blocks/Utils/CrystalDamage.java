package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.Objects;

public class CrystalDamage extends Block {

    public CrystalDamage() {
        super("CrystalDamage", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.words[0] = "Crystal Pos";
        this.words[1] = "Target Entity";
        this.typesAccepted.add(typesCollapse(MainBlockType.Vec3d));
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.description = "Calculates the damage of an explosion at a certain position";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            Vec3d pos = (Vec3d) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);
            Entity entity = (Entity) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, event);

        return Calculate_Damage(pos.x, pos.y, pos.z, entity);
    }


    public static float Calculate_Damage(double posX, double posY, double posZ, Entity entity) {
        double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0D;
        double blockDensity = entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
        double v = (1.0D - distancedsize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D));
        if (entity instanceof EntityLivingBase) {
            return get_blast_reduction((EntityLivingBase) entity, get_damage_multiplied(damage), new Explosion(Minecraft.getMinecraft().world, null, posX, posY, posZ, 6.0F, false, true));
        }
        return (float) 1.0D;
    }

    private static float get_damage_multiplied(float damage) {
        int diff = Minecraft.getMinecraft().world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
    }

    public static float get_blast_reduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            damage = CombatRules.getDamageAfterAbsorb(damage, (ep.getTotalArmorValue()), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion));
            damage *= 1.0F - MathHelper.clamp((float) k, 0.0F, 20.0F) / 25.0F;
            if (entity.isPotionActive(Objects.requireNonNull(MobEffects.RESISTANCE))) {
                damage -= damage / 4.0F;
            }
            return Math.max(damage, 0.0F);
        } else {
            return CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }
    }

}
