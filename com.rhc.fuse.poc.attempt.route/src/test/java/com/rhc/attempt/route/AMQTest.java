/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhc.attempt.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author dfreese
 */
@Ignore
public class AMQTest extends CamelTestSupport {
     @Produce
    private ProducerTemplate producer;
     

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        AmqCamelComponent amq = new AmqCamelComponent();
        registry.bind("amq", amq.resolveComponent("", context));
        return registry;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:q1")
                        .setExchangePattern(ExchangePattern.InOnly)
                        .to("amq:queue:attemptQueue");
            }
        };
    }

    @Test
    public void testBlast() {
        for (int i = 0; i < 3; i++) {
            producer.sendBody("direct:q1", "foo1");
            producer.sendBody("direct:q1", "foo2");
            producer.sendBody("direct:q1", "foo3");


        }
    }
    
}
