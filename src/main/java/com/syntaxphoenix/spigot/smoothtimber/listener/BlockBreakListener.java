package com.syntaxphoenix.spigot.smoothtimber.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.syntaxphoenix.spigot.smoothtimber.SmoothTimber;
import com.syntaxphoenix.spigot.smoothtimber.config.CutterConfig;
import com.syntaxphoenix.spigot.smoothtimber.utilities.Locator;
import com.syntaxphoenix.spigot.smoothtimber.utilities.PluginUtils;
import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;
import com.syntaxphoenix.syntaxapi.random.NumberGeneratorType;
import com.syntaxphoenix.syntaxapi.random.RandomNumberGenerator;

public class BlockBreakListener implements Listener {

	private final RandomNumberGenerator generator = NumberGeneratorType.MURMUR.create(System.currentTimeMillis() >> 3);

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}

		if (CutterConfig.ENABLE_WORLD) {
			boolean contains = CutterConfig.WORLD_LIST.contains(event.getBlock().getWorld().getName());
			if (CutterConfig.ENABLE_WORLD_BLACKLIST ? contains : !contains)
				return;
		}

		Player player = event.getPlayer();

		if (CutterConfig.ON_SNEAK)
			if (!player.isSneaking())
				return;

		if (CutterConfig.TOGGLEABLE)
			if (!SmoothTimber.STORAGE.hasToggled(player.getUniqueId()))
				return;

		VersionChanger change = PluginUtils.CHANGER;
		if (change.isWoodBlock(event.getBlock())) {
			if (change.hasCuttingItemInHand(player)) {
				ItemStack tool = change.getItemInHand(player);
				Location location = event.getBlock().getLocation();
				if (Locator.isPlayerPlaced(location)) {
					return;
				}
				event.setCancelled(true);
				Bukkit.getScheduler().runTaskAsynchronously(PluginUtils.MAIN, new Runnable() {
					@Override
					public void run() {
						int maxItems = CutterConfig.ENABLE_LUCK ? change.getMaxDropCount(tool) : 1;
						ArrayList<Location> woodBlocks = new ArrayList<>();
						Location blockLocation = location;
						int previous = woodBlocks.size();
						for (int y = 0; y < 256; y++) {
							Locator.locateWood(new Location(blockLocation.getWorld(), blockLocation.getBlockX(),
									blockLocation.getBlockY() + y, blockLocation.getBlockZ()), woodBlocks);
							int size = woodBlocks.size();
							if (size == previous) {
								break;
							}
							previous = size;
						}
						if (SmoothTimber.triggerChopEvent(player, location, change, tool, woodBlocks)) {
							return;
						}
						Bukkit.getScheduler().runTask(PluginUtils.MAIN, new Runnable() {
							@Override
							public void run() {
								for (int index = 0; index < woodBlocks.size(); index++) {
									Block block = woodBlocks.get(index).getBlock();
									if (change.hasPermissionForWood(player, block)) {
										if (change.removeDurabilityFromItem(tool) == null) {
											break;
										}
										change.toFallingBlock(block).setMetadata("STAnimate",
												new FixedMetadataValue(SmoothTimber.MAIN,
														maxItems == 1 ? maxItems : generateAmount(maxItems)));
										;
									}
								}
							}

							private int generateAmount(int max) {
								int drop = 1;
								float more = 1f / (max + 1);
								float previous = more * 2f;
								float next = more * 3f;
								float chance = generator.nextFloat() * (float) CutterConfig.LUCK_MULTIPLIER;
								while (true) {
									if (previous < chance && chance > next) {
										drop++;
										previous = next;
										next += more;
									} else if (previous < chance && chance < next) {
										drop++;
										break;
									} else {
										break;
									}
								}
								if (drop > 64)
									return 64;
								return drop;
							}

						});
					}
				});
			}
		}
	}
}
