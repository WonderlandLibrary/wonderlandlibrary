/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.advancements.critereon;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EnterBlockTrigger
implements ICriterionTrigger<Instance> {
    private static final ResourceLocation field_192196_a = new ResourceLocation("enter_block");
    private final Map<PlayerAdvancements, Listeners> field_192197_b = Maps.newHashMap();

    @Override
    public ResourceLocation func_192163_a() {
        return field_192196_a;
    }

    @Override
    public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {
        Listeners enterblocktrigger$listeners = this.field_192197_b.get(p_192165_1_);
        if (enterblocktrigger$listeners == null) {
            enterblocktrigger$listeners = new Listeners(p_192165_1_);
            this.field_192197_b.put(p_192165_1_, enterblocktrigger$listeners);
        }
        enterblocktrigger$listeners.func_192472_a(p_192165_2_);
    }

    @Override
    public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {
        Listeners enterblocktrigger$listeners = this.field_192197_b.get(p_192164_1_);
        if (enterblocktrigger$listeners != null) {
            enterblocktrigger$listeners.func_192469_b(p_192164_2_);
            if (enterblocktrigger$listeners.func_192470_a()) {
                this.field_192197_b.remove(p_192164_1_);
            }
        }
    }

    @Override
    public void func_192167_a(PlayerAdvancements p_192167_1_) {
        this.field_192197_b.remove(p_192167_1_);
    }

    @Override
    public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
        Block block = null;
        if (p_192166_1_.has("block")) {
            ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(p_192166_1_, "block"));
            if (!Block.REGISTRY.containsKey(resourcelocation)) {
                throw new JsonSyntaxException("Unknown block type '" + resourcelocation + "'");
            }
            block = Block.REGISTRY.getObject(resourcelocation);
        }
        HashMap<IProperty<?>, ?> map = null;
        if (p_192166_1_.has("state")) {
            if (block == null) {
                throw new JsonSyntaxException("Can't define block state without a specific block type");
            }
            BlockStateContainer blockstatecontainer = block.getBlockState();
            for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(p_192166_1_, "state").entrySet()) {
                IProperty<?> iproperty = blockstatecontainer.getProperty(entry.getKey());
                if (iproperty == null) {
                    throw new JsonSyntaxException("Unknown block state property '" + entry.getKey() + "' for block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }
                String s = JsonUtils.getString(entry.getValue(), entry.getKey());
                Optional<?> optional = iproperty.parseValue(s);
                if (!optional.isPresent()) {
                    throw new JsonSyntaxException("Invalid block state value '" + s + "' for property '" + entry.getKey() + "' on block '" + Block.REGISTRY.getNameForObject(block) + "'");
                }
                if (map == null) {
                    map = Maps.newHashMap();
                }
                map.put(iproperty, optional.get());
            }
        }
        return new Instance(block, map);
    }

    public void func_192193_a(EntityPlayerMP p_192193_1_, IBlockState p_192193_2_) {
        Listeners enterblocktrigger$listeners = this.field_192197_b.get(p_192193_1_.func_192039_O());
        if (enterblocktrigger$listeners != null) {
            enterblocktrigger$listeners.func_192471_a(p_192193_2_);
        }
    }

    static class Listeners {
        private final PlayerAdvancements field_192473_a;
        private final Set<ICriterionTrigger.Listener<Instance>> field_192474_b = Sets.newHashSet();

        public Listeners(PlayerAdvancements p_i47452_1_) {
            this.field_192473_a = p_i47452_1_;
        }

        public boolean func_192470_a() {
            return this.field_192474_b.isEmpty();
        }

        public void func_192472_a(ICriterionTrigger.Listener<Instance> p_192472_1_) {
            this.field_192474_b.add(p_192472_1_);
        }

        public void func_192469_b(ICriterionTrigger.Listener<Instance> p_192469_1_) {
            this.field_192474_b.remove(p_192469_1_);
        }

        public void func_192471_a(IBlockState p_192471_1_) {
            ArrayList<ICriterionTrigger.Listener<Instance>> list = null;
            for (ICriterionTrigger.Listener<Instance> listener : this.field_192474_b) {
                if (!listener.func_192158_a().func_192260_a(p_192471_1_)) continue;
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<Instance> listener1 : list) {
                    listener1.func_192159_a(this.field_192473_a);
                }
            }
        }
    }

    public static class Instance
    extends AbstractCriterionInstance {
        private final Block field_192261_a;
        private final Map<IProperty<?>, Object> field_192262_b;

        public Instance(@Nullable Block p_i47451_1_, @Nullable Map<IProperty<?>, Object> p_i47451_2_) {
            super(field_192196_a);
            this.field_192261_a = p_i47451_1_;
            this.field_192262_b = p_i47451_2_;
        }

        public boolean func_192260_a(IBlockState p_192260_1_) {
            if (this.field_192261_a != null && p_192260_1_.getBlock() != this.field_192261_a) {
                return false;
            }
            if (this.field_192262_b != null) {
                for (Map.Entry<IProperty<?>, Object> entry : this.field_192262_b.entrySet()) {
                    if (p_192260_1_.getValue(entry.getKey()) == entry.getValue()) continue;
                    return false;
                }
            }
            return true;
        }
    }
}

