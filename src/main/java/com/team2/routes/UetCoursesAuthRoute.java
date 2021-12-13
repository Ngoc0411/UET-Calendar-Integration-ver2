package com.team2.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.team2.model.UetAuthInfo;
import com.team2.model.UetAuthToken;
import com.team2.model.UetExportToken;

public class UetCoursesAuthRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		onException(Exception.class)
			.handled(true)
			.to("direct:rest-response/failure");
		
		from("direct:uet-auth")
		//Get uet auth token:
		.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
		.to("https://courses.uet.vnu.edu.vn/login/token.php?service=moodle_mobile_app&bridgeEndpoint=true")
		.unmarshal(new JacksonDataFormat(UetAuthToken.class))
		.process(e -> e.getIn().setHeader("wstoken", e.getIn().getBody(UetAuthToken.class).getToken()))
		.to("log:com.team2.routes?level=INFO")
		.to("direct:rest-response-uet-auth-token/success", "file:src/data/?fileName=uet_auth_token.json")
		
		//Get uet auth info:
		.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
		.process(e -> e.getIn().setBody("wstoken=" + e.getIn().getHeader("wstoken")
				  + "&wsfunction=core_webservice_get_site_info"))
		.to("https://courses.uet.vnu.edu.vn/webservice/rest/server.php?moodlewsrestformat=json&bridgeEndpoint=true")
		.unmarshal(new JacksonDataFormat(UetAuthInfo.class))
		.process(e -> e.getIn().setHeader("userid", e.getIn().getBody(UetAuthInfo.class).getUserid()))
		.to("log:com.team2.routes?level=INFO")
		.to("direct:rest-response-uet-auth-info/success", "file:src/data/?fileName=uet_auth_info.json")
		
		.process(e -> e.getOut().setBody("Đăng nhập thành công, thông tin đăng nhập đã được lưu vào hệ thống!"))
		.to("log:com.team2.routes?level=INFO");
	}

}
