package com.project.module.tl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import com.project.common.annotation.BaseAnnotation;
import com.project.common.service.impl.BaseServiceImpl;
import com.project.entity.Columncode;
import com.project.entity.Tablecode;
import com.project.module.tl.controller.vo.ColumncodeVO;
import com.project.module.tl.controller.vo.TablecodeVO;
import com.project.module.tl.dao.ColumncodeMapper;
import com.project.module.tl.dao.TablecodeMapper;
import com.project.module.tl.service.ColumncodeService;

/**
 * @author yelinsen
 * @version:2017-1-22
 */
@Service
public class ColumncodeServiceImpl extends BaseServiceImpl<ColumncodeVO>
		implements ColumncodeService {
	@BaseAnnotation
	@Autowired
	private ColumncodeMapper columncodeMapper;

	@Autowired
	private TablecodeMapper tablecodeMapper;

	@Override
	public Object doCreateCode(ColumncodeVO columncodeVO) {
		List<TablecodeVO> tablecodeVOs = new ArrayList<TablecodeVO>();
		for (String tableId : columncodeVO.getIds()) {
			Tablecode tablecode = tablecodeMapper.selectByPrimaryKey(tableId);
			Columncode columncode = new Columncode();
			columncode.setTableId(tableId);
			columncode.setValid(true);
			List<Columncode> columncodes = columncodeMapper.select(columncode);
			TablecodeVO tablecodeVO = new TablecodeVO();
			tablecodeVO.setColumncodes(columncodes);
			tablecodeVO.setTablecodeName(tablecode.getTablecodeName());
			tablecodeVO.setCommet(tablecode.getCommet());
			tablecodeVOs.add(tablecodeVO);
		}
		String tableNames[] = null;
		if(!ListUtils.isEmpty(tablecodeVOs)){
			Map<String,Object> map =new HashMap<String,Object>();
			map.put("tablecodeVOs", tablecodeVOs);
			tablecodeMapper.createTable(map);
			tableNames = new String[tablecodeVOs.size()];
			for (int i = 0; i < tableNames.length; i++) {
				tableNames[i] = tablecodeVOs.get(i).getTablecodeName();
			}
		}
		return tableNames;
	}
}
