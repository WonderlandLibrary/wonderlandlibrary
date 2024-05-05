// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.files.impl;

import java.util.Iterator;
import ru.fluger.client.friend.Friend;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import ru.fluger.client.Fluger;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import ru.fluger.client.files.FileManager;

public class FriendConfig extends FileManager.CustomFile
{
    public FriendConfig(final String name, final boolean loadOnStart) {
        super(name, loadOnStart);
    }
    
    @Override
    public void loadFile() {
        try {
            final BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String name = curLine.split(":")[0];
                if (Fluger.instance.friendManager != null) {
                    Fluger.instance.friendManager.addFriend(name);
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveFile() {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (final Friend friend : Fluger.instance.friendManager.getFriends()) {
                out.write(friend.getName().replace(" ", ""));
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
