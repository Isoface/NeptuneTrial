package es.outlook.adriansrj.neptunetrial.fish;

import es.outlook.adriansrj.neptunetrial.enums.EnumVanillaFish;
import es.outlook.adriansrj.neptunetrial.handler.PluginHandler;
import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import es.outlook.adriansrj.neptunetrial.util.fish.FishUtil;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author AdrianSR / 04/01/2022 / 02:54 p. m.
 */
public final class NeptuneFishCookingHandler extends PluginHandler {
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public NeptuneFishCookingHandler ( NeptuneTrial plugin ) {
		super ( plugin );
		register ( );
	}
	
	@EventHandler ( priority = EventPriority.HIGHEST, ignoreCancelled = true )
	public void onCook ( BlockCookEvent event ) {
		ItemStack       source       = event.getSource ( );
		EnumVanillaFish vanilla_fish = EnumVanillaFish.of ( source.getType ( ) );
		
		if ( event.getBlock ( ).getState ( ) instanceof Furnace
				&& vanilla_fish != null && FishUtil.isNeptuneFishItem ( source )
				&& vanilla_fish.getCookedMaterial ( ) != null ) {
			ItemStack result = source.clone ( );
			result.setType ( vanilla_fish.getCookedMaterial ( ) );
			
			event.setResult ( result );
		}
	}
}