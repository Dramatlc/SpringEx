package com.example.springproject.repository;

import com.example.springproject.entity.Article;
import com.example.springproject.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface MemberRepository extends CrudRepository<Member,Long> {
    @Override
    ArrayList<Member> findAll();
}
