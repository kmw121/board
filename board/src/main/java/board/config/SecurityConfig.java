package board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity //해당 파일로 시큐리티를 활성화.
@Configuration //IoC
public class SecurityConfig {


	   @Bean
	    public BCryptPasswordEncoder encoder() {
	        return new BCryptPasswordEncoder();
	    }
	   
	   
	   @Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		   
		   http.csrf().disable();
		   
	       http.authorizeHttpRequests()
	                .anyRequest().permitAll()
	                .and()
	                .formLogin()
	                .loginPage("/auth/signin")
	                .loginProcessingUrl("/auth/signin")
	                .defaultSuccessUrl("/");
	       http.logout()
	       		.logoutUrl("/logout")
	       		.logoutSuccessUrl("/");
	        
	     return http.build();

	    }
}
	   


