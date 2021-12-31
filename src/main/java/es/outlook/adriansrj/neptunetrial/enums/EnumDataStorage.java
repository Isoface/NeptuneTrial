package es.outlook.adriansrj.neptunetrial.enums;

import es.outlook.adriansrj.neptunetrial.storage.DataStorage;
import es.outlook.adriansrj.neptunetrial.storage.DataStorageSQLite;

/**
 * Enumerates the supported data storage systems.
 *
 * @author AdrianSR / 15/09/2021 / 06:18 p. m.
 */
public enum EnumDataStorage {
	
	SQLITE ( DataStorageSQLite.class ),
	;
	
	private final Class < ? extends DataStorage > clazz;
	
	EnumDataStorage ( Class < ? extends DataStorage > clazz ) {
		this.clazz = clazz;
	}
	
	public Class < ? extends DataStorage > getImplementationClass ( ) {
		return clazz;
	}
}
