package appu26j.mods.multiplayer;

import java.util.ArrayList;

import com.google.common.eventbus.Subscribe;

import appu26j.events.entity.EventTick;
import appu26j.events.mc.EventWorldChange;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.settings.Setting;
import net.minecraft.client.network.NetworkPlayerInfo;

@ModInterface(name = "Name Hider", description = "Hides your name on servers.", category = Category.MULTIPLAYER)
public class SnipeSafe extends Mod
{
	private ArrayList<String> players = new ArrayList<>();
	
	public SnipeSafe()
	{
		this.addSetting(new Setting("Hide all players' name", this, true));
	}
	
	@Subscribe
	public void onTick(EventTick e)
	{
		for (NetworkPlayerInfo networkPlayerInfo : this.mc.getNetHandler().getPlayerInfoMap())
		{
			if (!this.players.contains(networkPlayerInfo.getGameProfile().getName()))
			{
				if (!networkPlayerInfo.getGameProfile().getName().equals(this.mc.thePlayer.getName()))
				{
					this.players.add(networkPlayerInfo.getGameProfile().getName());
				}
			}
		}
	}
	
	@Subscribe
	public void onWorldChange(EventWorldChange e)
	{
		this.players.clear();
	}
	
	public ArrayList<String> getPlayers()
	{
		return this.players;
	}
}
