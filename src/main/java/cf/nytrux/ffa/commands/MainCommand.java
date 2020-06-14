package cf.nytrux.ffa.commands;

import cf.nytrux.ffa.FreeForAll;
import cf.nytrux.ffa.object.BetterUserManager;
import cf.nytrux.ffa.object.GameUser;
import cf.nytrux.ffa.utils.BukkitUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MainCommand implements CommandExecutor {

    private final BetterUserManager betterUserManager;
    private final FreeForAll plugin = FreeForAll.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player))
            return false;

        final Player player = (Player) sender;

        switch(args.length) {
            case 1:
                if(args[0].equalsIgnoreCase("admin")) {
                    player.sendMessage(BukkitUtils.translateColor(""));
                    player.sendMessage(BukkitUtils.translateColor("FFA Core &7- &7by Satanshitt & F3DEX22"));
                    player.sendMessage(BukkitUtils.translateColor(""));
                    player.sendMessage(BukkitUtils.translateColor("&7- /ffa admin &f- Muestra este mensaje de ayuda"));
                    player.sendMessage(BukkitUtils.translateColor("&7- /ffa admin setspawn &f- Establece el punto de aparición"));
                }

                if(!args[0].equalsIgnoreCase("stats"))
                    break;

                GameUser user = this.betterUserManager.getUsers().get(player.getUniqueId());

                player.sendMessage(BukkitUtils.translateColor(" "));
                player.sendMessage(BukkitUtils.translateColor("&9Estadísticas del jugador &b" + player.getName()));
                player.sendMessage(BukkitUtils.translateColor(" "));
                player.sendMessage(BukkitUtils.translateColor("&7Asesinatos: &b" + user.getKills() + "&7."));
                player.sendMessage(BukkitUtils.translateColor("&7Muertes: &b" + user.getDeaths() + "&7."));
                player.sendMessage(BukkitUtils.translateColor(" "));

                return true;

            case 2:
                if(!args[0].equalsIgnoreCase("admin"))
                    return false;

                if(!args[1].equalsIgnoreCase("setspawn"))
                    return false;

                if(!player.hasPermission("ffa.admin")) {
                    player.sendMessage(BukkitUtils.translateColor(this.plugin.getMainConfig().getString("lang.no-permission")));
                    return true;
                }

                YamlConfiguration config = plugin.getMainConfig();
                final String spawnLocationPath = "spawn.location";
                final String spawnWorldPath = "spawn.world";

                Location loc = player.getLocation();

                config.set(spawnWorldPath, loc.getWorld().getName());

                config.set(spawnLocationPath + "." + "x", loc.getX());
                config.set(spawnLocationPath + "." + "y", loc.getY());
                config.set(spawnLocationPath + "." + "z", loc.getZ());

                config.set(spawnLocationPath + "." + "yaw", loc.getYaw());
                config.set(spawnLocationPath + "." + "pitch", loc.getPitch());

                player.sendMessage(BukkitUtils.translateColor(config.getString("lang.spawn-set")));
                return true;


            default:
                player.sendMessage(BukkitUtils.translateColor(""));
                player.sendMessage(BukkitUtils.translateColor("&9 FFA Core &7- &7by Satanshitt & F3DEX22"));
                player.sendMessage(BukkitUtils.translateColor(""));
                player.sendMessage(BukkitUtils.translateColor("&7- /ffa &f- Muestra este mensaje de ayuda"));
                player.sendMessage(BukkitUtils.translateColor("&7- /ffa stats &f- Muestra tus estadísticas"));
                player.sendMessage(BukkitUtils.translateColor("&7- /ffa admin &f- Comandos para administradores"));

                return true;
        }

        return true;
    }


}