// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.impl.obj;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import moonsense.MoonsenseClient;
import moonsense.cosmetics.obj.OBJCosmetic;
import moonsense.cosmetics.obj.handler.OBJCosmeticHandler;

public class OBJBandana extends OBJCosmeticHandler
{
    private OBJCosmetic model;
    
    public OBJBandana() {
        super("Bandana", CosmeticsType.HAT);
        try {
            this.model = MoonsenseClient.INSTANCE.getCosmeticLoader().loadModel(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("streamlined/cosmetics/bandana/bandana.obj")).getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void render(final Entity entityIn, final ModelRenderer modelRenderer) {
    }
}
