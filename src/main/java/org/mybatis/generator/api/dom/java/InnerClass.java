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
package org.mybatis.generator.api.dom.java;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.dom.OutputUtilities;

import com.project.common.util.CommonUtils;


/**
 * This class encapsulates the idea of an inner class - it has methods that make
 * it easy to generate inner classes.
 * @author Jeff Butler
 */
public class InnerClass extends JavaElement {

    private List<Field> fields;

    private List<InnerClass> innerClasses;

    private List<InnerEnum> innerEnums;

    private FullyQualifiedJavaType superClass;

    private FullyQualifiedJavaType type;

    private Set<FullyQualifiedJavaType> superInterfaceTypes;

    private List<Method> methods;

    private boolean isAbstract;

    private List<InitializationBlock> initializationBlocks;

    /**
     *  
     */
    public InnerClass(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        fields = new ArrayList<Field>();
        innerClasses = new ArrayList<InnerClass>();
        innerEnums = new ArrayList<InnerEnum>();
        superInterfaceTypes = new HashSet<FullyQualifiedJavaType>();
        methods = new ArrayList<Method>();
        initializationBlocks = new ArrayList<InitializationBlock>();
    }

    public InnerClass(String typeName) {
        this(new FullyQualifiedJavaType(typeName));
    }

    /**
     * @return Returns the fields.
     */
    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    /**
     * @return Returns the superClass.
     */
    public FullyQualifiedJavaType getSuperClass() {
        return superClass;
    }

    /**
     * @param superClass The superClass to set.
     */
    public void setSuperClass(FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }

    public void setSuperClass(String superClassType) {
        this.superClass = new FullyQualifiedJavaType(superClassType);
    }

    /**
     * @return Returns the innerClasses.
     */
    public List<InnerClass> getInnerClasses() {
        return innerClasses;
    }

    public void addInnerClass(InnerClass innerClass) {
        innerClasses.add(innerClass);
    }

    public List<InnerEnum> getInnerEnums() {
        return innerEnums;
    }

    public void addInnerEnum(InnerEnum innerEnum) {
        innerEnums.add(innerEnum);
    }

    public List<InitializationBlock> getInitializationBlocks() {
        return initializationBlocks;
    }

    public void addInitializationBlock(InitializationBlock initializationBlock) {
        initializationBlocks.add(initializationBlock);
    }

    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        addFormattedJavadoc(sb, indentLevel);
        addFormattedAnnotations(sb, indentLevel);

        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append(getVisibility().getValue());

        if (isAbstract()) {
            sb.append("abstract "); //$NON-NLS-1$
        }

        if (isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        sb.append("class "); //$NON-NLS-1$
        sb.append(getType().getShortName());

        if (superClass != null) {
            sb.append(" extends "); //$NON-NLS-1$
            sb.append(superClass.getShortName());
        }

        if (superInterfaceTypes.size() > 0) {
            sb.append(" implements "); //$NON-NLS-1$

            boolean comma = false;
            for (FullyQualifiedJavaType fqjt : superInterfaceTypes) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }

                sb.append(fqjt.getShortName());
            }
        }

        sb.append(" {"); //$NON-NLS-1$
        indentLevel++;

        Iterator<Field> fldIter = fields.iterator();
//        Boolean isAddApiModelProperty = false;
        while (fldIter.hasNext()) {
            OutputUtilities.newLine(sb);
            Field field = fldIter.next();
            /**
             * @author XT
             */
            /*if(!XmlElement.tableName.toLowerCase().endsWith("_rel")){
                StringBuffer ssb = new StringBuffer();
                for (String line : field.getJavaDocLines()) {
                    ssb.append(line+",");
                }
                String commet = ssb.toString().replace("/", "").replace("*", "").replace(",", "").trim();
                if(ValidateUtils.isNotBlank(commet)){
                    if(!isAddApiModelProperty){
                        sb.insert(0, "import io.swagger.annotations.ApiModelProperty;\n");
                        isAddApiModelProperty =true;//只引入一次
                    }
                    field.getAnnotations().add("@ApiModelProperty(value=\""+commet+"\")//swagger api文档 response显示 ");
                }
            }*/
            /**
             * @author XT
             */
            sb.append(field.getFormattedContent(indentLevel));
            /**
             * @author XT
             */
            if (field.getName().toLowerCase().endsWith("name")) {
                OutputUtilities.newLine(sb);
                OutputUtilities.newLine(sb);
//                sb.append("    @ApiModelProperty(hidden=true)\n");
                sb.append("    public String getOnly" + CommonUtils.toUpperInitial(field.getName()) + "(){\n");
                sb.append("        if(" + field.getName() + " == null){ \n");
                sb.append("            return null;      \n");
                sb.append("        }                     \n");
                sb.append("        return \"%\"+" + field.getName() + ".trim()+\"%\";\n");
                sb.append("    }");
            }
            /**
             * @author XT
             */
            if (fldIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        if (initializationBlocks.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<InitializationBlock> blkIter = initializationBlocks.iterator();
        while (blkIter.hasNext()) {
            OutputUtilities.newLine(sb);
            InitializationBlock initializationBlock = blkIter.next();
            sb.append(initializationBlock.getFormattedContent(indentLevel));
            if (blkIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        if (methods.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<Method> mtdIter = methods.iterator();
        while (mtdIter.hasNext()) {
            OutputUtilities.newLine(sb);
            Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, false));
            if (mtdIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        if (innerClasses.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        Iterator<InnerClass> icIter = innerClasses.iterator();
        while (icIter.hasNext()) {
            OutputUtilities.newLine(sb);
            InnerClass innerClass = icIter.next();
            sb.append(innerClass.getFormattedContent(indentLevel));
            if (icIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        if (innerEnums.size() > 0) {
            OutputUtilities.newLine(sb);
        }

        Iterator<InnerEnum> ieIter = innerEnums.iterator();
        while (ieIter.hasNext()) {
            OutputUtilities.newLine(sb);
            InnerEnum innerEnum = ieIter.next();
            sb.append(innerEnum.getFormattedContent(indentLevel));
            if (ieIter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }

        indentLevel--;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }

    /**
     * @return Returns the superInterfaces.
     */
    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }

    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }

    /**
     * @return Returns the methods.
     */
    public List<Method> getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * @return Returns the type.
     */
    public FullyQualifiedJavaType getType() {
        return type;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbtract) {
        this.isAbstract = isAbtract;
    }
}
