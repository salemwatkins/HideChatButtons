package com.HideChatButtons;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.widgets.Widget;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PluginDescriptor(
		name = "Hide Chat Buttons",
		description = "Removes the chat filter boxes",
		tags = {"chat", "filter", "remove", "buttons"}
)
public class HideChatButtons extends Plugin
{
	@Inject
	private Client client;

	private static final int GROUP_ID = 162;
	private static final int[] BUTTON_IDS = {4,5,6,7,11,13,14,15,20,21,22,23,26,27,28,29,30,31};

	@Override
	protected void startUp() {
		log.debug("Plugin started!");
		updateChatFilterText();
	}

	@Override
	protected void shutDown() {
		log.debug("Plugin stopped!");
		// Clean-up if needed (e.g., reset states)
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOGGED_IN) {
			updateChatFilterText();
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event) {
		if (event.getGroupId() == GROUP_ID) {
			updateChatFilterText();
		}
	}

	private void updateChatFilterText() {
		for (int id : BUTTON_IDS) {
			setWidgetVisibility(id, false);
		}
	}

	private void setWidgetVisibility(int childId, boolean visible) {
		Widget widget = client.getWidget(GROUP_ID, childId);
		if (widget != null) {
			widget.setHidden(!visible);
			if (visible) {
				widget.setText("");
			}
			log.debug("Widget {} visibility set to {}", childId, visible ? "visible" : "hidden");
		} else {
			log.warn("Widget {} not found", childId);
		}
	}
}
