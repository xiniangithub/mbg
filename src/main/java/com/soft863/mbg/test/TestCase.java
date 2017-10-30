package com.soft863.mbg.test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.soft863.mbg.dao.GoodsMapper;
import com.soft863.mbg.entity.Goods;
import com.soft863.mbg.entity.GoodsExample;
import com.soft863.mbg.entity.GoodsExample.Criteria;

public class TestCase {

	/**
	 * �������򹤳������ļ�
	 * @throws Exception
	 */
	@Test
	public void testMbg() throws Exception {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File("mbg.xml");
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}
	
	private static SqlSessionFactory ssf = null;
	
	@BeforeClass
	public static void beforeClass() {
		InputStream is = TestCase.class.getResourceAsStream("/mybatis-config.xml");
		ssf = new SqlSessionFactoryBuilder().build(is);
	}
	
	/**
	 * ����Mybatis3Simple���򹤳����ɵĹ���
	 */
	/*@Test
	public void testMybatis3Simple() {
		SqlSession session = ssf.openSession();
		GoodsMapper goodsDao = session.getMapper(GoodsMapper.class);
		List<Goods> list = goodsDao.selectAll();
		for (Goods goods : list) {
			System.out.println(goods.getGoodsid());
		}
		session.close();
	}*/
	
	/**
	 * ����Mybatis3���򹤳����ɵĹ���
	 */
	@Test
	public void testMybatis3() {
		SqlSession session = ssf.openSession();
		GoodsMapper goodsDao = session.getMapper(GoodsMapper.class);
		/*
		 * selectByExample()����ѯ������¼������Ϊnullʱ����ѯ���м�¼��ָ������ʱ�������ǲ�ѯ������
		 */
		// ��ѯ���м�¼
		List<Goods> list = goodsDao.selectByExample(null);
		for (Goods goods : list) {
			System.out.println(goods.getGoodsid());
		}
		System.out.println("----------------------");
		
		// ����������ѯ��¼
		GoodsExample example = new GoodsExample(); // XxxExample���װ�Ĳ�ѯ����
		Criteria criteria = example.createCriteria(); // Criteria�з�װ��һϵ�е�����ƴ�ӷ���
		criteria.andGoodstotalBetween(0, 100); // ��Ʒ������0��100֮�����Ʒ
		
		/*
		 * ���where�����Ĺ�ϵ��andʱ������and��ͷ�ķ��������������ɣ�
		 * ����� or �Ĺ�ϵ����Ҫ�´���һ��Criteria��������������Ȼ��ʹ��example.or(Criteria����)
		 * �����Ϳ���ƴ���� or ������
		 */
		Criteria criteria2 = example.createCriteria();
		criteria2.andGoodstotalEqualTo(200);
		example.or(criteria2);
		
		List<Goods> list2 = goodsDao.selectByExample(example); // ��ѯ���
		for (Goods goods : list2) {
			System.out.println(goods.getGoodsid());
		}
		session.close();
	}
}
