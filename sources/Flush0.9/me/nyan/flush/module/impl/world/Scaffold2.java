package me.nyan.flush.module.impl.world;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event2D;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventSafewalk;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.combat.CombatUtils;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Scaffold2 extends Module {
    private final ModeSetting itemSpoof = new ModeSetting("Item Spoof", this, "Normal", "None", "Normal", "Packet");
    private final BooleanSetting keepRotations = new BooleanSetting("Keep Rotations", this, true),
            safewalk = new BooleanSetting("Safewalk", this, true),
            noSwing = new BooleanSetting("No Swing", this, true),
            blockCount = new BooleanSetting("Block Count", this, true);

    private final Float[] lastRotations = new Float[2];
    private int lastSlot;

    public Scaffold2() {
        super("Scaffold2", Category.WORLD);
    }

    @Override
    public void onEnable() {
        lastRotations[0] = null;
        lastSlot = mc.thePlayer.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (itemSpoof.is("none")) {
            mc.thePlayer.inventory.currentItem = lastSlot;
        }
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        int blocksSlot = getBlocksSlot();
        if (blocksSlot == -1) {
            if (itemSpoof.is("none")) {
                lastSlot = mc.thePlayer.inventory.currentItem;
            }
            return;
        }

        if (e.isPre()) {
            if (lastRotations[0] != null) {
                e.setYaw(lastRotations[0]);
                e.setPitch(lastRotations[1]);

                if (!keepRotations.getValue()) {
                    lastRotations[0] = null;
                }
            }
            return;
        }

        int index = 0;
        for (Vec3 vec3 : getPlayerCorners()) {
            if (index == 1) {
                for (int j = 0; j < 4; j++) {
                    double x = j < 2 ? mc.thePlayer.boundingBox.minX : mc.thePlayer.boundingBox.maxX;
                    double y = (int) mc.thePlayer.boundingBox.minY - 1;
                    double z = j % 2 == 0 ? mc.thePlayer.boundingBox.minZ : mc.thePlayer.boundingBox.maxZ;
                    BlockPos underPos = new BlockPos(x, y, z);
                    if (underPos.getBlock().getCollisionBoundingBox(mc.theWorld, underPos, mc.theWorld.getBlockState(underPos)) != null) {
                        return;
                    }
                }
            }

            double x = vec3.xCoord;
            double y = vec3.yCoord;
            double z = vec3.zCoord;

            BlockPos pos = new BlockPos(x, y, z);
            if (!pos.getBlock().isReplaceable(mc.theWorld, pos)) {
                index++;
                continue;
            }

            EnumFacing side = null;
            BlockPos adjacent = null;

            if (!mc.thePlayer.onGround) {
                BlockPos pos1 = pos.add(EnumFacing.DOWN.getDirectionVec());
                if (!pos1.getBlock().isReplaceable(mc.theWorld, pos1)) {
                    adjacent = pos1;
                    side = EnumFacing.DOWN;
                }
            }

            for (int i = 0; i < 4; i++) {
                EnumFacing facing = EnumFacing.getHorizontal(i);
                BlockPos pos1 = pos.add(facing.getDirectionVec());
                if (!pos1.getBlock().isReplaceable(mc.theWorld, pos1)) {
                    adjacent = pos1;
                    side = facing;
                    break;
                }
            }

            if (adjacent == null) {
                index++;
                continue;
            }

            Vec3 vec = getVec(side, x, y, z);
            pos = pos.add(side.getDirectionVec());

            boolean skip = false;
            int last = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = blocksSlot;

            if (lastRotations[0] != null && mc.playerController.onPlayerRightClick(
                    mc.thePlayer, mc.theWorld,
                    mc.thePlayer.getHeldItem(),
                    pos,
                    side.getOpposite(),
                    vec)) {
                if (!noSwing.getValue()) {
                    mc.thePlayer.swingItem();
                } else {
                    mc.getNetHandler().addToSendQueueNoEvent(new C0APacketAnimation());
                }
                skip = true;
            }

            if (itemSpoof.is("normal")) {
                mc.thePlayer.inventory.currentItem = last;
            }

            float[] rots = CombatUtils.getRotations(vec);
            lastRotations[0] = rots[0];
            lastRotations[1] = rots[1];

            if (skip) {
                break;
            }

            index++;
        }
    }

    private int getBlocksSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) {
                continue;
            }

            Item item = stack.getItem();
            if (item instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }

    private int getRemainingBlocks() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) {
                continue;
            }

            Item item = stack.getItem();
            if (item instanceof ItemBlock) {
                count += stack.stackSize;
            }
        }
        return count;
    }

    private Vec3 getVec(EnumFacing side, double x, double y, double z) {
        int dirX = side.getDirectionVec().getX();
        int dirY = side.getDirectionVec().getY();
        int dirZ = side.getDirectionVec().getZ();
        return new Vec3(dirX < 0 ? Math.floor(x) : dirX > 0 ? Math.ceil(x) : x,
                (dirY < 0 ? Math.floor(mc.thePlayer.posY) : dirY > 0 ? Math.ceil(mc.thePlayer.posY) :
                        mc.thePlayer.posY + MathUtils.getRandomNumber(0.4, 0.6)) - 1,
                dirZ < 0 ? Math.floor(z) : dirZ > 0 ? Math.ceil(z) : z);
    }

    private List<Vec3> getPlayerCorners() {
        List<Vec3> list = new ArrayList<>();
        double x = mc.thePlayer.posX;
        double y = (int) mc.thePlayer.boundingBox.minY - 1;
        double z = mc.thePlayer.posZ;
        list.add(new Vec3(x, y, z));

        for (int i = 0; i < 4; i++) {
            x = i < 2 ? mc.thePlayer.boundingBox.minX : mc.thePlayer.boundingBox.maxX;
            y = (int) mc.thePlayer.boundingBox.minY - 1;
            z = i % 2 == 0 ? mc.thePlayer.boundingBox.minZ : mc.thePlayer.boundingBox.maxZ;
            list.add(new Vec3(x, y, z));
        }
        return list;
    }

    @SubscribeEvent
    public void onSafewalk(EventSafewalk e) {
        if (safewalk.getValue()) {
            e.cancel();
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
    }

    @SubscribeEvent
    public void onRender2D(Event2D e) {
        if (!blockCount.getValue()) {
            return;
        }

        int slot = getBlocksSlot();
        int remaining = getRemainingBlocks();

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 20);
        String text = (slot == -1 ? "§c" : "§a") + remaining + "§r block" + (remaining != 1 ? "s" : "");
        float width = (slot != -1 ? 16 : 0) + 2 + font.getStringWidth(text);

        int y = e.getHeight() / 2 + 32;

        RenderUtils.fillRoundRect(e.getWidth() / 2F - width / 2F - 4, y - 4, width + 8, 24, 4, 0x88000000);

        if (slot != -1) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(slot);
            RenderHelper.enableGUIStandardItemLighting();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, e.getWidth() / 2 - (int) (width / 2F), y);
            RenderHelper.disableStandardItemLighting();
        }
        font.drawString(text,
                e.getWidth() / 2F - width / 2F + (slot != -1 ? 16 : 0),
                y + 16 / 2F - font.getFontHeight() / 2F,
                -1, true);
    }
}