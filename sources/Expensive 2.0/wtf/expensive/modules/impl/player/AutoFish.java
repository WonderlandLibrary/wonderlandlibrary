package wtf.expensive.modules.impl.player;

import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.Hand;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.misc.TimerUtil;

@FunctionAnnotation(name = "AutoFish", type = Type.Player)
public class AutoFish extends Function {

    private final TimerUtil delay = new TimerUtil();
    private boolean isHooked = false;
    private boolean needToHook = false;

    @Override
    public void onEvent(final Event event) {
        if (mc.player == null || mc.world == null) return;

        if (event instanceof EventPacket e) {
            if (e.getPacket() instanceof SPlaySoundEffectPacket p) {
                if (p.getSound().getName().getPath().equals("entity.fishing_bobber.splash")) {
                    isHooked = true;
                    delay.reset();
                    //ClientUtil.sendMesage("–€¡¿!");
                }
            }
        }

        if (event instanceof EventUpdate e) {
            if (delay.hasTimeElapsed(600) && isHooked) {
                mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                isHooked = false;
                needToHook = true;
                //ClientUtil.sendMesage("«¿¡–¿À!");
                delay.reset();
            }

            if (delay.hasTimeElapsed(300) && needToHook) {
                mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                needToHook = false;
                //ClientUtil.sendMesage("’”…Õ”À!");
                delay.reset();
            }
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        delay.reset();
        isHooked = false;
        needToHook = false;
    }
}
