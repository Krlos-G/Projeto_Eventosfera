package com.apievento.app.Controller;


import com.apievento.app.Event.Event;
import com.apievento.app.Event.EventRepository;
import com.apievento.app.Event_Image.EventImage;
import com.apievento.app.Event_Image.EventImageRepository;
import com.apievento.app.Event_Image.EventImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final EventImageService eventImageService;
    private final EventImageRepository eventImageRepository;
    private final EventRepository eventRepository;

    public ImageController(EventImageService eventImageService,
                           EventImageRepository eventImageRepository,
                           EventRepository eventRepository) {
        this.eventImageService = eventImageService;
        this.eventImageRepository = eventImageRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping("upload/{event_id}")
    public ResponseEntity<EventImage> uploadImage(
            @PathVariable Long event_id,
            @RequestParam("file")MultipartFile file){
        Event event = eventRepository.findById(event_id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        String fileName = eventImageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/download/")
                .path(fileName)
                .toUriString();

        EventImage eventImage = new EventImage();
        eventImage.setEvent(event);
        eventImage.setImage(fileDownloadUri);
        eventImage.setCreatedAt(new Date());

        EventImage savedImage = eventImageRepository.save(eventImage);

        return ResponseEntity.ok(savedImage);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = eventImageService.loadFileAsResource(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/event/{event_id}")
    public ResponseEntity<List<EventImage>> getEventImages(@PathVariable int event_id) {
        List<EventImage> images = eventImageRepository.findByEventId(event_id);
        return ResponseEntity.ok(images);
    }
}
