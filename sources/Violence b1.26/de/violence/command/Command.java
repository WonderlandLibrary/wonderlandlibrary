package de.violence.command;

public abstract class Command {
   public abstract String getDescription();

   public abstract String getName();

   public abstract String getUsage();

   public abstract void onCommand(String[] var1);
}
