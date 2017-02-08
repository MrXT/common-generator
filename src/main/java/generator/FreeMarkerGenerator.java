
package generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.project.common.util.CommonUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import generator.bean.FiledCommet;
import generator.bean.TableBean;
/**
 * 
 * FreeMarker工具类
 * 功能: 用于根据模板生成contorller,service及serviceImpl代码模板.
 * @author XT
 * @version 2015-03-30
 */
public class FreeMarkerGenerator {
    
    private static String modulePackage = CommonUtils.readResource("mapper.modulePackage", "generator/generator.properties");
    private static String xmlPackage = CommonUtils.readResource("mapper.xmlPackage", "generator/generator.properties");
    private static String mappers = CommonUtils.readResource("mapper.mappers", "generator/generator.properties");
    private static String targetJavaProject = CommonUtils.readResource("targetJavaProject", "generator/generator.properties");
    private static String targetResourcesProject = CommonUtils.readResource("targetResourcesProject", "generator/generator.properties");
    private static String entityPackage = CommonUtils.readResource("mapper.entityPackage", "generator/generator.properties");
    private static String entitySuperClass = CommonUtils.readResource("mapper.entitySuperClass", "generator/generator.properties");
    public static String jdbcUrl = CommonUtils.readResource("jdbc.url", "generator/generator.properties");
    public static String jdbcUsername = CommonUtils.readResource("jdbc.username", "generator/generator.properties");
    public static String jdbcPassword = CommonUtils.readResource("jdbc.password", "generator/generator.properties");
    public static String jdbcDriverClass = CommonUtils.readResource("jdbc.driver-class-name", "generator/generator.properties");
    public static String userTableName = CommonUtils.readResource("userTableName", "generator/generator.properties");
    
    /** 模板配置 */
    private Configuration cfg;
    /** Controller模板*/
    private final static int FTL_TYPE_CONTROLLER = 1;
    /** Service模板*/
    private final static int FTL_TYPE_SERVICE = 2;
    /** ServiceImpl模板*/
    private final static int FTL_TYPE_SERVICEIMPL = 3;
    /** 带查询功能的JSP模板*/
    private final static int FTL_TYPE_MANAGE_JSP = 4;
    /** 信息编辑JSP模板*/
    private final static int FTL_TYPE_EDIT_JSP = 5;
    /** 可编辑的JSP模板*/
    private final static int FTL_TYPE_MANAGE_JSP_2 = 6;
    /** 扩张查询参数*/
    private final static int FTL_TYPE_VO = 7;
    /**generatorConfig mybatis生成所需要的xml*/
    private final static int FTL_TYPE_GENERATOR_XML = 8;
    /** 模板路径*/
    private final static String TEMPLATE_PATH = "/generator/template";
    /** 生成文件人 */
    private final static String AUTH_NAME = System.getProperty("user.name");//系统用户名a
    //private final static String TEMPLATE_PATH = new File(System.getProperty("user.dir")+"//WebRoot//WEB-INF//classes//template
    /** 是否生成页面 */
    private boolean isCreateJsp;
    
    /** 是否生成generatorConfig.xml */
    private boolean isCreateXml;
    
    
    public boolean isCreateXml() {
        return isCreateXml;
    }
    
    public void setCreateXml(boolean isCreateXml) {
        this.isCreateXml = isCreateXml;
    }
    public FreeMarkerGenerator(boolean isCreateJsp,boolean isCreateXml) {
        this.isCreateJsp = isCreateJsp;
        this.isCreateXml = isCreateXml;
    }
    /**
     *  通过表实体生成文件
     * @param tableBean 表实体
     * @throws IOException
     */
    private boolean generate(TableBean tableBean) throws IOException {
        // 初始化FreeMarker配置,设置FreeMarker的模版文件位置
        cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setClassForTemplateLoading(this.getClass(), TEMPLATE_PATH);
//        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
        cfg.setLocale(Locale.CHINA);
        cfg.setDefaultEncoding("UTF-8");
        
        //生成代码
        if(!isCreateXml){
            generateFile(FTL_TYPE_SERVICE,tableBean);
            generateFile(FTL_TYPE_SERVICEIMPL,tableBean);
            generateFile(FTL_TYPE_CONTROLLER,tableBean);
            generateFile(FTL_TYPE_VO,tableBean);
        }else{
            return generateFile(FTL_TYPE_GENERATOR_XML,tableBean);
        }
        if(isCreateJsp){
            generateFile(FTL_TYPE_MANAGE_JSP,tableBean);
        }
        return true;
    }
    
    private boolean generateFile(int ftlType,TableBean tableBean){
        String moduleName = tableBean.getBasePackage().substring(tableBean.getBasePackage().lastIndexOf(".")+1);//模块名
        String simpleEntityName = toLowerInitial(toLowerInitial(tableBean.getEntityName()).replace(moduleName, ""));
        // 获取参数集合
        Map<String, Object> root = new HashMap<String,Object>();
        root.put("modulePackage", tableBean.getBasePackage());//模块包名
        root.put("domainPackage", entityPackage);//实体包名
        root.put("entityName", tableBean.getEntityName());//实体名称
        root.put("simpleEntityName", simpleEntityName);//实体简单名称
        root.put("moduleName", moduleName);//模块名
        root.put("entityCommet", tableBean.getCommet());
        try {
            root.put("haveUserId", tableBean.hasUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //xml所需要的
        if(isCreateXml){
            root.put("mappers", mappers);
            root.put("entitySuperClass", entitySuperClass);
            root.put("targetJavaProject",targetJavaProject);
            root.put("targetResourcesProject", targetResourcesProject);
            root.put("entitySuperClass", entitySuperClass);
            root.put("jdbcDriverClass", jdbcDriverClass);
            root.put("jdbcPassword",jdbcPassword);
            root.put("jdbcUrl", jdbcUrl);
            root.put("jdbcUsername", jdbcUsername);
            root.put("targetXMLPackage", tableBean.getTargetXMLPackage());
            root.put("tableName", tableBean.getTableName());
            root.put("isRel", tableBean.getIsRel());
        }
        List<FiledCommet> commets = tableBean.getFiledCommets();
        root.put("filedCommets", commets);//属性名称集合
        StringBuilder apiResponsejson = new StringBuilder();
        StringBuilder apiRequestjson = new StringBuilder();
        for (FiledCommet commet :commets) {
            if(commet.getPropertyName().equals("name")){
                apiResponsejson.append("@ApiResponseDesc(value=\"userId\",description=\"用户ID\"),");
                apiRequestjson.append("@ApiImplicitParam(name=\"userId\",paramType=\"query\",dataType=\"String\",value=\"用户ID\"),");
            }else{
                apiRequestjson.append("@ApiImplicitParam(name=\""+commet.getPropertyName()+"\",paramType=\"query\",dataType=\""+CommonUtils.toUpperInitial(commet.getType())+"\",value=\""+commet.getColumnName()+"\"),");
            }
            String apiResponseType = null;
            switch (commet.getType()) {
                case "string":
                    apiResponseType = "ApiResponseType.STRING";
                    break;
                case "boolean":
                    apiResponseType = "ApiResponseType.BOOLEAN";
                    break;
                case "integer":
                    apiResponseType = "ApiResponseType.INTEGER";
                    break;
                default:
                    apiResponseType = "ApiResponseType.STRING";
                    break;
            }
            Boolean isString = commet.getType().equals("string")?true:false;
            apiResponsejson.append("@ApiResponseDesc(value=\""+commet.getPropertyName()+"\""+(isString?"":",type=\""+apiResponseType+"\"")+",description=\""+commet.getColumnName()+"\"),");
        }
        if(apiResponsejson.length()>0){
            apiResponsejson.deleteCharAt(apiResponsejson.length()-1);
        }
        if(apiRequestjson.length()>0){
            apiRequestjson.deleteCharAt(apiRequestjson.length()-1);
        }
        root.put("apiResponsejsons", apiResponsejson.toString());//api　response json
        root.put("apiRequestjsons", apiRequestjson.toString());//api　response json
        //结束
        root.put("authName", AUTH_NAME);
        if(isCreateJsp){
            StringBuilder sb = new StringBuilder("var jsons = [");
            for (FiledCommet commet :commets) {
                sb.append("{\"columnName\":\""+commet.getColumnName()+"\",\"propertyName\":\""+commet.getPropertyName()+"\"},");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("];");
            root.put("jsons", sb.toString());
        }
        
        String typeName = "";
        String fileDir = "";
        String fileName = "";
        String entityName = tableBean.getEntityName();
        // 设置文件存放位置
        switch (ftlType) {
            case FTL_TYPE_CONTROLLER:
                typeName = "Controller";
                fileDir = "src/main/java/"+tableBean.getBasePackage().replace(".", "/")+"/controller/";
                fileName = fileDir + entityName + typeName + ".java";
                break;
            case FTL_TYPE_VO:
                typeName = "VO";
                fileDir = "src/main/java/"+tableBean.getBasePackage().replace(".", "/")+"/controller/vo/";
                fileName = fileDir + entityName + typeName + ".java";
                break;
            case FTL_TYPE_GENERATOR_XML:
                typeName = "generatorConfig";
                fileDir = "src/main/resources/generator/";
                fileName = fileDir+"generatorConfig.xml";
                break;
            case FTL_TYPE_SERVICE:
                typeName = "Service";
                fileDir = "src/main/java/"+tableBean.getBasePackage().replace(".", "/")+"/service/";
                fileName = fileDir + entityName + typeName + ".java";
                break;
            case FTL_TYPE_SERVICEIMPL:
                typeName = "ServiceImpl";
                fileDir = "src/main/java/"+tableBean.getBasePackage().replace(".", "/")+"/service/impl/";
                fileName = fileDir + entityName + typeName + ".java";
                break;
            case FTL_TYPE_MANAGE_JSP:
                typeName = "html";
                fileDir = "src/main/resources/templates/"+moduleName+"/";
                fileName = fileDir + simpleEntityName + "_list.html";
                break;
            case FTL_TYPE_EDIT_JSP:
                typeName = "EditJsp";
                fileDir = "WebRoot/WEB-INF/jsp/"+moduleName+"/";
                fileName = fileDir + simpleEntityName + "_edit.jsp";
                break;
            case FTL_TYPE_MANAGE_JSP_2:
                typeName = "ManageJsp2";
                fileDir = "WebRoot/WEB-INF/jsp/"+moduleName+"/";
                fileName = fileDir + simpleEntityName + "_manage2.jsp";
            default:
                break;
        }
        
        try {
            File dir = new File(fileDir);
            FileUtils.forceMkdir(dir);
            Template template = cfg.getTemplate(typeName+".ftl");
            Writer out = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
            template.process(root, out);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 将指定字符的首字母小写
     * @param str 需要转换的字符
     */
    public String toLowerInitial(String str){
        char ch[];
        ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }
    /**
     * 生成code
     * @param isCreateServiceController 是否创建service与controller
     * @param isCreateXml 是否创建xml,dao,entity
     * @param tableNames 表名数组 
     * 表名格式 （moduleName 模块名）
     * 1.关联表：moduleName_tableName1_tableName2_rel ...  sys_user_role_rel 用户角色关联表
     * 2.单表:moduleName_tableName ... sys_user 用户表
     * @return
     * @throws IOException 
     * @throws Exception 
     */
    public static Boolean generatorCode(Boolean isCreateServiceController,Boolean isCreateXml,String ... tableNames) throws IOException, Exception{
        FreeMarkerGenerator fmg = new FreeMarkerGenerator(true,isCreateXml);
        //封装要生成的表实例
        List<TableBean> tableBeans = new ArrayList<TableBean>();
        //不允许生成的几张表（系统级别）
        String [] noGenatorTables = {"sys_user","sys_menu","sys_role","sys_user_role_rel","sys_role_menu_rel","tl_columncode","tl_tablecode"};
        for (String tableName : tableNames) {
            Boolean isHave = false;
            for (String string : noGenatorTables) {
                if(CommonUtils.compareString(tableName, string, true)){
                    isHave = true;
                    break;
                }
            }
            if(isHave){
                continue;
            }
            TableBean tableBean = new TableBean();
            tableName = tableName.toLowerCase();
            tableBean.setTableName(tableName);
            String moduleName = tableName.substring(0, tableName.indexOf("_"));
            tableBean.setBasePackage(modulePackage+"."+moduleName);
            tableBean.setTargetXMLPackage(xmlPackage+"."+moduleName);
            String entityName = FreeMarkerGenerator.handleEntityName(tableName.substring(tableName.indexOf("_")+1,tableName.length()));
            tableBean.setEntityName(entityName);
            if(tableName.endsWith("_rel")){
                 tableBean.setIsRel(true);
                 tableBean.setEntityName(entityName.substring(0,entityName.indexOf("Rel")));
            }else{
                 tableBean.setIsRel(false);
            }
            tableBeans.add(tableBean);
        }
        for (TableBean tableBean : tableBeans) {
            if(fmg.isCreateXml()){
                if(fmg.generate(tableBean)){//生成 generatorConfig.xml
                    fmg.mybatisGenerator();//生成xml,dao,entity
                    if(isCreateServiceController){//
                        if(!tableBean.getIsRel()){
                            fmg.setCreateXml(false);
                            //生成controller与service
                            fmg.generate(tableBean);
                            fmg.setCreateXml(true);
                        }
                    }
                }
            }else{
                if(isCreateServiceController){
                    if(!tableBean.getIsRel()){
                        //生成controller与service
                        fmg.generate(tableBean);
                    }
                }
            }
        }
        return true;
    }
    private void mybatisGenerator() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        org.mybatis.generator.config.Configuration config = cp.parseConfiguration(new File("src/main/resources/generator/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
    /**
     * 处理表名变为驼峰，首字母大写
     * @param entityNameStr
     * @return
     */
    public static String handleEntityName(String entityNameStr) {
        String []entityNameStrs = entityNameStr.split("_");
        StringBuilder sb = new StringBuilder();
        for (String entityName : entityNameStrs) {
            sb.append(CommonUtils.toUpperInitial(entityName));
        }
        return sb.toString();
    }
    /**
     * 注意备份，该方法会覆盖你之前的代码
     * 通过数据库来生成代码(默认生成包含dao,xml,entity)
     * @param isCreateServiceController 是否生成serviceController
     * @return 
     * @return
     * @throws SQLException 
     */
    public static Boolean generatorCode(boolean isCreateServiceController) throws Exception{
      //注册驱动  
        Class.forName(jdbcDriverClass);  
        //得到连接  
        Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);  
        DatabaseMetaData   dbMetaData   =   conn.getMetaData();    
        //可为:"TABLE",   "VIEW",   "SYSTEM   TABLE",      
        //"GLOBAL   TEMPORARY",   "LOCAL   TEMPORARY",   "ALIAS",   "SYNONYM"    
        String[]   types   =   {"TABLE"};    
        ResultSet   tabs   =   dbMetaData.getTables(null,   null,   null,types/*只要表就好了*/);    
        /*记录集的结构如下:   
            TABLE_CAT       String   =>   table   catalog   (may   be   null)     
            TABLE_SCHEM   String   =>   table   schema   (may   be   null)     
            TABLE_NAME     String   =>   table   name     
            TABLE_TYPE     String   =>   table   type.     
            REMARKS           String   =>   explanatory   comment   on   the   table     
            TYPE_CAT         String   =>   the   types   catalog   (may   be   null)     
            TYPE_SCHEM     String   =>   the   types   schema   (may   be   null)     
            TYPE_NAME       String   =>   type   name   (may   be   null)     
            SELF_REFERENCING_COL_NAME   String   =>   name   of   the   designated   "identifier"   column   of   a   typed   table   (may   be   null)     
            REF_GENERATION   String   =>   specifies   how   values   in   SELF_REFERENCING_COL_NAME   are   created.   Values   are   "SYSTEM",   "USER",   "DERIVED".   (may   be   null)     
          */    
        tabs.last();
        String tables[] = new String[tabs.getRow()];
        tabs.beforeFirst();
        int i =0 ;
        while(tabs.next()){    
            tables[i++] = tabs.getObject("TABLE_NAME").toString();    
        }
        conn.close();
        return generatorCode(isCreateServiceController,true,tables);
    }
    
}
