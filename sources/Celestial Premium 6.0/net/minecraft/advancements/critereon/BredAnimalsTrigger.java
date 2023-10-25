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
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class BredAnimalsTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_192171_a = new ResourceLocation("bred_animals");
    private final Map<PlayerAdvancements, Listeners> field_192172_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_192171_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners bredanimalstrigger$listeners = this.field_192172_b.get(p_192165_1_);
        if (bredanimalstrigger$listeners == null) {
            bredanimalstrigger$listeners = new Listeners(p_192165_1_);
            this.field_192172_b.put(p_192165_1_, bredanimalstrigger$listeners);
        }
        bredanimalstrigger$listeners.func_192343_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners bredanimalstrigger$listeners = this.field_192172_b.get(p_192164_1_);
        if (bredanimalstrigger$listeners != null) {
            bredanimalstrigger$listeners.func_192340_b(p_192164_2_);
            if (bredanimalstrigger$listeners.func_192341_a()) {
                this.field_192172_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_192172_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("parent"));
        EntityPredicate entitypredicate1 = EntityPredicate.func_192481_a(p_192166_1_.get("partner"));
        EntityPredicate entitypredicate2 = EntityPredicate.func_192481_a(p_192166_1_.get("child"));
        return new Instance(entitypredicate, entitypredicate1, entitypredicate2);
    }

    public void func_192168_a(EntityPlayerMP p_192168_1_, EntityAnimal p_192168_2_, EntityAnimal p_192168_3_, EntityAgeable p_192168_4_) {
        Listeners bredanimalstrigger$listeners = this.field_192172_b.get(p_192168_1_.func_192039_O());
        if (bredanimalstrigger$listeners != null) {
            bredanimalstrigger$listeners.func_192342_a(p_192168_1_, p_192168_2_, p_192168_3_, p_192168_4_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_192344_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_192345_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47409_1_) {
            this.field_192344_a = p_i47409_1_;
        }

        public boolean func_192341_a() {
            return this.field_192345_b.isEmpty();
        }

        public void func_192343_a(ICriterionTrigger.Listener<Instance> p_192343_1_) {
            this.field_192345_b.add(p_192343_1_);
        }

        public void func_192340_b(ICriterionTrigger.Listener<Instance> p_192340_1_) {
            this.field_192345_b.remove(p_192340_1_);
        }

        public void func_192342_a(EntityPlayerMP p_192342_1_, EntityAnimal p_192342_2_, EntityAnimal p_192342_3_, EntityAgeable p_192342_4_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_192345_b) {
                if (!listener.func_192158_a().func_192246_a(p_192342_1_, p_192342_2_, p_192342_3_, p_192342_4_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_192344_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final EntityPredicate field_192247_a;
        private final EntityPredicate field_192248_b;
        private final EntityPredicate field_192249_c;

        public Instance(EntityPredicate p_i47408_1_, EntityPredicate p_i47408_2_, EntityPredicate p_i47408_3_) {
            super(field_192171_a);
            this.field_192247_a = p_i47408_1_;
            this.field_192248_b = p_i47408_2_;
            this.field_192249_c = p_i47408_3_;
        }

        public boolean func_192246_a(EntityPlayerMP p_192246_1_, EntityAnimal p_192246_2_, EntityAnimal p_192246_3_, EntityAgeable p_192246_4_) {
            if (!this.field_192249_c.func_192482_a(p_192246_1_, p_192246_4_)) {
                return false;
            }
            return this.field_192247_a.func_192482_a(p_192246_1_, p_192246_2_) && this.field_192248_b.func_192482_a(p_192246_1_, p_192246_3_) || this.field_192247_a.func_192482_a(p_192246_1_, p_192246_3_) && this.field_192248_b.func_192482_a(p_192246_1_, p_192246_2_);
        }
    }
}

