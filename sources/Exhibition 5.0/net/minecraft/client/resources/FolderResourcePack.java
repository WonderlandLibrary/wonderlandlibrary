// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.util.HashSet;
import java.io.FileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import com.google.common.collect.Sets;
import java.util.Set;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class FolderResourcePack extends AbstractResourcePack
{
    private static final String __OBFID = "CL_00001076";
    
    public FolderResourcePack(final File p_i1291_1_) {
        super(p_i1291_1_);
    }
    
    @Override
    protected InputStream getInputStreamByName(final String p_110591_1_) throws IOException {
        return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, p_110591_1_)));
    }
    
    @Override
    protected boolean hasResourceName(final String p_110593_1_) {
        return new File(this.resourcePackFile, p_110593_1_).isFile();
    }
    
    @Override
    public Set getResourceDomains() {
        final HashSet var1 = Sets.newHashSet();
        final File var2 = new File(this.resourcePackFile, "assets/");
        if (var2.isDirectory()) {
            for (final File var6 : var2.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY)) {
                final String var7 = AbstractResourcePack.getRelativeName(var2, var6);
                if (!var7.equals(var7.toLowerCase())) {
                    this.logNameNotLowercase(var7);
                }
                else {
                    var1.add(var7.substring(0, var7.length() - 1));
                }
            }
        }
        return var1;
    }
}
