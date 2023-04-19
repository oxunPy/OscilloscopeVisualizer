package com.example.program.app.service;

import com.example.program.app.property.OsciDataProperty;
import com.example.program.common.service.BaseService;
import com.example.program.util.DataGrid;
import org.apache.commons.collections.MapUtils;

import java.util.*;

public class OsciDataService extends BaseService {


    public List<OsciDataProperty> listData(Map<String, Object> param, int page, int rows){
        openCurrentSession();
        String sql = "select\n" +
                     "    od.id, od.dataname, od.date, od.file_id, of.file_name, of.original_name, of.file_path\n" +
                     "from osci_data od\n" +
                     "left join osci_file of on od.file_id = of.id\n" +
                     "where od.status <> 'DELETED' and of.status <> 'DELETED' and od.dataname like '%%' || :name || '%%'\n" +
                     "                             and od.date >= to_timestamp(coalesce(:fromDate, '1970-01-01'), 'yyyy-mm-dd')\n" +
                     "                             and od.date <= to_timestamp(coalesce(:toDate, '2100-01-01'), 'yyyy-mm-dd')\n";

        Map<String, Object> values = new HashMap<>();

        List eList = osciDataDao.findSQL(sql, param);
        List<OsciDataProperty> listData = new ArrayList<>();
        eList.forEach(e -> {
           OsciDataProperty oData = new OsciDataProperty();
        });
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
            sql += " and lower(od.dataname) line ? ";
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
}
