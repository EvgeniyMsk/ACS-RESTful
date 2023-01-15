package ou.acs.constants;

import java.util.HashMap;
import java.util.Map;

public class AccessConstants {
    private Map<String, String> dictionary;
    private static AccessConstants INSTANCE = getInstance();

    private static AccessConstants getInstance() {
        if (INSTANCE == null)
        {
            INSTANCE = new AccessConstants();
        }
        return INSTANCE;
    }

    private AccessConstants() {
        dictionary = new HashMap<>();
        dictionary.put("admin", "Администратор");
//        SKMK
        dictionary.put("skmk", "ОО СКМК ФСО России");
        dictionary.put("alexander_garden", "Здание хозяйственного блока (расположенное в Тайницком саду Московского Кремля)");
        dictionary.put("autodrom", "Автодром ФСО России");
        dictionary.put("bkd", "Большой Кремлевский дворец");
        dictionary.put("cavalry_escort", "Пункт дислокации Кавалерийского почетного эскорта ПП СКМК ФСО России");
        dictionary.put("gkd_guest", "Гостевая резиденция Президента Российской Федерации в Государственном Кремлевском дворце");
        dictionary.put("gkd_vip", "«Особая зона» Президента Российской Федерации в Государственном Кремлевском дворце");
        dictionary.put("kamenny_bridge_left", "Большой Каменный мост (левая сторона, подмостовые помещения)");
        dictionary.put("kamenny_bridge_right", "Большой Каменный мост (правая сторона, подмостовые помещения)");
        dictionary.put("kremlin", "Официальная резиденция Президента Российской Федерации – Московский Кремль");
        dictionary.put("kremlin_1", "1 корпус Московского Кремля");
        dictionary.put("kremlin_2", "2 корпус Московского Кремля");
        dictionary.put("kremlin_2a_arsenal", "Спортивный комплекс «Арсенал» (2А корпус Московского Кремля)");
        dictionary.put("kremlin_5", "5 корпус Московского Кремля");
        dictionary.put("kremlin_6", "6 корпус Московского Кремля");
        dictionary.put("kremlin_7", "7 корпус Московского Кремля");
        dictionary.put("kremlin_8", "8 корпус Московского Кремля");
        dictionary.put("kremlin_9", "9 корпус Московского Кремля");
        dictionary.put("kremlin_14", "Подземные технические помещения 14 корпуса Московского Кремля");
        dictionary.put("kremlin_24", "24 корпус Московского Кремля");
        dictionary.put("kupavna", "Военный лагерь «Купавна» ПП СКМК ФСО России");
        dictionary.put("kutafya", "Пункт пропуска Кутафьи башни Московского Кремля");
        dictionary.put("lenin", "Мавзолей В.И. Ленина");
        dictionary.put("manezh_9_11", "Административные здания ФСО России [г. Москва, ул. Манежная, д. 9 и 11]");
        dictionary.put("memorial", "Общенациональный мемориал воинской славы");
        dictionary.put("spasskaya", "Спасское бюро пропусков");
        dictionary.put("ustyinskiy_bridge", "Большой Устьинский мост (подмостовые помещения)");

//        sou-uoo-1
        dictionary.put("som-uoo-1", "1 отдел УОО СОМ ФСО России");
        dictionary.put("autobase_8", "Автобаза 8 отдела ТУ СХО ФСО России");
        dictionary.put("laundry", "Служебные помещения «Спецпрачечная»");
        dictionary.put("official_residence_government", "Официальная резиденция Правительства Российской Федерации");
        dictionary.put("reception_house_government", "Дом приемов Правительства Российской Федерации");
//        sou-uoo-2
        dictionary.put("som-uoo-2", "2 отдел УОО СОМ ФСО России");
        dictionary.put("cherkasky_13_4", "Комплекс зданий АП РФ на Старой площади (Черкасский пер., 13-4)");
        dictionary.put("ilinka_8_2_4", "Комплекс зданий АП РФ на Старой площади (ул. Ильинка, 8-2-4)");
        dictionary.put("ilinka_10_2_1", "Комплекс зданий АП РФ на Старой площади (ул. Ильинка, 10-2-1)");
        dictionary.put("ilinka_12_2_1_1", "Комплекс зданий АП РФ на Старой площади (ул. Ильинка, 12-2-1-1)");
        dictionary.put("ipatevsky_3_1", "Комплекс зданий АП РФ на Старой площади (Ипатьевский пер., 3-1)");
        dictionary.put("ipatevsky_4-10_1", "Комплекс зданий АП РФ на Старой площади (Ипатьевский пер., д. 4-10-1)");
        dictionary.put("ipatevsky_9", "Комплекс зданий АП РФ на Старой площади (Ипатьевский пер., 9)");
        dictionary.put("ipatevsky_9_1_1", "Комплекс зданий АП РФ на Старой площади (Ипатьевский пер., 9-1-1)");
        dictionary.put("nikitnikov_2_9_11_1", "Комплекс зданий АП РФ на Старой площади (Никитников пер., 2-9-11-1)");
        dictionary.put("nikolsky_3", "Комплекс зданий АП РФ на Старой площади (Никольский пер., 3)");
        dictionary.put("nikolsky_4_1_3", "Комплекс зданий АП РФ на Старой площади (Старая пл., 4-1-3)");
        dictionary.put("staraya_2_14_1", "Комплекс зданий АП РФ на Старой площади (Старая пл., 2-14-1)");
        dictionary.put("staraya_4_1", "Комплекс зданий АП РФ на Старой площади (Старая пл., 4-1)");
        dictionary.put("staraya_6_1", "Комплекс зданий АП РФ на Старой площади (Старая пл., 6-1)");
        dictionary.put("staraya_8_5_1", "Комплекс зданий АП РФ на Старой площади (Старая пл., 8-5-1)");
        dictionary.put("staraya_10_4_1", "Комплекс зданий АП РФ на Старой площади (Старая пл., 10-4-1)");
        dictionary.put("varvarka_7_11_14_1", "Комплекс зданий АП РФ на Старой площади (ул. Варварка, 7-11-14-1)");
        dictionary.put("varvarka_11_1_2", "Комплекс зданий АП РФ на Старой площади (ул. Варварка, 11-1-2)");
        dictionary.put("reception_president", "Приемная Президента Российской Федерации (ул. Ильинка, 23-1)");
        dictionary.put("ipatevsky_12", "Комплекс зданий АП РФ на Старой площади (Ипатьевский пер., 12/2/1)");

//        som-uoo-3
        dictionary.put("som-uoo-3", "3 отдел УОО СОМ ФСО России");
        dictionary.put("duma", "Комплекс зданий Государственной Думы Федерального Собрания Российской Федерации");
        dictionary.put("union_house", "«Дом Союзов»");
//        som-uoo-4
        dictionary.put("som-uoo-4", "4 отдел УОО СОМ ФСО России");
        dictionary.put("barvikha_2", "Государственная дача «Барвиха-2»");
        dictionary.put("barvikha_3", "Государственная дача «Барвиха-3»");
        dictionary.put("gorki_10_5", "Государственная дача «Горки-10/5»");
        dictionary.put("gorki_10_14", "Государственная дача «Горки 10/14»");
        dictionary.put("kalchuga", "Объект «Калчуга»");
        dictionary.put("moscow_river_1", "Государственная дача «Москва-река-1»");
        dictionary.put("moscow_river_5", "Государственная дача «Москва-река-5»");
        dictionary.put("sosnovka_4", "Государственный особняк «Сосновка-4»");
        dictionary.put("usovo", "Объект «База Усово»");
        dictionary.put("zareche", "Объект «Заречье»");
        dictionary.put("zubalovo", "Государственная дача «Зубалово»");

//        som-uoo-5
        dictionary.put("som-uoo-5", "5 отдел УОО СОМ ФСО России");
        dictionary.put("autobase_2", "Автобаза 2 отдела ТУ СХО ФСО России");
        dictionary.put("cbb", "Административное здание ЦББ УМО СХО ФСО России");
        dictionary.put("foodbase", "Продовольственная база УПОО СХО ФСО России");
        dictionary.put("kosigina_65", "Административные здания ФСО России");
        dictionary.put("mansion_8", "Государственный особняк № 8");
        dictionary.put("novoarbatsky_bridge", "Новоарбатский автодорожный мост (подмостовые помещения)");
        dictionary.put("sbk", "Объект «СБК» ");
        dictionary.put("terem", "Объект «Терем»");
        dictionary.put("volinskoe_1_3", "Государственный особняк «Волынское 1, 3»");
//        som-uoo-6
        dictionary.put("som-uoo-6", "6 отдел УОО СОМ ФСО России");
        dictionary.put("federation_council", "Комплекс зданий Совета Федерации Федерального Собрания Российской Федерации");
        dictionary.put("neglinnaya_23", "Административные здания ФНС России");
        dictionary.put("russia_ships", "Место базирования группы судов «Россия»");
//        som-uoo-8
        dictionary.put("som-uoo-8", "8 отдел УОО СОМ ФСО России");
        dictionary.put("210", "«Объект № 210»");
        dictionary.put("222", "«Объект № 222»");
        dictionary.put("abc", "");
        dictionary.put("authorized_representative", "Здание полномочных представителей Президента Российской Федерации");
        dictionary.put("cik", "Комплекс зданий Центральной избирательной комиссии Российской Федерации");
        dictionary.put("k-14", "«Объект К-14»");
        dictionary.put("k-16", "«Объект К-16»");
        dictionary.put("varvarka_5a_5b_5_10", "Административные здания ФСО России");
        dictionary.put("arsenal", "ФОК \"Арсенал\" ФСО России");
        dictionary.put("abts", "Государственный особняк \"АБЦ\"");
//        som-uoo-9
        dictionary.put("som-uoo-9", "9 отдел УОО СОМ ФСО России");
        dictionary.put("104", "«Объект № 104»");
        dictionary.put("206", "«Объект № 206»");
        dictionary.put("244", "«Объект № 244»");
        dictionary.put("246", "«Объект № 246»");
        dictionary.put("autobase_3", "Автобаза 3 отдела ТУ СХО ФСО России");
        dictionary.put("krasnaya_presnya_3", "Административное здание ФСО России");
        dictionary.put("rozhdestvenka_14_1", "Административное здание ФСО России");

//        Подразделения-заказчики
//        ИЭУ СИТО ФСО России
        dictionary.put("sito-ieu", "ИЭУ СИТО ФСО России");
//        ЦОИ «Энергия» ФСО России
        dictionary.put("energia", "ЦОИ «Энергия» ФСО России");
        dictionary.put("2-430", "ЦОИ «Энергия» ФСО России");
//        1 УО СБП ФСО России
        dictionary.put("sbp-uo-1", "1 УО СБП ФСО России");
        dictionary.put("moscow_river_4", "ГД «Москва-река-4»");
        dictionary.put("nikolina_gora", "Объект «Николина гора»");
//        2 УО СБП ФСО России
        dictionary.put("sbp-uo-2", "2 УО СБП ФСО России");
        dictionary.put("gorki_10_10", "Государственная дача «Горки-10/10»");
        dictionary.put("ogarevo_2", "Официальная загородная резиденция Президента Российской Федерации - государственная дача «Огарево-2»");
//        3 УО СБП ФСО России
        dictionary.put("sbp-uo-3", "3 УО СБП ФСО России");
        dictionary.put("gorki_9", "ГД «Горки-9»");
        dictionary.put("milovka", "Объект «Миловка»");
//        Академия ФСО России
        dictionary.put("academy", "Академия ФСО России");
//        ВИПС (филиал) Академии ФСО России
        dictionary.put("vips", "ВИПС (филиал) Академии ФСО России");
        dictionary.put("academy_voronezh_minskaya", "ВИПС (филиал) Академии ФСО России");
    }


    public static String getTitle(String title) {
        return INSTANCE.dictionary.get(title);
    }
}
