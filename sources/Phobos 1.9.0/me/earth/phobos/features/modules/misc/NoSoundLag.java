package me.earth.phobos.features.modules.misc;

import com.google.common.collect.Sets;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.combat.AutoCrystal;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Set;

public class NoSoundLag
        extends Module {
    private static final Set<SoundEvent> BLACKLIST;
    private static NoSoundLag instance;

    static {
        BLACKLIST = Sets.newHashSet(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER);
    }

    public Setting<Boolean> crystals = this.register(new Setting<Boolean>("Crystals", true));
    public Setting<Boolean> armor = this.register(new Setting<Boolean>("Armor", true));
    public Setting<Float> soundRange = this.register(new Setting<Float>("SoundRange", Float.valueOf(12.0f), Float.valueOf(0.0f), Float.valueOf(12.0f)));

    public NoSoundLag() {
        super("NoSoundLag", "Prevents Lag through sound spam.", Module.Category.MISC, true, false, false);
        instance = this;
    }

    public static NoSoundLag getInstance() {
        if (instance == null) {
            instance = new NoSoundLag();
        }
        return instance;
    }

    public static void removeEntities(SPacketSoundEffect packet, float range) {
        BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
        ArrayList<Entity> toRemove = new ArrayList<Entity>();
        for (Entity entity : NoSoundLag.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistanceSq(pos) <= MathUtil.square(range)))
                continue;
            toRemove.add(entity);
        }
        for (Entity entity : toRemove) {
            entity.setDead();
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event != null && event.getPacket() != null && NoSoundLag.mc.player != null && NoSoundLag.mc.world != null && event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = event.getPacket();
            if (this.crystals.getValue().booleanValue() && packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE && (AutoCrystal.getInstance().isOff() || !AutoCrystal.getInstance().sound.getValue().booleanValue() && AutoCrystal.getInstance().threadMode.getValue() != AutoCrystal.ThreadMode.SOUND)) {
                NoSoundLag.removeEntities(packet, this.soundRange.getValue().floatValue());
            }
            if (BLACKLIST.contains(packet.getSound()) && this.armor.getValue().booleanValue()) {
                event.setCanceled(true);
            }
        }
    }
}
