package com.xiao.util;

import com.xiao.bean.Message;
import com.xiao.bean.Person;
import com.xiao.bean.Group;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Neo4j工具类，用于创建节点和关系
 */
@Component
public class Neo4jUtil {
    
    @Autowired
    private Session session;
    
    /**
     * 创建Message节点
     * @param message 消息实体
     * @return 创建的节点ID
     */
    public Long createMessageNode(Message message) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("wxId", message.getWxId());
        parameters.put("message", message.getMessage());
        parameters.put("isExtreme", message.getIsExtreme());
        parameters.put("time", message.getTime());
        parameters.put("groupId", message.getGroupId());
        
        String query = "CREATE (m:Message {wxId: $wxId, message: $message, " +
                "isExtreme: $isExtreme, time: $time, groupId: $groupId}) RETURN id(m) as nodeId";
        
        Result result = session.run(query, parameters);
        return result.single().get("nodeId").asLong();
    }
    
    /**
     * 创建Person节点
     * @param person 人员实体
     * @return 创建的节点ID
     */
    public Long createPersonNode(Person person) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", person.getId());
        parameters.put("idCode", person.getIdCode());
        parameters.put("wxId", person.getWxId());
        parameters.put("isReal", person.getIsReal());
        parameters.put("isImport", person.getIsImport());
        parameters.put("wxIds", person.getWxIds());
        parameters.put("phone", person.getPhone());
        parameters.put("address", person.getAddress());
        parameters.put("name", person.getName());
        parameters.put("personType", person.getPersonType());
        parameters.put("city", person.getCity());
        
        String query = "CREATE (p:Person {id: $id, idCode: $idCode, wxId: $wxId, isReal: $isReal, " +
                "isImport: $isImport, wxIds: $wxIds, phone: $phone, address: $address, " +
                "name: $name, personType: $personType, city: $city}) RETURN id(p) as nodeId";
        
        Result result = session.run(query, parameters);
        return result.single().get("nodeId").asLong();
    }
    
    /**
     * 创建Group节点
     * @param group 群组实体
     * @return 创建的节点ID
     */
    public Long createGroupNode(Group group) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", group.getId());
        parameters.put("groupId", group.getGroupId());
        parameters.put("groupName", group.getGroupName());
        parameters.put("groupOwnerWxId", group.getGroupOwnerWxId());
        parameters.put("groupOwnerName", group.getGroupOwnerName());
        parameters.put("groupOwnerIdCode", group.getGroupOwnerIdCode());
        parameters.put("groupOwnerPhone", group.getGroupOwnerPhone());
        parameters.put("groupOwnerAddress", group.getGroupOwnerAddress());
        
        String query = "CREATE (g:Group {id: $id, groupId: $groupId, groupName: $groupName, " +
                "groupOwnerWxId: $groupOwnerWxId, groupOwnerName: $groupOwnerName, " +
                "groupOwnerIdCode: $groupOwnerIdCode, groupOwnerPhone: $groupOwnerPhone, " +
                "groupOwnerAddress: $groupOwnerAddress}) RETURN id(g) as nodeId";
        
        Result result = session.run(query, parameters);
        return result.single().get("nodeId").asLong();
    }
    
    /**
     * 创建Person发送Message的关系
     * @param personWxId 人员微信ID
     * @param message 消息对象，用于精确匹配
     */
    public void createSentRelationship(String personWxId, Message message) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("personWxId", personWxId);
        parameters.put("wxId", message.getWxId());
        parameters.put("message", message.getMessage());
        parameters.put("isExtreme", message.getIsExtreme());
        parameters.put("time", message.getTime());
        parameters.put("groupId", message.getGroupId());
        
        String query = "MATCH (p:Person), (m:Message) " +
                "WHERE p.wxId = $personWxId " +
                "AND m.wxId = $wxId " +
                "AND m.message = $message " +
                "AND m.isExtreme = $isExtreme " +
                "AND m.time = $time " +
                "AND m.groupId = $groupId " +
                "CREATE (p)-[:Sent]->(m)";
        
        session.run(query, parameters);
    }
    
    /**
     * 创建Person是Group成员的关系
     * @param personWxId 人员微信ID
     * @param groupId 群组ID
     */
    public void createMemberRelationship(String personWxId, String groupId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("personWxId", personWxId);
        parameters.put("groupId", groupId);
        
        String query = "MATCH (p:Person), (g:Group) " +
                "WHERE p.wxId = $personWxId AND g.groupId = $groupId " +
                "CREATE (p)-[:Member]->(g)";
        
        session.run(query, parameters);
    }
    
    /**
     * 批量创建Message节点
     * @param messages 消息列表
     */
    public void batchCreateMessageNodes(List<Message> messages) {
        for (Message message : messages) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("wxId", message.getWxId());
            parameters.put("message", message.getMessage());
            parameters.put("isExtreme", message.getIsExtreme());
            parameters.put("time", message.getTime());
            parameters.put("groupId", message.getGroupId());
            
            String query = "CREATE (m:Message {wxId: $wxId, message: $message, " +
                    "isExtreme: $isExtreme, time: $time, groupId: $groupId})";
            
            session.run(query, parameters);
        }
    }
    
    /**
     * 批量创建Person节点
     * @param persons 人员列表
     */
    public void batchCreatePersonNodes(List<Person> persons) {
        for (Person person : persons) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", person.getId());
            parameters.put("idCode", person.getIdCode());
            parameters.put("wxId", person.getWxId());
            parameters.put("isReal", person.getIsReal());
            parameters.put("isImport", person.getIsImport());
            parameters.put("wxIds", person.getWxIds());
            parameters.put("phone", person.getPhone());
            parameters.put("address", person.getAddress());
            parameters.put("name", person.getName());
            parameters.put("personType", person.getPersonType());
            parameters.put("city", person.getCity());
            
            String query = "CREATE (p:Person {id: $id, idCode: $idCode, wxId: $wxId, isReal: $isReal, " +
                    "isImport: $isImport, wxIds: $wxIds, phone: $phone, address: $address, " +
                    "name: $name, personType: $personType, city: $city})";
            
            session.run(query, parameters);
        }
    }
    
    /**
     * 批量创建Group节点
     * @param groups 群组列表
     */
    public void batchCreateGroupNodes(List<Group> groups) {
        for (Group group : groups) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", group.getId());
            parameters.put("groupId", group.getGroupId());
            parameters.put("groupName", group.getGroupName());
            parameters.put("groupOwnerWxId", group.getGroupOwnerWxId());
            parameters.put("groupOwnerName", group.getGroupOwnerName());
            parameters.put("groupOwnerIdCode", group.getGroupOwnerIdCode());
            parameters.put("groupOwnerPhone", group.getGroupOwnerPhone());
            parameters.put("groupOwnerAddress", group.getGroupOwnerAddress());
            
            String query = "CREATE (g:Group {id: $id, groupId: $groupId, groupName: $groupName, " +
                    "groupOwnerWxId: $groupOwnerWxId, groupOwnerName: $groupOwnerName, " +
                    "groupOwnerIdCode: $groupOwnerIdCode, groupOwnerPhone: $groupOwnerPhone, " +
                    "groupOwnerAddress: $groupOwnerAddress})";
            
            session.run(query, parameters);
        }
    }
    
    /**
     * 执行Cypher查询并返回结果列表
     * @param cypher Cypher查询语句
     * @param params 查询参数
     * @return 结果列表
     */
    public List<Map<String, Object>> executeCypher(String cypher, Map<String, Object> params) {
        Result result = session.run(cypher, params);
        List<Map<String, Object>> list = new ArrayList<>();
        while (result.hasNext()) {
            Record record = result.next();
            Map<String, Object> map = new HashMap<>();
            record.keys().forEach(key -> map.put(key, record.get(key).asObject()));
            list.add(map);
        }
        return list;
    }
    
    /**
     * 查询单个节点
     * @param cypher Cypher查询语句
     * @param nodeName 节点名称
     * @return 节点属性列表
     */
    public List<Map<String, Object>> queryNodes(String cypher, String nodeName) {
        Result result = session.run(cypher);
        List<Map<String, Object>> list = new ArrayList<>();
        while (result.hasNext()) {
            Value value = result.next().get(nodeName);
            list.add(value.asMap());
        }
        return list;
    }





    /**
     * 通用创建节点方法，使用反射获取实体类信息
     * @param entity 实体对象
     * @param <T> 实体类型
     * @return 创建的节点ID
     */
    public <T> Long createNode(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("实体对象不能为空");
        }

        try {
            // 获取实体类名作为标签名
            String label = entity.getClass().getSimpleName();

            // 使用反射获取所有字段
            Field[] fields = entity.getClass().getDeclaredFields();

            // 准备参数和Cypher查询的属性部分
            Map<String, Object> parameters = new HashMap<>();
            StringBuilder propertyPairs = new StringBuilder();

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                String propertyName = field.getName();
                Object propertyValue = field.get(entity);

                // 跳过null值
                if (propertyValue != null) {
                    // 添加属性名和参数占位符到Cypher
                    if (propertyPairs.length() > 0) {
                        propertyPairs.append(", ");
                    }
                    propertyPairs.append(propertyName).append(": $").append(propertyName);

                    // 添加参数
                    parameters.put(propertyName, propertyValue);
                }
            }

            // 构建Cypher查询
            String query = "CREATE (n:" + label + " {" + propertyPairs.toString() + "}) RETURN id(n) as nodeId";

            // 执行查询
            Result result = session.run(query, parameters);
            return result.single().get("nodeId").asLong();

        } catch (IllegalAccessException e) {
            throw new RuntimeException("反射获取实体属性失败: " + e.getMessage(), e);
        }
    }

    /**
     * 批量创建节点
     * @param entities 实体对象列表
     * @param <T> 实体类型
     */
    public <T> void batchCreateNodes(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        // 获取第一个实体确定类名
        T firstEntity = entities.get(0);
        String label = firstEntity.getClass().getSimpleName();

        // 准备批处理
        for (T entity : entities) {
            try {
                // 使用反射获取所有字段
                Field[] fields = entity.getClass().getDeclaredFields();

                // 准备参数和Cypher查询的属性部分
                Map<String, Object> parameters = new HashMap<>();
                StringBuilder propertyPairs = new StringBuilder();

                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);

                    String propertyName = field.getName();
                    Object propertyValue = field.get(entity);

                    // 跳过null值
                    if (propertyValue != null) {
                        // 添加属性名和参数占位符到Cypher
                        if (propertyPairs.length() > 0) {
                            propertyPairs.append(", ");
                        }
                        propertyPairs.append(propertyName).append(": $").append(propertyName);

                        // 添加参数
                        parameters.put(propertyName, propertyValue);
                    }
                }

                // 构建Cypher查询
                String query = "CREATE (n:" + label + " {" + propertyPairs.toString() + "})";

                // 执行查询
                session.run(query, parameters);

            } catch (IllegalAccessException e) {
                throw new RuntimeException("反射获取实体属性失败: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 通用创建关系方法
     * @param sourceEntity 源实体
     * @param targetEntity 目标实体
     * @param relationshipType 关系类型
     * @param sourceIdentifier 源实体标识字段名
     * @param targetIdentifier 目标实体标识字段名
     * @param <S> 源实体类型
     * @param <T> 目标实体类型
     */
    public <S, T> void createRelationship(S sourceEntity, T targetEntity, String relationshipType,
                                          String sourceIdentifier, String targetIdentifier) {
        if (sourceEntity == null || targetEntity == null) {
            throw new IllegalArgumentException("源实体或目标实体不能为空");
        }

        try {
            // 获取实体类名作为标签名
            String sourceLabel = sourceEntity.getClass().getSimpleName();
            String targetLabel = targetEntity.getClass().getSimpleName();

            // 获取标识字段值
            Field sourceField = sourceEntity.getClass().getDeclaredField(sourceIdentifier);
            sourceField.setAccessible(true);
            Object sourceValue = sourceField.get(sourceEntity);

            Field targetField = targetEntity.getClass().getDeclaredField(targetIdentifier);
            targetField.setAccessible(true);
            Object targetValue = targetField.get(targetEntity);

            // 准备参数
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("sourceValue", sourceValue);
            parameters.put("targetValue", targetValue);

            // 构建Cypher查询
            String query = "MATCH (source:" + sourceLabel + "), (target:" + targetLabel + ") " +
                    "WHERE source." + sourceIdentifier + " = $sourceValue AND target." + targetIdentifier + " = $targetValue " +
                    "CREATE (source)-[:" + relationshipType + "]->(target)";

            // 执行查询
            session.run(query, parameters);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("反射获取实体属性失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建精确匹配关系
     * @param sourceEntity 源实体
     * @param targetEntity 目标实体
     * @param relationshipType 关系类型
     * @param <S> 源实体类型
     * @param <T> 目标实体类型
     */
    public <S, T> void createExactRelationship(S sourceEntity, T targetEntity, String relationshipType) {
        if (sourceEntity == null || targetEntity == null) {
            throw new IllegalArgumentException("源实体或目标实体不能为空");
        }

        try {
            // 获取实体类名作为标签名
            String sourceLabel = sourceEntity.getClass().getSimpleName();
            String targetLabel = targetEntity.getClass().getSimpleName();

            // 源实体匹配条件
            Map<String, Object> parameters = new HashMap<>();
            StringBuilder sourceCondition = new StringBuilder();
            Field[] sourceFields = sourceEntity.getClass().getDeclaredFields();

            for (int i = 0; i < sourceFields.length; i++) {
                Field field = sourceFields[i];
                field.setAccessible(true);

                String propertyName = field.getName();
                Object propertyValue = field.get(sourceEntity);

                if (propertyValue != null) {
                    if (sourceCondition.length() > 0) {
                        sourceCondition.append(" AND ");
                    }
                    sourceCondition.append("source.").append(propertyName).append(" = $source_").append(propertyName);
                    parameters.put("source_" + propertyName, propertyValue);
                }
            }

            // 目标实体匹配条件
            StringBuilder targetCondition = new StringBuilder();
            Field[] targetFields = targetEntity.getClass().getDeclaredFields();

            for (int i = 0; i < targetFields.length; i++) {
                Field field = targetFields[i];
                field.setAccessible(true);

                String propertyName = field.getName();
                Object propertyValue = field.get(targetEntity);

                if (propertyValue != null) {
                    if (targetCondition.length() > 0) {
                        targetCondition.append(" AND ");
                    }
                    targetCondition.append("target.").append(propertyName).append(" = $target_").append(propertyName);
                    parameters.put("target_" + propertyName, propertyValue);
                }
            }

            // 构建Cypher查询
            String query = "MATCH (source:" + sourceLabel + "), (target:" + targetLabel + ") " +
                    "WHERE " + sourceCondition.toString() + " AND " + targetCondition.toString() + " " +
                    "CREATE (source)-[:" + relationshipType + "]->(target)";

            // 执行查询
            session.run(query, parameters);

        } catch (IllegalAccessException e) {
            throw new RuntimeException("反射获取实体属性失败: " + e.getMessage(), e);
        }
    }
} 