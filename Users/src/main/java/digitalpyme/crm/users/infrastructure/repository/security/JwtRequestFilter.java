package digitalpyme.crm.users.infrastructure.repository.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.users.domain.User;
import digitalpyme.crm.users.domain.services.UserService;
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
import java.util.Optional;

@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JwtRequestFilter(UserService userService) {
        this.userService = userService;
    }

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

            Optional<User> optionalUser = userService.findUserById(username.getIdUser());

            if (optionalUser.isPresent()) {
                User userDetails = optionalUser.get();

                if (username.getEmail().equals(userDetails.getEmail()) && username.getIdUser().equals(userDetails.getIdUser())) {

                    List<GrantedAuthority> authorities = new ArrayList<>();

                    if (userDetails.getIsAdmin()) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    } else {
                        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    }

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails.getEmail(), null, authorities);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        chain.doFilter(request, response);
    }
}