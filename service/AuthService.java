package service;

import dao.UserDAO;
import model.User;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO;
    private static User currentUser;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public User login(String email, String password, String expectedRole) throws SQLException, AuthenticationException {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new AuthenticationException("Email and password are required.");
        }

        User user = userDAO.findByEmail(email.trim());
        if (user == null) {
            throw new AuthenticationException("Invalid email or password.");
        }

        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password.");
        }

        if (expectedRole != null && !expectedRole.trim().isEmpty()) {
            if (!expectedRole.equalsIgnoreCase(user.getRole())) {
                throw new AuthenticationException("Account role mismatch. You cannot log in as " + expectedRole + ".");
            }
        }

        currentUser = user;
        return user;
    }

    public User register(String fullName, String email, String password, String confirmPassword,
                         String phone, String address, String role) throws SQLException, AuthenticationException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new AuthenticationException("Full name is required.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new AuthenticationException("Email address is required.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new AuthenticationException("Please enter a valid email address.");
        }
        if (password == null || password.isEmpty()) {
            throw new AuthenticationException("Password is required.");
        }
        if (!password.equals(confirmPassword)) {
            throw new AuthenticationException("Passwords do not match.");
        }
        if (userDAO.emailExists(email.trim())) {
            throw new AuthenticationException("An account with this email already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPasswordHash(hashedPassword);
        user.setPhone(phone != null ? phone.trim() : null);
        user.setAddress(address != null ? address.trim() : null);
        user.setRole(role != null ? role.toUpperCase() : "CUSTOMER");

        User created = userDAO.createUser(user);
        currentUser = created;
        return created;
    }

    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
