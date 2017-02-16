/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhc.attempt.route;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dcnorris
 */
@Component(immediate = true)
public class CamelRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CamelRunner.class);

    private CamelContext context;

    private Set<RoutesBuilder> routesBuilders;

    public CamelRunner() {
        routesBuilders = Sets.newConcurrentHashSet();
    }

    @Activate
    public void activate(BundleContext bundleContext) throws Exception {
        context = new OsgiDefaultCamelContext(bundleContext);
        context.start();
        routesBuilders.forEach(rb -> {
            try {
                context.addRoutes(rb);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    @Deactivate
    public void deactivate() throws Exception {
        context.stop();
    }

    @Reference(service = RouteBuilder.class,
            cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC,
            policyOption = ReferencePolicyOption.GREEDY, unbind = "unbindRouteBuilder")
    public void addRouteBuilder(RouteBuilder routeBuilder) {
        if (context != null) {
            try {
                context.addRoutes(routeBuilder);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }

        routesBuilders.add(routeBuilder);
    }

    public void unbindRouteBuilder(RouteBuilder routeBuilder) {
        routeBuilder.getRouteCollection().getRoutes().stream().forEach(route -> {
            try {
                context.removeRoute(route.getId());
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }
    
}
