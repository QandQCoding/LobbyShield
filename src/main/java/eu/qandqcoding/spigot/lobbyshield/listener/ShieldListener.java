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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ShieldListener implements Listener {


    public static ArrayList<Player> schutzschild = new ArrayList<>();

    public Player player;
    private Config messagesConfig;
    private Config config;


    public ShieldListener() {
        this.messagesConfig = LobbyShield.getInstance().getConfigManager().getMessagesConfig();
        this.config = LobbyShield.getInstance().getConfigManager().getConfig();
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() == null) return;
            if (event.getItem().getItemMeta() == null) return;
            if (event.getItem().getItemMeta().getDisplayName() == null) return;
            if (event.getItem().getItemMeta().getDisplayName().equals(this.config.getString("item.name"))) {
                SchildInventory(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        SchutzSchildManager manager = new SchutzSchildManager(player, JavaPlugin.getPlugin(LobbyShield.class));
        if (player.getOpenInventory().getTitle().equals(this.config.getString("item.inventory_name"))) {
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(this.config.getString("item.inventory_deactivate_item_name"))) {
                if (CommandUtils.hasPermission(player, "messages.commands.shield.permission")) {
                    if (schutzschild().contains(player)) {
                        schutzschild.remove(player);
                        manager.stop();
                        player.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.deactivated", player));
                        player.closeInventory();
                    }
                }
                player.closeInventory();
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(this.config.getString("item.inventory_activate_item_name"))) {
                if (CommandUtils.hasPermission(player, "messages.commands.shield.permission")) {
                    if (!schutzschild.contains(player)) {
                        schutzschild.add(player);
                        manager.start();
                        player.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.activated", player));
                        player.closeInventory();
                    }
                }
            }
            event.setCancelled(true);
            if (event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1f, 1f);
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SchutzSchildManager manager = new SchutzSchildManager(player, JavaPlugin.getPlugin(LobbyShield.class));
        if (CommandUtils.hasPermission(player, "messages.commands.shield.permission")) {
            if( this.config.getBoolean("item.on_join.enabled")) {
                player.getInventory().setItem(this.config.getInt("item.on_join.slot"), new ItemAPI(this.config.getString("item.name"), Material.getMaterial(this.config.getString("item.material")), 1, this.config.getStringList("item.lore")).build());
            }
            if (this.config.getBoolean("item.on_join.auto_activate")) {
                if (!schutzschild.contains(player)) {
                    schutzschild.add(player);
                    manager.start();
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        SchutzSchildManager manager = new SchutzSchildManager(player, JavaPlugin.getPlugin(LobbyShield.class));
        Player player = e.getPlayer();
        schutzschild.remove(player);
        manager.stop();
    }


    public void SchildInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, this.config.getString("item.inventory_name"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemAPI("§7", Material.BLACK_STAINED_GLASS_PANE, 1).addHideFlag().build());
        }
        if (schutzschild.contains(player)) {
            inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).setName(this.config.getString("item.inventory_deactivate_item_name")).setSkull(this.config.getString("item.inventory_deactivate_item_skull")).toItemStack());

        } else {
            inventory.setItem(13, new ItemBuilder(Material.PLAYER_HEAD).setName(this.config.getString("item.inventory_activate_item_name")).setSkull(this.config.getString("item.inventory_activate_item_skull")).toItemStack());
            if (this.config.getBoolean("item.sound.enabled")) {
                player.playSound(player.getLocation(), Sound.valueOf(this.config.getString("item.sound.sound")), this.config.getInt("item.sound.volume"), this.config.getInt("item.sound.pitch"));
            }
        }
        player.openInventory(inventory);
    }

    public ArrayList<Player> schutzschild() {
        return schutzschild;
    }

    public class SchutzSchildManager {
        private int taskId;
        private final Player player;
        private final LobbyShield plugin;
        private int multiply;
        private int hight;
        private int near;
        private Config config;


        public SchutzSchildManager(Player player, LobbyShield plugin) {

            this.config = LobbyShield.getInstance().getConfigManager().getConfig();
            this.player = player;
            this.plugin = plugin;
            this.multiply = this.config.getInt("shield.knockback-rate");
            this.hight = this.config.getInt("shield.hight");
            this.near = plugin.getConfig().getInt("shield.distance");
        }

        public void start() {
            Player player = this.player;
            if (!player.isOnline()) {
                return;
            }

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (ShieldListener.schutzschild.contains(player)) {
                        player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 1);

                        for (Entity entity : player.getNearbyEntities(near, near, near)) {
                            if (entity instanceof Player && entity != player && ShieldListener.schutzschild.contains(player)) {
                                Player players = (Player) entity;
                                if (player.getLocation().distanceSquared(players.getLocation()) <= near * near) {
                                    for (int i = 0; i < 10; i++) {
                                        players.setVelocity(players.getLocation().getDirection().multiply(-multiply).setY(hight));
                                    }
                                }
                            }
                        }
                    } else {
                        stop();
                    }
                }
            }, 0L, 5L);
        }

        public void stop() {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    public class EffectPlayer {
        private boolean stop;

        public void playEffect(Location loc, boolean vis) {
            int i = 0;
            while (!stop) {
                for (i = 0; i <= 8; i += (!vis && i == 3) ? 2 : 1)
                    loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, i);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
            }
        }

        public void stop() {
            stop = true;
        }
    }




}