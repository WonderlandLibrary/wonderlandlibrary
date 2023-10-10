package vestige.handler.client;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.FinalPacketSendEvent;
import vestige.event.impl.PacketReceiveEvent;
import vestige.util.IMinecraft;

@Getter
public class BalanceHandler implements IMinecraft {

    @Setter
    private long balance;

    private long lastNanoTime;

    public BalanceHandler() {
        Vestige.instance.getEventManager().register(this);
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(event.getPacket() instanceof S08PacketPlayerPosLook) {
            balance -= 50000000;
        }
    }

    @Listener
    public void onFinalSend(FinalPacketSendEvent event) {
        if(event.getPacket() instanceof C03PacketPlayer) {
            if(!event.isCancelled()) {
                long nanoTime = System.nanoTime();

                if(!mc.getNetHandler().doneLoadingTerrain || mc.thePlayer.ticksExisted < 30) {
                    balance = 0;
                    lastNanoTime = nanoTime - 50000000;
                }

                balance += 50000000;

                balance -= nanoTime - lastNanoTime;

                lastNanoTime = nanoTime;
            }
        }
    }

    public long getBalanceInMS() {
        return (long) (balance / 1E6);
    }

}
