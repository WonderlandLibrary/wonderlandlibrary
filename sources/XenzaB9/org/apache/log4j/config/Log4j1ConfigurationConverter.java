// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

import org.apache.logging.log4j.core.tools.BasicCommandLineArguments;
import java.nio.file.FileVisitor;
import javax.xml.transform.TransformerException;
import org.apache.logging.log4j.core.config.ConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.SimpleFileVisitor;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.io.IOException;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import java.io.OutputStream;
import java.io.InputStream;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;

public final class Log4j1ConfigurationConverter
{
    private static final String FILE_EXT_XML = ".xml";
    private final CommandLineArguments cla;
    
    public static void main(final String[] args) {
        CommandLine.run(new CommandLineArguments(), System.err, args);
    }
    
    public static Log4j1ConfigurationConverter run(final CommandLineArguments cla) {
        final Log4j1ConfigurationConverter log4j1ConfigurationConverter = new Log4j1ConfigurationConverter(cla);
        log4j1ConfigurationConverter.run();
        return log4j1ConfigurationConverter;
    }
    
    private Log4j1ConfigurationConverter(final CommandLineArguments cla) {
        this.cla = cla;
    }
    
    protected void convert(final InputStream input, final OutputStream output) throws IOException {
        final ConfigurationBuilder<BuiltConfiguration> builder = new Log4j1ConfigurationParser().buildConfigurationBuilder(input);
        builder.writeXmlConfiguration(output);
    }
    
    InputStream getInputStream() throws IOException {
        final Path pathIn = this.cla.getPathIn();
        return (pathIn == null) ? System.in : new InputStreamWrapper(Files.newInputStream(pathIn, new OpenOption[0]), pathIn.toString());
    }
    
    OutputStream getOutputStream() throws IOException {
        final Path pathOut = this.cla.getPathOut();
        return (pathOut == null) ? System.out : Files.newOutputStream(pathOut, new OpenOption[0]);
    }
    
    private void run() {
        if (this.cla.getRecurseIntoPath() != null) {
            final AtomicInteger countOKs = new AtomicInteger();
            final AtomicInteger countFails = new AtomicInteger();
            try {
                Files.walkFileTree(this.cla.getRecurseIntoPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                        if (Log4j1ConfigurationConverter.this.cla.getPathIn() == null || file.getFileName().equals(Log4j1ConfigurationConverter.this.cla.getPathIn())) {
                            Log4j1ConfigurationConverter.this.verbose("Reading %s", file);
                            String newFile = file.getFileName().toString();
                            final int lastIndex = newFile.lastIndexOf(".");
                            newFile = ((lastIndex < 0) ? (newFile + ".xml") : (newFile.substring(0, lastIndex) + ".xml"));
                            final Path resolvedPath = file.resolveSibling(newFile);
                            try (final InputStream input = new InputStreamWrapper(Files.newInputStream(file, new OpenOption[0]), file.toString());
                                 final OutputStream output = Files.newOutputStream(resolvedPath, new OpenOption[0])) {
                                try {
                                    final ByteArrayOutputStream tmpOutput = new ByteArrayOutputStream();
                                    Log4j1ConfigurationConverter.this.convert(input, tmpOutput);
                                    tmpOutput.close();
                                    DefaultConfigurationBuilder.formatXml(new StreamSource(new ByteArrayInputStream(tmpOutput.toByteArray())), new StreamResult(output));
                                    countOKs.incrementAndGet();
                                }
                                catch (final ConfigurationException | IOException e) {
                                    countFails.incrementAndGet();
                                    if (Log4j1ConfigurationConverter.this.cla.isFailFast()) {
                                        throw e;
                                    }
                                    e.printStackTrace();
                                }
                                catch (final TransformerException e2) {
                                    countFails.incrementAndGet();
                                    if (Log4j1ConfigurationConverter.this.cla.isFailFast()) {
                                        throw new IOException(e2);
                                    }
                                    e2.printStackTrace();
                                }
                                Log4j1ConfigurationConverter.this.verbose("Wrote %s", resolvedPath);
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            catch (final IOException e) {
                throw new ConfigurationException(e);
            }
            finally {
                this.verbose("OK = %,d, Failures = %,d, Total = %,d", countOKs.get(), countFails.get(), countOKs.get() + countFails.get());
            }
        }
        else {
            this.verbose("Reading %s", this.cla.getPathIn());
            try (final InputStream input = this.getInputStream();
                 final OutputStream output = this.getOutputStream()) {
                this.convert(input, output);
            }
            catch (final IOException e2) {
                throw new ConfigurationException(e2);
            }
            this.verbose("Wrote %s", this.cla.getPathOut());
        }
    }
    
    private void verbose(final String template, final Object... args) {
        if (this.cla.isVerbose()) {
            System.err.println(String.format(template, args));
        }
    }
    
    @CommandLine.Command(name = "Log4j1ConfigurationConverter")
    public static class CommandLineArguments extends BasicCommandLineArguments implements Runnable
    {
        @CommandLine.Option(names = { "--failfast", "-f" }, description = { "Fails on the first failure in recurse mode." })
        private boolean failFast;
        @CommandLine.Option(names = { "--in", "-i" }, description = { "Specifies the input file." })
        private Path pathIn;
        @CommandLine.Option(names = { "--out", "-o" }, description = { "Specifies the output file." })
        private Path pathOut;
        @CommandLine.Option(names = { "--recurse", "-r" }, description = { "Recurses into this folder looking for the input file" })
        private Path recurseIntoPath;
        @CommandLine.Option(names = { "--verbose", "-v" }, description = { "Be verbose." })
        private boolean verbose;
        
        public Path getPathIn() {
            return this.pathIn;
        }
        
        public Path getPathOut() {
            return this.pathOut;
        }
        
        public Path getRecurseIntoPath() {
            return this.recurseIntoPath;
        }
        
        public boolean isFailFast() {
            return this.failFast;
        }
        
        public boolean isVerbose() {
            return this.verbose;
        }
        
        public void setFailFast(final boolean failFast) {
            this.failFast = failFast;
        }
        
        public void setPathIn(final Path pathIn) {
            this.pathIn = pathIn;
        }
        
        public void setPathOut(final Path pathOut) {
            this.pathOut = pathOut;
        }
        
        public void setRecurseIntoPath(final Path recurseIntoPath) {
            this.recurseIntoPath = recurseIntoPath;
        }
        
        public void setVerbose(final boolean verbose) {
            this.verbose = verbose;
        }
        
        @Override
        public void run() {
            if (this.isHelp()) {
                CommandLine.usage(this, System.err);
                return;
            }
            new Log4j1ConfigurationConverter(this, null).run();
        }
        
        @Override
        public String toString() {
            return "CommandLineArguments [recurseIntoPath=" + this.recurseIntoPath + ", verbose=" + this.verbose + ", pathIn=" + this.pathIn + ", pathOut=" + this.pathOut + "]";
        }
    }
}
