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
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class EffectsChangedTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_193154_a = new ResourceLocation("effects_changed");
    private final Map<PlayerAdvancements, Listeners> field_193155_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_193154_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners effectschangedtrigger$listeners = this.field_193155_b.get(p_192165_1_);
        if (effectschangedtrigger$listeners == null) {
            effectschangedtrigger$listeners = new Listeners(p_192165_1_);
            this.field_193155_b.put(p_192165_1_, effectschangedtrigger$listeners);
        }
        effectschangedtrigger$listeners.func_193431_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners effectschangedtrigger$listeners = this.field_193155_b.get(p_192164_1_);
        if (effectschangedtrigger$listeners != null) {
            effectschangedtrigger$listeners.func_193429_b(p_192164_2_);
            if (effectschangedtrigger$listeners.func_193430_a()) {
                this.field_193155_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_193155_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        MobEffectsPredicate mobeffectspredicate = MobEffectsPredicate.func_193471_a(p_192166_1_.get("effects"));
        return new Instance(mobeffectspredicate);
    }

    public void func_193153_a(EntityPlayerMP p_193153_1_) {
        Listeners effectschangedtrigger$listeners = this.field_193155_b.get(p_193153_1_.func_192039_O());
        if (effectschangedtrigger$listeners != null) {
            effectschangedtrigger$listeners.func_193432_a(p_193153_1_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_193433_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_193434_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47546_1_) {
            this.field_193433_a = p_i47546_1_;
        }

        public boolean func_193430_a() {
            return this.field_193434_b.isEmpty();
        }

        public void func_193431_a(ICriterionTrigger.Listener<Instance> p_193431_1_) {
            this.field_193434_b.add(p_193431_1_);
        }

        public void func_193429_b(ICriterionTrigger.Listener<Instance> p_193429_1_) {
            this.field_193434_b.remove(p_193429_1_);
        }

        public void func_193432_a(EntityPlayerMP p_193432_1_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_193434_b) {
                if (!listener.func_192158_a().func_193195_a(p_193432_1_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_193433_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final MobEffectsPredicate field_193196_a;

        public Instance(MobEffectsPredicate p_i47545_1_) {
            super(field_193154_a);
            this.field_193196_a = p_i47545_1_;
        }

        public boolean func_193195_a(EntityPlayerMP p_193195_1_) {
            return this.field_193196_a.func_193472_a(p_193195_1_);
        }
    }
}

