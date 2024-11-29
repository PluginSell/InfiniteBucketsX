package com.pluginsell.infinitebucketsx;

import com.pluginsell.infiniteBuckets.BucketItems;
import com.pluginsell.infinitebucketsx.utils.UsesBucketItems;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.pluginsell.infinitebucketsx.Main.checkPlugin;
import static com.pluginsell.infinitebucketsx.Main.initEconomy;

public class BucketEvents implements Listener {
    @EventHandler
    void bucketChangeEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand() != null && player.getItemInHand().hasItemMeta() && (checkBucket(player.getItemInHand()) != null)) {
            checkPlugin();
            if (!Main.data.getConfig().getBoolean("separate-buckets")) {
                String change = Main.data.getConfig().getString("bucket-change");
                if (change == null) {
                    change = "LEFT_CLICK";
                }
                change = change.toUpperCase().replaceAll("_CLICK", "");
                boolean shift = false;
                if (change.contains("SHIFT")) {
                    shift = true;
                    change = change.replaceAll("SHIFT_", "");
                }
                if (shift && !player.isSneaking() || !shift && player.isSneaking()) return;
                if (change.equals("LEFT") && !e.getAction().equals(Action.LEFT_CLICK_AIR) && !e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    return;
                }
                if (change.equals("RIGHT") && !e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    return;
                }
                e.setCancelled(true);
                player.openInventory(new BucketGUI().bucketInv());
            }
        }
    }

    @EventHandler
    void inventoryClickEvent(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equals(new BucketGUI().bucketInv().getName())) {
            checkPlugin();
            e.setCancelled(true);
            ItemStack bucket = null;
            int uses = getUses(player.getItemInHand());
            if (e.getSlot() == 0) {
                bucket = new UsesBucketItems().getBucket("W", uses);
                player.closeInventory();
            } else if (e.getSlot() == 8) {
                bucket = new UsesBucketItems().getBucket("L", uses);
            }
            if (bucket != null) {
                player.getInventory().getItemInHand().setType(bucket.getType());
                player.getInventory().getItemInHand().setItemMeta(bucket.getItemMeta());
                player.closeInventory();
                player.sendMessage(Main.prefix + Main.color("&aYou changed the bucket to an " + bucket.getItemMeta().getDisplayName()));
            }
        }
    }

    @EventHandler
    public void onBucketPlaceEvent(PlayerBucketEmptyEvent e) {
        ItemStack itemStack = e.getPlayer().getItemInHand();
        if (itemStack != null) {
            bucketEvent(itemStack, e);
        }
    }

    void bucketEvent(ItemStack itemStack, PlayerBucketEmptyEvent e) {
        String bucketType = checkBucket(itemStack);
        if (bucketType != null) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            ItemMeta meta = itemStack.getItemMeta();
            checkPlugin();
            int cost = 0;
            if (Main.data.getConfig().contains(bucketType + ".cost")) {
                cost = Main.data.getConfig().getInt(bucketType + ".cost");
            }
            if (cost > 0) {
                Economy econ;
                initEconomy();
                econ = Main.getEconomy();
                if (econ != null) {
                    double balance = econ.getBalance(player);
                    if (balance - cost >= 0) {
                        econ.withdrawPlayer(player, cost);
                        if (Main.data.getConfig().getBoolean("spend-notifications")) {
                            player.sendMessage(Main.prefix + Main.color("&aYou have spent $&6" + cost + " &aplacing an " + meta.getDisplayName()));
                        }
                    } else {
                        if (Main.data.getConfig().getBoolean("insufficient-notifications")) {
                            player.sendMessage(Main.prefix + Main.color("&cYou need to have at least $&6" + cost + " &cto use an " + meta.getDisplayName()));
                            return;
                        }
                    }
                }
            }
            checkUses(player, bucketType);
            player.updateInventory();
            e.getBlockClicked().getRelative(e.getBlockFace()).setType(Material.WATER);
        }
    }

    public String checkBucket(ItemStack itemStack) {
        List<String> itemLore = new ArrayList<>();
        List<String> bucketLore = new ArrayList<>();
        itemStack = itemStack.clone();
        ItemMeta meta = itemStack.getItemMeta();
        for (String lore : meta.getLore()) {
            if (!lore.contains("Uses: ") && !lore.isEmpty()) {
                itemLore.add(lore);
            }
        }

        String bucketType = null;

        if (meta.getDisplayName().equals(new UsesBucketItems().getBucket("W", -1).getItemMeta().getDisplayName())) {
            bucketType = "water-bucket";
            for (String lore : new UsesBucketItems().getBucket("W", -1).getItemMeta().getLore()) {
                if (!lore.contains("Uses: ") && !lore.isEmpty()) {
                    bucketLore.add(lore);
                }
            }
        } else if (meta.getDisplayName().equals(new UsesBucketItems().getBucket("L", -1).getItemMeta().getDisplayName())) {
            bucketType = "lava-bucket";
            for (String lore : new UsesBucketItems().getBucket("L", -1).getItemMeta().getLore()) {
                if (!lore.contains("Uses: ") && !lore.isEmpty()) {
                    bucketLore.add(lore);
                }
            }
        }
        if (itemLore.equals(bucketLore)) {
            return bucketType;
        }
        return null;
    }

    public void checkUses(Player player, String string) {
        ItemStack itemStack = player.getItemInHand();
        ItemMeta meta = itemStack.getItemMeta();
        if (Main.data.getConfig().getBoolean(string + ".toggle-uses")) {
            for (String lore : meta.getLore()) {
                if (lore.contains("Uses: ")) {
                    try {
                        int uses = getUses(itemStack);
                        if (uses > 1) {
                            uses = uses - 1;
                        } else {
                            player.setItemInHand(null);
                        }
                        if (string.equals("water-bucket")) {
                            meta = new UsesBucketItems().getBucket("W", uses).getItemMeta();
                        } else if (string.equals("lava-bucket")) {
                            meta = new UsesBucketItems().getBucket("L", uses).getItemMeta();
                        }
                        break;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            itemStack.setItemMeta(meta);
        }
    }

    public int getUses(ItemStack itemStack) {
        int uses = -1;
        for (String lore : itemStack.getItemMeta().getLore()) {
            if (lore.contains("Uses: ")) {
                try {
                    uses = Integer.parseInt(ChatColor.stripColor(lore).replace("Uses: ", ""));
                    break;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return uses;
    }
}
