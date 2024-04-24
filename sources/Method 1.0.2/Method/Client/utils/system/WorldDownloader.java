package Method.Client.utils.system;

import Method.Client.utils.visual.ChatUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.GameType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldDownloader {
    public AnvilChunkLoader newdownload;
    public boolean Saving = false;
    public File SaveDir;
    public String Savename;

    public  ArrayList<Chunk> chunks = new ArrayList<>();

    @Subscribe
    public void ChunkeventLOAD(ChunkEvent.Load event) {
        if (Saving) {
            chunks.add(event.getChunk());
        }
    }


    public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata, File file) {
        return new AnvilSaveHandler(file, saveName, storePlayerdata, new DataFixer(Wrapper.mc.getDataFixer().version));
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Saving) {
            ArrayList<Chunk> remove = new ArrayList<>();
            for (Chunk option : chunks) {
                if (Wrapper.mc.world.isChunkGeneratedAt(option.x, option.z))
                    try {
                        newdownload.saveChunk(Wrapper.INSTANCE.world(), Wrapper.mc.world.getChunk(option.x, option.z));
                    } catch (MinecraftException | IOException e) {
                        e.printStackTrace();
                    }
                if (newdownload.isChunkGeneratedAt(option.x, option.z)) {
                    remove.add(option);
                }
            }
            chunks.removeAll(remove);
        }
    }

    public void start() {
        ChatUtils.message("Starting world download");
        Savename = "Download Time" + (int) System.currentTimeMillis() / 1000;
        SaveDir = new File(new File(Wrapper.INSTANCE.mc().gameDir, "saves").getAbsolutePath(), Savename);

        if (!SaveDir.exists())
            SaveDir.mkdir();
        else {
            ChatUtils.error("This file already Exists?");
            ChatUtils.message(SaveDir.getPath());
            return;
        }
        newdownload = new AnvilChunkLoader(new File(SaveDir.getAbsolutePath()), Wrapper.mc.getDataFixer());
        Saving = true;
        chunks.addAll(Wrapper.mc.world.getChunkProvider().loadedChunks.values());
        ChatUtils.message(".minecraft/saves/" + Savename);
    }

    protected NBTTagList newDoubleNBTList(double... numbers) {
        NBTTagList nbttaglist = new NBTTagList();

        for (double d0 : numbers) {
            nbttaglist.appendTag(new NBTTagDouble(d0));
        }

        return nbttaglist;
    }

    public void stop() {
        ChatUtils.message("Stopped world download");
        newdownload.flush();
        Saving = false;
        ISaveHandler isavehandler = getSaveLoader(Savename, true, new File(Wrapper.INSTANCE.mc().gameDir, "saves"));
        WorldInfo info = new WorldInfo(Wrapper.mc.world.getWorldInfo());
        NBTTagCompound player = new NBTTagCompound();
        Wrapper.mc.player.writeToNBT(player);

        info.setSpawn(Wrapper.mc.player.getPosition());
        info.playerTag = player;
        try {
            player.setTag("Pos", newDoubleNBTList(Wrapper.mc.player.posX, Wrapper.mc.player.posY, Wrapper.mc.player.posZ));
        } catch (Exception ignored) {
        }

        info.setAllowCommands(true);
        info.setGameType(GameType.CREATIVE);
        info.generatorOptions = "3;minecraft:air;127;decoration";
        info.setTerrainType(WorldType.FLAT);
        isavehandler.saveWorldInfoWithPlayer(info, player);

        if (newdownload.getPendingSaveCount() > 1) {
            ChatUtils.warning("There are still: " + newdownload.getPendingSaveCount() + " Chunks Pending to be saved");
        }
        ChatUtils.message("Finished Download!");
    }


}
