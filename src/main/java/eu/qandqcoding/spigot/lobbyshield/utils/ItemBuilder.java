package eu.qandqcoding.spigot.lobbyshield.utils;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;


public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        if (amount < 1) {
            amount = 1;
        }
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkull(String textures) {
        if (this.item.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) this.item.getItemMeta();
            try {
                Object profile = Class.forName("com.mojang.authlib.GameProfile")
                        .getConstructor(UUID.class, String.class).newInstance(UUID.randomUUID(), null);
                Object properties = profile.getClass().getMethod("getProperties").invoke(profile);
                Object property = Class.forName("com.mojang.authlib.properties.Property")
                        .getConstructor(String.class, String.class).newInstance("textures", textures);
                properties.getClass().getMethod("put", Object.class, Object.class).invoke(properties, "textures",
                        property);
                Field profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, profile);
            } catch (ReflectiveOperationException e) {
                LobbyShield.getInstance().getLogger().log(Level.SEVERE, "Could not create skull", e);
            }
            this.item.setItemMeta(meta);
            return this;
        } else {
            return this;
        }
    }

    public ItemStack toItemStack() {
        return this.item;
    }
}
