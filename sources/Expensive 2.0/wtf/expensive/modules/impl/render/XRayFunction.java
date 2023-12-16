package wtf.expensive.modules.impl.render;

import net.minecraft.block.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.network.play.server.SMultiBlockChangePacket;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventInput;
import wtf.expensive.events.impl.player.EventMotion;
import wtf.expensive.events.impl.player.EventMove;
import wtf.expensive.events.impl.player.EventWorldChange;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.ModeSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;
import wtf.expensive.util.world.WorldUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author dedinside
 * @since 05.06.2023
 */

// pasta
@FunctionAnnotation(name = "XRay", type = Type.Render)
public class XRayFunction extends Function {

    CopyOnWriteArrayList<BlockPos> waiting = new CopyOnWriteArrayList<>();

    public SliderSetting up = new SliderSetting("Вверх", 5, 0, 30, 1);

    public SliderSetting down = new SliderSetting("Вниз", 5, 0, 30, 1);
    public SliderSetting radius = new SliderSetting("Радиус", 20, 0, 30, 1);
    public SliderSetting delay = new SliderSetting("Задержка", 13, 0, 40, 1);
    public SliderSetting skip = new SliderSetting("Пропуск блоков", 3, 1, 5, 1);


    public MultiBoxSetting ores = new MultiBoxSetting("Задержка",
            new BooleanOption("Уголь", false),
            new BooleanOption("Железо", false),
            new BooleanOption("Редстоун", false),
            new BooleanOption("Золото", false),
            new BooleanOption("Эмеральд", false),
            new BooleanOption("Алмаз", false),
            new BooleanOption("Незерит", false)
    );

    public XRayFunction() {
        addSettings(radius, up, down, delay,skip, ores);
    }

    CopyOnWriteArrayList<BlockPos> clicked = new CopyOnWriteArrayList<>();

    BlockPos clicking;
    Thread thread;

    @Override
    protected void onDisable() {
        super.onDisable();
        if (thread != null) {
            thread.interrupt();
            thread.stop();
        }
        clicking = null;
        clicked.clear();
        waiting.clear();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        waiting = removeEveryOther(getBlocks(), skip.getValue().intValue());
        thread = new Thread(() -> {
            if (mc.player != null) {
                for (BlockPos click : waiting) {
                    mc.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.START_DESTROY_BLOCK, click, Direction.UP));
                    clicked.add(click);
                    clicking = click;
                    try {
                        Thread.sleep(delay.getValue().intValue());
                    } catch (InterruptedException e) {
                        System.out.println("Anti Xray: " + e.getMessage());
                    }
                }
            }
        });
        thread.start();
    }

    private int isValid(BlockPos pos) {

        BlockState state = mc.world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof OreBlock ore) {
            if (ore == Blocks.COAL_ORE && ores.get(0)) {
                return ColorUtil.rgba(12, 12, 12, 255);
            }
            if (ore == Blocks.IRON_ORE && ores.get(1)) {
                return ColorUtil.rgba(122, 122, 122, 255);
            }
            if (ore == Blocks.REDSTONE_ORE && ores.get(2)) {
                return ColorUtil.rgba(255, 82, 82, 255);
            }
            if (ore == Blocks.GOLD_ORE && ores.get(3)) {
                return ColorUtil.rgba(247, 255, 102, 255);
            }
            if (ore == Blocks.EMERALD_ORE && ores.get(4)) {
                return ColorUtil.rgba(116, 252, 101, 255);
            }
            if (ore == Blocks.DIAMOND_ORE && ores.get(5)) {
                return ColorUtil.rgba(77, 219, 255, 255);
            }
            if (ore == Blocks.ANCIENT_DEBRIS && ores.get(6)) {
                return ColorUtil.rgba(105, 60, 12, 255);
            }
        }
        return -1;
    }

    private float animation;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventInput e) {
            if (thread != null) {
                if (thread.isAlive()) {
                    e.setStrafe(0);
                    e.setForward(0);
                    e.setJump(false);
                    e.setSneak(false);
                }
            }
        }


        if (event instanceof EventPacket e) {
            if (thread != null) {
                if (thread.isAlive()) {
                    if (e.isSendPacket()) {
                        if (e.getPacket() instanceof CPlayerPacket) {
                            e.setCancel(true);
                        }
                        if (e.getPacket() instanceof CAnimateHandPacket) {
                            e.setCancel(true);
                        }
                        if (e.getPacket() instanceof CPlayerTryUseItemPacket) {
                            e.setCancel(true);
                        }
                        if (e.getPacket() instanceof CHeldItemChangePacket) {
                            e.setCancel(true);
                        }
                    }
                }
            }
        }
        if (event instanceof EventMotion e) {
            if (thread != null) {
                if (thread.isAlive()) {
                    e.setCancel(true);
                }
            }
        }
        if (event instanceof EventRender e) {

            if (e.isRender2D()) {
                float width = 100;
                float heigth = 15;

                RenderUtil.Render2D.drawRoundedCorner(e.scaledResolution.getScaledWidth() / 2f - width / 2, 10, width, heigth, 4, ColorUtil.rgba(10, 10, 10, 128));
                float x = e.scaledResolution.getScaledWidth() / 2f - width / 2;
                Fonts.msMedium[16].drawString(e.matrixStack, clicked.size() + "/" + waiting.size(), x + 5, 15, -1);

                long millis = (waiting.size() * delay.getValue().intValue()) - clicked.size() * delay.getValue().intValue();

                String time = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                ;

                Fonts.msMedium[16].drawString(e.matrixStack, time, x + width - Fonts.msMedium[16].getWidth(time) - 5, 15, -1);
            }

            if (e.isRender3D()) {
                if (clicked != null && clicking != null) {
                    RenderUtil.Render3D.drawBlockBox(clicking, ColorUtil.rgba(83, 252, 154, 255));
                    for (BlockPos click : clicked) {
                        int color = isValid(click);
                        if (color != -1) {
                            RenderUtil.Render3D.drawBlockBox(click, color);
                        }
                    }
                }
            }
        }
    }

    private static <T> CopyOnWriteArrayList<T> removeEveryOther(CopyOnWriteArrayList<T> inList, int offset) {
        if (offset == 1) return inList;
        CopyOnWriteArrayList<T> outList = new CopyOnWriteArrayList<>();
        @SuppressWarnings("unchecked")
        T[] ts = (T[]) inList.toArray();
        for (int i = 0; i < ts.length; i++) {
            if (i % offset == 0) {
                outList.add(ts[i]);
            }
        }
        return outList;
    }

    CopyOnWriteArrayList<BlockPos> getBlocks() {
        CopyOnWriteArrayList<BlockPos> blocks = new CopyOnWriteArrayList<>();
        BlockPos start = mc.player.getPosition();
        int dis = radius.getValue().intValue();
        int up = this.up.getValue().intValue();
        int down = this.down.getValue().intValue();
        for (int y = up; y >= -down; y--)
            for (int x = dis; x >= -dis; x--)

                for (int z = dis; z >= -dis; z--) {
                    BlockPos pos = start.add(x, y, z);

                    if (pos.getY() > 0) {
                        Block block = mc.world.getBlockState(pos).getBlock();
                        if (block instanceof AirBlock) continue;
                        blocks.add(pos);
                    }

                }
        Collections.shuffle(blocks);
        return blocks;
    }

}
