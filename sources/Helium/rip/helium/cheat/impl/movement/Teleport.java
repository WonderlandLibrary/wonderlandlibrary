package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import org.lwjgl.input.Mouse;
import rip.helium.ChatUtil;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.combat.aura.Vec3;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.MovementUtils;
import rip.helium.utils.PlayerUtils;
import rip.helium.utils.Stopwatch;

public class Teleport extends Cheat {
    private final Stopwatch timer = new Stopwatch();
    private Vec3 target;
    private int stage;

    public Teleport() {
        super("Teleport", "Teleport!", CheatCategory.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.stage = 0;
    }

    @Collect
    public void sendPacket(SendPacketEvent event) {
        if (this.stage == 1 && !this.timer.hasPassed(6000L))
            event.setCancelled(true);

    }

    @Collect
    public void onRecieve(ProcessPacketEvent event) {
        if (this.stage == 1 && !this.timer.hasPassed(6000L) && (event.getPacket() instanceof S02PacketChat ||
                event.getPacket() instanceof S45PacketTitle))
            event.setCancelled(true);
    }

    @Collect
    public void onUpdate(PlayerUpdateEvent event) {
        int x = mc.objectMouseOver.getBlockPos().getX();
        int y = mc.objectMouseOver.getBlockPos().getY() + 1;
        int z = mc.objectMouseOver.getBlockPos().getZ();
        switch (this.stage) {
            case 0:
                if (Mouse.isButtonDown(1) && !mc.thePlayer.isSneaking() && mc.inGameHasFocus) {
                    this.timer.reset();
                    this.target = new Vec3(x, y, z);
                    this.stage = 1;
                }
                break;
            case 1:
                if (this.timer.hasPassed(6000L)) {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput(0.0F, 0.0F, true, true));
                    mc.thePlayer.setPosition(this.target.getX(), this.target.getY()
                            , this.target.getZ());
                    this.stage = 0;
                }
                break;
        }
        if (mc.thePlayer.hurtTime == 9)
            this.timer.reset();
    }

    @Collect
    public void onMove(PlayerMoveEvent event) {
        if ((((this.stage == 1) ? 1 : 0) & (!this.timer.hasPassed(6000L) ? 1 : 0)) != 0)
            MovementUtils.setSpeed(event, 0.0D);
    }
}
