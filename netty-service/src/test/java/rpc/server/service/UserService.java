package rpc.server.service;

import rpc.bo.User;

import java.util.ArrayList;
import java.util.List;

public class UserService implements rpc.api.UserService {
    @Override
    public List<User> list() {
        User user1=new User().setId(1l).setName("张三");
        User user2=new User().setId(2l).setName("李四");
        User user3=new User().setId(3l).setName("王五");

        List<User> users=new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        return users;
    }
}
