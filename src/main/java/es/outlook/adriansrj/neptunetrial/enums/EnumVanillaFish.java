package es.outlook.adriansrj.neptunetrial.enums;

import org.bukkit.Material;

/**
 * @author AdrianSR / 29/12/2021 / 03:17 p. m.
 */
public enum EnumVanillaFish {
	
	COD ( Material.COD , 2.0F , 5.0F , 0.4F , 6.0F ) {
		@Override
		public Material getCookedMaterial ( ) {
			return Material.COOKED_COD;
		}
	},
	
	SALMON ( Material.SALMON , 2.0F , 6.0F , 0.2F , 9.6F ) {
		@Override
		public Material getCookedMaterial ( ) {
			return Material.COOKED_SALMON;
		}
	},
	
	TROPICAL ( Material.TROPICAL_FISH , 7.0F , 6.0F ),
	PUFFERFISH ( Material.PUFFERFISH , 8.0F , 7.5F ),
	;
	
	public static EnumVanillaFish of ( Material material ) {
		for ( EnumVanillaFish vanilla_fish : EnumVanillaFish.values ( ) ) {
			if ( vanilla_fish.getMaterial ( ) == material
					|| vanilla_fish.getCookedMaterial ( ) == material ) {
				return vanilla_fish;
			}
		}
		return null;
	}
	
	private final Material material;
	private final float    raw_food;
	private final float    food;
	private final float    raw_saturation;
	private final float    saturation;
	
	EnumVanillaFish ( Material material , float raw_food , float food , float raw_saturation , float saturation ) {
		this.material       = material;
		this.raw_food       = raw_food;
		this.food           = food;
		this.raw_saturation = raw_saturation;
		this.saturation     = saturation;
	}
	
	EnumVanillaFish ( Material material , float raw_food , float raw_saturation ) {
		this ( material , raw_food , 0.0F , raw_saturation , 0.0F );
	}
	
	public Material getMaterial ( ) {
		return material;
	}
	
	public Material getCookedMaterial ( ) {
		return null;
	}
	
	public float getRawFood ( ) {
		return raw_food;
	}
	
	public float getFood ( ) {
		return food;
	}
	
	public float getRawSaturation ( ) {
		return raw_saturation;
	}
	
	public float getSaturation ( ) {
		return saturation;
	}
}