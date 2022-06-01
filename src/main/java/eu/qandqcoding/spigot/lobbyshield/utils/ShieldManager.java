package eu.qandqcoding.spigot.lobbyshield.utils;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import eu.qandqcoding.spigot.lobbyshield.listener.ShieldListener;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShieldManager {
    public String team;

    public Player p;

    int counter;

    int time;

    BukkitRunnable task;

    public void ShieldManager(final Player p) {
        ShieldListener.schutzschild.add(p);
        int multiply = 2;
        double hight = 1.0D;
        int near = 5;
        this.task = new BukkitRunnable() {
            public void run() {
                if (ShieldListener.schutzschild.contains(p)) {
                    ShieldManager.this.playEffect(p.getLocation(), true);
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (players != p && p.getLocation().distance(players.getLocation()) <= 5.0D && ShieldListener.schutzschild.contains(p))
                            for (int i = 0; i < 10; i++)
                                players.setVelocity(players.getLocation().getDirection().multiply(-2).setY(1.0D));
                    }
                } else {
                    ShieldManager.this.stop();
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
