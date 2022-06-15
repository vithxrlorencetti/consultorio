package br.com.uniamerica.api.service;

import br.com.uniamerica.api.entity.Convenio;
import br.com.uniamerica.api.entity.Especialidade;
import br.com.uniamerica.api.repository.ConvenioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ConvenioService {

    @Autowired
    private ConvenioRepository convenioRepository;

    public Convenio findById(Long id){
        return this.convenioRepository.findById(id).orElse(new Convenio());
    }

    public Page<Convenio> listAll(Pageable pageable){
        return this.convenioRepository.findAll(pageable);
    }

    @Transactional
    public void insert(Convenio convenio){
        this.convenioRepository.save(convenio);
    }

    @Transactional
    public void update(Long id, Convenio convenio){
        if (id == convenio.getId()){
            this.convenioRepository.save(convenio);
        } else {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void desativar(Long id, Convenio convenio){
        if (id == convenio.getId()) {
            this.convenioRepository.desativar(convenio.getId());
        }
        else {
            throw new RuntimeException();
        }
    }
}
