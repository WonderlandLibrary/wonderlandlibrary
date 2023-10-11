package artemis.command.friend;

import java.util.ArrayList;

import artemis.command.friend.sub.Friend;
import net.minecraft.util.StringUtils;

public class FriendManager {
private ArrayList<Friend> friends = new ArrayList<Friend>();
	


public void addFriend(String name, String alias) {
	friends.add(new Friend(name, alias));
}
	
	public ArrayList<Friend> getFriends() {
		return this.friends;
	}
	
	public void removeFriend(String name) {
		for(Friend f : this.friends) {
			if(f.getName().equalsIgnoreCase(name)) {
				this.friends.remove(f);
				break;
			}
		}
	
}
	public void clearFriends() {
		this.friends.clear();
	}
	
	public boolean isFriend(String name) {
		boolean isFriend = false;
		for (Friend f : this.friends) {
			if (f.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
				isFriend = true;
				break;
			}
		}
		return isFriend;
	}
	
}
