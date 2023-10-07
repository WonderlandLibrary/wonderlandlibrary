// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;

public class PlayerConfiguration
{
    private PlayerItemModel[] playerItemModels;
    private boolean initialized;
    
    public PlayerConfiguration() {
        this.playerItemModels = new PlayerItemModel[0];
        this.initialized = false;
    }
    
    public void renderPlayerItems(final ModelBiped modelBiped, final AbstractClientPlayer player, final float scale, final float partialTicks) {
        if (this.initialized) {
            for (final PlayerItemModel model : this.playerItemModels) {
                model.render(modelBiped, player, scale, partialTicks);
            }
        }
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public void setInitialized(final boolean initialized) {
        this.initialized = initialized;
    }
    
    public PlayerItemModel[] getPlayerItemModels() {
        return this.playerItemModels;
    }
    
    public void addPlayerItemModel(final PlayerItemModel playerItemModel) {
        this.playerItemModels = (PlayerItemModel[])Config.addObjectToArray(this.playerItemModels, playerItemModel);
    }
}
