package com.karol172.blog.service;

import com.karol172.blog.exception.StorageException;
import com.karol172.blog.exception.StorageFileNotFoundException;
import com.karol172.blog.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;

@Service
public class StorageService {

    private final Path imagesLocation;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Autowired
    public StorageService(StorageProperties properties) {
        this.imagesLocation = Paths.get(properties.getImagesLocation());
        this.init();
    }

    public void store (MultipartFile file, String filename) {
        try {
            if (file.isEmpty())
                throw new StorageException("Plik jest pusty " + filename);
            if (filename.contains(".."))
                throw new StorageException("Błędna nazwa pliku " + filename);
            try (InputStream inputStream = file.getInputStream()) {
                InputStream inputStream2 = new ByteArrayInputStream(file.getBytes());
                if (ImageIO.read(inputStream2) == null)
                    throw new StorageException("To nie jest plik z obrazem");
                Files.copy(inputStream, this.imagesLocation.resolve(StringUtils.cleanPath(filename)),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Wystąpił błąd przy wczytywaniu pliku " + filename, e);
        }
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable())
                return resource;
            else
                throw new StorageFileNotFoundException("Nie można wczytać pliku: " + filename);
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Nie można wczytać pliku: " + filename, e);
        }
    }

    public Path load(String filename) {
        return imagesLocation.resolve(filename);
    }

    public void delete(String fileName){
        try {
            Files.delete(load(fileName));
        }
        catch (IOException e) {
            throw new StorageException("Wystąpił błąd przy usuwaniu plików.");
        }
    }


    private void init() {
        if (!Files.exists(imagesLocation))
            try {
                Files.createDirectories(imagesLocation);
            }
            catch (IOException e) {
                throw new StorageException("Nie można utworzyć katalogu.", e);
            }
    }
}