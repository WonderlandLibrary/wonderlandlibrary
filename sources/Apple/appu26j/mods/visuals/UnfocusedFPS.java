package appu26j.mods.visuals;

import appu26j.Apple;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;

@ModInterface(name = "Unfocused FPS", description = "Limits FPS to 15 when game is not focused, to save resources.", category = Category.VISUALS)
public class UnfocusedFPS extends Mod
{
    public static int previousFPSLimit = 0;
    private static UnfocusedFPS instance;
    public static boolean temp = true;
    
    public UnfocusedFPS()
    {
        this.instance = this;
    }
    
    public static UnfocusedFPS get()
    {
        return instance;
    }
}
