package com.syntaxphoenix.spigot.smoothtimber.version.manager;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.syntaxphoenix.syntaxapi.random.NumberGeneratorType;
import com.syntaxphoenix.syntaxapi.random.RandomNumberGenerator;

public interface VersionChanger {
	
	final RandomNumberGenerator random = NumberGeneratorType.MURMUR.create();
	
	public boolean hasCuttingItemInHand(Player player);
	
	public ItemStack removeDurabilityFromItem(ItemStack stack);
	
	public void setItemInPlayerHand(Player player, ItemStack stack);
	
	public boolean isWoodBlock(Block block);

	public void setupConfig();

	public boolean hasPermissionForWood(Player p, Block b);

	public ItemStack getItemInHand(Player p);

	public ItemStack getAirItem();
	
	public Entity toFallingBlock(Block block);

	public EntityType getFallingBlockType();

	public void dropItemByFallingBlock(FallingBlock block);

}
