package com.springbatch.demo.springbatchapplication.writer;

import com.springbatch.demo.springbatchapplication.model.User;
import com.springbatch.demo.springbatchapplication.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserItemWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void write(List<? extends User> list) throws Exception {
        userRepository.saveAllAndFlush(list);
    }
}
