package me.rhys.base.friend;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendManager {
  private List<Friend> friendList = Collections.synchronizedList(new CopyOnWriteArrayList<>());
  
  public List<Friend> getFriendList() {
    return this.friendList;
  }
  
  public void removeFriend(String name) {
    this.friendList.remove(getFriend(name));
  }
  
  public void addFriend(String name) {
    this.friendList.add(new Friend(name, null));
  }
  
  public void addFriend(String name, String alias) {
    this.friendList.add(new Friend(name, alias));
  }
  
  public void changeName(String oldName, String newName) {
    getFriend(oldName).setName(newName);
  }
  
  public void changeAlias(String name, String alias) {
    getFriend(name).setAlias(alias);
  }
  
  public Friend getFriend(String name) {
    return this.friendList.parallelStream()
      .filter(friend -> friend.getName().equalsIgnoreCase(name))
      
      .findFirst().orElse(null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\friend\FriendManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */