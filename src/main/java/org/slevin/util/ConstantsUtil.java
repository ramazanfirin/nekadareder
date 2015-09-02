package org.slevin.util;

public class ConstantsUtil {
final public static String ilceBasedQuery = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=__category__&address_town=__ilceId__&sorting=__sorting__&a1966=__tapuDurumu__&language=tr&pagingOffset=__parameterPaging__&pagingSize=__parameterPagingSize__";
final public static String sehirBasedQuery ="https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=__category__&address_country=1&address_city=__sehirId__&sorting=__sorting__&a1966=__tapuDurumu__&language=tr&pagingOffset=__parameterPaging__&pagingSize=__parameterPagingSize__";

final public static String lastDaySehirBasedTapuluQuery = "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_city=__sehirId__&date=1day&sorting=__sorting__&a1966=__tapuDurumu__&language=tr&pagingOffset=__parameterPaging__&pagingSize=__parameterPagingSize__";
final public static String lastDayIlceBasedTapuluQuery =  "https://api.sahibinden.com/sahibinden-ral/rest/classifieds/search?category=16633&address_town=__ilceId__&date=1day&sorting=__sorting__&a1966=__tapuDurumu__&language=tr&pagingOffset=__parameterPaging__&pagingSize=__parameterPagingSize__";

final public static String CATEGORY_DAIRE="16633";
final public static String CATEGORY_RESIDANS="16633";
final public static String CATEGORY_MUSTAKIL_EV="1663";
final public static String CATEGORY_VILLA="16633";
final public static String CATEGORY_CIFTLIK_EVI="16633";
final public static String CATEGORY_KOSK="16633";
final public static String CATEGORY_YALI="16633";
final public static String CATEGORY_YALI_DAIRESI="16633";
final public static String CATEGORY_YAZLIK="16633";
final public static String CATEGORY_PREFABRIK_EV="16633";
final public static String CATEGORY_KOOPERATIF="16633";

final public static String SORTING_ASC="date_asc";
final public static String SORTING_DESC="date_desc";
final public static String TAPU_DURUMU_EVET="true";
final public static String TAPU_DURUMU_HAYIR="False";

final public static Double EUR=3.2;
final public static Double USD=2.9;
}
