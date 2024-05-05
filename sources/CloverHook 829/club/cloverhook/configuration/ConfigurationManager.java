package club.cloverhook.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import club.cloverhook.Cloverhook;

/**
 * @author antja03S
 */
public class ConfigurationManager {

    public static final File CONFIGURATION_DIR = new File(Cloverhook.clientDir + File.separator + "configurations");

    private ArrayList<Configuration> configurationList;

    public ConfigurationManager() {
        configurationList = new ArrayList<>();
    }

    public void add(Configuration configuration) {
        Configuration possibleConfiguration = searchByIdentifier(configuration.getIdentifier());

        if (possibleConfiguration == null)
            configurationList.add(configuration);
        else {
            configurationList.set(configurationList.indexOf(possibleConfiguration), configuration);
        }
    }

    public void delete(Configuration configuration) {
        if (configurationList.contains(configuration))
            configurationList.get(configurationList.indexOf(configuration)).delete();
        configurationList.remove(configuration);
    }

    public Configuration searchByIdentifier(String identifier) {
        for (Configuration configuration : configurationList) {
            if (configuration.getIdentifier().equals(identifier)) {
                return configuration;
            }
        }
        return null;
    }

    public void saveConfigurationFiles() {
        if (!CONFIGURATION_DIR.exists())
            CONFIGURATION_DIR.mkdirs();
        configurationList.forEach(Configuration::saveFile);
    }

    public void loadConfigurationFiles(boolean refresh) {
        if (!CONFIGURATION_DIR.exists())
            return;

        if (!configurationList.isEmpty())
            saveConfigurationFiles();

        configurationList.clear();

        for (File file : Objects.requireNonNull(CONFIGURATION_DIR.listFiles())) {
            if (file.getName().endsWith(".conf")) {
                add(new Configuration(file, refresh));
            }
        }
    }

    public ArrayList<Configuration> getConfigurationList() {
        return configurationList;
    }
}
