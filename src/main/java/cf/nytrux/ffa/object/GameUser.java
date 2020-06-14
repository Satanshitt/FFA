package cf.nytrux.ffa.object;

import cf.nytrux.ffa.FreeForAll;
import cf.nytrux.ffa.utils.BukkitUtils;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GameUser {

    // Static
    private static final String STATS_PATH = "Stats";
    private static final String SCOREBOARD_PATH = "Scoreboard";
    private static final YamlConfiguration mainConfig = FreeForAll.getInstance().getMainConfig();
    private static final YamlConfiguration stats = FreeForAll.getInstance().getStatsConfig();

    @Getter
    @Setter
    private int kills;

    @Getter
    @Setter
    private int deaths;

    @Getter
    @Setter
    private int killstreak;

    // Fields
    @Getter
    private final UUID uuid;
    private final String name;
    private final BPlayerBoard board;

    public GameUser(final UUID newUserUniqueId) {
        this.uuid = newUserUniqueId;
        this.name = Bukkit.getPlayer(newUserUniqueId).getName();

        // Stats setup
        this.kills = stats.getInt(STATS_PATH + "." + this.uuid.toString() + "." + "kills", 0);
        this.deaths = stats.getInt(STATS_PATH + "." + this.uuid.toString() + "." + "deaths", 0);
        this.killstreak = stats.getInt(STATS_PATH + "." + this.uuid.toString() + "." + "killstreak", 0);

        // Scoreboard setup
        this.board = Netherboard.instance().createBoard(Bukkit.getPlayer(uuid), "FreeForAll");
        this.updateScoreboard();
    }

    public void unload() {
        // User unload

        FreeForAll.getInstance().getLogger().info("Unloading user " + this.uuid + " [" + this.kills + " ; " + this.deaths + " ; " + this.killstreak + " ]");

        Netherboard.instance().deleteBoard(Bukkit.getPlayer(uuid));

        stats.set(STATS_PATH + "." + this.uuid.toString() + "." + "kills", this.kills);
        stats.set(STATS_PATH + "." + this.uuid.toString() + "." + "deaths", this.deaths);
        stats.set(STATS_PATH + "." + this.uuid.toString() + "." + "killstreak", this.killstreak);
        stats.set(STATS_PATH + "." + this.uuid.toString() + "." + "name", this.name);

        FreeForAll.getInstance().getLogger().info("User unload finished");
    }

    public final void updateScoreboard() {

        if (!FreeForAll.getInstance().getMainConfig().getBoolean(SCOREBOARD_PATH + "." + "enabled", false))
            return;

        final List<String> boardLines = mainConfig.getStringList(SCOREBOARD_PATH + "." + "lines");

        this.board.setName(BukkitUtils.translateColor(mainConfig.getString(SCOREBOARD_PATH + "." + "title")));

        Collections.reverse(boardLines);

        for (int i = 0; i < boardLines.size(); i++)
            this.board.set(BukkitUtils.translateColor(boardLines.get(i)
                    .replace("{KILLS}", String.valueOf(this.kills))
                    .replace("{DEATHS}", String.valueOf(this.deaths))
                    .replace("{KILLSTREAK}", String.valueOf(this.killstreak))), i+1);

    }

}
