package cf.nytrux.ffa.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Kit {


    public static void applyKit(Player p) {

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        ItemStack arrows = new ItemStack(Material.ARROW);
        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta swordItemMeta = sword.getItemMeta();
        ItemMeta arrow = arrows.getItemMeta();
        ItemMeta rodItemMeta = rod.getItemMeta();
        ItemMeta chestplateItemMeta = chestplate.getItemMeta();
        ItemMeta helmetItemMeta = helmet.getItemMeta();
        ItemMeta leggingsItemMeta = leggings.getItemMeta();
        ItemMeta bootsItemMeta = boots.getItemMeta();
        ItemMeta goldenAppleItemMeta = goldenApple.getItemMeta();
        ItemMeta bowItemMeta = bow.getItemMeta();

        swordItemMeta.setDisplayName(BukkitUtils.translateColor("&dSword"));
        rodItemMeta.setDisplayName(BukkitUtils.translateColor("&dRod"));
        chestplateItemMeta.setDisplayName(BukkitUtils.translateColor("&dChestplate"));
        helmetItemMeta.setDisplayName(BukkitUtils.translateColor("&dHelmet"));
        leggingsItemMeta.setDisplayName(BukkitUtils.translateColor("&dLeggings"));
        bootsItemMeta.setDisplayName(BukkitUtils.translateColor("&dBoots"));
        goldenAppleItemMeta.setDisplayName(BukkitUtils.translateColor("&dGapple"));
        bowItemMeta.setDisplayName(BukkitUtils.translateColor("&dBow"));
        swordItemMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        chestplateItemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        leggingsItemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        bootsItemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        helmetItemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        bowItemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        sword.setItemMeta(swordItemMeta);
        rod.setItemMeta(rodItemMeta);
        arrows.setItemMeta(arrow);
        chestplate.setItemMeta(chestplateItemMeta);
        helmet.setItemMeta(helmetItemMeta);
        leggings.setItemMeta(leggingsItemMeta);
        boots.setItemMeta(bootsItemMeta);
        goldenApple.setItemMeta(goldenAppleItemMeta);
        bow.setItemMeta(bowItemMeta);
        goldenApple.setAmount(5);
        arrows.setAmount(32);

        p.getInventory().setItem(0, sword);
        p.getInventory().setItem(1, rod);
        p.getInventory().setItem(2, bow);
        p.getInventory().setItem(8, goldenApple);
        p.getInventory().setItem(7, arrows);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setHelmet(helmet);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);


    }

}