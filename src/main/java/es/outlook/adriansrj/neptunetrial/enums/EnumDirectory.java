package es.outlook.adriansrj.neptunetrial.enums;

import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;

import java.io.File;

/**
 * Enumerates the directories the plugin handles.
 *
 * @author AdrianSR / 23/08/2021 / Time: 10:12 a. m.
 */
public enum EnumDirectory {
	
	/**
	 * Fish directory
	 */
	FISH_DIRECTORY ( "fish" )
	
	;
	
	private final String  name;
	private final boolean required;
	
	EnumDirectory ( String name , boolean required ) {
		this.name     = name;
		this.required = required;
	}
	
	EnumDirectory ( String name ) {
		this ( name , false );
	}
	
	/**
	 * Gets whether this directory should be
	 * generated if not exists when the
	 * plugin is enabled.
	 *
	 * @return whether to generate if not exists
	 * at the plugin startup.
	 */
	public boolean isRequired ( ) {
		return required;
	}
	
	public String getName ( ) {
		return name;
	}
	
	public File getDirectory ( ) {
		return new File ( NeptuneTrial.getInstance ( ).getDataFolder ( ) , name );
	}
	
	public File getDirectoryMkdirs ( ) {
		File directory = getDirectory ( );
		
		if ( !directory.exists ( ) ) {
			directory.mkdirs ( );
		}
		return directory;
	}
}