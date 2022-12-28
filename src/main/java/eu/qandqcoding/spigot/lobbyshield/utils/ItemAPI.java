package eu.qandqcoding.spigot.lobbyshield.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemAPI {

    ItemStack itemStack;
    ItemMeta itemMeta;
    SkullMeta skullMeta;

    public ItemAPI(String displayname, Material material, byte subid, int amount) {
        itemStack = new ItemStack(material, amount, subid);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
    }

    public ItemAPI(String displayname, Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
    }

    public ItemAPI(String displayname, Material material, byte subid, int amount, Enchantment enchantment, int level) {
        itemStack = new ItemStack(material, amount, subid);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.addEnchant(enchantment, level, true);
    }

    public ItemAPI(String displayname, Material material, byte subid, int amount, Enchantment enchantment, int level, List<String> lore) {
        itemStack = new ItemStack(material, amount, subid);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.addEnchant(enchantment, level, true);
        itemMeta.setLore(lore);
    }

    public ItemAPI(String displayname, Material material, byte subid, int amount, List<String> lore) {
        itemStack = new ItemStack(material, amount, subid);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setLore(lore);
    }

    public ItemAPI(String displayname, String skullowner, int amount) {
        itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(displayname);
        skullMeta.setOwner(skullowner);
    }

    public ItemAPI(String displayname, Material material, int amount, List<String> lore) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setLore(lore);
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack buildSkull() {
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
    public ItemAPI addHideFlag() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }
}
