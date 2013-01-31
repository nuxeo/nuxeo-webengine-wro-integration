package org.nuxeo.platform.wro.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.locator.factory.UriLocatorFactory;
import ro.isdc.wro.util.StringUtils;
import ro.isdc.wro.util.WroUtil;

public class FromJsResourceReader {

    private final Resource parentResource;
    private final UriLocatorFactory uriLocatorFactory;


    public FromJsResourceReader(Resource parentResource, UriLocatorFactory uriLocatorFactory) {
        this.parentResource = parentResource;
        this.uriLocatorFactory = uriLocatorFactory;
    }

    public String readFile(String path) {
        String uri = computeAbsoluteUrl(parentResource, path);
        try {
            InputStream is = uriLocatorFactory.locate(uri);
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer);
            String result = writer.toString();
            return result;
        } catch (IOException e) {
            return "";
        }

    }


    /**
     * Computes absolute url of the imported resource.
     *
     * @param relativeResource {@link Resource} where the import statement is found.
     * @param importUrl found import url.
     * @return absolute url of the resource to import.
     */
    private String computeAbsoluteUrl(final Resource relativeResource, final String importUrl) {
      final String folder = FilenameUtils.getFullPath(relativeResource.getUri());
      // remove '../' & normalize the path.
      final String absoluteImportUrl = StringUtils.cleanPath(folder + importUrl);
      return absoluteImportUrl;
    }

}
