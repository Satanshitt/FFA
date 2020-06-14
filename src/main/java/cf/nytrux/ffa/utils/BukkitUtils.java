package cf.nytrux.ffa.utils;

import net.md_5.bungee.api.ChatColor;

public class BukkitUtils {

    public static String translateColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String translateColor(char character, String text) {
        return ChatColor.translateAlternateColorCodes(character, text);
    }

}
