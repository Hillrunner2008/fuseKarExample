package com.rhc.attempt.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.ComponentResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dcnorris
 */
@Component(immediate = true, service = RouteBuilder.class)
public class AttemptRoute extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(AttemptRoute.class);

    @Override
    public void configure() throws Exception {
        from("timer://demo?delay=2000&fixedRate=true")
                .setBody(simple("Hello Camel"))
                .log("${body}");
    }

    @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policyOption = ReferencePolicyOption.GREEDY,
            target = "(component=jms)")
    public void setJmsComponent(ComponentResolver componentResolver) {
        LOG.info("jms component resolved");
    }

}
