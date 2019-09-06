/*
 * Copyright (C) 2019 ULTA
 * http://www.ulta.com
 * BrijendraK@ulta.com
 * All rights reserved
 */
package com.ulta.product.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.ulta.product.exception.UltaException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientFactory;

@Configuration
public class CommercetoolsConfig {

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@Autowired
	Environment env;
	SphereClient sphereClient = null;

	@Bean(destroyMethod = "close")
	public SphereClient sphereClient() throws UltaException {
		try {
			SphereClientConfig config = SphereClientConfig.of(env.getProperty("ctprojectKey"),
					env.getProperty("ctclientId"), env.getProperty("ctclientSecret"), env.getProperty("ctauthUrl"),
					env.getProperty("ctapiUrl"));
			sphereClient = SphereClientFactory.of().createClient(config);
		} catch (Exception e) {
			throw new UltaException("Internal server error during CT connection");
		}
		return sphereClient;

	}
}