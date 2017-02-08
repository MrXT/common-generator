package ${modulePackage}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.common.service.impl.BaseServiceImpl;
import ${modulePackage}.controller.vo.${entityName}VO;
import ${modulePackage}.dao.${entityName}Mapper;
import ${modulePackage}.service.${entityName}Service;
import com.project.common.annotation.BaseAnnotation;
/**
 * @author ${authName}
 * @version:${.now?date}
 */
 @Service
public class ${entityName}ServiceImpl extends BaseServiceImpl<${entityName}VO> implements ${entityName}Service{
	@BaseAnnotation
	@Autowired
	private ${entityName}Mapper ${entityName?uncap_first}Mapper;
}
