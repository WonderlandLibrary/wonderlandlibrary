// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.statemap;

import java.util.Iterator;
import java.util.IdentityHashMap;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.block.Block;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import java.util.Map;

public class BlockStateMapper
{
    private Map field_178450_a;
    private Set field_178449_b;
    private static final String __OBFID = "CL_00002478";
    
    public BlockStateMapper() {
        this.field_178450_a = Maps.newIdentityHashMap();
        this.field_178449_b = Sets.newIdentityHashSet();
    }
    
    public void func_178447_a(final Block p_178447_1_, final IStateMapper p_178447_2_) {
        this.field_178450_a.put(p_178447_1_, p_178447_2_);
    }
    
    public void registerBuiltInBlocks(final Block... p_178448_1_) {
        Collections.addAll(this.field_178449_b, p_178448_1_);
    }
    
    public Map func_178446_a() {
        final IdentityHashMap var1 = Maps.newIdentityHashMap();
        for (final Block var3 : Block.blockRegistry) {
            if (!this.field_178449_b.contains(var3)) {
                var1.putAll(((IStateMapper)Objects.firstNonNull(this.field_178450_a.get(var3), (Object)new DefaultStateMapper())).func_178130_a(var3));
            }
        }
        return var1;
    }
}
