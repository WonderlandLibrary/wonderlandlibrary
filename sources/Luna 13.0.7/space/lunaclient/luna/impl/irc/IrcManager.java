package space.lunaclient.luna.impl.irc;

import java.io.IOException;
import java.io.PrintStream;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class IrcManager
  extends PircBot
{
  private final String IRC_HostName = "irc.mibbit.net";
  private final int IRC_HostPort = 6667;
  public static final String IRC_ChannelName = "#LunaClient";
  private static String username;
  
  public IrcManager(String username)
  {
    try
    {
      String firstname = username.substring(0, 1);
      int i = Integer.parseInt(firstname);
      System.out.println("[IRC] Usernames Cannont begin with numbers");
      username = "MC" + username;
    }
    catch (NumberFormatException localNumberFormatException) {}
    username = username;
  }
  
  public void connect()
  {
    setAutoNickChange(true);
    setName(username);
    changeNick(username);
    System.out.println("Connecting To IRC");
    try
    {
      connect("irc.mibbit.net", 6667);
    }
    catch (NickAlreadyInUseException e)
    {
      e.printStackTrace();
    }
    catch (IOException e2)
    {
      e2.printStackTrace();
    }
    catch (IrcException e3)
    {
      e3.printStackTrace();
    }
    joinChannel("#LunaClient");
    System.out.println(isConnected() ? "Connected to #LunaClient!" : "Failed to connect");
  }
}
