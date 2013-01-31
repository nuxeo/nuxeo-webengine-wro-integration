package org.nuxeo.platform.wro.test;

/*
 * (C) Copyright 2006-2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     bstefanescu
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.webengine.test.WebEngineFeature;
import org.nuxeo.runtime.test.WorkingDirectoryConfigurator;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.RuntimeFeature;
import org.nuxeo.runtime.test.runner.RuntimeHarness;
import org.nuxeo.runtime.test.runner.SimpleFeature;



@Features(WebEngineFeature.class)
public class MyWebEngineFeature extends SimpleFeature implements WorkingDirectoryConfigurator {

    @Override
    public void initialize(FeaturesRunner runner) throws Exception {
        runner.getFeature(RuntimeFeature.class).getHarness().addWorkingDirectoryConfigurator(this);
    }



    @Override
    public void configure(RuntimeHarness harness, File workingDir) throws IOException {
        System.out.println(workingDir.getAbsolutePath());
        File dest = new File(workingDir, "web/root.war/WEB-INF/");
        dest.mkdirs();

        copyToWebInf(workingDir, new String[] { "web.xml", "wro.properties"});


    }


    private void copyToWebInf(File workingDir, String[] files) throws IOException {
        for ( String file : files) {
            InputStream in = getResource("web/WEB-INF/" + file).openStream();
            File dest = new File(workingDir + "/web/root.war/WEB-INF/", file);
            try {
                FileUtils.copyToFile(in, dest);
            } finally {
                in.close();
            }
        }

    }



    private static URL getResource(String resource) {
        //return Thread.currentThread().getContextClassLoader().getResource(resource);
        return Jetty.class.getClassLoader().getResource(resource);
    }

}
