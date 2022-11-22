package com.technoidentity.service;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();

  public void save(String folderName, MultipartFile file, String fileName);

  public Resource load(String folderName, String fileName);

  public void delete(String folderName, String fileName);

  public void deleteAll(String folderName);

  public Stream<Path> loadAll(String folderName);
}
