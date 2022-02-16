package com.epam.esm.kalimulin.certificate.service.util;

import org.jasypt.util.text.StrongTextEncryptor;

public class EncryptionText {
	
	private static StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
	private static String privateData = "secret-password";
	
	static {
		textEncryptor.setPassword(privateData);
	}
	
	public static String encrypt(String text) {
		return textEncryptor.encrypt(text);
	}
	
	public static String decrypt(String text) {
		return textEncryptor.decrypt(text);
	}
	
}
