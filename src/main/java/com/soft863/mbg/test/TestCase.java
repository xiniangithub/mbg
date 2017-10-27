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
	 * 运行逆向工程配置文件
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
	 * 测试Mybatis3Simple逆向工程生成的功能
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
	 * 测试Mybatis3逆向工程生成的功能
	 */
	@Test
	public void testMybatis3() {
		SqlSession session = ssf.openSession();
		GoodsMapper goodsDao = session.getMapper(GoodsMapper.class);
		/*
		 * selectByExample()：查询多条记录，参数为null时，查询所有记录，指定参数时，参数是查询的条件
		 */
		// 查询所有记录
		List<Goods> list = goodsDao.selectByExample(null);
		for (Goods goods : list) {
			System.out.println(goods.getGoodsid());
		}
		System.out.println("----------------------");
		
		// 根据条件查询记录
		GoodsExample example = new GoodsExample(); // XxxExample类封装的查询条件
		Criteria criteria = example.createCriteria(); // Criteria中封装了一系列的条件拼接方法
		criteria.andGoodstotalBetween(0, 100); // 商品数量在0到100之间的商品
		
		/*
		 * 多个where条件的关系是and时，调用and开头的方法设置条件即可；
		 * 如果是 or 的关系，则要新创建一个Criteria对象，设置条件，然后使用example.or(Criteria对象)
		 * 方法就可以拼接上 or 的条件
		 */
		Criteria criteria2 = example.createCriteria();
		criteria2.andGoodstotalEqualTo(200);
		example.or(criteria2);
		
		List<Goods> list2 = goodsDao.selectByExample(example); // 查询结果
		for (Goods goods : list2) {
			System.out.println(goods.getGoodsid());
		}
		session.close();
	}
}
