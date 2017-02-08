package com.project.entity;

import com.project.common.bean.BaseEntity;

import java.util.Date;

import javax.persistence.*;

@Table(name = "tl_tablecode")
public class Tablecode extends BaseEntity {
    @Id
    @Column(name = "tablecode_id")
    private String tablecodeId;

    /**
     * 表名
     */
    @Column(name = "tablecode_name")
    private String tablecodeName;
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

	public String getOnlyTablecodeName(){
        if(tablecodeName == null){ 
            return null;      
        }                     
        return "%"+tablecodeName.trim()+"%";
    }

    private Date ctime;

    /**
     * @return tablecode_id
     */
    public String getTablecodeId() {
        return tablecodeId;
    }

    /**
     * @param tablecodeId
     */
    public void setTablecodeId(String tablecodeId) {
        this.tablecodeId = tablecodeId;
    }

    /**
     * 获取表名
     *
     * @return tablecode_name - 表名
     */
    public String getTablecodeName() {
        return tablecodeName;
    }

    /**
     * 设置表名
     *
     * @param tablecodeName 表名
     */
    public void setTablecodeName(String tablecodeName) {
        this.tablecodeName = tablecodeName;
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