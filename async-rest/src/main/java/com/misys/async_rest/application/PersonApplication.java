package com.misys.async_rest.application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.misys.async_rest.dao.PersonDao;

public class PersonApplication extends ResourceConfig {

	public PersonApplication(final PersonDao dao) {
		packages("com.misys").
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(dao).to(PersonDao.class);		
			}
		});
	}
}
