// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.List;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;

public class DataManager
{
    private final Lock threadLock;
    private final Lock waypointLock;
    private static final Lock identityLock;
    public static LinkedHashMap<String, PlayerIdentity> identityCacheMap;
    
    public DataManager() {
        this.threadLock = new ReentrantLock();
        this.waypointLock = new ReentrantLock();
    }
    
    public static synchronized void savePlayerIdentity(final PlayerIdentity id, final boolean delete) throws IOException {
        DataManager.identityLock.lock();
        try {
            final File dir = new File("playeridentitycache");
            if (!dir.exists()) {
                dir.mkdir();
            }
            final File f = new File("playeridentitycache/" + id.getStringUuid() + ".mcid");
            if (f.exists() || delete) {
                f.delete();
                if (delete) {
                    return;
                }
            }
            final FileOutputStream fstream = new FileOutputStream(f);
            final ObjectOutputStream stream = new ObjectOutputStream(fstream);
            stream.writeObject(id);
            stream.close();
            fstream.close();
        }
        finally {
            DataManager.identityLock.unlock();
        }
    }
    
    public static PlayerIdentity getPlayerIdentity(final String UUID) {
        if (DataManager.identityCacheMap.containsKey(UUID)) {
            return DataManager.identityCacheMap.get(UUID);
        }
        return new PlayerIdentity(UUID);
    }
    
    public synchronized void loadPlayerIdentities() throws IOException {
        DataManager.identityLock.lock();
        try {
            final File f = new File("playeridentitycache");
            if (!f.exists()) {
                return;
            }
            if (!f.isDirectory()) {
                f.delete();
                return;
            }
            final List<File> files = Arrays.asList(f.listFiles());
            FileInputStream inputStream;
            ObjectInputStream objectInputStream;
            Object wayptObj;
            final Exception ex2;
            Exception ex;
            files.stream().filter(file -> file.getName().endsWith(".mcid")).forEach(wyptFile -> {
                try {
                    inputStream = new FileInputStream(wyptFile);
                    objectInputStream = new ObjectInputStream(inputStream);
                    wayptObj = objectInputStream.readObject();
                    if (wayptObj instanceof PlayerIdentity) {
                        DataManager.identityCacheMap.put(((PlayerIdentity)wayptObj).getStringUuid(), (PlayerIdentity)wayptObj);
                        objectInputStream.close();
                        inputStream.close();
                    }
                    else {
                        objectInputStream.close();
                        inputStream.close();
                    }
                }
                catch (IOException | ClassNotFoundException ex3) {
                    ex = ex2;
                    ex.printStackTrace();
                }
            });
        }
        finally {
            DataManager.identityLock.unlock();
        }
    }
    
    static {
        identityLock = new ReentrantLock();
        DataManager.identityCacheMap = new LinkedHashMap<String, PlayerIdentity>();
    }
}
