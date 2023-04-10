package com.bownet.drops;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                    if (!(main.isClaimed(player, codeInUse))) {
                        ItemStack rewardItem = main.CodeToReward(codeInUse);
                        if (!(rewardItem == null)) {
                            if (!main.invFull(player)) {
                                player.getInventory().addItem(rewardItem);
                                main.addToClaimed(player, codeInUse);
                                player.sendMessage(prefix + " " + ChatColor.GREEN + "Item has been delivered to your inventory successfully.");
                            } else {
                                player.sendMessage(prefix + " " + ChatColor.RED + "The drop couldn't be claimed, as your inventory is full.");
                            }
                        } else {
                            player.sendMessage(prefix + " " + ChatColor.RED + "The reward couldn't be found.");
                            System.out.println("BowDrops: The drop item couldn't be found.");
                        }
                    } else {
                        player.sendMessage(prefix + " " + ChatColor.RED + "You have already redeemed this code.");
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
