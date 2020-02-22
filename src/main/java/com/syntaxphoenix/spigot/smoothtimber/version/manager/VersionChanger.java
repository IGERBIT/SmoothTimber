package com.syntaxphoenix.spigot.smoothtimber.version.manager;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface VersionChanger {
	
	public boolean hasCuttingItemInHand(Player player);
	
	public ItemStack removeDurabilityFromItem(ItemStack stack);
	
	public void setItemInPlayerHand(Player player, ItemStack stack);
	
	public boolean isWoodBlock(Block block);

	public void setupConfig();

	public boolean hasPermissionForWood(Player p, Block b);

	public ItemStack getItemInHand(Player p);

	public ItemStack getAirItem();

}
