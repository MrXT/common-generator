package generator.bean;

import generator.FreeMarkerGenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.common.util.CommonUtils;
import com.project.common.util.ValidateUtils;


public class TableBean {

    private String tableName;// 表名

    private String entityName;// 实体名
    
    private String commet;//注释
    
    public void setCommet(String commet) {
        this.commet = commet;
    }

    /**
     * 通过tl_tablecode获取实体的注释
     * @author xt
     * @return
     */
    public String getCommet() {
        if(this.commet != null){
            return this.commet;
        }
        try {
            Class.forName(FreeMarkerGenerator.jdbcDriverClass);
            Connection conn = DriverManager.getConnection(FreeMarkerGenerator.jdbcUrl, FreeMarkerGenerator.jdbcUsername,
                FreeMarkerGenerator.jdbcPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select commet from tl_tablecode where tablecode_name = '"+getTableName()+"' limit 1");
            if (rs.next()) {
                this.commet = rs.getString("commet");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {

        }
        if(ValidateUtils.isBlank(this.commet)){
            this.commet = this.entityName;
        }
        return this.commet;
    }
    private Boolean isRel;// 是否是关联关系表

    private String basePackage;// 模块的包名

    private String targetXMLPackage;// xml所在的包名

    private Boolean haveUserId;

    /**
     * 通过表名来查询
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Boolean hasUserId() throws Exception {// 是否该表的字段包含user_id
        if (haveUserId != null) {
            return haveUserId;
        }
        Class.forName(FreeMarkerGenerator.jdbcDriverClass);
        // 得到连接
        Connection conn = DriverManager
            .getConnection(FreeMarkerGenerator.jdbcUrl, FreeMarkerGenerator.jdbcUsername, FreeMarkerGenerator.jdbcPassword);
        String sql = "select * from " + tableName;
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData data = rs.getMetaData();
        for (int j = 1; j <= data.getColumnCount(); j++) {
            if (data.getColumnName(j).toLowerCase().indexOf("user_id") != -1 && !tableName.equalsIgnoreCase(FreeMarkerGenerator.userTableName)) {
                haveUserId = true;
                return true;
            }else{
                haveUserId = false;
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        return false;
    }
    private List<FiledCommet> commets = null;
    public List<FiledCommet> getFiledCommets() {
        if(this.commets != null){
            return this.commets;
        }
        List<FiledCommet> commets = new ArrayList<FiledCommet>();
        try {
            Class.forName(FreeMarkerGenerator.jdbcDriverClass);
            Connection conn = DriverManager.getConnection(FreeMarkerGenerator.jdbcUrl, FreeMarkerGenerator.jdbcUsername,
                FreeMarkerGenerator.jdbcPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                FiledCommet commet = new FiledCommet();
                String fields[] = rs.getString("Field").toLowerCase().split("_");
                if (ValidateUtils.isNotBlank(rs.getString("Comment"))) {
                    StringBuilder sb = new StringBuilder();
                    for (String string : fields) {
                        sb.append(CommonUtils.toUpperInitial(string));
                    }
                    if (sb.toString().toLowerCase().equals("userid")) {// 如果==user_id
                        commet.setColumnName("用户名");
                        commet.setPropertyName("name");
                    } else {
                        commet.setColumnName(rs.getString("Comment"));
                        commet.setPropertyName(CommonUtils.toLowerInitial(sb.toString()));
                    }
                    String type = rs.getString("Type");
                    if(type.contains("varchar")){
                        type="string";
                    }else if(type.contains("int")){
                        type="integer";
                    }else if(type.contains("tinyint")){
                        type="boolean";
                    }else if(type.contains("date")){
                        type="string";
                    }else{
                        type="string";
                    }
                    commet.setType(type);
                    commet.setIsNull(rs.getString("Null").equals("NO")?true:false);
                    commets.add(commet);
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {

        }
        this.commets = commets;
        return commets;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Boolean getIsRel() {
        return isRel;
    }

    public void setIsRel(Boolean isRel) {
        this.isRel = isRel;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTargetXMLPackage() {
        return targetXMLPackage;
    }

    public void setTargetXMLPackage(String targetXMLPackage) {
        this.targetXMLPackage = targetXMLPackage;
    }
}
