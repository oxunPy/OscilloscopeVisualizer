package com.example.program.app.service;

import com.example.program.app.Launch;
import com.example.program.app.entity.OsciFileEntity;
import com.example.program.app.entity.OsciToolEntity;
import com.example.program.app.property.OsciToolProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.LogUtil;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import com.example.program.util.exception.SimpleDesktopException;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OsciToolService extends BaseService {

    private final LogUtil log = LogUtil.getLog(this.getClass());

    public List<OsciToolProperty> listTools() {
        List<OsciToolProperty> list = new ArrayList<>();

        openCurrentSession();
        try {
            String hql = "from OsciToolEntity where 1 = 1 and status <> ? ";
            List<OsciToolEntity> listEntities = toolDao.find(hql, new Object[]{EntityStatus.DELETED});
            if (!listEntities.isEmpty()) {
                list = listEntities.stream().map(e -> OsciToolProperty.newInstance(e, false)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            Note.error(StringConfig.getValue("err.db.get") + "\n" + e);
        }
        return list;
    }

    public InputStream getToolPhoto(Long toolId){
        openCurrentSession();
        InputStream is = null;
        try {
            OsciToolEntity toolEntity = toolDao.findFirst("from OsciToolEntity where 1 = 1 and status = ? and id = ? ", new Object[]{EntityStatus.ACTIVE, toolId});
            if(toolEntity.getImageFileId() == null) is = getClass().getResourceAsStream("/img/no-photo.png");
            else is = getImageInputStreamById(toolEntity.getImageFileId());
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.get") + " \n" + ex);
            closeCurrentSession();
            throw new SimpleDesktopException(StringConfig.getValue("err.db.get"), getClass());
        }
        closeCurrentSession();
        return is;
    }

    public InputStream getImageInputStreamById(Long imageId){
        InputStream is = null;

        try{
            OsciFileEntity fileEntity = osciFileDao.findFirst("from OsciFileEntity where 1 = 1 and status = ? and id = ? ", new Object[]{EntityStatus.ACTIVE, imageId});
            String filename = fileEntity.getFilename();
//            is = Files.newInputStream(Paths.get(Launch.properties.getStr("osci.upload.img.path")).resolve(filename));
            BufferedImage buffImage50 = Thumbnails.of(Files.newInputStream(Paths.get(Launch.properties.getStr("osci.upload.img.path")).resolve(filename))).height(50).asBufferedImage();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(buffImage50, "jpg", os);
            is = new ByteArrayInputStream(os.toByteArray());
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.get") + " \n" + ex);
            closeCurrentSession();
            throw new SimpleDesktopException(StringConfig.getValue("err.db.get"), getClass());
        }
        return is;
    }

    public OsciToolProperty saveOsciTool(OsciToolProperty property){
        openCurrentSessionWithTransaction();
        OsciToolEntity entity = new OsciToolEntity();
        try{
            entity = property.toEntity(entity, false);
            entity.setCreated(new Date());
            entity.setStatus(EntityStatus.ACTIVE);
            toolDao.saveOrUpdate(entity);
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.saveOrUpdate") + "\n" + ex);
            closeCurrentSessionWithTransaction();
            throw new SimpleDesktopException(StringConfig.getValue("err.db.saveOrUpdate"), getClass());
        }
        closeCurrentSessionWithTransaction();
        return OsciToolProperty.newInstance(entity, false);
    }

    public OsciToolEntity getToolById(Long toolId, boolean withOpenSession){
        if(withOpenSession) openCurrentSession();
        String hql = "from OsciToolEntity where 1 = 1 and status <> ? and id = ? ";
        OsciToolEntity entity = toolDao.findFirst(hql, new Object[]{EntityStatus.DELETED, toolId});
        if(withOpenSession) closeCurrentSession();
        return entity;
    }

    public Boolean deleteTool(Long toolId){
        if(toolId == null) return null;
        openCurrentSessionWithTransaction();
        try{
            OsciToolEntity entity = getToolById(toolId, false);
            entity.setStatus(EntityStatus.DELETED);
            toolDao.save(entity);
        }
        catch (Exception ex){
            log.print(StringConfig.getValue("err.db.delete") + "\n " + ex);
            closeCurrentSessionWithTransaction();
            return false;
        }
        closeCurrentSessionWithTransaction();
        return true;
    }

}
