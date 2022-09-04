package com.springbatch.demo.springbatchapplication.repository;

import com.springbatch.demo.springbatchapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
