package br.com.dswbackend.config;

import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dswbackend.dtos.Login;
import br.com.dswbackend.exceptions.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends OncePerRequestFilter {

  private AuthenticationManager authenticationManager;

  public AuthFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (request.getServletPath().contains("/api/v1/usuario/login")) {
      try {
        Login login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
        Authentication auth = authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(login.email(), login.senha()));
        var user = (User) auth.getPrincipal();
        String tokenJWT = JWT.create()
            .withSubject(user.getUsername())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
            .sign(Algorithm.HMAC512("secret"));
        response.setHeader("Authorization", "Bearer " + tokenJWT);
      } catch (BadCredentialsException e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(new ObjectMapper().writeValueAsString(
            new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value())));
      }
      filterChain.doFilter(request, response);
      return;
    }

    String token = request.getHeader("Authorization");

    if (token.isBlank() || !token.startsWith("Bearer ")) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      filterChain.doFilter(request, response);
      return;
    }

    token = token.split(" ")[1];
    try {
      JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512("secret")).build();
      DecodedJWT decoded = jwtVerifier.verify(token);
      String subject = decoded.getSubject();
      var auth = UsernamePasswordAuthenticationToken.authenticated(subject, null, NO_AUTHORITIES);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } catch (Exception e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(MediaType.APPLICATION_JSON.toString());
      response.getWriter().write(new ObjectMapper().writeValueAsString(
          new ErrorResponse("Token inválido", HttpStatus.UNAUTHORIZED.value())));
      return;
    }
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return request.getServletPath().contains("/api/v1/usuario/create")
        || request.getServletPath().contains("/api/v1/usuario/forgot")
        || request.getServletPath().contains("/api/v1/usuario/reset");
  }

}
