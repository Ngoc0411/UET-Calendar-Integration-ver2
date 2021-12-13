package com.team2.routes;

import java.io.InputStream;
import java.net.URLConnection;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.commons.io.IOUtils;


public class WebServiceRoute extends RouteBuilder {

	public static final String HOSTNAME = "https://zachtrong.com:4444";
	
	@Override
	public void configure() throws Exception {
		restConfiguration().component("jetty").host("0.0.0.0").port(8080).bindingMode(RestBindingMode.off);
		
		from("jetty://http://0.0.0.0:8080?matchOnUriPrefix=true")
			.process(new Processor() {

				@Override
				public void process(Exchange exchange) throws Exception {
					Message in = exchange.getIn();

		            String relativepath = in.getHeader(Exchange.HTTP_PATH, String.class);
		            String requestPath = in.getHeader("CamelServletContextPath", String.class); //CamelServletContextPath

		            if (relativepath.contains("/rest")) {
		            	return;
		            }
		            if (relativepath.isEmpty() || relativepath.equals("/")) {
		                relativepath = "index.html";
		            }

		            final String formattedPath = String.format("static/%s", relativepath);

		            Message out = exchange.getOut();
		            try {
			            InputStream inputStream = exchange.getContext().getClassResolver().loadResourceAsStream(formattedPath);

		                out.setBody(IOUtils.toByteArray(inputStream));
		                out.setHeader(Exchange.CONTENT_TYPE, URLConnection.guessContentTypeFromStream(inputStream));
		            } catch (Throwable t) {
		                out.setBody(relativepath + " not found.");
		                out.setHeader(Exchange.HTTP_RESPONSE_CODE, "404");
		            }
				}
			});
		
		rest("/rest")
			.post("/uet-auth").to("direct:uet-auth");	
//		rest("/rest")
//			.get("/uet-calendar").to("direct:uet-courses-calendar");
		rest("/rest")
			.get("/google-calendar").to("direct:google-calendar");
	}
}
