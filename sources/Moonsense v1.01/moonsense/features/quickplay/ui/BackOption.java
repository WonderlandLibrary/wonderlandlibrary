// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.quickplay.ui;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import moonsense.features.modules.QuickplayModule;

public class BackOption implements QuickplayOption
{
    @Override
    public String getText() {
        return "< Back";
    }
    
    @Override
    public void onClick(final QuickplayMenu palette, final QuickplayModule mod) {
        palette.back();
    }
    
    @Override
    public ItemStack getIcon() {
        return new ItemStack(Items.arrow);
    }
}
