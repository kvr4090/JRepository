package com.epam.esm.kalimulin.certificate.dao.util;

public class Order {
	
	public Order(String propertyName, boolean b) {
	}

	public static Order asc(String propertyName) {
		return new Order(propertyName, true);
	}

	public static Order desc(String propertyName) {
		return new Order(propertyName, false);
	}
}
