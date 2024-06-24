package io.tech1.framework.b2b.base.security.jwt.configurations;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.foundation.configurations.mvc.ApplicationMVC;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

@Configuration
public class ApplicationBaseSecurityJwtMvc extends ApplicationMVC {

    @Autowired
    public ApplicationBaseSecurityJwtMvc(ApplicationFrameworkProperties applicationFrameworkProperties) {
        super(applicationFrameworkProperties);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        var basePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();
        configurer.addPathPrefix(basePathPrefix, resource -> resource.isAnnotationPresent(AbstractFrameworkBaseSecurityResource.class));
    }
}
