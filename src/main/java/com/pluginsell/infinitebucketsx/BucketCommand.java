package com.pluginsell.infinitebucketsx;

import com.pluginsell.infinitebucketsx.utils.UsesBucketItems;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class BucketCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean hasPerm = sender instanceof Player;
        for (PermissionAttachmentInfo perm : sender.getEffectivePermissions()) {
            if (perm.getPermission().startsWith("infinitebucketsx.")) {
                hasPerm = true;
                break;
            }
        }
        if (hasPerm) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (!(sender instanceof Player) || sender.hasPermission("infinitebucketsx.give")) {
                        if (args.length >= 2) {
                            Player player = Bukkit.getPlayer(args[1]);
                            if (player == null || !player.isOnline()) {
                                sender.sendMessage(Main.prefix + Main.color("&cCould not find that player."));
                            } else {
                                String type = "WATER";
                                int uses = -1;
                                if (Main.data.getConfig().getBoolean("separate-buckets")) {
                                    if (args.length >= 3) {
                                        try {
                                            uses = Integer.parseInt(args[2]);
                                        } catch (NumberFormatException ignored) {
                                            try {
                                                uses = Integer.parseInt(args[3]);
                                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
                                            }
                                        }
                                        if (args[2].toUpperCase().startsWith("L")) {
                                            type = "LAVA";
                                        }
                                    }
                                } else {
                                    if (args.length >= 3) {
                                        try {
                                            uses = Integer.parseInt(args[2]);
                                        } catch (NumberFormatException ignored) {
                                            try {
                                                uses = Integer.parseInt(args[3]);
                                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
                                            }
                                        }
                                    }
                                    try {
                                        if (args[2].toUpperCase().startsWith("L")) {
                                            type = "LAVA";
                                        }
                                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                                }
                                if (player.getInventory().firstEmpty() == -1) {
                                    player.getWorld().dropItemNaturally(player.getLocation(), new UsesBucketItems().getBucket(type, uses));
                                } else {
                                    player.getInventory().addItem(new UsesBucketItems().getBucket(type, uses));
                                }
                                sender.sendMessage(Main.prefix + Main.color("&aYou have given &6" + player.getName() + " &aan &e" + new UsesBucketItems().getBucket(type, uses).getItemMeta().getDisplayName()));
                                if (sender instanceof Player) {
                                    player.sendMessage(Main.prefix + Main.color("&6" + sender.getName() + " &ahas given you an " + new UsesBucketItems().getBucket(type, uses).getItemMeta().getDisplayName()));
                                } else {
                                    player.sendMessage(Main.prefix + Main.color("&aYou have been given an " + new UsesBucketItems().getBucket(type, uses).getItemMeta().getDisplayName()));
                                }
                                return true;
                            }
                        }
                    } else {
                        sender.sendMessage(Main.prefix + Main.color("&cYou do not have permissions to use that command."));
                        return true;
                    }
                } else {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (!(sender instanceof Player) || sender.hasPermission("infinitebucketsx.reload")) {
                            Main.data.reloadConfig();
                            sender.sendMessage(Main.prefix + Main.color("&aSuccessfully reloaded config.yml."));
                        } else {
                            sender.sendMessage(Main.prefix + Main.color("&cYou do not have permissions to use that command."));
                        }
                        return true;
                    }
                }
            }
            sender.sendMessage(Main.prefix + Main.color("&cCommand Usage: &a/infinitebucketsx reload"));
            if (!Main.data.getConfig().getBoolean("separate-buckets")) {
                sender.sendMessage(Main.prefix + Main.color("&cCommand Usage: &a/infinitebucketsx give <player>"));
            } else {
                sender.sendMessage(Main.prefix + Main.color("&cCommand Usage: &a/infinitebucketsx give <player> (water/lava>"));
            }
        } else {
            sender.sendMessage(Main.prefix + Main.color("&cYou do not have permissions to use that command."));
        }
        return true;
    }
}
