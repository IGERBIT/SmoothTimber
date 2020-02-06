package com.syntaxphoenix.smoothtimber.version.changer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.syntaxphoenix.smoothtimber.config.CutterConfig;
import com.syntaxphoenix.smoothtimber.utilities.Lists;
import com.syntaxphoenix.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.smoothtimber.version.manager.VersionExchanger;
import com.syntaxphoenix.smoothtimber.version.manager.WoodType;

public class v1_13xChanger implements VersionChanger {

	@Override
	public boolean hasCuttingItemInHand(Player player) {
		return CutterConfig.cutterMaterials.contains(getItemInHand(player).getType().name());
	}

	@Override
	public ItemStack removeDurabilityFromItem(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		if(meta instanceof Damageable) {
			Damageable dmg = (Damageable) meta;
			int damage = dmg.getDamage() + 1;
			if(stack.getType().getMaxDurability() - damage < 0) {
				stack.setAmount(0);
				return null;
			}
			dmg.setDamage(damage = dmg.getDamage() + 1);
		}
		stack.setItemMeta(meta);
		return stack;
	}

	@Override
	public void setItemInPlayerHand(Player player, ItemStack stack) {
		player.getEquipment().setItemInMainHand(stack);
	}

	@Override
	public boolean isWoodBlock(Block block) {
		return (block.getBlockData().getMaterial().name().endsWith("_LOG") || block.getBlockData().getMaterial().name().endsWith("_WOOD") || block.getBlockData().getMaterial().name().endsWith("_FENCE"));
	}

	@Override
	public void setupConfig() {
		CutterConfig.cutterMaterials.addAll(Lists.asList("WOODEN_AXE", "STONE_AXE",
				"IRON_AXE", "GOLDEN_AXE", "DIAMOND_AXE"));
	}

	@Override
	public boolean hasPermissionForWood(Player p, Block b) {
		if(!CutterConfig.permissionsEnabled) {
			return true;
		}
		String mat = b.getBlockData().getMaterial().name();
		WoodType type = WoodType.OAK;
		if(mat.startsWith("BIRCH_")) {
			type = WoodType.BIRCH;
		} else if(mat.startsWith("JUNGLE_")) {
			type = WoodType.JUNGLE;
		} else if(mat.startsWith("SPRUCE_")) {
			type = WoodType.SPRUCE;
		} else if(mat.startsWith("DARK_OAK_")) {
			type = WoodType.DARKOAK;
		} else if(mat.startsWith("ACACIA_")) {
			type = WoodType.ACACIA;
		}
		return VersionExchanger.checkPermission(type, p);
	}

	@Override
	public ItemStack getItemInHand(Player p) {
		return p.getEquipment().getItemInMainHand();
	}

	@Override
	public ItemStack getAirItem() {
		return new ItemStack(Material.AIR);
	}

}
