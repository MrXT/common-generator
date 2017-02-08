package com.project.module.tl.controller;
import generator.FreeMarkerGenerator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.exception.ServerException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.FastJsonUtils;
import com.project.common.util.ListUtils;
import com.project.common.util.ValidateUtils;
import com.project.entity.Tablecode;
import com.project.module.tl.controller.vo.ColumncodeVO;
import com.project.module.tl.controller.vo.TablecodeVO;
import com.project.module.tl.service.ColumncodeService;
import com.project.module.tl.service.TablecodeService;

/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-1-22
 */
@Controller
@RequestMapping("/tl/columncode")
@ApiIgnore
public class ColumncodeController{
	@Autowired
	private ColumncodeService columncodeService;
	
	@Autowired
    private TablecodeService tablecodeService;
	
	@RequestMapping(value="/manage",method=RequestMethod.GET)
    public String manage(ColumncodeVO columncodeVO,Map<String, Object> map){
    	//把增删改查权限放入map中
    	SessionHolder.setMenuMap(map, "tl/columncode");
    	Tablecode tablecode = tablecodeService.queryByPrimaryKey(columncodeVO.getTableId());
    	TablecodeVO tablecodeVO = FastJsonUtils.beanToBean(tablecode, TablecodeVO.class);
    	if(tablecodeVO.getRel() != null && tablecodeVO.getRel()){
    	    tablecodeVO.setTableName(tablecodeVO.getTableName().substring(0, tablecodeVO.getTableName().lastIndexOf("_")));
    	}else{
    	    tablecodeVO.setTableName(tablecodeVO.getTablecodeName().substring(tablecodeVO.getTablecodeName().indexOf("_")+1,tablecodeVO.getTablecodeName().length()));
    	}
    	map.put("params", tablecodeVO);
       	return "tl/columncode_list";
    }
	@RequestMapping(value="/createCode")
	@ResponseBody
    public Object createCode(ColumncodeVO columncodeVO){
		if(ListUtils.isEmpty(columncodeVO.getIds())){
			throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
		}
       	String [] tableNames = (String[]) columncodeService.doCreateCode(columncodeVO);
       	if(tableNames!=null){
       	    try {
                FreeMarkerGenerator.generatorCode(true, true, tableNames);
            } catch (Exception e) {
                throw new ServerException(e.getMessage());
            }
       	}
       	return tableNames == null ? 0 :tableNames.length;
    }
	/**
     * 根据条件单表分页与不分页查询（默认不分页）
     * @param condition
     * @return
     */
    @RequestMapping(value="/query",method=RequestMethod.GET)
    @ResponseBody
    public Object query(ColumncodeVO condition) {
        return columncodeService.queryMapByCondition(condition);
    }

    /**
     * 新增与保存
     * @param condition
     * @return
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public Object update(ColumncodeVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return columncodeService.doUpdate(condition);
    }

    @RequestMapping(value="/insert",method=RequestMethod.POST)
    @ResponseBody
    public Object insert(ColumncodeVO condition) {
        return columncodeService.doInsert(condition);
    }
    /**
     * 失效
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalid",method=RequestMethod.POST)
    @ResponseBody
    public Object doInvalidate(ColumncodeVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return columncodeService.doInvalidate(condition);
    }

    /**
     * 逻辑删除（批量）
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object doBatchInvalid(ColumncodeVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return columncodeService.doBatchInvalidate(condition);
    }

    /**
     * 批量有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object batchRevalid(ColumncodeVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return columncodeService.doBatchRevalidate(condition);
    }

    /**
     * 有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalid",method=RequestMethod.POST)
    @ResponseBody
    public Object revalid(ColumncodeVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return columncodeService.doRevalidate(condition);
    }
}