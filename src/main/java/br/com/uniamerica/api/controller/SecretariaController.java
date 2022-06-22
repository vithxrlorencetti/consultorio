package br.com.uniamerica.api.controller;

import br.com.uniamerica.api.entity.Secretaria;
import br.com.uniamerica.api.service.SecretariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/secretarias")
public class SecretariaController {

    @Autowired
    private SecretariaService secretariaService;

    @GetMapping("/{idSecretaria}")
    public ResponseEntity<Secretaria> findById(@PathVariable("idSecretaria") Long idSecretaria) {
        return ResponseEntity.ok().body(this.secretariaService.findById(idSecretaria));
    }

    @GetMapping
    public ResponseEntity<Page<Secretaria>> listByAllPage(Pageable pageable) {
        return ResponseEntity.ok().body(this.secretariaService.listAll(pageable));
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody Secretaria secretaria) {
        try {
            this.secretariaService.insert(secretaria);
            return ResponseEntity.ok().body("Secretaria Cadastrada com Sucesso.");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{idSecretaria}")
    public ResponseEntity<?> update(@PathVariable Long idSecretaria, @RequestBody Secretaria secretaria) {
        try {
            this.secretariaService.update(idSecretaria, secretaria);
            return ResponseEntity.ok().body("Secretaria Atualizada com Sucesso.");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/desativar/{idEspecialidade}")
    public ResponseEntity<?> desativar(@PathVariable Long idSecretaria, @RequestBody Secretaria secretaria) {
        try {
            this.secretariaService.desativar(idSecretaria, secretaria);
            return ResponseEntity.ok().body("Secretaria Desativada com Sucesso.");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
