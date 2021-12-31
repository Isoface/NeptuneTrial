package es.outlook.adriansrj.neptunetrial.fish;

import es.outlook.adriansrj.neptunetrial.enums.EnumVanillaFish;
import es.outlook.adriansrj.neptunetrial.util.Configurable;
import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.StringUtil;
import es.outlook.adriansrj.neptunetrial.util.YamlUtil;
import es.outlook.adriansrj.neptunetrial.util.fish.FishUtil;
import es.outlook.adriansrj.neptunetrial.util.reflection.EnumReflection;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AdrianSR / 29/12/2021 / 02:55 p. m.
 */
public class NeptuneFish implements Configurable < NeptuneFish > {
	
	protected       String          name;
	protected       String          display_name;
	protected final List < String > description = new ArrayList <> ( );
	protected       EnumVanillaFish vanilla_fish;
	protected       double          chance;
	protected       double          min_quality;
	protected       double          max_quality;
	protected       int             data;
	
	public NeptuneFish ( String name , String display_name , List < String > description ,
			EnumVanillaFish vanilla_fish , double min_quality , double max_quality , double chance ) {
		this.name         = FishUtil.formatName ( name );
		this.display_name = StringUtil.translateAlternateColorCodes (
				StringUtil.defaultIfBlank ( display_name , StringUtil.EMPTY ) );
		this.vanilla_fish = vanilla_fish;
		this.min_quality  = FishUtil.normalizeQuality ( min_quality );
		this.max_quality  = FishUtil.normalizeQuality ( max_quality );
		this.chance       = chance;
		
		if ( description != null ) {
			this.description.addAll ( StringUtil.translateAlternateColorCodes ( description ) );
		}
	}
	
	public NeptuneFish ( String name , String display_name , List < String > description ,
			EnumVanillaFish vanilla_fish , double chance ) {
		this ( name , display_name , description , vanilla_fish , 0.0D , 5.0D , chance );
	}
	
	public NeptuneFish ( ) {
		// to be loaded
	}
	
	public String getName ( ) {
		return FishUtil.formatName ( name );
	}
	
	public String getDisplayName ( ) {
		return display_name;
	}
	
	public List < String > getDescription ( ) {
		return description;
	}
	
	public EnumVanillaFish getVanillaFish ( ) {
		return vanilla_fish;
	}
	
	public double getChance ( ) {
		return chance;
	}
	
	public int getData ( ) {
		return data;
	}
	
	public double getMaxQuality ( ) {
		return max_quality;
	}
	
	public double getMinQuality ( ) {
		return min_quality;
	}
	
	public ItemStack toItemStack ( boolean cooked , double weight , double quality ) {
		if ( cooked && vanilla_fish.getCookedMaterial ( ) == null ) {
			return null;
		}
		
		ItemStack result = new ItemStack ( cooked ? vanilla_fish.getCookedMaterial ( ) : vanilla_fish.getMaterial ( ) );
		ItemMeta  meta   = result.getItemMeta ( );
		
		if ( meta == null ) {
			meta = Bukkit.getItemFactory ( ).getItemMeta ( vanilla_fish.getMaterial ( ) );
		}
		
		meta.displayName ( Component.text ( StringUtil.defaultIfBlank (
				display_name , StringUtil.EMPTY ) ) );
		meta.lore ( description.stream ( ).map ( line -> Component.text (
						line.replace ( Constants.CHANCE_PLACEHOLDER ,
									   String.valueOf ( String.format ( "%.2f" , chance ) ) )
								.replace ( Constants.WEIGHT_PLACEHOLDER ,
										   String.valueOf ( String.format ( "%.2f" , weight ) ) ) ) )
							.collect ( Collectors.toList ( ) ) );
		
		if ( data > 0 ) {
			meta.setCustomModelData ( data );
		}
		
		// setting weight
		meta.getPersistentDataContainer ( ).set (
				Constants.WEIGHT_NAMESPACE , PersistentDataType.DOUBLE , weight );
		// setting quality
		meta.getPersistentDataContainer ( ).set (
				Constants.QUALITY_NAMESPACE , PersistentDataType.DOUBLE , quality );
		
		result.setItemMeta ( meta );
		
		return result;
	}
	
	public ItemStack toItemStack ( boolean cooked ) {
		// random weight
		float weight_base = vanilla_fish.getRawFood ( ) * 2.0F;
		
		if ( cooked && vanilla_fish.getCookedMaterial ( ) != null ) {
			weight_base = vanilla_fish.getFood ( ) * 2.0F;
		}
		
		return toItemStack ( cooked ,
							 weight_base + ( Math.random ( ) * ( weight_base / 2.0F ) ) ,
							 Math.min ( Math.max ( Math.random ( ) * Constants.MAX_QUALITY , min_quality ) , max_quality ) );
	}
	
	@Override
	public int save ( ConfigurationSection section ) {
		int save = 0;
		
		// saving name
		if ( StringUtil.isNotBlank ( name ) ) {
			save += YamlUtil.setNotEqual ( section , Constants.NAME_KEY , FishUtil.formatName ( name ) ) ? 1 : 0;
		}
		
		// saving display name
		if ( StringUtil.isNotBlank ( display_name ) ) {
			save += YamlUtil.setNotEqual (
					section , Constants.DISPLAY_NAME_KEY ,
					StringUtil.untranslateAlternateColorCodes ( display_name ) ) ? 1 : 0;
		}
		
		// saving description
		if ( description.size ( ) > 0 ) {
			save += YamlUtil.setNotEqual (
					section , Constants.DESCRIPTION_KEY ,
					StringUtil.untranslateAlternateColorCodes ( description ) ) ? 1 : 0;
		} else {
			save += section.isSet ( Constants.DESCRIPTION_KEY ) ? 1 : 0;
			section.set ( Constants.DESCRIPTION_KEY , null );
		}
		
		// saving vanilla fish
		if ( vanilla_fish != null ) {
			save += YamlUtil.setNotEqual ( section , Constants.VANILLA_KEY , vanilla_fish.name ( ) ) ? 1 : 0;
		}
		
		// saving chance
		save += YamlUtil.setNotEqual ( section , Constants.CHANCE_KEY , chance ) ? 1 : 0;
		// saving data
		save += YamlUtil.setNotEqual ( section , Constants.DATA_KEY , data ) ? 1 : 0;
		// saving max & min quality
		save += YamlUtil.setNotEqual (
				section , Constants.MAX_QUALITY_KEY , FishUtil.normalizeQuality ( max_quality ) ) ? 1 : 0;
		save += YamlUtil.setNotEqual (
				section , Constants.MIN_QUALITY_KEY , FishUtil.normalizeQuality ( min_quality ) ) ? 1 : 0;
		
		return save;
	}
	
	@Override
	public NeptuneFish load ( ConfigurationSection section ) {
		this.name         = FishUtil.formatName ( section.getString ( Constants.NAME_KEY , StringUtil.EMPTY ) );
		this.display_name = StringUtil.translateAlternateColorCodes ( section.getString (
				Constants.DISPLAY_NAME_KEY , StringUtil.EMPTY ) );
		this.vanilla_fish = EnumReflection.getEnumConstant (
				EnumVanillaFish.class , section.getString ( Constants.VANILLA_KEY , StringUtils.EMPTY ).toUpperCase ( ) );
		this.chance       = section.getDouble ( Constants.CHANCE_KEY );
		this.data         = section.getInt ( Constants.DATA_KEY );
		this.max_quality  = FishUtil.normalizeQuality ( section.getDouble ( Constants.MAX_QUALITY_KEY ) );
		this.min_quality  = FishUtil.normalizeQuality ( section.getDouble ( Constants.MIN_QUALITY_KEY ) );
		
		this.description.clear ( );
		this.description.addAll ( StringUtil.translateAlternateColorCodes (
				section.getStringList ( Constants.DESCRIPTION_KEY ) ) );
		
		return this;
	}
	
	@Override
	public boolean isValid ( ) {
		return StringUtil.isNotBlank ( name ) && vanilla_fish != null && chance > 0.0D;
	}
}