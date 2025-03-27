package com.xiao.util;

import com.xiao.bean.Message;
import com.xiao.bean.Person;
import com.xiao.bean.Group;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

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
} 