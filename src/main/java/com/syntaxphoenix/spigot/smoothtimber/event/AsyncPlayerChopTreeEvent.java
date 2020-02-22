/**
 * 
 * @author StevenLPHD
 * 
 */
package com.syntaxphoenix.spigot.smoothtimber.event;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.syntaxphoenix.spigot.smoothtimber.version.manager.VersionChanger;

public class AsyncPlayerChopTreeEvent extends Event implements Cancellable {
	
    private static final HandlerList handlers = new HandlerList();
	
	private final Player player;
	private final Location treeLocation;
	private final VersionChanger versionChange;
	private final ItemStack toolStack;
	private final ArrayList<Location> blocksToChop;

	public AsyncPlayerChopTreeEvent(Player player, Location treeLocation, VersionChanger version, ItemStack tool, ArrayList<Location> blocks) {
		super(true);
		this.player = player;
		this.treeLocation = treeLocation;
		this.versionChange = version;
		this.toolStack = tool;
		this.blocksToChop = blocks;
	}
	
    private boolean cancelled = false;
    
    public boolean isCancelled() {
    	return cancelled;
    }
    
    public void setCancelled(boolean cancel) {
    	this.cancelled = cancel;
    }
	
	public final Player getPlayer() {
		return player;
	}
	
	public final Location getTreeLocation() {
		return treeLocation;
	}
	
	public final VersionChanger getVersionUtil() {
		return versionChange;
	}

	public final ItemStack getToolStack() {
		return toolStack;
	}

	public ArrayList<Location> getBlockLocations() {
		return blocksToChop;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
