/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.vfs;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.ZipDir;

public class UrlTypeVFS
implements Vfs.UrlType {
    public static final String[] REPLACE_EXTENSION = new String[]{".ear/", ".jar/", ".war/", ".sar/", ".har/", ".par/"};
    final String VFSZIP = "vfszip";
    final String VFSFILE = "vfsfile";
    Predicate<File> realFile = file -> file.exists() && file.isFile();

    @Override
    public boolean matches(URL url) {
        return "vfszip".equals(url.getProtocol()) || "vfsfile".equals(url.getProtocol());
    }

    @Override
    public Vfs.Dir createDir(URL url) {
        try {
            URL adaptedUrl = this.adaptURL(url);
            return new ZipDir(new JarFile(adaptedUrl.getFile()));
        }
        catch (Exception e2) {
            try {
                return new ZipDir(new JarFile(url.getFile()));
            }
            catch (IOException e1) {
                if (Reflections.log != null) {
                    Reflections.log.warn("Could not get URL", (Throwable)e2);
                    Reflections.log.warn("Could not get URL", (Throwable)e1);
                }
                return null;
            }
        }
    }

    public URL adaptURL(URL url) throws MalformedURLException {
        if ("vfszip".equals(url.getProtocol())) {
            return this.replaceZipSeparators(url.getPath(), this.realFile);
        }
        if ("vfsfile".equals(url.getProtocol())) {
            return new URL(url.toString().replace("vfsfile", "file"));
        }
        return url;
    }

    URL replaceZipSeparators(String path, Predicate<File> acceptFile) throws MalformedURLException {
        int pos = 0;
        while (pos != -1) {
            File file;
            if ((pos = this.findFirstMatchOfDeployableExtention(path, pos)) <= 0 || !acceptFile.test(file = new File(path.substring(0, pos - 1)))) continue;
            return this.replaceZipSeparatorStartingFrom(path, pos);
        }
        throw new ReflectionsException("Unable to identify the real zip file in path '" + path + "'.");
    }

    int findFirstMatchOfDeployableExtention(String path, int pos) {
        Pattern p2 = Pattern.compile("\\.[ejprw]ar/");
        Matcher m2 = p2.matcher(path);
        if (m2.find(pos)) {
            return m2.end();
        }
        return -1;
    }

    URL replaceZipSeparatorStartingFrom(String path, int pos) throws MalformedURLException {
        String zipFile = path.substring(0, pos - 1);
        String zipPath = path.substring(pos);
        int numSubs = 1;
        for (String ext : REPLACE_EXTENSION) {
            while (zipPath.contains(ext)) {
                zipPath = zipPath.replace(ext, ext.substring(0, 4) + "!");
                ++numSubs;
            }
        }
        String prefix = "";
        for (int i2 = 0; i2 < numSubs; ++i2) {
            prefix = prefix + "zip:";
        }
        if (zipPath.trim().length() == 0) {
            return new URL(prefix + "/" + zipFile);
        }
        return new URL(prefix + "/" + zipFile + "!" + zipPath);
    }
}

