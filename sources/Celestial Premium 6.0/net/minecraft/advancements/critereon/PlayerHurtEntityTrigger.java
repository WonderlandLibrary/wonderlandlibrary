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
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PlayerHurtEntityTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_192222_a = new ResourceLocation("player_hurt_entity");
    private final Map<PlayerAdvancements, Listeners> field_192223_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_192222_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners playerhurtentitytrigger$listeners = this.field_192223_b.get(p_192165_1_);
        if (playerhurtentitytrigger$listeners == null) {
            playerhurtentitytrigger$listeners = new Listeners(p_192165_1_);
            this.field_192223_b.put(p_192165_1_, playerhurtentitytrigger$listeners);
        }
        playerhurtentitytrigger$listeners.func_192522_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners playerhurtentitytrigger$listeners = this.field_192223_b.get(p_192164_1_);
        if (playerhurtentitytrigger$listeners != null) {
            playerhurtentitytrigger$listeners.func_192519_b(p_192164_2_);
            if (playerhurtentitytrigger$listeners.func_192520_a()) {
                this.field_192223_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_192223_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        DamagePredicate damagepredicate = DamagePredicate.func_192364_a(p_192166_1_.get("damage"));
        EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("entity"));
        return new Instance(damagepredicate, entitypredicate);
    }

    public void func_192220_a(EntityPlayerMP p_192220_1_, Entity p_192220_2_, DamageSource p_192220_3_, float p_192220_4_, float p_192220_5_, boolean p_192220_6_) {
        Listeners playerhurtentitytrigger$listeners = this.field_192223_b.get(p_192220_1_.func_192039_O());
        if (playerhurtentitytrigger$listeners != null) {
            playerhurtentitytrigger$listeners.func_192521_a(p_192220_1_, p_192220_2_, p_192220_3_, p_192220_4_, p_192220_5_, p_192220_6_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_192523_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_192524_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47407_1_) {
            this.field_192523_a = p_i47407_1_;
        }

        public boolean func_192520_a() {
            return this.field_192524_b.isEmpty();
        }

        public void func_192522_a(ICriterionTrigger.Listener<Instance> p_192522_1_) {
            this.field_192524_b.add(p_192522_1_);
        }

        public void func_192519_b(ICriterionTrigger.Listener<Instance> p_192519_1_) {
            this.field_192524_b.remove(p_192519_1_);
        }

        public void func_192521_a(EntityPlayerMP p_192521_1_, Entity p_192521_2_, DamageSource p_192521_3_, float p_192521_4_, float p_192521_5_, boolean p_192521_6_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_192524_b) {
                if (!listener.func_192158_a().func_192278_a(p_192521_1_, p_192521_2_, p_192521_3_, p_192521_4_, p_192521_5_, p_192521_6_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_192523_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final DamagePredicate field_192279_a;
        private final EntityPredicate field_192280_b;

        public Instance(DamagePredicate p_i47406_1_, EntityPredicate p_i47406_2_) {
            super(field_192222_a);
            this.field_192279_a = p_i47406_1_;
            this.field_192280_b = p_i47406_2_;
        }

        public boolean func_192278_a(EntityPlayerMP p_192278_1_, Entity p_192278_2_, DamageSource p_192278_3_, float p_192278_4_, float p_192278_5_, boolean p_192278_6_) {
            if (!this.field_192279_a.func_192365_a(p_192278_1_, p_192278_3_, p_192278_4_, p_192278_5_, p_192278_6_)) {
                return false;
            }
            return this.field_192280_b.func_192482_a(p_192278_1_, p_192278_2_);
        }
    }
}

