package me.nyan.flush.friend;

import net.minecraft.entity.EntityLivingBase;
import me.nyan.flush.Flush;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    private final File dataFile;
    private static final List<String> friends = new ArrayList<>();

    public boolean addFriend(String e) {
        if (!friends.contains(e)) {
            friends.add(e);
            return true;
        }
        return false;
    }

    public boolean removeFriend(String e) {
        if (friends.contains(e)) {
            friends.remove(e);
            return true;
        }

        return false;
    }

    public boolean isFriend(EntityLivingBase e) {
        return friends.contains(e.getName());
    }

    public List<String> getFriends() {
        return friends;
    }

    public FriendManager() {
        dataFile = new File(Flush.getClientPath(), "friends.txt");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            PrintWriter printWriter = new PrintWriter(dataFile);

            for (String str : getFriends())
                printWriter.println(str);

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = reader.readLine();

            while (line != null) {
                addFriend(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
