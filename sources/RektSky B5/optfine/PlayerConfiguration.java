/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import optfine.Config;
import optfine.PlayerItemModel;

public class PlayerConfiguration {
    private PlayerItemModel[] playerItemModels = new PlayerItemModel[0];
    private boolean initialized = false;

    public void renderPlayerItems(ModelBiped p_renderPlayerItems_1_, AbstractClientPlayer p_renderPlayerItems_2_, float p_renderPlayerItems_3_, float p_renderPlayerItems_4_) {
        if (this.initialized) {
            for (int i2 = 0; i2 < this.playerItemModels.length; ++i2) {
                PlayerItemModel playeritemmodel = this.playerItemModels[i2];
                playeritemmodel.render(p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_, p_renderPlayerItems_4_);
            }
        }
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void setInitialized(boolean p_setInitialized_1_) {
        this.initialized = p_setInitialized_1_;
    }

    public PlayerItemModel[] getPlayerItemModels() {
        return this.playerItemModels;
    }

    public void addPlayerItemModel(PlayerItemModel p_addPlayerItemModel_1_) {
        this.playerItemModels = (PlayerItemModel[])Config.addObjectToArray(this.playerItemModels, p_addPlayerItemModel_1_);
    }
}

