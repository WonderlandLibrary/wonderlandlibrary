package net.optifine.shaders;

import com.google.common.base.Joiner;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.src.Config;
import net.optifine.shaders.IShaderPack;
import net.optifine.util.StrUtils;

public class ShaderPackZip
implements IShaderPack {
    protected File packFile;
    protected ZipFile packZipFile;
    protected String baseFolder;

    public ShaderPackZip(String name, File file) {
        this.packFile = file;
        this.packZipFile = null;
        this.baseFolder = "";
    }

    public void close() {
        if (this.packZipFile != null) {
            try {
                this.packZipFile.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.packZipFile = null;
        }
    }

    public InputStream getResourceAsStream(String resName) {
        try {
            ZipEntry zipentry;
            String s;
            if (this.packZipFile == null) {
                this.packZipFile = new ZipFile(this.packFile);
                this.baseFolder = this.detectBaseFolder(this.packZipFile);
            }
            if ((s = StrUtils.removePrefix((String)resName, (String)"/")).contains((CharSequence)"..")) {
                s = this.resolveRelative(s);
            }
            return (zipentry = this.packZipFile.getEntry(this.baseFolder + s)) == null ? null : this.packZipFile.getInputStream(zipentry);
        }
        catch (Exception var4) {
            return null;
        }
    }

    private String resolveRelative(String name) {
        ArrayDeque deque = new ArrayDeque();
        String[] astring = Config.tokenize((String)name, (String)"/");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            if (s.equals((Object)"..")) {
                if (deque.isEmpty()) {
                    return "";
                }
                deque.removeLast();
                continue;
            }
            deque.add((Object)s);
        }
        String s1 = Joiner.on((char)'/').join((Iterable)deque);
        return s1;
    }

    private String detectBaseFolder(ZipFile zip) {
        ZipEntry zipentry = zip.getEntry("shaders/");
        if (zipentry != null && zipentry.isDirectory()) {
            return "";
        }
        Pattern pattern = Pattern.compile((String)"([^/]+/)shaders/");
        Enumeration enumeration = zip.entries();
        while (enumeration.hasMoreElements()) {
            String s1;
            ZipEntry zipentry1 = (ZipEntry)enumeration.nextElement();
            String s = zipentry1.getName();
            Matcher matcher = pattern.matcher((CharSequence)s);
            if (!matcher.matches() || (s1 = matcher.group(1)) == null) continue;
            if (s1.equals((Object)"shaders/")) {
                return "";
            }
            return s1;
        }
        return "";
    }

    public boolean hasDirectory(String resName) {
        try {
            if (this.packZipFile == null) {
                this.packZipFile = new ZipFile(this.packFile);
                this.baseFolder = this.detectBaseFolder(this.packZipFile);
            }
            String s = StrUtils.removePrefix((String)resName, (String)"/");
            ZipEntry zipentry = this.packZipFile.getEntry(this.baseFolder + s);
            return zipentry != null;
        }
        catch (IOException var4) {
            return false;
        }
    }

    public String getName() {
        return this.packFile.getName();
    }
}
