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
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class KilledTrigger
implements ICriterionTrigger<Instance> {
    private final Map<PlayerAdvancements, Listeners> field_192213_a = Maps.newHashMap();
    private final ResourceLocation field_192214_b;

    public KilledTrigger(ResourceLocation p_i47433_1_) {
        this.field_192214_b = p_i47433_1_;
    }

    @Override
    public ResourceLocation func_192163_a() {
        return this.field_192214_b;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners killedtrigger$listeners = this.field_192213_a.get(p_192165_1_);
        if (killedtrigger$listeners == null) {
            killedtrigger$listeners = new Listeners(p_192165_1_);
            this.field_192213_a.put(p_192165_1_, killedtrigger$listeners);
        }
        killedtrigger$listeners.func_192504_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners killedtrigger$listeners = this.field_192213_a.get(p_192164_1_);
        if (killedtrigger$listeners != null) {
            killedtrigger$listeners.func_192501_b(p_192164_2_);
            if (killedtrigger$listeners.func_192502_a()) {
                this.field_192213_a.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_192213_a.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        return new Instance(this.field_192214_b, EntityPredicate.func_192481_a(p_192166_1_.get("entity")), DamageSourcePredicate.func_192447_a(p_192166_1_.get("killing_blow")));
    }

    public void func_192211_a(EntityPlayerMP p_192211_1_, Entity p_192211_2_, DamageSource p_192211_3_) {
        Listeners killedtrigger$listeners = this.field_192213_a.get(p_192211_1_.func_192039_O());
        if (killedtrigger$listeners != null) {
            killedtrigger$listeners.func_192503_a(p_192211_1_, p_192211_2_, p_192211_3_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_192505_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_192506_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47455_1_) {
            this.field_192505_a = p_i47455_1_;
        }

        public boolean func_192502_a() {
            return this.field_192506_b.isEmpty();
        }

        public void func_192504_a(ICriterionTrigger.Listener<Instance> p_192504_1_) {
            this.field_192506_b.add(p_192504_1_);
        }

        public void func_192501_b(ICriterionTrigger.Listener<Instance> p_192501_1_) {
            this.field_192506_b.remove(p_192501_1_);
        }

        public void func_192503_a(EntityPlayerMP p_192503_1_, Entity p_192503_2_, DamageSource p_192503_3_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_192506_b) {
                if (!listener.func_192158_a().func_192270_a(p_192503_1_, p_192503_2_, p_192503_3_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_192505_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final EntityPredicate field_192271_a;
        private final DamageSourcePredicate field_192272_b;

        public Instance(ResourceLocation p_i47454_1_, EntityPredicate p_i47454_2_, DamageSourcePredicate p_i47454_3_) {
            super(p_i47454_1_);
            this.field_192271_a = p_i47454_2_;
            this.field_192272_b = p_i47454_3_;
        }

        public boolean func_192270_a(EntityPlayerMP p_192270_1_, Entity p_192270_2_, DamageSource p_192270_3_) {
            return !this.field_192272_b.func_193418_a(p_192270_1_, p_192270_3_) ? false : this.field_192271_a.func_192482_a(p_192270_1_, p_192270_2_);
        }
    }
}

