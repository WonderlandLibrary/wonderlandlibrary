/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public final class Version {
    private final long buildTimeMillis;
    private static final String PROP_REPO_STATUS = ".repoStatus";
    private static final String PROP_LONG_COMMIT_HASH = ".longCommitHash";
    private final String artifactVersion;
    private static final String PROP_VERSION = ".version";
    private final String repositoryStatus;
    private static final String PROP_BUILD_DATE = ".buildDate";
    private static final String PROP_COMMIT_DATE = ".commitDate";
    private final String artifactId;
    private static final String PROP_SHORT_COMMIT_HASH = ".shortCommitHash";
    private final String shortCommitHash;
    private final long commitTimeMillis;
    private final String longCommitHash;

    public String toString() {
        return this.artifactId + '-' + this.artifactVersion + '.' + this.shortCommitHash + ("clean".equals(this.repositoryStatus) ? "" : " (repository: " + this.repositoryStatus + ')');
    }

    public String longCommitHash() {
        return this.longCommitHash;
    }

    public long buildTimeMillis() {
        return this.buildTimeMillis;
    }

    private Version(String string, String string2, long l, long l2, String string3, String string4, String string5) {
        this.artifactId = string;
        this.artifactVersion = string2;
        this.buildTimeMillis = l;
        this.commitTimeMillis = l2;
        this.shortCommitHash = string3;
        this.longCommitHash = string4;
        this.repositoryStatus = string5;
    }

    public String artifactVersion() {
        return this.artifactVersion;
    }

    public static void main(String[] stringArray) {
        for (Version version : Version.identify().values()) {
            System.err.println(version);
        }
    }

    public static Map<String, Version> identify() {
        return Version.identify(null);
    }

    private static long parseIso8601(String string) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(string).getTime();
        }
        catch (ParseException parseException) {
            return 0L;
        }
    }

    public static Map<String, Version> identify(ClassLoader classLoader) {
        String string;
        Object object3;
        TreeMap<String, Version> treeMap;
        Object object2;
        if (classLoader == null) {
            classLoader = PlatformDependent.getContextClassLoader();
        }
        Properties properties = new Properties();
        try {
            object2 = classLoader.getResources("META-INF/io.netty.versions.properties");
            while (object2.hasMoreElements()) {
                treeMap = (URL)object2.nextElement();
                object3 = ((URL)((Object)treeMap)).openStream();
                properties.load((InputStream)object3);
                try {
                    ((InputStream)object3).close();
                }
                catch (Exception exception) {}
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        object2 = new HashSet();
        for (Object object3 : properties.keySet()) {
            String string2;
            string = (String)object3;
            int n = string.indexOf(46);
            if (n <= 0 || !properties.containsKey((string2 = string.substring(0, n)) + PROP_VERSION) || !properties.containsKey(string2 + PROP_BUILD_DATE) || !properties.containsKey(string2 + PROP_COMMIT_DATE) || !properties.containsKey(string2 + PROP_SHORT_COMMIT_HASH) || !properties.containsKey(string2 + PROP_LONG_COMMIT_HASH) || !properties.containsKey(string2 + PROP_REPO_STATUS)) continue;
            object2.add(string2);
        }
        treeMap = new TreeMap<String, Version>();
        object3 = object2.iterator();
        while (object3.hasNext()) {
            string = (String)object3.next();
            treeMap.put(string, new Version(string, properties.getProperty(string + PROP_VERSION), Version.parseIso8601(properties.getProperty(string + PROP_BUILD_DATE)), Version.parseIso8601(properties.getProperty(string + PROP_COMMIT_DATE)), properties.getProperty(string + PROP_SHORT_COMMIT_HASH), properties.getProperty(string + PROP_LONG_COMMIT_HASH), properties.getProperty(string + PROP_REPO_STATUS)));
        }
        return treeMap;
    }

    public String shortCommitHash() {
        return this.shortCommitHash;
    }

    public String repositoryStatus() {
        return this.repositoryStatus;
    }

    public String artifactId() {
        return this.artifactId;
    }

    public long commitTimeMillis() {
        return this.commitTimeMillis;
    }
}

