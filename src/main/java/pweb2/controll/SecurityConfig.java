package pweb2.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        return mailSender;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                //todo mundo tem acesso
                        .requestMatchers(
                                "/","/home","/login",
                                "/forgot-password","/reset-senha",

                                /*=== Cadastro de clientes ===*/
                                "/clientes/novo","/clientes/salvar"
                        ).permitAll()
                        /*== Produtos*/
                                .requestMatchers(
                                        "/produtos", "/produtos/adicionar-no-carrinho",
                                        "/produtos/carrinho", "/produtos/remover-do-carrinho/"
                                ).permitAll()
                        /*  ADMIN */
                                .requestMatchers(
                                        "/produtos/novo", "/produtos/editar/**","/produtos/deletar/",
                                        "/departamentos/**","/departamentos"
                                ).hasAuthority("ROLE_ADMIN")
                        /*== VENDAS ==*/
                                .requestMatchers(
                                        "/vendas/finalizar","/vendas/minhas-compras"
                                ).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                .requestMatchers(
                                        "/vendas/novo","/vendas/adicionar-item",
                                        "/vendas/deletarItem","/vendas/salvar"
                                ).hasAnyAuthority("ROLE_ADMIN")
                        /*== CLIENTES (ADMINISTRADOR*/
                                .requestMatchers(
                                        "/clientes","/clientes/editar/**","clientes/remove/**"
                                ).hasAnyAuthority("ROLE_ADMIN")
                        /* QUALUQER UM*/
                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/produtos", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AndRequestMatcher())
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
