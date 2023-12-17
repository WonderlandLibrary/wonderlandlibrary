// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import net.minecraft.util.Util;
import java.util.Locale;
import java.io.FileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import com.google.common.base.CharMatcher;

public class FolderResourcePack extends AbstractResourcePack
{
    private static final boolean ON_WINDOWS;
    private static final CharMatcher BACKSLASH_MATCHER;
    
    public FolderResourcePack(final File resourcePackFileIn) {
        super(resourcePackFileIn);
    }
    
    protected static boolean validatePath(final File p_191384_0_, final String p_191384_1_) throws IOException {
        String s = p_191384_0_.getCanonicalPath();
        if (FolderResourcePack.ON_WINDOWS) {
            s = FolderResourcePack.BACKSLASH_MATCHER.replaceFrom((CharSequence)s, '/');
        }
        return s.endsWith(p_191384_1_);
    }
    
    @Override
    protected InputStream getInputStreamByName(final String name) throws IOException {
        final File file1 = this.getFile(name);
        if (file1 == null) {
            throw new ResourcePackFileNotFoundException(this.resourcePackFile, name);
        }
        return new BufferedInputStream(new FileInputStream(file1));
    }
    
    @Override
    protected boolean hasResourceName(final String name) {
        return this.getFile(name) != null;
    }
    
    @Nullable
    private File getFile(final String p_191385_1_) {
        try {
            final File file1 = new File(this.resourcePackFile, p_191385_1_);
            if (file1.isFile() && validatePath(file1, p_191385_1_)) {
                return file1;
            }
        }
        catch (IOException ex) {}
        return null;
    }
    
    @Override
    public Set<String> getResourceDomains() {
        final Set<String> set = (Set<String>)Sets.newHashSet();
        final File file1 = new File(this.resourcePackFile, "assets/");
        if (file1.isDirectory()) {
            for (final File file2 : file1.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY)) {
                final String s = AbstractResourcePack.getRelativeName(file1, file2);
                if (s.equals(s.toLowerCase(Locale.ROOT))) {
                    set.add(s.substring(0, s.length() - 1));
                }
                else {
                    this.logNameNotLowercase(s);
                }
            }
        }
        return set;
    }
    
    static {
        ON_WINDOWS = (Util.getOSType() == Util.EnumOS.WINDOWS);
        BACKSLASH_MATCHER = CharMatcher.is('\\');
    }
}
