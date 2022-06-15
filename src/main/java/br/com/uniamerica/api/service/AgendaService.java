package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.*;
import br.com.uniamerica.api.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public Agenda findById(Long id){
        return this.agendaRepository.findById(id).orElse(new Agenda());
    }

    public Page<Agenda> listAll(Pageable pageable){
        return this.agendaRepository.findAll(pageable);
    }

    @Transactional
    public void saveTransaction(final Agenda agenda){
        this.agendaRepository.save(agenda);
    }

    public void insert(final Agenda agenda){

        if(!ObjectUtils.isEmpty(agenda.getSecretaria())){

            agenda.setStatus(StatusAgenda.aprovado);

            if (!agenda.getEncaixe()) {
                Assert.isTrue(this.agendaRepository.isDataDisponivel(
                        agenda,
                        agenda.getDataDe(),
                        agenda.getDataAte(),
                        agenda.getMedico(),
                        agenda.getPaciente()
                ).size() == 0, "Error: Gerou um conflito entre horários, sugira outro horário.");
            }
        }
        else {

            agenda.setStatus(StatusAgenda.pendente);
            agenda.setEncaixe(false);

            Assert.isTrue(this.agendaRepository.isDataDisponivel(
                    agenda,
                    agenda.getDataDe(),
                    agenda.getDataAte(),
                    agenda.getMedico(),
                    agenda.getPaciente()
            ).size() == 0, "Error: Gerou um conflito entre horários, sugira outro horário.");
        }

        this.saveTransaction(agenda);
    }

    public void update(final Long idAgenda, final Agenda agenda){

        this.realizarValidacoes(agenda);

        if (!agenda.getEncaixe()) {
            Assert.isTrue(this.agendaRepository.isDataDisponivel(
                    agenda,
                    agenda.getDataDe(),
                    agenda.getDataAte(),
                    agenda.getMedico(),
                    agenda.getPaciente()
            ).size() == 0, "Error: Gerou um conflito entre horários, sugira outro horário.");
        }

        this.saveTransaction(agenda);
    }

    public void realizarValidacoes(final Agenda agenda){

        Assert.isTrue(this.isDataMenorQueAtual(agenda.getDataAte()),
                "Error: Data Até é menor que a data atual.");
        Assert.isTrue(this.isDataMenorQueAtual(agenda.getDataDe()),
                "Error: Data De é menor que a data atual.");

        Assert.isTrue(this.isDataAteMaiorQueDataDe(agenda.getDataDe(), agenda.getDataAte()),
                "Error: Data Até é maior que a Data De.");

        Assert.isTrue(this.isOnHorarioDeAtendimento(agenda.getDataAte()),
                "Error: A Data De está fora do horário comercial. (8 as 12 e 14 as 18)");
        Assert.isTrue(this.isOnHorarioDeAtendimento(agenda.getDataDe()),
                "Error: A Data Até está fora do horário comercial. (8 as 12 e 14 as 18)");

        Assert.isTrue(this.isFimDeSemana(agenda.getDataDe()),
                "Error: A Data De informada será em um final de semana.");
        Assert.isTrue(this.isFimDeSemana(agenda.getDataAte()),
                "Error: A Data Até informada será em um final de semana.");
    }


    public boolean isDataMenorQueAtual(final LocalDateTime localDateTime){
        return  localDateTime.isAfter(LocalDateTime.now());
    }

    public boolean isDataAteMaiorQueDataDe(final LocalDateTime localDateTimeDe, final LocalDateTime localDateTimeAte){
        return localDateTimeAte.compareTo(localDateTimeDe) >= 0;
    }

    private boolean isOnHorarioDeAtendimento(final LocalDateTime localDateTime){
        return (localDateTime.getHour() >= 8  && localDateTime.getHour() < 12)
                || (localDateTime.getHour() >= 14 && localDateTime.getHour() < 18) ? true : false;
    }

    public boolean isFimDeSemana(LocalDateTime localDateTimeDe){
        return  localDateTimeDe.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                ||
                localDateTimeDe.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    @Transactional
    public void desativar(final Long id, final Agenda agenda) {
        if (id == agenda.getId()) {
            this.agendaRepository.desativar(agenda.getId());
        } else {
            throw new RuntimeException("Error: Informações inconsistente, tente novamento mais tarde;");
        }
    }

    public void updateStatusAgendaAprovado(Agenda agenda, Secretaria secretaria) {
        if(agenda.getStatus().equals(StatusAgenda.pendente) && secretaria != null){
            updateStatusAgenda(agenda, StatusAgenda.aprovado);
        }
    }

    public void updateStatusAgendaRejeitado(Agenda agenda, Secretaria secretaria) {
        if(agenda.getStatus().equals(StatusAgenda.pendente) && secretaria != null){
            updateStatusAgenda(agenda, StatusAgenda.rejeitado);
        }
    }

    public void updateStatusAgendaCancelado(Agenda agenda, Secretaria secretaria) {
        if((agenda.getStatus().equals(StatusAgenda.aprovado)) || (agenda.getStatus().equals(StatusAgenda.pendente))
            &&
            secretaria != null || agenda.getPaciente() != null){
                updateStatusAgenda(agenda, StatusAgenda.cancelado);
        }
    }

    public void updateStatusAgendaCompareceu(Agenda agenda, Secretaria secretaria) {
        if(agenda.getStatus().equals(StatusAgenda.aprovado) &&
           secretaria != null &&
           isDataMenorQueAtual(agenda.getDataDe()) == false &&
           isDataMenorQueAtual(agenda.getDataAte()) == false){
                updateStatusAgenda(agenda, StatusAgenda.compareceu);
        }
    }

    public void updateStatusAgendaNaoCompareceu(Agenda agenda, Secretaria secretaria) {
        if(agenda.getStatus().equals(StatusAgenda.aprovado) &&
           secretaria != null &&
           isDataMenorQueAtual(agenda.getDataDe()) == false &&
           isDataMenorQueAtual(agenda.getDataAte()) == false){
                updateStatusAgenda(agenda, StatusAgenda.nao_compareceu);
        }
    }

    @Transactional
    public void updateStatusAgenda(Agenda agenda, StatusAgenda statusAgenda){
        agenda.setStatus(statusAgenda);
        agendaRepository.save(agenda);
    }

}
