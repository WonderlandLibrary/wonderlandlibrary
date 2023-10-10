package appu26j.mods.visuals;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;

@ModInterface(name = "Dab Mod", description = "Press F to dab! Made by MrCrayFish and 2PI", category = Category.VISUALS)
public class DabMod extends Mod
{
    private int prevThirdPersonView = 0;
    private boolean prevKeyDown = false;
    
    @Subscribe
    public void onTick(EventTick e)
    {
        if (this.mc.gameSettings.dab.isKeyDown())
        {
            if (!this.prevKeyDown)
            {
                this.prevThirdPersonView = this.mc.gameSettings.thirdPersonView;
                this.mc.gameSettings.thirdPersonView = 2;
                this.prevKeyDown = true;
            }
        }
        
        else
        {
            if (this.prevKeyDown)
            {
                this.mc.gameSettings.thirdPersonView = this.prevThirdPersonView;
                this.prevKeyDown = false;
            }
        }
    }
}
