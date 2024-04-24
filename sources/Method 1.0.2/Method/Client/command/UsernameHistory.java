package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Command.CommandFeatures(description = "Finds username's past", Syntax = "NameHistory <username>")

public class UsernameHistory extends Command
{

	@Override
	public void runCommand(String s, String[] args)
	{
		final String name = args[0];
		try {
			String uuid = grabUUID(name);
			String names = readURL(new URL("https://api.mojang.com/user/profiles/" + uuid + "/names"));
			if (names.isEmpty()) {
				ChatUtils.error(name + " has had no username changes.");
			} else {
				Collection<GameProfile> profiles = new Gson().fromJson(names, new TypeToken<Collection<GameProfile>>(){}.getType());
				StringBuilder output = new StringBuilder();
				profiles.forEach(profile -> output.append("\"").append(profile.getName()).append("\", "));
				ChatUtils.warning(name  + " has had the usernames: " + output.substring(0, output.length() - 2) + ".");
			}
		}
		catch(Exception e)
		{
			ChatUtils.error("Failed to look up user.");
		}
	}
	private String grabUUID(String name) {
		try {
			String userInfo = readURL(new URL("https://api.mojang.com/users/profiles/minecraft/" + name));
			Map<String, Object> output = new Gson().fromJson(userInfo, new TypeToken<Map<String, Object>>(){}.getType());
			return output.get("id").toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String readURL(URL url) {
		String temp = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			temp = reader.lines().collect(Collectors.joining());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return temp;
	}

}