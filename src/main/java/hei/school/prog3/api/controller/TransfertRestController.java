package hei.school.prog3.api.controller;

import hei.school.prog3.model.Transfert;
import hei.school.prog3.service.TransfertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransfertRestController {
    private final TransfertService transfertService;

    @GetMapping("/transfert")
    public ResponseEntity<List<Transfert>> getAllTransferts() {
        List<Transfert> transferts = transfertService.getAllTransferts();
        return transferts.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(transferts);
    }
}