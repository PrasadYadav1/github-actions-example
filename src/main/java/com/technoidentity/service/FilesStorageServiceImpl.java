package com.technoidentity.service;

import com.technoidentity.entity.User;
import com.technoidentity.repository.UserRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FilesStorageServiceImpl implements FilesStorageService {

  @Autowired private final UserRepository userRepository;

  private final Path root = Paths.get("src/main/resources/images");

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(String folderName, MultipartFile file, String fileName) {
    User user;
    try {
      Files.copy(
          file.getInputStream(),
          this.root.resolve(Paths.get(folderName + "/" + fileName)).normalize().toAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String folderName, String fileName) {
    try {
      Path file = root.resolve(Paths.get(folderName + "/" + fileName)).normalize().toAbsolutePath();
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void delete(String folderName, String fileName) {
    try {
      Path file = root.resolve(Paths.get(folderName + "/" + fileName)).normalize().toAbsolutePath();
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        FileSystemUtils.deleteRecursively(file.toFile());
      } else {
        throw new RuntimeException("Could not delete the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll(String folderName) {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll(String folderName) {
    try {

      return Files.walk(this.root, 1)
          .filter(path -> !path.equals(this.root))
          .map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not delete the files!");
    }
  }
}
