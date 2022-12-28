package eu.qandqcoding.spigot.lobbyshield.listener;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.config.Config;
import eu.qandqcoding.spigot.lobbyshield.utils.CommandUtils;
import eu.qandqcoding.spigot.lobbyshield.utils.ItemAPI;
import eu.qandqcoding.spigot.lobbyshield.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class ShieldListener implements Listener {


    public static ArrayList<Player> schutzschild = new ArrayList<>();

    public Player player;
    BukkitRunnable task;
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
        final Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().getTitle().equals(this.config.getString("item.inventory_name"))) {
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(this.config.getString("item.inventory_deactivate_item_name"))) {
                if (CommandUtils.hasPermission(player, "messages.commands.shield.permission")) {
                    if (schutzschild().contains(player)) {
                        schutzschild.remove(player);
                        SchutzSchildManager(player);
                        player.sendMessage(this.messagesConfig.getMessage("messages.commands.shield.deactivated", player));
                        player.closeInventory();
                    }
                }
                player.closeInventory();
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(this.config.getString("item.inventory_activate_item_name"))) {
                if (CommandUtils.hasPermission(player, "messages.commands.shield.permission")) {
                    if (!schutzschild.contains(player)) {
                        schutzschild.add(player);
                        SchutzSchildManager(player);
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
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (CommandUtils.hasPermission(player, "messages.commands.shield.permission")) {
            if( this.config.getBoolean("item.on_join.enabled")) {
                player.getInventory().setItem(this.config.getInt("item.on_join.slot"), new ItemAPI(this.config.getString("item.name"), Material.getMaterial(this.config.getString("item.material")), 1, this.config.getStringList("item.lore")).build());
            }
            if (this.config.getBoolean("item.on_join.auto_activate")) {
                if (!schutzschild.contains(player)) {
                    schutzschild.add(player);
                    SchutzSchildManager(player);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        schutzschild.remove(player);
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

    public void SchutzSchildManager(final Player p) {
        int multiply = 2;
        double hight = 1.0D;
        int near = 5;
        this.task = new BukkitRunnable() {
            public void run() {
                if (ShieldListener.schutzschild.contains(p)) {
                    ShieldListener.this.playEffect(p.getLocation(), true);
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (players != p && p.getLocation().distance(players.getLocation()) <= 5.0D && ShieldListener.schutzschild.contains(p))
                            for (int i = 0; i < 10; i++)
                                players.setVelocity(players.getLocation().getDirection().multiply(-2).setY(1.0D));
                    }
                } else {
                    ShieldListener.this.stop();
                }
            }
        };
        this.task.runTaskTimer(LobbyShield.getInstance(), 0L, 5L);
    }

    void stop() {
        this.task.cancel();
    }

    public void playEffect(Location loc, boolean vis) {
        for (int i = 0; i <= 8; i += (!vis && i == 3) ? 2 : 1)
            loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, i);
    }


}