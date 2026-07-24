package EmployeeManagementSystem.jwt;

import EmployeeManagementSystem.entity.UserSession;
import EmployeeManagementSystem.repository.UserSessionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserSessionRepository userSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        try {

            // ================= SAFE SKIP =================
            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            // ================= TOKEN EXTRACTION =================
            String token = extractTokenFromCookies(request);

            // IMPORTANT: NEVER BLOCK REQUEST (NO 403 HERE)
            if (token == null || token.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }
            Optional<UserSession> session =
                    userSessionRepository.findByJwtTokenAndIsActiveTrue(token);

            if(session.isEmpty()){
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request,response);
                return;
            }

            // ================= AUTH CHECK =================
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                if (username != null && role != null
                        && jwtUtil.validateToken(token, username)) {

                    // normalize role
                    role = role.trim().toUpperCase();

                    if (!role.startsWith("ROLE_")) {
                        role = "ROLE_" + role;
                    }

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role))
                            );

                    auth.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    System.out.println("JWT AUTH SUCCESS -> " + username + " | " + role);
                }
            }

        } catch (Exception e) {

            // IMPORTANT: NEVER THROW 403 OR STOP REQUEST
            SecurityContextHolder.clearContext();

            System.out.println("JWT AUTH ERROR: " + e.getMessage());
        }

        // ALWAYS CONTINUE CHAIN
        filterChain.doFilter(request, response);
    }

    // ================= PUBLIC PATHS =================
    private boolean isPublicPath(String path) {
        if (path.equals("/auth/logout")) {
            return false;
        }

        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/webjars/")
                || path.equals("/error")
                || path.equals("/access-denied")
                || path.startsWith("/h2-console"); // optional
    }

    // ================= COOKIE TOKEN =================
    private String extractTokenFromCookies(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("jwtToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}