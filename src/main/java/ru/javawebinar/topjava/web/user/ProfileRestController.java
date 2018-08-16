package ru.javawebinar.topjava.web.user;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.AuthorizedUser.id;

@Controller
public class ProfileRestController extends AbstractUserController {

    public User get() {
        return super.get(id());
    }

    public void delete() {
        super.delete(id());
    }

    public void update(User user) {
        super.update(user, id());
    }
}