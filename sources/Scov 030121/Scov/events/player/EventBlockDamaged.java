package Scov.events.player;

import Scov.events.Cancellable;
import Scov.events.Event;
import net.minecraft.util.BlockPos;

public final class EventBlockDamaged extends Cancellable implements Event {
   private final BlockPos blockPos;

   public EventBlockDamaged(BlockPos blockPos) {
      this.blockPos = blockPos;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }
}
