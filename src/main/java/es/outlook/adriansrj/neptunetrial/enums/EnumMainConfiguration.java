package es.outlook.adriansrj.neptunetrial.enums;

import es.outlook.adriansrj.neptunetrial.configuration.ConfigurationEntry;
import es.outlook.adriansrj.neptunetrial.util.reflection.ClassReflection;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author AdrianSR / 16/12/2021 / 06:03 p. m.
 */
public enum EnumMainConfiguration implements ConfigurationEntry {
	
	// database enable
	ENABLE_DATABASE ( "database.enable" , false ),
	// database type
	DATABASE_TYPE ( "database.type" , EnumDataStorage.SQLITE.name ( ) ),
	
	// sqlite database
	DATABASE_SQLITE_PATH ( "database.sqlite.path" , "path" ),
	
	;
	
	private final String      key;
	private final Object      default_value;
	private final Class < ? > type;
	private       Object      value;
	
	EnumMainConfiguration ( String key , Object default_value , Class < ? > type ) {
		this.key           = key.trim ( );
		this.default_value = default_value;
		this.value         = default_value;
		this.type          = type;
	}
	
	EnumMainConfiguration ( String key , Object default_value ) {
		this ( key , default_value , default_value.getClass ( ) );
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
		return type;
	}
	
	@Override
	public void load ( ConfigurationSection section ) {
		Object raw = section.get ( getKey ( ) );
		
		if ( raw != null && ClassReflection.compatibleTypes ( this.type , raw ) ) {
			this.value = raw;
		}
	}
}
