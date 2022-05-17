package edu.nus.iss.vttpfinalapplication;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.nus.iss.vttpfinalapplication.filters.AutheticationFilter;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<AutheticationFilter> filterRegistrationBean() {
        
        AutheticationFilter filter = new AutheticationFilter();
        FilterRegistrationBean<AutheticationFilter> registrationFilter = new FilterRegistrationBean<>();

        registrationFilter.setFilter(filter);
        registrationFilter.addUrlPatterns("/protected/*");

        return registrationFilter;
    }
    
}
