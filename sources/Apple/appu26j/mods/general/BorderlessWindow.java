package appu26j.mods.general;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;

@ModInterface(name = "Borderless Window", description = "A borderless fullscreen!", category = Category.GENERAL)
public class BorderlessWindow extends Mod
{
    private boolean wasFullScreen = false;
    
    @Override
    public void onEnable()
    {
        super.onEnable();
        this.wasFullScreen = this.mc.isFullScreen();
        
        if (this.wasFullScreen)
        {
            this.mc.toggleFullscreen();
        }
        
        this.mc.toggleBorderlessFullscreen();
    }
    
    @Override
    public void onDisable()
    {
        super.onDisable();
        this.mc.toggleBorderlessFullscreen();
        
        if (this.wasFullScreen)
        {
            this.mc.toggleFullscreen();
        }
    }
}
