package moonsense.cosmetic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CosmeticBoolean
{
    public static boolean cape1;
    public static boolean snowCape;
    
    static {
    	CosmeticBoolean.cape1 = true;
        CosmeticBoolean.snowCape = false;
    }
    
    
    public static boolean Cape1() {
        return CosmeticBoolean.cape1;
    }
    
    public static boolean SnowCape() {
        return CosmeticBoolean.snowCape;
    }
}
