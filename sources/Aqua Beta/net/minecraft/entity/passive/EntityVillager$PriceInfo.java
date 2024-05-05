package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.util.Tuple;

static class EntityVillager.PriceInfo
extends Tuple<Integer, Integer> {
    public EntityVillager.PriceInfo(int p_i45810_1_, int p_i45810_2_) {
        super((Object)p_i45810_1_, (Object)p_i45810_2_);
    }

    public int getPrice(Random rand) {
        return (Integer)this.getFirst() >= (Integer)this.getSecond() ? (Integer)this.getFirst() : (Integer)this.getFirst() + rand.nextInt((Integer)this.getSecond() - (Integer)this.getFirst() + 1);
    }
}
