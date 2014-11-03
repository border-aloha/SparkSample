/**
 * 
 */
package jp.bananafish.spark.sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.http.HttpStatus;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;

/**
 * @author border
 *
 */
public class PostalCodeCsv {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String appName = "PostalCode Analyzer"; // アプリケーション名。別に何でもいい
        String master = "local"; // 分散しないでローカルで動かすための指定
        // スタート
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
        JavaSparkContext sc = new JavaSparkContext(conf);

        // 入力ファイル準備
        File inputFile = setupInputFile();
        JavaRDD<Address> rddRecords = sc.textFile(inputFile.getAbsolutePath())
                .map(new Function<String, Address>() {

                    public Address call(String line) throws Exception {
                        // CSVをばらしてフィールドにセット。手抜きです。
                        String[] fields = line.split(",");
                        Address address = new Address();
                        address.setPostalCode(fields[2]); // 郵便番号
                        address.setPrefectureName(fields[6]); // 都道府県名
                        return address;
                    }

                }).cache();
        // クエリ準備
        JavaSQLContext sqlContext = new JavaSQLContext(sc);
        JavaSchemaRDD table = sqlContext.applySchema(rddRecords, Address.class);
        table.registerAsTable("address_table");
        table.printSchema();
        // 後始末
        try {
            FileUtils.forceDeleteOnExit(inputFile);
        } catch (IOException e) {
            new RuntimeException(e);
        }

    }

    /**
     * 入力ファイルの準備
     */
    private static File setupInputFile() {
        String destinationPath = SystemUtils.getJavaIoTmpDir()
                .getAbsolutePath();
        File file = null;
        // 日本郵便が公開している全国郵便番号データCSVファイルを持ってくる
        HttpClient httpClient = new HttpClient();
        HttpMethod httpMethod = new GetMethod(
                "http://www.post.japanpost.jp/zipcode/dl/kogaki/zip/ken_all.zip");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // データ取得
            int statusCode = httpClient.executeMethod(httpMethod);
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("");
            }
            inputStream = new BufferedInputStream(
                    httpMethod.getResponseBodyAsStream());

            // ZIPファイル保存
            file = new File(destinationPath, "ken_all.zip");
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            IOUtils.copyLarge(inputStream, outputStream);
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);

            // ZIPを展開
            ZipFile zipFile = new ZipFile(file);
            zipFile.extractAll(destinationPath);
            FileUtils.forceDeleteOnExit(file);
            file = new File(destinationPath, "ken_all.csv");
            if (!file.exists()) {
                throw new IOException("Received file not found");
            }
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ZipException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return file;
    }
}
