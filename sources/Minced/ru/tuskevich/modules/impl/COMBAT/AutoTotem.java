// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import java.util.Comparator;
import java.util.function.Predicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Iterator;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.event.EventTarget;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AutoTotem", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd", type = Type.COMBAT)
public class AutoTotem extends Module
{
    SliderSetting healthAmount;
    BooleanSetting absorption;
    BooleanSetting switchBack;
    BooleanSetting noBallSwitch;
    BooleanSetting checkExplosion;
    SliderSetting radiusExplosion;
    BooleanSetting checkObsidian;
    SliderSetting radiusObs;
    BooleanSetting checkFall;
    SliderSetting fallDistance;
    BooleanSetting checkArmor;
    public static int swapBack;
    
    public AutoTotem() {
        this.healthAmount = new SliderSetting("Health", 3.5f, 1.0f, 20.0f, 0.1f);
        this.absorption = new BooleanSetting("Check Absorption", true);
        this.switchBack = new BooleanSetting("Swap Back", true);
        this.noBallSwitch = new BooleanSetting("No Ball Switch", false);
        this.checkExplosion = new BooleanSetting("Check Explosion", false);
        this.radiusExplosion = new SliderSetting("Distance to Explosion", 6.0f, 1.0f, 8.0f, 1.0f, () -> this.checkExplosion.get());
        this.checkObsidian = new BooleanSetting("Check Obsidian", false);
        this.radiusObs = new SliderSetting("Distance to Obsidian", 6.0f, 1.0f, 8.0f, 1.0f);
        this.checkFall = new BooleanSetting("Check Fall", false);
        this.fallDistance = new SliderSetting("Fall Distance", 15.0f, 5.0f, 125.0f, 5.0f, () -> this.checkFall.get());
        this.checkArmor = new BooleanSetting("Check Armor", false);
        this.add(this.healthAmount, this.absorption, this.switchBack, this.noBallSwitch, this.checkExplosion, this.radiusExplosion, this.checkObsidian, this.radiusObs, this.checkFall, this.fallDistance, this.checkArmor);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final int totem = getSlotIDFromItem(Items.TOTEM_OF_UNDYING);
        final Minecraft mc = AutoTotem.mc;
        float hp = Minecraft.player.getHealth();
        float healthValue = this.healthAmount.getFloatValue();
        if (this.absorption.get()) {
            final float n = hp;
            final Minecraft mc2 = AutoTotem.mc;
            hp = n + Minecraft.player.getAbsorptionAmount();
        }
        if (this.checkArmor.get()) {
            final Minecraft mc3 = AutoTotem.mc;
            if (Minecraft.player.getTotalArmorValue() <= 17) {
                final Minecraft mc4 = AutoTotem.mc;
                if (Minecraft.player.getTotalArmorValue() != 0) {
                    healthValue *= 2.5f;
                }
            }
            final Minecraft mc5 = AutoTotem.mc;
            if (Minecraft.player.getTotalArmorValue() == 0) {
                healthValue *= 4.0f;
            }
        }
        final boolean totemCheck = hp <= healthValue || this.checkCrystal() || this.checkBed() || this.checkFall(this.fallDistance.getFloatValue()) || this.checkTNT() || this.checkObsidian();
        final Minecraft mc6 = AutoTotem.mc;
        final boolean totemInHand = Minecraft.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
        final Minecraft mc7 = AutoTotem.mc;
        final boolean handNotNull = !(Minecraft.player.getHeldItemOffhand().getItem() instanceof ItemAir);
        if (totemCheck) {
            if (totem >= 0 && !totemInHand) {
                final PlayerControllerMP playerController = AutoTotem.mc.playerController;
                final int windowId = 0;
                final int slotId = totem;
                final int mouseButton = 1;
                final ClickType pickup = ClickType.PICKUP;
                final Minecraft mc8 = AutoTotem.mc;
                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                final PlayerControllerMP playerController2 = AutoTotem.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = 45;
                final int mouseButton2 = 1;
                final ClickType pickup2 = ClickType.PICKUP;
                final Minecraft mc9 = AutoTotem.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                if (handNotNull) {
                    final PlayerControllerMP playerController3 = AutoTotem.mc.playerController;
                    final int windowId3 = 0;
                    final int slotId3 = totem;
                    final int mouseButton3 = 0;
                    final ClickType pickup3 = ClickType.PICKUP;
                    final Minecraft mc10 = AutoTotem.mc;
                    playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
                    if (AutoTotem.swapBack == -1) {
                        AutoTotem.swapBack = totem;
                    }
                }
            }
            return;
        }
        if (!totemInHand && AutoTotem.swapBack >= 0) {
            final PlayerControllerMP playerController4 = AutoTotem.mc.playerController;
            final int windowId4 = 0;
            final int swapBack = AutoTotem.swapBack;
            final int mouseButton4 = 0;
            final ClickType pickup4 = ClickType.PICKUP;
            final Minecraft mc11 = AutoTotem.mc;
            playerController4.windowClick(windowId4, swapBack, mouseButton4, pickup4, Minecraft.player);
            final PlayerControllerMP playerController5 = AutoTotem.mc.playerController;
            final int windowId5 = 0;
            final int slotId4 = 45;
            final int mouseButton5 = 0;
            final ClickType pickup5 = ClickType.PICKUP;
            final Minecraft mc12 = AutoTotem.mc;
            playerController5.windowClick(windowId5, slotId4, mouseButton5, pickup5, Minecraft.player);
            if (handNotNull) {
                final PlayerControllerMP playerController6 = AutoTotem.mc.playerController;
                final int windowId6 = 0;
                final int swapBack2 = AutoTotem.swapBack;
                final int mouseButton6 = 0;
                final ClickType pickup6 = ClickType.PICKUP;
                final Minecraft mc13 = AutoTotem.mc;
                playerController6.windowClick(windowId6, swapBack2, mouseButton6, pickup6, Minecraft.player);
            }
            AutoTotem.swapBack = -1;
            return;
        }
        if (AutoTotem.swapBack >= 0) {
            final PlayerControllerMP playerController7 = AutoTotem.mc.playerController;
            final int windowId7 = 0;
            final int swapBack3 = AutoTotem.swapBack;
            final int mouseButton7 = 0;
            final ClickType pickup7 = ClickType.PICKUP;
            final Minecraft mc14 = AutoTotem.mc;
            playerController7.windowClick(windowId7, swapBack3, mouseButton7, pickup7, Minecraft.player);
            final PlayerControllerMP playerController8 = AutoTotem.mc.playerController;
            final int windowId8 = 0;
            final int slotId5 = 45;
            final int mouseButton8 = 0;
            final ClickType pickup8 = ClickType.PICKUP;
            final Minecraft mc15 = AutoTotem.mc;
            playerController8.windowClick(windowId8, slotId5, mouseButton8, pickup8, Minecraft.player);
            if (handNotNull) {
                final PlayerControllerMP playerController9 = AutoTotem.mc.playerController;
                final int windowId9 = 0;
                final int swapBack4 = AutoTotem.swapBack;
                final int mouseButton9 = 0;
                final ClickType pickup9 = ClickType.PICKUP;
                final Minecraft mc16 = AutoTotem.mc;
                playerController9.windowClick(windowId9, swapBack4, mouseButton9, pickup9, Minecraft.player);
            }
            AutoTotem.swapBack = -1;
        }
    }
    
    @EventTarget
    public void onDisplay(final EventDisplay eventDisplay) {
        if (this.fountTotemCount() > 0) {
            Fonts.Nunito14.drawStringWithOutline(this.fountTotemCount() + "", eventDisplay.sr.getScaledWidth() / 2 - 2, eventDisplay.sr.getScaledHeight() / 2 + 40, (this.fountTotemCount() > 1) ? -1 : new Color(255, 58, 58).getRGB());
            int i = 0;
            while (true) {
                final int n = i;
                final Minecraft mc = AutoTotem.mc;
                if (n >= Minecraft.player.inventory.getSizeInventory()) {
                    break;
                }
                final Minecraft mc2 = AutoTotem.mc;
                final ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    AutoTotem.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, eventDisplay.sr.getScaledWidth() / 2 - 8, eventDisplay.sr.getScaledHeight() / 2 + 20);
                    GlStateManager.popMatrix();
                }
                ++i;
            }
        }
    }
    
    private int fountTotemCount() {
        int count = 0;
        int i = 0;
        while (true) {
            final int n = i;
            final Minecraft mc = AutoTotem.mc;
            if (n >= Minecraft.player.inventory.getSizeInventory()) {
                break;
            }
            final Minecraft mc2 = AutoTotem.mc;
            final ItemStack stack = Minecraft.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                ++count;
            }
            ++i;
        }
        return count;
    }
    
    public boolean isBall() {
        if (!this.noBallSwitch.get()) {
            return false;
        }
        final Minecraft mc = AutoTotem.mc;
        final ItemStack stack = Minecraft.player.getHeldItemOffhand();
        return stack.getDisplayName().toLowerCase().contains("\ufffd\ufffd\ufffd") || stack.getDisplayName().toLowerCase().contains("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd");
    }
    
    private boolean checkFall(final float fall) {
        if (!this.checkFall.get()) {
            return false;
        }
        final Minecraft mc = AutoTotem.mc;
        return Minecraft.player.fallDistance > fall;
    }
    
    private boolean checkTNT() {
        if (!this.checkExplosion.get() || this.isBall()) {
            return false;
        }
        for (final Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed) {
                final Minecraft mc = AutoTotem.mc;
                if (Minecraft.player.getDistance(entity) <= 4.0f) {
                    return true;
                }
            }
            if (entity instanceof EntityMinecartTNT) {
                final Minecraft mc2 = AutoTotem.mc;
                if (Minecraft.player.getDistance(entity) <= this.radiusExplosion.getFloatValue()) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    private boolean checkCrystal() {
        if (!this.checkExplosion.get() || this.isBall()) {
            return false;
        }
        for (final Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                final Minecraft mc = AutoTotem.mc;
                if (Minecraft.player.getDistance(entity) <= this.radiusExplosion.getFloatValue()) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    private boolean checkObsidian() {
        if (!this.checkObsidian.get() || this.isBall()) {
            return false;
        }
        final Minecraft mc;
        final BlockPos pos = getSphere(getPlayerPosLocal(), this.radiusObs.getFloatValue(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> {
            mc = AutoTotem.mc;
            return Double.valueOf(getDistanceOfEntityToBlock(Minecraft.player, blockPos));
        })).orElse(null);
        return pos != null;
    }
    
    private boolean checkBed() {
        if (!this.checkExplosion.get() || this.isBall()) {
            return false;
        }
        final Minecraft mc;
        final BlockPos pos = getSphere(getPlayerPosLocal(), this.radiusExplosion.getFloatValue(), 6, false, true, 0).stream().filter(this::IsValidBed).min(Comparator.comparing(blockPos -> {
            mc = AutoTotem.mc;
            return Double.valueOf(getDistanceOfEntityToBlock(Minecraft.player, blockPos));
        })).orElse(null);
        return pos != null;
    }
    
    public static int getSlotIDFromItem(final Item item) {
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            final Minecraft mc = AutoTotem.mc;
            final ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }
    
    public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int x = blockPos.getX();
        final int y = blockPos.getY();
        final int z = blockPos.getZ();
        for (int n4 = x - (int)n; n4 <= x + n; ++n4) {
            for (int n5 = z - (int)n; n5 <= z + n; ++n5) {
                for (int n6 = b2 ? (y - (int)n) : y; n6 < (b2 ? (y + n) : ((float)(y + n2))); ++n6) {
                    final double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }
    
    public static BlockPos getPlayerPosLocal() {
        final Minecraft mc = AutoTotem.mc;
        if (Minecraft.player == null) {
            return BlockPos.ORIGIN;
        }
        final Minecraft mc2 = AutoTotem.mc;
        final double floor = Math.floor(Minecraft.player.posX);
        final Minecraft mc3 = AutoTotem.mc;
        final double floor2 = Math.floor(Minecraft.player.posY);
        final Minecraft mc4 = AutoTotem.mc;
        return new BlockPos(floor, floor2, Math.floor(Minecraft.player.posZ));
    }
    
    public static double getDistance(final double n, final double n2, final double n3, final double n4, final double n5, final double n6) {
        final double n7 = n - n4;
        final double n8 = n2 - n5;
        final double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }
    
    private boolean IsValidBlockPos(final BlockPos pos) {
        final IBlockState state = AutoTotem.mc.world.getBlockState(pos);
        return state.getBlock() instanceof BlockObsidian;
    }
    
    private boolean IsValidBed(final BlockPos pos) {
        final IBlockState state = AutoTotem.mc.world.getBlockState(pos);
        return state.getBlock() instanceof BlockBed;
    }
    
    public static double getDistanceOfEntityToBlock(final Entity entity, final BlockPos blockPos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    static {
        AutoTotem.swapBack = -1;
    }
}
