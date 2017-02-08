package ${modulePackage}.controller.vo;

import ${domainPackage}.${entityName};
<#if haveUserId>
import com.project.common.annotation.Sort;
import io.swagger.annotations.ApiModelProperty;
</#if>
/**
 * 接受请求参数
 * @author ${authName}
 * @version:${.now?date}
 */
public class ${entityName}VO extends ${entityName}{
	//TODO 可以扩展参数
	<#if haveUserId>
	@Sort("su.name")
	@ApiModelProperty(value="用户名")
	private String name;//（用户名）

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    @ApiModelProperty(hidden=true)
    public String getOnlyName() {
    	if(name == null){
    		return null; 
    	}
        return '%'+name.trim()+'%';
    }
    @ApiModelProperty(hidden=true)
    public String getLeftOnlyName() {
    	if(name == null){
    		return null; 
    	}
        return '%'+name.trim();
    }
    @ApiModelProperty(hidden=true)
    public String getRightOnlyName() {
    	if(name == null){
    		return null; 
    	}
        return name.trim()+'%';
    }
	</#if> 
}