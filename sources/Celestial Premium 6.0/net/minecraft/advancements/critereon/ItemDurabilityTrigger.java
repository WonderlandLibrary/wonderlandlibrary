/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemDurabilityTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_193159_a = new ResourceLocation("item_durability_changed");
    private final Map<PlayerAdvancements, Listeners> field_193160_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_193159_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(p_192165_1_);
        if (itemdurabilitytrigger$listeners == null) {
            itemdurabilitytrigger$listeners = new Listeners(p_192165_1_);
            this.field_193160_b.put(p_192165_1_, itemdurabilitytrigger$listeners);
        }
        itemdurabilitytrigger$listeners.func_193440_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(p_192164_1_);
        if (itemdurabilitytrigger$listeners != null) {
            itemdurabilitytrigger$listeners.func_193438_b(p_192164_2_);
            if (itemdurabilitytrigger$listeners.func_193439_a()) {
                this.field_193160_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_193160_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("durability"));
        MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(p_192166_1_.get("delta"));
        return new Instance(itempredicate, minmaxbounds, minmaxbounds1);
    }

    public void func_193158_a(EntityPlayerMP p_193158_1_, ItemStack p_193158_2_, int p_193158_3_) {
        Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(p_193158_1_.func_192039_O());
        if (itemdurabilitytrigger$listeners != null) {
            itemdurabilitytrigger$listeners.func_193441_a(p_193158_2_, p_193158_3_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_193442_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_193443_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47512_1_) {
            this.field_193442_a = p_i47512_1_;
        }

        public boolean func_193439_a() {
            return this.field_193443_b.isEmpty();
        }

        public void func_193440_a(ICriterionTrigger.Listener<Instance> p_193440_1_) {
            this.field_193443_b.add(p_193440_1_);
        }

        public void func_193438_b(ICriterionTrigger.Listener<Instance> p_193438_1_) {
            this.field_193443_b.remove(p_193438_1_);
        }

        public void func_193441_a(ItemStack p_193441_1_, int p_193441_2_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_193443_b) {
                if (!listener.func_192158_a().func_193197_a(p_193441_1_, p_193441_2_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_193442_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final ItemPredicate field_193198_a;
        private final MinMaxBounds field_193199_b;
        private final MinMaxBounds field_193200_c;

        public Instance(ItemPredicate p_i47511_1_, MinMaxBounds p_i47511_2_, MinMaxBounds p_i47511_3_) {
            super(field_193159_a);
            this.field_193198_a = p_i47511_1_;
            this.field_193199_b = p_i47511_2_;
            this.field_193200_c = p_i47511_3_;
        }

        public boolean func_193197_a(ItemStack p_193197_1_, int p_193197_2_) {
            if (!this.field_193198_a.func_192493_a(p_193197_1_)) {
                return false;
            }
            if (!this.field_193199_b.func_192514_a(p_193197_1_.getMaxDamage() - p_193197_2_)) {
                return false;
            }
            return this.field_193200_c.func_192514_a(p_193197_1_.getItemDamage() - p_193197_2_);
        }
    }
}

