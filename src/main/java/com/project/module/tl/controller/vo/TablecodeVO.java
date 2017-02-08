package com.project.module.tl.controller.vo;

import java.util.List;

import com.project.entity.Columncode;
import com.project.entity.Tablecode;

/**
 * 接受请求参数
 * @author yelinsen
 * @version:2017-1-22
 */
public class TablecodeVO extends Tablecode {

    public String getTableName() {
        if(tableName != null){return tableName;}
        if (getRel() != null) {
            if (getRel()) {
                return getTablecodeName().substring(getTablecodeName().indexOf("_") + 1, getTablecodeName().toLowerCase().lastIndexOf("_rel")) + "_name";
            } else {
                return getTablecodeName().substring(getTablecodeName().indexOf("_") + 1, getTablecodeName().length()) + "_name";
            }
        } else {
            return null;
        }
    }

    public Boolean getRel() {
        return getTablecodeName() == null ? null : getTablecodeName().toLowerCase().endsWith("_rel");
    }

    public String getTableId() {
        if (getRel() != null) {
            if (getRel()) {
                return getTablecodeName().substring(getTablecodeName().indexOf("_") + 1, getTablecodeName().toLowerCase().lastIndexOf("_rel")) + "_id";
            } else {
                return getTablecodeName().substring(getTablecodeName().indexOf("_") + 1, getTablecodeName().length()) + "_id";
            }
        }
        return null;
    }

    // TODO 可以扩展参数
    List<Columncode> columncodes;

    public List<Columncode> getColumncodes() {
        return columncodes;
    }

    public void setColumncodes(List<Columncode> columncodes) {
        this.columncodes = columncodes;
    }
    private String tableName;
    public void setTableName(String substring) {
        this.tableName = substring;
    }

}