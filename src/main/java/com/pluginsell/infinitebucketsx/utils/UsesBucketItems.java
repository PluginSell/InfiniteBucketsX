package com.pluginsell.infinitebucketsx.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class UsesBucketItems {
    public ItemStack getBucket(String type, int uses) {
        if(type.startsWith("L")) {
            type = "L";
        } else {
            type = "W";
        }
        ItemStack itemStack;
        if (type.equals("W")) {
            itemStack = (new UsesBucketItems()).waterBucket(uses);
        } else {
            itemStack = (new UsesBucketItems()).lavaBucket(uses);
        }
        return itemStack;
    }

    private ItemStack waterBucket(int uses) {
        ItemStack waterBucket = new ItemStack(Material.WATER_BUCKET);
        ItemMeta bucketMeta = waterBucket.getItemMeta();
        bucketMeta.setDisplayName("§b§lInfinite Water Bucket");
        bucketMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> lore = new ArrayList();
        lore.add("");
        lore.add("§bRight click this bucket to use.");
        lore.add("§bThis bucket will never empty!");
        if(uses > 0) {
        lore.add("");
            lore.add("§6Uses: §e" + uses);
        }
        bucketMeta.setLore(lore);
        waterBucket.setItemMeta(bucketMeta);
        waterBucket.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        return waterBucket;
    }

    private ItemStack lavaBucket(int uses) {
        ItemStack lavaBucket = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta bucketMeta = lavaBucket.getItemMeta();
        bucketMeta.setDisplayName("§c§lInfinite Lava Bucket");
        bucketMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> lore = new ArrayList();
        lore.add("");
        lore.add("§bRight click this bucket to use.");
        lore.add("§bThis bucket will never empty!");
        if(uses > 0) {
            lore.add("");
            lore.add("§6Uses: §e" + uses);
        }
        bucketMeta.setLore(lore);
        lavaBucket.setItemMeta(bucketMeta);
        lavaBucket.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        return lavaBucket;
    }
}
