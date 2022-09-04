package com.springbatch.demo.springbatchapplication.processor;

import com.springbatch.demo.springbatchapplication.model.User;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        return user;
    }
}
