package xyz.acproject.blogs.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//@Configuration
public class SSLConfig {
//	@Bean
//	public ServletWebServerFactory servletContainer() {
//		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory()
//		{
//			@Override
//			protected void postProcessContext(Context context) {
//				// TODO 自动生成的方法存根
//				SecurityConstraint securityConstraint=new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection=new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//			}
//		};
//		tomcat.addAdditionalTomcatConnectors(httpsConnector());
//		return tomcat;
//	}
//	@Bean
//	public Connector httpsConnector() {
//		//tomcat运行协议
//		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//		connector.setScheme("http");
//		//Connector监听的http的端口号
//		connector.setSecure(false);
//		connector.setPort(8080);
//		//监听到http的端口号后转向到的https的端口号
//		connector.setRedirectPort(8443);
//		return connector;
//	}
}
