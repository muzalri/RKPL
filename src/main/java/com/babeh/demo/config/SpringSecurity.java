package com.babeh.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .authorizeHttpRequests((authorize) ->
        authorize.requestMatchers("/register/**","/js/**", "/css/**","/gambar/**").permitAll()
        .requestMatchers("/").hasAnyRole("ADMIN","MANAGER","KASIR")
        .requestMatchers("/users").hasRole("ADMIN")
        .requestMatchers("/edit/**").hasRole("ADMIN")
        .requestMatchers("/error/**").permitAll()  
        .requestMatchers("/delete/**").hasRole("ADMIN")
        .requestMatchers("/menu/**").hasRole("MANAGER")
        .requestMatchers("get-transaksi").hasRole("MANAGER")
        .requestMatchers("/laporan/**").hasRole("MANAGER")
        .requestMatchers("/transaksi/**").hasAnyRole("KASIR","MANAGER")
        .requestMatchers("/api/**").hasRole("KASIR")
)
            .formLogin(
                form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login") 
                    .defaultSuccessUrl("/")
                    .permitAll()
            )
            .logout(
                logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll()
            );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }
}
