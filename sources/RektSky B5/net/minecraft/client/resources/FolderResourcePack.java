/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.resources.AbstractResourcePack;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class FolderResourcePack
extends AbstractResourcePack {
    public FolderResourcePack(File resourcePackFileIn) {
        super(resourcePackFileIn);
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, name)));
    }

    @Override
    protected boolean hasResourceName(String name) {
        return new File(this.resourcePackFile, name).isFile();
    }

    @Override
    public Set<String> getResourceDomains() {
        HashSet<String> set = Sets.newHashSet();
        File file1 = new File(this.resourcePackFile, "assets/");
        if (file1.isDirectory()) {
            for (File file2 : file1.listFiles(DirectoryFileFilter.DIRECTORY)) {
                String s2 = FolderResourcePack.getRelativeName(file1, file2);
                if (!s2.equals(s2.toLowerCase())) {
                    this.logNameNotLowercase(s2);
                    continue;
                }
                set.add(s2.substring(0, s2.length() - 1));
            }
        }
        return set;
    }
}

