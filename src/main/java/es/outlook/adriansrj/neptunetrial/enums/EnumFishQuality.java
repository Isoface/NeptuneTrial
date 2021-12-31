package es.outlook.adriansrj.neptunetrial.enums;

import es.outlook.adriansrj.neptunetrial.util.Constants;
import es.outlook.adriansrj.neptunetrial.util.fish.FishUtil;

/**
 * @author AdrianSR / 31/12/2021 / 06:13 p. m.
 */
public enum EnumFishQuality {
	
	HIGH ( EnumLanguage.QUALITY_HIGH ),
	REGULAR ( EnumLanguage.QUALITY_REGULAR ),
	BAD ( EnumLanguage.QUALITY_BAD );
	
	public static EnumFishQuality of ( double quality ) {
		EnumFishQuality[] qualities      = values ( );
		double            quality_factor = FishUtil.normalizeQuality ( quality ) / Constants.MAX_QUALITY;
		
		return qualities[ ( qualities.length - 1 ) - ( int ) Math.round (
				quality_factor * ( qualities.length - 1 ) ) ];
	}
	
	private final EnumLanguage language;
	
	EnumFishQuality ( EnumLanguage language ) {
		this.language = language;
	}
	
	public EnumLanguage getLanguage ( ) {
		return language;
	}
}
