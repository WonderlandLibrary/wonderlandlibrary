//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\1\Desktop\Minecraft-Deobfuscator3000-1.2.3\config"!

package info.sigmaclient.sigma.modules.item;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.StaffActives;
import info.sigmaclient.sigma.utils.TimerUtil;
import info.sigmaclient.sigma.utils.player.RotationUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.TripWireHookBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SkullItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import top.fl0wowp4rty.phantomshield.annotations.Native;
import java.util.Random;


public class ChestStealer extends Module {
    private final NumberValue auraRange = new NumberValue("Range", 3, 6, 1, NumberValue.NUMBER_TYPE.INT);
//    private final BooleanValue fix = new BooleanValue("Fix ViaVersion", true);
    private final NumberValue delay = new NumberValue("Delay", 0.1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    private final NumberValue odelay = new NumberValue("Open Delay", 0.1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    private final NumberValue cdelay = new NumberValue("Close Delay", 0.1, 0, 1, NumberValue.NUMBER_TYPE.FLOAT);
    private final BooleanValue aura = new BooleanValue("Aura", false);
    private final BooleanValue random = new BooleanValue("Random", false);
    private final ModeValue modeValue = new ModeValue("Mode", "Normal", new String[]{"Normal", "Funtime", "Smart"});
    private final BooleanValue key = new BooleanValue("Key", false){
        @Override
        public boolean isHidden() {
            return !modeValue.is("Funtime");
        }
    };

    private final BooleanValue skull = new BooleanValue("Skull", false){
        @Override
        public boolean isHidden() {
            return !modeValue.is("Funtime");
        }
    };
    private final BooleanValue totem = new BooleanValue("Totem", false){
        @Override
        public boolean isHidden() {
            return !modeValue.is("Funtime");
        }
    };
    private final BooleanValue scrap = new BooleanValue("Netherite Scrap", false){
        @Override
        public boolean isHidden() {
            return !modeValue.is("Funtime");
        }
    };

    private final List<BlockPos> openedChests = new ArrayList<>();
    public List<Integer> inventoryFixSlots = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil openTimer = new TimerUtil();
    public static boolean stealing;
    private InvManager invManager;
    private boolean clear;

    public ChestStealer() {
        super("ChestStealer", Category.Item, "auto steal chests");
     registerValue(auraRange);
     registerValue(delay);
     registerValue(odelay);
     registerValue(cdelay);
     registerValue(aura);
     registerValue(random);
     registerValue(modeValue);
     registerValue(key);
     registerValue(skull);
     registerValue(scrap);
     registerValue(totem);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent e) {
        if(e.isPre()){
            if (aura.isEnable()) {
                final int radius = auraRange.getValue().intValue();
                for (int x = -radius; x < radius; x++) {
                    for (int y = -radius; y < radius; y++) {
                        for (int z = -radius; z < radius; z++) {
                            final BlockPos pos = new BlockPos(mc.player.getPosX() + x, mc.player.getPosY() + y, mc.player.getPosZ() + z);
                            if (mc.world.getBlockState(pos).getBlock() == Blocks.CHEST && !openedChests.contains(pos)) {
                                if (mc.playerController.processRightClickBlock(mc.player, mc.world, pos, Direction.UP, new Vector3d(pos), Hand.MAIN_HAND) == ActionResultType.SUCCESS) {
                                    mc.player.swingArm(Hand.MAIN_HAND);
                                    final float[] rotations = RotationUtils.getFacingRotations2(pos.getX(), pos.getY(), pos.getZ());
                                    e.yaw = rotations[0];
                                    e.pitch = rotations[1];
                                    openedChests.add(pos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent e) {
        if (invManager == null)
            invManager = (InvManager) SigmaNG.getSigmaNG().moduleManager.getModule(InvManager.class);
        if (mc.player != null && mc.player.openContainer instanceof ChestContainer) {
            long oDelays = (long) (odelay.getValue().floatValue() * 50 * 10);
            oDelays = oDelays == 0 ? 0 : (oDelays + new Random().nextInt(100));
            if(!openTimer.hasTimeElapsed(oDelays)) return;
            ChestContainer chest = (ChestContainer) mc.player.openContainer;
            IInventory chestInv = chest.getLowerChestInventory();
            clear = true;

            List<Integer> slots = new ArrayList<>();
            for (int i = 0; i < chestInv.getSizeInventory(); i++) {
                ItemStack is = chestInv.getStackInSlot(i);
                if (is != ItemStack.EMPTY) {
                    switch (modeValue.getValue()){
                        case "Normal"->{
                            slots.add(i);
                        }
                        case "Smart"->{
                            if(!invManager.isBadItem(is, i, true))
                                slots.add(i);
                        }
                        case "Funtime"->{
                            if(skull.isEnable()){
                                if(is.getItem() instanceof SkullItem){
                                    slots.add(i);
                                }
                            }
                            if(key.isEnable()){
                                if(is.getTranslationKey().contains("tripwire")){
                                    slots.add(i);
                                }
                            }
                            if(totem.isEnable()){
                                if(is.getTranslationKey().contains("totem")){
                                    slots.add(i);
                                }
                            }
                            if(scrap.isEnable()){
                                if(is.getTranslationKey().contains("scrap")){
                                    slots.add(i);
                                }
                            }
                        }
                    }
                }
            }


            if(random.isEnable()){
                Collections.shuffle(slots);
            }
            long delays = (long) (delay.getValue().floatValue() * 50 * 10);
            delays = delays == 0 ? 0 : (delays + new Random().nextInt(100));
            long closeDelays = (long) (cdelay.getValue().floatValue() * 50 * 10);
            closeDelays = closeDelays == 0 ? 0 : (closeDelays + new Random().nextInt(100));
            if(timer.hasTimeElapsed(closeDelays, false)) {
                if (slots.isEmpty() || isInventoryFull()) {
                    items.clear();
                    clear = false;
                    stealing = false;
                    mc.player.closeScreen();
                    return;
                }
            }
            long finalDelays = delays;
            slots.forEach(s -> {
                ItemStack is = chestInv.getStackInSlot(s);
                Item item = is != ItemStack.EMPTY ? is.getItem() : null;
                if (item != Items.AIR && !items.contains(item) &&
                        (delay.getValue().floatValue() == 0 || timer.hasTimeElapsed(finalDelays, true))) {

//                    if(fix.isEnable()) {
//                        mc.playerController.windowClickFixed(chest.windowId, s, 0, ClickType.QUICK_MOVE, mc.player, false);
//                    }else{
                        mc.playerController.windowClick(chest.windowId, s, 0, ClickType.QUICK_MOVE, mc.player);
//                    }
                }
            });
        } else if (clear) {
            timer.reset();
            openTimer.reset();
            items.clear();
            clear = false;
            inventoryFixSlots.clear();
        }else{
            timer.reset();
            openTimer.reset();
        }
    }
    @Override
    public void onEnable() {
        openedChests.clear();
        super.onEnable();
    }

    private boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (mc.player.container.getSlot(i).getStack() == ItemStack.EMPTY) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onWorldEvent(WorldEvent event) {
        openedChests.clear();
        super.onWorldEvent(event);
    }

}
