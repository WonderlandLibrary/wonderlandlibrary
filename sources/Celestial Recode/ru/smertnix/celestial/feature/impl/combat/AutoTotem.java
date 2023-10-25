package ru.smertnix.celestial.feature.impl.combat;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.math.TimerHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoTotem extends Feature {
    private final NumberSetting health;
    private final BooleanSetting countTotem;
    private final BooleanSetting checkCrystal;
    private final BooleanSetting checkTnt;
    private final BooleanSetting noBallSwitch = new BooleanSetting("No Ball Switch", true, () -> true);
    private final NumberSetting radiusTnt;
    
    private final BooleanSetting checkObs;
    private final NumberSetting radiusObs;
    
    private final BooleanSetting checkBed;
    private final NumberSetting radiusBed;
    
    private final BooleanSetting inventoryOnly;
    private final NumberSetting radiusCrystal;
    private final BooleanSetting switchBack = new BooleanSetting("Swap Back", "Автоматически свапает тотем.", true, () -> true);

    private final List<Integer> lastItem = new ArrayList<>();
    private final TimerHelper timerHelper = new TimerHelper();
    private boolean swap = false;

    public AutoTotem() {
        super("Auto Totem", "Автоматически свапает предмет на тотем при малом количестве хп", FeatureCategory.Combat);
        health = new NumberSetting("Health", 3.5f, 1.f, 20.f, 0.5F, () -> true);
        inventoryOnly = new BooleanSetting("Only Inventory", false, () -> true);
        countTotem = new BooleanSetting("Totem Counter", true, () -> true);
        checkCrystal = new BooleanSetting("Check Crystal", true, () -> true);
        radiusCrystal = new NumberSetting("Distance to Crystal", 6, 1, 8, 1, checkCrystal::getBoolValue);
        checkTnt = new BooleanSetting("Check Tnt", true, () -> true);
        checkObs = new BooleanSetting("Check Obsidian", false, () -> true);
        checkBed = new BooleanSetting("Check Bed", false, () -> true);
        radiusBed = new NumberSetting("Distance to Bed", 6, 1, 8, 1, () -> checkBed.getBoolValue());
        radiusObs = new NumberSetting("Distance to Obsidian", 6, 1, 8, 1, () -> checkObs.getBoolValue());
        radiusTnt = new NumberSetting("Distance to Tnt", 6, 1, 8, 1, () -> checkTnt.getBoolValue());
        addSettings(health, switchBack, noBallSwitch, checkCrystal, radiusCrystal, checkTnt, radiusTnt, checkObs, radiusObs, checkBed, radiusBed, countTotem);
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.Totem) {
                count++;
            }
        }
        return count;
    }


    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (fountTotemCount() > 0 && countTotem.getBoolValue()) {
            mc.rubik_18.drawStringWithShadow(fountTotemCount() + "", (event.getResolution().getScaledWidth() / 2f + 19), (event.getResolution().getScaledHeight() / 2f), -1);
            for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.Totem) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (inventoryOnly.getBoolValue() && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        int tIndex = -1;
        int totemCount = 0;

        for (int i = 0; i < 45; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem && tIndex == -1) {
                tIndex = i;
            }
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem) {
                totemCount++;
            }
        }

        if ((mc.player.getHealth() < health.getNumberValue() || checkObsidian() || checkCrystal() || checkTNT()) && totemCount != 0 && tIndex != -1) {
            if (mc.player.getHeldItemOffhand().getItem() != Items.Totem) {
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, tIndex < 9 ? tIndex + 36 : tIndex, 0, ClickType.PICKUP, mc.player);
                swap = true;
                lastItem.add(tIndex);
            }
        } else if (switchBack.getBoolValue() && (swap || totemCount == 0) && lastItem.size() > 0) {
            if (!(mc.player.inventory.getStackInSlot(lastItem.get(0)).getItem() instanceof ItemAir)) {
                if (timerHelper.hasReached(2)) {

                    mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);

                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                 //   mc.playerController.windowClick(0, lastItem.get(0) < 9 ? lastItem.get(0) + 36 : lastItem.get(0), 0, ClickType.PICKUP, mc.player);
                    timerHelper.reset();

                }
            }
            swap = false;
            lastItem.clear();
        }
    }
    
    private boolean checkObsidian() {
        if (!checkObs.getBoolValue()) {
            return false;
        }
        BlockPos pos = getSphere(getPlayerPosLocal(), (float) radiusObs.getNumberValue(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
        return pos != null;
    }
    
    public static double getDistanceOfEntityToBlock(Entity entity, BlockPos blockPos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static double getDistance(double n, double n2, double n3, double n4, double n5, double n6) {
        double n7 = n - n4;
        double n8 = n2 - n5;
        double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        return state.getBlock() instanceof BlockObsidian;
    }
    
    public static BlockPos getPlayerPosLocal() {
        if (mc.player == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static List<BlockPos> getSphere(BlockPos blockPos, float n, int n2, boolean b, boolean b2, int n3) {
        ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        int n4 = x - (int)n;
        while ((float)n4 <= (float)x + n) {
            int n5 = z - (int)n;
            while ((float)n5 <= (float)z + n) {
                int n6 = b2 ? y - (int)n : y;
                while (true) {
                    float f = n6;
                    float f2 = b2 ? (float)y + n : (float)(y + n2);
                    if (!(f < f2)) break;
                    double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? (y - n6) * (y - n6) : 0);
                    if (n7 < (double)(n * n) && (!b || n7 >= (double)((n - 1.0f) * (n - 1.0f)))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                    ++n6;
                }
                ++n5;
            }
            ++n4;
        }
        return list;
    }

    private boolean checkTNT() {
        if (!checkTnt.getBoolValue()) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusTnt.getNumberValue()) {
                return true;
            }
            if (!(entity instanceof EntityMinecartTNT) || !(AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusTnt.getNumberValue()))
                continue;
            return true;
        }
        return false;
    }

    private boolean checkCrystal() {
        if (!checkCrystal.getBoolValue()) {
            return false;
        }
        for (Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !(AutoTotem.mc.player.getDistanceToEntity(entity) <= radiusCrystal.getNumberValue()))
                continue;
            return true;
        }
        return false;
    }
}