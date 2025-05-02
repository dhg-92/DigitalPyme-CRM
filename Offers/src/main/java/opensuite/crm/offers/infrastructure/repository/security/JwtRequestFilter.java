package opensuite.crm.offers.infrastructure.repository.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        User username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            jwt = authorizationHeader.substring(7);
            if (!JwtUtil.validateToken(jwt)) {
                response = new ExceptionHandler().InvalidToken(response);
                return;
            }

            username = JwtUtil.extractUsername(jwt);
            if (username == null) {
                response = new ExceptionHandler().InvalidToken(response);
                return;
            }

            List<GrantedAuthority> authorities = new ArrayList<>();

            if (username.getIsAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username.getEmail(), null, authorities);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request, response);
    }
}