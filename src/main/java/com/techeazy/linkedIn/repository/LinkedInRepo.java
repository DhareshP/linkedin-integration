package com.techeazy.linkedIn.repository;

import com.techeazy.linkedIn.entity.LinkedInPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedInRepo extends JpaRepository<LinkedInPost , Integer> {
}
