// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import java.nio.file.attribute.PosixFilePermissions;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.Builder;
import java.io.IOException;
import java.util.Iterator;
import org.apache.logging.log4j.core.util.FileUtils;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.FileVisitor;
import java.util.List;
import java.nio.file.Path;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "PosixViewAttribute", category = "Core", printObject = true)
public class PosixViewAttributeAction extends AbstractPathAction
{
    private final Set<PosixFilePermission> filePermissions;
    private final String fileOwner;
    private final String fileGroup;
    
    private PosixViewAttributeAction(final String basePath, final boolean followSymbolicLinks, final int maxDepth, final PathCondition[] pathConditions, final StrSubstitutor subst, final Set<PosixFilePermission> filePermissions, final String fileOwner, final String fileGroup) {
        super(basePath, followSymbolicLinks, maxDepth, pathConditions, subst);
        this.filePermissions = filePermissions;
        this.fileOwner = fileOwner;
        this.fileGroup = fileGroup;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Override
    protected FileVisitor<Path> createFileVisitor(final Path basePath, final List<PathCondition> conditions) {
        return new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                for (final PathCondition pathFilter : conditions) {
                    final Path relative = basePath.relativize(file);
                    if (!pathFilter.accept(basePath, relative, attrs)) {
                        AbstractAction.LOGGER.trace("Not defining posix attribute base={}, relative={}", basePath, relative);
                        return FileVisitResult.CONTINUE;
                    }
                }
                FileUtils.defineFilePosixAttributeView(file, PosixViewAttributeAction.this.filePermissions, PosixViewAttributeAction.this.fileOwner, PosixViewAttributeAction.this.fileGroup);
                return FileVisitResult.CONTINUE;
            }
        };
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
    
    @Override
    public String toString() {
        return "PosixViewAttributeAction [filePermissions=" + this.filePermissions + ", fileOwner=" + this.fileOwner + ", fileGroup=" + this.fileGroup + ", getBasePath()=" + this.getBasePath() + ", getMaxDepth()=" + this.getMaxDepth() + ", getPathConditions()=" + this.getPathConditions() + "]";
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<PosixViewAttributeAction>
    {
        @PluginConfiguration
        private Configuration configuration;
        private StrSubstitutor subst;
        @PluginBuilderAttribute
        @Required(message = "No base path provided")
        private String basePath;
        @PluginBuilderAttribute
        private boolean followLinks;
        @PluginBuilderAttribute
        private int maxDepth;
        @PluginElement("PathConditions")
        private PathCondition[] pathConditions;
        @PluginBuilderAttribute("filePermissions")
        private String filePermissionsString;
        private Set<PosixFilePermission> filePermissions;
        @PluginBuilderAttribute
        private String fileOwner;
        @PluginBuilderAttribute
        private String fileGroup;
        
        public Builder() {
            this.followLinks = false;
            this.maxDepth = 1;
        }
        
        @Override
        public PosixViewAttributeAction build() {
            if (Strings.isEmpty(this.basePath)) {
                AbstractAction.LOGGER.error("Posix file attribute view action not valid because base path is empty.");
                return null;
            }
            if (this.filePermissions == null && Strings.isEmpty(this.filePermissionsString) && Strings.isEmpty(this.fileOwner) && Strings.isEmpty(this.fileGroup)) {
                AbstractAction.LOGGER.error("Posix file attribute view not valid because nor permissions, user or group defined.");
                return null;
            }
            if (!FileUtils.isFilePosixAttributeViewSupported()) {
                AbstractAction.LOGGER.warn("Posix file attribute view defined but it is not supported by this files system.");
                return null;
            }
            return new PosixViewAttributeAction(this.basePath, this.followLinks, this.maxDepth, this.pathConditions, (this.subst != null) ? this.subst : this.configuration.getStrSubstitutor(), (this.filePermissions != null) ? this.filePermissions : ((this.filePermissionsString != null) ? PosixFilePermissions.fromString(this.filePermissionsString) : null), this.fileOwner, this.fileGroup, null);
        }
        
        public Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }
        
        public Builder withSubst(final StrSubstitutor subst) {
            this.subst = subst;
            return this;
        }
        
        public Builder withBasePath(final String basePath) {
            this.basePath = basePath;
            return this;
        }
        
        public Builder withFollowLinks(final boolean followLinks) {
            this.followLinks = followLinks;
            return this;
        }
        
        public Builder withMaxDepth(final int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }
        
        public Builder withPathConditions(final PathCondition[] pathConditions) {
            this.pathConditions = pathConditions;
            return this;
        }
        
        public Builder withFilePermissionsString(final String filePermissionsString) {
            this.filePermissionsString = filePermissionsString;
            return this;
        }
        
        public Builder withFilePermissions(final Set<PosixFilePermission> filePermissions) {
            this.filePermissions = filePermissions;
            return this;
        }
        
        public Builder withFileOwner(final String fileOwner) {
            this.fileOwner = fileOwner;
            return this;
        }
        
        public Builder withFileGroup(final String fileGroup) {
            this.fileGroup = fileGroup;
            return this;
        }
    }
}
