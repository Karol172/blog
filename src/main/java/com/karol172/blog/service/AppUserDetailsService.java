package com.karol172.blog.service;

import com.karol172.blog.model.Permission;
import com.karol172.blog.model.User;
import com.karol172.blog.model.UserGroup;
import com.karol172.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
@Transactional
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(s);

        if (user == null)
            throw new UsernameNotFoundException("Użytkownik nie znaleziony.");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getActivated(), true, true, true, getAuthorities(user.getUserGroup()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserGroup userGroup) {
        return getGrantedAuthorities(userGroup.getPermissions());
    }

    private Collection<GrantedAuthority> getGrantedAuthorities (Collection<Permission> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        permissions.forEach( p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
        return authorities;
    }
}
