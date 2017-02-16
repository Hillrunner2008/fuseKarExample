package com.rhc.attempt.route;

import org.apache.camel.LoggingLevel;
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
@Component(service = RouteBuilder.class)
public class AttemptRoute extends RouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(AttemptRoute.class);

    @Override
    public void configure() throws Exception {
        
        from("amq:queue:attemptQueue").routeId(AttemptRoute.class.getCanonicalName()+"-attemptqueue").log(LoggingLevel.INFO,"${body}");    
    }

    @Reference(cardinality = ReferenceCardinality.MANDATORY,
            policyOption = ReferencePolicyOption.GREEDY,
            target = "(component=amq)")
    public void setAmQComponent(ComponentResolver componentResolver) {
        LOG.info("amq component resolved");
    }
}
