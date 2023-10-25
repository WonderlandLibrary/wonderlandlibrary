package org.apache.logging.log4j.core.script;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.ExtensionLanguageMapping;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.IOUtils;
import org.apache.logging.log4j.core.util.NetUtils;

@Plugin(name = "ScriptFile", category = "Core", printObject = true)
public class ScriptFile extends AbstractScript {
  private final Path filePath;
  
  private final boolean isWatched;
  
  public ScriptFile(String name, Path filePath, String language, boolean isWatched, String scriptText) {
    super(name, language, scriptText);
    this.filePath = filePath;
    this.isWatched = isWatched;
  }
  
  public Path getPath() {
    return this.filePath;
  }
  
  public boolean isWatched() {
    return this.isWatched;
  }
  
  @PluginFactory
  public static ScriptFile createScript(@PluginAttribute("name") String name, @PluginAttribute("language") String language, @PluginAttribute("path") String filePathOrUri, @PluginAttribute("isWatched") Boolean isWatched, @PluginAttribute("charset") Charset charset) {
    String scriptText;
    if (filePathOrUri == null) {
      LOGGER.error("No script path provided for ScriptFile");
      return null;
    } 
    if (name == null)
      name = filePathOrUri; 
    URI uri = NetUtils.toURI(filePathOrUri);
    File file = FileUtils.fileFromUri(uri);
    if (language == null && file != null) {
      String fileExtension = FileUtils.getFileExtension(file);
      if (fileExtension != null) {
        ExtensionLanguageMapping mapping = ExtensionLanguageMapping.getByExtension(fileExtension);
        if (mapping != null)
          language = mapping.getLanguage(); 
      } 
    } 
    if (language == null) {
      LOGGER.info("No script language supplied, defaulting to {}", "JavaScript");
      language = "JavaScript";
    } 
    Charset actualCharset = (charset == null) ? Charset.defaultCharset() : charset;
    try (Reader reader = new InputStreamReader((file != null) ? new FileInputStream(file) : uri
          .toURL().openStream(), actualCharset)) {
      scriptText = IOUtils.toString(reader);
    } catch (IOException e) {
      LOGGER.error("{}: language={}, path={}, actualCharset={}", e.getClass().getSimpleName(), language, filePathOrUri, actualCharset);
      return null;
    } 
    Path path = (file != null) ? Paths.get(file.toURI()) : Paths.get(uri);
    if (path == null) {
      LOGGER.error("Unable to convert {} to a Path", uri.toString());
      return null;
    } 
    return new ScriptFile(name, path, language, ((isWatched == null) ? Boolean.FALSE : isWatched).booleanValue(), scriptText);
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (!getName().equals(this.filePath.toString()))
      sb.append("name=").append(getName()).append(", "); 
    sb.append("path=").append(this.filePath);
    if (getLanguage() != null)
      sb.append(", language=").append(getLanguage()); 
    sb.append(", isWatched=").append(this.isWatched);
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\script\ScriptFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */