package com.solfugasoft.photohosting;


import com.solfugasoft.photohosting.model.AppUser;
import com.solfugasoft.photohosting.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private AppUserRepo appUserRepo;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AppUserRepo appUserRepo) {
        this.userDetailsService = userDetailsService;
        this.appUserRepo = appUserRepo;
    }

    //definicja uzytkownikow
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // testowanie logowania -> auth.inMemoryAuthentication().withUser(new User("Adam", passwordEncoder().encode("pas123"), Collections.singleton(new SimpleGrantedAuthority("user"))));
        //obsługa logowania uzytkowanika z bazy danych
        auth.userDetailsService(userDetailsService);
    }

    // definicja endpointow
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/test1").authenticated()
                .and()
                .formLogin().permitAll(); //dostep dla wszystkich
    }

    @Bean
    //uzyty interface pozwala na szyfrowanie hasla
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        AppUser appUser = new AppUser("Adam1", passwordEncoder().encode("Adam1"), "USER");
        appUserRepo.save(appUser);
    }


}
