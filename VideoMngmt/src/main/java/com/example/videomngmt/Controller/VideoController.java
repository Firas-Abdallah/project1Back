package com.example.videomngmt.Controller;

import com.example.videomngmt.Entity.Video;
import com.example.videomngmt.Service.VideoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping("/getAllVideos")
    public List<Video> getAllVideos() {
        return videoService.getAllVideos().stream()
                .peek(video -> video.setVideoUrl("/videos/" + video.getVideoUrl()))
                .collect(Collectors.toList());
    }


    @GetMapping("getVideoById/{id}")
    public Video getVideoById(@PathVariable Long id) {
        return videoService.getVideoById(id);
    }

    @PostMapping("createVideo")
    public Video createVideo(@RequestBody Video video) {
        return videoService.saveVideo(video);
    }

    @PutMapping("updateVideo/{id}")
    public Video updateVideo(@PathVariable Long id, @RequestBody Video video) {
        video.setVideoId(id);
        return videoService.saveVideo(video);
    }

    @DeleteMapping("deleteVideo/{id}")
    public void deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
    }

    @PostMapping("/addVideo")
    public ResponseEntity<Response> addVideo(@ModelAttribute Video video1,
                                                                          @RequestParam(value = "video", required = false) MultipartFile video ){
        try {
            // Check if a video file is provided
            if (video != null && !video.isEmpty()) {
                // Save the uploaded video file and get the video URL
                String videoUrl = saveVideo(video);
                // Set the video URL for the blog
                video1.setVideoUrl(videoUrl);
            }

            // Save the blog
            Video saveVideo = videoService.saveVideo(video1);

            if (saveVideo != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to save the uploaded video file and return the video URL
    private String saveVideo(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        // Define the directory path relative to the application's root directory
        String directoryPath = System.getProperty("user.dir") + "/src/main/webapp/video/";

        // Create the directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // Creates directories if they do not exist
            if (!created) {
                // Log or handle the error if directory creation fails
                throw new IOException("Failed to create directory: " + directoryPath);
            }
        }

        File serverFile = new File(directoryPath + originalFilename);
        FileUtils.writeByteArrayToFile(serverFile, file.getBytes());

        return originalFilename;
}
    @GetMapping(path = "/Video/{videoId}", produces = "video/mp4")
    public ResponseEntity<byte[]> getVideo(@PathVariable("videoId") Long videoId) throws Exception {
        byte[] videoContent = videoService.getvidId(videoId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("video/mp4"));
        return new ResponseEntity<>(videoContent, headers, HttpStatus.OK);
    }


}
