package com.bownet.drops;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class ClaimDropCMD implements CommandExecutor {

    private Main main;
    public ClaimDropCMD(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String startprefix = main.getConfig().getString("Prefix");
            String prefix = ChatColor.translateAlternateColorCodes('&', startprefix);
            if (player.hasPermission("bowdrops.claimdrop")) {
                if (args.length == 1) {
                    String codeInUse = args[0];
                    List<String> codes = main.getConfig().getStringList("Codes");
                    List<String> rewards = main.getConfig().getStringList("Rewards");
                    if (codes.contains(codeInUse)) {
                        int codeIndex = codes.indexOf(codeInUse);
                        String rewardItem = rewards.get(codeIndex);
                        if (rewardItem.contains("ORAXEN:")) {
                            String nameOxnItem = rewardItem.replace("ORAXEN:", "");
                            String nameOxnItem2 = nameOxnItem.toLowerCase();
                            ItemBuilder OxnUnBuiltItem = OraxenItems.getItemById(nameOxnItem2);
                            if (OxnUnBuiltItem == null) {
                                player.sendMessage(prefix + " " + ChatColor.RED + "There was an error, as the item couldn't be found.");
                                System.out.println("BowDrops: The drop item couldn't be found!");
                            } else {
                                ItemStack OxnBuiltItem = OxnUnBuiltItem.build();
                                player.getInventory().addItem(OxnBuiltItem);
                                player.sendMessage(prefix + " " + ChatColor.GREEN + "Item has been delivered to your inventory successfully.");
                            }
                        } else if (rewardItem.contains("MINECRAFT:")) {
                            String nameMCItem = rewardItem.replace("MINECRAFT:", "");
                            Material MCItemName = Material.getMaterial(nameMCItem);
                            if (MCItemName == null) {
                                player.sendMessage(prefix + " " + ChatColor.RED + "There was an error, as the item couldn't be found.");
                                System.out.println("BowDrops: The drop item couldn't be found!");
                            } else {
                                ItemStack MCItem = new ItemStack(MCItemName, 1);
                                player.getInventory().addItem(MCItem);
                                player.sendMessage(prefix + " " + ChatColor.GREEN + "Item has been delivered to your inventory successfully.");
                            }
                        } else {
                            player.sendMessage(prefix + " " + ChatColor.RED + "There was an error, as the item couldn't be found.");
                            System.out.println("BowDrops: The drop item couldn't be found!");
                        }
                    } else {
                        player.sendMessage(prefix + " " + ChatColor.RED + "This drop code could not be found.");
                    }
                } else {
                    player.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "Claim Drop Usage:");
                    player.sendMessage(ChatColor.AQUA + "/claimdrop (drop code): Claim an item drop from a code.");
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
