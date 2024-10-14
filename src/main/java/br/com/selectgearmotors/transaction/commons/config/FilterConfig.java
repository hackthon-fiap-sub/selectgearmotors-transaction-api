package br.com.selectgearmotors.transaction.commons.config;

import br.com.selectgearmotors.transaction.commons.filter.JwtRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> loggingFilter() {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtRequestFilter());
        registrationBean.addUrlPatterns("/vehicles/*"); // Defina as rotas necessárias
        return registrationBean;
    }
}