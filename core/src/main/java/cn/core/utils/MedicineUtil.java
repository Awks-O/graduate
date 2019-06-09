package cn.core.utils;

import cn.core.beans.MedicineDO;
import cn.core.daos.MedicinePageDao;

public class MedicineUtil {

    private static MedicinePageDao medicinePageDao;

    public static void setMedicinePageDao(MedicinePageDao medicinePageDao) {
        MedicineUtil.medicinePageDao = medicinePageDao;
    }

    public static String get(String name) {
        MedicineDO config = medicinePageDao.findByMedicineName(name);

        assert (config != null);
        return config.getMedicineName();
    }

    public static String get(String name, String defaultValue) {
        MedicineDO config = medicinePageDao.findByMedicineName(name);
        return config != null ? config.getMedicineName() : defaultValue;
    }

    public static String getInt(String name) {
        throw new UnsupportedOperationException("等你实现");
    }
}
