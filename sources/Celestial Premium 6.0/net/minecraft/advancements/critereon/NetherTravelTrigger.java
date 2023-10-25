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
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public class NetherTravelTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_193169_a = new ResourceLocation("nether_travel");
    private final Map<PlayerAdvancements, Listeners> field_193170_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_193169_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners nethertraveltrigger$listeners = this.field_193170_b.get(p_192165_1_);
        if (nethertraveltrigger$listeners == null) {
            nethertraveltrigger$listeners = new Listeners(p_192165_1_);
            this.field_193170_b.put(p_192165_1_, nethertraveltrigger$listeners);
        }
        nethertraveltrigger$listeners.func_193484_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners nethertraveltrigger$listeners = this.field_193170_b.get(p_192164_1_);
        if (nethertraveltrigger$listeners != null) {
            nethertraveltrigger$listeners.func_193481_b(p_192164_2_);
            if (nethertraveltrigger$listeners.func_193482_a()) {
                this.field_193170_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_193170_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        LocationPredicate locationpredicate = LocationPredicate.func_193454_a(p_192166_1_.get("entered"));
        LocationPredicate locationpredicate1 = LocationPredicate.func_193454_a(p_192166_1_.get("exited"));
        DistancePredicate distancepredicate = DistancePredicate.func_193421_a(p_192166_1_.get("distance"));
        return new Instance(locationpredicate, locationpredicate1, distancepredicate);
    }

    public void func_193168_a(EntityPlayerMP p_193168_1_, Vec3d p_193168_2_) {
        Listeners nethertraveltrigger$listeners = this.field_193170_b.get(p_193168_1_.func_192039_O());
        if (nethertraveltrigger$listeners != null) {
            nethertraveltrigger$listeners.func_193483_a(p_193168_1_.getServerWorld(), p_193168_2_, p_193168_1_.posX, p_193168_1_.posY, p_193168_1_.posZ);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_193485_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_193486_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47575_1_) {
            this.field_193485_a = p_i47575_1_;
        }

        public boolean func_193482_a() {
            return this.field_193486_b.isEmpty();
        }

        public void func_193484_a(ICriterionTrigger.Listener<Instance> p_193484_1_) {
            this.field_193486_b.add(p_193484_1_);
        }

        public void func_193481_b(ICriterionTrigger.Listener<Instance> p_193481_1_) {
            this.field_193486_b.remove(p_193481_1_);
        }

        public void func_193483_a(WorldServer p_193483_1_, Vec3d p_193483_2_, double p_193483_3_, double p_193483_5_, double p_193483_7_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_193486_b) {
                if (!listener.func_192158_a().func_193206_a(p_193483_1_, p_193483_2_, p_193483_3_, p_193483_5_, p_193483_7_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_193485_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final LocationPredicate field_193207_a;
        private final LocationPredicate field_193208_b;
        private final DistancePredicate field_193209_c;

        public Instance(LocationPredicate p_i47574_1_, LocationPredicate p_i47574_2_, DistancePredicate p_i47574_3_) {
            super(field_193169_a);
            this.field_193207_a = p_i47574_1_;
            this.field_193208_b = p_i47574_2_;
            this.field_193209_c = p_i47574_3_;
        }

        public boolean func_193206_a(WorldServer p_193206_1_, Vec3d p_193206_2_, double p_193206_3_, double p_193206_5_, double p_193206_7_) {
            if (!this.field_193207_a.func_193452_a(p_193206_1_, p_193206_2_.x, p_193206_2_.y, p_193206_2_.z)) {
                return false;
            }
            if (!this.field_193208_b.func_193452_a(p_193206_1_, p_193206_3_, p_193206_5_, p_193206_7_)) {
                return false;
            }
            return this.field_193209_c.func_193422_a(p_193206_2_.x, p_193206_2_.y, p_193206_2_.z, p_193206_3_, p_193206_5_, p_193206_7_);
        }
    }
}

