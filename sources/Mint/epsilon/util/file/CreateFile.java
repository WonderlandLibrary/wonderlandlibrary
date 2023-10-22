package epsilon.util.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;

import net.minecraft.client.Minecraft;

public class CreateFile { //Probly just delete all of this since as im pushing you have added config manager so
	
    private static final Minecraft mc = Minecraft.getMinecraft();
	
	private static final String mintpath = mc.mcDataDir.getAbsolutePath() + File.separator + "Mint" + File.separator;
	private static final String config = mintpath + File.separator + "Config" + File.separator;
	
	public static void initiateFolder() {
		
		if(getFileOrPath(mintpath).exists() && getFileOrPath(config).exists()) return;
		
	
	
		final File d = new File(mintpath);
		final File c = new File(config);

		if(!getFileOrPath(mintpath).exists()) {
			
		    d.mkdirs();
		    
		    System.out.println("Created " + mintpath);
		    
		    c.mkdirs();
		    
		    System.out.println("Created " + config);
	    
		}else if (!getFileOrPath(config).exists()) {
		    c.mkdirs();
		    
		    System.out.println("Created " + config);
		    
		}
	    
		
		
	}
	
	public static void createConfigFile(final String name) {

		final File c = new File(config + File.separator + name + File.separator);
		
		c.mkdirs();
		
        writeConfigData();
        
	}
	
	private static void writeConfigData() {
		
	}
	

    public static File getFileOrPath(final String fileName) {
        return new File(mintpath + fileName.replace("\\", File.separator));
    }

}
