package es.outlook.adriansrj.neptunetrial.fish;

import es.outlook.adriansrj.neptunetrial.enums.EnumLanguage;
import es.outlook.adriansrj.neptunetrial.enums.EnumStat;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.storage.DataStorage;
import es.outlook.adriansrj.neptunetrial.storage.DataStorageHandler;
import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.fish.FishUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author AdrianSR / 30/12/2021 / 08:19 a. m.
 */
public final class NeptuneFishFishingHandler extends PluginHandler {
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public NeptuneFishFishingHandler ( NeptuneTrial plugin ) {
		super ( plugin );
		register ( );
	}
	
	@EventHandler ( priority = EventPriority.MONITOR, ignoreCancelled = true )
	public void onFishing ( PlayerFishEvent event ) {
		Player player = event.getPlayer ( );
		Entity caught = event.getCaught ( );
		
		if ( event.getState ( ) == PlayerFishEvent.State.CAUGHT_FISH
				&& caught instanceof Item ) {
			Item        caught_item = ( Item ) caught;
			NeptuneFish fish        = FishUtil.randomFish ( );
			ItemStack   fish_item   = fish != null ? fish.toItemStack ( false ) : null;
			
			if ( fish_item != null ) {
				caught_item.setItemStack ( fish_item );
				
				/* sound & stat */
				Double weight = fish_item.hasItemMeta ( ) ? fish_item.getItemMeta ( ).getPersistentDataContainer ( )
						.get ( Constants.WEIGHT_NAMESPACE , PersistentDataType.DOUBLE ) : null;
				
				if ( weight != null ) {
					// playing catch sound
					player.playSound ( event.getHook ( ).getLocation ( ) , Sound.ENTITY_DOLPHIN_SPLASH ,
									   Math.max ( weight.floatValue ( ) / 5.0F , 1.0F ) , 1.0F );
					
					// updating the heaviest fish stat
					DataStorage storage = DataStorageHandler.getInstance ( ).getDataStorage ( );
					
					if ( storage != null ) {
						try {
							int heaviest = storage.getStatValue ( player , EnumStat.HEAVIEST_FISH );
							int current  = ( int ) Math.round ( weight );
							
							if ( current > heaviest ) {
								storage.setStatValue ( player , EnumStat.HEAVIEST_FISH , current );
								
								// message
								player.sendMessage ( String.format (
										EnumLanguage.NEW_HEAVIEST_MESSAGE.getAsString ( ) ,
										String.valueOf ( current ) ) );
							}
						} catch ( Exception e ) {
							e.printStackTrace ( );
						}
					}
				}
			}
		} else if ( event.getState ( ) == PlayerFishEvent.State.BITE ) {
			// to actually stop the sound we have to
			// do it in the next tick, otherwise it
			// will not stop it.
			Bukkit.getScheduler ( ).scheduleSyncDelayedTask ( NeptuneTrial.getInstance ( ) , ( ) -> stopSound (
					player , Sound.ENTITY_FISHING_BOBBER_SPLASH ) );
		}
	}
	
	// ---- utils
	
	private void stopSound ( Player player , Sound sound ) {
		for ( SoundCategory category : SoundCategory.values ( ) ) {
			player.stopSound ( sound , category );
		}
	}
}
