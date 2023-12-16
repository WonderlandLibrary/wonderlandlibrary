// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.model.base;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBase;

public class CosmeticModelBase extends ModelBase
{
    protected final ModelBiped playerModel;
    
    public CosmeticModelBase(final RenderPlayer player) {
        this.playerModel = (ModelBiped)player.getMainModel();
    }
}
