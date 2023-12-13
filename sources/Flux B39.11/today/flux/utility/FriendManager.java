package today.flux.utility;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import today.flux.Flux;

public class FriendManager {
	private ArrayList<String> friends;

	public FriendManager() {
		this.friends = new ArrayList<>();
	}

	public void addFriend(String name) {
		if (!this.getFriends().contains(name.toLowerCase())) {
			this.getFriends().add(name.toLowerCase());
		}
	}

	public void delFriend(String name) {
		if (this.getFriends().contains(name.toLowerCase())) {
			this.getFriends().remove(name.toLowerCase());
		}
	}

	public ArrayList<String> getFriends() {
		return this.friends;
	}

	public boolean isFriend(String name) {
		for (final String friend : this.getFriends()) {
			if (friend.contains(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTeam(EntityPlayer entity) {
		return WorldRenderUtils.getTeamColor(entity) == WorldRenderUtils
				.getTeamColor(Minecraft.getMinecraft().thePlayer)
				|| (Flux.INSTANCE.getFriendManager().isFriend(entity.getName()) && Flux.friends.getValueState());
	}
	
	public static boolean isMobTeam(EntityMob entity) {
		String mobpre = entity.getDisplayName().getFormattedText().substring(0,2);
		String playerpre = Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().substring(0,2);
		if(mobpre.equals("") || playerpre.equals("")) return false;
		return (Flux.INSTANCE.mobfriends.getValueState() && mobpre.equals(playerpre));
	}
}
