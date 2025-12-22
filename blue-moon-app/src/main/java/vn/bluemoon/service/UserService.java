package vn.bluemoon.service;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.dto.RegisterRequest;
import vn.bluemoon.model.dto.UserSearchRequest;
import vn.bluemoon.model.entity.User;
import vn.bluemoon.repository.UserRepository;
import vn.bluemoon.util.PasswordHasher;
import vn.bluemoon.validation.ValidationException;
import vn.bluemoon.validation.Validators;

import java.util.List;

/**
 * User service
 */
public class UserService {
    private final UserRepository userRepository = new UserRepository();

    /**
     * Register new user
     * @param request Registration request
     * @return Created user
     * @throws ValidationException if validation fails
     * @throws DbException if database error occurs
     */
    public User register(RegisterRequest request) throws ValidationException, DbException {
        // Validate input
        Validators.validateUsername(request.getUsername());
        Validators.validateEmail(request.getEmail());
        Validators.validateRequired(request.getFullName(), "Họ và tên");
        Validators.validatePassword(request.getPassword());

        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Tên đăng nhập đã tồn tại");
        }

        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email đã tồn tại");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(PasswordHasher.hash(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setIsActive(true);
        user.setMustChangePassword(false);

        return userRepository.create(user);
    }

    /**
     * Search users
     * @param request Search request
     * @return List of users
     * @throws DbException if database error occurs
     */
    public List<User> searchUsers(UserSearchRequest request) throws DbException {
        return userRepository.search(
            request.getUsername(),
            request.getEmail(),
            request.getFullName(),
            request.getPhone()
        );
    }

    /**
     * Update user
     * @param user User to update
     * @throws DbException if database error occurs
     */
    public void updateUser(User user) throws DbException {
        userRepository.update(user);
    }

    /**
     * Disable user account
     * @param userId User ID
     * @throws DbException if database error occurs
     */
    public void disableUser(Integer userId) throws DbException {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setIsActive(false);
            userRepository.update(user);
        }
    }

    /**
     * Enable user account
     * @param userId User ID
     * @throws DbException if database error occurs
     */
    public void enableUser(Integer userId) throws DbException {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setIsActive(true);
            userRepository.update(user);
        }
    }

    /**
     * Require password change for user
     * @param userId User ID
     * @throws DbException if database error occurs
     */
    public void requirePasswordChange(Integer userId) throws DbException {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setMustChangePassword(true);
            userRepository.update(user);
        }
    }
}




