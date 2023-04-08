package com.bownet.drops;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class ChestDropCMD implements CommandExecutor {

    private Main main;
    public ChestDropCMD(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String startprefix = main.getConfig().getString("Prefix");
            String prefix = ChatColor.translateAlternateColorCodes('&', startprefix);
            if (player.hasPermission("bowdrops.chestdrop")) {
                if (args.length == 3) {
                    String codeInUse = args[0];
                    int xcoord = Integer.parseInt(args[1]);
                    int zcoord = Integer.parseInt(args[2]);
                    int ycoord = player.getWorld().getHighestBlockYAt(xcoord, zcoord) + 1;
                    List<String> codes = main.getConfig().getStringList("Codes");
                    List<String> rewards = main.getConfig().getStringList("Rewards");
                    if (codes.contains(codeInUse)) {
                        int codeIndex = codes.indexOf(codeInUse);
                        String rewardItem = rewards.get(codeIndex);
                        if (rewardItem.contains("ORAXEN:")) {
                            String nameOxnItem = rewardItem.replace("ORAXEN:", "");
                            String nameOxnItem2 = nameOxnItem.toLowerCase();
                            ItemBuilder OxnUnBuiltItem = OraxenItems.getItemById(nameOxnItem2);
                            if (OxnUnBuiltItem != null) {
                                ItemStack OxnBuiltItem = OxnUnBuiltItem.build();
                                int timeToWait = main.getConfig().getInt("EarlyNotifyAndDelay");
                                player.sendMessage(prefix + " " + ChatColor.GREEN + "The chest drop will be dropped in " + timeToWait + " minutes.");
                                Bukkit.broadcast(prefix + " " + ChatColor.GOLD + "A chest drop will be happening in " + timeToWait + " minutes.", "bowdrops.earlynotify");
                                int timeToWaitTicks = timeToWait * 1200;
                                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                                    Block block = player.getWorld().getBlockAt(xcoord, ycoord, zcoord);
                                    block.setType(Material.CHEST);
                                    Container chest = (Container) block.getState();
                                    chest.getInventory().setItem(13, OxnBuiltItem);
                                    ItemStack identifierItem = new ItemStack(Material.ANDESITE, 1);
                                    ItemMeta identifierMeta = identifierItem.getItemMeta();
                                    identifierMeta.setDisplayName(ChatColor.DARK_AQUA + "BowDrops Identifier");
                                    identifierItem.setItemMeta(identifierMeta);
                                    chest.getInventory().setItem(0, identifierItem);
                                    ArmorStand hologram = (ArmorStand) player.getWorld().spawn(new Location(player.getWorld(), xcoord, ycoord, zcoord), ArmorStand.class);
                                    hologram.setInvisible(true);
                                    hologram.setGravity(false);
                                    hologram.setInvulnerable(true);
                                    hologram.setCustomNameVisible(true);
                                    hologram.setCustomName(ChatColor.GOLD + "Chest Drop, open for a reward!");
                                    Firework firework = player.getWorld().spawn(new Location(player.getWorld(), xcoord, ycoord+1, zcoord), Firework.class);
                                    FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta();
                                    meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build());
                                    meta.setPower(2);
                                    firework.setFireworkMeta(meta);
                                    Bukkit.broadcastMessage(prefix + " " + ChatColor.DARK_AQUA + "A chest has dropped! " + ChatColor.GREEN + "X: " + xcoord + ", Z: " + zcoord);
                                    int activeTime = main.getConfig().getInt("ActiveTime");
                                    int activeTimeTicks = activeTime * 72000;
                                    if (activeTime <= 0) {
                                        player.sendMessage(prefix + " " + ChatColor.AQUA + "This drop will not expire.");
                                    } else {
                                        player.sendMessage(prefix + " " + ChatColor.AQUA + "This drop will expire in " + ChatColor.DARK_GREEN + activeTime + ChatColor.AQUA + " hours.");
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                                            chest.getInventory().clear();
                                            chest.setType(Material.AIR);
                                            for (Entity entity : chest.getLocation().getChunk().getEntities()) {
                                                if (entity.getLocation().distance(chest.getLocation()) <= 1.5) {
                                                    if (entity.getType() == EntityType.ARMOR_STAND) {
                                                        entity.remove();
                                                    }
                                                }
                                            }
                                        }, activeTimeTicks);
                                    }
                                    }, timeToWaitTicks);
                            } else {
                                player.sendMessage(prefix + " " + ChatColor.RED + "There was an error, as the item couldn't be found.");
                                System.out.println("BowDrops: The drop item couldn't be found!");
                            }
                        } else if (rewardItem.contains("MINECRAFT:")) {
                            String nameMCItem = rewardItem.replace("MINECRAFT:", "");
                            Material MCItemName = Material.getMaterial(nameMCItem);
                            if (MCItemName != null) {
                                ItemStack MCItem = new ItemStack(MCItemName, 1);
                                int timeToWait = main.getConfig().getInt("EarlyNotifyAndDelay");
                                player.sendMessage(prefix + " " + ChatColor.GREEN + "The chest drop will be dropped in " + timeToWait + " minutes.");
                                if (main.getConfig().getBoolean("ConnectToBungee") == false) {
                                    Bukkit.broadcast(prefix + " " + ChatColor.GOLD + "A chest drop will be happening in " + timeToWait + " minutes.", "bowdrops.earlynotify");
                                } else if (main.getConfig().getBoolean("ConnectToBungee") == true) {
                                    System.out.println("BowDrops: Connections to Bungee don't work yet!");
                                }
                                int timeToWaitTicks = timeToWait * 1200;
                                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                                    Block block = player.getWorld().getBlockAt(xcoord, ycoord, zcoord);
                                    block.setType(Material.CHEST);
                                    Container chest = (Container) block.getState();
                                    chest.getInventory().setItem(13, MCItem);
                                    ItemStack identifierItem = new ItemStack(Material.ANDESITE, 1);
                                    ItemMeta identifierMeta = identifierItem.getItemMeta();
                                    identifierMeta.setDisplayName(ChatColor.DARK_AQUA + "BowDrops Identifier");
                                    identifierItem.setItemMeta(identifierMeta);
                                    chest.getInventory().setItem(0, identifierItem);
                                    ArmorStand hologram = (ArmorStand) player.getWorld().spawn(new Location(player.getWorld(), xcoord, ycoord, zcoord), ArmorStand.class);
                                    hologram.setInvisible(true);
                                    hologram.setGravity(false);
                                    hologram.setInvulnerable(true);
                                    hologram.setCustomNameVisible(true);
                                    hologram.setCustomName(ChatColor.GOLD + "Chest Drop, open for a reward!");
                                    Firework firework = player.getWorld().spawn(new Location(player.getWorld(), xcoord, ycoord+1, zcoord), Firework.class);
                                    FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta();
                                    meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build());
                                    meta.setPower(2);
                                    firework.setFireworkMeta(meta);
                                    Bukkit.broadcastMessage(prefix + " " + ChatColor.DARK_AQUA + "A chest has dropped! " + ChatColor.GREEN + "X: " + xcoord + ", Z: " + zcoord);
                                    int activeTime = main.getConfig().getInt("ActiveTime");
                                    int activeTimeTicks = activeTime * 72000;
                                    if (activeTime <= 0) {
                                        player.sendMessage(prefix + " " + ChatColor.AQUA + "This drop will not expire.");
                                    } else {
                                        player.sendMessage(prefix + " " + ChatColor.AQUA + "This drop will expire in " + ChatColor.DARK_GREEN + activeTime + ChatColor.AQUA + " hours.");
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                                            chest.getInventory().clear();
                                            chest.setType(Material.AIR);
                                            for (Entity entity : chest.getLocation().getChunk().getEntities()) {
                                                if (entity.getLocation().distance(chest.getLocation()) <= 1.5) {
                                                    if (entity.getType() == EntityType.ARMOR_STAND) {
                                                        entity.remove();
                                                    }
                                                }
                                            }
                                        }, activeTimeTicks);
                                    }
                                    }, timeToWaitTicks);
                            } else {
                                player.sendMessage(prefix + " " + ChatColor.RED + "There was an error, as the item couldn't be found.");
                                System.out.println("BowDrops: The drop item couldn't be found!");
                            }
                        } else {
                            player.sendMessage(prefix + " " + ChatColor.RED + "There was an error, as the item couldn't be found.");
                            System.out.println("BowDrops: The drop item couldn't be found!");
                        }
                    } else {
                        player.sendMessage(prefix + " " + ChatColor.RED + "This drop code could not be found.");
                    }
                } else {
                    player.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "Chest Drop Usage:");
                    player.sendMessage(ChatColor.AQUA + "/chestdrop (drop code) (x) (z): Drop a chest containing a reward item.");
                }
            } else {
                player.sendMessage(prefix + " " + ChatColor.RED + "You don't have permission to run this command!");
            }
        } else {
            System.out.println("Only players can run this command!");
        }
        return false;
    }
}