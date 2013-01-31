package org.nuxeo.platform.wro;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.nuxeo.common.Environment;
import org.nuxeo.runtime.api.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.config.factory.ServletContextPropertyWroConfigurationFactory;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.resource.locator.ClasspathUriLocator;
import ro.isdc.wro.model.resource.locator.ServletContextUriLocator;
import ro.isdc.wro.model.resource.locator.UrlUriLocator;
import ro.isdc.wro.model.resource.locator.factory.SimpleUriLocatorFactory;
import ro.isdc.wro.model.resource.locator.factory.UriLocatorFactory;

public class NuxeoWroManagerFactory extends ConfigurableWroManagerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(NuxeoWroManagerFactory.class);

    @Override
    protected WroModelFactory newModelFactory() {
        return new NuxeoWroModelFactory();
    }

    @Override
    protected UriLocatorFactory newUriLocatorFactory() {
        return new SimpleUriLocatorFactory().addUriLocator(
                new ServletContextUriLocator())
                .addUriLocator(new ClasspathUriLocator())
                .addUriLocator(new WebengineUriLocator())
                .addUriLocator(new UrlUriLocator());
    }

    @Override
    protected Properties newConfigProperties() {

        // default location is /WEB-INF/wro.properties
        try {
            Environment env = Environment.getDefault();
            File config = env.getConfig();
            File wro = new File(config, "wro.properties");
            if (wro.exists()) {
                final InputStream propertyStream = new FileInputStream(wro);
                Validate.notNull(propertyStream);
                final Properties props = new Properties();
                props.load(propertyStream);
                return props;
            } else {
                return new ServletContextPropertyWroConfigurationFactory(
                        Context.get()
                                .getServletContext()).createProperties();
            }
        } catch (final Exception e) {
            LOG.debug("No configuration property file found.");
            return null;
        }

    }
}
