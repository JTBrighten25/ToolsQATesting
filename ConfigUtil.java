package org.automation.seleniumdemo.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

//class retrieves configuration data
public class ConfigUtil {
	
	//method fetches property value related to key
	public static Object fetchPropertyValue(String key) throws IOException
	{
		FileInputStream file = new FileInputStream("./Config/config.properties");
		Properties property = new Properties();
		property.load(file);
		return property.get(key);
	}
}
