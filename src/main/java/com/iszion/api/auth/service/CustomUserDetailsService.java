package com.iszion.api.auth.service;

import com.iszion.api.auth.dto.CustomUserDetails;
import com.iszion.api.auth.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails userDetails = authMapper.getUserId(username);

        if (userDetails == null) {
            throw new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.");
        }


        return new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(),
                userDetails.getPassword(), true, true, true, true,
                getRoles(userDetails.getRole())
        );
    }

    public Collection<? extends GrantedAuthority> getRoles(String role) {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(role));
        return auth;
    }
}
