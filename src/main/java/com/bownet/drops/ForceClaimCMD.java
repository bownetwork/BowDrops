package com.bownet.drops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ForceClaimCMD implements CommandExecutor {
    private Main main;
    public ForceClaimCMD(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String startprefix = main.getConfig().getString("Prefix");
            String prefix = ChatColor.translateAlternateColorCodes('&', startprefix);
            if (player.hasPermission("bowdrops.admin")) {
                if (args.length == 2) {
                    Player claimingPlayer = Bukkit.getServer().getPlayer(args[0]);
                    if (claimingPlayer != null) {
                        claimingPlayer.sendMessage(prefix + " " + ChatColor.GREEN + "An admin sent you a drop!");
                        claimingPlayer.sendMessage(prefix + " " + ChatColor.GOLD + "This will not count towards your redemptions of this code.");
                        String codeInUse = args[1];
                        ItemStack rewardItem = main.CodeToReward(codeInUse);
                        if (!(rewardItem == null)) {
                            if (!main.invFull(claimingPlayer)) {
                                claimingPlayer.getInventory().addItem(rewardItem);
                                claimingPlayer.sendMessage(prefix + " " + ChatColor.GREEN + "Item has been delivered to your inventory successfully.");
                                player.sendMessage(prefix + " " + ChatColor.GREEN + "Item delivered successfully.");
                            } else {
                                claimingPlayer.sendMessage(prefix + " " + ChatColor.RED + "The drop couldn't be claimed, as your inventory is full.");
                                player.sendMessage(prefix + " " + ChatColor.RED + "That player's inventory is full!");
                            }
                        } else {
                            player.sendMessage(prefix + " " + ChatColor.RED + "The reward couldn't be found.");
                            claimingPlayer.sendMessage(prefix + " " + ChatColor.RED + "The reward couldn't be found.");
                            System.out.println("BowDrops: The drop item couldn't be found.");
                        }
                    } else {
                        player.sendMessage(prefix + " " + ChatColor.RED + "This player isn't online.");
                    }
                } else {
                    player.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "Force Claim Help");
                    player.sendMessage(ChatColor.AQUA + "/forceclaim (player) (code): Force a player to claim a drop with the code provided.");
                }
            } else {
                player.sendMessage(prefix + " " + ChatColor.RED + "You don't have permission to run this command!");
            }
        }
        return false;
    }
}
