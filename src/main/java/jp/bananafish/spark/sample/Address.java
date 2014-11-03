/**
 * 
 */
package jp.bananafish.spark.sample;

import java.io.Serializable;

/**
 * 郵便番号CSVのレコード
 * 
 * @author border
 *
 */
public class Address implements Serializable {

    /**
     * 全国地方公共団体コード（JIS X0401、X0402）
     */
    private String jisCode;

    /**
     * （旧）郵便番号（5桁）
     */
    private String oldPostalCode;

    /**
     * 郵便番号（7桁）
     */
    private String postalCode;

    /**
     * 都道府県名カナ
     */
    private String prefectureNameKana;

    /**
     * 市区町村名カナ
     */
    private String cityNameKana;

    /**
     * 町域名カナ
     */
    private String townAreaNameKana;

    /**
     * 都道府県名
     */
    private String prefectureName;

    /**
     * 市区町村名
     */
    private String cityName;

    /**
     * 町域名
     */
    private String townAreaName;

    /**
     * 一町域が二以上の郵便番号で表される場合の表示
     */
    private String flag1;

    /**
     * 小字毎に番地が起番されている町域の表示
     */
    private String flag2;

    /**
     * 丁目を有する町域の場合の表示
     */
    private String flag3;

    /**
     * 一つの郵便番号で二以上の町域を表す場合の表示
     */
    private String flag4;

    /**
     * 更新の表示
     */
    private String updateType;

    /**
     * 変更理由
     */
    private String reasonForChange;

    /**
     * @return the jisCode
     */
    public String getJisCode() {
        return jisCode;
    }

    /**
     * @param jisCode
     *            the jisCode to set
     */
    public void setJisCode(String jisCode) {
        this.jisCode = jisCode;
    }

    /**
     * @return the oldPostalCode
     */
    public String getOldPostalCode() {
        return oldPostalCode;
    }

    /**
     * @param oldPostalCode
     *            the oldPostalCode to set
     */
    public void setOldPostalCode(String oldPostalCode) {
        this.oldPostalCode = oldPostalCode;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode
     *            the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the prefectureNameKana
     */
    public String getPrefectureNameKana() {
        return prefectureNameKana;
    }

    /**
     * @param prefectureNameKana
     *            the prefectureNameKana to set
     */
    public void setPrefectureNameKana(String prefectureNameKana) {
        this.prefectureNameKana = prefectureNameKana;
    }

    /**
     * @return the cityNameKana
     */
    public String getCityNameKana() {
        return cityNameKana;
    }

    /**
     * @param cityNameKana
     *            the cityNameKana to set
     */
    public void setCityNameKana(String cityNameKana) {
        this.cityNameKana = cityNameKana;
    }

    /**
     * @return the townAreaNameKana
     */
    public String getTownAreaNameKana() {
        return townAreaNameKana;
    }

    /**
     * @param townAreaNameKana
     *            the townAreaNameKana to set
     */
    public void setTownAreaNameKana(String townAreaNameKana) {
        this.townAreaNameKana = townAreaNameKana;
    }

    /**
     * @return the prefectureName
     */
    public String getPrefectureName() {
        return prefectureName;
    }

    /**
     * @param prefectureName
     *            the prefectureName to set
     */
    public void setPrefectureName(String prefectureName) {
        this.prefectureName = prefectureName;
    }

    /**
     * @return the cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * @param cityName
     *            the cityName to set
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * @return the townAreaName
     */
    public String getTownAreaName() {
        return townAreaName;
    }

    /**
     * @param townAreaName
     *            the townAreaName to set
     */
    public void setTownAreaName(String townAreaName) {
        this.townAreaName = townAreaName;
    }

    /**
     * @return the flag1
     */
    public String getFlag1() {
        return flag1;
    }

    /**
     * @param flag1
     *            the flag1 to set
     */
    public void setFlag1(String flag1) {
        this.flag1 = flag1;
    }

    /**
     * @return the flag2
     */
    public String getFlag2() {
        return flag2;
    }

    /**
     * @param flag2
     *            the flag2 to set
     */
    public void setFlag2(String flag2) {
        this.flag2 = flag2;
    }

    /**
     * @return the flag3
     */
    public String getFlag3() {
        return flag3;
    }

    /**
     * @param flag3
     *            the flag3 to set
     */
    public void setFlag3(String flag3) {
        this.flag3 = flag3;
    }

    /**
     * @return the flag4
     */
    public String getFlag4() {
        return flag4;
    }

    /**
     * @param flag4
     *            the flag4 to set
     */
    public void setFlag4(String flag4) {
        this.flag4 = flag4;
    }

    /**
     * @return the updateType
     */
    public String getUpdateType() {
        return updateType;
    }

    /**
     * @param updateType
     *            the updateType to set
     */
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    /**
     * @return the reasonForChange
     */
    public String getReasonForChange() {
        return reasonForChange;
    }

    /**
     * @param reasonForChange
     *            the reasonForChange to set
     */
    public void setReasonForChange(String reasonForChange) {
        this.reasonForChange = reasonForChange;
    }
}
