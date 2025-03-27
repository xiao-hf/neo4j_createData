package com.xiao;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.xiao.bean.Group;
import com.xiao.bean.Message;
import com.xiao.bean.Person;
import com.xiao.util.Neo4jUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class Neo4j326ApplicationTests {

    @Resource
    Session session;

    @Test
    public void test() {
        System.out.println(11);
    }

    @Test
    public void connectNeo4j() {
        String cql = "match (n:学生) return n";
        Result result = session.run(cql);
        List<Map> list = new ArrayList<>();
        while (result.hasNext()) {
            Value value = result.next().get(0);
            list.add(value.asMap());
        }
        System.out.println(list);
    }

    @Resource
    Neo4jUtil neo4jUtil;

    @Test
    public void createData() {
        long id = 1, wxid = 1;
        List<List<String>> personTypes = new ArrayList<>();
        personTypes.add(List.of("投资酒类"));
        personTypes.add(List.of("中农集团"));
        personTypes.add(List.of("购买房产"));
        personTypes.add(Arrays.asList("投资酒类","中农集团"));
        personTypes.add(Arrays.asList("中农集团","购买房产"));
        personTypes.add(Arrays.asList("投资酒类","中农集团","购买房产"));

        List<String> addresses = Arrays.asList("南京市鼓楼区中山北路123号","上海市浦东新区陆家嘴环路888号","北京市朝阳区建国路456号","广州市天河区天河路789号","深圳市南山区科技园路10号","杭州市西湖区文三路15号","成都市锦江区红星路20号","武汉市江汉区解放大道25号","重庆市渝中区解放碑30号","西安市碑林区南大街35号","天津市和平区南京路40号","苏州市姑苏区平江路45号","青岛市市南区香港中路50号","长沙市芙蓉区五一大道55号","南昌市东湖区八一大道60号","合肥市庐阳区长江中路65号","郑州市金水区农业路70号","济南市历下区经十路75号","福州市鼓楼区五四路80号","厦门市思明区湖滨南路85号");
        List<String> groupNames = Arrays.asList("警务协作群","刑侦技术交流群","治安巡逻小队","特警行动组","网安情报分析群","公安内部交流","交警指挥中心","反诈联合行动组","社区警务工作站","警员业务培训群");
        List<String> personNames = Arrays.asList("张伟","王芳","李娜","刘洋","陈明","杨丽","赵鑫","黄强","周艳","吴超","徐亮","孙颖","马云","朱婷","胡杰","郭静","何涛","高峰","林萍","梁晨","谢辉","罗婷","宋杰","郑洁","唐涛","许梅","邓超","冯伟","韩雪","曹颖","彭磊","董丽","程刚","潘婷","袁华","姜涛","傅洋","蒋华","尹丽","熊伟","秦勇","江涛","贾静","夏磊","方芳","石磊","谭芳","廖婷","姚伟","白洁","江鑫","崔静","钟华","邹静","孟丽","龙华","万芳","段磊","曾婷","范磊","金鑫","葛涛","丁洁","史伟","苏芳","魏磊","毛丽","任超","卢静","沈阳","姬磊","汪芳","常鑫","章伟","陆芳","杜洁","阮磊","贺芳","顾鑫","武伟","孔超","齐芳","莫伟","严鑫","戚超","温芳","韦鑫","欧阳伟","尚超","康婷","辛鑫","阎芳","项伟","柳芳","安鑫","易超","乔婷","翁超","储芳","盛鑫","雷洁","于超","时芳","倪鑫","牛超","付芳","申鑫");

        List<Group> groups = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        List<Message> messages = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Group group = new Group();
            group.setId(id);
            group.setGroupId(Long.toString(id++));
            groups.add(group);
        }
        for (int i = 1; i <= 100; i++) {
            Person person = new Person();
            person.setId(id++);
            person.setName(personNames.get(i - 1));
            person.setIdCode(randomS(18));
            person.setPhone(randomNumStr(11));
            person.setAddress(randomT(addresses));
            persons.add(person);
        }
        for (int i = 1; i <= 1000; i++) {
            Message message = new Message();
            messages.add(message);
        }

        for (int i = 1; i <= 10; i++) {
            Group group = groups.get(i - 1);
            group.setGroupName(randomT(groupNames));
            Person owner = persons.get(i);
            group.setGroupOwnerWxId(owner.getId().toString());
            group.setGroupOwnerName(owner.getName());
            group.setGroupOwnerIdCode(owner.getIdCode());
            group.setGroupOwnerPhone(owner.getPhone());
            group.setGroupOwnerAddress(owner.getAddress());
        }

        for (int i = 1; i <= 100; i++) {
            Person person = persons.get(i - 1);
            person.setWxId(Long.toString(wxid++));
            person.setCity(person.getAddress().substring(0, 2));
            person.setPersonType(randomT(personTypes));
            person.setIsReal(randomBool());
            if (person.getIsReal()) {
                List<String> wxids = new ArrayList<>();
                wxids.add(person.getWxId());
                for (int j = 1; j <= randomInt(1, 3); j++) {
                    wxids.add(Long.toString(wxid++));
                }
                person.setWxIds(wxids);
            }
            person.setIsImport(randomBool());
        }
        for (int i = 1; i <= 1000; i++) {
            int j = (i + 9) / 10;
            Person person = persons.get(j - 1);

            Message message = messages.get(i - 1);
            message.setWxId(person.getWxId());
            message.setMessage(randomS(20));
            message.setIsExtreme(randomBool());
            message.setTime(randomDate().getTime());

            int k = (i + 99) / 100 - 1;
            Group group = groups.get(k);
            message.setGroupId(group.getGroupId());
        }


        // 1.插入节点
        messages.forEach(message -> neo4jUtil.createMessageNode(message));
        groups.forEach(group -> neo4jUtil.createGroupNode(group));
        persons.forEach(person -> neo4jUtil.createPersonNode(person));

        // 2.插入关系
        Set<Integer> set = new HashSet<>();
        for (int i = 1; i <= 1000; i++) {
            Message msg = messages.get(i - 1);
            neo4jUtil.createSentRelationship(msg.getWxId(), msg);
            int j = (i + 9) / 10 - 1;
            Person person = persons.get(j);
            Message message = messages.get(i - 1);
            int k = (i + 99) / 100 - 1;
            if (!set.contains(j * 10000 + k)) {
                neo4jUtil.createMemberRelationship(person.getWxId(), message.getGroupId());
                set.add(j * 10000 + k);
            }
        }
    }



    static boolean randomBool() {
        return randomInt(0, 1) > 0;
    }
    static <T> T randomT(List<T> list) {
        return list.get(randomInt(0, list.size() - 1));
    }

    static Set<Long> randomIds(int max, int count) {
        Set<Long> set = new HashSet<>();
        for (int i = 1; i <= count; i++) {
            long j = -1;
            while (set.contains(j))
                j = randomLong(1, max);
            set.add(j);
        }
        return set;
    }

    static String randomUrl() {
        return "https://" + randomS(100) + ".com";
    }

    static Date now() {
        return new Date();
    }

    static Random random = new Random();

    private static int randomInt(int L, int R) {
        return L + random.nextInt(R - L + 1);
    }

    private static long randomLong(int L, int R) {
        if (R - L + 1 <= 0) {
            int x = 1;
        }
        return L + random.nextInt(R - L + 1);
    }

    private static String randomNumStr(int len) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < len; i++)
            res.append(random.nextInt(10));
        return res.toString();
    }

    private static String randomS(int len) {
        return RandomUtil.randomString(len);
    }

    private static String uid() {
        return IdUtil.randomUUID();
    }

    private static Date randomDate() { // 距现在1年内的随机时间
        Date now = new Date();
        int i = randomInt(1, 365 * 24);
        return DateUtil.offset(now, DateField.HOUR, -i);
    }

    private static String randomIp() {
        return "192.168." + randomInt(100, 199) + "." + randomInt(100, 199);
    }

    private static String json() {
        return "{ \"id\": \"1\"}";
    }

    private static Date randomRangeDate(Date start, Date end) {
        int difSec = (int) ((end.getTime() - start.getTime()) / 1000 / 3600);
        if (difSec < 0) {
            int x = 1;
        }
        return DateUtil.offset(end, DateField.HOUR, -randomInt(0, difSec));
    }

    private static int dayDif(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / 1000 / 3600 / 24);
    }


}
