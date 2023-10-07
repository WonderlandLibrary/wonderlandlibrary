package wtf.monsoon.api.auth;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.auth.database.Database;
import wtf.monsoon.api.auth.util.Encryption;
import wtf.monsoon.api.auth.util.HWID;
import wtf.monsoon.api.auth.util.Security;
import wtf.monsoon.impl.ui.menu.MonsoonMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Authentication extends Thread {

	public Minecraft mc = Minecraft.getMinecraft();

	public String key = "1behk23kbb2kf8o22";
	public String status;
	public String username;
	public String password;
	public String hwid;

	public Authentication(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	@Override
	public void run()
	{
		try
		{
			Database.establishUsers();

			if (Database.isUser(getEncryptedAuthString(this.username, this.password, this.key)) && !Security.wiresharkRunning()) {
				System.out.println(Database.isUser(getEncryptedAuthString(this.username, this.password, this.key)) + "");
				Monsoon.INSTANCE.setAuthorized(true);
				Monsoon.INSTANCE.setMonsoonName(this.username);

				this.status = EnumChatFormatting.GREEN + "Logged in! Welcome to Monsoon, " + this.username + "!";

				mc.displayGuiScreen(new MonsoonMainMenu());

				return;
			} else {
				Monsoon.INSTANCE.setAuthorized(false);
			}

		}
		catch (Exception e)
		{
			System.out.println("Error with authentication");
		}
	}

	public static String getAuthString(String inputUsername, String inputPassword)
	{
		return inputUsername + "::" + inputPassword + "::" + HWID.hwid;
	}

	public static String getEncryptedAuthString(String inputUsername, String inputPassword, String key)
	{
		String authString = getAuthString(inputUsername, inputPassword);

		return Encryption.hashMD5(Encryption.encryptAES(authString, key));
	}

	public String getStatus()
	{
		return this.status;
	}

}