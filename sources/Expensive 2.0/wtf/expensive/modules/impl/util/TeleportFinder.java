package wtf.expensive.modules.impl.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.*;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;

import java.lang.reflect.Method;

@FunctionAnnotation(name = "Teleport Finder", type = Type.Util)
public class TeleportFinder extends Function {
    @Override
    public void onEvent(Event event) {

        if (event instanceof EventPacket e) {
           if (e.isReceivePacket()) {
               IPacket<?> packet = e.getPacket();

               for (Method m : packet.getClass().getMethods()) {
                   if (m.getName().toLowerCase().contains("entityid")) {
                       System.out.println(packet);
                   }
               }
           }
        }

    }
}
