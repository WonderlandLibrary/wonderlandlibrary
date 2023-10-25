/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

public final class Version {
    private static final String apiVersion = "2.0.5";
    private static final String buildNumber = "1088";
    private static final String antBuildNumberToken = "@BUILD_NUMBER@";
    private static final String antAPIVersionToken = "@API_VERSION@";

    private Version() {
    }

    public static String getVersion() {
        String version = "Unversioned";
        if (!antAPIVersionToken.equals(apiVersion)) {
            version = apiVersion;
        }
        if (!antBuildNumberToken.equals(buildNumber)) {
            version = version + "-b1088";
        }
        return version;
    }
}

