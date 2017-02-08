package com.project.module.tl.service;

import com.project.module.tl.controller.vo.ColumncodeVO;
import com.project.common.service.BaseService;
/**
 * 功能：TODO
 * @author yelinsen
 * @version:2017-1-22
 */
public interface ColumncodeService extends BaseService<ColumncodeVO>{
	/**
	 * 代码生成器
	 * @param columncodeVO
	 * @return
	 */
	Object doCreateCode(ColumncodeVO columncodeVO);

}