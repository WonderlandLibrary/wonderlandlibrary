/**
 * Time: 2:52:24 AM
 * Date: Jan 2, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventMouse;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;

/**
 * @author cool1
 */
public class AutoClicker extends Module {

    /**
     * @param data
     */
    public AutoClicker(ModuleData data) {
        super(data);
        settings.put(DELAY, new Setting<>(DELAY, 100, "Base click delay.", 25, 50, 500));
        settings.put(RANDOM, new Setting<>(RANDOM, true, "Randomize click delay."));
        settings.put(MIN, new Setting<>(MIN, 50, "Minimum click randomization.", 25, 25, 200));
        settings.put(MAX, new Setting<>(MAX, 100, "Maximum click randomization.", 25, 25, 200));
        settings.put(MOUSE, new Setting<>(MOUSE, true, "Click when mouse is held down."));
    }

    public static final String DELAY = "DELAY";
    public static final String RANDOM = "RANDOM";
    public static final String MIN = "MINRAND";
    public static final String MAX = "MAXRAND";
    public static final String MOUSE = "ON-MOUSE";

    public EntityLivingBase targ;
    Timer timer = new Timer();

	/*
     * (non-Javadoc)
	 * 
	 * @see EventListener#onEvent(Event)
	 */

    public static int randomNumber(int max, int min) {
        // Random rand = new Random();
        int ii = -min + (int) (Math.random() * ((max - (-min)) + 1));
        return ii;
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventMouse.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre() && mc.currentScreen == null && mc.thePlayer.isEntityAlive()) {
                if ((Boolean) settings.get(MOUSE).getValue() && !Mouse.isButtonDown(0)) {
                    return;
                }
                int delay = ((Number) settings.get(DELAY).getValue()).intValue();
                int minran = ((Number) settings.get(MIN).getValue()).intValue();
                int maxran = ((Number) settings.get(MAX).getValue()).intValue();
                boolean random = ((Boolean) settings.get(RANDOM).getValue());
                if (timer.delay(delay + (random ? randomNumber(minran, maxran) : 0))) {
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    mc.thePlayer.swingItem();
                    mc.clickMouse();
                    timer.reset();
                }
            }
        }
        if (event instanceof EventMouse) {
            EventMouse em = (EventMouse) event;
            if (em.getButtonID() == 1) {

            }
        }
    }
}
