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
import java.time.LocalDateTime;
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

            // ================= PUBLIC URLS =================
            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            // ================= READ JWT =================
            String token = extractTokenFromCookies(request);

            System.out.println("=======================================");
            System.out.println("REQUEST : " + request.getRequestURI());
            System.out.println("TOKEN   : " + token);

            if (token == null || token.isBlank()) {
                System.out.println("JWT token not found.");
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            // ================= DATABASE SESSION =================
            Optional<UserSession> optionalSession =
                    userSessionRepository.findByJwtTokenAndIsActiveTrue(token);

            if (optionalSession.isEmpty()) {
                System.out.println("Token not found in UserSession table.");

                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            UserSession session = optionalSession.get();

            if (session.getExpiresAt() != null &&
                    session.getExpiresAt().isBefore(LocalDateTime.now())) {

                System.out.println("Session expired.");

                session.setIsActive(false);
                userSessionRepository.save(session);

                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            // ================= AUTHENTICATE =================
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                System.out.println("USERNAME : " + username);
                System.out.println("ROLE     : " + role);

                if (username != null &&
                        role != null &&
                        jwtUtil.validateToken(token, username)) {

                    role = role.trim().toUpperCase();

                    if (!role.startsWith("ROLE_")) {
                        role = "ROLE_" + role;
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role))
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    System.out.println("JWT AUTH SUCCESS -> "
                            + username + " | " + role);

                } else {

                    System.out.println("JWT validation failed.");

                    SecurityContextHolder.clearContext();
                }
            }

        } catch (Exception e) {

            SecurityContextHolder.clearContext();

            System.out.println("JWT AUTH ERROR");
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    // ================= PUBLIC PATHS =================

    private boolean isPublicPath(String path) {

        // Logout should always require authentication
        if ("/auth/logout".equals(path)) {
            return false;
        }

        // Allow all authentication endpoints
        if (path.startsWith("/auth/")) {
            return true;
        }

        return path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/webjars/")
                || path.equals("/favicon.ico")
                || path.equals("/error")
                || path.startsWith("/h2-console");
    }

    // ================= JWT COOKIE =================

    private String extractTokenFromCookies(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {

            if ("jwtToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}