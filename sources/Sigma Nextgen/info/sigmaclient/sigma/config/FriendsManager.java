package info.sigmaclient.sigma.config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class FriendsManager {
    public ArrayList<String> friends = new ArrayList<>();
    public boolean isFriend(Entity entity){
        if(!(entity instanceof PlayerEntity)) return false;
        return friends.contains(entity.getName().getUnformattedComponentText().toLowerCase());
    }
}
