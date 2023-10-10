// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.util.Constants;
import java.util.HashMap;
import java.util.Map;
import java.nio.channels.FileLock;
import java.nio.channels.FileChannel;
import org.apache.logging.log4j.core.util.FileUtils;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.FileTime;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Date;
import org.apache.logging.log4j.core.config.Configuration;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.FileSystems;
import org.apache.logging.log4j.core.LoggerContext;
import java.nio.ByteBuffer;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class FileManager extends OutputStreamManager
{
    private static final FileManagerFactory FACTORY;
    private final boolean isAppend;
    private final boolean createOnDemand;
    private final boolean isLocking;
    private final String advertiseURI;
    private final int bufferSize;
    private final Set<PosixFilePermission> filePermissions;
    private final String fileOwner;
    private final String fileGroup;
    private final boolean attributeViewEnabled;
    
    @Deprecated
    protected FileManager(final String fileName, final OutputStream os, final boolean append, final boolean locking, final String advertiseURI, final Layout<? extends Serializable> layout, final int bufferSize, final boolean writeHeader) {
        this(fileName, os, append, locking, advertiseURI, layout, writeHeader, ByteBuffer.wrap(new byte[bufferSize]));
    }
    
    @Deprecated
    protected FileManager(final String fileName, final OutputStream os, final boolean append, final boolean locking, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super(os, fileName, layout, writeHeader, buffer);
        this.isAppend = append;
        this.createOnDemand = false;
        this.isLocking = locking;
        this.advertiseURI = advertiseURI;
        this.bufferSize = buffer.capacity();
        this.filePermissions = null;
        this.fileOwner = null;
        this.fileGroup = null;
        this.attributeViewEnabled = false;
    }
    
    @Deprecated
    protected FileManager(final LoggerContext loggerContext, final String fileName, final OutputStream os, final boolean append, final boolean locking, final boolean createOnDemand, final String advertiseURI, final Layout<? extends Serializable> layout, final boolean writeHeader, final ByteBuffer buffer) {
        super(loggerContext, os, fileName, createOnDemand, layout, writeHeader, buffer);
        this.isAppend = append;
        this.createOnDemand = createOnDemand;
        this.isLocking = locking;
        this.advertiseURI = advertiseURI;
        this.bufferSize = buffer.capacity();
        this.filePermissions = null;
        this.fileOwner = null;
        this.fileGroup = null;
        this.attributeViewEnabled = false;
    }
    
    protected FileManager(final LoggerContext loggerContext, final String fileName, final OutputStream os, final boolean append, final boolean locking, final boolean createOnDemand, final String advertiseURI, final Layout<? extends Serializable> layout, final String filePermissions, final String fileOwner, final String fileGroup, final boolean writeHeader, final ByteBuffer buffer) {
        super(loggerContext, os, fileName, createOnDemand, layout, writeHeader, buffer);
        this.isAppend = append;
        this.createOnDemand = createOnDemand;
        this.isLocking = locking;
        this.advertiseURI = advertiseURI;
        this.bufferSize = buffer.capacity();
        final Set<String> views = FileSystems.getDefault().supportedFileAttributeViews();
        if (views.contains("posix")) {
            this.filePermissions = ((filePermissions != null) ? PosixFilePermissions.fromString(filePermissions) : null);
            this.fileGroup = fileGroup;
        }
        else {
            this.filePermissions = null;
            this.fileGroup = null;
            if (filePermissions != null) {
                FileManager.LOGGER.warn("Posix file attribute permissions defined but it is not supported by this files system.");
            }
            if (fileGroup != null) {
                FileManager.LOGGER.warn("Posix file attribute group defined but it is not supported by this files system.");
            }
        }
        if (views.contains("owner")) {
            this.fileOwner = fileOwner;
        }
        else {
            this.fileOwner = null;
            if (fileOwner != null) {
                FileManager.LOGGER.warn("Owner file attribute defined but it is not supported by this files system.");
            }
        }
        this.attributeViewEnabled = (this.filePermissions != null || this.fileOwner != null || this.fileGroup != null);
    }
    
    public static FileManager getFileManager(final String fileName, final boolean append, boolean locking, final boolean bufferedIo, final boolean createOnDemand, final String advertiseUri, final Layout<? extends Serializable> layout, final int bufferSize, final String filePermissions, final String fileOwner, final String fileGroup, final Configuration configuration) {
        if (locking && bufferedIo) {
            locking = false;
        }
        return AbstractManager.narrow(FileManager.class, OutputStreamManager.getManager(fileName, new FactoryData(append, locking, bufferedIo, bufferSize, createOnDemand, advertiseUri, layout, filePermissions, fileOwner, fileGroup, configuration), FileManager.FACTORY));
    }
    
    @Override
    protected OutputStream createOutputStream() throws IOException {
        final String filename = this.getFileName();
        FileManager.LOGGER.debug("Now writing to {} at {}", filename, new Date());
        final File file = new File(filename);
        this.createParentDir(file);
        final FileOutputStream fos = new FileOutputStream(file, this.isAppend);
        if (file.exists() && file.length() == 0L) {
            try {
                final FileTime now = FileTime.fromMillis(System.currentTimeMillis());
                Files.setAttribute(file.toPath(), "creationTime", now, new LinkOption[0]);
            }
            catch (Exception ex) {
                FileManager.LOGGER.warn("Unable to set current file time for {}", filename);
            }
            this.writeHeader(fos);
        }
        this.defineAttributeView(Paths.get(filename, new String[0]));
        return fos;
    }
    
    protected void createParentDir(final File file) {
    }
    
    protected void defineAttributeView(final Path path) {
        if (this.attributeViewEnabled) {
            try {
                path.toFile().createNewFile();
                FileUtils.defineFilePosixAttributeView(path, this.filePermissions, this.fileOwner, this.fileGroup);
            }
            catch (Exception e) {
                FileManager.LOGGER.error("Could not define attribute view on path \"{}\" got {}", path, e.getMessage(), e);
            }
        }
    }
    
    @Override
    protected synchronized void write(final byte[] bytes, final int offset, final int length, final boolean immediateFlush) {
        if (this.isLocking) {
            try {
                final FileChannel channel = ((FileOutputStream)this.getOutputStream()).getChannel();
                try (final FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
                    super.write(bytes, offset, length, immediateFlush);
                }
                return;
            }
            catch (IOException ex) {
                throw new AppenderLoggingException("Unable to obtain lock on " + this.getName(), ex);
            }
        }
        super.write(bytes, offset, length, immediateFlush);
    }
    
    @Override
    protected synchronized void writeToDestination(final byte[] bytes, final int offset, final int length) {
        if (this.isLocking) {
            try {
                final FileChannel channel = ((FileOutputStream)this.getOutputStream()).getChannel();
                try (final FileLock lock = channel.lock(0L, Long.MAX_VALUE, false)) {
                    super.writeToDestination(bytes, offset, length);
                }
                return;
            }
            catch (IOException ex) {
                throw new AppenderLoggingException("Unable to obtain lock on " + this.getName(), ex);
            }
        }
        super.writeToDestination(bytes, offset, length);
    }
    
    public String getFileName() {
        return this.getName();
    }
    
    public boolean isAppend() {
        return this.isAppend;
    }
    
    public boolean isCreateOnDemand() {
        return this.createOnDemand;
    }
    
    public boolean isLocking() {
        return this.isLocking;
    }
    
    public int getBufferSize() {
        return this.bufferSize;
    }
    
    public Set<PosixFilePermission> getFilePermissions() {
        return this.filePermissions;
    }
    
    public String getFileOwner() {
        return this.fileOwner;
    }
    
    public String getFileGroup() {
        return this.fileGroup;
    }
    
    public boolean isAttributeViewEnabled() {
        return this.attributeViewEnabled;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("fileURI", this.advertiseURI);
        return result;
    }
    
    static {
        FACTORY = new FileManagerFactory();
    }
    
    private static class FactoryData extends ConfigurationFactoryData
    {
        private final boolean append;
        private final boolean locking;
        private final boolean bufferedIo;
        private final int bufferSize;
        private final boolean createOnDemand;
        private final String advertiseURI;
        private final Layout<? extends Serializable> layout;
        private final String filePermissions;
        private final String fileOwner;
        private final String fileGroup;
        
        public FactoryData(final boolean append, final boolean locking, final boolean bufferedIo, final int bufferSize, final boolean createOnDemand, final String advertiseURI, final Layout<? extends Serializable> layout, final String filePermissions, final String fileOwner, final String fileGroup, final Configuration configuration) {
            super(configuration);
            this.append = append;
            this.locking = locking;
            this.bufferedIo = bufferedIo;
            this.bufferSize = bufferSize;
            this.createOnDemand = createOnDemand;
            this.advertiseURI = advertiseURI;
            this.layout = layout;
            this.filePermissions = filePermissions;
            this.fileOwner = fileOwner;
            this.fileGroup = fileGroup;
        }
    }
    
    private static class FileManagerFactory implements ManagerFactory<FileManager, FactoryData>
    {
        @Override
        public FileManager createManager(final String name, final FactoryData data) {
            final File file = new File(name);
            try {
                FileUtils.makeParentDirs(file);
                final boolean writeHeader = !data.append || !file.exists();
                final int actualSize = data.bufferedIo ? data.bufferSize : Constants.ENCODER_BYTE_BUFFER_SIZE;
                final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[actualSize]);
                final FileOutputStream fos = data.createOnDemand ? null : new FileOutputStream(file, data.append);
                final FileManager fm = new FileManager(data.getLoggerContext(), name, fos, data.append, data.locking, data.createOnDemand, data.advertiseURI, data.layout, data.filePermissions, data.fileOwner, data.fileGroup, writeHeader, byteBuffer);
                if (fos != null && fm.attributeViewEnabled) {
                    fm.defineAttributeView(file.toPath());
                }
                return fm;
            }
            catch (IOException ex) {
                AbstractManager.LOGGER.error("FileManager (" + name + ") " + ex, ex);
                return null;
            }
        }
    }
}
