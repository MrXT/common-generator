
package generator.bean;

/**
 * 字段注释
 * ClassName: FiledCommet <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年1月16日
 * @version 1.0
 */
public class FiledCommet {
    private String columnName;//注释名称
    private String propertyName;//属性名
    private String type;//属性类型
    
    private Boolean isNull;//是否为空
    public Boolean getIsNull() {
        return isNull;
    }
    
    public void setIsNull(Boolean isNull) {
        this.isNull = isNull;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
    
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    
}

