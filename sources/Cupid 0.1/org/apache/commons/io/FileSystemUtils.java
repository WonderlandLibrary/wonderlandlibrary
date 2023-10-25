package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class FileSystemUtils {
  private static final FileSystemUtils INSTANCE = new FileSystemUtils();
  
  private static final int INIT_PROBLEM = -1;
  
  private static final int OTHER = 0;
  
  private static final int WINDOWS = 1;
  
  private static final int UNIX = 2;
  
  private static final int POSIX_UNIX = 3;
  
  private static final int OS;
  
  private static final String DF;
  
  static {
    int os = 0;
    String dfPath = "df";
    try {
      String osName = System.getProperty("os.name");
      if (osName == null)
        throw new IOException("os.name not found"); 
      osName = osName.toLowerCase(Locale.ENGLISH);
      if (osName.indexOf("windows") != -1) {
        os = 1;
      } else if (osName.indexOf("linux") != -1 || osName.indexOf("mpe/ix") != -1 || osName.indexOf("freebsd") != -1 || osName.indexOf("irix") != -1 || osName.indexOf("digital unix") != -1 || osName.indexOf("unix") != -1 || osName.indexOf("mac os x") != -1) {
        os = 2;
      } else if (osName.indexOf("sun os") != -1 || osName.indexOf("sunos") != -1 || osName.indexOf("solaris") != -1) {
        os = 3;
        dfPath = "/usr/xpg4/bin/df";
      } else if (osName.indexOf("hp-ux") != -1 || osName.indexOf("aix") != -1) {
        os = 3;
      } else {
        os = 0;
      } 
    } catch (Exception ex) {
      os = -1;
    } 
    OS = os;
    DF = dfPath;
  }
  
  @Deprecated
  public static long freeSpace(String path) throws IOException {
    return INSTANCE.freeSpaceOS(path, OS, false, -1L);
  }
  
  public static long freeSpaceKb(String path) throws IOException {
    return freeSpaceKb(path, -1L);
  }
  
  public static long freeSpaceKb(String path, long timeout) throws IOException {
    return INSTANCE.freeSpaceOS(path, OS, true, timeout);
  }
  
  public static long freeSpaceKb() throws IOException {
    return freeSpaceKb(-1L);
  }
  
  public static long freeSpaceKb(long timeout) throws IOException {
    return freeSpaceKb((new File(".")).getAbsolutePath(), timeout);
  }
  
  long freeSpaceOS(String path, int os, boolean kb, long timeout) throws IOException {
    if (path == null)
      throw new IllegalArgumentException("Path must not be empty"); 
    switch (os) {
      case 1:
        return kb ? (freeSpaceWindows(path, timeout) / 1024L) : freeSpaceWindows(path, timeout);
      case 2:
        return freeSpaceUnix(path, kb, false, timeout);
      case 3:
        return freeSpaceUnix(path, kb, true, timeout);
      case 0:
        throw new IllegalStateException("Unsupported operating system");
    } 
    throw new IllegalStateException("Exception caught when determining operating system");
  }
  
  long freeSpaceWindows(String path, long timeout) throws IOException {
    path = FilenameUtils.normalize(path, false);
    if (path.length() > 0 && path.charAt(0) != '"')
      path = "\"" + path + "\""; 
    String[] cmdAttribs = { "cmd.exe", "/C", "dir /a /-c " + path };
    List<String> lines = performCommand(cmdAttribs, 2147483647, timeout);
    for (int i = lines.size() - 1; i >= 0; i--) {
      String line = lines.get(i);
      if (line.length() > 0)
        return parseDir(line, path); 
    } 
    throw new IOException("Command line 'dir /-c' did not return any info for path '" + path + "'");
  }
  
  long parseDir(String line, String path) throws IOException {
    int bytesStart = 0;
    int bytesEnd = 0;
    int j = line.length() - 1;
    while (j >= 0) {
      char c = line.charAt(j);
      if (Character.isDigit(c)) {
        bytesEnd = j + 1;
        break;
      } 
      j--;
    } 
    while (j >= 0) {
      char c = line.charAt(j);
      if (!Character.isDigit(c) && c != ',' && c != '.') {
        bytesStart = j + 1;
        break;
      } 
      j--;
    } 
    if (j < 0)
      throw new IOException("Command line 'dir /-c' did not return valid info for path '" + path + "'"); 
    StringBuilder buf = new StringBuilder(line.substring(bytesStart, bytesEnd));
    for (int k = 0; k < buf.length(); k++) {
      if (buf.charAt(k) == ',' || buf.charAt(k) == '.')
        buf.deleteCharAt(k--); 
    } 
    return parseBytes(buf.toString(), path);
  }
  
  long freeSpaceUnix(String path, boolean kb, boolean posix, long timeout) throws IOException {
    if (path.length() == 0)
      throw new IllegalArgumentException("Path must not be empty"); 
    String flags = "-";
    if (kb)
      flags = flags + "k"; 
    if (posix)
      flags = flags + "P"; 
    (new String[3])[0] = DF;
    (new String[3])[1] = flags;
    (new String[3])[2] = path;
    (new String[2])[0] = DF;
    (new String[2])[1] = path;
    String[] cmdAttribs = (flags.length() > 1) ? new String[3] : new String[2];
    List<String> lines = performCommand(cmdAttribs, 3, timeout);
    if (lines.size() < 2)
      throw new IOException("Command line '" + DF + "' did not return info as expected " + "for path '" + path + "'- response was " + lines); 
    String line2 = lines.get(1);
    StringTokenizer tok = new StringTokenizer(line2, " ");
    if (tok.countTokens() < 4) {
      if (tok.countTokens() == 1 && lines.size() >= 3) {
        String line3 = lines.get(2);
        tok = new StringTokenizer(line3, " ");
      } else {
        throw new IOException("Command line '" + DF + "' did not return data as expected " + "for path '" + path + "'- check path is valid");
      } 
    } else {
      tok.nextToken();
    } 
    tok.nextToken();
    tok.nextToken();
    String freeSpace = tok.nextToken();
    return parseBytes(freeSpace, path);
  }
  
  long parseBytes(String freeSpace, String path) throws IOException {
    try {
      long bytes = Long.parseLong(freeSpace);
      if (bytes < 0L)
        throw new IOException("Command line '" + DF + "' did not find free space in response " + "for path '" + path + "'- check path is valid"); 
      return bytes;
    } catch (NumberFormatException ex) {
      throw new IOExceptionWithCause("Command line '" + DF + "' did not return numeric data as expected " + "for path '" + path + "'- check path is valid", ex);
    } 
  }
  
  List<String> performCommand(String[] cmdAttribs, int max, long timeout) throws IOException {
    List<String> lines = new ArrayList<String>(20);
    Process proc = null;
    InputStream in = null;
    OutputStream out = null;
    InputStream err = null;
    BufferedReader inr = null;
    try {
      Thread monitor = ThreadMonitor.start(timeout);
      proc = openProcess(cmdAttribs);
      in = proc.getInputStream();
      out = proc.getOutputStream();
      err = proc.getErrorStream();
      inr = new BufferedReader(new InputStreamReader(in));
      String line = inr.readLine();
      while (line != null && lines.size() < max) {
        line = line.toLowerCase(Locale.ENGLISH).trim();
        lines.add(line);
        line = inr.readLine();
      } 
      proc.waitFor();
      ThreadMonitor.stop(monitor);
      if (proc.exitValue() != 0)
        throw new IOException("Command line returned OS error code '" + proc.exitValue() + "' for command " + Arrays.asList(cmdAttribs)); 
      if (lines.isEmpty())
        throw new IOException("Command line did not return any info for command " + Arrays.asList(cmdAttribs)); 
      return lines;
    } catch (InterruptedException ex) {
      throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(cmdAttribs) + " timeout=" + timeout, ex);
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
      IOUtils.closeQuietly(err);
      IOUtils.closeQuietly(inr);
      if (proc != null)
        proc.destroy(); 
    } 
  }
  
  Process openProcess(String[] cmdAttribs) throws IOException {
    return Runtime.getRuntime().exec(cmdAttribs);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\FileSystemUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */