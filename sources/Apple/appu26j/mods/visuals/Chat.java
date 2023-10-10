package appu26j.mods.visuals;

import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

@ModInterface(name = "Chat", description = "Allows you to enable and change the behaviour of the chat.", category = Category.VISUALS)
public class Chat extends Mod
{
    private int amount = 0, temp = 0;
    private String lastMessage = "";
    
	public Chat()
	{
        this.addSetting(new Setting("No Background", this, false));
		this.addSetting(new Setting("Infinite History", this, false));
		this.addSetting(new Setting("Text Shadow", this, true));
        this.addSetting(new Setting("Don't Clear History", this, false));
        this.addSetting(new Setting("No Close My Chat", this, false));
	}
}
