package com.canon.majik.api.core;

import com.canon.majik.api.utils.Globals;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class PlayerManager implements Globals {
    ArrayList<String> friends;
    ArrayList<String> enemies;

    public PlayerManager(){
        MinecraftForge.EVENT_BUS.register(this);
    }

    public ArrayList<String> getEnemies() {
        return enemies;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void addFriend(String name){
        friends.add(name);
    }

    public void removeFriend(String name){
        friends.remove(name);
    }

    public void addEnemy(String name){
        enemies.add(name);
    }

    public void removeEnemy(String name){
        enemies.remove(name);
    }
}
