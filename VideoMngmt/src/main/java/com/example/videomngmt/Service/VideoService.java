package com.example.videomngmt.Service;

import com.example.videomngmt.Entity.Video;
import com.example.videomngmt.Repo.VideoRepo;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepo videoRepo;
    final ServletContext context;


    public List<Video> getAllVideos() {
        return videoRepo.findAll();
    }

    public Video getVideoById(Long id) {
        return videoRepo.findById(id).orElse(null);
    }

    public Video saveVideo(Video video) {
        return videoRepo.save(video);
    }

    public void deleteVideo(Long id) {
        videoRepo.deleteById(id);
    }

    public byte[] getvidId(Long videoId) throws Exception {
        Video video = videoRepo.findById(videoId).orElseThrow(() -> new Exception("Video for found"));
        return Files.readAllBytes(Paths.get(context.getRealPath("/video/") + video.getVideoUrl()));
   }
}
