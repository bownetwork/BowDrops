package com.bownet.drops;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCMD implements CommandExecutor {

    private Main main;

    public AdminCMD(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String startprefix = main.getConfig().getString("Prefix");
            String prefix = ChatColor.translateAlternateColorCodes('&', startprefix);
            if (player.hasPermission("bowdrops.admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        main.ReloadPlugin(player);
                    } else {
                        player.sendMessage(prefix + " " + ChatColor.RED + "Incorrect usage! Run /bowdrops for the correct usage.");
                    }
                } else {
                    player.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "BowDrops Command Help");
                    player.sendMessage(ChatColor.AQUA + "/bowdrops reload: Reloads the plugin.");
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
