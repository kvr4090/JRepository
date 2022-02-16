package com.epam.esm.kalimulin.certificate.service.util;

import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.ENCRYPT_PARAM;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.ENCRYPT_PROP;
import static com.epam.esm.kalimulin.certificate.service.util.ServiceConstant.ERROR_LOAD_PROPS;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.jasypt.util.text.StrongTextEncryptor;

public class TextEncryptor {
	
	private static StrongTextEncryptor textEncryptor;
	
	static {
		try {
			ResourceBundle encryptPropBundle = ResourceBundle.getBundle(ENCRYPT_PROP);
			textEncryptor = new StrongTextEncryptor();
			textEncryptor.setPassword(encryptPropBundle.getString(ENCRYPT_PARAM));
		} catch (MissingResourceException exception) {			
			throw new RuntimeException(ERROR_LOAD_PROPS + exception.getMessage());
		}
	}
	
	public static String encrypt(String text) {
		return textEncryptor.encrypt(text);
	}
	
	public static String decrypt(String text) {
		return textEncryptor.decrypt(text);
	}
}
