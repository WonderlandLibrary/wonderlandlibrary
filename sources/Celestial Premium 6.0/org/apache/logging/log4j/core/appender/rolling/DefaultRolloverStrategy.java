/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescription;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescriptionImpl;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;
import org.apache.logging.log4j.core.appender.rolling.helper.FileRenameAction;
import org.apache.logging.log4j.core.appender.rolling.helper.GZCompressAction;
import org.apache.logging.log4j.core.appender.rolling.helper.ZipCompressAction;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="DefaultRolloverStrategy", category="Core", printObject=true)
public class DefaultRolloverStrategy
implements RolloverStrategy {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final int MIN_WINDOW_SIZE = 1;
    private static final int DEFAULT_WINDOW_SIZE = 7;
    private final int maxIndex;
    private final int minIndex;
    private final boolean useMax;
    private final StrSubstitutor subst;
    private final int compressionLevel;

    protected DefaultRolloverStrategy(int minIndex, int maxIndex, boolean useMax, int compressionLevel, StrSubstitutor subst) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.useMax = useMax;
        this.compressionLevel = compressionLevel;
        this.subst = subst;
    }

    @Override
    public RolloverDescription rollover(RollingFileManager manager) throws SecurityException {
        if (this.maxIndex >= 0) {
            String renameTo;
            int fileIndex = this.purge(this.minIndex, this.maxIndex, manager);
            if (fileIndex < 0) {
                return null;
            }
            StringBuilder buf = new StringBuilder();
            manager.getPatternProcessor().formatFileName(this.subst, buf, (Object)fileIndex);
            String currentFileName = manager.getFileName();
            String compressedName = renameTo = buf.toString();
            AbstractAction compressAction = null;
            if (renameTo.endsWith(".gz")) {
                renameTo = renameTo.substring(0, renameTo.length() - 3);
                compressAction = new GZCompressAction(new File(renameTo), new File(compressedName), true);
            } else if (renameTo.endsWith(".zip")) {
                renameTo = renameTo.substring(0, renameTo.length() - 4);
                compressAction = new ZipCompressAction(new File(renameTo), new File(compressedName), true, this.compressionLevel);
            }
            FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), false);
            return new RolloverDescriptionImpl(currentFileName, false, renameAction, compressAction);
        }
        return null;
    }

    private int purge(int lowIndex, int highIndex, RollingFileManager manager) {
        return this.useMax ? this.purgeAscending(lowIndex, highIndex, manager) : this.purgeDescending(lowIndex, highIndex, manager);
    }

    private int purgeDescending(int lowIndex, int highIndex, RollingFileManager manager) {
        int i;
        int suffixLength = 0;
        ArrayList<FileRenameAction> renames = new ArrayList<FileRenameAction>();
        StringBuilder buf = new StringBuilder();
        manager.getPatternProcessor().formatFileName(buf, (Object)lowIndex);
        String lowFilename = this.subst.replace(buf);
        if (lowFilename.endsWith(".gz")) {
            suffixLength = 3;
        } else if (lowFilename.endsWith(".zip")) {
            suffixLength = 4;
        }
        for (i = lowIndex; i <= highIndex; ++i) {
            String highFilename;
            File toRename = new File(lowFilename);
            boolean isBase = false;
            if (suffixLength > 0) {
                File toRenameBase = new File(lowFilename.substring(0, lowFilename.length() - suffixLength));
                if (toRename.exists()) {
                    if (toRenameBase.exists()) {
                        toRenameBase.delete();
                    }
                } else {
                    toRename = toRenameBase;
                    isBase = true;
                }
            }
            if (!toRename.exists()) break;
            if (i == highIndex) {
                if (toRename.delete()) break;
                return -1;
            }
            buf.setLength(0);
            manager.getPatternProcessor().formatFileName(buf, (Object)(i + 1));
            String renameTo = highFilename = this.subst.replace(buf);
            if (isBase) {
                renameTo = highFilename.substring(0, highFilename.length() - suffixLength);
            }
            renames.add(new FileRenameAction(toRename, new File(renameTo), true));
            lowFilename = highFilename;
        }
        for (i = renames.size() - 1; i >= 0; --i) {
            Action action = (Action)renames.get(i);
            try {
                if (action.execute()) continue;
                return -1;
            }
            catch (Exception ex) {
                LOGGER.warn("Exception during purge in RollingFileAppender", (Throwable)ex);
                return -1;
            }
        }
        return lowIndex;
    }

    private int purgeAscending(int lowIndex, int highIndex, RollingFileManager manager) {
        int i;
        int suffixLength = 0;
        ArrayList<FileRenameAction> renames = new ArrayList<FileRenameAction>();
        StringBuilder buf = new StringBuilder();
        manager.getPatternProcessor().formatFileName(buf, (Object)highIndex);
        String highFilename = this.subst.replace(buf);
        if (highFilename.endsWith(".gz")) {
            suffixLength = 3;
        } else if (highFilename.endsWith(".zip")) {
            suffixLength = 4;
        }
        int maxIndex = 0;
        for (i = highIndex; i >= lowIndex; --i) {
            File toRename = new File(highFilename);
            if (i == highIndex && toRename.exists()) {
                maxIndex = highIndex;
            } else if (maxIndex == 0 && toRename.exists()) {
                maxIndex = i + 1;
                break;
            }
            boolean isBase = false;
            if (suffixLength > 0) {
                File toRenameBase = new File(highFilename.substring(0, highFilename.length() - suffixLength));
                if (toRename.exists()) {
                    if (toRenameBase.exists()) {
                        toRenameBase.delete();
                    }
                } else {
                    toRename = toRenameBase;
                    isBase = true;
                }
            }
            if (toRename.exists()) {
                String lowFilename;
                if (i == lowIndex) {
                    if (toRename.delete()) break;
                    return -1;
                }
                buf.setLength(0);
                manager.getPatternProcessor().formatFileName(buf, (Object)(i - 1));
                String renameTo = lowFilename = this.subst.replace(buf);
                if (isBase) {
                    renameTo = lowFilename.substring(0, lowFilename.length() - suffixLength);
                }
                renames.add(new FileRenameAction(toRename, new File(renameTo), true));
                highFilename = lowFilename;
                continue;
            }
            buf.setLength(0);
            manager.getPatternProcessor().formatFileName(buf, (Object)(i - 1));
            highFilename = this.subst.replace(buf);
        }
        if (maxIndex == 0) {
            maxIndex = lowIndex;
        }
        for (i = renames.size() - 1; i >= 0; --i) {
            Action action = (Action)renames.get(i);
            try {
                if (action.execute()) continue;
                return -1;
            }
            catch (Exception ex) {
                LOGGER.warn("Exception during purge in RollingFileAppender", (Throwable)ex);
                return -1;
            }
        }
        return maxIndex;
    }

    public String toString() {
        return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ")";
    }

    @PluginFactory
    public static DefaultRolloverStrategy createStrategy(@PluginAttribute(value="max") String max, @PluginAttribute(value="min") String min, @PluginAttribute(value="fileIndex") String fileIndex, @PluginAttribute(value="compressionLevel") String compressionLevelStr, @PluginConfiguration Configuration config) {
        int maxIndex;
        int minIndex;
        boolean useMax;
        boolean bl = useMax = fileIndex == null ? true : fileIndex.equalsIgnoreCase("max");
        if (min != null) {
            minIndex = Integer.parseInt(min);
            if (minIndex < 1) {
                LOGGER.error("Minimum window size too small. Limited to 1");
                minIndex = 1;
            }
        } else {
            minIndex = 1;
        }
        if (max != null) {
            maxIndex = Integer.parseInt(max);
            if (maxIndex < minIndex) {
                maxIndex = minIndex < 7 ? 7 : minIndex;
                LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
            }
        } else {
            maxIndex = 7;
        }
        int compressionLevel = Integers.parseInt(compressionLevelStr, -1);
        return new DefaultRolloverStrategy(minIndex, maxIndex, useMax, compressionLevel, config.getStrSubstitutor());
    }
}

