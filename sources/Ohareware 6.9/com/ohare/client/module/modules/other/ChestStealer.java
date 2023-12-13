package com.ohare.client.module.modules.other;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.TimerUtil;
import com.ohare.client.utils.value.impl.BooleanValue;
import com.ohare.client.utils.value.impl.NumberValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;

public class ChestStealer extends Module {
    private TimerUtil timer = new TimerUtil();
    private NumberValue<Integer> delay = new NumberValue("Delay", 80, 50, 500, 10);
    private BooleanValue aura = new BooleanValue("Multi",false);
    private SecureRandom rnd = new SecureRandom();

    //Chest Aura
    private List<BlockPos> raidedChests = new ArrayList<>();
    private List<C0EPacketClickWindow> queuedClicks = new ArrayList<>();
    private int waiting,clicks;
    private List<Integer> windowIds = new ArrayList<>();
    private boolean openingChest,stealing;
    private Container container;
    private C08PacketPlayerBlockPlacement placePacket = null;
    private List<Container> alreadyOpened = new ArrayList<>();

    public ChestStealer() {
        super("ChestStealer", Category.OTHER, new Color(0, 25, 255, 255).getRGB());
        setDescription("Be a nigger and steal shit");
        setRenderlabel("Chest Stealer");
        addValues(delay,aura);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        alreadyOpened.clear();
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(aura.isEnabled() ? "Multi" : "Single");
        if(aura.isEnabled()) {
            if(event.isPre()) {
                if(isInventoryFull()) return;
                if(alreadyOpened.contains(container)) return;
                alreadyOpened.clear();

                if (openingChest && queuedClicks.isEmpty()) {
                    waiting++;
                    if (waiting > 20) {
                        openingChest = false;
                    }
                } else {
                    waiting = 0;
                }

                if (clicks < 3 && !queuedClicks.isEmpty()) {
                    C0EPacketClickWindow windowClick = queuedClicks.remove(0);
                    mc.getNetHandler().getNetworkManager().sendPacket(windowClick);
                    if (container != null) {
                        container.slotClick(windowClick.getSlotId(), windowClick.getUsedButton(), windowClick.getMode(), mc.thePlayer);
                    }
                    clicks++;
                    if (queuedClicks.isEmpty()) {
                        mc.getNetHandler().getNetworkManager().sendPacket(new C0DPacketCloseWindow(0));
                    }
                } else {
                    clicks = 0;
                }
                if (!queuedClicks.isEmpty() || openingChest || (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.isSneaking())) {
                    return;
                }
                List<BlockPos> positions = new ArrayList<>();
                for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
                    BlockPos position = tileEntity.getPos();
                    if (!raidedChests.contains(position) && mc.thePlayer.getDistanceSq(position.getX() + 0.5, position.getY() + 0.5 - mc.thePlayer.getEyeHeight(), position.getZ() + 0.5) < 4.5F*4.5F) {
                        if (tileEntity instanceof TileEntityChest) {
                            TileEntityChest chest = (TileEntityChest) tileEntity;
                            if (chest.adjacentChestZNeg == null && chest.adjacentChestXNeg == null) {
                                positions.add(position);
                            }
                        }
                    }
                }
                if (positions.isEmpty()) {
                    raidedChests.clear();
                } else {
                    BlockPos position = positions.get(0);
                    raidedChests.add(position);
                    float[] rotation = getAnglesToPosition(mc.thePlayer, position.getX() + 0.5, position.getY() + 1, position.getZ() + 0.5);

                    event.setYaw((float) (rotation[0] + Math.random() - 0.5));
                    event.setPitch((float) (rotation[1] + Math.random() - 0.5));

                    openingChest = true;
                    mc.getNetHandler().getNetworkManager().sendPacket(new C0DPacketCloseWindow(0));
                    placePacket = new C08PacketPlayerBlockPlacement(position, 1, null, 0, 0, 0);
                }
            }
            if (placePacket != null) {
                mc.getNetHandler().getNetworkManager().sendPacket(placePacket);
                stealing = true;
                placePacket = null;
            }
        } else {
            if (event.isPre()) {
                if (mc.currentScreen instanceof GuiChest) {
                    final GuiChest chest = (GuiChest) mc.currentScreen;
                    if (isChestEmpty(chest) || isInventoryFull()) {
                        mc.thePlayer.closeScreen();
                        return;
                    }
                    for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                        final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                        if (stack != null && timer.sleep((long) secRanFloat(delay.getValue(), delay.getValue() * 2))) {
                            mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                            break;
                        }
                    }
                }

            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S2DPacketOpenWindow && ((S2DPacketOpenWindow) event.getPacket()).getGuiId().equals("minecraft:chest")) {

            if (openingChest) {
                event.setCanceled(true);
                S2DPacketOpenWindow openWindow = (S2DPacketOpenWindow) event.getPacket();
                container = new GuiChest(mc.thePlayer.inventory, new ContainerLocalMenu(openWindow.getGuiId(), openWindow.getWindowTitle(), openWindow.getSlotCount())).inventorySlots;
                windowIds.add(openWindow.getWindowId());
                stealing = true;
            }
        }

        if (event.getPacket() instanceof S30PacketWindowItems) {
            S30PacketWindowItems windowItems = (S30PacketWindowItems) event.getPacket();

            if (openingChest && windowIds.contains(windowItems.func_148911_c())) {
                stealing = true;
                short clickNumber = 0;
                for (int index = 0; index < windowItems.getItemStacks().length - 36; index++) {
                    ItemStack itemStack = windowItems.getItemStacks()[index];
                    if (itemStack != null) {
                        queuedClicks.add(new C0EPacketClickWindow(windowItems.func_148911_c(), index, 0, 1, itemStack, clickNumber++));
                    }
                }
                container.putStacksInSlots(windowItems.getItemStacks());
                windowIds.remove(windowItems.func_148911_c());
                openingChest = false;

                if (queuedClicks.isEmpty()) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C0DPacketCloseWindow(0));
                    stealing = false;
                    alreadyOpened.add(container);
                    queuedClicks.clear();
                    raidedChests.clear();
                    windowIds.clear();
                }
            }
        }

        if (event.getPacket() instanceof S2EPacketCloseWindow && mc.currentScreen instanceof GuiChat) {
            event.setCanceled(true);
        }
    }

    private float[] getAnglesToPosition(Entity player, double x, double y, double z) {
        double deltaX = x - player.posX;
        double deltaY = y - player.posY - player.getEyeHeight() - 0.3;
        double deltaZ = z - player.posZ;
        double yawToEntity; // tangent degree to entity

        if (deltaZ < 0 && deltaX < 0) { // quadrant 3
            yawToEntity = 90D + Math.toDegrees(Math.atan(deltaZ / deltaX)); // 90
            // degrees
            // forward
        } else if (deltaZ < 0 && deltaX > 0) { // quadrant 4
            yawToEntity = -90D + Math.toDegrees(Math.atan(deltaZ / deltaX)); // 90
            // degrees
            // back
        } else { // quadrants one or two
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ
                * deltaZ); // distance away for calculating pitch
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ)); // tangent

        yawToEntity = wrapAngleTo180((float) yawToEntity);
        pitchToEntity = wrapAngleTo180((float) pitchToEntity);

        yawToEntity = Double.isNaN(yawToEntity) ? 0 : yawToEntity;
        pitchToEntity = Double.isNaN(pitchToEntity) ? 0 : pitchToEntity;

        return new float[] { (float) yawToEntity, (float) pitchToEntity };
    }

    private static float wrapAngleTo180(float angle) {
        angle %= 360.0F;

        while (angle >= 180.0F) {
            angle -= 360.0F;
        }

        while (angle < -180.0F) {
            angle += 360.0F;
        }
        return angle;
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null) {
                return false;
            }
        }
        return true;
    }

    private float secRanFloat(float min, float max) {
        return rnd.nextFloat() * (max - min) + min;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

}