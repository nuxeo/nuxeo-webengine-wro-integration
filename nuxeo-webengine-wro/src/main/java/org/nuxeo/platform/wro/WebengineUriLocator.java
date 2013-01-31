package org.nuxeo.platform.wro;

import java.io.IOException;
import java.io.InputStream;

import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.Module;
import org.nuxeo.ecm.webengine.scripting.ScriptFile;
import org.nuxeo.runtime.api.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.model.resource.locator.wildcard.WildcardUriLocatorSupport;

public class WebengineUriLocator extends WildcardUriLocatorSupport {

    private static final String LOCATOR_URI_PREFIX = "webengine:";
    private static final Logger LOG = LoggerFactory.getLogger(WebengineUriLocator.class);

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(LOCATOR_URI_PREFIX);
    }

    @Override
    public InputStream locate(String uri) throws IOException {
        if (uri == null) {
            return null;
        }

        String path = uri.substring(LOCATOR_URI_PREFIX.length());

        int p = path.indexOf('/', 1);
        String moduleName = null;
        if (p > -1) {
            moduleName = path.substring(0, p);
            path = path.substring(p);
        } else {
            return null;
        }
        try {
            WebEngine engine = Framework.getService(WebEngine.class);
            Module module = engine.getModule(moduleName);
            if (module == null) {
                return null;
            }
            ScriptFile file = module.getSkinResource("/resources/" + path);
            if(file == null) {
                LOG.error("Unable to resolve resource : " + uri);
            }
            return file.getInputStream();
        } catch (Exception e) {
            return null;
        }

    }

}
