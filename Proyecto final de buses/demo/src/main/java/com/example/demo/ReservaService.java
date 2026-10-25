
package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public Reserva guardar(Reserva reserva) {
        if (reserva.getUsuario() == null) {
            throw new IllegalArgumentException(
                    "La reserva debe estar asociada a un usuario.");
        }

        return reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return reservaRepository.buscarReservasPorUsuario(usuarioId);
    }
}
