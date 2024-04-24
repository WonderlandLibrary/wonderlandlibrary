package me.crimp.claudius.mod.modules.misc;

import me.crimp.claudius.event.events.PacketEvent;
import me.crimp.claudius.mod.modules.Module;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class NoGlobalSounds
        extends Module {

    public NoGlobalSounds() {
        super("NoGlobalSounds", "Spawns", Category.Misc,false,false);
    }
//thanks to krazzy moneky for code i skid
    //TODO ADD PER SOUND SETTING

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive e) {
            if (e.getPacket() instanceof SPacketSoundEffect) {
                final SPacketSoundEffect sPacketSoundEffect = e.getPacket();
                if (sPacketSoundEffect.getCategory() == SoundCategory.WEATHER && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_LIGHTNING_THUNDER) {
                    e.setCanceled(true);
                }
            }
            if (e.getPacket() instanceof SPacketEffect) {
                final SPacketEffect sPacketEffect = e.getPacket();

                if (sPacketEffect.getSoundType() == 1038 || sPacketEffect.getSoundType() == 1023 || sPacketEffect.getSoundType() == 1028) {
                    e.setCanceled(true);
                }
            }
        }
    }
