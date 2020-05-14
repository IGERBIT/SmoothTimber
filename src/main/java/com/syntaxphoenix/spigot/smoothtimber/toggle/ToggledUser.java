package com.syntaxphoenix.spigot.smoothtimber.toggle;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.syntaxphoenix.spigot.smoothtimber.config.Message;

public class ToggledUser {

	private final UUID uniqueId;
	private int time;

	/*
	 * 
	 */

	public ToggledUser(UUID uniqueId) {
		this.uniqueId = uniqueId;
		this.time = -1;
	}

	public ToggledUser(UUID uniqueId, int time) {
		this.uniqueId = uniqueId;
		this.time = time;
	}

	/*
	 * 
	 */

	public final UUID getUniqueId() {
		return uniqueId;
	}

	public int getRemainingTime() {
		return time;
	}

	/*
	 * 
	 */

	protected boolean update() {
		if (time == -1)
			return false;
		if ((time -= 1) == 0)
			return true;
		return false;
	}

	/*
	 * 
	 */

	protected void onToggle(boolean status) {
		Player player = Bukkit.getPlayer(uniqueId);
		if (player == null)
			return;

		if (status)
			player.sendMessage(Message.GLOBAL_PREFIX.colored() + ' ' + (time == -1
					? Message.TOGGLE_ON_FOREVER.colored(new String[] { "%tool%", Message.TOOLS_WOODCHOPPER.message() })
					: Message.TOGGLE_ON_TIMED.colored(new String[][] { { "%tool%", Message.TOOLS_WOODCHOPPER.message() },
							{ "%time%", time + " " + (time == 1 ? Message.TIME_SECOND.message()
									: Message.TIME_SECONDS.message()) } })));
		else
			player.sendMessage(Message.GLOBAL_PREFIX.colored() + ' '
					+ Message.TOGGLE_OFF.colored(new String[] { "%tool%", Message.TOOLS_WOODCHOPPER.message() }));

	}

}
