/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
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
    public static final Splitter entryNameSplitter = Splitter.on((char)'/').omitEmptyStrings().limit(3);
    private ZipFile resourcePackZipFile;

    @Override
    public void close() throws IOException {
        if (this.resourcePackZipFile != null) {
            this.resourcePackZipFile.close();
            this.resourcePackZipFile = null;
        }
    }

    private ZipFile getResourcePackZipFile() throws IOException {
        if (this.resourcePackZipFile == null) {
            this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
        }
        return this.resourcePackZipFile;
    }

    @Override
    protected InputStream getInputStreamByName(String string) throws IOException {
        ZipFile zipFile = this.getResourcePackZipFile();
        ZipEntry zipEntry = zipFile.getEntry(string);
        if (zipEntry == null) {
            throw new ResourcePackFileNotFoundException(this.resourcePackFile, string);
        }
        return zipFile.getInputStream(zipEntry);
    }

    public FileResourcePack(File file) {
        super(file);
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public Set<String> getResourceDomains() {
        ZipFile zipFile;
        try {
            zipFile = this.getResourcePackZipFile();
        }
        catch (IOException iOException) {
            return Collections.emptySet();
        }
        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
        HashSet hashSet = Sets.newHashSet();
        while (enumeration.hasMoreElements()) {
            ArrayList arrayList;
            ZipEntry zipEntry = enumeration.nextElement();
            String string = zipEntry.getName();
            if (!string.startsWith("assets/") || (arrayList = Lists.newArrayList((Iterable)entryNameSplitter.split((CharSequence)string))).size() <= 1) continue;
            String string2 = (String)arrayList.get(1);
            if (!string2.equals(string2.toLowerCase())) {
                this.logNameNotLowercase(string2);
                continue;
            }
            hashSet.add(string2);
        }
        return hashSet;
    }

    @Override
    public boolean hasResourceName(String string) {
        try {
            return this.getResourcePackZipFile().getEntry(string) != null;
        }
        catch (IOException iOException) {
            return false;
        }
    }
}

