/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.util;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import net.minecraft.realms.Realms;
import org.apache.commons.io.FileUtils;

public class RealmsPersistence {
    private static final String FILE_NAME = "realms_persistence.json";

    public static RealmsPersistenceData readFile() {
        File file = new File(Realms.getGameDirectoryPath(), FILE_NAME);
        Gson gson = new Gson();
        try {
            return gson.fromJson(FileUtils.readFileToString(file), RealmsPersistenceData.class);
        }
        catch (IOException e) {
            return new RealmsPersistenceData();
        }
    }

    public static void writeFile(RealmsPersistenceData data) {
        File file = new File(Realms.getGameDirectoryPath(), FILE_NAME);
        Gson gson = new Gson();
        String json = gson.toJson(data);
        try {
            FileUtils.writeStringToFile(file, json);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public static class RealmsPersistenceData {
        public String newsLink;
        public boolean hasUnreadNews = false;

        private RealmsPersistenceData() {
        }

        private RealmsPersistenceData(String newsLink, boolean hasUnreadNews) {
            this.newsLink = newsLink;
            this.hasUnreadNews = hasUnreadNews;
        }
    }
}

