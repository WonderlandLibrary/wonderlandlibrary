// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.RegistrySimple;

public class SoundRegistry extends RegistrySimple
{
    private Map field_148764_a;
    private static final String __OBFID = "CL_00001151";
    
    @Override
    protected Map createUnderlyingMap() {
        return this.field_148764_a = Maps.newHashMap();
    }
    
    public void registerSound(final SoundEventAccessorComposite p_148762_1_) {
        this.putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
    }
    
    public void clearMap() {
        this.field_148764_a.clear();
    }
}
