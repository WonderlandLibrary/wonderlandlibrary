package cn.hanabi.command;

import com.darkmagician6.eventapi.EventManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public abstract class Command {
   private final String name;
   private final String[] aliases;
   protected static final Minecraft mc = Minecraft.getMinecraft();

   protected Command(String name, String... aliases) {
      this.name = name;
      this.aliases = aliases;
      EventManager.register(this);
   }

   public abstract void run(String var1, String[] var2);

   public abstract List autocomplete(int var1, String[] var2);

   public boolean match(String name) {
      String[] var2 = this.aliases;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String alias = var2[var4];
         if (alias.equalsIgnoreCase(name)) {
            return true;
         }
      }

      return this.name.equalsIgnoreCase(name);
   }

   @NotNull List getNameAndAliases() {
      List l = new ArrayList();
      l.add(this.name);
      l.addAll(Arrays.asList(this.aliases));
      return l;
   }
}
