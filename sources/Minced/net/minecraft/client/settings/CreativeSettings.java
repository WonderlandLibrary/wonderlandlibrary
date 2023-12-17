// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.settings;

import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.File;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class CreativeSettings
{
    private static final Logger LOGGER;
    protected Minecraft minecraft;
    private final File dataFile;
    private final HotbarSnapshot[] hotbarSnapshots;
    
    public CreativeSettings(final Minecraft minecraftIn, final File dataDir) {
        this.hotbarSnapshots = new HotbarSnapshot[9];
        this.minecraft = minecraftIn;
        this.dataFile = new File(dataDir, "hotbar.nbt");
        for (int i = 0; i < 9; ++i) {
            this.hotbarSnapshots[i] = new HotbarSnapshot();
        }
        this.read();
    }
    
    public void read() {
        try {
            final NBTTagCompound nbttagcompound = CompressedStreamTools.read(this.dataFile);
            if (nbttagcompound == null) {
                return;
            }
            for (int i = 0; i < 9; ++i) {
                this.hotbarSnapshots[i].fromTag(nbttagcompound.getTagList(String.valueOf(i), 10));
            }
        }
        catch (Exception exception) {
            CreativeSettings.LOGGER.error("Failed to load creative mode options", (Throwable)exception);
        }
    }
    
    public void write() {
        try {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            for (int i = 0; i < 9; ++i) {
                nbttagcompound.setTag(String.valueOf(i), this.hotbarSnapshots[i].createTag());
            }
            CompressedStreamTools.write(nbttagcompound, this.dataFile);
        }
        catch (Exception exception) {
            CreativeSettings.LOGGER.error("Failed to save creative mode options", (Throwable)exception);
        }
    }
    
    public HotbarSnapshot getHotbarSnapshot(final int index) {
        return this.hotbarSnapshots[index];
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
