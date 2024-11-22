package com.udacity.jwdnd.course1.cloudstorage.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public File[] getFiles(Integer userId) {
        return fileMapper.getFiles(userId);
    }

    public void addFile(MultipartFile file, Integer userId) throws IOException {
        byte[] fileData = file.getBytes();
        File newFile = new File(file.getOriginalFilename(), file.getContentType(), file.getSize(), userId, fileData);
        fileMapper.insert(newFile);
    }

    public File getFile(String fileName) {
        return fileMapper.getFile(fileName);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public void deleteFile(String fileName) {
        fileMapper.deleteFile(fileName);
    }
}