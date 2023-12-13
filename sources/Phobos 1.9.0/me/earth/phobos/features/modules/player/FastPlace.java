package me.earth.phobos.features.modules.player;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.InventoryUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemMinecart;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastPlace
        extends Module {
    private final Setting<Boolean> all = this.register(new Setting<Boolean>("All", false));
    private final Setting<Boolean> obby = this.register(new Setting<Object>("Obsidian", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> enderChests = this.register(new Setting<Object>("EnderChests", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> crystals = this.register(new Setting<Object>("Crystals", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> exp = this.register(new Setting<Object>("Experience", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> Minecart = this.register(new Setting<Object>("Minecarts", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> feetExp = this.register(new Setting<Boolean>("ExpFeet", false));
    private final Setting<Boolean> fastCrystal = this.register(new Setting<Boolean>("PacketCrystal", false));
    private BlockPos mousePos = null;

    public FastPlace() {
        super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.feetExp.getValue().booleanValue()) {
            boolean offHand;
            boolean mainHand = FastPlace.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE;
            boolean bl = offHand = FastPlace.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE;
            if (FastPlace.mc.gameSettings.keyBindUseItem.isKeyDown() && (FastPlace.mc.player.getActiveHand() == EnumHand.MAIN_HAND && mainHand || FastPlace.mc.player.getActiveHand() == EnumHand.OFF_HAND && offHand)) {
                Phobos.rotationManager.lookAtVec3d(FastPlace.mc.player.getPositionVector());
            }
        }
    }

    @Override
    public void onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class) && this.exp.getValue().booleanValue()) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        if (InventoryUtil.holdingItem(BlockObsidian.class) && this.obby.getValue().booleanValue()) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        if (InventoryUtil.holdingItem(BlockEnderChest.class) && this.enderChests.getValue().booleanValue()) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        if (InventoryUtil.holdingItem(ItemMinecart.class) && this.Minecart.getValue().booleanValue()) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        if (this.all.getValue().booleanValue()) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        if (InventoryUtil.holdingItem(ItemEndCrystal.class) && (this.crystals.getValue().booleanValue() || this.all.getValue().booleanValue())) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
        if (this.fastCrystal.getValue().booleanValue() && FastPlace.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            boolean offhand;
            boolean bl = offhand = FastPlace.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
            if (offhand || FastPlace.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                RayTraceResult result = FastPlace.mc.objectMouseOver;
                if (result == null) {
                    return;
                }
                switch (result.typeOfHit) {
                    case MISS: {
                        this.mousePos = null;
                        break;
                    }
                    case BLOCK: {
                        this.mousePos = FastPlace.mc.objectMouseOver.getBlockPos();
                        break;
                    }
                    case ENTITY: {
                        Entity entity;
                        if (this.mousePos == null || (entity = result.entityHit) == null || !this.mousePos.equals(new BlockPos(entity.posX, entity.posY - 1.0, entity.posZ)))
                            break;
                        FastPlace.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                }
            }
        }
    }
}

