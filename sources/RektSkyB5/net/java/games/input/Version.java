/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Version {
    private Version() {
    }

    public static String getVersion() {
        String version = "Unversioned";
        try {
            Properties p2 = new Properties();
            InputStream is = Version.class.getResourceAsStream("/META-INF/maven/net.java.jinput/coreapi/pom.properties");
            if (is != null) {
                p2.load(is);
                version = p2.getProperty("version", "");
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return version;
    }
}

