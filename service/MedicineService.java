package service;

import dao.MedicineDAO;
import model.Medicine;

import java.sql.SQLException;
import java.util.List;

public class MedicineService {

    private final MedicineDAO medicineDAO;

    public MedicineService() {
        this.medicineDAO = new MedicineDAO();
    }

    public List<Medicine> getAllMedicines() throws SQLException {
        return medicineDAO.getAllMedicines();
    }

    public List<Medicine> searchMedicines(String query, String category, String sortOption) throws SQLException {
        return medicineDAO.searchMedicines(query, category, sortOption);
    }

    public Medicine getMedicineById(int id) throws SQLException {
        return medicineDAO.findById(id);
    }

    public Medicine getMedicineByName(String name) throws SQLException {
        return medicineDAO.findByName(name);
    }
}
