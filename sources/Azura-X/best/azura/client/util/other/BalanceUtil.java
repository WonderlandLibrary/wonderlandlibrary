package best.azura.client.util.other;

import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.impl.events.EventWorldChange;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.core.EventPriority;
import best.azura.eventbus.handler.EventHandler;
import net.minecraft.network.play.client.C03PacketPlayer;

public class BalanceUtil {
    private static long balance, lastPacket;
    public static final BalanceUtil INSTANCE = new BalanceUtil();
    @EventHandler(EventPriority.LOWEST)
    public void onEvent(final Event event) {
        if (event instanceof EventWorldChange)
            balance = lastPacket = 0;
        if (event instanceof EventSentPacket) {
            final EventSentPacket e = (EventSentPacket) event;
            if (e.getPacket() instanceof C03PacketPlayer) {
                if (lastPacket == 0) lastPacket = System.currentTimeMillis();
                long delay = System.currentTimeMillis() - lastPacket;
                balance += e.isCancelled() ? -delay : 50 - delay;
                lastPacket = System.currentTimeMillis();
            }
        }
    }
    public static long getBalance() {
        return balance;
    }
}