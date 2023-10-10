// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.io.Reader;
import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.io.IOException;
import org.apache.logging.log4j.core.util.IOUtils;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import org.apache.logging.log4j.core.util.ExtensionLanguageMapping;
import org.apache.logging.log4j.core.util.FileUtils;
import org.apache.logging.log4j.core.util.NetUtils;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.nio.file.Path;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ScriptFile", category = "Core", printObject = true)
public class ScriptFile extends AbstractScript
{
    private final Path filePath;
    private final boolean isWatched;
    
    public ScriptFile(final String name, final Path filePath, final String language, final boolean isWatched, final String scriptText) {
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
    public static ScriptFile createScript(@PluginAttribute("name") String name, @PluginAttribute("language") String language, @PluginAttribute("path") final String filePathOrUri, @PluginAttribute("isWatched") final Boolean isWatched, @PluginAttribute("charset") final Charset charset) {
        if (filePathOrUri == null) {
            ScriptFile.LOGGER.error("No script path provided for ScriptFile");
            return null;
        }
        if (name == null) {
            name = filePathOrUri;
        }
        final URI uri = NetUtils.toURI(filePathOrUri);
        final File file = FileUtils.fileFromUri(uri);
        if (language == null && file != null) {
            final String fileExtension = FileUtils.getFileExtension(file);
            if (fileExtension != null) {
                final ExtensionLanguageMapping mapping = ExtensionLanguageMapping.getByExtension(fileExtension);
                if (mapping != null) {
                    language = mapping.getLanguage();
                }
            }
        }
        if (language == null) {
            ScriptFile.LOGGER.info("No script language supplied, defaulting to {}", "JavaScript");
            language = "JavaScript";
        }
        final Charset actualCharset = (charset == null) ? Charset.defaultCharset() : charset;
        String scriptText;
        try (final Reader reader = new InputStreamReader((file != null) ? new FileInputStream(file) : uri.toURL().openStream(), actualCharset)) {
            scriptText = IOUtils.toString(reader);
        }
        catch (IOException e) {
            ScriptFile.LOGGER.error("{}: language={}, path={}, actualCharset={}", e.getClass().getSimpleName(), language, filePathOrUri, actualCharset);
            return null;
        }
        final Path path = (file != null) ? Paths.get(file.toURI()) : Paths.get(uri);
        if (path == null) {
            ScriptFile.LOGGER.error("Unable to convert {} to a Path", uri.toString());
            return null;
        }
        return new ScriptFile(name, path, language, (isWatched == null) ? Boolean.FALSE : isWatched, scriptText);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (!this.getName().equals(this.filePath.toString())) {
            sb.append("name=").append(this.getName()).append(", ");
        }
        sb.append("path=").append(this.filePath);
        if (this.getLanguage() != null) {
            sb.append(", language=").append(this.getLanguage());
        }
        sb.append(", isWatched=").append(this.isWatched);
        return sb.toString();
    }
}
