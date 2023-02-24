package board.config.auth;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import board.domain.user.User;

@Data
public class PrincipalDetails implements UserDetails {
	 private static final long serialVersionUID = 1L;
	    private User user;
	    private Map<String,Object> attributes;
	    public PrincipalDetails(User user){
	        this.user = user;
	    }

	    public PrincipalDetails(User user,Map<String, Object> attributes){
	        this.user = user;
	        this.attributes = attributes;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {

	        Collection<GrantedAuthority> collector = new ArrayList<>();
	        collector.add(() -> {return user.getRole();});
	        return null;
	    }

	    @Override
	    public String getPassword() {
	        return user.getPassword();
	    }

	    @Override
	    public String getUsername() {
	        return user.getUsername();
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
	        return true;
	    }
}
