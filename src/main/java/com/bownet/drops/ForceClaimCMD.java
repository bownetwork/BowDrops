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
                    Player claimingPlayer = Bukkit.getServer().getOfflinePlayer(args[0]).getPlayer();
                    claimingPlayer.sendMessage(prefix + " " + ChatColor.GREEN + "An admin forced you to claim a drop code.");
                    claimingPlayer.performCommand("claimdrop " + args[1]);
                } else {
                    player.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "Force Claim Help");
                    player.sendMessage(ChatColor.AQUA + "/forceclaim (player) (code): Force a player to claim a drop with the code provided.");
                }
            }
        }
        return false;
    }
}
