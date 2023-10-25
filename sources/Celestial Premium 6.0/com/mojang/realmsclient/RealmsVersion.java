/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RealmsVersion {
    private static String version;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getVersion() {
        if (version != null) {
            return version;
        }
        BufferedReader reader = null;
        try {
            InputStream versionStream = RealmsVersion.class.getResourceAsStream("/version");
            reader = new BufferedReader(new InputStreamReader(versionStream));
            version = reader.readLine();
            reader.close();
            String string = version;
            return string;
        }
        catch (Exception exception) {
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException iOException) {}
            }
        }
        return null;
    }
}

