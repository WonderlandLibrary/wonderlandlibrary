package appu26j;

import java.util.ArrayList;
import java.util.stream.Collectors;

import appu26j.interfaces.MinecraftInterface;
import appu26j.mods.Mod;
import net.minecraft.client.gui.ScaledResolution;

// Class to cache ScaledResolution :D
public class Cache implements MinecraftInterface
{
    private static ScaledResolution scaledResolution;
    
    static
    {
        scaledResolution = new ScaledResolution(mc);
    }
    
    /**
     * Gets the cached ScaledResolution. Always use this method instead.
     * @return ScaledResolution
     */
    public static ScaledResolution getSR()
    {
        return scaledResolution;
    }
    
    /**
     * Sets the cached ScaledResolution. Make sure you know what you're doing.
     * @param scaledResolution
     */
    public static void setSR(ScaledResolution scaledResolution)
    {
        Cache.scaledResolution = scaledResolution;
        
        if (Apple.CLIENT.getModsManager() != null)
        {
            for (Mod mod : Apple.CLIENT.getModsManager().getMods().stream().filter(mod -> mod.isEnabled() && mod.hasGUI()).collect(Collectors.toCollection(ArrayList::new)))
            {
                if ((mod.getX() + mod.getWidth()) > scaledResolution.getScaledWidth())
                {
                    mod.setPosition(scaledResolution.getScaledWidth() - mod.getWidth(), mod.getY());
                }
                
                if ((mod.getY() + mod.getHeight()) > scaledResolution.getScaledHeight())
                {
                    mod.setPosition(mod.getX(), scaledResolution.getScaledHeight() - mod.getHeight());
                }
            }
        }
    }
}
