package Method.Client.module.Onscreen.Display;

import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.OnscreenGUI;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.utils.TimerUtils;
import Method.Client.utils.visual.RenderUtils;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.Executors;
import java.util.zip.DeflaterOutputStream;

import static Method.Client.Main.setmgr;


public final class ChunkSize extends Module {
    public ChunkSize() {
        super("ChunkSize", Keyboard.KEY_NONE, Category.ONSCREEN, "ChunkSize");
    }

    static Setting Delay;
    static Setting TextColor;
    static Setting BgColor;
    static Setting xpos;
    static Setting ypos;
    static Setting Shadow;
    static Setting Frame;
    static Setting FontSize;
    static Setting Background;
    static PinableFrame pin;
    public static final TimerUtils timer = new TimerUtils();

    public static boolean running = false;
    public static long size = 0L;
    public static long previousSize = 0L;
    public static ChunkPos current = null;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(Delay = new Setting("Delay", this, 1, 1, 10, true));

        setmgr.add(TextColor = new Setting("TextColor", this, 0, 1, 1, 1));
        setmgr.add(BgColor = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Background = new Setting("Background", this, false));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 250, -20, (mc.displayHeight) + 40, true));
        pin = new ChunkSizeRUN();
        OnscreenGUI.pinableFrames.add(pin);
    }

    AnvilChunkLoader loader;

    NBTTagCompound root = null;
    NBTTagCompound level;
    DataOutputStream compressed;


    @Override
    public void onEnable() {
        PinableFrame.Toggle(pin, true);
        timer.setLastMS();
        loader = new AnvilChunkLoader(null, null);
        running = false;
        size = previousSize = 0L;
        current = null;
        root = new NBTTagCompound();
        level = new NBTTagCompound();
        root.setTag("Level", level);
        root.setInteger("DataVersion", 2021);
    }

    @Override
    public void onDisable() {
        PinableFrame.Toggle(pin, false);
    }


    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (running) {
            return;
        }

        if (event.phase == TickEvent.Phase.END) {
            Chunk chunk = mc.world.getChunk(mc.player.getPosition());
            if (chunk.isEmpty()) {
                return;
            }

            ChunkPos pos = chunk.getPos();
            if (!pos.equals(current) || (timer.isDelay(1000L))) {

                if (current != null && !pos.equals(current)) {
                    size = previousSize = 0L;
                }
                current = pos;
                running = true;
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        try {
                            loader.writeChunkToNBT(chunk, mc.world, level);
                        } catch (Throwable t) {
                            size = -1L;
                            previousSize = 0L;
                            return;
                        }
                        compressed = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(new ByteArrayOutputStream(8096))));
                        try {
                            CompressedStreamTools.write(root, compressed);
                            previousSize = size;
                            size = compressed.size();
                        } catch (IOException e) {
                            size = -1L;
                            previousSize = 0L;
                        }
                    } finally {
                        timer.setLastMS();
                        running = false;
                    }
                });
            }
        }

    }

    public static class ChunkSizeRUN extends PinableFrame {

        public ChunkSizeRUN() {
            super("ChunkSizeSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());

        }

        @Override
        public void setup() {
            getSetup(this, xpos, ypos, Frame, FontSize);
        }

        @Override
        public void Ongui() {
            getInit(this, xpos, ypos, Frame, FontSize);
        }

        @Subscribe
        public void onRenderGameOverlay(Text event) {
            final String Size = " " + String.format("[%s | %s]", size == -1 ? "<error>" : toFormattedBytes(size), difference(size - previousSize));

            int posx = (int) (this.x * RenderUtils.simpleScale(false));
            int posy = (int) (this.y * RenderUtils.simpleScale(true));

            if (Background.getValBoolean())
                Gui.drawRect(posx, posy + 10, posx + widthcal(Frame, Size), posy + 20, BgColor.getcolor());

            
            fontSelect(Frame, Size, posx, posy + 10, TextColor.getcolor(), Shadow.getValBoolean());
            
        }


        private static String toFormattedBytes(long size) {
            NumberFormat format = NumberFormat.getInstance();
            format.setGroupingUsed(true);
            if (size < 1000) // less than 1KB
            {
                return format.format(size) + " B";
            } else if (size < 1000000) // less than 1MB
            {
                return format.format((double) size / 1000.D) + " KB";
            } else {
                return format.format((double) size / 1000000.D) + " MB";
            }
        }

        private static String difference(long size) {
            if (size == 0) {
                return "+0 B";
            }
            if (size > 0) {
                return "+" + toFormattedBytes(size);
            } else {
                return "-" + toFormattedBytes(Math.abs(size));
            }
        }

    }

}

