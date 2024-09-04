package com.example.videomngmt.Repo;

import com.example.videomngmt.Entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepo extends JpaRepository<Video, Long> {
}
