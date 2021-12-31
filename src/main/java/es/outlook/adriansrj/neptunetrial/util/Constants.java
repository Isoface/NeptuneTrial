package es.outlook.adriansrj.neptunetrial.util;

import es.outlook.adriansrj.neptunetrial.main.NeptuneTrial;
import org.bukkit.NamespacedKey;

/**
 * @author AdrianSR / 29/12/2021 / 03:11 p. m.
 */
public class Constants {
	
	public static final double MAX_QUALITY = 5.0D;
	
	public static final String NAME_KEY         = "name";
	public static final String DISPLAY_NAME_KEY = "display-name";
	public static final String DESCRIPTION_KEY  = "description";
	public static final String VANILLA_KEY      = "vanilla";
	public static final String CHANCE_KEY       = "chance";
	public static final String QUALITY_KEY      = "quality";
	public static final String MAX_QUALITY_KEY  = "max-quality";
	public static final String MIN_QUALITY_KEY  = "min-quality";
	public static final String DATA_KEY         = "data";
	
	public static final String QUALITY_PLACEHOLDER      = "%quality%";
	public static final String QUALITY_TEXT_PLACEHOLDER = "%quality_text%";
	public static final String CHANCE_PLACEHOLDER       = "%chance%";
	public static final String WEIGHT_PLACEHOLDER       = "%weight%";
	
	public static final String WEIGHT_NAMESPACE_KEY    = "fish.weight";
	public static final String QUALITY_NAMESPACE_KEY   = "fish.quality";
	public static final String APPRAISED_NAMESPACE_KEY = "fish.appraised";
	
	public static final NamespacedKey WEIGHT_NAMESPACE;
	public static final NamespacedKey QUALITY_NAMESPACE;
	public static final NamespacedKey APPRAISED_NAMESPACE;
	
	static {
		WEIGHT_NAMESPACE    = new NamespacedKey ( NeptuneTrial.getInstance ( ) , WEIGHT_NAMESPACE_KEY );
		QUALITY_NAMESPACE   = new NamespacedKey ( NeptuneTrial.getInstance ( ) , QUALITY_NAMESPACE_KEY );
		APPRAISED_NAMESPACE = new NamespacedKey ( NeptuneTrial.getInstance ( ) , APPRAISED_NAMESPACE_KEY );
	}
	
	public static final int APPRAISE_GUI_INPUT_SLOT  = 12;
	public static final int APPRAISE_GUI_RESULT_SLOT = 14;
	public static final int APPRAISE_GUI_CLOSE_SLOT  = 26;
	public static final int APPRAISE_GUI_STATUS_SLOT = 4;
	
	public static final long APPRAISE_PROCESS_DURATION = 3000;
	
}