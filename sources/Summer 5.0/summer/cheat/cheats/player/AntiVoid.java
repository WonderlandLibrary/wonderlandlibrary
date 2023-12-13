package summer.cheat.cheats.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.TimerUtils;
import summer.cheat.cheats.movement.Flight;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;

public class AntiVoid extends Cheats {
    private boolean hasfallen;

    public Minecraft mc = Minecraft.getMinecraft();

    public AntiVoid() {
        super("AntiVoid", "Get out of void", Selection.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (e.isPre() && !isBlockUnderneath() && Minecraft.thePlayer.fallDistance > 2.85F
                && !CheatManager.getInstance(Flight.class).isToggled()) {
            e.setY(EventUpdate.getY() + 8.0D);
            this.hasfallen = true;
        }
    }

    private boolean isBlockUnderneath() {
        boolean blockUnderneath = false;
        for (int i = 0; i < Minecraft.thePlayer.posY + 2.0D; i++) {
            BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, i, Minecraft.thePlayer.posZ);
            if (!(Minecraft.theWorld.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockAir))
                blockUnderneath = true;
        }
        return blockUnderneath;
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
        Timer.timerSpeed = 1.0F;
    }
}