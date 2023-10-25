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
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class LevitationTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_193164_a = new ResourceLocation("levitation");
    private final Map<PlayerAdvancements, Listeners> field_193165_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_193164_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners levitationtrigger$listeners = this.field_193165_b.get(p_192165_1_);
        if (levitationtrigger$listeners == null) {
            levitationtrigger$listeners = new Listeners(p_192165_1_);
            this.field_193165_b.put(p_192165_1_, levitationtrigger$listeners);
        }
        levitationtrigger$listeners.func_193449_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners levitationtrigger$listeners = this.field_193165_b.get(p_192164_1_);
        if (levitationtrigger$listeners != null) {
            levitationtrigger$listeners.func_193446_b(p_192164_2_);
            if (levitationtrigger$listeners.func_193447_a()) {
                this.field_193165_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_193165_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        DistancePredicate distancepredicate = DistancePredicate.func_193421_a(p_192166_1_.get("distance"));
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("duration"));
        return new Instance(distancepredicate, minmaxbounds);
    }

    public void func_193162_a(EntityPlayerMP p_193162_1_, Vec3d p_193162_2_, int p_193162_3_) {
        Listeners levitationtrigger$listeners = this.field_193165_b.get(p_193162_1_.func_192039_O());
        if (levitationtrigger$listeners != null) {
            levitationtrigger$listeners.func_193448_a(p_193162_1_, p_193162_2_, p_193162_3_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_193450_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_193451_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47572_1_) {
            this.field_193450_a = p_i47572_1_;
        }

        public boolean func_193447_a() {
            return this.field_193451_b.isEmpty();
        }

        public void func_193449_a(ICriterionTrigger.Listener<Instance> p_193449_1_) {
            this.field_193451_b.add(p_193449_1_);
        }

        public void func_193446_b(ICriterionTrigger.Listener<Instance> p_193446_1_) {
            this.field_193451_b.remove(p_193446_1_);
        }

        public void func_193448_a(EntityPlayerMP p_193448_1_, Vec3d p_193448_2_, int p_193448_3_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_193451_b) {
                if (!listener.func_192158_a().func_193201_a(p_193448_1_, p_193448_2_, p_193448_3_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_193450_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final DistancePredicate field_193202_a;
        private final MinMaxBounds field_193203_b;

        public Instance(DistancePredicate p_i47571_1_, MinMaxBounds p_i47571_2_) {
            super(field_193164_a);
            this.field_193202_a = p_i47571_1_;
            this.field_193203_b = p_i47571_2_;
        }

        public boolean func_193201_a(EntityPlayerMP p_193201_1_, Vec3d p_193201_2_, int p_193201_3_) {
            if (!this.field_193202_a.func_193422_a(p_193201_2_.x, p_193201_2_.y, p_193201_2_.z, p_193201_1_.posX, p_193201_1_.posY, p_193201_1_.posZ)) {
                return false;
            }
            return this.field_193203_b.func_192514_a(p_193201_3_);
        }
    }
}

