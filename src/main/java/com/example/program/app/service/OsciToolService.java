package com.example.program.app.service;

import com.example.program.app.entity.OsciToolEntity;
import com.example.program.app.property.OsciToolProperty;
import com.example.program.common.service.BaseService;
import com.example.program.util.LogUtil;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OsciToolService extends BaseService {

    private final LogUtil log = LogUtil.getLog(this.getClass());

    public List<OsciToolProperty> listTools() {
        List<OsciToolProperty> list = new ArrayList<>();

        openCurrentSession();
        try {
            String hql = "from OsciToolEntity where 1 = 1";
            List<OsciToolEntity> listEntities = toolDao.find(hql);
            if (!listEntities.isEmpty()) {
                list = listEntities.stream().map(e -> OsciToolProperty.newInstance(e, false)).collect(Collectors.toList());
            }
        } catch (Exception e) {
            Note.error(StringConfig.getValue("err.db.get") + "\n" + e);
        }
        return list;
    }


}
