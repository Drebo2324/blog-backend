package com.drebo.blog.backend.services;

import com.drebo.blog.backend.security.BlogUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = getToken(request);
            if(token != null) {
                UserDetails userDetails = authenticationService.validateToken(token);

                //create Spring Security authentication token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                //set authentication token in the Spring Security context -> authenticate user for duration of request
                SecurityContextHolder.getContext().setAuthentication(authentication);

                //to access userId throughout request
                if(userDetails instanceof BlogUserDetails) {
                    request.setAttribute("userId", ((BlogUserDetails) userDetails).getId());
                }
            }
        } catch (Exception e) {
            //do not throw exceptions, just don't authenticate
            log.warn("Received invalid authorization token");
        }

        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        //check if authorization header starts with Bearer
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            //remove Bearer from String
            return bearerToken.substring(7);
        }
        return null;
    }
}
