// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules;

import net.minecraft.client.Minecraft;
import moonsense.event.impl.SCRenderEvent;
import org.lwjgl.input.Keyboard;
import moonsense.event.impl.SCClientTickEvent;
import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCKeyEvent;
import moonsense.event.EventBus;
import moonsense.MoonsenseClient;
import moonsense.enums.ModuleCategory;
import moonsense.utils.KeyBinding;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class DabModule extends SCModule
{
    public static DabModule INSTANCE;
    private final Setting keybind;
    private final InputEvent evt;
    
    public DabModule() {
        super("Dab Mod", "Allows your character to perform a dab move!");
        DabModule.INSTANCE = this;
        this.evt = new InputEvent();
        this.keybind = new Setting(this, "Keybind").setDefault(new KeyBinding(34));
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        MoonsenseClient.INSTANCE.getEventManager();
        EventBus.register(this.evt);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        MoonsenseClient.INSTANCE.getEventManager();
        EventBus.unregister(this.evt);
    }
    
    public InputEvent getInputEvent() {
        return this.evt;
    }
    
    public final class InputEvent
    {
        public boolean dabbing;
        public boolean printed;
        public final int MAX_DABBING_HELD = 8;
        public int dabbingHeld;
        public int prevDabbingHeld;
        public float firstPersonPartialTicks;
        
        public InputEvent() {
            this.dabbing = false;
            this.printed = false;
            this.dabbingHeld = 0;
            this.prevDabbingHeld = 0;
        }
        
        @SubscribeEvent
        public void onKeyInput(final SCKeyEvent event) {
        }
        
        @SubscribeEvent
        public void onTick(final SCClientTickEvent event) {
            if (Keyboard.isKeyDown(((KeyBinding)DabModule.this.keybind.getObject()).getKeyCode())) {
                if (!this.dabbing) {
                    this.dabbing = true;
                }
            }
            else {
                this.dabbing = false;
            }
            this.prevDabbingHeld = this.dabbingHeld;
            if (this.dabbing && this.dabbingHeld < 8) {
                ++this.dabbingHeld;
            }
            else if (!this.dabbing && this.dabbingHeld > 0) {
                --this.dabbingHeld;
            }
        }
        
        @SubscribeEvent
        public void onRender(final SCRenderEvent event) {
            this.firstPersonPartialTicks = Minecraft.getMinecraft().getTimer().renderPartialTicks;
        }
    }
}
