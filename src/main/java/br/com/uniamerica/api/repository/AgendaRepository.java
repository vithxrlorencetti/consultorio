package br.com.uniamerica.api.repository;

import br.com.uniamerica.api.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author Eduardo Sganderla
 *
 * @since 1.0.0, 31/03/2022
 * @version 1.0.0
 */
@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    @Modifying
    @Query("UPDATE Agenda agenda " +
            "SET agenda.excluido = now() " +
            "WHERE agenda.id = :agenda")
    public void updateStatusRemovido(@Param("agenda") Long idAgenda);

    @Query("SELECT Agenda agenda " +
            "WHERE agenda.dataDe >= :dataDe AND agenda.dataAte <= :dataAte")
    public boolean isDataDisponivel(@Param("dataDe") LocalDateTime dataDe, @Param("dataAte") LocalDateTime dataAte);

}
