package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Medico;
import br.com.uniamerica.api.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public Optional<Medico> findById(Long id){
        return this.medicoRepository.findById(id);
    }

    public Page<Medico> listAll(Pageable pageable){
        return this.medicoRepository.findAll(pageable);
    }

    @Transactional
    public void update(Long id, Medico medico){
        if (id == medico.getId()) {
            this.medicoRepository.save(medico);
        }
        else {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void insert(Medico medico){
        this.medicoRepository.save(medico);
    }

    @Transactional
    public void updateDataExcluido(Long id, Medico medico){
        if (id == medico.getId()) {
            this.medicoRepository.updateStatus(medico.getId());
        }
        else {
            throw new RuntimeException();
        }
    }
}
