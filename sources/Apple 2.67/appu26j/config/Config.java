package appu26j.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import appu26j.Apple;
import appu26j.mods.Mod;
import appu26j.settings.Setting;

public class Config
{
	private Gson gson = new Gson(), prettyGson = new GsonBuilder().setPrettyPrinting().create();
	private JsonParser jsonParser = new JsonParser();
	public File currentConfig = Apple.CONFIG;
	
	public Config()
	{
		this.loadMods();
	}
	
	/**
	 * Saves mods into the config.json file
	 */
	public void saveMods()
	{
		new Thread(() ->
		{
		    JsonObject jsonObject = new JsonObject();
	        PrintWriter printWriter = null;
            jsonObject.addProperty("Apple Client Version", Apple.VERSION);
	        
	        for (Mod mod : Apple.CLIENT.getModsManager().getMods())
	        {
	            JsonObject jsonMod = new JsonObject();
	            jsonMod.addProperty("Enabled", mod.isEnabled());
	            jsonObject.add(mod.getName(), jsonMod);
	            
	            for (Setting setting : mod.getSettings())
	            {
	                if (setting.getTypeOfSetting().equals("Check Box"))
	                {
	                    jsonMod.addProperty(setting.getName(), setting.getCheckBoxValue());
	                }
	                
	                else if (setting.getTypeOfSetting().equals("Mode"))
	                {
	                    jsonMod.addProperty(setting.getName(), setting.getModeValue());
	                }
	                
	                else if (setting.getTypeOfSetting().equals("Slider"))
	                {
	                    jsonMod.addProperty(setting.getName(), setting.getSliderValue());
	                }
	                
	                else if (setting.getTypeOfSetting().equals("Text Box"))
	                {
	                    jsonMod.addProperty(setting.getName(), setting.getTextBoxValue());
	                }
	                
	                else
	                {
	                    jsonMod.addProperty(setting.getName(), setting.getColors()[0] + ", " + setting.getColors()[1] + ", " + setting.getColors()[2]);
	                }
	            }
	            
	            if (mod.hasGUI())
	            {
	                jsonMod.addProperty("Position X", mod.getX());
	                jsonMod.addProperty("Position Y", mod.getY());
	            }
	        }
	        
	        try
	        {
	            printWriter = new PrintWriter(new FileWriter(this.currentConfig));
	            printWriter.println(this.prettyGson.toJson(jsonObject));
	        }
	        
	        catch (Exception e)
	        {
	            ;
	        }
	        
	        finally
	        {
	            if (printWriter != null)
	            {
	                printWriter.close();
	            }
	        }
		}).start();
	}
	
	/**
     * Loads mods from the config.json file
     */
	public void loadMods()
	{
		BufferedReader bufferedReader = null;
		
		try
		{
			bufferedReader = new BufferedReader(new FileReader(this.currentConfig));
			JsonObject jsonObject = (JsonObject) this.jsonParser.parse(bufferedReader);
			bufferedReader.close();
			Iterator<Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
			
			while (iterator.hasNext())
			{
				Entry<String, JsonElement> entry = iterator.next();
				String modName = entry.getKey();
				
				/**
				 * The below is for legacy Apple Client configs.
				 * In the v2.24 update, I have changed a lot of mods'
				 * names, which breaks configs. To prevent that, I
				 * change the mod's name here.
				 */
				if (modName.equals("Key Strokes"))
				{
				    modName = "Keystrokes";
				}
				
				else if (modName.equals("Boss Bar"))
                {
                    modName = "Bossbar";
                }
                
				else if (modName.equals("MC Chat"))
                {
                    modName = "Chat";
                }
                
				else if (modName.equals("Cross Hair"))
                {
                    modName = "Crosshair";
                }
                
				else if (modName.equals("Name Tags"))
                {
                    modName = "Nametags";
                }
                
				else if (modName.equals("Score Board"))
				{
				    modName = "Scoreboard";
				}
				
				else if (modName.equals("Time Clock"))
                {
                    modName = "Clock";
                }
				
				else if (modName.equals("Hurt Camera"))
                {
                    modName = "No Hurt Cam";
                }
				
				else if (modName.equals("Anti Snipe"))
                {
                    modName = "Name Hider";
                }
				
				else if (modName.equals("Clock Timer"))
				{
				    modName = "Timer Countdown";
				}
				
				else if (modName.equals("Damage Tint"))
                {
                    modName = "Hit Color";
                }
				
				Mod mod = Apple.CLIENT.getModsManager().getMod(modName);
				
				if (mod != null)
				{
					JsonObject jsonMod = (JsonObject) entry.getValue();
					
					if (jsonMod.get("Enabled").getAsBoolean())
					{
						mod.setEnabled(true);
					}
					
					else
					{
					    if (mod.isEnabled())
					    {
					        mod.setEnabled(false);
					    }
					}
					
					for (Setting setting : mod.getSettings())
					{
						try
						{
							if (setting.getTypeOfSetting().equals("Check Box"))
							{
							    setting.setIndex(jsonMod.get(setting.getName()).getAsBoolean() ? 1 : 0);
								setting.setCheckBoxValue(jsonMod.get(setting.getName()).getAsBoolean());
							}
							
							else if (setting.getTypeOfSetting().equals("Mode"))
							{
								setting.setModeValue(jsonMod.get(setting.getName()).getAsString());
							}
							
							else if (setting.getTypeOfSetting().equals("Slider"))
							{
								setting.setSliderValue(jsonMod.get(setting.getName()).getAsFloat());
							}
							
							else if (setting.getTypeOfSetting().equals("Text Box"))
							{
								setting.setTextBoxValue(jsonMod.get(setting.getName()).getAsString());
							}
							
							else
							{
								String[] colors = jsonMod.get(setting.getName()).getAsString().split(", ");
								int[] intColors = new int[] {Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2])};
								setting.setColors(intColors);
							}
						}
						
						catch (Exception e)
						{
							;
						}
					}
					
					if (mod.hasGUI())
					{
						mod.setPosition(jsonMod.get("Position X").getAsFloat(), jsonMod.get("Position Y").getAsFloat());
					}
				}
			}
		}
		
		catch (Exception e)
		{
			;
		}
		
		finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				}
				
				catch (Exception e)
				{
					;
				}
			}
		}
	}
}
