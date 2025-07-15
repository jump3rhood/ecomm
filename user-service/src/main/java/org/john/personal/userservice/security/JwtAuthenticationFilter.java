package org.john.personal.userservice.security;

import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.john.personal.userservice.config.JwtUserPrincipal;
import org.john.personal.userservice.services.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Step 1: Extract JWT from request
        String jwt = getJwtFromRequest(request);
        System.out.println(jwt);

        // Step 2: If no JWT, continue with filter chain
        if(jwt == null){
            filterChain.doFilter(request, response);
            return;
        }
        // Step 3: Validate and extract claims
        JWTClaimsSet claims = jwtService.validateTokenAndExtractClaims(jwt);

        // Step 4: If invalid, continue with filter chain
        if(claims == null){
            filterChain.doFilter(request, response);
            return;
        }
        // Step 5: Create custom authentication object
        String username = claims.getSubject();
        Long userId = jwtService.extractUserId(claims);
        String firstName = jwtService.extractFirstName(claims);
        String lastName = jwtService.extractLastName(claims);
        Collection<SimpleGrantedAuthority> authorities = jwtService.extractAuthorities(claims);
        JwtUserPrincipal principal = new JwtUserPrincipal(username, userId, firstName, lastName, authorities );


        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, null, authorities
        );

        // Step 6: Set security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Step 7: continue with filter chain
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        System.out.println("from JwtAuthFilter: No Token in header!!!");
        return null;
    }
}
