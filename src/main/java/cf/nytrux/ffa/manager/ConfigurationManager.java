package cf.nytrux.ffa.manager;

import cf.nytrux.ffa.IRequiresPluginInstance;
import org.jetbrains.annotations.NotNull;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

// DONE_TODO: Create asynchronous saveConfiguration method (parameters: YamlConfiguration object)

@SuppressWarnings("unused")
public class ConfigurationManager implements IRequiresPluginInstance {

    private final HashMap<String, YamlConfiguration> configurations = new HashMap<>();

    public ConfigurationManager() {
        plugin.getLogger().info("Initializing configuration manager...");
    }

    /**
     * @param fileName          Name of the file to be created, it mustn't end with an extension.
     * @param fileExtension     The extension of the configuration file.
     * @param loadConfiguration Whether or not to load the configuration into the object after its creation.
     * @throws IllegalArgumentException Thrown if the fileName argument contains an extension.
     */
    public final void createConfiguration(@NotNull String fileName, @NotNull String fileExtension, boolean loadConfiguration) throws IllegalArgumentException {

        if (fileName.endsWith(".yml") || fileName.endsWith(".csv"))
            throw new IllegalArgumentException("You mustn't use file extensions in the file name.");

        this.createConfiguration(new File(plugin.getDataFolder(), fileName + fileExtension), loadConfiguration);
    }



    /**
     * @param configFile        The file that contains the configuration.
     * @param loadConfiguration Whether or not to load the configuration into the object after its creation.
     */
    public final void createConfiguration(@NotNull final File configFile, boolean loadConfiguration) {

        File pluginDataFolder = plugin.getDataFolder();

        if (!pluginDataFolder.exists()) {
            plugin.getLogger().info("Plugin directory don't exist, creating it...");
            boolean result = pluginDataFolder.mkdirs();

            if (!result) {
                plugin.getLogger().severe("Could not create plugin directory.");
                return;
            }

        }

        try {

            if (configFile.exists()) {
                plugin.getLogger().info("Config file " + configFile.getName() + " already exists.");
            } else {
                plugin.saveResource(configFile.getName(), false);
                plugin.getLogger().info("Config file " + configFile.getName() + " has been created.");
            }

        } catch (SecurityException exception) {
            plugin.getLogger().severe("Config file " + configFile.getName() + " could not be created.");
            exception.printStackTrace();
        }

        this.configurations.put(configFile.getName(), new YamlConfiguration());

        if (loadConfiguration)
            this.loadConfiguration(configFile);
    }



    /**
     * @param fileName      Name of the file to be created, it mustn't end with an extension.
     * @param fileExtension The extension of the configuration file.
     * @throws IllegalArgumentException Thrown if the fileName argument contains an extension or the method createConfiguration() hasn't been executed.
     */
    public final void loadConfiguration(@NotNull final String fileName, @NotNull final String fileExtension) throws IllegalArgumentException {

        if (fileName.endsWith(".yml") || fileName.endsWith(".csv"))
            throw new IllegalArgumentException("You mustn't use file extensions in the file name.");

        this.loadConfiguration(new File(plugin.getDataFolder(), fileName + fileExtension));

    }



    /**
     * @param configFile The file that contains the configuration.
     * @throws IllegalArgumentException Thrown if the fileName argument contains an extension or the method createConfiguration() hasn't been executed.
     */
    public final void loadConfiguration(@NotNull final File configFile) throws IllegalArgumentException {

        if (!configFile.exists() || !this.configurations.containsKey(configFile.getName()))
            throw new IllegalStateException("You must create the configuration file before loading it.");

        try {
            this.configurations.get(configFile.getName()).load(configFile);
        } catch (IOException | InvalidConfigurationException exception) {
            plugin.getLogger().severe("Could not load configuration file " + configFile.getName() + ": " + exception.getClass().getName());
            exception.printStackTrace();
        }

    }



    /**
     *
     * @param fileName Name of the configuration file.
     * @return The YamlConfiguration object containing the file's YAML values.
     */
    public final YamlConfiguration getConfiguration(@NotNull final String fileName) {
        return this.configurations.get(fileName);
    }



    /**
     *
     * @param fileName Name of the configuration file.
     * @param async Whether or not the configuration will be saved asynchronously, useful when the server is stopping
     */
    public final void saveConfiguration(@NotNull final String fileName, boolean async) {

        File configFile = new File(plugin.getDataFolder(), fileName);

        if(async) {

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    this.getConfiguration(fileName).save(configFile);
                } catch(IOException exception) {
                    plugin.getLogger().severe("Could not save configuration file " + fileName + ": IOException");
                    exception.printStackTrace();
                }
            });


        } else {

            try {
                this.getConfiguration(fileName).save(configFile);
            } catch(IOException exception) {
                plugin.getLogger().severe("Could not save configuration file " + fileName + ": IOException");
                exception.printStackTrace();
            }


        }

    }
}
