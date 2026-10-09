package com.ainexus.config; import org.springframework.context.annotation.Configuration; import org.springframework.web.servlet.config.annotation.*;
@Configuration public class WebConfig implements WebMvcConfigurer { private final AuthInterceptor auth; public WebConfig(AuthInterceptor a){auth=a;} @Override public void addInterceptors(InterceptorRegistry r){r.addInterceptor(auth);} }
