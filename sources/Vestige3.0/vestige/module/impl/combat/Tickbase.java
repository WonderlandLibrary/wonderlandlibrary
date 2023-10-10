package vestige.module.impl.combat;

import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.PostMotionEvent;
import vestige.event.impl.RenderEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.IntegerSetting;

public class Tickbase extends Module {

    private Killaura killauraModule;

    private int counter = -1;
    public boolean freezing;

    private final IntegerSetting ticks = new IntegerSetting("Ticks", 3, 1, 10, 1);

    public Tickbase() {
        super("Tickbase", Category.COMBAT);
        this.addSettings(ticks);
    }

    @Override
    public void onEnable() {
        counter = -1;
        freezing = false;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onClientStarted() {
        killauraModule = Vestige.instance.getModuleManager().getModule(Killaura.class);
    }

    public int getExtraTicks() {
        if(counter-- > 0) {
            return -1;
        } else {
            freezing = false;
        }

        if(killauraModule.isEnabled() && (killauraModule.getTarget() == null || killauraModule.getDistanceToEntity(killauraModule.getTarget()) > killauraModule.range.getValue())) {
            if(killauraModule.findTarget(!killauraModule.mode.is("Fast Switch"), killauraModule.startingRange.getValue() + 0.75) != null && mc.thePlayer.hurtTime <= 2) {
                return counter = ticks.getValue();
            }
        }

        return 0;
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        if(freezing) {
            mc.thePlayer.posX = mc.thePlayer.lastTickPosX;
            mc.thePlayer.posY = mc.thePlayer.lastTickPosY;
            mc.thePlayer.posZ = mc.thePlayer.lastTickPosZ;
        }
    }

    @Listener
    public void onRender(RenderEvent event) {
        if(freezing) {
            mc.timer.renderPartialTicks = 0F;
        }
    }

}
