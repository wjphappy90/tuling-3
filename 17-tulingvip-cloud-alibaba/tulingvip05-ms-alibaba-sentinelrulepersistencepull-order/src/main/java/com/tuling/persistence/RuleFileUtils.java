package com.tuling.persistence;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:sentinel 规则文件存储工具类
* @author: smlz
* @createDate: 2019/11/28 16:30
* @version: 1.0
*/
@Slf4j
public class RuleFileUtils {


    /**
     * 方法实现说明:若路径不存在就创建路径
     * @author:smlz
     * @param filePath:文件存储路径
     * @return: void
     * @exception: IOException
     * @date:2019/11/28 16:32
     */
    public static void mkdirIfNotExits(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            log.info("创建Sentinel规则目录:{}",filePath);
            file.mkdirs();
        }
    }


    /**
     * 方法实现说明:若文件不存在就创建路径
     * @author:smlz
     * @param ruleFileMap 规则存储文件
     * @return: void
     * @exception: IOException
     * @date:2019/11/28 16:32
     */
    public static void createFileIfNotExits(Map<String,String> ruleFileMap) throws IOException {

        Set<String> ruleFilePathSet = ruleFileMap.keySet();

        Iterator<String> ruleFilePathIter = ruleFilePathSet.iterator();

        while (ruleFilePathIter.hasNext()) {
            String ruleFilePathKey = ruleFilePathIter.next();
            String ruleFilePath  = PersistenceRuleConstant.rulesMap.get(ruleFilePathKey).toString();
            File ruleFile = new File(ruleFilePath);
            if(!ruleFile.exists()) {
                log.info("创建Sentinel 规则文件:{}",ruleFile);
                ruleFile.createNewFile();
            }
        }
    }

}