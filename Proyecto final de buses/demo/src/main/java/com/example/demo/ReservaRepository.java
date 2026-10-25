
package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioIdOrderByFechaRegistroDesc(Long usuarioId);

    @Query("SELECT r FROM Reserva r WHERE r.usuario.id = :usuarioId ORDER BY r.fechaRegistro DESC")
    List<Reserva> buscarReservasPorUsuario(@Param("usuarioId") Long usuarioId);
}
