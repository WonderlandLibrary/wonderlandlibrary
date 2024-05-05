/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;

public class FileResourcePack
extends AbstractResourcePack
implements Closeable {
    public static final Splitter entryNameSplitter = Splitter.on('/').omitEmptyStrings().limit(3);
    private ZipFile resourcePackZipFile;

    public FileResourcePack(File resourcePackFileIn) {
        super(resourcePackFileIn);
    }

    private ZipFile getResourcePackZipFile() throws IOException {
        if (this.resourcePackZipFile != null) return this.resourcePackZipFile;
        this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
        return this.resourcePackZipFile;
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        ZipFile zipfile = this.getResourcePackZipFile();
        ZipEntry zipentry = zipfile.getEntry(name);
        if (zipentry != null) return zipfile.getInputStream(zipentry);
        throw new ResourcePackFileNotFoundException(this.resourcePackFile, name);
    }

    @Override
    public boolean hasResourceName(String name) {
        try {
            if (this.getResourcePackZipFile().getEntry(name) == null) return false;
            return true;
        }
        catch (IOException var3) {
            return false;
        }
    }

    @Override
    public Set<String> getResourceDomains() {
        ZipFile zipfile;
        try {
            zipfile = this.getResourcePackZipFile();
        }
        catch (IOException var8) {
            return Collections.emptySet();
        }
        Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
        HashSet<String> set = Sets.newHashSet();
        while (enumeration.hasMoreElements()) {
            ArrayList<String> list;
            ZipEntry zipentry = enumeration.nextElement();
            String s = zipentry.getName();
            if (!s.startsWith("assets/") || (list = Lists.newArrayList(entryNameSplitter.split(s))).size() <= 1) continue;
            String s1 = (String)list.get(1);
            if (!s1.equals(s1.toLowerCase())) {
                this.logNameNotLowercase(s1);
                continue;
            }
            set.add(s1);
        }
        return set;
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public void close() throws IOException {
        if (this.resourcePackZipFile == null) return;
        this.resourcePackZipFile.close();
        this.resourcePackZipFile = null;
    }
}

