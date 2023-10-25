package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.potion.Potion;

public static class EntitySpider.GroupData
implements IEntityLivingData {
    public int potionEffectId;

    public void func_111104_a(Random rand) {
        int i = rand.nextInt(5);
        if (i <= 1) {
            this.potionEffectId = Potion.moveSpeed.id;
        } else if (i <= 2) {
            this.potionEffectId = Potion.damageBoost.id;
        } else if (i <= 3) {
            this.potionEffectId = Potion.regeneration.id;
        } else if (i <= 4) {
            this.potionEffectId = Potion.invisibility.id;
        }
    }
}
