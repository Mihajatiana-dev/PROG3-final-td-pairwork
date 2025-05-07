package hei.school.prog3.service;

import hei.school.prog3.dao.operations.TransfertDAO;
import hei.school.prog3.model.Transfert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransfertService {

    private final TransfertDAO transfertDAO;

    public Transfert saveTransfert(String playerId, String clubId, String status) {
        Transfert transfert = new Transfert();
        transfert.setPlayerId(playerId);
        transfert.setClubId(clubId);
        transfert.setStatus(status);
        transfert.setTransfertDate(LocalDateTime.now());
        return transfertDAO.save(transfert);
    }
}