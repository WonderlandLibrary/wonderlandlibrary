// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Enumeration;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.util.zip.ZipFile;
import com.google.common.base.Splitter;
import java.io.Closeable;

public class FileResourcePack extends AbstractResourcePack implements Closeable
{
    public static final Splitter entryNameSplitter;
    private ZipFile resourcePackZipFile;
    private static final String __OBFID = "CL_00001075";
    
    public FileResourcePack(final File p_i1290_1_) {
        super(p_i1290_1_);
    }
    
    private ZipFile getResourcePackZipFile() throws IOException {
        if (this.resourcePackZipFile == null) {
            this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
        }
        return this.resourcePackZipFile;
    }
    
    @Override
    protected InputStream getInputStreamByName(final String p_110591_1_) throws IOException {
        final ZipFile var2 = this.getResourcePackZipFile();
        final ZipEntry var3 = var2.getEntry(p_110591_1_);
        if (var3 == null) {
            throw new ResourcePackFileNotFoundException(this.resourcePackFile, p_110591_1_);
        }
        return var2.getInputStream(var3);
    }
    
    public boolean hasResourceName(final String p_110593_1_) {
        try {
            return this.getResourcePackZipFile().getEntry(p_110593_1_) != null;
        }
        catch (IOException var3) {
            return false;
        }
    }
    
    @Override
    public Set getResourceDomains() {
        ZipFile var1;
        try {
            var1 = this.getResourcePackZipFile();
        }
        catch (IOException var8) {
            return Collections.emptySet();
        }
        final Enumeration var2 = var1.entries();
        final HashSet var3 = Sets.newHashSet();
        while (var2.hasMoreElements()) {
            final ZipEntry var4 = var2.nextElement();
            final String var5 = var4.getName();
            if (var5.startsWith("assets/")) {
                final ArrayList var6 = Lists.newArrayList(FileResourcePack.entryNameSplitter.split((CharSequence)var5));
                if (var6.size() <= 1) {
                    continue;
                }
                final String var7 = var6.get(1);
                if (!var7.equals(var7.toLowerCase())) {
                    this.logNameNotLowercase(var7);
                }
                else {
                    var3.add(var7);
                }
            }
        }
        return var3;
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    
    @Override
    public void close() throws IOException {
        if (this.resourcePackZipFile != null) {
            this.resourcePackZipFile.close();
            this.resourcePackZipFile = null;
        }
    }
    
    static {
        entryNameSplitter = Splitter.on('/').omitEmptyStrings().limit(3);
    }
}
