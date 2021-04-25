package springbootapplication.service;

import springbootapplication.model.User;
import java.util.List;

public interface UserService {
    void save(User user, String [] roles);

    void delete(int id);

    void update(User user, String [] roles);

    void encodePassword(User user);

    List<User> getAllUsers();

    User findById(int id);

    User findByEmail(String email);
}
