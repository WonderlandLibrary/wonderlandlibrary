package de.theBest.MythicX.modules.world;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.events.EventPreMotion;
import de.theBest.MythicX.events.EventSyncItem;
import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.events.Movefix;
import de.theBest.MythicX.modules.Module;
import de.theBest.MythicX.utils.RotationUtil;
import de.Hero.settings.Setting;
import eventapi.EventTarget;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", Type.World, 0, Category.WORLD, Color.green, "Auto places blocks below you");
    }
    public static final BlackList blackList = new BlackList();
    public static boolean legit;
    private int slot;
    @EventTarget
    public void onRotate(EventPreMotion e){
        e.setPitch(RotationUtil.pitch);
        e.setYaw(RotationUtil.yaw);
        RotationUtil.setYaw(mc.thePlayer.rotationYaw + 180, (float) MythicX.setmgr.getSettingByName("ScaffoldRotationSpeed").getValDouble());
        RotationUtil.setPitch(82,(float) MythicX.setmgr.getSettingByName("ScaffoldRotationSpeed").getValDouble());
    }

    @EventTarget
    public void syncItem(EventSyncItem eventSyncItem) {
        if (getBlockSlot() != -1) {
            ((EventSyncItem) eventSyncItem).setSlot(this.slot = getBlockSlot());
        }

    }

    @EventTarget
    public void onmove (Movefix e) {
        if(MythicX.setmgr.getSettingByName("ScaffoldMoveFix").getValBoolean()) {
            e.setYaw(RotationUtil.yaw + 180);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {


        mc.gameSettings.keyBindSprint.pressed = false;
        mc.thePlayer.setSprinting(false);
        final BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if(MythicX.setmgr.getSettingByName("Mode").getValString().equalsIgnoreCase("Legit")){
            legit = true;
            if (mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air) {
                mc.gameSettings.keyBindSneak.pressed = true;
                mc.thePlayer.setSneaking(true);
                rightClickMouse(mc.thePlayer.inventory.getStackInSlot(slot), slot);
            } else {
                mc.gameSettings.keyBindSneak.pressed = false;

                Object onDisable = null;
                Object onDisable1 = null;
                mc.gameSettings.keybindSneak.pressed = false;
            }
        }else if(MythicX.setmgr.getSettingByName("Mode").getValString().equalsIgnoreCase("SafeWalk")) {
            legit = false;
            if (mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air) {
                rightClickMouse(mc.thePlayer.inventory.getStackInSlot(slot), slot);
            }
        }
    }

    @Override
    public void onDisable() {

        mc.gameSettings.keyBindSprint.pressed = false;
        mc.thePlayer.setSprinting(false);
        mc.thePlayer.setSneaking(false);
    }

    @Override
    public void onEnable() {

    }
    public int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack s = mc.thePlayer.inventory.getStackInSlot(i);
            if (s != null
                    && s.getItem() instanceof ItemBlock
                    && blackList.isNotBlacklisted(
                    Item.itemRegistry.getNameForObject(s.getItem()).toString())) return i;
        }
        return -1;
    }
    static class BlackList {

        List<String> stringids = new ArrayList<>();

        public BlackList() {
            addID("minecraft:wooden_slab");
            addID("minecraft:stone_slab");
            addID("minecraft:banner");
            addID("minecraft:beacon");
            addID("minecraft:trapped_chest");
            addID("minecraft:chest");
            addID("minecraft:anvil");
            addID("minecraft:enchanting_table");
            addID("minecraft:crafting_table");
            addID("minecraft:furnace");
            addID("minecraft:banner");
            addID("minecraft:wall_banner");
            addID("minecraft:standing_banner");
            addID("minecraft:web");
            addID("minecraft:sapling");
            addID("minecraft:ender_chest");
            addID("minecraft:tnt");
        }

        public boolean isNotBlacklisted(String blockID) {
            return !stringids.contains(blockID);
        }

        public void addID(String id) {
            stringids.add(id);
        }
    }
    public void rightClickMouse(ItemStack itemstack, int slot) {
        if (!mc.playerController.getIsHittingBlock()) {
            mc.rightClickDelayTimer = 4;
            try {
                switch (mc.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        if (mc.playerController.isPlayerRightClickingOnEntity(
                                mc.thePlayer, mc.objectMouseOver.entityHit, mc.objectMouseOver)) {
                        } else if (mc.playerController.interactWithEntitySendPacket(
                                mc.thePlayer, mc.objectMouseOver.entityHit)) {
                            mc.thePlayer.jump();
                        }

                        break;

                    case BLOCK:
                        BlockPos blockpos = mc.objectMouseOver.getBlockPos();
                        if (mc.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
                            int i = itemstack != null ? itemstack.stackSize : 0;
                            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemstack, blockpos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec))
                                mc.thePlayer.swingItem();
                            if (itemstack == null) return;
                            if (itemstack.stackSize == 0) mc.thePlayer.inventory.mainInventory[slot] = null;
                        }
                    default:
                        break;
                }
            } catch (NullPointerException e) {

            }
        }
    }  @Override
    public void setup(){
        ArrayList<String> options = new ArrayList<>();
        options.add("Legit");
        options.add("SafeWalk");
        MythicX.setmgr.rSetting(new Setting("Mode", this, "Legit", options));
        MythicX.setmgr.rSetting(new Setting("ScaffoldMoveFix", this, false));
        MythicX.setmgr.rSetting(new Setting("ScaffoldRotationSpeed", this, 50,1,360,true));
    }


}