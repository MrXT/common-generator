<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
	<context id="Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat">
		<property name="beginningDelimiter" value="`" />
		<property name="endingDelimiter" value="`" />
		<plugin type="tk.mybatis.mapper.generator.MapperPlugin">
			<property name="mappers" value="${mappers}" />
		</plugin>

		<commentGenerator>
			<property name="javaFileEncoding" value="UTF-8" />
			<property name="suppressAllComments" value="true" />
			<property name="suppressDate" value="true" />
		</commentGenerator>

		<jdbcConnection driverClass="${jdbcDriverClass}" connectionURL="${jdbcUrl}" userId="${jdbcUsername}" password="${jdbcPassword}">
		</jdbcConnection>
		<javaModelGenerator targetPackage="${domainPackage}" targetProject="${targetJavaProject}">
			<#if !isRel> 
				<property name="rootClass" value="${entitySuperClass}" />
			</#if> 
		</javaModelGenerator>

		<sqlMapGenerator targetPackage="${targetXMLPackage}" targetProject="${targetResourcesProject}" />

		<javaClientGenerator targetPackage="${modulePackage}.dao" targetProject="${targetJavaProject}" type="XMLMAPPER">
		</javaClientGenerator>

		<!-- <table tableName="tb_tags" domainObjectName="Tags"> </table> -->
		<!-- <table tableName="bs_account" domainObjectName="Account"> -->
		<!-- </table> -->
		<!-- <table tableName="bs_record" domainObjectName="Record"> -->
		<!-- </table> -->
		<table tableName="${tableName}" domainObjectName="${entityName}">
		</table>
	</context>
</generatorConfiguration>