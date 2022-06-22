package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Historico;
import br.com.uniamerica.api.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/api/historicos")
public class HistoricoController {

    @Autowired
    public HistoricoRepository historicoRepository;

    @GetMapping
    public ResponseEntity<List<Historico>> listAllHistorico(){
        return new ResponseEntity<>(historicoRepository.findAll(), HttpStatus.OK) ;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Historico historico){
        historicoRepository.save(historico);
        return new ResponseEntity<>("Registro Cadastrado", HttpStatus.OK);
    }
}
