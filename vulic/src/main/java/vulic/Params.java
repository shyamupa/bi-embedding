package vulic;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by upadhya3 on 9/25/15.
 */
public class Params {

	 
	private static final Logger logger = LoggerFactory.getLogger(Params.class);
//	private static Params theInstance;

//	public static void initialize() {
//		initialize("config/vulic.properties");
//	}
//	public static void initialize(String file) {
//		assert theInstance == null;
//		try {
//			theInstance = new Params(file);
//		} catch (ConfigurationException e) {
//			throw new RuntimeException(e);
//		}
//		logger.info("Finished Initializing Configs");
//	}
//
//	public static Params getInstance() {
//		if (theInstance == null)
//			initialize("config/vulic.properties");
//		return theInstance;
//	}

	public static String writedir;
	public static int VECSIZE;
	
//	private Params(String file) throws ConfigurationException {
//
//		PropertiesConfiguration config = new PropertiesConfiguration(file);
//		this.writedir = config.getString("writedir");
//		this.VECSIZE = config.getInt("vecsize");
//	}
	static void loadConfig(String filepath) throws ConfigurationException
	{
		PropertiesConfiguration config = new PropertiesConfiguration(filepath);
		writedir = config.getString("writedir");
		VECSIZE = config.getInt("vecsize");
	}

}
