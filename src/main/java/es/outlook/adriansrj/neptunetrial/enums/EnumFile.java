package es.outlook.adriansrj.neptunetrial.enums;

import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;

import java.io.File;
import java.io.IOException;

/**
 * @author AdrianSR / 22/08/2021 / Time: 10:14 p. m.
 */
public enum EnumFile {
	
	MAIN_CONFIGURATION_FILE ( "MainConfiguration.yml" ),
	LANGUAGE_CONFIGURATION ( "LangConfiguration.yml" ),
	
	;
	
	private final String        name;
	private final EnumDirectory directory;
	
	private EnumFile ( EnumDirectory directory , String name ) {
		this.directory = directory;
		this.name      = name;
	}
	
	private EnumFile ( String name ) {
		this ( null , name );
	}
	
	public String getName ( ) {
		return name;
	}
	
	public File getFile ( ) {
		return new File ( directory != null ? directory.getDirectory ( )
								  : NeptuneTrial.getInstance ( ).getDataFolder ( ) , name );
	}
	
	/**
	 * Gets the file, and creates it if not exists.
	 *
	 * @return the file.
	 */
	public File safeGetFile ( ) {
		File file = getFile ( );
		
		if ( !file.getParentFile ( ).exists ( ) ) {
			file.getParentFile ( ).mkdirs ( );
		}
		
		if ( !file.exists ( ) ) {
			try {
				file.createNewFile ( );
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
		}
		return file;
	}
}
