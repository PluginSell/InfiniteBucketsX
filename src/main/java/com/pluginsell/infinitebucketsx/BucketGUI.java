package com.pluginsell.infinitebucketsx;

import com.pluginsell.infinitebucketsx.utils.UsesBucketItems;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class BucketGUI {
    public Inventory bucketInv() {
        Inventory inventory = Bukkit.createInventory(null, 9, Main.color("&cSelect Infinite Bucket Type"));
        inventory.setItem(0, new UsesBucketItems().getBucket("W", 0));
        inventory.setItem(8, new UsesBucketItems().getBucket("L", 0));
        return inventory;
    }
}
