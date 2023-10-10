package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;

@ModInterface(name = "Item Physics", description = "Gives dropped items physics.", category = Category.VISUALS)
public class ItemPhysics extends Mod
{
    private static long lastRenderTime = 0;
    
    public static float getRotation()
    {
        return (System.nanoTime() - lastRenderTime) / 1.0E8F;
    }
    
    public static void updateLastRenderTime()
    {
        lastRenderTime = System.nanoTime();
    }
}
