package org.alphacentauri.management.modules;

import java.util.ArrayList;
import java.util.UUID;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.commands.Command;
import org.alphacentauri.management.exceptions.SkidException;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.StringUtils;

public class Module {
   private String name;
   private String description;
   private String[] aliases;
   private Module.Category category;
   private int color;
   private String tag = "";
   private Property enabled = new Property(this, "Enabled", Boolean.valueOf(false), false);
   private Property bypass = new Property(this, "Bypassing_AntiCheat", AntiCheat.Vanilla, false);

   public Module(String name, String description, String[] aliases, Module.Category category, int color) {
      this.name = name;
      this.description = description;
      this.aliases = aliases;
      this.category = category;
      this.color = color;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void disable() {
      this.setEnabled(false);
   }

   public void enable() {
      this.setEnabled(true);
   }

   public String[] getAliases() {
      return this.aliases;
   }

   public int getColor() {
      return this.color;
   }

   public void setColor(int color) {
      this.color = color;
   }

   public void setEnabled(boolean enabled) {
      this.setEnabledSilent(enabled);
      if(AC.getConfig().showToggleMsg()) {
         AC.addChat(this.getName(), StringUtils.getModuleState(enabled));
      }

   }

   public boolean isEnabled() {
      return ((Boolean)this.enabled.value).booleanValue();
   }

   public String getDescription() {
      return this.description;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String tag) {
      this.tag = tag.length() > 0?"[" + tag + "]":tag;
   }

   public Module.Category getCategory() {
      return this.category;
   }

   public ArrayList autocomplete(Command cmd) {
      String[] args = cmd.getArgs();
      ArrayList<String> ret = new ArrayList();
      if(args.length == 1) {
         for(Property property : AC.getPropertyManager().ofModule(this)) {
            if(property.isVisible() && property.getName().equalsIgnoreCase(args[0])) {
               return ret;
            }
         }

         for(Property property : AC.getPropertyManager().ofModule(this)) {
            if(property.isVisible() && property.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
               ret.add(property.getName());
            }
         }
      } else if(args.length == 2) {
         for(Property property : AC.getPropertyManager().ofModule(this)) {
            if(property.isVisible() && property.getName().equalsIgnoreCase(args[0])) {
               if(property.value instanceof Boolean) {
                  if("true".startsWith(args[1])) {
                     ret.add("true");
                  }

                  if("false".startsWith(args[1])) {
                     ret.add("false");
                  }
               } else if(property.value instanceof Enum) {
                  for(Object o : ((Enum)property.value).getDeclaringClass().getEnumConstants()) {
                     if(o instanceof Enum && ((Enum)o).name().toLowerCase().startsWith(args[1].toLowerCase())) {
                        ret.add(((Enum)o).name());
                     }
                  }
               }
               break;
            }
         }

         return ret;
      }

      return ret;
   }

   public AntiCheat getBypass() {
      return (AntiCheat)this.bypass.value;
   }

   public void setAliases(String[] aliases) {
      this.aliases = aliases;
   }

   public void toggle() {
      this.setEnabled(!this.isEnabled());
   }

   public void toggleSilent() {
      this.setEnabledSilent(!this.isEnabled());
   }

   public void setEnabledSilent(boolean enabled) {
      this.enabled.value = Boolean.valueOf(enabled);
   }

   public void setCategory(Module.Category category) {
      this.category = category;
   }

   public boolean onCommand(Command cmd) {
      String[] args = cmd.getArgs();
      if(args.length == 0) {
         this.toggle();
         return true;
      } else if(args.length == 1) {
         for(Property property : AC.getPropertyManager().ofModule(this)) {
            if(property.getName().equalsIgnoreCase(args[0]) && property.isVisible()) {
               if(property.value instanceof Boolean) {
                  property.value = Boolean.valueOf(!((Boolean)property.value).booleanValue());
                  AC.addChat(this.getName(), property.getName() + " is now " + StringUtils.getModuleState(((Boolean)property.value).booleanValue()));
               } else {
                  AC.addChat(this.getName(), property.getName() + " = " + AC.getPropertyManager().serialize(property.value));
               }

               return true;
            }
         }

         AC.addChat(this.getName(), "No such property!");
         return true;
      } else {
         if(args.length >= 2) {
            String search = args[0];
            StringBuilder argsMerger = new StringBuilder();

            for(int i = 1; i < args.length; ++i) {
               argsMerger.append(args[i]).append(' ');
            }

            argsMerger.deleteCharAt(argsMerger.length() - 1);
            String input = argsMerger.toString();

            for(Property property : AC.getPropertyManager().ofModule(this)) {
               if(property.getName().equalsIgnoreCase(search) && property.isVisible()) {
                  try {
                     if(property.value instanceof String) {
                        property.value = input;
                     } else if(property.value instanceof Integer) {
                        int v = Integer.parseInt(input);
                        if((property.getName().equalsIgnoreCase("range") || property.getName().equalsIgnoreCase("reach")) && v < 0) {
                           AC.addChat(this.getName(), "Value Out of Range!");
                           return true;
                        }

                        property.value = Integer.valueOf(v);
                     } else if(property.value instanceof Boolean) {
                        boolean value;
                        if(input.equalsIgnoreCase("true")) {
                           value = true;
                        } else {
                           if(!input.equalsIgnoreCase("false")) {
                              AC.addChat(this.getName(), "Valid values: true/false");
                              return true;
                           }

                           value = false;
                        }

                        property.value = Boolean.valueOf(value);
                     } else if(property.value instanceof Float) {
                        float v = Float.parseFloat(input);
                        if((property.getName().equalsIgnoreCase("range") || property.getName().equalsIgnoreCase("reach")) && v < 0.0F) {
                           AC.addChat(this.getName(), "Value Out of Range!");
                           return true;
                        }

                        property.value = Float.valueOf(v);
                     } else if(property.value instanceof Double) {
                        double v = Double.parseDouble(input);
                        if((property.getName().equalsIgnoreCase("range") || property.getName().equalsIgnoreCase("reach")) && v < 0.0D) {
                           AC.addChat(this.getName(), "Value Out of Range!");
                           return true;
                        }

                        property.value = Double.valueOf(v);
                     } else if(property.value instanceof Character) {
                        property.value = Character.valueOf(input.charAt(0));
                     } else if(property.value instanceof Long) {
                        long v = Long.parseLong(input);
                        if((property.getName().equalsIgnoreCase("range") || property.getName().equalsIgnoreCase("reach")) && v < 0L) {
                           AC.addChat(this.getName(), "Value Out of Range!");
                           return true;
                        }

                        property.value = Long.valueOf(v);
                     } else if(property.value instanceof Short) {
                        short v = Short.parseShort(input);
                        if((property.getName().equalsIgnoreCase("range") || property.getName().equalsIgnoreCase("reach")) && v < 0) {
                           AC.addChat(this.getName(), "Value Out of Range!");
                           return true;
                        }

                        property.value = Short.valueOf(v);
                     } else if(property.value instanceof UUID) {
                        property.value = UUID.fromString(input);
                     } else if(property.value instanceof Enum) {
                        boolean found = false;

                        for(Object ec : ((Enum)property.value).getDeclaringClass().getEnumConstants()) {
                           Enum enumConstant = (Enum)ec;
                           if(enumConstant.name().equalsIgnoreCase(input)) {
                              property.value = ec;
                              found = true;
                              break;
                           }
                        }

                        if(!found) {
                           AC.addChat(this.getName(), property.value.getClass().getSimpleName() + " not found!");
                           return true;
                        }
                     } else {
                        if(!(property.value instanceof Iterable)) {
                           AC.addChat(this.getName(), "Unknown type...");
                           return false;
                        }

                        try {
                           throw new SkidException("Add Iterable parsing in Module");
                        } catch (SkidException var16) {
                           var16.printStackTrace();
                        }
                     }

                     AC.addChat(this.getName(), "Set " + property.getName() + " to " + input);
                     return true;
                  } catch (Exception var17) {
                     if(AC.isDebug()) {
                        var17.printStackTrace();
                     }
                     break;
                  }
               }
            }
         }

         AC.addChat(this.getName(), "Usage: " + AC.getConfig().getPrefix() + cmd.getCommand() + " [<property> <value>]");
         return false;
      }
   }

   public void setBypass(AntiCheat ac) {
      this.bypass.value = ac;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public static enum Category {
      Combat,
      World,
      Movement,
      Render,
      Exploits,
      Fun,
      Misc;
   }
}
