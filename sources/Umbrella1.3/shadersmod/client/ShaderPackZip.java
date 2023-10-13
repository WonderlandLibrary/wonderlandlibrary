/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import optifine.StrUtils;
import shadersmod.client.IShaderPack;

public class ShaderPackZip
implements IShaderPack {
    protected File packFile;
    protected ZipFile packZipFile;

    public ShaderPackZip(String name, File file) {
        this.packFile = file;
        this.packZipFile = null;
    }

    @Override
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

    @Override
    public InputStream getResourceAsStream(String resName) {
        try {
            String excp;
            ZipEntry entry;
            if (this.packZipFile == null) {
                this.packZipFile = new ZipFile(this.packFile);
            }
            return (entry = this.packZipFile.getEntry(excp = StrUtils.removePrefix(resName, "/"))) == null ? null : this.packZipFile.getInputStream(entry);
        }
        catch (Exception var4) {
            return null;
        }
    }

    @Override
    public boolean hasDirectory(String resName) {
        try {
            String e;
            ZipEntry entry;
            if (this.packZipFile == null) {
                this.packZipFile = new ZipFile(this.packFile);
            }
            return (entry = this.packZipFile.getEntry(e = StrUtils.removePrefix(resName, "/"))) != null;
        }
        catch (IOException var4) {
            return false;
        }
    }

    @Override
    public String getName() {
        return this.packFile.getName();
    }
}

