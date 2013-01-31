package org.nuxeo.platform.wro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.impl.ModuleConfiguration;
import org.nuxeo.runtime.api.Framework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.model.WroModel;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;

public class NuxeoWroModelFactory implements WroModelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(NuxeoWroModelFactory.class);
    /**
     * This merge all wro.xml contained in all WebEngine root directory
     */
    @Override
    public WroModel create() {
        WroModel model = new WroModel();
        LOG.info("Creating WRO Model by aggregating webengin modules wro.xml");
        WebEngine engine = Framework.getLocalService(WebEngine.class);
        ModuleConfiguration[] modules = engine.getModuleManager()
                .getModules();
        for (ModuleConfiguration conf : modules) {
            File root = conf.get()
                    .getRoot();
            final File wroXml = new File(root.getAbsolutePath() + "/wro.xml");
            if (wroXml.exists()) {
                LOG.info("Found a wro.xml file in " + conf.getName() + " webengin module");
                final XmlModelFactory modelFactory = new XmlModelFactory() {
                    @Override
                    protected InputStream getModelResourceAsStream()
                            throws IOException {
                        return new FileInputStream(wroXml);
                    };
                };
                WroModel otherModel = modelFactory.create();
                model.merge(otherModel);
            }
        }
        return model;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

}
