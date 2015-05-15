package com.misys.async_rest.application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.misys.async_rest.dao.PersonDao;

public class PersonApplication extends ResourceConfig {

	public PersonApplication(final PersonDao dao) {
		
		JacksonJsonProvider json = new JacksonJsonProvider().
				configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).
				configure(SerializationFeature.INDENT_OUTPUT, true);
		
		
		packages("com.misys").
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(dao).to(PersonDao.class);		
			}
		});
		register(json);
	}
}
