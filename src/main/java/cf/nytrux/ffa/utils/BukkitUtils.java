package cf.nytrux.ffa.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class BukkitUtils {

    public static String translateColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String translateColor(char character, String text) {
        return ChatColor.translateAlternateColorCodes(character, text);
    }

    public static String getFormattedHealth(Player player) {
        return String.valueOf(
                (int) (player.getHealth() / 2.0D)
        );
    }

}
