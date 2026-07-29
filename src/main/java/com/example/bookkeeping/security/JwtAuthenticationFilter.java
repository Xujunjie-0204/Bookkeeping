package com.example.bookkeeping.security;

import com.example.bookkeeping.system.user.entity.SysUser;
import com.example.bookkeeping.system.user.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SysUserMapper sysUserMapper;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, SysUserMapper sysUserMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Long userId = jwtTokenProvider.getUserId(token);
                SysUser user = sysUserMapper.selectById(userId);
                if (user != null && user.getStatus() != null && user.getStatus().intValue() == 1) {
                    UserPrincipal principal = new UserPrincipal(
                            user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            user.getNickname(),
                            user.getStatus()
                    );
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                log.debug("JWT 解析失败: {}", ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
