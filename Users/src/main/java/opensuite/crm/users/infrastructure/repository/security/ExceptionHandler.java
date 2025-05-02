package opensuite.crm.users.infrastructure.repository.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class ExceptionHandler {

    protected static String messageContentNotPermission = "{\"error\": \"Access Denied\", \"message\": \"You do not have permission to access this resource.\"}";
    protected static String messageContentInvalidCredentials = "{\"error\": \"Access Denied\", \"message\": \"Invalid credentials.\"}";

    protected static String contentType = "application/json";

    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(contentType);
            response.getWriter().write(messageContentNotPermission);
        }
    }

    public static class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
                throws IOException {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(contentType);
            response.getWriter().write(messageContentNotPermission);
        }
    }


    public HttpServletResponse InvalidToken(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(contentType);
        response.getWriter().write(messageContentInvalidCredentials);;
        return response;
    }
}
