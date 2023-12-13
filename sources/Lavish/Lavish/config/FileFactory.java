// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.config;

import net.minecraft.client.Minecraft;
import com.google.gson.GsonBuilder;
import java.io.File;
import com.google.gson.Gson;
import Lavish.utils.misc.Container;

public class FileFactory extends Container<IFile>
{
    private final Gson GSON;
    private File root;
    
    public FileFactory() {
        this.GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    }
    
    @Override
    public void add(final IFile item) {
        item.setFile(this.root);
        super.add(item);
    }
    
    public void saveFile(final Class<? extends IFile> iFile) {
        final IFile file = this.findByClass(iFile);
        if (file != null) {
            file.save(this.GSON);
        }
    }
    
    public void loadFile(final Class<? extends IFile> iFile) {
        final IFile file = this.findByClass(iFile);
        if (file != null) {
            file.load(this.GSON);
        }
    }
    
    public void save() {
        System.out.println("Saving Modules");
        this.forEach(file -> file.save(this.GSON));
    }
    
    public void load() {
        System.out.println("Loading Modules");
        this.forEach(file -> file.load(this.GSON));
    }
    
    public void setupRoot() {
        this.root = new File(Minecraft.getMinecraft().mcDataDir, "Lavish");
        System.out.println("Minecraft dir: " + Minecraft.getMinecraft().mcDataDir);
        if (!this.root.exists() && !this.root.mkdirs()) {
            Minecraft.logger.warn("Failed to create the root folder \"" + this.root.getPath() + "\".");
        }
    }
}
