package com.project.entity;

import com.project.common.bean.BaseEntity;

import java.util.Date;

import javax.persistence.*;

@Table(name = "tl_columncode")
public class Columncode extends BaseEntity {
    @Id
    @Column(name = "columncode_id")
    private String columncodeId;

    /**
     * 列名
     */
    @Column(name = "columncode_name")
    private String columncodeName;
    
    /**
     * 注释
     */
    private String commet;
    
    
    public String getCommet() {
        return commet;
    }

    
    public void setCommet(String commet) {
        this.commet = commet;
    }

    public String getOnlyColumncodeName(){
        if(columncodeName == null){ 
            return null;      
        }                     
        return "%"+columncodeName.trim()+"%";
    }

    /**
     * 类型
     */
    private String type;

    /**
     * 长度
     */
    private Integer length;

    /**
     * 默认空
     */
    @Column(name = "default_null")
    private Boolean defaultNull;

    @Column(name = "table_id")
    private String tableId;

    private Date ctime;

    /**
     * @return columncode_id
     */
    public String getColumncodeId() {
        return columncodeId;
    }

    /**
     * @param columncodeId
     */
    public void setColumncodeId(String columncodeId) {
        this.columncodeId = columncodeId;
    }

    /**
     * 获取列名
     *
     * @return columncode_name - 列名
     */
    public String getColumncodeName() {
        return columncodeName;
    }

    /**
     * 设置列名
     *
     * @param columncodeName 列名
     */
    public void setColumncodeName(String columncodeName) {
        this.columncodeName = columncodeName;
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取长度
     *
     * @return length - 长度
     */
    public Integer getLength() {
        return length;
    }

    /**
     * 设置长度
     *
     * @param length 长度
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * 获取默认空
     *
     * @return default_null - 默认空
     */
    public Boolean getDefaultNull() {
        return defaultNull;
    }

    /**
     * 设置默认空
     *
     * @param defaultNull 默认空
     */
    public void setDefaultNull(Boolean defaultNull) {
        this.defaultNull = defaultNull;
    }

    /**
     * @return table_id
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * @param tableId
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     * @return ctime
     */
    public Date getCtime() {
        return ctime;
    }

    /**
     * @param ctime
     */
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}