package com.project.module.tl.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.project.common.constant.SystemConstant;
import com.project.common.exception.BusinessException;
import com.project.common.spring.SessionHolder;
import com.project.common.util.ListUtils;
import com.project.common.util.ValidateUtils;
import com.project.module.tl.controller.vo.TablecodeVO;
import com.project.module.tl.service.TablecodeService;

/**
 * 功能：正向代码生成器
 * @author yelinsen
 * @version:2017-1-22
 */
@Controller
@RequestMapping("/tl/tablecode")
@ApiIgnore
public class TablecodeController{
	@Autowired
	private TablecodeService tablecodeService;
	
	@RequestMapping(value="/manage",method=RequestMethod.GET)
    public String manage(TablecodeVO tablecodeVO,Map<String, Object> map){
    	//把增删改查权限放入map中
    	SessionHolder.setMenuMap(map, "tl/tablecode");
       	return "tl/tablecode_list";
    }
	/**
     * 根据条件单表分页与不分页查询（默认不分页）
     * @param condition
     * @return
     */
    @RequestMapping(value="/query",method=RequestMethod.GET)
    @ResponseBody
    public Object query(TablecodeVO condition) {
        return tablecodeService.queryMapByCondition(condition);
    }

    /**
     * 新增与保存
     * @param condition
     * @return
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public Object update(TablecodeVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return tablecodeService.doUpdate(condition);
    }

    @RequestMapping(value="/insert",method=RequestMethod.POST)
    @ResponseBody
    public Object insert(TablecodeVO condition) {
        return tablecodeService.doInsert(condition);
    }
    /**
     * 失效
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalid",method=RequestMethod.POST)
    @ResponseBody
    public Object doInvalidate(TablecodeVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return tablecodeService.doInvalidate(condition);
    }

    /**
     * 逻辑删除（批量）
     * @param condition
     * @return
     */
    @RequestMapping(value="/invalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object doBatchInvalid(TablecodeVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return tablecodeService.doBatchInvalidate(condition);
    }

    /**
     * 批量有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalidBatch",method=RequestMethod.POST)
    @ResponseBody
    public Object batchRevalid(TablecodeVO condition) {
        if (ListUtils.isEmpty(condition.getIds())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return tablecodeService.doBatchRevalidate(condition);
    }

    /**
     * 有效
     * @param condition
     * @return
     */
    @RequestMapping(value="/revalid",method=RequestMethod.POST)
    @ResponseBody
    public Object revalid(TablecodeVO condition) {
        if (ValidateUtils.isBlank(condition.getId())) {
            throw new BusinessException(SystemConstant.NULL_MUSTPARAMETER);
        }
        return tablecodeService.doRevalidate(condition);
    }
}