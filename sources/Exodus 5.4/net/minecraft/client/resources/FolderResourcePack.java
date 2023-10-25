/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  org.apache.commons.io.filefilter.DirectoryFileFilter
 */
package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.resources.AbstractResourcePack;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class FolderResourcePack
extends AbstractResourcePack {
    @Override
    public Set<String> getResourceDomains() {
        HashSet hashSet = Sets.newHashSet();
        File file = new File(this.resourcePackFile, "assets/");
        if (file.isDirectory()) {
            File[] fileArray = file.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY);
            int n = fileArray.length;
            int n2 = 0;
            while (n2 < n) {
                File file2 = fileArray[n2];
                String string = FolderResourcePack.getRelativeName(file, file2);
                if (!string.equals(string.toLowerCase())) {
                    this.logNameNotLowercase(string);
                } else {
                    hashSet.add(string.substring(0, string.length() - 1));
                }
                ++n2;
            }
        }
        return hashSet;
    }

    @Override
    protected InputStream getInputStreamByName(String string) throws IOException {
        return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, string)));
    }

    public FolderResourcePack(File file) {
        super(file);
    }

    @Override
    protected boolean hasResourceName(String string) {
        return new File(this.resourcePackFile, string).isFile();
    }
}

