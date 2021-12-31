package es.outlook.adriansrj.neptunetrial.enums;

import es.outlook.adriansrj.neptunetrial.configuration.ConfigurationEntry;
import es.outlook.adriansrj.neptunetrial.util.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author AdrianSR / 04/09/2021 / 03:21 p. m.
 */
public enum EnumLanguage implements ConfigurationEntry {
	
	QUALITY_HIGH ( "quality.good" , ChatColor.GREEN + "High Quality" ),
	QUALITY_REGULAR ( "quality.regular" , ChatColor.YELLOW + "Regular Quality" ),
	QUALITY_BAD ( "quality.bad" , ChatColor.RED + "Poor Quality" ),
	
	// congratulations
	CONGRATULATIONS_MESSAGE ( "congratulations" ,
							  ChatColor.GREEN + "Congratulations! Your fish is of high quality!" ),
	
	// new heaviest message
	NEW_HEAVIEST_MESSAGE ( "new-heaviest" ,
						   ChatColor.GREEN + "New heaviest: %s" ),
	;
	
	private final String key;
	private final Object default_value;
	private       Object value;
	
	EnumLanguage ( String key , String default_value ) {
		this.key           = key;
		this.default_value = StringUtil.untranslateAlternateColorCodes ( default_value );
		this.value         = this.default_value;
	}
	
	@Override
	public String getKey ( ) {
		return key;
	}
	
	@Override
	public String getComment ( ) {
		return null;
	}
	
	@Override
	public Object getDefaultValue ( ) {
		return default_value;
	}
	
	@Override
	public Object getValue ( ) {
		return value;
	}
	
	@Override
	public Class < ? > getValueType ( ) {
		return String.class;
	}
	
	@Override
	public String getAsString ( ) {
		return StringUtil.translateAlternateColorCodes ( StringEscapeUtils.unescapeJava (
				ConfigurationEntry.super.getAsString ( ) ) );
	}
	
	@Override
	public void load ( ConfigurationSection section ) {
		this.value = section.getString ( getKey ( ) , StringUtil.EMPTY );
	}
}