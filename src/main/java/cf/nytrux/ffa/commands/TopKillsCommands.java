package cf.nytrux.ffa.commands;

import cf.nytrux.ffa.IRequiresPluginInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class TopKillsCommands implements CommandExecutor, IRequiresPluginInstance {

    //private FFA plugin = FFA.getPlugin(FFA.class);


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Map<String, Integer> players = new HashMap<>();

        for (String uuid : plugin.getStatsConfig().getConfigurationSection("Stats").getKeys(false)) {
            players.put(uuid, plugin.getStatsConfig().getInt("Stats." + uuid + ".kills"));
        }

        sender.sendMessage("§7§m-----------------------");

        String nextTop = "";
        Integer nextTopKills = 0;
        String name = "";

        for (int i = 1; i < 4; i++) {
            for (String playerName : players.keySet()) {
                if (players.get(playerName) > nextTopKills) {

                    nextTop = playerName;
                    name = plugin.getStatsConfig().getString("Stats." + nextTop + ".name");
                    nextTopKills = players.get(playerName);

                }
            }
            sender.sendMessage("§9#" + i + " " + name + "§7: §b" + nextTopKills);
            players.remove(nextTop);
            nextTop = "";
            nextTopKills = 0;
        }
        sender.sendMessage("§7§m-----------------------");

        return true;
    }


}