package com.bownet.drops;

import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin implements Listener {

    YamlConfiguration modifycodes;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("chestdrop").setExecutor(new ChestDropCMD(this));
        getCommand("claimdrop").setExecutor(new ClaimDropCMD(this));
        getCommand("forceclaim").setExecutor(new ForceClaimCMD(this));
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        File codes = new File(getDataFolder(), "codes.yml");
        if (!codes.exists()) {
            try {
                codes.createNewFile();
            } catch (IOException e) {
                System.out.println("BowDrops: Failed to load the codes file!");
                throw new RuntimeException(e);
            }
        }
        YamlConfiguration modifycodes = YamlConfiguration.loadConfiguration(codes);
        System.out.println("BowDrops has been loaded.");
    }

    @Override
    public void onDisable() {
        System.out.println("BowDrops has been unloaded.");
    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        try {
            if (e.getClickedBlock().getType() == Material.CHEST) {
                Container chest = (Container) e.getClickedBlock().getState();
                ItemStack identifier = chest.getInventory().getItem(0);
                ItemStack identifierCheck = new ItemStack(Material.ANDESITE, 1);
                ItemMeta metaIdentifier = identifierCheck.getItemMeta();
                metaIdentifier.setDisplayName(ChatColor.DARK_AQUA + "BowDrops Identifier");
                identifierCheck.setItemMeta(metaIdentifier);
                if (identifier.isSimilar(identifierCheck)) {
                    String startprefix = getConfig().getString("Prefix");
                    String prefix = ChatColor.translateAlternateColorCodes('&', startprefix);
                    e.setCancelled(true);
                    ItemStack itemToGive = chest.getInventory().getItem(13);
                    e.getPlayer().getInventory().addItem(itemToGive);
                    chest.getInventory().clear();
                    e.getClickedBlock().setType(Material.AIR);
                    for (Entity entity : chest.getLocation().getChunk().getEntities()) {
                        if (entity.getLocation().distance(chest.getLocation()) <= 1.5) {
                            if (entity.getType() == EntityType.ARMOR_STAND) {
                                entity.remove();
                            }
                        }
                    }
                    e.getPlayer().sendMessage(prefix + " " + ChatColor.GREEN + "You successfully claimed the drop!");
                }
            }
        } catch (NullPointerException exception) {}
    }

    public ItemStack CodeToReward(String code) {
        if (modifycodes.contains(code)) {
            String reward = modifycodes.getString(code);
            if (reward.contains("ORAXEN:")) {
                String nameOxnItem = reward.replace("ORAXEN:", "");
                String nameOxnItem2 = nameOxnItem.toLowerCase();
                ItemBuilder OxnUnBuiltItem = OraxenItems.getItemById(nameOxnItem2);
                if (OxnUnBuiltItem != null) {
                    ItemStack OxnBuiltItem = OxnUnBuiltItem.build();
                    return OxnBuiltItem;
                } else {
                    return null;
                }
            } else if (reward.contains("MINECRAFT:")) {
                String nameMCItem = reward.replace("MINECRAFT:", "");
                Material MCItemName = Material.getMaterial(nameMCItem);
                if (MCItemName != null) {
                    ItemStack MCItem = new ItemStack(MCItemName, 1);
                    return MCItem;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}