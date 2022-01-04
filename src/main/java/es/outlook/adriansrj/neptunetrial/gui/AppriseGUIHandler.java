package es.outlook.adriansrj.neptunetrial.gui;

import es.outlook.adriansrj.neptunetrial.enums.EnumFishQuality;
import es.outlook.adriansrj.neptunetrial.enums.EnumLanguage;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.fish.FishUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author AdrianSR / 30/12/2021 / 04:03 p. m.
 */
public final class AppriseGUIHandler extends PluginHandler {
	
	/**
	 * Fish appraise task.
	 */
	private static class AppraiseTask extends BukkitRunnable {
		
		private       long      timestamp;
		private final Inventory inventory;
		private final Player    player;
		
		// process progress effect
		private int       processing_index;
		private int       last_processing_index;
		private boolean   processing_dir;
		private ItemStack decoration;
		
		public AppraiseTask ( Inventory inventory , Player player ) {
			this.inventory = inventory;
			this.player    = player;
		}
		
		@Override
		public void run ( ) {
			if ( timestamp == 0L ) {
				timestamp = System.currentTimeMillis ( );
			}
			
			ItemStack input = inventory.getItem ( Constants.APPRAISE_GUI_INPUT_SLOT );
			
			if ( input != null ) {
				if ( System.currentTimeMillis ( ) - timestamp > Constants.APPRAISE_PROCESS_DURATION ) {
					if ( FishUtil.appraiseFish ( input ) ) {
						inventory.setItem ( Constants.APPRAISE_GUI_STATUS_SLOT , buildStatusItem ( true ) );
						
						// congratulations effect
						Double quality = input.getItemMeta ( ).getPersistentDataContainer ( )
								.get ( Constants.QUALITY_NAMESPACE , PersistentDataType.DOUBLE );
						
						if ( quality != null && EnumFishQuality.of ( quality ) == EnumFishQuality.HIGH ) {
							player.sendMessage ( EnumLanguage.CONGRATULATIONS_MESSAGE.getAsString ( ) );
							
							// launching firework
							Firework firework = player.getWorld ( )
									.spawn ( player.getLocation ( ) , Firework.class );
							FireworkMeta           firework_meta = firework.getFireworkMeta ( );
							FireworkEffect.Builder builder       = FireworkEffect.builder ( );
							
							builder.withFlicker ( );
							builder.withFade ( Color.GREEN );
							builder.withColor ( Color.GREEN );
							builder.with ( FireworkEffect.Type.STAR );
							
							firework_meta.addEffect ( builder.build ( ) );
							firework_meta.setPower ( 1 );
							firework.setFireworkMeta ( firework_meta );
						}
					} else {
						inventory.setItem ( Constants.APPRAISE_GUI_STATUS_SLOT , buildStatusItem ( false ) );
					}
					
					inventory.setItem ( Constants.APPRAISE_GUI_INPUT_SLOT , null );
					inventory.setItem ( Constants.APPRAISE_GUI_RESULT_SLOT , input );
					
					inventory.setItem ( last_processing_index , decoration );
					inventory.setItem ( processing_index , decoration );
					
					cancel ( );
				} else {
					if ( decoration == null ) {
						// we will create a backup of the decoration
						// items, so we can restore them later.
						decoration = inventory.getItem ( 20 );
					}
					
					if ( last_processing_index != 0 ) {
						inventory.setItem ( last_processing_index , decoration );
					}
					
					inventory.setItem ( calculateNextProcessingIndex ( ) , buildProcessProgressItem ( ) );
					last_processing_index = processing_index;
				}
			} else {
				cancel ( );
			}
			
			player.updateInventory ( );
		}
		
		// ---- utils
		
		private ItemStack buildStatusItem ( boolean ok ) {
			ItemStack result = new ItemStack ( ok ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE );
			ItemMeta  meta   = Bukkit.getItemFactory ( ).getItemMeta ( result.getType ( ) );
			
			meta.displayName ( Component.text ( ok ? ChatColor.GREEN + "Successfully Appraised"
														: ChatColor.RED + "Something went wrong" ) );
			meta.setUnbreakable ( true );
			result.setItemMeta ( meta );
			
			return result;
		}
		
		private ItemStack buildProcessProgressItem ( ) {
			ItemStack result = new ItemStack ( Material.GREEN_STAINED_GLASS_PANE );
			ItemMeta  meta   = Bukkit.getItemFactory ( ).getItemMeta ( result.getType ( ) );
			
			meta.displayName ( Component.text ( ChatColor.GREEN + "Processing" ) );
			result.setItemMeta ( meta );
			
			return result;
		}
		
		private int calculateNextProcessingIndex ( ) {
			if ( processing_index == 0 ) {
				processing_index = 20;
				processing_dir   = true;
				return processing_index;
			} else {
				if ( processing_dir ) {
					if ( processing_index + 1 <= 24 ) {
						processing_index++;
					} else {
						processing_index--;
						processing_dir = false;
					}
				} else {
					if ( processing_index - 1 >= 20 ) {
						processing_index--;
					} else {
						processing_index++;
						processing_dir = true;
					}
				}
			}
			return processing_index;
		}
	}
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public AppriseGUIHandler ( NeptuneTrial plugin ) {
		super ( plugin );
		register ( );
	}
	
	// handler responsible handling the interactions of the players
	// with the appraise-GUIs.
	@EventHandler ( priority = EventPriority.HIGHEST, ignoreCancelled = true )
	public void onClick ( InventoryClickEvent event ) {
		HumanEntity user      = event.getView ( ).getPlayer ( );
		Inventory   inventory = event.getClickedInventory ( );
		int         index     = event.getSlot ( );
		
		if ( inventory != null && inventory.getHolder ( ) instanceof AppraiseGUIHolder ) {
			if ( index == Constants.APPRAISE_GUI_INPUT_SLOT ) {
				ItemStack current_input = inventory.getItem ( Constants.APPRAISE_GUI_INPUT_SLOT );
				boolean   cancel        = true;
				
				if ( current_input == null || current_input.getType ( ).isEmpty ( ) ) {
					cancel = false;
					
					// starting apprise process
					new AppraiseTask ( inventory , ( Player ) user ).runTaskTimer (
							NeptuneTrial.getInstance ( ) , 5L , 5L );
				} else {
					user.sendMessage ( ChatColor.RED + "Another fish is being appraised!" );
				}
				
				if ( cancel ) {
					event.setCancelled ( true );
				}
			} else if ( index == Constants.APPRAISE_GUI_RESULT_SLOT ) {
				ItemStack result = inventory.getItem ( Constants.APPRAISE_GUI_RESULT_SLOT );
				
				if ( result == null || result.getType ( ).isEmpty ( ) ) {
					event.setCancelled ( true );
				}
			} else {
				if ( index == Constants.APPRAISE_GUI_CLOSE_SLOT ) {
					user.closeInventory ( );
				}
				
				event.setCancelled ( true );
			}
		}
	}
	
	// handler responsible for giving back the input fish
	// when the player closes the inventory. if the inventory
	// of the player is full then the input fish will be dropped.
	@EventHandler ( priority = EventPriority.HIGHEST, ignoreCancelled = true )
	public void onClose ( InventoryCloseEvent event ) {
		Inventory inventory = event.getInventory ( );
		
		if ( inventory.getHolder ( ) instanceof AppraiseGUIHolder ) {
			ItemStack current_input  = inventory.getItem ( Constants.APPRAISE_GUI_INPUT_SLOT );
			ItemStack current_result = inventory.getItem ( Constants.APPRAISE_GUI_RESULT_SLOT );
			
			if ( inventory.getHolder ( ) instanceof AppraiseGUIHolder
					&& ( current_input != null && !current_input.getType ( ).isEmpty ( ) ||
					current_result != null && !current_result.getType ( ).isEmpty ( ) ) ) {
				HumanEntity     user             = event.getPlayer ( );
				PlayerInventory player_inventory = user.getInventory ( );
				ItemStack       save             = current_input != null ? current_input : current_result;
				
				if ( player_inventory.firstEmpty ( ) != -1 ) {
					player_inventory.addItem ( save );
					
					if ( user instanceof Player ) {
						( ( Player ) user ).updateInventory ( );
					}
				} else {
					event.getPlayer ( ).getWorld ( ).dropItem ( user.getLocation ( ) , save );
				}
			}
		}
	}
}