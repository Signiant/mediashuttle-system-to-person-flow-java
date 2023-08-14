package com.workflows.service;

import org.openapitools.client.model.FileRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "transferfiles")
public class FileRequestPropsService {
    public List<FileRequest> getFiles() {
        return files;
    }

    public void setFiles(List<FileRequest> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "ApplicationProps{" +
                "files=" + files +
                '}';
    }

    private List<FileRequest> files;
}
