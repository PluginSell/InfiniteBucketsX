package com.pluginsell.infinitebucketsx;

import com.pluginsell.infiniteBuckets.BucketItems;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class BucketGUI {
    public Inventory bucketInv() {
        Inventory inventory = Bukkit.createInventory(null, 9, Main.color("&cSelect Infinite Bucket Type"));
        inventory.setItem(0, new BucketItems().getBucket("W"));
        inventory.setItem(8, new BucketItems().getBucket("L"));
        return inventory;
    }
}
