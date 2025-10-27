package dao;

import dto.ElevatorLogDTO;

import java.util.List;

public interface ElevatorDAO {
    public List<ElevatorLogDTO> selectELVLog();
}
