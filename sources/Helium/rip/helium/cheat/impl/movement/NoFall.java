package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.StringsProperty;

public class NoFall extends Cheat {
    public BooleanProperty fastfall;
    boolean canmeme;
    private final StringsProperty mode;

    public NoFall() {
        super("NoFall", "Negates fall damage on hypickle.", CheatCategory.MOVEMENT);
        this.mode = new StringsProperty("Mode", "change the mode.", null, false, true, new String[]{"Spoof", "Ghostly"}, new Boolean[]{true, false});
        fastfall = new BooleanProperty("Fast Fall", "Falls faster.", null, false);
        this.registerProperties(mode, fastfall);
    }

    public static boolean isBlockUnder() { // friend helped make this
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

    public void onEnable() {
    }

    @Collect
    public void onPlayerUpdate(final PlayerUpdateEvent playerUpdateEvent) {
        if (mode.getValue().get("Spoof")) {
            if (mc.thePlayer.fallDistance >= 2.75) {
                playerUpdateEvent.setOnGround(true);
            }
        } else if (mode.getValue().get("Ghostly")) {
            if (mc.thePlayer.fallDistance >= 2F && isBlockUnder()) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer(true));
                if (!fastfall.getValue()) {
                    mc.thePlayer.fallDistance = 0;
                }
            }
        }

        if (!Helium.instance.cheatManager.isCheatEnabled("Flight")) {
            if (fastfall.getValue() && isBlockUnder()) {
                if (mc.thePlayer.fallDistance >= 2.75) {
                    mc.thePlayer.motionY -= 0.5;
                }
            }
        }
    }
}