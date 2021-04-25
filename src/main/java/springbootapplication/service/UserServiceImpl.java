package springbootapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springbootapplication.dao.UserRepository;
import springbootapplication.model.Role;
import springbootapplication.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Qualifier("UserServiceImpl")
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user, String[] roles) {
        user.setActive(true);
        for (String s : roles) {
            if (s.equals("Admin")) {
                user.addRoleToUser(new Role("ROLE_ADMIN"));
            } else if (s.equals("User")) {
                user.addRoleToUser(new Role("ROLE_USER"));
            }
        }
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(User user, String[] roles) {

        user.deleteRoles();
        user.setActive(true);
        for (String s : roles) {
            if (s.equals("Admin")) {
                user.addRoleToUser(new Role("ROLE_ADMIN"));
            } else if (s.equals("User")) {
                user.addRoleToUser(new Role("ROLE_USER"));
            }
        }
        userRepository.save(user);
    }

    @Override
    public void encodePassword(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new org.springframework.security.core.userdetails.User
                (user.getEmail(), user.getPassword(), grantedAuthorities);
    }
}
