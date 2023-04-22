package com.example.program.app.service;

import com.example.program.app.entity.OsciLanguageEntity;
import com.example.program.app.property.OsciLanguageProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;
import com.example.program.util.exception.UnsafeUpdateException;
import org.python.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class OsciLanguageService extends BaseService {
    private LogUtil log = LogUtil.getLog(this.getClass());

    public List<OsciLanguageProperty> list(){
        openCurrentSession();
        List<OsciLanguageProperty> list = new ArrayList<>();

        try {
            List<OsciLanguageEntity> entities = languageDao.find("from OsciLanguageEntity l where l.status <> ?", new Object[]{EntityStatus.DELETED});
            entities.forEach(e -> list.add(OsciLanguageProperty.newInstance(e, false)));
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.get") + " \n" + ex.getMessage());
            closeCurrentSession();
            throw new UnsafeUpdateException(StringConfig.getValue("err.db.get"), getClass());
        }
        closeCurrentSession();
        return list;
    }

}
