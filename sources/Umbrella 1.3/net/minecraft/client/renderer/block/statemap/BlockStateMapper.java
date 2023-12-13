/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;

public class BlockStateMapper {
    private Map field_178450_a = Maps.newIdentityHashMap();
    private Set field_178449_b = Sets.newIdentityHashSet();
    private static final String __OBFID = "CL_00002478";

    public void func_178447_a(Block p_178447_1_, IStateMapper p_178447_2_) {
        this.field_178450_a.put(p_178447_1_, p_178447_2_);
    }

    public void registerBuiltInBlocks(Block ... p_178448_1_) {
        Collections.addAll(this.field_178449_b, p_178448_1_);
    }

    public Map func_178446_a() {
        IdentityHashMap var1 = Maps.newIdentityHashMap();
        for (Block var3 : Block.blockRegistry) {
            if (this.field_178449_b.contains(var3)) continue;
            var1.putAll(((IStateMapper)Objects.firstNonNull(this.field_178450_a.get(var3), (Object)new DefaultStateMapper())).func_178130_a(var3));
        }
        return var1;
    }
}

