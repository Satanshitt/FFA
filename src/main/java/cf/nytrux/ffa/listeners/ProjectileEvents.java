package cf.nytrux.ffa.listeners;

import cf.nytrux.ffa.FreeForAll;
import cf.nytrux.ffa.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;


public class ProjectileEvents implements Listener {

    private final FreeForAll plugin = FreeForAll.getInstance();

    @SuppressWarnings("unused")
    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent e) {

        if (e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
            final Player victim = (Player) e.getEntity();
            final Player bowShooter = (Player) ((Arrow) e.getDamager()).getShooter();

            final double victimHealth = victim.getHealth();
            int damage = (int) e.getFinalDamage();
            final int victimHealthWithDamage = (int) victimHealth - damage;

            if (victimHealthWithDamage > 0)
                bowShooter.sendMessage(BukkitUtils.translateColor(this.plugin.getMainConfig().getString("lang.bow-hit-message")
                        .replace("%victim%", victim.getName())
                        .replace("%health%", String.valueOf(victimHealthWithDamage / 2.0D))));

        }

    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow)
            Bukkit.getScheduler().runTaskLater(plugin, () -> event.getEntity().remove(), 20);

    }

}