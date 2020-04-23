package com.syntaxphoenix.spigot.smoothtimber.version.changer;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.syntaxphoenix.spigot.smoothtimber.config.CutterConfig;
import com.syntaxphoenix.spigot.smoothtimber.utilities.Lists;
import com.syntaxphoenix.spigot.smoothtimber.utilities.Storage;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionExchanger;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.WoodType;

@SuppressWarnings("deprecation")
public class v1_8xChanger implements VersionChanger {

	@Override
	public boolean hasCuttingItemInHand(Player player) {
		return CutterConfig.CUTTER_MATERIALS.contains(getItemInHand(player).getType().name());
	}

	@Override
	public ItemStack removeDurabilityFromItem(ItemStack stack) {
		if (CutterConfig.ENABLE_UNBREAKING) {
			int level = stack.getEnchantmentLevel(Enchantment.DURABILITY);
			float chance = 100 / (level <= 0 ? 1 : (level + 1));
			if (random.nextFloat(0, 100) > chance) {
				return stack;
			}
		}
		Integer durability = stack.getDurability() + 1;
		if (stack.getType().getMaxDurability() < durability) {
			stack.setAmount(0);
			return null;
		}
		stack.setDurability(durability.shortValue());
		return stack;
	}

	@Override
	public int getMaxDropCount(ItemStack stack) {
		int level = stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
		return level <= 0 ? 1 : level + 1;
	}

	@Override
	public void setItemInPlayerHand(Player player, ItemStack stack) {
		player.setItemInHand(stack);
	}

	@Override
	public boolean isWoodBlock(Block block) {
		String type = block.getType().name();
		return (type.replace("_2", "").equals("LOG") || type.equals("FENCE"));
	}

	@Override
	public void setupConfig() {
		CutterConfig.CUTTER_MATERIALS
				.addAll(Lists.asList("WOOD_AXE", "STONE_AXE", "IRON_AXE", "GOLD_AXE", "DIAMOND_AXE"));
	}

	@Override
	public boolean hasPermissionForWood(Player p, Block b) {
		if (!CutterConfig.ENABLE_PERMISSIONS) {
			return true;
		}
		WoodType type = WoodType.OAK;
		Material mat = b.getType();
		int id = b.getData();
		if (mat == Material.valueOf("LOG")) {
			if (id == 1 || id == 5 || id == 9 || id == 13) {
				type = WoodType.SPRUCE;
			} else if (id == 2 || id == 6 || id == 10 || id == 14) {
				type = WoodType.BIRCH;
			} else if (id == 3 || id == 7 || id == 11 || id == 15) {
				type = WoodType.JUNGLE;
			}
		} else if (mat == Material.valueOf("LOG_2")) {
			if (id == 1 || id == 3 || id == 5 || id == 7 || id == 9 || id == 11 || id == 13 || id == 15) {
				type = WoodType.DARKOAK;
			} else if (id == 0 || id == 2 || id == 4 || id == 6 || id == 8 || id == 10 || id == 12 || id == 14) {
				type = WoodType.ACACIA;
			}
		}
		return VersionExchanger.checkPermission(type, p);
	}

	@Override
	public ItemStack getItemInHand(Player p) {
		return p.getItemInHand();
	}

	@Override
	public ItemStack getAirItem() {
		return new ItemStack(Material.AIR);
	}

	@Override
	public Entity toFallingBlock(Block block) {
		Material type = block.getType();
		byte data = block.getData();
		block.setType(Material.AIR);
		return block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0.2, 0.5), type, data);
	}

	@Override
	public EntityType getFallingBlockType() {
		return EntityType.FALLING_BLOCK;
	}

	@Override
	public void dropItemByFallingBlock(FallingBlock block, int amount) {
		block.getWorld().dropItem(block.getLocation(),
				getItemStack((Material) Storage.MATERIAL.run("type", Storage.FALLING_BLOCK.run(block, "id")),
						(byte) Storage.FALLING_BLOCK.run(block, "data"), amount));
	}

	public ItemStack getItemStack(Material type, byte id, int amount) {
		if (type == Material.valueOf("LOG")) {
			if (id == 1 || id == 5 || id == 9 || id == 13) {
				return new MaterialData(type, (byte) 1).toItemStack(amount);
			} else if (id == 2 || id == 6 || id == 10 || id == 14) {
				return new MaterialData(type, (byte) 2).toItemStack(amount);
			} else if (id == 3 || id == 7 || id == 11 || id == 15) {
				return new MaterialData(type, (byte) 3).toItemStack(amount);
			} else {
				return new MaterialData(type, (byte) 0).toItemStack(amount);
			}
		} else if (type == Material.valueOf("LOG_2")) {
			if (id == 1 || id == 3 || id == 5 || id == 7) {
				return new MaterialData(type, (byte) 1).toItemStack(amount);
			} else if (id == 0 || id == 2 || id == 4 || id == 6) {
				return new MaterialData(type, (byte) 0).toItemStack(amount);
			}
		}
		return new ItemStack(type, amount);
	}

}
