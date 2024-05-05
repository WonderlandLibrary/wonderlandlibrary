package space.lunaclient.luna.impl.managers;

import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.util.StringUtils;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.file.CustomFile;
import space.lunaclient.luna.api.friend.Friend;
import space.lunaclient.luna.api.manager.Manager;
import space.lunaclient.luna.impl.files.FriendsFile;

public class FriendManager
  extends Manager<Friend>
{
  public FriendManager() {}
  
  public void addFriend(String name, String alias)
  {
    getContents().add(new Friend(name, alias));
    saveFile();
  }
  
  public void deleteFriend(String name)
  {
    for (Friend friend : getContents()) {
      if ((friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))) || (friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name))))
      {
        getContents().remove(friend);
        saveFile();
        break;
      }
    }
  }
  
  public static boolean isFriend(String name)
  {
    for (Friend friend : Luna.INSTANCE.FRIEND_MANAGER.getContents()) {
      if ((friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))) || (friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name)))) {
        return true;
      }
    }
    return false;
  }
  
  private void saveFile()
  {
    try
    {
      Luna.INSTANCE.FILE_MANAGER.getFile(FriendsFile.class).saveFile();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
