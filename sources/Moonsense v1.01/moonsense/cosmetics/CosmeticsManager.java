// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics;

import java.awt.Color;
import moonsense.features.SettingsManager;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import moonsense.cosmetics.obj.handler.OBJCosmeticHandler;
import java.util.List;

public class CosmeticsManager
{
    public static List<OBJCosmeticHandler> cosmetics;
    
    static {
        CosmeticsManager.cosmetics = new ArrayList<OBJCosmeticHandler>();
    }
    
    public static void registerCosmetics(final OBJCosmeticHandler... handlers) {
        CosmeticsManager.cosmetics.addAll(Arrays.asList(handlers));
    }
    
    public static float[] getCosmeticColor() {
        return new Color(SettingsManager.INSTANCE.cosmeticColor.getColor()).getRGBColorComponents(null);
    }
}
