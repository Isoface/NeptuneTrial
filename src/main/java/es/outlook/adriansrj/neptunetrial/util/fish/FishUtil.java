package es.outlook.adriansrj.neptunetrial.util.fish;

import es.outlook.adriansrj.neptunetrial.enums.EnumFishQuality;
import es.outlook.adriansrj.neptunetrial.enums.EnumVanillaFish;
import es.outlook.adriansrj.neptunetrial.fish.NeptuneFish;
import es.outlook.adriansrj.neptunetrial.fish.NeptuneFishHandler;
import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.RandomUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author AdrianSR / 29/12/2021 / 04:02 p. m.
 */
public class FishUtil {
	
	//	public static final double MAX_TO_INCREASE_PER_LEVEL = 4.2D;
	
	/**
	 * Appraises the provided fish item.
	 * <br>
	 * Note that this will not have any effect
	 * if the item is already appraised.
	 *
	 * @param item the item of the fish to apprise.
	 * @return true if the item was not already appraised, or if the item is not fish.
	 */
	public static boolean appraiseFish ( ItemStack item ) {
		ItemMeta        meta         = item.getItemMeta ( );
		EnumVanillaFish vanilla_fish = EnumVanillaFish.of ( item.getType ( ) );
		
		if ( meta != null && vanilla_fish != null ) {
			PersistentDataContainer data            = meta.getPersistentDataContainer ( );
			Double                  quality_wrapper = data.get ( Constants.QUALITY_NAMESPACE , PersistentDataType.DOUBLE );
			
			if ( quality_wrapper != null && !data.has ( Constants.APPRAISED_NAMESPACE , PersistentDataType.INTEGER ) ) {
				data.set ( Constants.APPRAISED_NAMESPACE , PersistentDataType.INTEGER , 1 );
				
				// displaying quality in the lore
				List < Component > lore = meta.lore ( );
				
				if ( lore == null ) {
					lore = new ArrayList <> ( );
				}
				
				lore.add ( Component.text ( EnumFishQuality.of ( quality_wrapper ).getLanguage ( ).getAsString ( ) ) );
				meta.lore ( lore );
				
				// updating meta
				item.setItemMeta ( meta );
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a random fish based on provided
	 * level of luck.
	 *
	 * @return a random fish.
	 */
	public static NeptuneFish randomFish ( ) {
		Collection < NeptuneFish > entries = NeptuneFishHandler.getInstance ( ).getFishes ( );
		
		if ( entries.size ( ) > 0 ) {
			NeptuneFish[] array = entries.toArray ( new NeptuneFish[ 0 ] );
			
			// calculating total chance
			double total_chance = 0.0D;
			
			for ( NeptuneFish entry : array ) {
				// we take into account only valid entries
				if ( entry != null && entry.isValid ( ) ) {
					total_chance += entry.getChance ( );
				}
			}
			
			// then getting the random entry
			while ( true ) {
				NeptuneFish next   = array[ RandomUtil.nextInt ( array.length ) ];
				double      random = Math.random ( ) * total_chance;
				
				if ( next.isValid ( ) && random <= next.getChance ( ) ) {
					return next;
				}
			}
		} else {
			return null;
		}
	}
	
	public static String formatName ( String name ) {
		return name.toLowerCase ( ).replace ( ' ' , '-' );
	}
	
	public static double normalizeQuality ( double quality ) {
		return Math.max ( Math.min ( quality , Constants.MAX_QUALITY ) , 0.0D );
		//		quality = Math.abs ( quality );
		//
		//		if ( quality % Constants.MAX_QUALITY != 0.0D ) {
		//			return quality % Constants.MAX_QUALITY;
		//		} else {
		//			return Constants.MAX_QUALITY;
		//		}
	}
}
