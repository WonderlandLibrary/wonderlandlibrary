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
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

public class ConstructBeaconTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_192181_a = new ResourceLocation("construct_beacon");
    private final Map<PlayerAdvancements, Listeners> field_192182_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_192181_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners constructbeacontrigger$listeners = this.field_192182_b.get(p_192165_1_);
        if (constructbeacontrigger$listeners == null) {
            constructbeacontrigger$listeners = new Listeners(p_192165_1_);
            this.field_192182_b.put(p_192165_1_, constructbeacontrigger$listeners);
        }
        constructbeacontrigger$listeners.func_192355_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners constructbeacontrigger$listeners = this.field_192182_b.get(p_192164_1_);
        if (constructbeacontrigger$listeners != null) {
            constructbeacontrigger$listeners.func_192353_b(p_192164_2_);
            if (constructbeacontrigger$listeners.func_192354_a()) {
                this.field_192182_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_192182_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("level"));
        return new Instance(minmaxbounds);
    }

    public void func_192180_a(EntityPlayerMP p_192180_1_, TileEntityBeacon p_192180_2_) {
        Listeners constructbeacontrigger$listeners = this.field_192182_b.get(p_192180_1_.func_192039_O());
        if (constructbeacontrigger$listeners != null) {
            constructbeacontrigger$listeners.func_192352_a(p_192180_2_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_192356_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_192357_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47374_1_) {
            this.field_192356_a = p_i47374_1_;
        }

        public boolean func_192354_a() {
            return this.field_192357_b.isEmpty();
        }

        public void func_192355_a(ICriterionTrigger.Listener<Instance> p_192355_1_) {
            this.field_192357_b.add(p_192355_1_);
        }

        public void func_192353_b(ICriterionTrigger.Listener<Instance> p_192353_1_) {
            this.field_192357_b.remove(p_192353_1_);
        }

        public void func_192352_a(TileEntityBeacon p_192352_1_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_192357_b) {
                if (!listener.func_192158_a().func_192252_a(p_192352_1_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_192356_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final MinMaxBounds field_192253_a;

        public Instance(MinMaxBounds p_i47373_1_) {
            super(field_192181_a);
            this.field_192253_a = p_i47373_1_;
        }

        public boolean func_192252_a(TileEntityBeacon p_192252_1_) {
            return this.field_192253_a.func_192514_a(p_192252_1_.getLevels());
        }
    }
}

