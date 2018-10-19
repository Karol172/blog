package com.karol172.blog.service;

import com.karol172.blog.dto.FileDto;
import com.karol172.blog.exception.StorageException;
import com.karol172.blog.exception.StorageFileNotFoundException;
import com.karol172.blog.form.FileForm;
import com.karol172.blog.model.File;
import com.karol172.blog.model.User;
import com.karol172.blog.repository.FileRepostiory;
import com.karol172.blog.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilesService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private FileRepostiory fileRepostiory;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Resource> getImage (String filename) {
        try {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + storageService.loadAsResource(filename).getFilename() + "\"")
                    .body(storageService.loadAsResource(filename));
        } catch (StorageFileNotFoundException e) {
            return null;
        }
    }

    public void getImages (Model model) {
        List<FileDto> list = new ArrayList<>();
        fileRepostiory.findByOrderByAdditionDateDesc().forEach(f->list.add(new FileDto(f)));
        model.addAttribute("files", list);
        model.addAttribute("fileForm", new FileForm(LocalDateTime.now()));
    }

    public void addImage (FileForm fileForm, Model model, BindingResult result, HttpServletRequest request) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        User user = userRepository.findFirstByUsername(request.getRemoteUser());
        if (!result.hasErrors() && user != null) {
            LocalDateTime localDateTime = fileForm.getAdditionDate();
            String filename = new StringBuilder(new String("file"))
                    .append(user.getId())
                    .append(localDateTime.getYear())
                    .append(localDateTime.getMonthValue())
                    .append(localDateTime.getDayOfMonth())
                    .append(localDateTime.getHour())
                    .append(localDateTime.getMinute())
                    .append(localDateTime.getSecond()).append('.')
                    .append(FilenameUtils.getExtension(fileForm.getImage().getOriginalFilename())).toString();
            File file = new File(filename, localDateTime, userRepository.findFirstByUsername(request.getRemoteUser()));
            if (fileRepostiory.findFirstByFileName(file.getFileName()) == null) {
                try {
                    storageService.store(fileForm.getImage(), file.getFileName());
                    fileRepostiory.save(file);
                    model.addAttribute("successInfo", "Pomyślnie dodano obrazka.");
                } catch (StorageException e) {
                    model.addAttribute("failureInfo", e.getMessage());
                }
            }
        }
        getImages(model);
    }

    public void removeImage (long idImage, Model model) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        File file = fileRepostiory.findFirstById(idImage);
        if (file != null) {
            try {
                storageService.delete(file.getFileName());
                fileRepostiory.delete(file);
                model.addAttribute("successInfo", "Pomyślnie usunięto obrazek.");
            } catch (StorageException e) {
                model.addAttribute("failureInfo", "Wystąpił błąd przy usuwaniu obrazka.");
            }
        }
        getImages(model);
    }
}
