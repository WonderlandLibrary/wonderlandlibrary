package appu26j.mods.multiplayer;

import org.lwjgl.input.Keyboard;

import com.google.common.eventbus.Subscribe;

import appu26j.Apple;
import appu26j.events.mc.EventKey;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;

@ModInterface(name = "Quick Play", description = "Allows you to press R and click on a game to play it.", category = Category.MULTIPLAYER)
public class QuickPlay extends Mod
{
    @Subscribe
    public void onKey(EventKey e)
    {
        if (e.getKey() == Keyboard.KEY_R)
        {
            this.mc.displayGuiScreen(Apple.CLIENT.getQuickPlayGUI());
        }
    }
}
