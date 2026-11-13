import com.lostandfound.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    private User user;

    @Test
    void testConstructorGetters(){
        user = User.of("Student", "A", "1234", "iekls@mail.com");

        assertEquals("Student", user.getRole());
        assertEquals("A", user.getUsername());
        assertEquals("1234", user.getPassword());
        assertEquals("iekls@mail.com", user.getEmail());
    }
}