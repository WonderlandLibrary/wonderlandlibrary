package dev.zprestige.prestige.client.managers;

import java.awt.*;
import java.util.ArrayList;

public class SocialsManager {
    public ArrayList<String> friends = new ArrayList<>();
    public ArrayList<String> enemies = new ArrayList<>();

    public Color getColor(String string, Color color) {
        Color color2;
        if (isFriend(string)) {
            color2 = Color.CYAN;
        } else if (isEnemy(string)) {
            color2 = Color.RED;
        } else {
            color2 = color;
        }
        return color2;
    }

    public Color getColor(String string) {
        Color color;
        if (isFriend(string)) {
            color = Color.CYAN;
        } else if (isEnemy(string)) {
            color = Color.RED;
        } else {
            color = Color.WHITE;
        }
        return color;
    }

    public void addFriend(String string) {
        friends.add(string);
    }

    public void removeFriend(String string) {
        friends.remove(string);
    }

    public boolean isFriend(String string) {
        return friends.contains(string);
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void addEnemy(String string) {
        enemies.add(string);
    }

    public void removeEnemy(String string) {
        enemies.remove(string);
    }

    public boolean isEnemy(String string) {
        return enemies.contains(string);
    }

    public ArrayList<String> getEnemies() {
        return enemies;
    }

    public int getType(String string) {
        return isFriend(string) ? 1 : (isEnemy(string) ? -1 : 0);
    }
}
