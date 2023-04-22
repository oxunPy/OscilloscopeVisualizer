package com.example.program.app.service;

import com.example.program.app.Launch;
import com.example.program.app.entity.OsciFileEntity;
import com.example.program.app.property.OsciFileProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.LogUtil;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import com.example.program.util.exception.SimpleDesktopException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

public class OsciFileService extends BaseService {
    private LogUtil log = LogUtil.getLog(this.getClass());

    private final String IMG_FILE_FOLDER_PATH = Launch.properties.getStr("osci.upload.img.path");
    private final String OSCI_DATA_FOLDER_PATH = Launch.properties.getStr("osci.upload.file.path");

    public OsciFileProperty saveFile(File file, OsciFileEntity.FileType fileType) throws SimpleDesktopException{
        openCurrentSessionWithTransaction();
        OsciFileEntity osciFileEntity = new OsciFileEntity();
        try {
            String originalName = file.getName();
            String newFileName = saveFileAndReturnNewFileName(file, fileType);
            // #saving file in db
            osciFileEntity.setFileType(fileType);
            osciFileEntity.setOriginalName(originalName);
            osciFileEntity.setStatus(EntityStatus.ACTIVE);
            osciFileEntity.setFilename(newFileName);
            osciFileEntity.setCreated(new Date());
            osciFileDao.save(osciFileEntity);
        }
        catch(Exception ex){
            log.print("err.db.saveOrUpdate" + ex);
            closeCurrentSessionWithTransaction();
        }
        closeCurrentSessionWithTransaction();
        return OsciFileProperty.newInstance(osciFileEntity, false);
    }

    public String saveFileAndReturnNewFileName(File file, OsciFileEntity.FileType fileType) {                      // save file
        String path = (fileType == OsciFileEntity.FileType.OSCI_DATA ? OSCI_DATA_FOLDER_PATH : IMG_FILE_FOLDER_PATH);
        String newFileName = "";
        try{
            newFileName = UUID.randomUUID() + "_" + file.getName().replaceAll("[+]", " ");
            Files.copy(Files.newInputStream(file.toPath()), Paths.get(path).resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException ex){
            log.print(StringConfig.getValue("err.db.saveOrUpdate") + ex);
            throw new SimpleDesktopException("Error!, saving file", getClass());
        }
        return newFileName;
    }

}

