package appu26j;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import appu26j.utils.FileUtil;

public class BuiltInResourcePackDownloader
{
    /**
     * Downloads the built in pack, 1.14 Textures
     */
	public static void downloadPack()
	{
		try
		{
			File packZip = new File("resourcepacks", "1.14 Textures.zip");
            File serverResourcePacks = new File("server-resource-packs");
			File pack = new File("resourcepacks", "1.14 Textures");
			
			if (!serverResourcePacks.exists())
			{
			    serverResourcePacks.mkdirs();
			}
			
			if (pack.exists())
			{
				return;
			}
			
			URL url = new URL("https://github.com/AppleClient/1.14-Textures/releases/download/1.14-Textures/1.14.Textures.zip");
			FileUtils.copyURLToFile(url, packZip);
			FileUtil.unzip(packZip, pack);
			packZip.delete();
		}
		
		catch (Exception e)
		{
			;
		}
	}
}
