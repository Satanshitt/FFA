package cf.nytrux.ffa;

import cf.nytrux.ffa.commands.MainCommand;
import cf.nytrux.ffa.commands.TopKillsCommands;
import cf.nytrux.ffa.listeners.ProjectileEvents;
import cf.nytrux.ffa.listeners.PlayerEvents;
import cf.nytrux.ffa.object.BetterUserManager;
import cf.nytrux.ffa.manager.ConfigurationManager;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;


public class FreeForAll extends JavaPlugin {

    @Getter
    private static FreeForAll instance;

    @Getter
    private ConfigurationManager configurationManager;
    @Getter
    private final BetterUserManager betterUserManager = new BetterUserManager();


    @Getter
    private YamlConfiguration mainConfig;
    @Getter
    private YamlConfiguration statsConfig;

    @Override
    public void onEnable() {

        instance = this;


        // Managers
        configurationManager = new ConfigurationManager();


        // Configuration setup
        configurationManager.createConfiguration("config", ".yml", true);
        this.mainConfig = configurationManager.getConfiguration("config.yml");

        configurationManager.createConfiguration("stats", ".yml", true);
        this.statsConfig = configurationManager.getConfiguration("stats.yml");

        this.registerCommands();
        this.registerListeners();
    }



    @Override
    public void onDisable() {



        this.getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);

        this.betterUserManager.unload();

        configurationManager.saveConfiguration("config.yml", false);
        configurationManager.saveConfiguration("stats.yml", false);

        instance = null;
    }

    public final void registerCommands() {
        this.getCommand("ffa").setExecutor(new MainCommand(this.betterUserManager));
        this.getCommand("topkills").setExecutor(new TopKillsCommands());
    }

    public final void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerEvents(this.betterUserManager), this);
        getServer().getPluginManager().registerEvents(new ProjectileEvents(), this);
    }

}