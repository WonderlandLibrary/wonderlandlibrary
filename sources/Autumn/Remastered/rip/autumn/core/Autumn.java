package rip.autumn.core;

import org.lwjgl.opengl.Display;
import rip.autumn.core.registry.impl.EventBusRegistry;
import rip.autumn.core.registry.impl.ManagerRegistry;

public final class Autumn {
   public static final Autumn INSTANCE = builder().name("Autumn").version("1.0.1").authors("MarkGG").build();
   public static final EventBusRegistry EVENT_BUS_REGISTRY = new EventBusRegistry();
   public static final ManagerRegistry MANAGER_REGISTRY = new ManagerRegistry();
   private final String name, version, author;

   private Autumn(String name, String version, String author) {
      this.name = name;
      this.version = version;
      this.author = author;
   }

   private static Autumn.Builder builder() {
      return new Autumn.Builder();
   }

   public String getName() {
      return this.name;
   }

   public String getVersion() {
      return this.version;
   }

   public String getAuthor() {
      return this.author;
   }

   public void start() {
      Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
      Display.setTitle(this.name + " " + this.version + " - Remastered by " + this.author);
   }

   public void stop() {
      MANAGER_REGISTRY.moduleManager.saveData();
   }

   public static class Builder {
      private String name, version, author;

      protected Builder() {
      }

      public Autumn.Builder name(String name) {
         this.name = name;
         return this;
      }

      public Autumn.Builder version(String version) {
         this.version = version;
         return this;
      }

      public Autumn.Builder authors(String author) {
         this.author = author;
         return this;
      }

      public final Autumn build() {
         return new Autumn(this.name, this.version, this.author);
      }
   }
}
