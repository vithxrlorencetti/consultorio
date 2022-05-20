package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Agenda;
import br.com.uniamerica.api.entity.StatusAgenda;
import br.com.uniamerica.api.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public Optional<Agenda> findById(Long id){
        return this.agendaRepository.findById(id);
    }

    public Page<Agenda> listAll(Pageable pageable){
        return this.agendaRepository.findAll(pageable);
    }

    @Transactional
    public void updateStatus(Long id, Agenda agenda) {
        if (id == agenda.getId()) {
            this.agendaRepository.updateStatus(agenda.getId());
        } else {
            throw new RuntimeException();
        }
    }

    public void updateStatusAgendaAprovado(Agenda agenda) {
        if(agenda.getStatus().equals(StatusAgenda.pendente)){
            updateStatusAgenda(agenda, StatusAgenda.aprovado);
        }
    }

    public void updateStatusAgendaRejeitado(Agenda agenda) {
        if(agenda.getStatus().equals(StatusAgenda.pendente)){
            updateStatusAgenda(agenda, StatusAgenda.rejeitado);
        }
    }

    public void updateStatusAgendaCancelado(Agenda agenda) {
        if((agenda.getStatus().equals(StatusAgenda.aprovado)) || (agenda.getStatus().equals(StatusAgenda.pendente))){
            updateStatusAgenda(agenda, StatusAgenda.cancelado);
        }
    }

    public void updateStatusAgendaCompareceu(Agenda agenda) {
        if(agenda.getStatus().equals(StatusAgenda.aprovado)){
            updateStatusAgenda(agenda, StatusAgenda.compareceu);
        }
    }

    public void updateStatusAgendaNaoCompareceu(Agenda agenda) {
        if(agenda.getStatus().equals(StatusAgenda.aprovado)){
            updateStatusAgenda(agenda, StatusAgenda.nao_compareceu);
        }
    }



    @Transactional
    public void updateStatusAgenda(Agenda agenda, StatusAgenda statusAgenda){
        agenda.setStatus(statusAgenda);
        agendaRepository.save(agenda);
    }

    public void validaDisponibilidade(){

    }

    public void validaData(LocalDateTime localDateTimeDe, LocalDateTime localDateTimeAte){
        if(((localDateTimeDe.compareTo(LocalDateTime.now())) && (localDateTimeAte.compareTo(LocalDateTime.now()))) <= 0) {
            throw new RuntimeException("Data informada menor que a atual");
        }
    }

    public void validaEncaixe(Agenda agenda){
        if(agenda.getEncaixe() == false) {
            validaDisponibilidade();
        }
    }



}
