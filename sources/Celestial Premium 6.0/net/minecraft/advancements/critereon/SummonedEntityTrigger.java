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
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class SummonedEntityTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_192232_a = new ResourceLocation("summoned_entity");
    private final Map<PlayerAdvancements, Listeners> field_192233_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_192232_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners summonedentitytrigger$listeners = this.field_192233_b.get(p_192165_1_);
        if (summonedentitytrigger$listeners == null) {
            summonedentitytrigger$listeners = new Listeners(p_192165_1_);
            this.field_192233_b.put(p_192165_1_, summonedentitytrigger$listeners);
        }
        summonedentitytrigger$listeners.func_192534_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners summonedentitytrigger$listeners = this.field_192233_b.get(p_192164_1_);
        if (summonedentitytrigger$listeners != null) {
            summonedentitytrigger$listeners.func_192531_b(p_192164_2_);
            if (summonedentitytrigger$listeners.func_192532_a()) {
                this.field_192233_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_192233_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("entity"));
        return new Instance(entitypredicate);
    }

    public void func_192229_a(EntityPlayerMP p_192229_1_, Entity p_192229_2_) {
        Listeners summonedentitytrigger$listeners = this.field_192233_b.get(p_192229_1_.func_192039_O());
        if (summonedentitytrigger$listeners != null) {
            summonedentitytrigger$listeners.func_192533_a(p_192229_1_, p_192229_2_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_192535_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_192536_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47372_1_) {
            this.field_192535_a = p_i47372_1_;
        }

        public boolean func_192532_a() {
            return this.field_192536_b.isEmpty();
        }

        public void func_192534_a(ICriterionTrigger.Listener<Instance> p_192534_1_) {
            this.field_192536_b.add(p_192534_1_);
        }

        public void func_192531_b(ICriterionTrigger.Listener<Instance> p_192531_1_) {
            this.field_192536_b.remove(p_192531_1_);
        }

        public void func_192533_a(EntityPlayerMP p_192533_1_, Entity p_192533_2_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_192536_b) {
                if (!listener.func_192158_a().func_192283_a(p_192533_1_, p_192533_2_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_192535_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final EntityPredicate field_192284_a;

        public Instance(EntityPredicate p_i47371_1_) {
            super(field_192232_a);
            this.field_192284_a = p_i47371_1_;
        }

        public boolean func_192283_a(EntityPlayerMP p_192283_1_, Entity p_192283_2_) {
            return this.field_192284_a.func_192482_a(p_192283_1_, p_192283_2_);
        }
    }
}

