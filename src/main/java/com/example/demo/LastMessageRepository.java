package com.example.demo;

import com.example.demo.models.LastMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;


/**
 * Created by albertchubakov on 07.03.2018.
 */
public interface LastMessageRepository extends CrudRepository<LastMessage, Long> {



}
