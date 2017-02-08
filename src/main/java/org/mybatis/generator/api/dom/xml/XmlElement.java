/*
 * Copyright 2006 The Apache Software Foundation Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.mybatis.generator.api.dom.xml;

import generator.FreeMarkerGenerator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.OutputUtilities;

import com.project.common.util.CommonUtils;


/**
 * @author Jeff Butler
 */
public class XmlElement extends Element {

    /**
     * XT
     */
    public static String tableNameSimple = "";

    public static String tableName = "";

    /**
     * XT
     */
    private List<Attribute> attributes;

    private List<Element> elements;

    private String name;

    /**
     *  
     */
    public XmlElement(String name) {
        super();
        attributes = new ArrayList<Attribute>();
        elements = new ArrayList<Element>();
        this.name = name;
    }

    /**
     * Copy constructor. Not a truly deep copy, but close enough for most
     * purposes.
     * @param original
     */
    public XmlElement(XmlElement original) {
        super();
        attributes = new ArrayList<Attribute>();
        attributes.addAll(original.attributes);
        elements = new ArrayList<Element>();
        elements.addAll(original.elements);
        this.name = original.name;
    }

    /**
     * @return Returns the attributes.
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * @return Returns the elements.
     */
    public List<Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void addElement(int index, Element element) {
        elements.add(index, element);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append('<');
        sb.append(name);

        for (Attribute att : attributes) {
            sb.append(' ');
            sb.append(att.getFormattedContent());
        }

        if (elements.size() > 0) {
            sb.append(" >"); //$NON-NLS-1$
            for (Element element : elements) {
                OutputUtilities.newLine(sb);
                sb.append(element.getFormattedContent(indentLevel + 1));
            }
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, indentLevel);
            sb.append("</"); //$NON-NLS-1$
            sb.append(name);
            sb.append('>');
            /**
             * @author XT
             */
            if (indentLevel == 1) {
                Boolean haveUserId = false;// 确定该表是否有userId
                String field = null;
                for (Element element : elements) {
                    if (element instanceof XmlElement) {
                        for (Attribute attribute : ((XmlElement) element).getAttributes()) {
                            if ("column".equalsIgnoreCase(attribute.getName())) {
                                if (attribute.getValue().toLowerCase().indexOf("user_id") != -1) {
                                    haveUserId = true;
                                    field = attribute.getValue();
                                    break;
                                }
                            }
                        }
                    }
                    if (haveUserId) {
                        break;
                    }
                }
                if (!XmlElement.tableName.toLowerCase().endsWith("_rel")) {// 不是关联关系表，且字段有userId属性
                    sb = generatorWhere(sb,haveUserId);// 生成where
                    sb = generatorQueryMapByCondition(sb,haveUserId,field);// 生成一个默认的方法
                }
            }
            /**
             * @author XT
             */
        } else {
            sb.append(" />"); //$NON-NLS-1$
        }

        return sb.toString();
    }
    /**
     * 生成查询sql
     * 涉及到用户表,这里写死了的 sys_user
     * @param sb
     * @param haveUserId 表是否包含user_id
     * @param 包含user_id 的字段
     * @return
     */
    private StringBuilder generatorQueryMapByCondition(StringBuilder sb, Boolean haveUserId,String field) {
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 1);
        sb.append("<select id=\"queryMapByCondition\" resultType=\"map\">");
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 2);
        sb.append("select "+(haveUserId?"distinct ":""));
        for (Element element : elements) {
            if (element instanceof XmlElement) {
                String filedName = null;
                String propertyName = null;
                for (Attribute attribute : ((XmlElement) element).getAttributes()) {
                    if ("column".equalsIgnoreCase(attribute.getName())) {
                        filedName = attribute.getValue();
                    }
                    if ("property".equalsIgnoreCase(attribute.getName())) {
                        propertyName = attribute.getValue();
                    }
                }
                if (!CommonUtils.hasBlank(filedName, propertyName)) {
                    OutputUtilities.newLine(sb);
                    OutputUtilities.xmlIndent(sb, 3);
                    sb.append(XmlElement.tableNameSimple + "." + filedName + " as " + propertyName +",");
                }
            }
        }
        if(haveUserId && !FreeMarkerGenerator.userTableName.equalsIgnoreCase(XmlElement.tableName)){
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, 3);
            sb.append("su.name name,");
        }
        sb.deleteCharAt(sb.length()-1);//删除最后一个逗号
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 2);
        sb.append("from "+XmlElement.tableName+" "+XmlElement.tableNameSimple);
        if(!FreeMarkerGenerator.userTableName.equalsIgnoreCase(XmlElement.tableName) && haveUserId){//如果不为用户表且有user_id字段就添加inner join
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, 2);
            sb.append("inner join "+FreeMarkerGenerator.userTableName+" su on su.user_id = "+XmlElement.tableNameSimple+"."+field);//用户表这里写死了。
        }
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 2);
        sb.append("<include refid=\"baseWhere\"></include>");
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 1);
        sb.append("</select>");
        return sb;
    }
    /**
     * 生成where条件
     * @param sb
     * @param haveUserId
     * @return
     */
    private StringBuilder generatorWhere(StringBuilder sb,Boolean haveUserId) {
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 1);
        sb.append("<sql id=\"baseWhere\">");
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 2);
        sb.append("<trim prefix=\"where\" prefixOverrides=\"and | or\">");
        boolean isKeyword = false;
        for (Element element : elements) {
            if (element instanceof XmlElement) {
                String filedName = null;
                String propertyName = null;
                Boolean isString = false;
                for (Attribute attribute : ((XmlElement) element).getAttributes()) {
                    if ("column".equalsIgnoreCase(attribute.getName())) {
                        filedName = attribute.getValue();
                    }
                    if ("property".equalsIgnoreCase(attribute.getName())) {
                        propertyName = attribute.getValue();
                    }
                    if ("jdbcType".equalsIgnoreCase(attribute.getName())) {
                        isString = "VARCHAR".equalsIgnoreCase(attribute.getValue());
                    }
                }
                if (!CommonUtils.hasBlank(filedName, propertyName)) {
                    OutputUtilities.newLine(sb);
                    OutputUtilities.xmlIndent(sb, 3);
                    sb.append("<if test=\"" + propertyName + " != null" + (isString ? " and " + propertyName + " != ''" : "") + "\">");
                    OutputUtilities.newLine(sb);
                    OutputUtilities.xmlIndent(sb, 4);
                    String query = null;
                    if(filedName.toLowerCase().endsWith("name")){// 表中字段以name结尾 ，InnerClass 一样涉及模糊查询
                        query = "like #{only" + CommonUtils.toUpperInitial(propertyName) + "}";
                    }else{
                        query = "= #{" + propertyName + "}";
                    }
                    sb.append("and " + XmlElement.tableNameSimple + "." + filedName +" "+query);
                    OutputUtilities.newLine(sb);
                    OutputUtilities.xmlIndent(sb, 3);
                    sb.append("</if>");
                    if(!isKeyword){//是否已经添加了一次（只能添加一次）
                        if(filedName.toLowerCase().endsWith("name")){
                            isKeyword = true;
                            OutputUtilities.newLine(sb);//添加关键字查询条件
                            sb.append("      <if test=\"keyword != null and keyword != ''\">\n");
                            sb.append("        and ("+XmlElement.tableNameSimple+"."+filedName+" like #{onlyKeyword} <!-- or 字段二 like #{onlyKeyword} -->)\n");
                            sb.append("      </if>");
                        }
                    }
                }
            }
        }
        OutputUtilities.newLine(sb);//添加时间段查询条件(默认操作时候utime)
        sb.append("      <if test=\"startDate != null\">\n");
        sb.append("        and "+XmlElement.tableNameSimple+".utime &gt; #{startDate}\n");
        sb.append("      </if>");
        OutputUtilities.newLine(sb);//添加时间段查询条件(默认操作时候utime)
        sb.append("      <if test=\"endDate != null\">\n");
        sb.append("        and "+XmlElement.tableNameSimple+".utime &lt; #{endDate}\n");
        sb.append("      </if>");
        if(haveUserId && !FreeMarkerGenerator.userTableName.equalsIgnoreCase(XmlElement.tableName)){
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, 3);
            sb.append("<if test=\"name != null and name != ''\">");
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, 4);
            sb.append("and su.name like #{onlyName}");
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, 3);
            sb.append("</if>");
        }
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 2);
        sb.append("</trim>");
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 1);
        sb.append("</sql>");
        return sb;
    }

    public void setName(String name) {
        this.name = name;
    }
}
