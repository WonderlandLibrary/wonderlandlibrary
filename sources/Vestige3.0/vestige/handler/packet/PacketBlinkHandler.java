package vestige.handler.packet;

import lombok.Getter;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.FinalPacketSendEvent;
import vestige.util.IMinecraft;
import vestige.util.network.PacketUtil;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class PacketBlinkHandler implements IMinecraft {

    private final CopyOnWriteArrayList<Packet> packetsQueue = new CopyOnWriteArrayList<>();

    private boolean blinkingMove;
    private boolean blinkingPing;
    private boolean blinkingOther;

    private boolean clearedPackets;

    public PacketBlinkHandler() {
        Vestige.instance.getEventManager().register(this);
    }

    @Listener(20)
    public void onFinalSend(FinalPacketSendEvent event) {
        if(mc.thePlayer == null || mc.thePlayer.ticksExisted < 5) {
            if(!clearedPackets) {
                packetsQueue.clear();
                stopAll();
                clearedPackets = true;
            }
        } else {
            clearedPackets = false;
        }

        Packet packet = event.getPacket();

        if(!event.isCancelled()) {
            if(isMove(packet)) {
                if(blinkingMove) {
                    event.setCancelled(true);
                    packetsQueue.add(packet);
                }
            } else if(isPing(packet)) {
                if(blinkingPing) {
                    event.setCancelled(true);
                    packetsQueue.add(packet);
                }
            } else {
                if(blinkingOther) {
                    event.setCancelled(true);
                    packetsQueue.add(packet);
                }
            }
        }
    }

    public void startBlinkingMove() {
        this.blinkingMove = true;
    }

    public void releaseMove() {
        if(!packetsQueue.isEmpty()) {
            ArrayList<Packet> toRemove = new ArrayList<>();

            for(Packet packet : packetsQueue) {
                if(isMove(packet)) {
                    PacketUtil.sendPacketNoEvent(packet);
                    toRemove.add(packet);
                }
            }

            if(!toRemove.isEmpty()) {
                for(Packet p : toRemove) {
                    packetsQueue.remove(p);
                }
            }

            toRemove.clear();
        }
    }

    public void stopBlinkingMove() {
        this.releaseMove();

        this.blinkingMove = false;
    }

    public void startBlinkingPing() {
        this.blinkingPing = true;
    }

    public void releasePing() {
        if(!packetsQueue.isEmpty()) {
            ArrayList<Packet> toRemove = new ArrayList<>();

            for(Packet packet : packetsQueue) {
                if(isPing(packet)) {
                    PacketUtil.sendPacketNoEvent(packet);
                    toRemove.add(packet);
                }
            }

            if(!toRemove.isEmpty()) {
                for(Packet p : toRemove) {
                    packetsQueue.remove(p);
                }
            }

            toRemove.clear();
        }
    }

    public void stopBlinkingPing() {
        this.releasePing();

        this.blinkingPing = false;
    }

    public void startBlinkingOther() {
        this.blinkingOther = true;
    }

    public void releaseOther() {
        if(!packetsQueue.isEmpty()) {
            ArrayList<Packet> toRemove = new ArrayList<>();

            for(Packet packet : packetsQueue) {
                if(!isMove(packet) && !isPing(packet)) {
                    PacketUtil.sendPacketNoEvent(packet);
                    toRemove.add(packet);
                }
            }

            if(!toRemove.isEmpty()) {
                for(Packet p : toRemove) {
                    packetsQueue.remove(p);
                }
            }

            toRemove.clear();
        }
    }

    public void stopBlinkingOther() {
        this.releaseOther();

        this.blinkingOther = false;
    }

    public void clearMove() {
        if(!packetsQueue.isEmpty()) {
            ArrayList<Packet> toRemove = new ArrayList<>();

            for(Packet packet : packetsQueue) {
                if(isMove(packet)) {
                    toRemove.add(packet);
                }
            }

            if(!toRemove.isEmpty()) {
                for(Packet p : toRemove) {
                    packetsQueue.remove(p);
                }
            }

            toRemove.clear();
        }
    }

    public void clearPing() {
        if(!packetsQueue.isEmpty()) {
            ArrayList<Packet> toRemove = new ArrayList<>();

            for(Packet packet : packetsQueue) {
                if(isPing(packet)) {
                    toRemove.add(packet);
                }
            }

            if(!toRemove.isEmpty()) {
                for(Packet p : toRemove) {
                    packetsQueue.remove(p);
                }
            }

            toRemove.clear();
        }
    }

    public void clearOther() {
        if(!packetsQueue.isEmpty()) {
            ArrayList<Packet> toRemove = new ArrayList<>();

            for(Packet packet : packetsQueue) {
                if(!isMove(packet) && !isPing(packet)) {
                    toRemove.add(packet);
                }
            }

            if(!toRemove.isEmpty()) {
                for(Packet p : toRemove) {
                    packetsQueue.remove(p);
                }
            }

            toRemove.clear();
        }
    }

    public void startBlinkingAll() {
        this.blinkingMove = true;
        this.blinkingPing = true;
        this.blinkingOther = true;
    }

    public void releaseAll() {
        if(!packetsQueue.isEmpty()) {
            for(Packet packet : packetsQueue) {
                PacketUtil.sendPacketNoEvent(packet);
            }

            packetsQueue.clear();
        }
    }

    public void stopAll() {
        this.releaseAll();

        this.blinkingMove = false;
        this.blinkingPing = false;
        this.blinkingOther = false;
    }

    public boolean isBlinking() {
        return blinkingMove || blinkingPing || blinkingOther;
    }

    public boolean isBlinkingAll() {
        return blinkingMove && blinkingPing && blinkingOther;
    }

    public boolean isMove(Packet p) {
        return p instanceof C03PacketPlayer || p instanceof C0BPacketEntityAction;
    }

    public boolean isPing(Packet p) {
        return p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive;
    }

}