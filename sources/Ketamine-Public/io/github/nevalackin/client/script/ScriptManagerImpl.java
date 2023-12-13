package io.github.nevalackin.client.script;

import io.github.nevalackin.client.script.ScriptManager;
import io.github.nevalackin.client.core.KetamineClient;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class ScriptManagerImpl implements ScriptManager {

    private final Map<String, Script> scripts = new HashMap<>();

    public ScriptManagerImpl() {
        final File scriptsDir = KetamineClient.getInstance().getFileManager().getFile("scripts");

        final File[] files = scriptsDir.listFiles();

        if (files != null) {
            for (final File file : files) {
                if (FilenameUtils.getExtension(file.getName()).equals(EXTENSION.substring(1))) {
                    final String script = FilenameUtils.removeExtension(file.getName());

                    try (final BufferedReader reader = Files.newBufferedReader(Paths.get(script))) {
                        final StringBuilder builder = new StringBuilder();

                        String nextLine;
                        while ((nextLine = reader.readLine()) != null) {
                            builder.append(nextLine).append('\n');
                        }

                        this.scripts.put(script, new Script(builder.toString()));
                    } catch (final IOException ignored) {
                    }
                }
            }
        }
    }

    @Override
    public boolean enable(final String script, final boolean enabled) {
        final Script scriptObj = this.scripts.get(script);

        if (scriptObj != null) {
            scriptObj.setEnabled(enabled);

            return true;
        }

        return false;
    }
}
