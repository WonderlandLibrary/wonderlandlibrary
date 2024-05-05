package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.TickEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modes.TestMode;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Module.Mod(displayName = "TestModule")
public class TestModule extends Module
{
	private TestMode test1;
	private TestMode test2;
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public TestModule() {
        this.test1 = new TestMode("Test1", true, this);
        this.test2 = new TestMode("Test2", false, this);
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.test1);
        OptionManager.getOptionList().add(this.test2);
        this.updateSuffix();
        super.preInitialize();
    }
    
    public void updateSuffix() {
        if (this.test1.getValue()) {
            this.setSuffix("Test1");
        }
        else if (this.test2.getValue()) {
        	this.setSuffix("Test2");
        }
    }
    
    public void enable() {
    	super.enable();
    }
    
    public void disable() {
    	super.disable();
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    @EventTarget
    public void onUpdate(final UpdateEvent event) {
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
    }
}
