package com.bownet.drops;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                    if (Bukkit.getServer().getPlayer(args[0]).isOnline()) {
                        Player claimingPlayer = Bukkit.getServer().getPlayer(args[0]);
                        claimingPlayer.sendMessage(prefix + " " + ChatColor.GREEN + "An admin forced you to claim a drop code.");
                        claimingPlayer.performCommand("claimdrop " + args[1]);
                    } else {
                        player.sendMessage(prefix + " " + ChatColor.RED + "This player isn't online.");
                    }
                } else {
                    player.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "Force Claim Help");
                    player.sendMessage(ChatColor.AQUA + "/forceclaim (player) (code): Force a player to claim a drop with the code provided.");
                }
            }
        }
        return false;
    }
}
