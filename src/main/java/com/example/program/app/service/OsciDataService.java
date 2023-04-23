package com.example.program.app.service;

import com.example.program.app.entity.OsciDataEntity;
import com.example.program.app.entity.OsciToolEntity;
import com.example.program.app.property.OsciDataProperty;
import com.example.program.app.property.OsciToolProperty;
import com.example.program.common.service.BaseService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.DataGrid;
import com.example.program.util.LogUtil;
import com.example.program.util.StringConfig;
import com.example.program.util.exception.SimpleDesktopException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class OsciDataService extends BaseService {

    private LogUtil log = LogUtil.getLog(getClass());

    public List<OsciDataProperty> listData(Map<String, Object> param, int page, int rows){
        openCurrentSession();
        String sql = "select od.id as id, od.dataname as dataName, od.info as info, od.date as date,  od.osci_tool_id as osciToolId, od.file_id as fileId, of.file_name as fileName, of.original_name as originalName \n" +
                     "from osci_data od \n" +
                     "left join osci_file of on od.file_id = of.id \n" +
                     "where od.status <> 'DELETED' and of.status <> 'DELETED' \n" +
                     " and od.date >= to_timestamp(coalesce(cast(:fromDate as text), '1970-01-01'), 'yyyy-mm-dd') and od.date <= to_timestamp(coalesce(cast(:toDate as text), '2100-01-01'), 'yyyy-mm-dd') ";

        if(param.containsKey("name") && !StringUtils.isEmpty((String) param.get("name"))){
            sql += " and od.dataname like '%%' || :name || '%%' ";
        }
        else param.remove("name");

        if(param.containsKey("toolId") && MapUtils.getLong(param,"toolId") != null){
            sql += " and od.osci_tool_id = :toolId";
        }
        else param.remove("toolId");

        List eList = osciDataDao.findSQL(sql, param);
        List<OsciDataProperty> listData = new ArrayList<>();
        for(Object o : eList){
            Map<String, Object> eMap = (HashMap<String, Object>) o;
            OsciDataProperty dataP = new OsciDataProperty();
            dataP.setId(MapUtils.getLong(eMap, "id"));
            dataP.setDate((Date) eMap.get("date"));
            dataP.setOsciToolId(MapUtils.getLong(eMap, "oscitoolid"));
            dataP.setOsciFileId(MapUtils.getLong(eMap, "fileid"));
            dataP.setInfo(MapUtils.getString(eMap, "info"));
            dataP.getDataFile().setFilename(MapUtils.getString(eMap, "filename"));
            dataP.getDataFile().setOriginalName(MapUtils.getString(eMap, "originalname"));
            dataP.setDataName(MapUtils.getString(eMap, "dataname"));
            listData.add(dataP);
        }
        closeCurrentSession();
        return listData;
    }

    private Long total(Map<String, Object> param){
        openCurrentSession();
        String sql = "select count(*) \n" +
                     "from osci_data od\n" +
                     "left join osci_file of on od.file_id = of.id\n" +
                     "where od.status <> 'DELETED' and of.status <> 'DELETED'";
        List<Object> values = new ArrayList<>();
        sql = addWhere(param, sql, values);
        Long count = osciDataDao.countSQL(sql, values);
        closeCurrentSession();
        return count;
    }

    private String addWhere(Map<String, Object> param, String sql ,List<Object> values){
        if(!MapUtils.getString(param, "name").isEmpty()){
            sql += " and lower(od.dataname) like ? ";
            values.add("%%" + MapUtils.getString(param, "name").trim().toLowerCase() + "%%");
        }
        if(param.containsKey("fromDate")){
            sql += " and od.date >= to_timestamp(coalesce(cast(? as text), '1970-01-01'), 'yyyy-mm-dd')";
            values.add(param.get("fromDate"));
        }
        if(param.containsKey("toDate")){
            sql += " and od.date <= to_timestamp(coalesce(cast(? as text), '2100-01-01'), 'yyyy-mm-dd')";
            values.add(param.get("toDate"));
        }
        return sql;
    }

    public DataGrid<OsciDataProperty> dataGrid(Map<String, Object> param, int page, int rows){
        DataGrid<OsciDataProperty> result = new DataGrid<>();
        result.setTotal(total(param).intValue());
        result.setRows(listData(param, page, rows));
        return result;
    }

    public OsciDataProperty saveOsciData(OsciDataProperty property){
        openCurrentSessionWithTransaction();
        OsciDataEntity entity = new OsciDataEntity();
        try{
            entity = property.toEntity(entity, false);
            entity.setCreated(new Date());
            entity.setStatus(EntityStatus.ACTIVE);
            osciDataDao.save(entity);
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.saveOrUpdate") + "\n" + ex);
            closeCurrentSessionWithTransaction();
            throw new SimpleDesktopException(StringConfig.getValue("err.db.saveOrUpdate"), getClass());
        }
        closeCurrentSessionWithTransaction();
        return OsciDataProperty.newInstance(entity, false);
    }

    public Boolean deleteData(Long dataId){
        Boolean result = false;
        if(dataId == null || dataId <= 0) result = false;
        openCurrentSessionWithTransaction();
        try{
            OsciDataEntity entity = findById(dataId, false);
            entity.setStatus(EntityStatus.DELETED);
            osciDataDao.saveOrUpdate(entity);
            result = true;
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.delete") + "\n " + ex);
            closeCurrentSessionWithTransaction();
            result = false;
        }
        closeCurrentSessionWithTransaction();
        return result;
    }

    public OsciDataEntity findById(Long id, boolean withSession){
        if(id == null || id <= 0) return null;
        if(withSession) openCurrentSession();

        OsciDataEntity entity = null;
        try{
            entity = osciDataDao.findFirst("from OsciDataEntity where 1 = 1 and status <> ? and id = ? ", new Object[]{EntityStatus.DELETED, id});
        }
        catch(Exception ex){
            log.print(StringConfig.getValue("err.db.get") + " \n" + ex);
            if(withSession) closeCurrentSession();
        }
        if(withSession) closeCurrentSession();

        return entity;
    }


}
