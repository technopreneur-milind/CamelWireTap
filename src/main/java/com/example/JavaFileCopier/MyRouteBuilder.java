package com.example.JavaFileCopier;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file:F:/data/inbox?noop=true").wireTap("direct:logging").choice().when(header("CamelFileName").endsWith(".xml"))
				.to("direct:xmlOrders").when(header("CamelFileName").endsWith(".doc")).to("direct:docOrders")
				.otherwise().to("direct:errorOrders");
		
		from("direct:logging").process(new Processor() {

			public void process(Exchange exchange) throws Exception {
				System.out.println("Logging the orders "
						 + exchange.getIn().getHeader("CamelFileName"));
			}
		});
		

		from("direct:xmlOrders").process(new Processor() {

			public void process(Exchange exchange) throws Exception {
				System.out.println("Received XML order: "
						 + exchange.getIn().getHeader("CamelFileName"));
			}
		});
		
		from("direct:docOrders").process(new Processor() {

			public void process(Exchange exchange) throws Exception {
				System.out.println("Received doc order: "
						 + exchange.getIn().getHeader("CamelFileName"));
			}
		});
		from("direct:errorOrders").process(new Processor() {

			public void process(Exchange exchange) throws Exception {
				System.out.println("Received Bad order: "
						 + exchange.getIn().getHeader("CamelFileName"));
			}
		});
	}
}
