package slavikcodd3r.rainbow.utils;

import java.util.Iterator;
import java.io.Writer;
import java.net.URI;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Reader;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import slavikcodd3r.rainbow.Rainbow;

import java.io.File;

public final class FileUtils
{
    public static List<String> read(final File inputFile) {
        final List<String> readContent = new ArrayList<String>();
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                readContent.add(str);
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return readContent;
    }
    
    public static void write(final File outputFile, final List<String> writeContent, final boolean overrideContent) {
        try {
            final Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            for (final String outputLine : writeContent) {
                out.write(String.valueOf(outputLine) + System.getProperty("line.separator"));
            }
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static File getConfigDir() {
        final File file = new File(ClientUtils.mc().mcDataDir, Rainbow.name + " " + Rainbow.version);
        if (!file.exists()) {
            file.mkdir();
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
    		    try {
    				Desktop.getDesktop().browse(new URI(Rainbow.vk));
    				Desktop.getDesktop().browse(new URI(Rainbow.youtube));
    		    	JOptionPane.showMessageDialog(null, "Не забудь подписаться на канал создателя и добавить его в друзья в ВК", Rainbow.name + " " + Rainbow.version, JOptionPane.WARNING_MESSAGE);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
        	}
        }
        return file;
    }
    
    public static File getConfigFile(final String name) {
        final File file = new File(getConfigDir(), String.format("%s.rainbow", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
