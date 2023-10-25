package net.minusmc.ravenb4.module.modules.combat;

import io.netty.buffer.Unpooled
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting
import java.nio.ByteBuffer


class AntiKnockback: Module("AntiKnockback", ModuleCategory.world){

    private val desc = DescriptionSetting("Description","Overrides Velocity.");

    private val horizontal = SliderSetting("Horizontal", 0.0, 0.0, 100.0, 1.0);
    private val vertical = SliderSetting("Vertical", 0.0, 0.0, 100.0, 1.0);

    //private val cancelExplosion = TickSetting("Cancel Explosion", true);
    private val damageBoost = TickSetting("Damage Boost", false);
    private val boostMutiplier = SliderSetting("Boost multiplier", 2.0, 1.0, 8.0, 0.1);
    private val groundCheck = TickSetting("Ground Check", false);

    init {
        this.settings.add(desc);

        this.settings.add(horizontal);
        this.settings.add(vertical);

        //this.settings.add(cancelExplosion); -- Doesn't matter whatsoever
        this.settings.add(damageBoost);
        this.settings.add(boostMutiplier);
        this.settings.add(groundCheck);
    }

    //Gosh, why the heck he do it like this, where is the packet event :sob:
    //Retard can't even make a proper packet event or what?
    fun handleVelocityPacket(velocity: S12PacketEntityVelocity) {
        if(groundCheck.get() && !mc.thePlayer.onGround) return;

        if(damageBoost.get()) {
            //velocity = S12PacketEntityVelocity(velocity.getEntityID(), (velocity.motionX * boostMutiplier.get()),
                //(velocity.motionY * boostMutiplier.get()), (velocity.motionZ * boostMutiplier.get()));
            val buffer = PacketBuffer(Unpooled.buffer());
            buffer.writeVarIntToBuffer(velocity.entityID);
            buffer.writeShort((velocity.motionX * boostMutiplier.get()).toInt());
            buffer.writeShort((velocity.motionY * boostMutiplier.get()).toInt());
            buffer.writeShort((velocity.motionZ * boostMutiplier.get()).toInt());
            velocity.readPacketData(buffer);
            buffer.release();
        }

        val buffer = PacketBuffer(Unpooled.buffer());
        buffer.writeVarIntToBuffer(velocity.entityID);
        buffer.writeShort((velocity.motionX * (horizontal.get() / 100)).toInt());
        buffer.writeShort((velocity.motionY * (vertical.get() / 100)).toInt());
        buffer.writeShort((velocity.motionZ * (horizontal.get() / 100)).toInt());
        velocity.readPacketData(buffer);
        buffer.release();
    }

}