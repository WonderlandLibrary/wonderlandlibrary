package org.dreamcore.client.files.impl;

import org.dreamcore.client.dreamcore;
import org.dreamcore.client.files.FileManager;
import org.dreamcore.client.friend.Friend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FriendConfig extends FileManager.CustomFile {

    public FriendConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                dreamcore.instance.friendManager.addFriend(name);
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Friend friend : dreamcore.instance.friendManager.getFriends()) {
                out.write(friend.getName().replace(" ", ""));
                out.write("\r\n");
            }
            out.close();
        } catch (Exception e) {

        }
    }
}
