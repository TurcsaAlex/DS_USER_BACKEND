package com.springjwt.security.jwt;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    //System.out.println("\n\n\n\n\n\n\n\n\n\n"+authException.toString()+"\n\n\n\n\n\n\n\n\n\n"+request.toString()+"\n\n\n\n\n\n\n\n\n\n\n");
    logger.error("Unauthorized error: {}", authException.getMessage());
    logger.error("\n\n\n\n\n\n\n\n\n\n"+authException.toString()+"\n\n\n\n\n\n\n\n\n\n"+request.toString()+"\n\n\n\n\n\n\n\n\n\n\n");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());
    body.put("authType",request.getAuthType());
    Enumeration<String> headerNames = request.getHeaderNames();
    int nr=0;
    if (headerNames != null) {
      while (headerNames.hasMoreElements()) {
        body.put("Header: "+nr, " "+request.getHeader(headerNames.nextElement()));
        nr++;
      }
    }

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }

}
