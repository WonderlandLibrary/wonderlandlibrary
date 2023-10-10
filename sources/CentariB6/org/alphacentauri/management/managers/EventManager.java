package org.alphacentauri.management.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.alphacentauri.AC;
import org.alphacentauri.core.CloudAPI;
import org.alphacentauri.core.CoreListener;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.util.RotationUtils;

public class EventManager {
   private ArrayList listeners = new ArrayList();
   private ArrayList toRegister = new ArrayList();
   private ArrayList toUnRegister = new ArrayList();

   public EventManager() {
      this.registerListeners();
   }

   private void registerListeners() {
      this.listeners.addAll((Collection)AC.getModuleManager().all().stream().filter((module) -> {
         return module instanceof EventListener;
      }).map((module) -> {
         return (EventListener)module;
      }).collect(Collectors.toList()));
      this.listeners.add(new RotationUtils());
      this.listeners.add(new CoreListener());
      this.listeners.add(new CloudAPI());
      this.listeners.add(AC.getKeyBindManager());
      this.listeners.add(AC.getNotificationManager());
   }

   public void registerListener(EventListener listener) {
      this.toRegister.add(listener);
   }

   public void unregisterListener(EventListener listener) {
      this.toUnRegister.add(listener);
   }

   public void fireEvent(Event event) {
      for(int i = 0; i < this.listeners.size(); ++i) {
         EventListener listener = (EventListener)this.listeners.get(i);
         if(listener != null && (!(listener instanceof Module) || ((Module)listener).isEnabled())) {
            listener.onEvent(event);
         }
      }

      if(!this.toRegister.isEmpty()) {
         this.listeners.addAll(this.toRegister);
         this.toRegister.clear();
      }

      if(!this.toUnRegister.isEmpty()) {
         this.listeners.removeAll(this.toUnRegister);
         this.toUnRegister.clear();
      }

   }
}
