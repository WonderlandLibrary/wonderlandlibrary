package me.rhys.base.command;

import java.util.Arrays;
import net.minecraft.client.entity.EntityPlayerSP;

public abstract class Command {
  private final String label;
  
  private final String usage;
  
  private final String description;
  
  private final String[] aliases;
  
  public String getLabel() {
    return this.label;
  }
  
  public String getUsage() {
    return this.usage;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public String[] getAliases() {
    return this.aliases;
  }
  
  public Command(String label, String usage, String description, String... aliases) {
    this.label = label;
    this.usage = usage;
    this.description = description;
    this.aliases = aliases;
  }
  
  public Command(String label, String description, String... aliases) {
    this(label, "", description, aliases);
  }
  
  public boolean isAlias(String label) {
    if (this.label.equalsIgnoreCase(label) || 
      Arrays.<String>stream(this.aliases).filter(s -> s.equalsIgnoreCase(label)).findFirst().orElse(null) != null)
      return true; 
    return false;
  }
  
  public abstract boolean handle(EntityPlayerSP paramEntityPlayerSP, String paramString, String[] paramArrayOfString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\command\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */