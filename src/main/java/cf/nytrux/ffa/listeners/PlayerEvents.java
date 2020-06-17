package cf.nytrux.ffa.listeners;

import cf.nytrux.ffa.IRequiresPluginInstance;
import cf.nytrux.ffa.object.BetterUserManager;
import cf.nytrux.ffa.object.GameUser;
import cf.nytrux.ffa.utils.BukkitUtils;
import cf.nytrux.ffa.utils.Kit;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class PlayerEvents implements Listener, IRequiresPluginInstance {

    private final BetterUserManager betterUserManager;

    @EventHandler
    public void onHungerLevelChange(final FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.setJoinMessage(null);

        final Player player = event.getPlayer();

        player.getInventory().clear();
        player.setHealth(20);
        player.setLevel(0);

        Kit.applyKit(player);

        // get user manager, load user
        plugin.getBetterUserManager().loadUser(player.getUniqueId());

        final String message = BukkitUtils.translateColor(this.plugin.getMainConfig().getString("lang.welcome-message"));
        event.getPlayer().sendMessage(message);

        YamlConfiguration config = this.plugin.getMainConfig();

        World world = Bukkit.getWorld(config.getString("spawn.world"));

        if(world == null)
            return;

        Location location = new Location(world, config.getDouble("spawn.location.x"), config.getDouble("spawn.location.y"), config.getDouble("spawn.location.z")
                , (float) config.getDouble("spawn.location.yaw"), (float) config.getDouble("spawn.location.pitch"));

        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(location), 1L);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        this.betterUserManager.unloadUser(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        YamlConfiguration config = this.plugin.getMainConfig();

        World world = Bukkit.getWorld(this.plugin.getConfig().getString("spawn.world"));

        if(world == null)
            return;

        if (!player.getWorld().getName().equalsIgnoreCase(this.plugin.getMainConfig().getString("spawn.world")))
            return;

        double x = config.getDouble("spawn" + ".location.x", 0.0);
        double y = config.getDouble("spawn" + ".location.y", 0.0);
        double z = config.getDouble("spawn" + ".location.z", 0.0);

        Location location = new Location(world, x, y, z);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.teleport(location), 1L);
    }


    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        event.setDroppedExp(0);
        event.getDrops().clear();

        final YamlConfiguration config = plugin.getMainConfig();
        final Player player = event.getEntity();

        if(event.getEntity().getKiller() == null) {
            event.setDeathMessage(BukkitUtils.translateColor(config.getString("lang.unknown-death-message")
                    .replace("%victim%", player.getName())));
            return;
        }

        final Player killer = event.getEntity().getKiller();

        final Random random = new Random();

        final List<String> messagesList = config.getStringList("lang.death-messages");
        final String message = messagesList.get(random.nextInt(messagesList.size()));

        event.setDeathMessage(BukkitUtils.translateColor(message.replace("%player%", player.getName())
                .replace("%killer%", killer.getName())
                .replace("%victim%", player.getName())));

        killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 1));
        killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 2));

        GameUser killerUser = this.betterUserManager.getUsers().get(killer.getUniqueId());
        killerUser.setKills(killerUser.getKills() + 1);
        killerUser.setKillstreak(killerUser.getKillstreak() + 1);

        GameUser deadUser = this.betterUserManager.getUsers().get(player.getUniqueId());
        deadUser.setKillstreak(0);
        deadUser.setDeaths(deadUser.getDeaths() + 1);

        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(6));

        ItemMeta meta = apple.getItemMeta();
        meta.setDisplayName(BukkitUtils.translateColor("&dGapple"));

        apple.setItemMeta(meta);

        killer.getInventory().addItem(apple);
        killer.updateInventory();

        killer.sendMessage(BukkitUtils.translateColor(this.plugin.getMainConfig().getString("lang.killer-info-message")
                .replace("%victim%", player.getName())
                .replace("%health%", BukkitUtils.getFormattedHealth(killer))));

        player.sendMessage(BukkitUtils.translateColor(this.plugin.getMainConfig().getString("lang.killed-info-message")
                .replace("%killer%", killer.getName())
                .replace("%health%", BukkitUtils.getFormattedHealth(killer))));

        killerUser.updateScoreboard();
        deadUser.updateScoreboard();
    }



    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();

        player.getInventory().clear();
        Kit.applyKit(player);

        GameUser user = this.betterUserManager.getUsers().get(player.getUniqueId());
        user.updateScoreboard();

        YamlConfiguration config = plugin.getMainConfig();

        World world = Bukkit.getWorld(config.getString("spawn.world"));

        if (world == null) {
            plugin.getLogger().warning("Spawn world isn't defined, the player won't be teleported.");
            return;
        }

        Location loc = new Location(world, config.getDouble("spawn.location.x", 0.0), config.getDouble("spawn.location.y", 0.0), config.getDouble("spawn.location.z", 0.0),
                (float) config.getDouble("spawn.location.yaw", 0.0F), (float) config.getDouble("spawn.location.pitch", 0.0F));

        event.setRespawnLocation(loc);
    }


}