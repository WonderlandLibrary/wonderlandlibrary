package me.crimp.claudius.event.events;

import me.crimp.claudius.event.EventStage;
import net.minecraft.entity.player.EntityPlayer;

public class DeathEvent extends EventStage {


        private final EntityPlayer player;
        public DeathEvent(EntityPlayer player) {
            this.player = player;
        }

        public EntityPlayer getPlayer() {
            return player;
        }
    }

