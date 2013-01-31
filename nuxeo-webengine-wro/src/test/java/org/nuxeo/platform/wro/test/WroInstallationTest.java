package org.nuxeo.platform.wro.test;

import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

@RunWith(FeaturesRunner.class)
@Features(MyWebEngineFeature.class)
@Jetty(port = 8084)
@Deploy("nuxeo-platform-wro-test-webengine-app")
public class WroInstallationTest {


    private static final String BASE_WRO_URL = "http://localhost:8084/we/wro/";

    @Before
    //Seems there are some lazy initializations....
    public void doBefore() {
        getHttpContent(BASE_WRO_URL + "simple.css");
    }

    @Test
    public void iCanRenderSimpleCssResourceWithWro() throws Exception {
        getHttpContent(BASE_WRO_URL + "simple.css");
        String content = getHttpContent(BASE_WRO_URL + "simple.css");
        assertThat(content,contains("table.header td{"));
    }


    @Test
    public void iCanRenderConcatenatedCss() throws Exception {
        String content = getHttpContent(BASE_WRO_URL + "compound.css");
        assertThat(content, contains(".toto{text-decoration:underline;"));
    }


    @Test
    public void iCanRenderLessCss() throws Exception {
        String content = getHttpContent(BASE_WRO_URL + "simpleless.css");
        assertThat(content, contains(".titi{width:6;}"));
    }

    @Test
    public void iCanRenderCompoundLessCss() throws Exception {
        String content = getHttpContent(BASE_WRO_URL + "compoundless.css");
        assertThat(content, contains("color:#000000"));
    }

    @Test
    public void iCanRenderBootstrap() throws Exception {
        String content = getHttpContent(BASE_WRO_URL + "bootstrap.css");
        assertThat(content, contains("article,aside"));
    }



    private Matcher<String> contains(final String pattern) {
        return new BaseMatcher<String>() {

            private Object resultItem;

            @Override
            public boolean matches(Object item) {
                resultItem = item;
                if(item == null || !( item instanceof String)) {
                    return false;
                }

                return ((String)item).contains(pattern);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Should contain : " + pattern);


            }

        };
    }



    private String getHttpContent(String url) {
        HttpClient client = new HttpClient();
        // Create a method instance.
        GetMethod method = new GetMethod(url);

        // Provide custom retry handler is necessary
//        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//                new DefaultHttpMethodRetryHandler(3, false));

        try {
          // Execute the method.
          int statusCode = client.executeMethod(method);

          if (statusCode != HttpStatus.SC_OK) {
            System.err.println("Method failed: " + method.getStatusLine());
          }

          // Read the response body.
          byte[] responseBody = method.getResponseBody();

          // Deal with the response.
          // Use caution: ensure correct character encoding and is not binary data
          return new String(responseBody);

        } catch (HttpException e) {
          System.err.println("Fatal protocol violation: " + e.getMessage());
          e.printStackTrace();
        } catch (IOException e) {
          System.err.println("Fatal transport error: " + e.getMessage());
          e.printStackTrace();
        } finally {
          // Release the connection.
          method.releaseConnection();
        }
        return "";

    }
}
