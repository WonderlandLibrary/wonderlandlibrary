/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure.template;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.gen.structure.template.Template;
import org.apache.commons.io.IOUtils;

public class TemplateManager {
    private final Map<String, Template> templates = Maps.newHashMap();
    private final String baseFolder;
    private final DataFixer field_191154_c;

    public TemplateManager(String p_i47239_1_, DataFixer p_i47239_2_) {
        this.baseFolder = p_i47239_1_;
        this.field_191154_c = p_i47239_2_;
    }

    public Template getTemplate(@Nullable MinecraftServer server, ResourceLocation id) {
        Template template = this.get(server, id);
        if (template == null) {
            template = new Template();
            this.templates.put(id.getPath(), template);
        }
        return template;
    }

    @Nullable
    public Template get(@Nullable MinecraftServer p_189942_1_, ResourceLocation p_189942_2_) {
        String s = p_189942_2_.getPath();
        if (this.templates.containsKey(s)) {
            return this.templates.get(s);
        }
        if (p_189942_1_ == null) {
            this.readTemplateFromJar(p_189942_2_);
        } else {
            this.readTemplate(p_189942_2_);
        }
        return this.templates.containsKey(s) ? this.templates.get(s) : null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean readTemplate(ResourceLocation server) {
        boolean bl;
        String s = server.getPath();
        File file1 = new File(this.baseFolder, s + ".nbt");
        if (!file1.exists()) {
            return this.readTemplateFromJar(server);
        }
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(file1);
            this.readTemplateFromStream(s, inputstream);
            bl = true;
        }
        catch (Throwable var10) {
            boolean flag;
            try {
                flag = false;
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(inputstream);
                throw throwable;
            }
            IOUtils.closeQuietly(inputstream);
            return flag;
        }
        IOUtils.closeQuietly(inputstream);
        return bl;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean readTemplateFromJar(ResourceLocation id) {
        boolean bl;
        String s = id.getNamespace();
        String s1 = id.getPath();
        InputStream inputstream = null;
        try {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s1 + ".nbt");
            this.readTemplateFromStream(s1, inputstream);
            bl = true;
        }
        catch (Throwable var10) {
            try {
                boolean flag = false;
                return flag;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            finally {
                IOUtils.closeQuietly(inputstream);
            }
        }
        IOUtils.closeQuietly(inputstream);
        return bl;
    }

    private void readTemplateFromStream(String id, InputStream stream) throws IOException {
        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(stream);
        if (!nbttagcompound.hasKey("DataVersion", 99)) {
            nbttagcompound.setInteger("DataVersion", 500);
        }
        Template template = new Template();
        template.read(this.field_191154_c.process(FixTypes.STRUCTURE, nbttagcompound));
        this.templates.put(id, template);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean writeTemplate(@Nullable MinecraftServer server, ResourceLocation id) {
        boolean bl;
        String s = id.getPath();
        if (server == null || !this.templates.containsKey(s)) return false;
        File file1 = new File(this.baseFolder);
        if (!file1.exists() ? !file1.mkdirs() : !file1.isDirectory()) {
            return false;
        }
        File file2 = new File(file1, s + ".nbt");
        Template template = this.templates.get(s);
        FileOutputStream outputstream = null;
        try {
            NBTTagCompound nbttagcompound = template.writeToNBT(new NBTTagCompound());
            outputstream = new FileOutputStream(file2);
            CompressedStreamTools.writeCompressed(nbttagcompound, outputstream);
            bl = true;
        }
        catch (Throwable var13) {
            try {
                boolean flag = false;
                return flag;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            finally {
                IOUtils.closeQuietly(outputstream);
            }
        }
        IOUtils.closeQuietly(outputstream);
        return bl;
    }

    public void remove(ResourceLocation p_189941_1_) {
        this.templates.remove(p_189941_1_.getPath());
    }
}

