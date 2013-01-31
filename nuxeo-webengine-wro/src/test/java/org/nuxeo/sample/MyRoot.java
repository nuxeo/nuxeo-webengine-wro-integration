/**
 * 
 */

package org.nuxeo.sample;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;


/**
 * The root entry for the WebEngine module. 
 * @author dmetzler
 */
@Path("/wrotest")
@Produces("text/html;charset=UTF-8")
@WebObject(type="MyRoot")
public class MyRoot extends ModuleRoot {

    @GET
    public Object doGet() {
        return getView("index");
    }

}
