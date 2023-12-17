// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure.template;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.io.File;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import com.google.common.collect.Maps;
import net.minecraft.util.datafix.DataFixer;
import java.util.Map;

public class TemplateManager
{
    private final Map<String, Template> templates;
    private final String baseFolder;
    private final DataFixer fixer;
    
    public TemplateManager(final String p_i47239_1_, final DataFixer p_i47239_2_) {
        this.templates = (Map<String, Template>)Maps.newHashMap();
        this.baseFolder = p_i47239_1_;
        this.fixer = p_i47239_2_;
    }
    
    public Template getTemplate(@Nullable final MinecraftServer server, final ResourceLocation id) {
        Template template = this.get(server, id);
        if (template == null) {
            template = new Template();
            this.templates.put(id.getPath(), template);
        }
        return template;
    }
    
    @Nullable
    public Template get(@Nullable final MinecraftServer server, final ResourceLocation templatePath) {
        final String s = templatePath.getPath();
        if (this.templates.containsKey(s)) {
            return this.templates.get(s);
        }
        if (server == null) {
            this.readTemplateFromJar(templatePath);
        }
        else {
            this.readTemplate(templatePath);
        }
        return this.templates.containsKey(s) ? this.templates.get(s) : null;
    }
    
    public boolean readTemplate(final ResourceLocation server) {
        final String s = server.getPath();
        final File file1 = new File(this.baseFolder, s + ".nbt");
        if (!file1.exists()) {
            return this.readTemplateFromJar(server);
        }
        InputStream inputstream = null;
        boolean flag;
        try {
            inputstream = new FileInputStream(file1);
            this.readTemplateFromStream(s, inputstream);
            return true;
        }
        catch (Throwable var10) {
            flag = false;
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
        return flag;
    }
    
    private boolean readTemplateFromJar(final ResourceLocation id) {
        final String s = id.getNamespace();
        final String s2 = id.getPath();
        InputStream inputstream = null;
        boolean flag;
        try {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s2 + ".nbt");
            this.readTemplateFromStream(s2, inputstream);
            return true;
        }
        catch (Throwable var10) {
            flag = false;
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
        return flag;
    }
    
    private void readTemplateFromStream(final String id, final InputStream stream) throws IOException {
        final NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(stream);
        if (!nbttagcompound.hasKey("DataVersion", 99)) {
            nbttagcompound.setInteger("DataVersion", 500);
        }
        final Template template = new Template();
        template.read(this.fixer.process(FixTypes.STRUCTURE, nbttagcompound));
        this.templates.put(id, template);
    }
    
    public boolean writeTemplate(@Nullable final MinecraftServer server, final ResourceLocation id) {
        final String s = id.getPath();
        if (server != null && this.templates.containsKey(s)) {
            final File file1 = new File(this.baseFolder);
            if (!file1.exists()) {
                if (!file1.mkdirs()) {
                    return false;
                }
            }
            else if (!file1.isDirectory()) {
                return false;
            }
            final File file2 = new File(file1, s + ".nbt");
            final Template template = this.templates.get(s);
            OutputStream outputstream = null;
            boolean flag;
            try {
                final NBTTagCompound nbttagcompound = template.writeToNBT(new NBTTagCompound());
                outputstream = new FileOutputStream(file2);
                CompressedStreamTools.writeCompressed(nbttagcompound, outputstream);
                return true;
            }
            catch (Throwable var13) {
                flag = false;
            }
            finally {
                IOUtils.closeQuietly(outputstream);
            }
            return flag;
        }
        return false;
    }
    
    public void remove(final ResourceLocation templatePath) {
        this.templates.remove(templatePath.getPath());
    }
}
