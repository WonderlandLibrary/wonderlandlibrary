// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.quickplay.ui;

import net.minecraft.item.ItemStack;
import moonsense.features.modules.QuickplayModule;

public interface QuickplayOption
{
    String getText();
    
    void onClick(final QuickplayMenu p0, final QuickplayModule p1);
    
    ItemStack getIcon();
}
