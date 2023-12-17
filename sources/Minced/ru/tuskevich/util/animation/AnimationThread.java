// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animation;

import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class AnimationThread extends Thread
{
    @Override
    public void run() {
        while (true) {
            try {
                while (true) {
                    GuiScreen.animation.update(Minecraft.getMinecraft().currentScreen != null);
                    EventManager.call(new EventAnimation());
                    Thread.sleep(20L);
                }
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
}
