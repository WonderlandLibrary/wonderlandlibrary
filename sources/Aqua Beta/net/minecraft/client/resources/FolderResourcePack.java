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
    public FolderResourcePack(File resourcePackFileIn) {
        super(resourcePackFileIn);
    }

    protected InputStream getInputStreamByName(String name) throws IOException {
        return new BufferedInputStream((InputStream)new FileInputStream(new File(this.resourcePackFile, name)));
    }

    protected boolean hasResourceName(String name) {
        return new File(this.resourcePackFile, name).isFile();
    }

    public Set<String> getResourceDomains() {
        HashSet set = Sets.newHashSet();
        File file1 = new File(this.resourcePackFile, "assets/");
        if (file1.isDirectory()) {
            for (File file2 : file1.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY)) {
                String s = FolderResourcePack.getRelativeName((File)file1, (File)file2);
                if (!s.equals((Object)s.toLowerCase())) {
                    this.logNameNotLowercase(s);
                    continue;
                }
                set.add((Object)s.substring(0, s.length() - 1));
            }
        }
        return set;
    }
}
