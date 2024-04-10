package info.sigmaclient.sigma.minimap.events;

import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;

public class FMLEvents
{
    public FMLEvents() {
       // EventManager.register(this);
    }
    
//    @EventTarget
//    public void playerTick(EventTick event) {
//        //if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
//            if (XaeroMinimap.getSettings() != null && (XaeroMinimap.getSettings().getDeathpoints() || XaeroMinimap.getSettings().getShowWaypoints() || XaeroMinimap.getSettings().getShowIngameWaypoints())) {
//                Minimap.updateWaypoints();
//            }
//           /* else if (Minimap.waypoints != null) {
//                Minimap.waypoints = null;
//            }*/
//            final Minecraft mc = XaeroMinimap.mc;
//            for (int i = 0; i < this.keyEvents.size(); ++i) {
//                final KeyEvent ke = this.keyEvents.get(i);
//                if (mc.currentScreen == null) {
//                    XaeroMinimap.ch.keyDown(ke.kb, ke.tickEnd, ke.isRepeat);
//                }
//                if (!ke.isRepeat) {
//                    if (!this.oldEventExists(ke.kb)) {
//                        this.oldKeyEvents.add(ke);
//                    }
//                    this.keyEvents.remove(i);
//                    --i;
//                }
//                else if (!ControlsHandler.isDown(ke.kb)) {
//                    XaeroMinimap.ch.keyUp(ke.kb, ke.tickEnd);
//                    this.keyEvents.remove(i);
//                    --i;
//                }
//            }
//            for (int i = 0; i < this.oldKeyEvents.size(); ++i) {
//                final KeyEvent ke = this.oldKeyEvents.get(i);
//                if (!ControlsHandler.isDown(ke.kb)) {
//                    XaeroMinimap.ch.keyUp(ke.kb, ke.tickEnd);
//                    this.oldKeyEvents.remove(i);
//                    --i;
//                }
//            }
//      //  }
//    }
//
//    @EventTarget
//    public void keyInput(EventKeyPressed event) {
//        if (XaeroMinimap.mc.thePlayer != null) {
//            final Minecraft mc = XaeroMinimap.mc;
//            if (mc.currentScreen == null) {
//                for (int i = 0; i < mc.gameSettings.keyBindings.length; ++i) {
//                    try {
//                        if (mc.currentScreen == null && !this.eventExists(mc.gameSettings.keyBindings[i]) && ControlsHandler.isDown(mc.gameSettings.keyBindings[i])) {
//                            this.keyEvents.add(new KeyEvent(mc.gameSettings.keyBindings[i], false, ModSettings.isKeyRepeat(mc.gameSettings.keyBindings[i]), true));
//                        }
//                    }
//                    catch (Exception ex) {}
//                }
//            }
//        }
//    }
}
