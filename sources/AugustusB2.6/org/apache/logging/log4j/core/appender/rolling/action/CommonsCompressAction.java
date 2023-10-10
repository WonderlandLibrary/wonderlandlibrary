// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.Files;
import org.apache.commons.compress.compressors.CompressorException;
import java.io.InputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.io.File;

public final class CommonsCompressAction extends AbstractAction
{
    private static final int BUF_SIZE = 8192;
    private final String name;
    private final File source;
    private final File destination;
    private final boolean deleteSource;
    
    public CommonsCompressAction(final String name, final File source, final File destination, final boolean deleteSource) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
    }
    
    @Override
    public boolean execute() throws IOException {
        return execute(this.name, this.source, this.destination, this.deleteSource);
    }
    
    public static boolean execute(final String name, final File source, final File destination, final boolean deleteSource) throws IOException {
        if (!source.exists()) {
            return false;
        }
        CommonsCompressAction.LOGGER.debug("Starting {} compression of {}", name, source.getPath());
        try (final FileInputStream input = new FileInputStream(source);
             final BufferedOutputStream output = new BufferedOutputStream(new CompressorStreamFactory().createCompressorOutputStream(name, new FileOutputStream(destination)))) {
            IOUtils.copy(input, output, 8192);
            CommonsCompressAction.LOGGER.debug("Finished {} compression of {}", name, source.getPath());
        }
        catch (CompressorException e) {
            throw new IOException(e);
        }
        if (deleteSource) {
            try {
                if (Files.deleteIfExists(source.toPath())) {
                    CommonsCompressAction.LOGGER.debug("Deleted {}", source.toString());
                }
                else {
                    CommonsCompressAction.LOGGER.warn("Unable to delete {} after {} compression. File did not exist", source.toString(), name);
                }
            }
            catch (Exception ex) {
                CommonsCompressAction.LOGGER.warn("Unable to delete {} after {} compression, {}", source.toString(), name, ex.getMessage());
            }
        }
        return true;
    }
    
    @Override
    protected void reportException(final Exception ex) {
        CommonsCompressAction.LOGGER.warn("Exception during " + this.name + " compression of '" + this.source.toString() + "'.", ex);
    }
    
    @Override
    public String toString() {
        return CommonsCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
    }
    
    public String getName() {
        return this.name;
    }
    
    public File getSource() {
        return this.source;
    }
    
    public File getDestination() {
        return this.destination;
    }
    
    public boolean isDeleteSource() {
        return this.deleteSource;
    }
}
