package hodei.naiz.teammorale.service.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by Hodei Eceiza
 * Date: 1/25/2022
 * Time: 00:09
 * Project: TeamMorale
 * Copyright: MIT
 */
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth implements UserDetails {
    private String userEmail;
    private String password;
    private String authority;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;//TODO: check this when building DB.
    }
}
