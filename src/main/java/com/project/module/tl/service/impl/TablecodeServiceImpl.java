package com.project.module.tl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.service.impl.BaseServiceImpl;
import com.project.module.tl.controller.vo.TablecodeVO;
import com.project.module.tl.dao.TablecodeMapper;
import com.project.module.tl.service.TablecodeService;
import com.project.common.annotation.BaseAnnotation;
/**
 * @author yelinsen
 * @version:2017-1-22
 */
 @Service
public class TablecodeServiceImpl extends BaseServiceImpl<TablecodeVO> implements TablecodeService{
	@BaseAnnotation
	@Autowired
	private TablecodeMapper tablecodeMapper;
	
}
