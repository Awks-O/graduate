package cn.core.utils;

import cn.core.beans.MedicineDO;
import cn.core.daos.MedicineDao;

public class MedicineUtil {

    private static MedicineDao medicineDao;

    public static void setMedicineDao(MedicineDao medicineDao) {
        MedicineUtil.medicineDao = medicineDao;
    }

    public static String get(String name) {
        MedicineDO config = medicineDao.findByMedicineName(name);

        assert (config != null);
        return config.getMedicineName();
    }

    public static String get(String name, String defaultValue) {
        MedicineDO config = medicineDao.findByMedicineName(name);
        return config != null ? config.getMedicineName() : defaultValue;
    }

    public static String getInt(String name) {
        throw new UnsupportedOperationException("等你实现");
    }
}
