package es.outlook.adriansrj.neptunetrial.gui;

import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.StringUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author AdrianSR / 30/12/2021 / 04:09 p. m.
 */
public class AppraiseGUIBuilder {
	
	private String title;
	
	public AppraiseGUIBuilder ( ) {
		this.title = "Fish Appraiser";
	}
	
	public AppraiseGUIBuilder title ( String title ) {
		this.title = title;
		return this;
	}
	
	public Inventory build ( Player player ) {
		Inventory result = Bukkit.createInventory (
				new AppraiseGUIHolder ( Bukkit.createInventory ( player , 27 ) ) ,
				27 ,
				Component.text ( StringUtil.limit ( StringUtil.translateAlternateColorCodes (
						StringUtil.defaultIfBlank ( title , "" ) ) , 27 ) ) );
		
		// decoration
		for ( int i = 0 ; i < result.getSize ( ) ; i++ ) {
			ItemStack decoration = new ItemStack ( Material.BLACK_STAINED_GLASS_PANE );
			ItemMeta  meta       = Bukkit.getItemFactory ( ).getItemMeta ( decoration.getType ( ) );
			
			meta.displayName ( Component.text ( " " ) );
			meta.setUnbreakable ( true );
			decoration.setItemMeta ( meta );
			
			result.setItem ( i , decoration );
		}
		
		// input/result slot
		result.setItem ( Constants.APPRAISE_GUI_INPUT_SLOT , null );
		result.setItem ( Constants.APPRAISE_GUI_RESULT_SLOT , null );
		
		// close button
		ItemStack close_button = new ItemStack ( Material.BARRIER );
		ItemMeta  close_meta   = Bukkit.getItemFactory ( ).getItemMeta ( close_button.getType ( ) );
		
		close_meta.displayName ( Component.text ( ChatColor.RED + "Close" ) );
		close_meta.setUnbreakable ( true );
		close_button.setItemMeta ( close_meta );
		
		result.setItem ( Constants.APPRAISE_GUI_CLOSE_SLOT , close_button );
		
		return result;
	}
}
