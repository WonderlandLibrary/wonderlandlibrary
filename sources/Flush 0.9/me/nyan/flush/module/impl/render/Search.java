package me.nyan.flush.module.impl.render;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Search extends Module {
    public Search() {
        super("Search", Category.RENDER);
    }

    private Thread searchThread;

    private final CopyOnWriteArrayList<BlockPos> positions = new CopyOnWriteArrayList<>();
    private final HashMap<Block, Integer> blocks = new HashMap<>();
    private final File dataFile = new File(Flush.getClientPath(), "search.json");

    private final NumberSetting range = new NumberSetting("Range", this, 40, 10, 100);

    @Override
    public void onEnable() {
        super.onEnable();
        load();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        positions.clear();
        save();
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (searchThread == null || searchThread.getState() == Thread.State.NEW ||
                searchThread.getState() == Thread.State.TERMINATED) {
            searchThread = new SearchThread();
            searchThread.setName(getName() + " Thread");
            searchThread.setDaemon(true);
            searchThread.start();
        }
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        if (blocks.isEmpty()) {
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();

        for (BlockPos position : positions) {
            if (!blocks.containsKey(position.getBlock())) {
                continue;
            }

            Block block = position.getBlock();
            RenderUtils.drawBlockBox(position, e.getPartialTicks(), ColorUtils.alpha(blocks.get(block), 140));
            RenderUtils.drawBlockOutline(position, e.getPartialTicks(), ColorUtils.darker(blocks.get(block)));
        }

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        RenderUtils.glColor(-1);
    }

    public void save() {
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JsonWriter writer = new JsonWriter(new PrintWriter(dataFile));
            writer.setIndent("  ");

            writer.beginArray();
            for (Map.Entry<Block, Integer> entry : blocks.entrySet()) {
                writer.beginObject();
                writer.name("blockId").value(Block.getIdFromBlock(entry.getKey()));
                writer.name("color").value(entry.getValue());
                writer.endObject();
            }
            writer.endArray();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!dataFile.exists()) {
            return;
        }

        blocks.clear();

        try {
            JsonArray json = new JsonParser().parse(new BufferedReader(new FileReader(dataFile)))
                    .getAsJsonArray();

            for (JsonElement element : json) {
                if (element instanceof JsonObject) {
                    JsonObject object = element.getAsJsonObject();
                    Block block = Block.getBlockById(object.get("blockId").getAsInt());
                    if (block == null || Item.getItemFromBlock(block) == null || block.getMaterial().isLiquid() ||
                            new ItemStack(block).getDisplayName().contains("Chiseled")) {
                        continue;
                    }
                    blocks.put(
                            block,
                            object.get("color").getAsInt()
                    );
                }
            }
        } catch (IllegalStateException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Block, Integer> getBlocks() {
        return blocks;
    }

    private class SearchThread extends Thread {
        @Override
        public void run() {
            if (blocks.isEmpty()) {
                return;
            }

            if (mc.thePlayer == null || mc.theWorld == null) {
                positions.clear();
                return;
            }

            int range = Search.this.range.getValueInt();

            ArrayList<BlockPos> outOfRange = new ArrayList<>();
            for (BlockPos position : positions) {
                if (MathHelper.sqrt_double(mc.thePlayer.getDistance(position.getX(), position.getY(), position.getZ())) > range) {
                    outOfRange.add(position);
                }
            }
            positions.removeAll(outOfRange);

            for (int x = -range; x < range; x++) {
                for (int y = range; y > -range; y--) {
                    for (int z = -range; z < range; z++) {
                        BlockPos pos = new BlockPos(mc.thePlayer.posX + x,
                                mc.thePlayer.posY + y,
                                mc.thePlayer.posZ + z);
                        if (MathHelper.sqrt_double(mc.thePlayer
                                .getDistance(pos.getX(), pos.getY(), pos.getZ())) <= range &&
                                blocks.containsKey(pos.getBlock()) && !positions.contains(pos)) {
                            positions.add(pos);
                        }
                    }
                }
            }
        }
    }
}
