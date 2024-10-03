package com.akerumort.postservice.repos;

import com.akerumort.postservice.entities.PostViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostViewCountRepository extends JpaRepository<PostViewCount, Long> {
}
