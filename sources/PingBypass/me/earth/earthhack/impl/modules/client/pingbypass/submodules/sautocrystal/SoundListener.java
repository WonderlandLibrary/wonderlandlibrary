package me.earth.earthhack.impl.modules.client.pingbypass.submodules.sautocrystal;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class SoundListener extends ModuleListener<ServerAutoCrystal, PacketEvent.Receive<SPacketSoundEffect>>
{
    protected SoundListener(ServerAutoCrystal module)
    {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketSoundEffect> event)
    {
        if (module.soundR.getValue())
        {
            SPacketSoundEffect packet = event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
            {
                killEntities(new BlockPos(packet.getX(), packet.getY(), packet.getZ()));
            }
        }
    }

    private void killEntities(BlockPos pos)
    {
        mc.addScheduledTask(() ->
        {
            for (Entity entity : mc.world.loadedEntityList)
            {
                if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(pos) <= 36)
                {
                    entity.setDead();
                }
            }
        });
    }

}
