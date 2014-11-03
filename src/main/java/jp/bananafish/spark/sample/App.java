package jp.bananafish.spark.sample;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;

/**
 * Apache Sparkをとりあえず動かしてみただけ
 */
public class App {
	public static void main(String[] args) {
		String appName = "sample"; // アプリケーション名。別に何でもいい
		String master = "local"; // 分散しないでローカルで動かすための指定
		// スタート
		SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
		JavaSparkContext sc = new JavaSparkContext(conf);
		// 試しにpom.xmlを処理してみる
		JavaRDD<String> textFile = sc.textFile("pom.xml", 2).cache();
		// ワードカウントしただけ
		long count = textFile.count();
		System.out.println("Word Count:" + count);
		// 同じようなものだが、半角スペースで分割してみる
		JavaRDD<String> mapped = textFile
				.flatMap(new FlatMapFunction<String, String>() {

					public Iterable<String> call(String t) throws Exception {
						return Arrays.asList(t.split(" "));
					}
				});
		// 分割された単語の中で、文字列長が最大なものを引っ張り出してみる
		String result = mapped.reduce(new Function2<String, String, String>() {

			public String call(String v1, String v2) throws Exception {
				if (v1.length() > v2.length()) {
					return v1;
				} else {
					return v2;
				}
			}
		});
		System.out.println(result);
	}
}
