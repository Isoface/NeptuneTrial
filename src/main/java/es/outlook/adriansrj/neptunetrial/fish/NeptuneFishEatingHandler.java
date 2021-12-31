package es.outlook.adriansrj.neptunetrial.fish;

import es.outlook.adriansrj.neptunetrial.enums.EnumVanillaFish;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.fish.FishUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author AdrianSR / 30/12/2021 / 11:24 a. m.
 */
public final class NeptuneFishEatingHandler extends PluginHandler {
	
	// The weight determines how much food levels the fish
	// will restore. The fool levels are the weight divided by 2.
	
	// The higher the quality the higher the saturation.
	// A fish of maximum quality will give a doubled saturation.
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public NeptuneFishEatingHandler ( NeptuneTrial plugin ) {
		super ( plugin );
		register ( );
	}
	
	@EventHandler ( priority = EventPriority.HIGHEST, ignoreCancelled = true )
	public void onEating ( PlayerItemConsumeEvent event ) {
		Player          player       = event.getPlayer ( );
		ItemStack       item         = event.getItem ( );
		ItemMeta        meta         = item.getItemMeta ( );
		EnumVanillaFish vanilla_fish = EnumVanillaFish.of ( item.getType ( ) );
		
		if ( vanilla_fish != null && meta != null ) {
			PersistentDataContainer data    = meta.getPersistentDataContainer ( );
			Double                  quality = data.get ( Constants.QUALITY_NAMESPACE , PersistentDataType.DOUBLE );
			Double                  weight  = data.get ( Constants.WEIGHT_NAMESPACE , PersistentDataType.DOUBLE );
			
			if ( quality != null && weight != null ) {
				// there is no way to customize the saturation
				// from the PlayerItemConsumeEvent, so we will
				// cancel it and consume the item in the next tick.
				event.setCancelled ( true );
				
				Bukkit.getScheduler ( ).scheduleSyncDelayedTask ( NeptuneTrial.getInstance ( ) , ( ) -> {
					// consuming item
					if ( item.getAmount ( ) > 1 ) {
						item.setAmount ( item.getAmount ( ) - 1 );
					} else {
						player.getInventory ( ).setItemInMainHand ( null );
					}
					
					player.updateInventory ( );
					
					// restoring player food
					player.setFoodLevel ( Math.min ( player.getFoodLevel ( ) + ( int ) Math.round ( weight / 2.0D ) , 20 ) );
					
					// restoring player saturation
					float saturation_base = vanilla_fish.getRawSaturation ( );
					
					if ( vanilla_fish.getCookedMaterial ( ) == item.getType ( ) ) {
						saturation_base = vanilla_fish.getSaturation ( );
					}
					
					double quality_factor = FishUtil.normalizeQuality ( quality ) / Constants.MAX_QUALITY;
					float  saturation     = ( float ) ( saturation_base + ( saturation_base * quality_factor ) );
					
					player.setSaturation ( player.getSaturation ( ) + saturation );
				} );
			}
		}
	}
}
