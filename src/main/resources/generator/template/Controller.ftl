package ${modulePackage}.controller;
import java.util.Map;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import com.project.common.exception.BusinessException;
import com.project.common.util.ValidateUtils;
import com.project.common.constant.SystemConstant;
import com.project.common.annotation.ApiResponseDesc;
import com.project.common.annotation.ApiResponseDescs;
import com.project.common.annotation.ApiResponseDesc.ApiResponseType;
import com.project.common.bean.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import com.project.common.spring.SessionHolder;
import com.project.common.controller.BaseController;
import ${modulePackage}.controller.vo.${entityName}VO;
import ${modulePackage}.service.${entityName}Service;

/**
 * 功能：TODO
 * @author ${authName}
 * @version:${.now?date}
 */
@Controller
@RequestMapping("/${moduleName}/${simpleEntityName}")
@Api(value="${entityCommet}-接口")
public class ${entityName}Controller extends BaseController<${entityName}VO>{
	@Autowired
	private ${entityName}Service ${entityName?uncap_first}Service;
	
	@RequestMapping(value="/manage",method=RequestMethod.GET)
	@ApiIgnore
    public String manage(${entityName}VO ${simpleEntityName}VO,Map<String, Object> map){
    	//把增删改查权限放入map中
    	SessionHolder.setMenuMap(map, "${moduleName}/${simpleEntityName}");
       	return "${moduleName}/${simpleEntityName}_list";
    }
    /**
     * 根据条件单表分页与不分页查询（默认不分页）
     * @param condition
     * @return
     */
    @RequestMapping(value="/query",method=RequestMethod.GET)
    @ApiOperation(value="${entityCommet}查询",response=QueryResult.class)//控制swagger api文档 response返回类型 ,response 目前支持QueryResult,List,Object 
    @ApiImplicitParams(value = { //控制swagger api文档 request 请求参数
            @ApiImplicitParam(name = "${simpleEntityName}Id", value = "${entityCommet}id",paramType="query", dataType = "String"),
            ${apiRequestjsons}
    })  
    @ApiResponseDescs({//控制swagger api文档 response 返回json格式
        @ApiResponseDesc(value="${simpleEntityName}Id",description="${entityCommet}id"),
        ${apiResponsejsons},
        @ApiResponseDesc(value="valid",type=ApiResponseType.BOOLEAN,description="有效"),
        @ApiResponseDesc(value="utime",description="操作时间"),
        @ApiResponseDesc(value="ctime",description="创建时间"),
        @ApiResponseDesc(value="uid",description="操作人ID")
    })
    @ResponseBody
    public Object query(@ApiIgnore ${entityName}VO condition) {//@ApiIgnore在接口文档界面上忽略该参数
        return ${simpleEntityName}Service.queryMapByCondition(condition);
    }

    /**
     * 新增与保存
     * @param condition
     * @return
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ApiOperation(value="${entityCommet}修改")  
    @ApiImplicitParams(value = {  
            @ApiImplicitParam(name = "${simpleEntityName}Id",required=true, value = "${entityCommet}id",paramType="query", dataType = "String"),
            ${apiRequestjsons}
    })  
    @ResponseBody
    public Object update(@ApiIgnore ${entityName}VO condition) {
        if (ValidateUtils.isBlank(condition.get${entityName}Id())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return ${simpleEntityName}Service.doUpdate(condition);
    }

    @RequestMapping(value="/insert",method=RequestMethod.POST)
    @ApiOperation(value="${entityCommet}新增")  
    @ApiImplicitParams(value = {  
            ${apiRequestjsons}
    })  
    @ResponseBody
    public Object insert(@ApiIgnore ${entityName}VO condition) {
     	if (ValidateUtils.isBlank(condition.get${entityName}Name())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return ${simpleEntityName}Service.doInsert(condition);
    }
}