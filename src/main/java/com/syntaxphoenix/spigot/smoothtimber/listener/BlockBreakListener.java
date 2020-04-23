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

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled()) {
			return;
		}

		if (CutterConfig.ENABLE_WORLD) {
			boolean contains = CutterConfig.WORLD_LIST.contains(e.getBlock().getWorld().getName());
			if (CutterConfig.ENABLE_WORLD_BLACKLIST ? contains : !contains)
				return;
		}

		Player p = e.getPlayer();
		if (CutterConfig.ON_SNEAK) {
			if (!p.isSneaking()) {
				return;
			}
		}
		VersionChanger change = PluginUtils.CHANGER;
		if (change.isWoodBlock(e.getBlock())) {
			if (change.hasCuttingItemInHand(p)) {
				ItemStack tool = change.getItemInHand(p);
				Location l = e.getBlock().getLocation();
				if (Locator.isPlayerPlaced(l)) {
					return;
				}
				e.setCancelled(true);
				Bukkit.getScheduler().runTaskAsynchronously(PluginUtils.MAIN, new Runnable() {
					@Override
					public void run() {
						int maxItems = CutterConfig.ENABLE_LUCK ? change.getMaxDropCount(tool) : 1;
						ArrayList<Location> woodBlocks = new ArrayList<>();
						Location bl = l;
						int prev = woodBlocks.size();
						for (int y = 0; y < 256; y++) {
							Locator.locateWood(
									new Location(bl.getWorld(), bl.getBlockX(), bl.getBlockY() + y, bl.getBlockZ()),
									woodBlocks);
							int size = woodBlocks.size();
							if (size == prev) {
								break;
							}
							prev = size;
						}
						if (SmoothTimber.triggerChopEvent(p, l, change, tool, woodBlocks)) {
							return;
						}
						Bukkit.getScheduler().runTask(PluginUtils.MAIN, new Runnable() {
							@Override
							public void run() {
								for (int v = 0; v < woodBlocks.size(); v++) {
									Block b = woodBlocks.get(v).getBlock();
									if (change.hasPermissionForWood(p, b)) {
										if (change.removeDurabilityFromItem(tool) == null) {
											break;
										}
										change.toFallingBlock(b).setMetadata("STAnimate", new FixedMetadataValue(
												SmoothTimber.m, maxItems == 1 ? maxItems : generateAmount(maxItems)));
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
