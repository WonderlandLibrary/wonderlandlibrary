package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;

public class Target
  extends Command
{
  protected final Minecraft mc = Minecraft.getMinecraft();
  
  public Target(String[] names, String description)
  {
    super(names, description);
  }
  
  public void fire(String[] args)
  {
    if (args == null)
    {
      printUsage();
      return;
    }
    if (args.length > 0)
    {
      String name = args[0];
      if (this.mc.theWorld.getPlayerEntityByName(name) != null)
      {
        EntityLivingBase vip = this.mc.theWorld.getPlayerEntityByName(name);
        me.arithmo.module.impl.combat.Killaura.vip = vip;
        ChatUtil.printChat("§c[§fS§c]§7 Now targeting " + args[0]);
        
        return;
      }
      me.arithmo.module.impl.combat.Killaura.vip = null;
      ChatUtil.printChat("§c[§fS§c]§7 No entity with the name \"" + args[0] + "\" currently exists.");
    }
    printUsage();
  }
  
  public String getUsage()
  {
    return "Target <Target>";
  }
  
  public void onEvent(Event event) {}
}
