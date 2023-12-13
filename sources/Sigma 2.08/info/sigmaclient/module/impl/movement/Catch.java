package info.sigmaclient.module.impl.movement;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;
import info.sigmaclient.Client;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;

/**
 * Created by Arithmo on 5/6/2017 at 4:28 PM.
 */
public class Catch extends Module {

    private Timer timer = new Timer();
    private boolean saveMe;
    private String VOID = "VOID";
    private String DISTANCE = "DIST";

    public Catch(ModuleData data) {
        super(data);
        settings.put(VOID, new Setting<>(VOID, true, "Only catch when falling into void."));
        settings.put(DISTANCE, new Setting<>(DISTANCE, 5, "The fall distance needed to catch.", 1, 4, 10));
    }

    @RegisterEvent(events = EventMove.class)
    public void onEvent(Event event) {
        EventMove em = (EventMove) event;
        if((saveMe && timer.delay(150)) || mc.thePlayer.isCollidedVertically) {
            saveMe = false;
            timer.reset();
        }
        int dist = ((Number)settings.get(DISTANCE).getValue()).intValue();
        if (mc.thePlayer.fallDistance > dist && !Client.getModuleManager().isEnabled(Fly.class)) {
            if (!((Boolean) settings.get(VOID).getValue()) || !isBlockUnder()) {
                if(!saveMe) {
                    saveMe = true;
                    timer.reset();
                }
                mc.thePlayer.fallDistance = 0;
                em.setY(mc.thePlayer.motionY = 0);
            }
        }
    }

    private boolean isBlockUnder() {
        for (int i = (int) (mc.thePlayer.posY - 1); i > 0; i--) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
                return true;
            }
        }
        return false;
    }

}
