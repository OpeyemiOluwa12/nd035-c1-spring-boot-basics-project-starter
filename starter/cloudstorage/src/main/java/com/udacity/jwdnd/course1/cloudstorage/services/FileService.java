package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FilesMapper filesMapper;


    public FileService(FilesMapper filesMapper) {
        this.filesMapper = filesMapper;
    }

    public int uploadFiles(MultipartFile multipartFile, Integer userId) {
        String contentType = multipartFile.getContentType();
        String fileName = multipartFile.getOriginalFilename();
        String fileSize = String.valueOf(multipartFile.getSize());
        byte[] fileData = null;
        try {
            fileData = multipartFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Files files = new Files(null, fileName, contentType, fileSize, userId, fileData);
        return filesMapper.insert(files);
    }

    public List<Files> getAllFiles(int userId) {
        return filesMapper.getFilesByUserId(userId);
    }

    public int deleteFile(int fileId) {
        return filesMapper.delete(fileId);
    }

    public Files getSingleFile(int fileId) {
        return filesMapper.getFileByFileId(fileId);
    }

    public boolean isFileNameAvailable(String fileName ){
        return filesMapper.getFileByFileName(fileName) !=null;
    }
}
