package eu.qandqcoding.spigot.lobbyshield.listener;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.config.Config;
import eu.qandqcoding.spigot.lobbyshield.utils.CommandUtils;
import eu.qandqcoding.spigot.lobbyshield.utils.ItemAPI;
import eu.qandqcoding.spigot.lobbyshield.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ShieldListener implements Listener {

    private final Set<Player> schutzschild = new HashSet<>();
    private final Config messagesConfig;
    private final Config config;

    public ShieldListener() {
        LobbyShield instance = LobbyShield.getInstance();
        this.messagesConfig = instance.getConfigManager().getMessagesConfig();
        this.config = instance.getConfigManager().getConfig();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (isValidShieldItem(event.getItem())) {
                openShieldInventory(player);
                event.setCancelled(true);
            }
        }
    }

    private boolean isValidShieldItem(ItemStack item) {
        return item != null &&
                item.hasItemMeta() &&
                item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().displayName().equals(config.getString("item.name"));
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        SchutzSchildManager manager = new SchutzSchildManager(player);

        String inventoryTitle = player.getOpenInventory().getTitle();

        if (inventoryTitle.equals(config.getString("item.inventory_name"))) {
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            String itemName = event.getCurrentItem().getItemMeta().displayName().toString();

            if (itemName.equals(config.getString("item.inventory_deactivate_item_name"))) {
                deactivateShield(player, manager);
            } else if (itemName.equals(config.getString("item.inventory_activate_item_name"))) {
                activateShield(player, manager);
            } else if (event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1f, 1f);
                player.closeInventory();
            }
            event.setCancelled(true);
        }
    }

    private void deactivateShield(Player player, SchutzSchildManager manager) {
        if (CommandUtils.hasPermission(player, "messages.commands.shield.permission") && schutzschild.contains(player)) {
            schutzschild.remove(player);
            manager.stop();
            player.sendMessage(messagesConfig.getMessage("messages.commands.shield.deactivated", player));
            player.closeInventory();
        }
    }

    private void activateShield(Player player, SchutzSchildManager manager) {
        if (CommandUtils.hasPermission(player, "messages.commands.shield.permission") && !schutzschild.contains(player)) {
            schutzschild.add(player);
            manager.start();
            player.sendMessage(messagesConfig.getMessage("messages.commands.shield.activated", player));
            player.closeInventory();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SchutzSchildManager manager = new SchutzSchildManager(player);

        if (CommandUtils.hasPermission(player, "messages.commands.shield.permission")) {
            if (config.getBoolean("item.on_join.enabled")) {
                player.getInventory().setItem(
                        config.getInt("item.on_join.slot"),
                        new ItemAPI(
                                config.getString("item.name"),
                                Material.getMaterial(config.getString("item.material")),
                                1,
                                config.getStringList("item.lore")
                        ).build()
                );
            }

            if (config.getBoolean("item.on_join.auto_activate") && !schutzschild.contains(player)) {
                schutzschild.add(player);
                manager.start();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        schutzschild.remove(player);
        new SchutzSchildManager(player).stop();
    }

    private void openShieldInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, config.getString("item.inventory_name"));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemAPI("§7", Material.BLACK_STAINED_GLASS_PANE, 1).addHideFlag().build());
        }

        if (schutzschild.contains(player)) {
            inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD)
                    .setName(config.getString("item.inventory_deactivate_item_name"))
                    .setSkull(config.getString("item.inventory_deactivate_item_skull"))
                    .toItemStack());
        } else {
            inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD)
                    .setName(config.getString("item.inventory_activate_item_name"))
                    .setSkull(config.getString("item.inventory_activate_item_skull"))
                    .toItemStack());

            if (config.getBoolean("item.sound.enabled")) {
                player.playSound(player.getLocation(), Sound.valueOf(config.getString("item.sound.sound")), config.getInt("item.sound.volume"), config.getInt("item.sound.pitch"));
            }
        }
        player.openInventory(inventory);
    }

    public class SchutzSchildManager {
        private final Player player;
        private final LobbyShield plugin = JavaPlugin.getPlugin(LobbyShield.class);
        private final int multiply;
        private final int height;
        private final int near;

        public SchutzSchildManager(Player player) {
            this.player = player;
            this.multiply = config.getInt("shield.knockback-rate");
            this.height = config.getInt("shield.height");
            this.near = config.getInt("shield.distance");
        }

        public void start() {
            if (!player.isOnline()) return;

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!schutzschild.contains(player)) {
                        cancel();
                        return;
                    }

                    player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 1);

                    player.getNearbyEntities(near, near, near).stream()
                            .filter(entity -> entity instanceof Player && entity != player && schutzschild.contains(player))
                            .map(entity -> (Player) entity)
                            .filter(p -> player.getLocation().distanceSquared(p.getLocation()) <= near * near)
                            .forEach(p -> p.setVelocity(p.getLocation().getDirection().multiply(-multiply).setY(height)));
                }
            }.runTaskTimer(plugin, 0L, 5L);
        }

        public void stop() {
            // BukkitRunnable automatically cancels itself, no need to track task IDs.
        }
    }
}
