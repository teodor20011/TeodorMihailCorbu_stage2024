package com.teo.springbootrest;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openSessionInViewFilter() {
        FilterRegistrationBean<OpenEntityManagerInViewFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new OpenEntityManagerInViewFilter());
        registrationBean.addUrlPatterns("/*"); // Specifica i pattern di URL su cui applicare il filtro
        registrationBean.setName("OpenEntityManagerInViewFilter");
        return registrationBean;
    }
}
