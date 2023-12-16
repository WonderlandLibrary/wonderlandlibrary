// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.Deque;
import com.google.common.base.Joiner;
import net.minecraft.src.Config;
import java.util.ArrayDeque;
import java.util.zip.ZipEntry;
import net.optifine.util.StrUtils;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.io.File;

public class ShaderPackZip implements IShaderPack
{
    protected File packFile;
    protected ZipFile packZipFile;
    protected String baseFolder;
    
    public ShaderPackZip(final String name, final File file) {
        this.packFile = file;
        this.packZipFile = null;
        this.baseFolder = "";
    }
    
    @Override
    public void close() {
        if (this.packZipFile != null) {
            try {
                this.packZipFile.close();
            }
            catch (final Exception ex) {}
            this.packZipFile = null;
        }
    }
    
    @Override
    public InputStream getResourceAsStream(final String resName) {
        try {
            if (this.packZipFile == null) {
                this.packZipFile = new ZipFile(this.packFile);
                this.baseFolder = this.detectBaseFolder(this.packZipFile);
            }
            String s = StrUtils.removePrefix(resName, "/");
            if (s.contains("..")) {
                s = this.resolveRelative(s);
            }
            final ZipEntry zipentry = this.packZipFile.getEntry(String.valueOf(this.baseFolder) + s);
            return (zipentry == null) ? null : this.packZipFile.getInputStream(zipentry);
        }
        catch (final Exception var4) {
            return null;
        }
    }
    
    private String resolveRelative(final String name) {
        final Deque<String> deque = new ArrayDeque<String>();
        final String[] astring = Config.tokenize(name, "/");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.equals("..")) {
                if (deque.isEmpty()) {
                    return "";
                }
                deque.removeLast();
            }
            else {
                deque.add(s);
            }
        }
        final String s2 = Joiner.on('/').join((Iterable)deque);
        return s2;
    }
    
    private String detectBaseFolder(final ZipFile zip) {
        final ZipEntry zipentry = zip.getEntry("shaders/");
        if (zipentry != null && zipentry.isDirectory()) {
            return "";
        }
        final Pattern pattern = Pattern.compile("([^/]+/)shaders/");
        final Enumeration<? extends ZipEntry> enumeration = zip.entries();
        while (enumeration.hasMoreElements()) {
            final ZipEntry zipentry2 = (ZipEntry)enumeration.nextElement();
            final String s = zipentry2.getName();
            final Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                final String s2 = matcher.group(1);
                if (s2 == null) {
                    continue;
                }
                if (s2.equals("shaders/")) {
                    return "";
                }
                return s2;
            }
        }
        return "";
    }
    
    @Override
    public boolean hasDirectory(final String resName) {
        try {
            if (this.packZipFile == null) {
                this.packZipFile = new ZipFile(this.packFile);
                this.baseFolder = this.detectBaseFolder(this.packZipFile);
            }
            final String s = StrUtils.removePrefix(resName, "/");
            final ZipEntry zipentry = this.packZipFile.getEntry(String.valueOf(this.baseFolder) + s);
            return zipentry != null;
        }
        catch (final IOException var4) {
            return false;
        }
    }
    
    @Override
    public String getName() {
        return this.packFile.getName();
    }
}
