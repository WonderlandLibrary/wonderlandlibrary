package slavikcodd3r.rainbow.module.modes;

import net.minecraft.client.gui.ScaledResolution;
import slavikcodd3r.rainbow.event.events.MoveEvent;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.Render2DEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.option.OptionManager;
import slavikcodd3r.rainbow.option.types.BooleanOption;

public class ScaffoldMode extends BooleanOption
{
    public ScaffoldMode(final String name, final boolean value, final Module module) {
        super(name, name, value, module);
    }
    
    @Override
    public void setValue(final Boolean value) {
        if (value) {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof ScaffoldMode) {
                    ((BooleanOption)option).setValueHard(false);
                }
            }
        }
        else {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof ScaffoldMode && option != this) {
                    ((BooleanOption)option).setValueHard(true);
                    break;
                }
            }
        }
        super.setValue(value);
    }
    
    public boolean enable() {
        return this.getValue();
    }
    
    public boolean onUpdate(final UpdateEvent event) {
        return this.getValue();
    }
    
    public boolean onPacket(final PacketSendEvent event) {
        return this.getValue();
    }
    
    private boolean onRender2D(final Render2DEvent event) {
        return this.getValue();
    }
    
    public boolean onMove(final MoveEvent event) {
        return this.getValue();
    }    
    
    public boolean disable() {
        return true;
    }
}
