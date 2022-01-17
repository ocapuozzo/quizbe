package org.quizbe.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class InternationalizationConfig implements WebMvcConfigurer {

  /**
   * This is where we'll add the interceptor object
   * that handles internationalization
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }

  /**
   * Set encoding and prefix file name of messages resources
   *
   * @return
   */
  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
            new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  private MessageSource messageSourceValidator() {
    ReloadableResourceBundleMessageSource messageSource =
            new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/i18n/validationMessages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSourceValidator());
    return bean;
  }

  /**
   * Instantiate the appropriate locale resolution strategy
   *
   * @return locale resolver
   */
  @Bean
  public LocaleResolver localeResolver() {
    // stores locale info in the session
    SessionLocaleResolver resolver = new SessionLocaleResolver();

    //default to US locale
    resolver.setDefaultLocale(Locale.US);

    return resolver;
  }


  /**
   * This interceptor allows visitors to change the locale
   *
   * @return a LocaleChangeInterceptor object
   */
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();

    //the request param that we'll use to determine the locale
    interceptor.setParamName("lang");

    return interceptor;
  }

}
