package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Paciente;
import br.com.uniamerica.api.entity.Secretaria;
import br.com.uniamerica.api.entity.TipoAtendimento;
import br.com.uniamerica.api.repository.PacienteRepository;
import br.com.uniamerica.api.repository.SecretariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public Optional<Paciente> findById(Long id){
        return this.pacienteRepository.findById(id);
    }

    public Page<Paciente> listAll(Pageable pageable){
        return this.pacienteRepository.findAll(pageable);
    }

    public void insert(Paciente paciente){
        this.validarFormulario(paciente);
        this.saveTransactional(paciente);
    }

    public void update(Long id, Paciente paciente){
        if (id == paciente.getId()){
            this.validarFormulario(paciente);
            this.saveTransactional(paciente);
        } else {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void updateStatus(Long id, Paciente paciente){
        if (id == paciente.getId()) {
            this.pacienteRepository.updateStatus(paciente.getId());
        }
        else {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void saveTransactional(Paciente paciente){
        this.pacienteRepository.save(paciente);
    }

    public void validarFormulario(Paciente paciente){

        if(paciente.getTipoAtendimento().equals(TipoAtendimento.convenio)){
            if (paciente.getConvenio() == null || paciente.getConvenio().getId() == null){
                throw new RuntimeException("Convenio não informado.");
            }
            if (paciente.getNumeroCartaoConvenio() == null){
                throw new RuntimeException("Numero do Cartão do Convenio não informado.");
            }
            if (paciente.getDataVencimento() == null){
                throw new RuntimeException("Data de Vencimento do Cartão não informada.");
            }
            if (paciente.getDataVencimento().compareTo(LocalDateTime.now()) > 0){
                throw new RuntimeException("Data de Vencimento do Cartão expirada.");
            }
        }

        if (paciente.getTipoAtendimento().equals(TipoAtendimento.particular)){
            paciente.setConvenio(null);
            paciente.setNumeroCartaoConvenio(null);
            paciente.setDataVencimento(null);
        }

    }

}
