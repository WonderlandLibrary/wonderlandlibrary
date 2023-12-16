package moonsense.cosmetic;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBase;

public class CosmeticModelBase extends ModelBase
{
    protected final ModelBiped playerModel;
    
    public CosmeticModelBase(final RenderPlayer player) {
        this.playerModel = player.getMainModel();
    }
}
