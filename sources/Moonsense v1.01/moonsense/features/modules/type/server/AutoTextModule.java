// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.server;

import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCKeyEvent;
import moonsense.enums.ModuleCategory;
import moonsense.utils.KeyBinding;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class AutoTextModule extends SCModule
{
    private final Setting command1;
    private final Setting hotkey1;
    private final Setting command2;
    private final Setting hotkey2;
    private final Setting command3;
    private final Setting hotkey3;
    private final Setting command4;
    private final Setting hotkey4;
    private final Setting command5;
    private final Setting hotkey5;
    private final Setting command6;
    private final Setting hotkey6;
    private final Setting command7;
    private final Setting hotkey7;
    private final Setting command8;
    private final Setting hotkey8;
    private final Setting command9;
    private final Setting hotkey9;
    private final Setting command10;
    private final Setting hotkey10;
    
    public AutoTextModule() {
        super("AutoText", "No description set!", 14);
        this.command1 = new Setting(this, "Message 1").setDefault("");
        this.hotkey1 = new Setting(this, "Hotkey 1").setDefault(new KeyBinding(-1));
        this.command2 = new Setting(this, "Message 2").setDefault("");
        this.hotkey2 = new Setting(this, "Hotkey 2").setDefault(new KeyBinding(-1));
        this.command3 = new Setting(this, "Message 3").setDefault("");
        this.hotkey3 = new Setting(this, "Hotkey 3").setDefault(new KeyBinding(-1));
        this.command4 = new Setting(this, "Message 4").setDefault("");
        this.hotkey4 = new Setting(this, "Hotkey 4").setDefault(new KeyBinding(-1));
        this.command5 = new Setting(this, "Message 5").setDefault("");
        this.hotkey5 = new Setting(this, "Hotkey 5").setDefault(new KeyBinding(-1));
        this.command6 = new Setting(this, "Message 6").setDefault("");
        this.hotkey6 = new Setting(this, "Hotkey 6").setDefault(new KeyBinding(-1));
        this.command7 = new Setting(this, "Message 7").setDefault("");
        this.hotkey7 = new Setting(this, "Hotkey 7").setDefault(new KeyBinding(-1));
        this.command8 = new Setting(this, "Message 8").setDefault("");
        this.hotkey8 = new Setting(this, "Hotkey 8").setDefault(new KeyBinding(-1));
        this.command9 = new Setting(this, "Message 9").setDefault("");
        this.hotkey9 = new Setting(this, "Hotkey 9").setDefault(new KeyBinding(-1));
        this.command10 = new Setting(this, "Message 10").setDefault("");
        this.hotkey10 = new Setting(this, "Hotkey 10").setDefault(new KeyBinding(-1));
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.SERVER;
    }
    
    @SubscribeEvent
    public void onKeyPress(final SCKeyEvent event) {
        final int keyCode = event.getKey();
        if (keyCode == this.hotkey1.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command1.getString());
        }
        if (keyCode == this.hotkey2.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command2.getString());
        }
        if (keyCode == this.hotkey3.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command3.getString());
        }
        if (keyCode == this.hotkey4.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command4.getString());
        }
        if (keyCode == this.hotkey5.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command5.getString());
        }
        if (keyCode == this.hotkey6.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command6.getString());
        }
        if (keyCode == this.hotkey7.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command7.getString());
        }
        if (keyCode == this.hotkey8.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command8.getString());
        }
        if (keyCode == this.hotkey9.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command9.getString());
        }
        if (keyCode == this.hotkey10.getValue().get(0).getKeyCode()) {
            this.mc.thePlayer.sendChatMessage(this.command10.getString());
        }
    }
}
