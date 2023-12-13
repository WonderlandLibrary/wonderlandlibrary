package team.massacre.impl.event;

import team.massacre.api.event.Cancellable;
import team.massacre.api.event.Event;

public final class WindowClickEvent extends Cancellable implements Event {
   private final int windowId;
   private final int slot;
   private final int hotbarSlot;
   private final int mode;

   public WindowClickEvent(int windowId, int slot, int hotbarSlot, int mode) {
      this.windowId = windowId;
      this.slot = slot;
      this.hotbarSlot = hotbarSlot;
      this.mode = mode;
   }

   public int getWindowId() {
      return this.windowId;
   }

   public int getSlot() {
      return this.slot;
   }

   public int getHotbarSlot() {
      return this.hotbarSlot;
   }

   public int getMode() {
      return this.mode;
   }
}
