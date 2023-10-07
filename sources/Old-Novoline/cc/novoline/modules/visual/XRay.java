package cc.novoline.modules.visual;

import cc.novoline.Novoline;
import cc.novoline.events.EventTarget;
import cc.novoline.events.events.SettingEvent;
import cc.novoline.events.events.TickUpdateEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.*;
import cc.novoline.utils.ColorUtils;
import cc.novoline.utils.RenderUtils;
import cc.novoline.utils.Timer;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static cc.novoline.gui.screen.setting.SettingType.*;
import static cc.novoline.modules.EnumModuleType.VISUALS;
import static org.lwjgl.input.Keyboard.KEY_NONE;

public final class XRay extends AbstractModule {

    /* fields */
    public static int alpha;
    public static boolean isEnabled;
    public static List<Integer> blockIdList = Lists.newArrayList(10, 11, 8, 9, 14, 15, 16, 21, 41, 42, 46, 48, 52, 56, 57, 61, 62, 73, 74, 84, 89, 103, 116, 117, 118, 120, 129, 133, 137, 145, 152, 153, 154);
    public static List<BlockPos> blockPosList = new CopyOnWriteArrayList<>();
    private Timer timer = new Timer();

    /* properties @off */
    @Property("opacity")
    private final IntProperty opacity = PropertyFactory.createInt(160).minimum(0).maximum(255);
    @Property("esp")
    private final BooleanProperty esp = PropertyFactory.booleanTrue();
    @Property("tracers")
    private final BooleanProperty tracers = PropertyFactory.booleanTrue();
    @Property("esp-ores")
    private final ListProperty ores = PropertyFactory.createList("Diamond").acceptableValues("Redstone", "Diamond", "Emerald", "Lapis", "Iron", "Coal", "Gold");
    @Property("distance")
    private final IntProperty distance = PropertyFactory.createInt(42).minimum(16).maximum(64);
    @Property("chunk-update")
    private final BooleanProperty update = PropertyFactory.booleanFalse();
    @Property("delay")
    private DoubleProperty delay = PropertyFactory.createDouble(10.0).minimum(1.0).maximum(30.0);

    /* constructors @on */
    public XRay(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "XRay", "XRay", KEY_NONE, VISUALS);
        Manager.put(new Setting("ESP_ORES", "ESP Ores", SELECTBOX, this, this.ores, () -> esp.get() || tracers.get()));
        Manager.put(new Setting("OPACITY", "Opacity", SLIDER, this, this.opacity, 5));
        Manager.put(new Setting("XRAY_DISTANCE", "Distance", SLIDER, this, this.distance, 4));
        Manager.put(new Setting("XR_TRACERS", "Tracers", CHECKBOX, this, this.tracers));
        Manager.put(new Setting("XR_ESP", "ESP", CHECKBOX, this, this.esp));
        Manager.put(new Setting("XR_UPDATE", "Chunks Update", CHECKBOX, this, this.update));
        Manager.put(new Setting("XR_DELAY", "Update Delay", SLIDER, this, this.delay, 0.5));
    }

    @Override
    public void onEnable() {
        onToggle(true);
    }

    @Override
    public void onDisable() {
        onToggle(false);
        timer.reset();
    }

    private void onToggle(boolean enabled) {
        blockPosList.clear();
        mc.renderGlobal.loadRenderers();
        isEnabled = enabled;
    }

    @EventTarget
    public void update(TickUpdateEvent event) {
        if (alpha != opacity.get()) {
            mc.renderGlobal.loadRenderers();
            alpha = opacity.get();

        } else if (update.get()) {
            if (timer.delay(1000 * delay.get())) {
                mc.renderGlobal.loadRenderers();
                timer.reset();
            }
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent e) {
        if (esp.get()) {
            for (BlockPos pos : blockPosList) {
                if (mc.player.getDistance(pos.getX(), pos.getZ()) <= distance.get()) {
                    Block block = mc.world.getBlockState(pos).getBlock();

                    if (block == Blocks.diamond_ore && ores.contains("Diamond")) {
                        render3D(pos, 0, 255, 255);
                    } else if (block == Blocks.iron_ore && ores.contains("Iron")) {
                        render3D(pos, 225, 225, 225);
                    } else if (block == Blocks.lapis_ore && ores.contains("Lapis")) {
                        render3D(pos, 0, 0, 255);
                    } else if (block == Blocks.redstone_ore && ores.contains("Redstone")) {
                        render3D(pos, 255, 0, 0);
                    } else if (block == Blocks.coal_ore && ores.contains("Coal")) {
                        render3D(pos, 0, 30, 30);
                    } else if (block == Blocks.emerald_ore && ores.contains("Emerald")) {
                        render3D(pos, 0, 255, 0);
                    } else if (block == Blocks.gold_ore && ores.contains("Gold")) {
                        render3D(pos, 255, 255, 0);
                    }
                }
            }
        }
    }

    private void render3D(BlockPos pos, int red, int green, int blue) {
        if (esp.get()) {
            RenderUtils.drawSolidBlockESP(pos, ColorUtils.getColor(red, green, blue));
        }

        if (tracers.get()) {
            RenderUtils.drawLine(pos, ColorUtils.getColor(red, green, blue));
        }
    }

    @EventTarget
    public void onSetting(SettingEvent event) {
        if (event.getSettingName().equals("XR_ESP") || event.getSettingName().equals("XR_TRACERS")) {
            blockPosList.clear();
            mc.renderGlobal.loadRenderers();
        }
    }

    public static boolean showESP() {
        return Novoline.getInstance().getModuleManager().getModule(XRay.class).esp.get();
    }

    public static int getDistance() {
        return Novoline.getInstance().getModuleManager().getModule(XRay.class).distance.get();
    }
}
