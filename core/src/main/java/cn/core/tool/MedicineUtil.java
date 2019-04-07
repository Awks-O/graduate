package cn.core.tool;

import cn.core.daos.MedicineDao;
import cn.core.beans.Medicine;

import static cn.core.common.utils.CheckUtil.check;

public class MedicineUtil {

    private static MedicineDao medicineDao;

    public static void setMedicineDao(MedicineDao medicineDao) {
        MedicineUtil.medicineDao = medicineDao;
    }

    public static String get(String name) {
        Medicine config = medicineDao.findByMedicineName(name);

        assert (config != null);
        return config.getMedicineName();
    }

    public static String get(String name, String defaultValue) {
        Medicine config = medicineDao.findByMedicineName(name);
        return config != null ? config.getMedicineName() : defaultValue;
    }

    public static String getInt(String name) {
        throw new UnsupportedOperationException("等你实现");
    }
}
