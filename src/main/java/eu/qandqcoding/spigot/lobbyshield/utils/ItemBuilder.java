package eu.qandqcoding.spigot.lobbyshield.utils;

import eu.qandqcoding.spigot.lobbyshield.LobbyShield;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public ItemBuilder(Material material, int amount, short durability) {
        if (amount < 1) {
            amount = 1;
        }
        this.item = new ItemStack(material, amount, durability);
    }

    public ItemBuilder(Material material, int amount, byte durability) {
        if (amount < 1) {
            amount = 1;
        }
        this.item = new ItemStack(material, amount, durability);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.item = itemStack;
    }

    public ItemBuilder setDurability(byte durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        this.item.setDurability(this.item.getDurability());
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        for (String l : lore) {
            this.setLore(ChatColor.translateAlternateColorCodes('&', l));
        }
        return this;
    }

    public ItemBuilder setPotionEffect(PotionEffectType effect) {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.addCustomEffect(new PotionEffect(effect, 20, 2, true), true);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> colored = new ArrayList<String>();
        lore.forEach(line -> colored.add(ChatColor.translateAlternateColorCodes('&', line)));
        itemMeta.setLore(colored);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        if (!lore.contains(line))
            return this;
        lore.remove(line);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        if (index < 0 || index > lore.size())
            return this;
        lore.remove(index);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (itemMeta.hasLore())
            lore = new ArrayList<>(itemMeta.getLore());
        lore.add(line);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.set(pos, line);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        this.item.addEnchantment(ench, level);
        this.item.getItemMeta().addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        this.item.addUnsafeEnchantment(ench, level);
        this.item.getItemMeta().addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemBuilder addEnchantItemMeta(Enchantment ench, int level, boolean hideEnch) {
        this.item.getItemMeta().addEnchant(ench, level, hideEnch);
        this.item.getItemMeta().addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> ench) {
        this.item.getItemMeta().addItemFlags(ItemFlag.values());
        this.item.addEnchantments(ench);
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment ench) {
        this.item.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder removeEnchantItemMeta(Enchantment ench) {
        this.item.getItemMeta().removeEnchant(ench);
        return this;
    }

    public ItemBuilder getEnchantsItemMeta() {
        this.item.getItemMeta().getEnchants();
        return this;
    }

    public ItemBuilder getEnchants() {
        this.item.getEnchantments();
        return this;
    }

    public ItemBuilder hasEnchant(Enchantment ench) {
        this.item.getItemMeta().hasEnchant(ench);
        return this;
    }

    public ItemBuilder hasEnchants() {
        this.item.getItemMeta().hasEnchants();
        return this;
    }

    public ItemBuilder getEnchantmentLevel(Enchantment ench) {
        this.item.getEnchantmentLevel(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta skullMeta = (SkullMeta) this.item.getItemMeta();
            skullMeta.setOwner(owner);
            this.item.setItemMeta(skullMeta);
        } catch (ClassCastException expected) {
        }
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

    @SuppressWarnings("deprecation")
    public ItemBuilder setDyeColor(DyeColor color) {
        this.item.setDurability(color.getDyeData());
        return this;
    }

    public ItemBuilder setArmorColor(Color color) {
        try {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.item.getItemMeta();
            leatherArmorMeta.setColor(color);
            this.item.setItemMeta(leatherArmorMeta);
        } catch (ClassCastException ignored) {
        }
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }
}
