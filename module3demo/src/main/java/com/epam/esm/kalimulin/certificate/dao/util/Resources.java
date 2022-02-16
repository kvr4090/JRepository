package com.epam.esm.kalimulin.certificate.dao.util;

import static com.epam.esm.kalimulin.certificate.dao.util.DaoConstant.HQL_QUERY_PROPERTY_FILE;

import java.util.ResourceBundle;

public class Resources {
	
	private static final ResourceBundle hqlQueryBundle = ResourceBundle.getBundle(HQL_QUERY_PROPERTY_FILE);
	
	public static String getQueryString(String query) {
		return hqlQueryBundle.getString(query);	
	}
}
