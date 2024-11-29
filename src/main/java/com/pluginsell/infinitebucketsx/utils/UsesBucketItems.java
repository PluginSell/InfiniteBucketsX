package com.pluginsell.infinitebucketsx.utils;

import com.pluginsell.infiniteBuckets.BucketItems;
import com.pluginsell.infinitebucketsx.Main;
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
            if(Main.data.getConfig().getBoolean("water-bucket.toggle-uses") && uses > -1) {
                itemStack = (new UsesBucketItems()).waterBucket(uses);
            } else {
                itemStack = new BucketItems().getBucket("W");
            }
        } else {
            if(Main.data.getConfig().getBoolean("lava-bucket.toggle-uses") && uses > -1) {
                itemStack = (new UsesBucketItems()).lavaBucket(uses);
            } else {
                itemStack = new BucketItems().getBucket("L");
            }
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
        lore.add("");
        lore.add("§6Uses: §e" + uses);
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
        lore.add("");
        lore.add("§6Uses: §e" + uses);
        bucketMeta.setLore(lore);
        lavaBucket.setItemMeta(bucketMeta);
        lavaBucket.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        return lavaBucket;
    }
}
