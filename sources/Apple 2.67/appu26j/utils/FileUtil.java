package appu26j.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil
{
	public static void unzip(File input, File output)
	{
		try
		{
	        if (!output.exists())
	        {
	        	output.mkdir();
	        }
	        
	        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(input));
	        ZipEntry zipEntry = zipInputStream.getNextEntry();
	        
	        while (zipEntry != null)
	        {
	            File file = new File(output, zipEntry.getName());
	            
	            if (!zipEntry.isDirectory())
	            {
	                extractFile(zipInputStream, file);
	            }
	            
	            else
	            {
	                file.mkdirs();
	            }
	            
	            zipInputStream.closeEntry();
	            zipEntry = zipInputStream.getNextEntry();
	        }
	        
	        zipInputStream.close();
		}
		
		catch (Exception e)
		{
			;
		}
    }
	
    private static void extractFile(ZipInputStream zipInputStream, File file)
    {
    	try
    	{
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[4096];
            int read = 0;
            
            while ((read = zipInputStream.read(bytes)) != -1)
            {
            	bufferedOutputStream.write(bytes, 0, read);
            }
            
            bufferedOutputStream.close();
    	}
    	
    	catch (Exception e)
    	{
    		;
    	}
    }
}
