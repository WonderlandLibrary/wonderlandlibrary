package de.violence.irc;

public abstract class IrcPacket {
   public abstract String getName();

   public abstract void onReceivePacket(String var1);
}
