package es.outlook.adriansrj.neptunetrial.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * @author AdrianSR / 30/12/2021 / 04:07 p. m.
 */
public class AppraiseGUIHolder implements InventoryHolder {
	
	private final Inventory inventory;
	
	public AppraiseGUIHolder ( Inventory inventory ) {
		this.inventory = inventory;
	}
	
	@Override
	public @NotNull Inventory getInventory ( ) {
		return inventory;
	}
}
