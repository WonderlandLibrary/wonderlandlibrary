// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.event.EventTarget;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class GuiWalk extends Feature
{
    public GuiWalk() {
        super("GuiWalk", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u0432 \u043e\u0442\u043a\u0440\u044b\u0442\u043e\u043c \u043a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440\u0435", Type.Player);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!(GuiWalk.mc.m instanceof bkn)) {
            GuiWalk.mc.t.X.i = Keyboard.isKeyDown(GuiWalk.mc.t.X.j());
            GuiWalk.mc.t.T.i = Keyboard.isKeyDown(GuiWalk.mc.t.T.j());
            GuiWalk.mc.t.V.i = Keyboard.isKeyDown(GuiWalk.mc.t.V.j());
            GuiWalk.mc.t.U.i = Keyboard.isKeyDown(GuiWalk.mc.t.U.j());
            GuiWalk.mc.t.W.i = Keyboard.isKeyDown(GuiWalk.mc.t.W.j());
            GuiWalk.mc.t.Z.i = Keyboard.isKeyDown(GuiWalk.mc.t.Z.j());
        }
    }
    
    @Override
    public void onDisable() {
        GuiWalk.mc.t.X.i = false;
        GuiWalk.mc.t.T.i = false;
        GuiWalk.mc.t.V.i = false;
        GuiWalk.mc.t.U.i = false;
        GuiWalk.mc.t.W.i = false;
        GuiWalk.mc.t.Z.i = false;
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
}
