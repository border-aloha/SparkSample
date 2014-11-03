/**
 * 
 */
package jp.bananafish.spark.sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

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
import org.apache.spark.sql.api.java.Row;

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
                        address.setJisCode(fields[0]); // JISコード
                        address.setPostalCode(fields[2]); // 郵便番号
                        address.setPrefectureName(fields[6]); // 都道府県名
                        return address;
                    }

                }).cache();
        // クエリ準備
        JavaSQLContext sqlContext = new JavaSQLContext(sc);
        JavaSchemaRDD table = sqlContext.applySchema(rddRecords, Address.class);
        table.registerTempTable("address_table");
        table.printSchema();
        JavaSchemaRDD res = sqlContext.sql(//
                "select prefectureName,count(prefectureName)" //
                        + "from address_table " //
                        + "group by prefectureName " //
                        + "order by prefectureName " //
                );
        // 後始末
        List<Row> list = res.collect();
        for (Row row : list) {
            int i = 0;
            String prefectureName = row.getString(i++);
            long count = row.getLong(i++);
            System.out.println(String.format("%s : %d", prefectureName, count));
        }
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
        Reader reader = null;
        Writer writer = null;
        try {
            // データ取得
            int statusCode = httpClient.executeMethod(httpMethod);
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("");
            }
            InputStream inputStream = new BufferedInputStream(
                    httpMethod.getResponseBodyAsStream());

            // ZIPファイル保存
            file = new File(destinationPath, "ken_all.zip");
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
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

            // ファイルのエンコーディングをUTF-8に変換
            file = new File(destinationPath, "ken_all.csv");
            FileUtils.forceDeleteOnExit(file);
            inputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inputStream, "MS932"));
            file = new File(destinationPath, "ken_all_utf8.csv");
            outputStream = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
            IOUtils.copyLarge(reader, writer);
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ZipException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
        return file;
    }
}
