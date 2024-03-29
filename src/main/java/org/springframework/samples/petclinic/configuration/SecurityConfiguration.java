package org.springframework.samples.petclinic.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/", "/oups").permitAll()
				.antMatchers("/session/**").permitAll()
				.antMatchers("/admin/**").hasAnyAuthority("admin")
				.antMatchers("/jugadores/new").hasAnyAuthority("admin")
				.antMatchers("/registerNewJugador").anonymous()
				.antMatchers("/owners/**").hasAnyAuthority("admin")
				.antMatchers("/jugadores/list/{page}").hasAnyAuthority("admin")
				.antMatchers("/jugadores/{jugadorId}").hasAnyAuthority("admin", "jugador")
				.antMatchers("/jugadores/{jugadorId}/delete").hasAnyAuthority("admin")
				.antMatchers("/jugadores/{jugadorId}/edit").hasAnyAuthority("admin","jugador")
				.antMatchers("/jugadores/{jugadorId}/playerMatches").hasAnyAuthority("jugador")
				.antMatchers("/matches/createMatch").hasAnyAuthority("jugador")
				.antMatchers("/matches/**").hasAnyAuthority("admin","jugador")
				.antMatchers("/chat/**").hasAnyAuthority("admin","jugador")
				.antMatchers("/{idMatch}/currentMatch").hasAnyAuthority("admin","jugador")
				.antMatchers("/statistics/achievements/{page}").hasAnyAuthority("jugador","admin")
				.antMatchers("/statistics/achievements/currentPlayer/{page}").hasAnyAuthority("jugador")
				.antMatchers("/statistics/achievements/admin/**").hasAnyAuthority("admin")
				.antMatchers("/cambiarEstadoOnline").hasAnyAuthority("admin","jugador")
				.antMatchers("/cambiarEstadoOffline").permitAll()
				.antMatchers("/perfil").hasAnyAuthority("jugador")
				.antMatchers("/jugadores/{jugadorId}/playerFriends").hasAnyAuthority("jugador")
				.antMatchers("/jugadores/{jugadorId1}/playerFriends/{jugadorId2}/delete").hasAnyAuthority("jugador")
				.antMatchers("/jugadores/addFriends/**").hasAnyAuthority("jugador")
				.antMatchers("/jugadores/friendRequests/**").hasAnyAuthority("jugador")
				.antMatchers("/invitarAmigo/{id}/{tipoInvitacion}").hasAnyAuthority("jugador")
				.antMatchers("/invitacionesPendientes").hasAnyAuthority("jugador")
				.antMatchers("/rechazarInvitacion/{id}").hasAnyAuthority("jugador")
				.antMatchers("/aceptarInvitacion/{id}").hasAnyAuthority("jugador")
				.anyRequest().denyAll()
				.and()
				 	.formLogin().defaultSuccessUrl("/cambiarEstadoOnline")
				 	/*.loginPage("/login")*/
				 	.failureUrl("/login-error")
				.and()
					.logout()
						.logoutSuccessUrl("/"); 
                // Configuración para que funcione la consola de administración 
                // de la BD H2 (deshabilitar las cabeceras de protección contra
                // ataques de tipo csrf y habilitar los framesets si su contenido
                // se sirve desde esta misma página.
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
	}

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled " + "from users " + "where username = ?")
                .authoritiesByUsernameQuery("select users.username, authority from authorities, Users where users.username = ? and users.id=authorities.username")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }

}
